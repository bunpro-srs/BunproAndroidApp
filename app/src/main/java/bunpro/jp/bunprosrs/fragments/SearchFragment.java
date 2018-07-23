package bunpro.jp.bunprosrs.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.fragments.contract.SearchContract;
import bunpro.jp.bunprosrs.fragments.contract.SearchController;
import bunpro.jp.bunprosrs.models.GrammarPoint;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import info.hoang8f.android.segmented.SegmentedGroup;

import static android.support.v7.widget.RecyclerView.*;

public class SearchFragment extends BaseFragment implements SearchContract.View {

    private Context mContext;

    SegmentedGroup filterGroup;
    RadioButton rbAll, rbUnlearned, rbLearned;
    SearchView svSearch;

    SwipeRefreshLayout sfLayout;

    RecyclerView rvWords;
    private StickyHeaderDecoration decor;
    SearchWordAdapter mAdapter;

    SearchContract.Controller mController;
    List<GrammarPoint> grammarPoints;
    int filter;

    public SearchFragment() {

    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_search, container, false);
        mContext = getActivity();
        mController = new SearchController(mContext);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterGroup = view.findViewById(R.id.segmented);
        filterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.rbAll) {
                    filter = 0;
                } else if (id == R.id.rbUnlearned) {
                    filter = 1;
                } else {
                    filter = 2;
                }

                mController.getAllWords(SearchFragment.this, filter);
            }
        });

        rbAll = view.findViewById(R.id.rbAll);
        rbLearned = view.findViewById(R.id.rbLearned);
        rbUnlearned = view.findViewById(R.id.rbUnlearned);

        sfLayout = view.findViewById(R.id.sfLayout);
        sfLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mController.getAllWords(SearchFragment.this, filter);
            }
        });

        rbAll.setChecked(true);
        rbLearned.setChecked(false);
        rbUnlearned.setChecked(false);

        svSearch = view.findViewById(R.id.svSearch);
        android.support.v7.widget.SearchView.SearchAutoComplete searchAutoComplete = (android.support.v7.widget.SearchView.SearchAutoComplete)svSearch.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.parseColor("#c7d1d3"));
        searchAutoComplete.setTextColor(Color.parseColor("#c7d1d3"));

        rvWords = view.findViewById(R.id.rvWords);
        rvWords.setHasFixedSize(true);
        rvWords.setItemAnimator(new DefaultItemAnimator());

        LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvWords.setLayoutManager(layoutManager);

        grammarPoints = new ArrayList<>();

        mAdapter = new SearchWordAdapter(grammarPoints, mContext, new ItemClickListener() {
            @Override
            public void positionClicked(int position) {

                GrammarPoint point = grammarPoints.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("grammar_point", point);
                Fragment fragment = new WordDetailFragment();
                fragment.setArguments(bundle);
                addFragment(fragment, true);
            }
        });

        decor = new StickyHeaderDecoration(mAdapter);
        rvWords.addItemDecoration(decor, 0);

        rvWords.setAdapter(mAdapter);

        initialize();

    }

    private void initialize() {
        filter = 0;
        sfLayout.setRefreshing(true);
    }

    @Override
    public void showError(String msg) {

        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void updateView(List<GrammarPoint> points) {

        if (sfLayout.isRefreshing()) {
            sfLayout.setRefreshing(false);
        }

        grammarPoints = points;
        mAdapter.update(grammarPoints);
        mAdapter.notifyDataSetChanged();

    }

    private class SearchWordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderAdapter<SearchWordAdapter.HeaderHolder> {

        private LayoutInflater inflater;
        private ItemClickListener listener;

        List<GrammarPoint> points;

        SearchWordAdapter(List<GrammarPoint> points, Context context, ItemClickListener listener) {
            inflater = LayoutInflater.from(context);
            this.listener = listener;
            this.points = points;

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = inflater.inflate(R.layout.item_search_word, viewGroup, false);
            return new ViewHolder(view, listener);

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            GrammarPoint point = this.points.get(position);
            if (viewHolder instanceof ViewHolder) {
                ((ViewHolder) viewHolder).tvJapanese.setText(point.title);
                ((ViewHolder) viewHolder).tvEnglish.setText(point.meaning);
            }
        }

        @Override
        public int getItemCount() {
            return this.points.size();
        }

        @Override
        public long getHeaderId(int position) {

            GrammarPoint point = points.get(position);
            String numberStr = point.level.replaceAll("[^0-9]", "");

            return Integer.parseInt(numberStr);

        }

        @NonNull
        @Override
        public HeaderHolder onCreateHeaderViewHolder(@NonNull ViewGroup parent) {
            final View view = inflater.inflate(R.layout.item_search_word_header, parent, false);
            return new HeaderHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(@NonNull HeaderHolder viewHolder, int position) {

            viewHolder.tvHeader.setText(String.format("N%s", String.valueOf(getHeaderId(position))));
        }

        void update(List<GrammarPoint> points) {

            this.points = points;

        }

        class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

            RelativeLayout rlContainer;
            TextView tvEnglish, tvJapanese;

            WeakReference<ItemClickListener> ref;

            ViewHolder(@NonNull View itemView, ItemClickListener listener) {
                super(itemView);
                ref = new WeakReference<>(listener);

                rlContainer = itemView.findViewById(R.id.rlContainer);
                rlContainer.setOnClickListener(this);

                tvEnglish = itemView.findViewById(R.id.tvEnglish);
                tvJapanese = itemView.findViewById(R.id.tvJapanese);

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

            TextView tvHeader;
            HeaderHolder(@NonNull View itemView) {
                super(itemView);
                tvHeader = itemView.findViewById(R.id.tvHeader);
            }
        }
    }

    private interface ItemClickListener {
        void positionClicked(int position);
    }

}
