package bunpro.jp.bunproapp.presentation.example;

import android.content.ClipData;
import android.content.ClipboardManager;
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

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.presentation.home.HomeActivity;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.presentation.BaseFragment;
import bunpro.jp.bunproapp.utils.config.AppData;
import bunpro.jp.bunproapp.utils.config.Constants;
import bunpro.jp.bunproapp.utils.SettingEvent;
import bunpro.jp.bunproapp.utils.TextUtils;

public class ExampleFragment extends BaseFragment implements View.OnClickListener, ExampleContract.View {
    ExampleContract.Presenter mPresenter;
    private Context mContext;

    LinearLayout llEnglish, llJapanese, llKanjiReadings;
    TextView tvEnglish, tvJapanese;
    Button btnBack;

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
        mPresenter = new ExamplePresenter(mContext);
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
        mAdapter = new KanjiWordAdapter(kanjis, new KanjiWordAdapter.ClickListener() {
            @Override
            public void positionClicked(int position) {

                clickedKanjiReadings(position);

            }
        });

        rvWords.setAdapter(mAdapter);

        initialize();
    }

    private void initialize() {

        selectedSentence = ExampleSentence.getCurrentExampleSentence();
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
            ((HomeActivity)getActivity()).popFragment();
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
    public void clipTextToBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Bunpro", text);
        clipboard.setPrimaryClip(clip);
    }
}
