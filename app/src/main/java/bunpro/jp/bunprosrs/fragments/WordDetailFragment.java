package bunpro.jp.bunprosrs.fragments;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.fragments.contract.WordDetailContract;
import bunpro.jp.bunprosrs.fragments.contract.WordDetailController;
import bunpro.jp.bunprosrs.models.ExampleSentence;
import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.utils.TextUtils;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import info.hoang8f.android.segmented.SegmentedGroup;


public class WordDetailFragment extends BaseFragment implements View.OnClickListener, WordDetailContract.View {

    private Context mContext;
    private WordDetailContract.Controller mController;

    Button btnBack, btnReset;
    RecyclerView rvWords;

    StickAdapter mAdapter;
    private StickyHeaderDecoration decor;

    private GrammarPoint selectedPoint;

    public WordDetailFragment() {

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

    private void initialize() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedPoint = bundle.getParcelable("grammar_point");
            Log.d("examples", String.valueOf(selectedPoint.example_sentences.size()));

            mAdapter = new StickAdapter(selectedPoint, mContext, new ItemClickListener() {
                @Override
                public void positionClicked(int position) {
                    if (position == 0) {
                        clickedDescription();
                    } else {
                        ExampleSentence sentence = selectedPoint.example_sentences.get(position - 1);
                        Bundle b = new Bundle();
                        b.putParcelable("example_sentence", sentence);
                        Fragment fragment = new ExampleFragment();
                        fragment.setArguments(b);
                        addFragment(fragment, true);
                    }
                }
            });

            decor = new StickyHeaderDecoration(mAdapter);
            rvWords.addItemDecoration(decor, 0);

            rvWords.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            popFragment();
        }

        if (id == R.id.btnReset) {

        }

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

    private class StickAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderAdapter<StickAdapter.HeaderHolder> {

        private GrammarPoint point;

        private static final int TYPE_DESCRIPTION = 0;
        private static final int TYPE_ITEM = 1;

        private LayoutInflater inflater;
        private ItemClickListener listener;

        StickAdapter(GrammarPoint point, Context context, ItemClickListener listener) {
            inflater = LayoutInflater.from(context);
            this.listener = listener;
            this.point = point;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            if (viewType == TYPE_DESCRIPTION) {
                final View view = inflater.inflate(R.layout.item_word_detail_description, viewGroup, false);
                return new DescriptionHolder(view, listener);
            } else {
                final View view = inflater.inflate(R.layout.item_word, viewGroup, false);
                return new ViewHolder(view, listener);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

            if (viewHolder instanceof DescriptionHolder) {

                ((DescriptionHolder) viewHolder).tvTitle.setText(point.title);
                ((DescriptionHolder) viewHolder).tvMeaning.setText(point.meaning);
                String structure = TextUtils.stripHtml(point.structure);
                structure = structure.replaceAll(",", "\n");
                ((DescriptionHolder) viewHolder).tvStructure.setText(structure);

            } else {

                ExampleSentence sentence = point.example_sentences.get(position - 1);
                ((ViewHolder) viewHolder).tvEnglish.setText(TextUtils.stripHtml(sentence.english));
                String japanese = TextUtils.removeKanji(sentence.japanese);
                ((ViewHolder) viewHolder).tvJapanese.setText(japanese);
            }
        }

        @Override
        public int getItemCount() {
            return point.example_sentences.size() + 1;
        }

        @Override
        public long getHeaderId(int position) {
            if (position == 0) {
                return StickyHeaderDecoration.NO_HEADER_ID;
            }
            return (long) 1;
        }

        @NonNull
        @Override
        public HeaderHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent) {
            final View view = inflater.inflate(R.layout.item_word_header, parent, false);
            return new HeaderHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(@NonNull HeaderHolder viewHolder, int position) {

        }


        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_DESCRIPTION;
            }

            return TYPE_ITEM;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            RelativeLayout rlContainer;
            TextView tvEnglish, tvJapanese;
            ImageView ivIndicator;

            WeakReference<ItemClickListener> ref;

            ViewHolder(@NonNull View itemView, ItemClickListener listener) {
                super(itemView);
                ref = new WeakReference<>(listener);

                rlContainer = itemView.findViewById(R.id.rlContainer);
                rlContainer.setOnClickListener(this);

                tvEnglish = itemView.findViewById(R.id.tvEnglish);
                tvJapanese = itemView.findViewById(R.id.tvJapanese);
                ivIndicator = itemView.findViewById(R.id.ivIndicator);
            }

            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.rlContainer) {
                    ref.get().positionClicked(getAdapterPosition());
                }
            }
        }

        class HeaderHolder extends RecyclerView.ViewHolder {

            SegmentedGroup sgGroup;
            RadioButton rbExamples, rbReadings;

            HeaderHolder(@NonNull View itemView) {
                super(itemView);

                sgGroup = itemView.findViewById(R.id.segmented);
                rbExamples = itemView.findViewById(R.id.rbExamples);
                rbReadings = itemView.findViewById(R.id.rbReadings);
                rbExamples.setChecked(true);
                rbReadings.setChecked(false);
            }
        }

        class DescriptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView tvTitle, tvMeaning, tvStructure;
            LinearLayout llDescription;

            WeakReference<ItemClickListener> ref;
            DescriptionHolder(@NonNull View itemView, ItemClickListener listener) {
                super(itemView);

                ref = new WeakReference<>(listener);

                llDescription = itemView.findViewById(R.id.llDescription);
                llDescription.setOnClickListener(this);

                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvMeaning = itemView.findViewById(R.id.tvMeaning);
                tvStructure = itemView.findViewById(R.id.tvStructure);

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

    private interface ItemClickListener {
        void positionClicked(int position);
    }
}
