package bunpro.jp.bunproapp.ui.level.detail;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

class LevelDetailAdapter extends RecyclerView.Adapter<LevelDetailAdapter.LevelDetailViewHolder> {

    List<GrammarPoint> grammarPoints;
    List<Review> reviews;
    ClickListener listener;

    LevelDetailAdapter(List<Review> reviews, List<GrammarPoint> grammarPoints, ClickListener listener) {
        this.listener = listener;
        this.grammarPoints = grammarPoints;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public LevelDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new LevelDetailViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_level, viewGroup, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelDetailViewHolder viewHolder, int position) {
        GrammarPoint grammarPoint = this.grammarPoints.get(position);
        viewHolder.tvJapanese.setText(grammarPoint.title);
        viewHolder.tvEnglish.setText(grammarPoint.meaning);

        if (checkReview(grammarPoint)) {
            viewHolder.ivReview.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivReview.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return grammarPoints.size();
    }

    public int getGrammarPointId(int position) {
        return grammarPoints.get(position).id;
    }

    void updateGrammarPoints(List<GrammarPoint> grammarPoints) {
        this.grammarPoints = grammarPoints;
    }

    private boolean checkReview(GrammarPoint point) {
        boolean result = false;
        if (this.reviews.size() > 0) {
            for (Review review : this.reviews) {
                if (review.complete && review.grammar_point_id == point.id) {
                    result = true;
                }
            }
        }

        return result;
    }

    public class LevelDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout llContainer;
        WeakReference<ClickListener> ref;
        TextView tvEnglish, tvJapanese;
        ImageView ivReview;

        LevelDetailViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            ref = new WeakReference<>(listener);

            llContainer = itemView.findViewById(R.id.llContainer);
            llContainer.setOnClickListener(this);

            tvEnglish = itemView.findViewById(R.id.tvEnglish);
            tvJapanese = itemView.findViewById(R.id.tvJapanese);
            ivReview = itemView.findViewById(R.id.ivReview);

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
