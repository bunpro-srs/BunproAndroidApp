package bunpro.jp.bunproapp.presentation.word;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.presentation.BaseFragment;
import bunpro.jp.bunproapp.utils.SettingEvent;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;


public class WordDetailFragment extends BaseFragment implements View.OnClickListener, WordDetailContract.View {
    private Context context;
    private WordDetailContract.Presenter wordDetailPresenter;

    Button btnBack, btnResetOrAdd;
    RecyclerView rvWords;

    StickAdapter mAdapter;

    private GrammarPoint selectedPoint;
    private Review review;

    private int type;

    public static WordDetailFragment newInstance() {
        return new WordDetailFragment();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_word_detail, container, false);

        context = getActivity();
        wordDetailPresenter = new WordDetailPresenter(this);
        selectedPoint = null;
        type = 0;

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wordDetailPresenter.stop();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnResetOrAdd = view.findViewById(R.id.btnResetOrAdd);
        btnResetOrAdd.setOnClickListener(this);

        rvWords = view.findViewById(R.id.rvWords);
        rvWords.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvWords.setLayoutManager(layoutManager);
        rvWords.setItemAnimator(new DefaultItemAnimator());

        initialize();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SettingEvent event) {
        mAdapter.notifyDataSetChanged();
    }

    private void initialize() {
        selectedPoint = wordDetailPresenter.getGrammarPoint(getArguments() != null ? getArguments().getInt("currentGrammarPointId") : -1);

        if (selectedPoint != null) {
            setActionLoading(false);

            wordDetailPresenter.updateReviewByGrammarPoint(selectedPoint.id);

            mAdapter = new StickAdapter(context, 0, review, selectedPoint, wordDetailPresenter.fetchExampleSentences(selectedPoint), wordDetailPresenter.fetchGrammarPointSupplementalLinks(selectedPoint), new StickAdapter.ItemClickListener() {
                @Override
                public void positionClicked(int position) {
                    if (position == 0) {
                        clickedDescription();
                    } else if (position == 1) {

                        Log.d("WordDetailFragment", "header clicked");

                    }
                }
            }, new StickAdapter.ItemChooseListener() {
                @Override
                public void chooseListener(int index) {
                    type = index;
                    mAdapter.updateType(index);
                    mAdapter.notifyDataSetChanged();
                }
            });

            rvWords.setAdapter(mAdapter);
        }
    }

    private boolean isReviewed(GrammarPoint point) {
        boolean status = false;
        List<Review> reviews = wordDetailPresenter.getReviews();
        if (reviews.size() > 0) {
            for (int k=0;k<reviews.size();k++) {
                if (reviews.get(k).complete && reviews.get(k).grammar_point_id == point.id) {
                    status = true;
                    break;
                }
            }
        }
        return status;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            popFragment();
        }

        if (id == R.id.btnResetOrAdd) {
            int tag = (int) btnResetOrAdd.getTag();
            if (tag == 1003) {
                wordDetailPresenter.addToReviews(selectedPoint.id);
            } else {
                resetOrRemove();
            }
        }
    }

    private void resetOrRemove() {

        View view = getLayoutInflater().inflate(R.layout.layout_reset_remove, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context);

        dialog.setContentView(view);
        dialog.show();

        RelativeLayout rlRemoveFromReviews = view.findViewById(R.id.rlRemoveFromReviews);
        rlRemoveFromReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordDetailPresenter.removeFromReviews(review.id);
                dialog.dismiss();
            }
        });

        RelativeLayout rlResetReviewProgress = view.findViewById(R.id.rlResetReviewProgress);
        rlResetReviewProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wordDetailPresenter.resetReviews(review.id);
                dialog.dismiss();
            }
        });

        RelativeLayout rlCancel = view.findViewById(R.id.rlCancel);
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void clickedDescription() {

        View view = getLayoutInflater().inflate(R.layout.layout_word_meaning, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);

        dialog.show();

        RelativeLayout rlCopyJapanese = view.findViewById(R.id.rlCopyJapanese);
        rlCopyJapanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipTextToBoard(selectedPoint.title);
                dialog.dismiss();
            }
        });

        RelativeLayout rlCopyMeaning = view.findViewById(R.id.rlCopyMeaning);
        rlCopyMeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipTextToBoard(selectedPoint.meaning);
                dialog.dismiss();
            }
        });

        RelativeLayout rlCancel = view.findViewById(R.id.rlCancel);
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void setActionLoading(boolean loading) {
        if (loading) {
            btnResetOrAdd.setText(R.string.loading_progressing);
            btnResetOrAdd.setTextColor(getResources().getColor(R.color.colorGrey));
            btnResetOrAdd.setEnabled(false);
        } else {
            if (!isReviewed(selectedPoint)) {
                btnResetOrAdd.setText(R.string.add_to_reviews);
                btnResetOrAdd.setTag(1003);
            } else {
                btnResetOrAdd.setText(R.string.reset_or_remove);
                btnResetOrAdd.setTag(1004);
            }
            btnResetOrAdd.setTextColor(getResources().getColor(R.color.colorBlue));
            btnResetOrAdd.setEnabled(true);
        }
    }

    public void updateReview(Review review) {
        this.review = review;
    }
}
