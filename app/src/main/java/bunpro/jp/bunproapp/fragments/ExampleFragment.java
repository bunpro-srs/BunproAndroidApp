package bunpro.jp.bunproapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.fragments.contract.ExampleContract;
import bunpro.jp.bunproapp.fragments.contract.ExampleController;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.utils.AppData;
import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.SettingEvent;
import bunpro.jp.bunproapp.utils.TextUtils;
import bunpro.jp.bunproapp.models.GrammarPoint;

public class ExampleFragment extends BaseFragment implements View.OnClickListener, ExampleContract.View {

    private Context mContext;

    LinearLayout llEnglish, llJapanese, llKanjiReadings;
    TextView tvEnglish, tvJapanese;
    Button btnBack;

    ExampleContract.Controller mController;
    GrammarPoint selectedPoint;
    ExampleSentence selectedSentence;

    RecyclerView rvWords;
    KanjiWordAdapter mAdapter;
    List<String> kanjis;

    public ExampleFragment() {

    }

    public static ExampleFragment newInstance() {
        return new ExampleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_example, container, false);
        mContext = getActivity();
        mController = new ExampleController(mContext);
        selectedPoint = null;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llEnglish = view.findViewById(R.id.llEnglish);
        llEnglish.setOnClickListener(this);
        tvEnglish = view.findViewById(R.id.tvEnglish);

        llJapanese = view.findViewById(R.id.llJapanese);
        llJapanese.setOnClickListener(this);

        tvJapanese = view.findViewById(R.id.tvJapanese);

        llKanjiReadings = view.findViewById(R.id.llKanjiReadings);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        rvWords = view.findViewById(R.id.rvWords);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvWords.setLayoutManager(layoutManager);
        rvWords.setItemAnimator(new DefaultItemAnimator());

        kanjis = new ArrayList<>();
        mAdapter = new KanjiWordAdapter(kanjis, new ClickListener() {
            @Override
            public void positionClicked(int position) {

                clickedKanjiReadings(position);

            }
        });

        rvWords.setAdapter(mAdapter);

        initialize();
    }

    private void initialize() {

        selectedSentence = ((MainActivity)getActivity()).getExampleSentence();
        if (selectedSentence != null) {
            String english = TextUtils.stripHtml(selectedSentence.english);
            english = TextUtils.removeSub(english);
            tvEnglish.setText(english);

            String japanese = TextUtils.removeKanji(selectedSentence.japanese);
            tvJapanese.setText(japanese);

            if (TextUtils.includeKanji(selectedSentence.japanese)) {
                llKanjiReadings.setVisibility(View.VISIBLE);
                rvWords.setVisibility(View.VISIBLE);
            } else {
                llKanjiReadings.setVisibility(View.GONE);
                rvWords.setVisibility(View.GONE);
            }


            int hideEnglish = AppData.getInstance(getActivity()).getHideEnglish();
            if (hideEnglish == Constants.SETTING_HIDE_ENGLISH_NO) {
                llEnglish.setTag(false);
                tvEnglish.setTextColor(Color.parseColor("#FFFFFF"));
                tvEnglish.setText(TextUtils.removeSub(TextUtils.stripHtml(selectedSentence.english)));
            } else {
                llEnglish.setTag(true);
                tvEnglish.setText(R.string.show_english);
                tvEnglish.setTextColor(Color.parseColor("#67B4F1"));
            }

            kanjis = TextUtils.getKanjis(TextUtils.stripHtml(selectedSentence.japanese));
            mAdapter.update(kanjis);
            mAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.llEnglish) {
            clickedEnglish();
        }

        if (id == R.id.llJapanese) {
            clickedJapanese();
        }


        if (id == R.id.btnBack) {
            popFragment();
        }
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
        int hideEnglish = AppData.getInstance(getActivity()).getHideEnglish();
        if (hideEnglish == Constants.SETTING_HIDE_ENGLISH_NO) {
            tvEnglish.setTextColor(Color.parseColor("#FFFFFF"));
            tvEnglish.setText(TextUtils.removeSub(TextUtils.stripHtml(selectedSentence.english)));
        } else {
            tvEnglish.setText(R.string.show_english);
            tvEnglish.setTextColor(Color.parseColor("#67B4F1"));
        }
    }

    private void clickedEnglish() {

        boolean stats = (boolean) llEnglish.getTag();
        if (stats) {

            tvEnglish.setTextColor(Color.parseColor("#FFFFFF"));
            tvEnglish.setText(TextUtils.removeSub(TextUtils.stripHtml(selectedSentence.english)));

        } else {

            tvEnglish.setText(R.string.show_english);
            tvEnglish.setTextColor(Color.parseColor("#67B4F1"));
        }

        llEnglish.setTag(!stats);
    }

    private void clickedJapanese() {

        View view = getLayoutInflater().inflate(R.layout.layout_english_reading, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);

        dialog.setContentView(view);
        dialog.show();

        RelativeLayout rlCopyJapanese = view.findViewById(R.id.rlCopyJapanese);
        rlCopyJapanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipTextToBoard(TextUtils.removeKanji(selectedSentence.japanese));
                dialog.dismiss();
            }
        });

        RelativeLayout rlCopyEnglish = view.findViewById(R.id.rlCopyEnglish);
        rlCopyEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clipTextToBoard(TextUtils.stripHtml(selectedSentence.english));
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

    private void clickedKanjiReadings(int position) {
        final String s = kanjis.get(position);

        View view = getLayoutInflater().inflate(R.layout.layout_kanji_reading, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);

        dialog.setContentView(view);
        dialog.show();

        RelativeLayout rlCopyKanji = view.findViewById(R.id.rlCopyKanji);
        rlCopyKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clipTextToBoard(TextUtils.getKanji(s));
                dialog.dismiss();
            }
        });

        RelativeLayout rlCopyKana = view.findViewById(R.id.rlCopyKana);
        rlCopyKana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clipTextToBoard(TextUtils.getKana(s));
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

    @Override
    protected void clipTextToBoard(String text) {
        super.clipTextToBoard(text);
    }

    class KanjiWordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<String> kanjis;
        ClickListener listener;

        KanjiWordAdapter(List<String> kanjis, ClickListener listener) {
            this.kanjis = kanjis;
            this.listener = listener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            return new WordViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kanji_word, viewGroup, false), listener);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            String word = kanjis.get(i);
            ((WordViewHolder)viewHolder).tvWord.setText(word);
        }

        @Override
        public int getItemCount() {
            return kanjis.size();
        }

        void update(List<String> kanjis) {
            this.kanjis = kanjis;
        }
    }

    class WordViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        LinearLayout llContainer;
        TextView tvWord;
        WeakReference<ClickListener> ref;


        WordViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            ref = new WeakReference<>(listener);
            llContainer = itemView.findViewById(R.id.llContainer);
            llContainer.setOnClickListener(this);
            tvWord = itemView.findViewById(R.id.tvWord);
        }

        @Override
        public void onClick(View view) {

            int id = view.getId();
            if (id == R.id.llContainer) {
                ref.get().positionClicked(getAdapterPosition());
            }

        }
    }

    interface ClickListener {
        void positionClicked(int position);
    }
}
