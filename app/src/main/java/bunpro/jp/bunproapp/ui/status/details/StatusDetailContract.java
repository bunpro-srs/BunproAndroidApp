package bunpro.jp.bunproapp.ui.status.details;

import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.models.GrammarPoint;

public interface StatusDetailContract {

    interface View {
        void updateLessons(Map<String, List<GrammarPoint>> pointsByLesson);
    }

    interface Presenter {
        void getLessons(StatusDetailContract.View v);
    }
}
