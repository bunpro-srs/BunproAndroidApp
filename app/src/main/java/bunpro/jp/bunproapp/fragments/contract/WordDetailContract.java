package bunpro.jp.bunproapp.fragments.contract;

import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;

public interface WordDetailContract {
    interface View {

    }

    interface Controller {
        Review getReview(GrammarPoint point);
    }
}
