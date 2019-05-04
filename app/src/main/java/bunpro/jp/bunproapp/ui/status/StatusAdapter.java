package bunpro.jp.bunproapp.ui.status;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.lang.ref.WeakReference;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.fragments.StatusDetailFragment;
import bunpro.jp.bunproapp.models.Status;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private StatusContract.Presenter statusPresenter;
    private ClickListener clickListener;

    public StatusAdapter(Context context, StatusContract.Presenter statusPresenter) {
        this.statusPresenter = statusPresenter;
        this.clickListener = new ClickListener() {
            @Override
            public void positionClicked(int position) {
                MainActivity mainActivity = (MainActivity)context;
                if (mainActivity.getGrammarPoints().isEmpty() || mainActivity.getReviews().isEmpty()) {
                    Toast.makeText(context, "Grammar points and reviews are not loaded yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                Fragment fragment = StatusDetailFragment.newInstance();
                Bundle bundle = new Bundle();

                if (position == statusPresenter.getStatus().size()) {
                    bundle.putString("status", "N1");
                    bundle.putString("level", "JLPT1");
                } else {
                    bundle.putString("status", statusPresenter.getStatus().get(position).getName());
                    bundle.putString("level", "JLPT" + String.valueOf(statusPresenter.getStatus().size() - position));
                }

                fragment.setArguments(bundle);
                mainActivity.addFragment(fragment);
            }
        };
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new StatusViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_status, viewGroup, false), this.clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder viewHolder, int position) {
        if (position == statusPresenter.getStatus().size()) {
            viewHolder.tvName.setText("N1");
            viewHolder.progressBar.setMax(0);
            viewHolder.progressBar.setProgress(0);
            viewHolder.tvStatus.setText(String.format("%s/%s", String.valueOf(0), String.valueOf(0)));
        } else {
            Status status = statusPresenter.getStatus().get(position);
            viewHolder.tvName.setText(status.getName());
            viewHolder.progressBar.setMax(status.getTotal());
            viewHolder.progressBar.setProgress(status.getStatus());
            viewHolder.tvStatus.setText(String.format("%s/%s", String.valueOf(status.getStatus()), String.valueOf(status.getTotal())));
        }
    }

    @Override
    public int getItemCount() {
        if (statusPresenter.getStatus().size() != 0) {
            return statusPresenter.getStatus().size();
        } else {
            return 0;
        }
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
