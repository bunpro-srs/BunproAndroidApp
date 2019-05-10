package bunpro.jp.bunproapp.ui.level;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.lang.ref.WeakReference;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.LevelViewHolder> {

    private ClickListener listener;
    private List<List<GrammarPoint>> pointsByLesson;
    private List<Review> reviews;

    LevelAdapter(List<List<GrammarPoint>> pointsByLesson, List<Review> reviews, ClickListener listener) {
        this.listener = listener;
        this.pointsByLesson = pointsByLesson;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LevelViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_status, viewGroup, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder viewHolder, int position) {
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
            for (GrammarPoint point : points) {
                for (Review review : reviews) {
                    if (point.id == review.grammar_point_id) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }

    public class LevelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout llContainer;
        WeakReference<ClickListener> ref;
        TextView tvName, tvStatus;

        RoundCornerProgressBar progressBar;

        LevelViewHolder(@NonNull View itemView, ClickListener listener) {
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


    public interface ClickListener {
        void positionClicked(int position);
    }
}
