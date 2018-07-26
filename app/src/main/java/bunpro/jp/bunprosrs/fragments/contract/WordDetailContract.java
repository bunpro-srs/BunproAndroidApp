package bunpro.jp.bunprosrs.fragments.contract;

import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.models.Review;

public interface WordDetailContract {
    interface View {

    }

    interface Controller {
        Review getReview(GrammarPoint point);
    }
}
