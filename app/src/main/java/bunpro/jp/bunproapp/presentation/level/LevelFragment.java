package bunpro.jp.bunproapp.presentation.level;

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

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.presentation.home.HomeActivity;
import bunpro.jp.bunproapp.presentation.BaseFragment;
import bunpro.jp.bunproapp.presentation.level.detail.LevelDetailFragment;

public class LevelFragment extends BaseFragment implements View.OnClickListener, LevelContract.View {
    private LevelContract.Presenter levelPresenter;

    private TextView tvName;
    private Button btnBack;

    private RecyclerView rvView;
    private LevelAdapter levelAdapter;

    public LevelFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        levelPresenter.stop();
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

        Bundle bundle = getArguments();
        String levelStr = bundle != null ? bundle.getString("level") : "";
        if (bundle != null) {
            String status = bundle.getString("status");
            tvName.setText(status);
        }

        levelAdapter = new LevelAdapter(levelPresenter.getLevelGrammarPointsByLessons(levelStr), levelPresenter.getReviews(), new LevelAdapter.ClickListener() {
            @Override
            public void positionClicked(int position) {
                Fragment fragment = LevelDetailFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("lesson", levelAdapter.getLessonId(position));

                fragment.setArguments(bundle);
                ((HomeActivity)getActivity()).addFragment(fragment);
            }
        });

        rvView.setAdapter(levelAdapter);
        updateLessons();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btnBack) {
            ((HomeActivity)getActivity()).popFragment();
        }
    }

    @Override
    public void updateLessons() {
        levelAdapter.notifyDataSetChanged();
    }
}
