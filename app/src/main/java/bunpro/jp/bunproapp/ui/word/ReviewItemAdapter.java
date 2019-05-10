package bunpro.jp.bunproapp.ui.word;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import bunpro.jp.bunproapp.R;
import bunpro.jp.bunproapp.models.Review;

class ReviewItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LayoutInflater inflater;
    Context mContext;

    Review review;

    ReviewItemAdapter(Review review, Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.review = review;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = inflater.inflate(R.layout.item_review, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int streak = this.review.streak;
        if (streak > position) {
            ((ReviewViewHolder) viewHolder).ivReview.setAlpha(1.0f);
        } else {
            ((ReviewViewHolder) viewHolder).ivReview.setAlpha(0.2f);
        }
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView ivReview;
        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivReview = itemView.findViewById(R.id.ivReview);

        }
    }
}
