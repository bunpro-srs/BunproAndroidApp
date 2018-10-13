package bunpro.jp.bunproapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.fragments.contract.WordDetailContract;
import bunpro.jp.bunproapp.fragments.contract.WordDetailController;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.utils.AppData;
import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.SettingEvent;
import bunpro.jp.bunproapp.utils.TextUtils;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.SupplementalLink;
import info.hoang8f.android.segmented.SegmentedGroup;
import se.fekete.furiganatextview.furiganaview.FuriganaTextView;


public class WordDetailFragment extends BaseFragment implements View.OnClickListener, WordDetailContract.View {

    private Context mContext;
    private WordDetailContract.Controller mController;

    Button btnBack, btnReset;
    RecyclerView rvWords;

    StickAdapter mAdapter;

    private GrammarPoint selectedPoint;
    private Review review;

    private int type;

    public WordDetailFragment() {

    }

    public static WordDetailFragment newInstance() {
        return new WordDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_word_detail, container, false);
        mContext = getActivity();
        mController = new WordDetailController(mContext);
        selectedPoint = null;
        type = 0;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        btnReset = view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);

        rvWords = view.findViewById(R.id.rvWords);
        rvWords.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
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

        selectedPoint = ((MainActivity)getActivity()).getGrammarPoint();
        if (!isReviewed(selectedPoint)) {
            btnReset.setText("Add to Reviews");
            btnReset.setTag(1003);
        } else {
            btnReset.setText("Reset/Remove");
            btnReset.setTag(1004);
        }

        review = mController.getReview(selectedPoint);

        if (selectedPoint != null) {

            mAdapter = new StickAdapter(0, review, selectedPoint, mContext, new ItemClickListener() {
                @Override
                public void positionClicked(int position) {
                    if (position == 0) {
                        clickedDescription();
                    } else if (position == 1) {

                        Log.d("WordDetailFragment", "header clicked");

                    } else {
                        if (type == 0) {

                            ExampleSentence sentence = selectedPoint.example_sentences.get(position - 2);
                            ((MainActivity)getActivity()).setExampleSentense(sentence);
                            Fragment fragment = ExampleFragment.newInstance();
                            ((MainActivity)getActivity()).addFragment(fragment);

                        } else {

                            SupplementalLink link = selectedPoint.supplemental_links.get(position - 2);
                            Intent bIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.link));
                            startActivity(bIntent);

                        }
                    }
                }
            }, new ItemChooseListener() {
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
        List<Review> reviews = ((MainActivity)getActivity()).getReviews();
        if (reviews.size() > 0) {

            for (int k=0;k<reviews.size();k++) {
                if (reviews.get(k).grammar_point_id == point.id) {
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

        if (id == R.id.btnReset) {
            int tag = (int)btnReset.getTag();
            if (tag == 1003) {
                addToReview();
            } else {
                resetOrRemove();
            }
        }

    }

    private void addToReview() {

    }

    private void resetOrRemove() {

        View view = getLayoutInflater().inflate(R.layout.layout_reset_remove, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);

        dialog.setContentView(view);
        dialog.show();

        RelativeLayout rlRemoveFromReviews = view.findViewById(R.id.rlRemoveFromReviews);
        rlRemoveFromReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RelativeLayout rlResetReviewProgress = view.findViewById(R.id.rlResetReviewProgress);
        rlResetReviewProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
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

    private class ReviewItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        LayoutInflater inflater;
        Context mContext;

        Review review;

        ReviewItemAdapter(Review review, Context context) {
            mContext = context;
            inflater = LayoutInflater.from(context);
            this.review = review;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

            View view = inflater.inflate(R.layout.item_review, viewGroup, false);
            return new ReviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            int streak = this.review.streak;
            if (streak > position) {
                ((ReviewViewHolder) viewHolder).ivReview.setAlpha(1.0f);
            } else {
                ((ReviewViewHolder) viewHolder).ivReview.setAlpha(0.2f);
            }
        }

        @Override
        public int getItemCount() {
            return 12;
        }

        class ReviewViewHolder extends RecyclerView.ViewHolder {

            ImageView ivReview;
            ReviewViewHolder(@NonNull View itemView) {
                super(itemView);
                ivReview = itemView.findViewById(R.id.ivReview);

            }
        }
    }

    private class StickAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private GrammarPoint point;
        private Review review;

        private int type;

        private static final int TYPE_DESCRIPTION = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_SELECTOR = 2;

        private LayoutInflater inflater;
        private ItemClickListener listener;
        private ItemChooseListener chooseListener;

        StickAdapter(int type, Review review, GrammarPoint point, Context context, ItemClickListener listener, ItemChooseListener chooseListener) {

            inflater = LayoutInflater.from(context);
            this.listener = listener;
            this.chooseListener = chooseListener;
            this.type = type;
            this.point = point;
            this.review = review;

            Collections.sort(this.point.example_sentences, ExampleSentence.IdComparator);
            Collections.sort(this.point.supplemental_links, SupplementalLink.IdComparator);

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            if (viewType == TYPE_DESCRIPTION) {
                final View view = inflater.inflate(R.layout.item_word_detail_description, viewGroup, false);
                return new DescriptionHolder(view, listener);
            } else if (viewType == TYPE_SELECTOR) {
                final View view = inflater.inflate(R.layout.item_word_header, viewGroup, false);
                return new HeaderHolder(view, this.chooseListener);
            } else {
                final View view = inflater.inflate(R.layout.item_word, viewGroup, false);
                return new ViewHolder(view, listener);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {

            if (type == 0) {
                if (viewHolder instanceof DescriptionHolder) {

                    ((DescriptionHolder) viewHolder).tvTitle.setText(point.title);
                    ((DescriptionHolder) viewHolder).tvMeaning.setText(point.meaning);
                    String structure = TextUtils.stripHtml(point.structure);
                    structure = structure.replaceAll(",", "\n");
                    ((DescriptionHolder) viewHolder).tvStructure.setText(structure);

                    if (point.caution != null && point.caution.length() != 0) {
                        ((DescriptionHolder) viewHolder).llCaution.setVisibility(View.VISIBLE);
                        ((DescriptionHolder) viewHolder).tvCaution.setText(TextUtils.stripHtml(point.caution));
                    } else {
                        ((DescriptionHolder) viewHolder).llCaution.setVisibility(View.GONE);
                    }

                    if (review != null) {
                        ((DescriptionHolder)viewHolder).llReviews.setVisibility(View.VISIBLE);
                        ReviewItemAdapter adapter = new ReviewItemAdapter(this.review, mContext);
                        ((DescriptionHolder) viewHolder).rvReviews.setAdapter(adapter);

                    } else {
                        ((DescriptionHolder)viewHolder).llReviews.setVisibility(View.GONE);
                    }

                } else if (viewHolder instanceof ViewHolder) {

                    ((ViewHolder) viewHolder).rlContainer.setVisibility(View.VISIBLE);
                    ((ViewHolder) viewHolder).llReadingContainer.setVisibility(View.GONE);
                    final ExampleSentence sentence = point.example_sentences.get(position - 2);
                    ((ViewHolder) viewHolder).tvEnglish.setText(TextUtils.stripHtml(sentence.english));
                    int furigana = AppData.getInstance(mContext).getFurigana();
                    if (furigana == Constants.SETTING_FURIGANA_ALWAYS) {

                        ((ViewHolder) viewHolder).tvJapanese.setVisibility(View.GONE);
                        ((ViewHolder) viewHolder).tvJapaneseFurigana.setVisibility(View.VISIBLE);
                        String japanese = TextUtils.stripHtml(sentence.japanese);

                        if (TextUtils.includeKanji(japanese)) {
                            ((ViewHolder) viewHolder).tvJapanese.setVisibility(View.GONE);
                            ((ViewHolder) viewHolder).tvJapaneseFurigana.setVisibility(View.VISIBLE);
                            String furiText = TextUtils.getFuriganaText(japanese);
                            ((ViewHolder) viewHolder).tvJapaneseFurigana.setFuriganaText(furiText, true);

                        } else {
                            ((ViewHolder) viewHolder).tvJapanese.setVisibility(View.VISIBLE);
                            ((ViewHolder) viewHolder).tvJapaneseFurigana.setVisibility(View.GONE);
                            ((ViewHolder) viewHolder).tvJapanese.setText(japanese);
                        }

                    } else if (furigana == Constants.SETTING_FURIGANA_NEVER) {

                        ((ViewHolder) viewHolder).tvJapaneseFurigana.setVisibility(View.GONE);
                        ((ViewHolder) viewHolder).tvJapanese.setVisibility(View.VISIBLE);
                        String japanese = TextUtils.removeKanji(TextUtils.stripHtml(sentence.japanese));
                        ((ViewHolder) viewHolder).tvJapanese.setText(japanese);

                    } else {

                        ((ViewHolder) viewHolder).tvJapaneseFurigana.setVisibility(View.GONE);
                        ((ViewHolder) viewHolder).tvJapanese.setVisibility(View.VISIBLE);
                        String japanese = TextUtils.removeKanji(TextUtils.stripHtml(sentence.japanese));
                        ((ViewHolder) viewHolder).tvJapanese.setText(japanese);

                    }

                    ((ViewHolder) viewHolder).ivIndicator.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {

                            ExampleSentence sentence = point.example_sentences.get(position -2);
                            boolean tag = (boolean)((ViewHolder) viewHolder).ivIndicator.getTag();

                            MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            Uri uri = Uri.parse(Constants.AUDIO_BASE_URL + sentence.audio_link);

                            Log.d("audio_link", Constants.AUDIO_BASE_URL + sentence.audio_link);

                            try {
                                mediaPlayer.setDataSource(mContext, uri);
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (tag) {

                                mediaPlayer.start();


                            } else {

                                ((ViewHolder) viewHolder).ivIndicator.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                }

                            }

                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    if (mediaPlayer != null) {
                                        ((ViewHolder) viewHolder).ivIndicator.setTag(true);
                                        ((ViewHolder) viewHolder).ivIndicator.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                                    }
                                }
                            });

                            ((ViewHolder) viewHolder).ivIndicator.setTag(!tag);

                        }
                    });

                }
            } else {
                if (viewHolder instanceof DescriptionHolder) {

                    ((DescriptionHolder) viewHolder).tvTitle.setText(point.title);
                    ((DescriptionHolder) viewHolder).tvMeaning.setText(point.meaning);
                    String structure = TextUtils.stripHtml(point.structure);
                    structure = structure.replaceAll(",", "\n");
                    ((DescriptionHolder) viewHolder).tvStructure.setText(structure);

                    if (point.caution != null && point.caution.length() != 0) {
                        ((DescriptionHolder) viewHolder).llCaution.setVisibility(View.VISIBLE);
                        ((DescriptionHolder) viewHolder).tvCaution.setText(TextUtils.stripHtml(point.caution));
                    } else {
                        ((DescriptionHolder) viewHolder).llCaution.setVisibility(View.GONE);
                    }

                    if (review != null) {
                        ((DescriptionHolder)viewHolder).llReviews.setVisibility(View.VISIBLE);
                        ReviewItemAdapter adapter = new ReviewItemAdapter(this.review, mContext);
                        ((DescriptionHolder) viewHolder).rvReviews.setAdapter(adapter);

                    } else {
                        ((DescriptionHolder)viewHolder).llReviews.setVisibility(View.GONE);
                    }

                } else if (viewHolder instanceof ViewHolder) {
                    ((ViewHolder) viewHolder).rlContainer.setVisibility(View.GONE);
                    ((ViewHolder) viewHolder).llReadingContainer.setVisibility(View.VISIBLE);

                    SupplementalLink link = point.supplemental_links.get(position - 2);
                    ((ViewHolder) viewHolder).tvSite.setText(link.site);
                    ((ViewHolder) viewHolder).tvDescription.setText(link.description);
                }
            }
        }

        void updateType(int type) {
            this.type = type;
        }

        @Override
        public int getItemCount() {

            if (type == 0) {
                return point.example_sentences.size() + 2;
            } else {
                return point.supplemental_links.size() + 2;
            }

        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_DESCRIPTION;
            } else if (position == 1) {
                return TYPE_SELECTOR;
            }

            return TYPE_ITEM;

        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            RelativeLayout rlContainer;
            TextView tvEnglish, tvJapanese;
            FuriganaTextView tvJapaneseFurigana;
            TextView tvSite, tvDescription;
            ImageView ivIndicator;

            LinearLayout container;
            RelativeLayout llReadingContainer;

            WeakReference<ItemClickListener> ref;

            ViewHolder(@NonNull View itemView, ItemClickListener listener) {
                super(itemView);
                ref = new WeakReference<>(listener);

                rlContainer = itemView.findViewById(R.id.rlContainer);
                rlContainer.setOnClickListener(this);

                tvEnglish = itemView.findViewById(R.id.tvEnglish);
                tvJapanese = itemView.findViewById(R.id.tvJapanese);
                tvJapaneseFurigana = itemView.findViewById(R.id.tvJapanese_furigana);
                ivIndicator = itemView.findViewById(R.id.ivIndicator);
                ivIndicator.setOnClickListener(this);
                ivIndicator.setTag(true);

                container = itemView.findViewById(R.id.container);
                llReadingContainer = itemView.findViewById(R.id.llReadingContainer);
                llReadingContainer.setOnClickListener(this);

                tvSite = itemView.findViewById(R.id.tvSite);
                tvDescription = itemView.findViewById(R.id.tvDescription);

            }

            @Override
            public void onClick(View view) {

                int id = view.getId();
                if (id != R.id.ivIndicator) {
                    ref.get().positionClicked(getAdapterPosition());
                }
            }
        }

        class HeaderHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {

            SegmentedGroup sgGroup;
            RadioButton rbExamples, rbReadings;
            WeakReference<ItemChooseListener> ref;

            HeaderHolder(@NonNull View itemView, ItemChooseListener listener) {
                super(itemView);

                ref = new WeakReference<>(listener);

                sgGroup = itemView.findViewById(R.id.segmented);
                rbExamples = itemView.findViewById(R.id.rbExamples);
                rbReadings = itemView.findViewById(R.id.rbReadings);
                rbExamples.setChecked(true);
                rbReadings.setChecked(false);

                sgGroup.setOnCheckedChangeListener(this);

            }

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbExamples) {
                    ref.get().chooseListener(0);
                } else {
                    ref.get().chooseListener(1);
                }
            }
        }

        class DescriptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView tvTitle, tvMeaning, tvStructure;
            LinearLayout llDescription;

            LinearLayout llCaution;
            TextView tvCaution;

            LinearLayout llReviews;
            RecyclerView rvReviews;

            WeakReference<ItemClickListener> ref;
            DescriptionHolder(@NonNull View itemView, ItemClickListener listener) {
                super(itemView);

                ref = new WeakReference<>(listener);

                llDescription = itemView.findViewById(R.id.llDescription);
                llDescription.setOnClickListener(this);

                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvMeaning = itemView.findViewById(R.id.tvMeaning);
                tvStructure = itemView.findViewById(R.id.tvStructure);

                llCaution = itemView.findViewById(R.id.llCaution);
                tvCaution = itemView.findViewById(R.id.tvCaution);

                llReviews = itemView.findViewById(R.id.llReviews);
                rvReviews = itemView.findViewById(R.id.rvReviews);
                rvReviews.setItemAnimator(new DefaultItemAnimator());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                rvReviews.setLayoutManager(layoutManager);
            }

            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.llDescription) {
                    ref.get().positionClicked(getAdapterPosition());
                }
            }
        }

    }

    private interface ItemChooseListener {
        void chooseListener(int index);
    }

    private interface ItemClickListener {
        void positionClicked(int position);
    }
}
