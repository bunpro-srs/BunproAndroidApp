package bunpro.jp.bunproapp.ui.search;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.ui.home.HomeActivity;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.ui.BaseFragment;
import bunpro.jp.bunproapp.ui.word.WordDetailFragment;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration;
import info.hoang8f.android.segmented.SegmentedGroup;

import static androidx.recyclerview.widget.RecyclerView.*;

public class SearchFragment extends BaseFragment implements SearchContract.View {
    private SearchContract.Presenter searchPresenter;
    private Context context;

    SegmentedGroup filterGroup;
    RadioButton rbAll, rbUnlearned, rbLearned;
//    SearchView svSearch;

    SwipeRefreshLayout sfLayout;

    RecyclerView rvWords;
    private StickyHeaderDecoration decor;
    SearchAdapter searchAdapter;

    int filter;

    public SearchFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchPresenter.stop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_search, container, false);
        context = getActivity();
        searchPresenter = new SearchPresenter(this);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvWords = view.findViewById(R.id.rvWords);
        rvWords.setHasFixedSize(true);
        rvWords.setItemAnimator(new DefaultItemAnimator());

        LayoutManager layoutManager = new LinearLayoutManager(context);
        rvWords.setLayoutManager(layoutManager);

        searchAdapter = new SearchAdapter(searchPresenter.getGrammarPoints(), context, new SearchAdapter.ItemClickListener() {
            @Override
            public void positionClicked(int position) {
                HomeActivity homeActivity = (HomeActivity)getActivity();
                if (!searchPresenter.checkSentenceAndLinksExistence()) {
                    Toast.makeText(context, "Examples and links are not loaded yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                Fragment fragment = WordDetailFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("currentGrammarPointId", searchAdapter.getGrammarPointId(position));
                fragment.setArguments(bundle);
                homeActivity.addFragment(fragment);
            }
        });

        decor = new StickyHeaderDecoration(searchAdapter);
        rvWords.addItemDecoration(decor, 0);

        rvWords.setAdapter(searchAdapter);

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

                searchPresenter.getAllWords(filter);
            }
        });

        rbAll = view.findViewById(R.id.rbAll);
        rbLearned = view.findViewById(R.id.rbLearned);
        rbUnlearned = view.findViewById(R.id.rbUnlearned);

        sfLayout = view.findViewById(R.id.sfLayout);
        sfLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchPresenter.getAllWords(filter);
            }
        });


//        svSearch = view.findViewById(R.id.svSearch);
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

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        if (sfLayout.isRefreshing()) {
            sfLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateView(List<GrammarPoint> points) {
        if (sfLayout.isRefreshing()) {
            sfLayout.setRefreshing(false);
        }

        searchAdapter.update(points);
        searchAdapter.notifyDataSetChanged();
    }

}
