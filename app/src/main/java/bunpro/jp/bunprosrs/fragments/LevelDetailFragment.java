package bunpro.jp.bunprosrs.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thefinestartist.Base;

import java.lang.ref.WeakReference;

import bunpro.jp.bunprosrs.R;

public class LevelDetailFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    TextView tvName;
    Button btnBack;


    RecyclerView rvView;
    LevelStatusAdapter mAdapter;

    public LevelDetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_level_detail, container, false);
        mContext = getActivity();
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvName);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        rvView = view.findViewById(R.id.rvView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvView.setLayoutManager(layoutManager);
        rvView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LevelStatusAdapter(new ClickListener() {
            @Override
            public void positionClicked(int position) {

                addFragment(new WordDetailFragment(), true);
            }
        });

        rvView.setAdapter(mAdapter);

        initialize();
    }


    private void initialize() {

        tvName.setText("Level 1");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            popFragment();
        }
    }

    private class LevelStatusAdapter extends RecyclerView.Adapter<LevelStatusViewHolder> {

        ClickListener listener;

        LevelStatusAdapter(ClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public LevelStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new LevelStatusViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_level, viewGroup, false), listener);
        }

        @Override
        public void onBindViewHolder(@NonNull LevelStatusViewHolder levelStatusViewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    private class LevelStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        LinearLayout llContainer;
        WeakReference<ClickListener> ref;

        LevelStatusViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            ref = new WeakReference<>(listener);

            llContainer = itemView.findViewById(R.id.llContainer);
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
