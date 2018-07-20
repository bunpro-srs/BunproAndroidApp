package bunpro.jp.bunprosrs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.wuadam.awesomewebview.AwesomeWebView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.activities.MainActivity;
import bunpro.jp.bunprosrs.fragments.contract.StatusContract;
import bunpro.jp.bunprosrs.fragments.contract.StatusController;
import bunpro.jp.bunprosrs.models.Review;
import bunpro.jp.bunprosrs.models.Status;
import bunpro.jp.bunprosrs.utils.Constants;
import bunpro.jp.bunprosrs.utils.UserData;

public class StatusFragment extends BaseFragment implements View.OnClickListener, StatusContract.View {

    private Context mContext;
    TextView tvName;
    TextView tvReviews;
    LinearLayout llReview;

    SwipeRefreshLayout slContainer;

    SwipeRefreshLayout slCotainer;
    RecyclerView rvView;
    StatusAdapter mAdapter;

    StatusContract.Controller mController;

    List<Status> mStatus;
    String userName;

    public StatusFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        slCotainer = view.findViewById(R.id.slContainer);

        slCotainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mController.getStatus(StatusFragment.this);

            }
        });

        tvName = view.findViewById(R.id.tvName);
        slCotainer = view.findViewById(R.id.slContainer);
        rvView = view.findViewById(R.id.rvView);

        mStatus = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvView.setLayoutManager(layoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new StatusAdapter(mStatus, new ClickListener() {
            @Override
            public void positionClicked(int position) {
                Fragment fragment = new StatusDetailFragment();

                Bundle bundle = new Bundle();
                bundle.putString("status", mStatus.get(position).getName());
                bundle.putString("user", userName);
                fragment.setArguments(bundle);
                addFragment(fragment, true);
            }
        });

        rvView.setAdapter(mAdapter);

        llReview = view.findViewById(R.id.llReview);
        llReview.setOnClickListener(this);

        tvReviews = view.findViewById(R.id.tvReviews);

        initialize();

    }

    private void initialize() {

        mController.setName(this);

        mController.getStatus(this);
        mController.getReviews(this);

        mController.getLessons(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llReview) {

            String token = UserData.getInstance(mContext).getUserKey();
            Map<String, String> header = new HashMap<>();
            header.put("token", token);
            new AwesomeWebView.Builder(mContext).setHeader(header).showUrl(false).show(Constants.APP_STUDY_URL);

        }
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingProgress(boolean stats) {

        if (stats) {
            if (!slCotainer.isRefreshing()) {
                slCotainer.setRefreshing(true);
            }
        } else {
            if (slCotainer.isRefreshing()) {
                slCotainer.setRefreshing(false);
            }
        }
    }

    @Override
    public void updateView(List<Status> status) {

        mStatus = status;
        mAdapter.setStatus(mStatus);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void updateUserName(String name) {
        tvName.setText(name);
        userName = name;
    }

    @Override
    public void updateReviewStatus(List<Review> reviews) {
        tvReviews.setText(String.format("%s Reviews", String.valueOf(reviews.size())));
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
            Status status = mStatus.get(position);
            viewHolder.tvName.setText(status.getName());

            viewHolder.progressBar.setMax(status.getTotal());
            viewHolder.progressBar.setProgress(status.getStatus());

            viewHolder.tvStatus.setText(String.format("%s/%s", String.valueOf(status.getStatus()), String.valueOf(status.getTotal())));
        }

        @Override
        public int getItemCount() {
            return mStatus.size();
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
