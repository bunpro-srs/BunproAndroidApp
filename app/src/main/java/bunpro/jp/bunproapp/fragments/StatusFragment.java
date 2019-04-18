package bunpro.jp.bunproapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.ybq.android.spinkit.SpinKitView;
import com.wuadam.awesomewebview.AwesomeWebView;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.fragments.contract.StatusContract;
import bunpro.jp.bunproapp.fragments.contract.StatusController;
import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.UserData;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;
import me.leolin.shortcutbadger.ShortcutBadger;

public class StatusFragment extends BaseFragment implements View.OnClickListener, StatusContract.View {

    private Context mContext;
    TextView tvName;
    TextView tvReviews;
    LinearLayout llReview;

    RelativeLayout study, cram;

    SwipeRefreshLayout slContainer;
    RecyclerView rvView;
    StatusAdapter mAdapter;

    StatusContract.Controller mController;

    List<Status> mStatus;
    String userName;

    List<Lesson> lessons;
    List<Review> reviews;
    List<GrammarPoint> grammarPoints;

    TextView tvReviewTimeTextView, tvUpdate1Hour, tvUpdate24Hours;
    SpinKitView spinKitView;

    public StatusFragment() {

    }


    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lessons = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_status, container, false);

        mContext = getActivity();
        mController = new StatusController(mContext);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slContainer = view.findViewById(R.id.slContainer);

        slContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mController.getStatus(StatusFragment.this);
            }
        });

        tvName = view.findViewById(R.id.tvName);
        slContainer = view.findViewById(R.id.slContainer);
        rvView = view.findViewById(R.id.rvView);

        mStatus = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvView.setLayoutManager(layoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new StatusAdapter(mStatus, new ClickListener() {
            @Override
            public void positionClicked(int position) {
                Fragment fragment = StatusDetailFragment.newInstance();
                Bundle bundle = new Bundle();

                if (position == mStatus.size()) {
                    bundle.putString("status", "N1");
                    bundle.putString("level", "JLPT1");
                } else {
                    bundle.putString("status", mStatus.get(position).getName());
                    bundle.putString("level", "JLPT" + String.valueOf(mStatus.size() + 1 - position));
                }

                fragment.setArguments(bundle);
                ((MainActivity)getActivity()).addFragment(fragment);
            }
        });

        rvView.setAdapter(mAdapter);

        llReview = view.findViewById(R.id.llReview);
        llReview.setOnClickListener(this);

        tvReviews = view.findViewById(R.id.tvReviews);

        study = view.findViewById(R.id.study);
        study.setOnClickListener(this);
        cram = view.findViewById(R.id.cram);
        cram.setOnClickListener(this);

        tvReviewTimeTextView = view.findViewById(R.id.tvUpdateText);
        spinKitView = view.findViewById(R.id.review_spin_kit);

        tvUpdate1Hour = view.findViewById(R.id.tvUpdate1Hour);
        tvUpdate24Hours = view.findViewById(R.id.tvUpdate24Hours);

        initialize();

        final Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!spinKitView.isShown()) {
                    spinKitView.setVisibility(View.VISIBLE);
                }
                mController.getReviews(StatusFragment.this);
                handler.postDelayed(this, 1000 * 60 * 5);
            }
        }, 1000 * 60 * 5);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initialize() {

        if (!spinKitView.isShown()) {
            spinKitView.setVisibility(View.VISIBLE);
        }

        mController.setName(this);
        this.grammarPoints = ((MainActivity)getActivity()).getGrammarPoints();
        this.reviews = ((MainActivity)getActivity()).getReviews();
        tvReviews.setText(String.format("%s Reviews", String.valueOf(reviews.size())));
        mController.getStatus(this);
        updateBadge();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llReview) {

            String token = UserData.getInstance(mContext).getUserKey();
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + token);
            new AwesomeWebView.Builder(mContext).setHeader(header).showUrl(false).show(Constants.APP_STUDY_URL);

        }

        if (id == R.id.cram) {
            String token = UserData.getInstance(mContext).getUserKey();
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + token);
            new AwesomeWebView.Builder(mContext).setHeader(header).showUrl(false).show(Constants.APP_CRAM);
        }

        if (id == R.id.study) {
            String token = UserData.getInstance(mContext).getUserKey();
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + token);
            new AwesomeWebView.Builder(mContext).setHeader(header).showUrl(false).show(Constants.APP_LEARN);
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingProgress(boolean stats) {

        if (stats) {
            if (!slContainer.isRefreshing()) {
                slContainer.setRefreshing(true);
            }
        } else {
            if (slContainer.isRefreshing()) {
                slContainer.setRefreshing(false);
            }
        }
    }

    @Override
    public void updateView(List<Status> status) {

        mStatus = status;
        mAdapter.setStatus(mStatus);
        mAdapter.notifyDataSetChanged();
        if (spinKitView.isShown()) {
            spinKitView.setVisibility(View.GONE);
        }
        updateReviewTime();
        calculateReviewsNumber();
        updateBadge();
    }

    @Override
    public void updateUserName(String name) {

        tvName.setText(name);
        userName = name;

    }

    @Override
    public void updateReviewStatus(List<Review> reviews) {
        this.reviews = reviews;
        MainActivity mainActivity = ((MainActivity)getActivity());
        if (mainActivity != null) {
            mainActivity.setReviews(this.reviews);
        } else {
            Log.e("Null activity", "Getting a null activity when trying to update the status !");
        }
        tvReviews.setText(String.format("%s Reviews", String.valueOf(reviews.size())));
        mController.getStatus(this);
        if (spinKitView.isShown()) {
            spinKitView.setVisibility(View.GONE);

        }
        updateReviewTime();
        calculateReviewsNumber();
        updateBadge();
    }

    @Override
    public void updateLessons(List<Lesson> lessons) {
        this.lessons = lessons;
        ((MainActivity)getActivity()).setLessons(this.lessons);

        mController.getReviews(this);
    }

    @Override
    public void updateGrammarPoints(List<GrammarPoint> grammarPoints) {

        ((MainActivity)getActivity()).setGrammarPoints(grammarPoints);
        this.grammarPoints = grammarPoints;
        mController.getReviews(this);
    }

    public void updateReviewTime() {

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat spf= new SimpleDateFormat("hh:mm aaa");
        String dateStr = spf.format(currentTime);
        tvReviewTimeTextView.setText(String.format("Updated: Today, %s", dateStr));

    }

    public void calculateReviewsNumber() {

        if (this.reviews.size() > 0) {

            int oneHours = 0;
            int oneDayHours = 0;

            Date currentDate = Calendar.getInstance().getTime();
            for (int k=0;k<this.reviews.size();k++) {

                try {
                    SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    Date reviewDate = spf.parse(this.reviews.get(k).next_review);

                    long diff = currentDate.getTime() - reviewDate.getTime();
                    if (diff <= 3600 * 1000) {
                        oneHours = oneHours + 1;
                    }

                    if (diff <= 3600 * 1000 * 24) {
                        oneDayHours = oneDayHours + 1;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            oneDayHours = oneDayHours - oneHours;

            tvUpdate24Hours.setText(String.format("+%s", String.valueOf(oneDayHours)));
            tvUpdate1Hour.setText(String.format("+%s", String.valueOf(oneHours)));

        }
    }

    private void updateBadge() {
        int number = this.reviews.size();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (number != 0) {
                ShortcutBadger.applyCount(getActivity().getApplicationContext(), number);
            } else {
                ShortcutBadger.removeCount(getActivity().getApplicationContext());
            }
        } else {
            Log.e("Null activity", "No activity context when trying to update the badge !");
        }
    }

    private class StatusAdapter extends RecyclerView.Adapter<StatusViewHolder> {

        List<Status> mStatus;
        ClickListener listener;

        StatusAdapter(List<Status> status, ClickListener listener) {

            mStatus = status;
            this.listener = listener;

        }

        @NonNull
        @Override
        public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new StatusViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_status, viewGroup, false), listener);
        }

        @Override
        public void onBindViewHolder(@NonNull StatusViewHolder viewHolder, int position) {

            if (position == mStatus.size()) {

                viewHolder.tvName.setText("N1");
                viewHolder.progressBar.setMax(0);
                viewHolder.progressBar.setProgress(0);
                viewHolder.tvStatus.setText(String.format("%s/%s", String.valueOf(0), String.valueOf(0)));

            } else {

                Status status = mStatus.get(position);
                viewHolder.tvName.setText(status.getName());
                viewHolder.progressBar.setMax(status.getTotal());
                viewHolder.progressBar.setProgress(status.getStatus());
                viewHolder.tvStatus.setText(String.format("%s/%s", String.valueOf(status.getStatus()), String.valueOf(status.getTotal())));

            }

        }

        @Override
        public int getItemCount() {
            if (mStatus.size() != 0) {
                return mStatus.size() + 1;
            } else {
                return 0;
            }

        }

        public void setStatus(List<Status> status) {

            mStatus = status;

        }
    }

    private class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout llContainer;
        TextView tvName, tvStatus;
        RoundCornerProgressBar progressBar;

        WeakReference<ClickListener> ref;

        StatusViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            ref = new WeakReference<>(listener);

            llContainer = itemView.findViewById(R.id.llContainer);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            progressBar = itemView.findViewById(R.id.progressBar);
            llContainer.setOnClickListener(this);

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
