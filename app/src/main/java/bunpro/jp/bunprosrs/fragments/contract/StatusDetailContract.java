package bunpro.jp.bunprosrs.fragments.contract;

import java.util.List;
import java.util.Map;

import bunpro.jp.bunprosrs.models.GrammarPoint;

public interface StatusDetailContract {

    interface View {
        void updateLessons(Map<String, List<GrammarPoint>> pointsByLesson);
    }

    interface Controller {
        void getLessons(StatusDetailContract.View v);
    }
}
