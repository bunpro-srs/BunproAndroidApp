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
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.activities.MainActivity;
import bunpro.jp.bunproapp.ui.status.details.StatusDetailFragment;
import bunpro.jp.bunproapp.models.Status;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private List<Status> status;
    private ClickListener clickListener;

    public StatusAdapter(Context context, List<Status> status) {
        this.status = status;
        this.clickListener = new ClickListener() {
            @Override
            public void positionClicked(int position) {
                MainActivity mainActivity = (MainActivity)context;
                if (mainActivity.getGrammarPoints().isEmpty() || mainActivity.getReviews().isEmpty()) {
                    Toast.makeText(context, "Grammar points and reviews are not loaded yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                Fragment fragment = new StatusDetailFragment();
                Bundle bundle = new Bundle();

                if (position == status.size()) {
                    bundle.putString("status", "N1");
                    bundle.putString("level", "JLPT1");
                } else {
                    bundle.putString("status", status.get(position).getName());
                    bundle.putString("level", "JLPT" + String.valueOf(status.size() - position));
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
        if (position == status.size()) {
            viewHolder.tvName.setText("N1");
            viewHolder.progressBar.setMax(0);
            viewHolder.progressBar.setProgress(0);
            viewHolder.tvStatus.setText(String.format("%s/%s", String.valueOf(0), String.valueOf(0)));
        } else {
            Status s = status.get(position);
            viewHolder.tvName.setText(s.getName());
            viewHolder.progressBar.setMax(s.getTotal());
            viewHolder.progressBar.setProgress(s.getStatus());
            viewHolder.tvStatus.setText(String.format("%s/%s", String.valueOf(s.getStatus()), String.valueOf(s.getTotal())));
        }
    }

    @Override
    public int getItemCount() {
        if (status.size() != 0) {
            return status.size();
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
