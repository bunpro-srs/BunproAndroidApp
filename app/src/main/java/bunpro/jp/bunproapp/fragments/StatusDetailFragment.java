package bunpro.jp.bunproapp.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.fragments.contract.StatusDetailContract;
import bunpro.jp.bunproapp.fragments.contract.StatusDetailController;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.Review;

public class StatusDetailFragment extends BaseFragment implements View.OnClickListener, StatusDetailContract.View {

    private Context mContext;
    TextView tvName;
    Button btnBack;

    RecyclerView rvView;
    StatusDetailAdapter mAdapter;
    List<Lesson> lessons;
    List<GrammarPoint> grammarPoints;
    List<Review> reviews;
    List<List<GrammarPoint>> pointsByLesson;

    StatusDetailContract.Controller mController;

    public StatusDetailFragment() {
        lessons = new ArrayList<>();
        grammarPoints = new ArrayList<>();
        reviews = new ArrayList<>();
        pointsByLesson = new ArrayList<>();
    }

    public static StatusDetailFragment newInstance() {
        return new StatusDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_status_detail, container, false);
        mContext = getActivity();

        grammarPoints = ((MainActivity)getActivity()).getGrammarPoints();
        reviews = ((MainActivity)getActivity()).getReviews();
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

        mAdapter = new StatusDetailAdapter(pointsByLesson, new ClickListener() {
            @Override
            public void positionClicked(int position) {
                Log.d("TAG", pointsByLesson.get(position).toString());
                Fragment fragment = LevelDetailFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt("lesson", position + 1);

                fragment.setArguments(bundle);
                ((MainActivity)getActivity()).addFragment(fragment);
            }
        });

        rvView.setAdapter(mAdapter);
        initialize();

    }

    private void initialize() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String status = bundle.getString("status");
            String levelStr = bundle.getString("level");
            mController = new StatusDetailController(mContext, levelStr, grammarPoints, reviews);
            tvName.setText(status);
            mController.getLessons(this);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btnBack) {
            popFragment();
        }
    }

    @Override
    public void updateLessons(Map<String, List<GrammarPoint>> pointsByLesson) {

        if (!pointsByLesson.isEmpty()) {
            arrangeGrammarPoints(pointsByLesson);
            mAdapter.updateData(this.pointsByLesson);
            mAdapter.notifyDataSetChanged();

        }
    }

    private void arrangeGrammarPoints(Map<String, List<GrammarPoint>> pointsByLesson) {

        List<String> mapKeys = new ArrayList<>(pointsByLesson.keySet());
        List<Integer> mapKeys_integer = new ArrayList<>();
        for (String key : mapKeys) {
            mapKeys_integer.add(Integer.parseInt(key));
        }
        Collections.sort(mapKeys_integer);
        mapKeys = new ArrayList<>();
        for (int k : mapKeys_integer) {
            mapKeys.add(String.valueOf(k));
        }

        this.pointsByLesson = new ArrayList<>();
        for (String key : mapKeys) {
            this.pointsByLesson.add(pointsByLesson.get(key));
        }

        ((MainActivity)getActivity()).setArrangedGrammarPoints(this.pointsByLesson);
    }


    private class StatusDetailAdapter extends RecyclerView.Adapter<StatusDetailViewHolder> {

        ClickListener listener;
        List<List<GrammarPoint>> pointsByLesson;

        StatusDetailAdapter(List<List<GrammarPoint>> pointsByLesson, ClickListener listener) {
            this.listener = listener;
            this.pointsByLesson = pointsByLesson;
        }

        @NonNull
        @Override
        public StatusDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new StatusDetailViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_status, viewGroup, false), listener);
        }

        @Override
        public void onBindViewHolder(@NonNull StatusDetailViewHolder viewHolder, int position) {
            viewHolder.tvName.setText(String.format("Lesson %s", String.valueOf(position+1)));
            List<GrammarPoint> points = pointsByLesson.get(position);

            viewHolder.tvStatus.setText(String.valueOf(String.valueOf(checkReview(points)) + " / " + String.valueOf(points.size())));
            viewHolder.progressBar.setProgress(checkReview(points));
            viewHolder.progressBar.setMax(points.size());
        }

        @Override
        public int getItemCount() {
            return this.pointsByLesson.size();
        }

        void updateData(List<List<GrammarPoint>> pointsByLesson) {
            this.pointsByLesson = pointsByLesson;
        }

        private int checkReview(List<GrammarPoint> points) {
            int count = 0;
            if (reviews.size() > 0) {
                for (Review review : reviews) {
                    for (GrammarPoint point : points) {
                        if (point.id == review.grammar_point_id) {
                            count = count + 1;
                            break;
                        }
                    }
                }
            }
            return count;
        }
    }

    private class StatusDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout llContainer;
        WeakReference<ClickListener> ref;
        TextView tvName, tvStatus;

        RoundCornerProgressBar progressBar;

        StatusDetailViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            ref = new WeakReference<>(listener);

            llContainer = itemView.findViewById(R.id.llContainer);
            llContainer.setOnClickListener(this);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            progressBar = itemView.findViewById(R.id.progressBar);

        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.llContainer) {
                ref.get().positionClicked(getAdapterPosition());
            }
        }
    }

    private interface ClickListener {
        void positionClicked(int position);
    }
}
