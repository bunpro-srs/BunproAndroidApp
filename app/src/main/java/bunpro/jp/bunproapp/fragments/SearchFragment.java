package bunpro.jp.bunproapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

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

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.fragments.contract.SearchContract;
import bunpro.jp.bunproapp.fragments.contract.SearchController;
import bunpro.jp.bunproapp.models.GrammarPoint;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import info.hoang8f.android.segmented.SegmentedGroup;

import static androidx.recyclerview.widget.RecyclerView.*;

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
        rvWords = view.findViewById(R.id.rvWords);
        rvWords.setHasFixedSize(true);
        rvWords.setItemAnimator(new DefaultItemAnimator());

        LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvWords.setLayoutManager(layoutManager);

        grammarPoints = new ArrayList<>();

        mAdapter = new SearchWordAdapter(grammarPoints, mContext, new ItemClickListener() {
            @Override
            public void positionClicked(int position) {
                MainActivity mainActivity = (MainActivity)getActivity();
                if (mainActivity.getExampleSentences().isEmpty() || mainActivity.getSupplimentalLinks().isEmpty()) {
                    Toast.makeText(mContext, "Examples and links are not loaded yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                Fragment fragment = WordDetailFragment.newInstance();
                mainActivity.setGrammarPoint(grammarPoints.get(position));
                mainActivity.addFragment(fragment);
            }
        });

        decor = new StickyHeaderDecoration(mAdapter);
        rvWords.addItemDecoration(decor, 0);

        rvWords.setAdapter(mAdapter);

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

                sfLayout.setRefreshing(true);

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


        svSearch = view.findViewById(R.id.svSearch);
        //androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete = (androidx.appcompat.widget.SearchView.SearchAutoComplete)svSearch.findViewById(androidx.appcompat.appcompat.R.id.search_src_text);
        //searchAutoComplete.setHintTextColor(Color.parseColor("#c7d1d3"));
        //searchAutoComplete.setTextColor(Color.parseColor("#c7d1d3"));


        filter = 0;
        rbAll.setChecked(true);
        rbLearned.setChecked(false);
        rbUnlearned.setChecked(false);
    }

    @Override
    public void showError(String msg) {

        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        if (sfLayout.isRefreshing()) {
            sfLayout.setRefreshing(false);
        }
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

            try {
                return Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                return 0;
            }
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
