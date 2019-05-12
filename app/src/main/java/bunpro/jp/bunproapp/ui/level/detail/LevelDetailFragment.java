package bunpro.jp.bunproapp.ui.level.detail;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.SupplementalLink;
import bunpro.jp.bunproapp.ui.home.HomeActivity;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.ui.word.WordDetailFragment;

public class LevelDetailFragment extends Fragment implements View.OnClickListener, LevelDetailContract.View {
    private Context context;

    private TextView tvName;
    private Button btnBack;

    private RecyclerView rvView;
    private LevelDetailAdapter mAdapter;
    private List<Review> reviews;
    private List<GrammarPoint> grammarPoints;

    private LevelDetailContract.Presenter levelDetailPresenter;

    public static LevelDetailFragment newInstance() {
        return new LevelDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        levelDetailPresenter.stop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_level_detail, container, false);

        context = getActivity();
        levelDetailPresenter = new LevelDetailPresenter(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvName);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        rvView = view.findViewById(R.id.rvView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvView.setLayoutManager(layoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LevelDetailAdapter(levelDetailPresenter.getReviews(), GrammarPoint.getGrammarPointList(), new LevelDetailAdapter.ClickListener() {
            @Override
            public void positionClicked(int position) {
                HomeActivity homeActivity = (HomeActivity)getActivity();
                if (!levelDetailPresenter.checkSentenceAndLinksExistence()) {
                    Toast.makeText(context, "Examples and links are not loaded yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                Fragment fragment = WordDetailFragment.newInstance();
                GrammarPoint.setCurrentGrammarPoint(grammarPoints.get(position));
                homeActivity.addFragment(fragment);
            }
        });

        rvView.setAdapter(mAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int lesson = bundle.getInt("lesson");
            tvName.setText(String.format("Lesson %s", String.valueOf(lesson)));
            levelDetailPresenter.pickGrammarPoint(lesson -1);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            ((HomeActivity)getActivity()).popFragment();
        }
    }

    @Override
    public void updateGrammarPoints(List<GrammarPoint> pointList) {

        this.grammarPoints = pointList;
        Collections.sort(this.grammarPoints, GrammarPoint.IdComparator);
        mAdapter.updateGrammarPoints(pointList);
        mAdapter.notifyDataSetChanged();

    }
}
