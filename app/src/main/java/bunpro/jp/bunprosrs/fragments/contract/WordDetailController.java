package bunpro.jp.bunprosrs.fragments.contract;

import android.content.Context;

import java.util.List;

import bunpro.jp.bunprosrs.activities.MainActivity;
import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.models.Review;

public class WordDetailController implements WordDetailContract.Controller {

    private Context mContext;

    public WordDetailController(Context context) {
        mContext = context;
    }

    @Override
    public Review getReview(GrammarPoint point) {
        Review review = null;

        List<Review> reviews = ((MainActivity)mContext).getReviews();
        if (reviews.size() > 0) {
            for (Review r : reviews) {
                if (r.grammar_point_id == point.id) {
                    review = r;
                    break;
                }
            }
        }

        return review;
    }
}
