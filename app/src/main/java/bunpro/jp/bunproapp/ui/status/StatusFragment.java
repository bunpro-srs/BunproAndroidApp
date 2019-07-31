package bunpro.jp.bunproapp.ui.status;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.ybq.android.spinkit.SpinKitView;
import com.wuadam.awesomewebview.AwesomeWebView;

import java.util.HashMap;
import java.util.Map;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.models.Status;
import bunpro.jp.bunproapp.ui.BaseFragment;
import bunpro.jp.bunproapp.utils.Constants;
import bunpro.jp.bunproapp.utils.UserData;
import me.leolin.shortcutbadger.ShortcutBadger;

public class StatusFragment extends BaseFragment implements StatusContract.View, View.OnClickListener {
    // TODO: set it private!! Waiting for the status API endpoint fix
    public StatusContract.Presenter statusPresenter;
    private Context context;
    // TODO: Remove accessor
    public Context getContext() {
        return context;
    }

    StatusAdapter statusAdapter;

    TextView tvName;
    TextView tvReviews;
    LinearLayout llReview;
    RelativeLayout study, cram;
    SwipeRefreshLayout slContainer;
    RecyclerView rvView;
    TextView tvReviewTimeTextView, tvUpdate1Hour, tvUpdate24Hours;
    SpinKitView spinKitView;

    private int reviewCount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        statusPresenter.stop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_status, container, false);
        statusPresenter = new StatusPresenter(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slContainer = view.findViewById(R.id.slContainer);
        slContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                statusPresenter.fetchStatus();
            }
        });

        tvName = view.findViewById(R.id.tvName);
        slContainer = view.findViewById(R.id.slContainer);
        rvView = view.findViewById(R.id.rvView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        rvView.setLayoutManager(layoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());
        statusAdapter = new StatusAdapter(context, Status.getStatusList(), statusPresenter);
        rvView.setAdapter(statusAdapter);

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

        final Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setReviewsLoadingProgress(true);
                statusPresenter.updateReviews();
                handler.postDelayed(this, (long)(1000 * 60 * 5));
            }
        }, (long)(1000 * 60 * 5));

        statusPresenter.updateReviews();
        statusPresenter.fetchStatus();
        statusPresenter.updateGrammarPoints();
    }

    @Override
    public void refresh() {
        statusAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llReview && reviewCount > 0) {
            String token = UserData.getInstance(context).getUserKey();
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + token);
            new AwesomeWebView.Builder(context).setHeader(header).showUrl(false).show(Constants.APP_STUDY_URL);
        }
        else if (id == R.id.cram) {
            String token = UserData.getInstance(context).getUserKey();
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + token);
            new AwesomeWebView.Builder(context).setHeader(header).showUrl(false).show(Constants.APP_CRAM);
        }
        else if (id == R.id.study) {
            String token = UserData.getInstance(context).getUserKey();
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + token);
            new AwesomeWebView.Builder(context).setHeader(header).showUrl(false).show(Constants.APP_LEARN);
        }
    }

    public void setGlobalLoadingProgress(boolean loading) {
        if (loading) {
            if (!slContainer.isRefreshing()) {
                slContainer.setRefreshing(true);
            }
        } else {
            if (slContainer.isRefreshing()) {
                slContainer.setRefreshing(false);
            }
        }
    }

    public void setReviewsLoadingProgress(boolean loading) {
        if (spinKitView != null) {
            if (loading) {
                spinKitView.setVisibility(View.VISIBLE);
            } else {
                spinKitView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateUsername(String username) {
        tvName.setText(username);
    }

    @Override
    public void updateReviewTime(String dateUpdated) {
        tvReviewTimeTextView.setText(String.format("Updated: Today, %s", dateUpdated));
    }

    @Override
    public void updateReviewNumbers(int currentReviews, int hourReviews, int dayReviews) {
        reviewCount = currentReviews;
        tvReviews.setText(String.format("%s Reviews", String.valueOf(currentReviews)));
        tvUpdate1Hour.setText(String.format("+%s", String.valueOf(hourReviews)));
        tvUpdate24Hours.setText(String.format("+%s", String.valueOf(dayReviews)));
        setReviewsLoadingProgress(false);
    }

    @Override
    public void updateBadge(int count) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (count != 0) {
                ShortcutBadger.applyCount(getActivity().getApplicationContext(), count);
            } else {
                ShortcutBadger.removeCount(getActivity().getApplicationContext());
            }
        } else {
            Log.e("Null activity", "No activity context when trying to update the badge !");
        }
    }
}
