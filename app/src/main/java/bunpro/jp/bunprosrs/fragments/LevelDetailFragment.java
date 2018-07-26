package bunpro.jp.bunprosrs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.activities.MainActivity;
import bunpro.jp.bunprosrs.fragments.contract.LevelDetailContract;
import bunpro.jp.bunprosrs.fragments.contract.LevelDetailController;
import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.models.Review;

public class LevelDetailFragment extends BaseFragment implements View.OnClickListener, LevelDetailContract.View {

    private Context mContext;
    TextView tvName;
    Button btnBack;


    RecyclerView rvView;
    LevelStatusAdapter mAdapter;
    List<Review> reviews;
    List<GrammarPoint> grammarPoints;

    private LevelDetailContract.Controller mController;

    public LevelDetailFragment() {
        reviews = new ArrayList<>();
        grammarPoints = new ArrayList<>();
    }

    public static LevelDetailFragment newInstance() {
        return new LevelDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_level_detail, container, false);
        mContext = getActivity();
        mController = new LevelDetailController(mContext);

        reviews = ((MainActivity)getActivity()).getReviews();

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvName);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        rvView = view.findViewById(R.id.rvView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvView.setLayoutManager(layoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LevelStatusAdapter(this.reviews, this.grammarPoints, new ClickListener() {
            @Override
            public void positionClicked(int position) {

                addFragment(new WordDetailFragment(), true);
            }
        });

        rvView.setAdapter(mAdapter);

        initialize();
    }


    private void initialize() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            int lesson = bundle.getInt("lesson");
            tvName.setText(String.format("Lesson %s", String.valueOf(lesson)));
            mController.getGrammarPoints(lesson -1, this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            popFragment();
        }
    }

    @Override
    public void updateGrammarPoints(List<GrammarPoint> pointList) {

        this.grammarPoints = pointList;
        Collections.sort(this.grammarPoints, GrammarPoint.IdComparator);
        mAdapter.updateGrammarPoints(this.reviews, pointList);
        mAdapter.notifyDataSetChanged();
    }



    private class LevelStatusAdapter extends RecyclerView.Adapter<LevelStatusViewHolder> {

        List<GrammarPoint> grammarPoints;
        List<Review> reviews;
        ClickListener listener;

        LevelStatusAdapter(List<Review> reviews, List<GrammarPoint> grammarPoints, ClickListener listener) {
            this.listener = listener;
            this.grammarPoints = grammarPoints;
            this.reviews = reviews;
        }

        @NonNull
        @Override
        public LevelStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            return new LevelStatusViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_level, viewGroup, false), listener);
        }

        @Override
        public void onBindViewHolder(@NonNull LevelStatusViewHolder viewHolder, int position) {
            GrammarPoint grammarPoint = this.grammarPoints.get(position);
            viewHolder.tvJapanese.setText(grammarPoint.title);
            viewHolder.tvEnglish.setText(grammarPoint.meaning);

            if (checkReview(grammarPoint)) {
                viewHolder.ivReview.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivReview.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return grammarPoints.size();
        }

        void updateGrammarPoints(List<Review> reviews, List<GrammarPoint> grammarPoints) {
            this.reviews = reviews;
            this.grammarPoints = grammarPoints;
        }

        private boolean checkReview(GrammarPoint point) {
            boolean result = false;
            if (this.reviews.size() > 0) {
                for (Review review : this.reviews) {
                    if (review.grammar_point_id == point.id) {
                        result = true;
                    }
                }
            }

            return result;
        }
    }

    private class LevelStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        LinearLayout llContainer;
        WeakReference<ClickListener> ref;
        TextView tvEnglish, tvJapanese;
        ImageView ivReview;

        LevelStatusViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            ref = new WeakReference<>(listener);

            llContainer = itemView.findViewById(R.id.llContainer);
            llContainer.setOnClickListener(this);

            tvEnglish = itemView.findViewById(R.id.tvEnglish);
            tvJapanese = itemView.findViewById(R.id.tvJapanese);
            ivReview = itemView.findViewById(R.id.ivReview);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.llContainer) {
                ref.get().positionClicked(getAdapterPosition());
            }
        }
    }

    private interface ClickListener {
        void positionClicked(int position);
    }
}
