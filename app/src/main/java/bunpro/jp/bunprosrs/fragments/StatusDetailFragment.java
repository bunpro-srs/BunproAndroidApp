package bunpro.jp.bunprosrs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunprosrs.R;
import bunpro.jp.bunprosrs.activities.MainActivity;
import bunpro.jp.bunprosrs.models.Lesson;

public class StatusDetailFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    TextView tvName;
    Button btnBack;

    RecyclerView rvView;
    StatusDetailAdapter mAdapter;
    List<Lesson> lessons;

    public StatusDetailFragment() {
        lessons = new ArrayList<>();
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
        lessons = ((MainActivity)getActivity()).getLessons();
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

        mAdapter = new StatusDetailAdapter(new ClickListener() {
            @Override
            public void positionClicked(int position) {
                Fragment fragment = new LevelDetailFragment();
                addFragment(fragment, true);
            }
        });

        rvView.setAdapter(mAdapter);

        initialize();

    }

    private void initialize() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            String status = bundle.getString("status");
            tvName.setText(status);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btnBack) {
            popFragment();
        }
    }


    private class StatusDetailAdapter extends RecyclerView.Adapter<StatusDetailViewHolder> {

        ClickListener listener;

        StatusDetailAdapter(ClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public StatusDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new StatusDetailViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_status, viewGroup, false), listener);
        }

        @Override
        public void onBindViewHolder(@NonNull StatusDetailViewHolder viewHolder, int position) {
            viewHolder.tvName.setText(String.format("Lesson %s", String.valueOf(position+1)));

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private class StatusDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout llContainer;
        WeakReference<ClickListener> ref;
        TextView tvName, tvStatus;

        StatusDetailViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            ref = new WeakReference<>(listener);

            llContainer = itemView.findViewById(R.id.llContainer);
            llContainer.setOnClickListener(this);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);

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
