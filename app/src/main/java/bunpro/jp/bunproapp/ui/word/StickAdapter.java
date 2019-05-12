package bunpro.jp.bunproapp.ui.word;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.SupplementalLink;
import bunpro.jp.bunproapp.utils.AppData;
import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.TextUtils;
import info.hoang8f.android.segmented.SegmentedGroup;
import se.fekete.furiganatextview.furiganaview.FuriganaTextView;

class StickAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private GrammarPoint point;
    private Review review;
    private Context context;

    private int type;
    private List<ExampleSentence> exampleSentences;
    private List<SupplementalLink> supplementalLinks;

    private static final int TYPE_DESCRIPTION = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_SELECTOR = 2;

    private LayoutInflater inflater;
    private ItemClickListener listener;
    private ItemChooseListener chooseListener;

    StickAdapter(Context context, int type, Review review, GrammarPoint point, List<ExampleSentence> sentences, List<SupplementalLink> links, ItemClickListener listener, ItemChooseListener chooseListener) {
        this.context = context;

        inflater = LayoutInflater.from(context);
        this.listener = listener;
        this.chooseListener = chooseListener;
        this.type = type;
        this.point = point;
        this.review = review;

        this.exampleSentences = sentences;
        this.supplementalLinks = links;

        Collections.sort(this.exampleSentences, ExampleSentence.IdComparator);
        Collections.sort(this.supplementalLinks, SupplementalLink.IdComparator);

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
                String structure = TextUtils.stripHtml(point.structure != null ? point.structure : "");
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
                    ReviewItemAdapter adapter = new ReviewItemAdapter(this.review, context);
                    ((DescriptionHolder) viewHolder).rvReviews.setAdapter(adapter);

                } else {
                    ((DescriptionHolder)viewHolder).llReviews.setVisibility(View.GONE);
                }

            } else if (viewHolder instanceof ViewHolder) {

                ((ViewHolder) viewHolder).rlContainer.setVisibility(View.VISIBLE);
                ((ViewHolder) viewHolder).llReadingContainer.setVisibility(View.GONE);
                final ExampleSentence sentence = exampleSentences.get(position - 2);
                ((ViewHolder) viewHolder).tvEnglish.setText(TextUtils.stripHtml(sentence.english != null ? sentence.english : ""));
                int furigana = AppData.getInstance(context).getFurigana();
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

                        ExampleSentence sentence = exampleSentences.get(position -2);
                        boolean tag = (boolean)((ViewHolder) viewHolder).ivIndicator.getTag();

                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                        if (sentence.audio_link == null || sentence.audio_link.equals("null")) {
                            Log.w("Audio not found", "The audio resource \"" + sentence.english + "\" was not found");
                            return;
                        }

                        Uri uri = Uri.parse(Constants.AUDIO_BASE_URL + sentence.audio_link);

                        try {
                            mediaPlayer.setDataSource(context, uri);
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            Log.e("IOException", "Error while setting mediaplayer.");
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
                    ReviewItemAdapter adapter = new ReviewItemAdapter(this.review, context);
                    ((DescriptionHolder) viewHolder).rvReviews.setAdapter(adapter);

                } else {
                    ((DescriptionHolder)viewHolder).llReviews.setVisibility(View.GONE);
                }

            } else if (viewHolder instanceof ViewHolder) {
                ((ViewHolder) viewHolder).rlContainer.setVisibility(View.GONE);
                ((ViewHolder) viewHolder).llReadingContainer.setVisibility(View.VISIBLE);

                SupplementalLink link = supplementalLinks.get(position - 2);
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
            return exampleSentences.size() + 2;
        } else {
            return supplementalLinks.size() + 2;
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

            container = itemView.findViewById(R.id.word_main_container);
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
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
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

    public interface ItemChooseListener {
        void chooseListener(int index);
    }

    public interface ItemClickListener {
        void positionClicked(int position);
    }
}