package bunpro.jp.bunprosrs.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.activities.MainActivity;
import bunpro.jp.bunprosrs.fragments.contract.ExampleContract;
import bunpro.jp.bunprosrs.fragments.contract.ExampleController;
import bunpro.jp.bunprosrs.models.ExampleSentence;
import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.utils.TextUtils;

public class ExampleFragment extends BaseFragment implements View.OnClickListener, ExampleContract.View {

    private Context mContext;

    LinearLayout llEnglish, llJapanese, llKanjiReadings;
    TextView tvEnglish, tvJapanese;
    Button btnBack;

    ExampleContract.Controller mController;
    GrammarPoint selectedPoint;
    ExampleSentence selectedSentence;

    RecyclerView rvWords;

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
        llEnglish.setTag(false);
        llEnglish.setOnClickListener(this);
        tvEnglish = view.findViewById(R.id.tvEnglish);

        llJapanese = view.findViewById(R.id.llJapanese);
        llJapanese.setOnClickListener(this);

        tvJapanese = view.findViewById(R.id.tvJapanese);

        llKanjiReadings = view.findViewById(R.id.llKanjiReadings);
        llKanjiReadings.setOnClickListener(this);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        rvWords = view.findViewById(R.id.rvWords);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvWords.setLayoutManager(layoutManager);
        rvWords.setItemAnimator(new DefaultItemAnimator());

        initialize();
    }

    private void initialize() {

        selectedSentence = ((MainActivity)getActivity()).getExampleSentence();
        if (selectedSentence != null) {
            String english = TextUtils.stripHtml(selectedSentence.english);
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

            List<String> kanjis = TextUtils.getKanji(TextUtils.stripHtml(selectedSentence.japanese));

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

        if (id == R.id.llKanjiReadings) {
            clickedKanjiReadings();
        }

        if (id == R.id.btnBack) {
            popFragment();
        }
    }

    private void clickedEnglish() {

        boolean stats = (boolean) llEnglish.getTag();
        if (stats) {

            tvEnglish.setTextColor(Color.parseColor("#FFFFFF"));
            tvEnglish.setText(TextUtils.stripHtml(selectedSentence.english));

        } else {

            tvEnglish.setText(R.string.show_english);
            tvEnglish.setTextColor(Color.parseColor("#ffff00"));
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

    private void clickedKanjiReadings() {
        View view = getLayoutInflater().inflate(R.layout.layout_kanji_reading, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);

        dialog.setContentView(view);
        dialog.show();

        RelativeLayout rlCopyKanji = view.findViewById(R.id.rlCopyKanji);
        rlCopyKanji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        RelativeLayout rlCopyKana = view.findViewById(R.id.rlCopyKana);
        rlCopyKana.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void clipTextToBoard(String text) {
        super.clipTextToBoard(text);
    }
}
