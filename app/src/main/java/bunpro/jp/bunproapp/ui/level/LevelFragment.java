package bunpro.jp.bunproapp.ui.level;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.ui.level.detail.LevelDetailFragment;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

public class LevelFragment extends Fragment implements View.OnClickListener, LevelContract.View {
    private LevelContract.Presenter levelPresenter;

    private TextView tvName;
    private Button btnBack;

    private RecyclerView rvView;
    private LevelAdapter mAdapter;

    public LevelFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_status_detail, container, false);

        levelPresenter = new LevelPresenter(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tvName);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        rvView = view.findViewById(R.id.rvView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvView.setLayoutManager(layoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LevelAdapter(GrammarPoint.getArrangedGrammarPointList(), Review.getReviewList(), new LevelAdapter.ClickListener() {
            @Override
            public void positionClicked(int position) {
                Fragment fragment = LevelDetailFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("lesson", position + 1);

                fragment.setArguments(bundle);
                ((MainActivity)getActivity()).addFragment(fragment);
            }
        });

        rvView.setAdapter(mAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String status = bundle.getString("status");
            String levelStr = bundle.getString("level");
            tvName.setText(status);
            levelPresenter.sortGrammarPoints(levelStr);
            updateLessons(GrammarPoint.getPointsByLessonMap());
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btnBack) {
            ((MainActivity)getActivity()).popFragment();
        }
    }

    @Override
    public void updateLessons(Map<String, List<GrammarPoint>> pointsByLesson) {
        if (!pointsByLesson.isEmpty()) {
            levelPresenter.arrangeGrammarPoints(pointsByLesson);
            mAdapter.updateData(GrammarPoint.getArrangedGrammarPointList());
            mAdapter.notifyDataSetChanged();
        }
    }
}
