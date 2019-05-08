package bunpro.jp.bunproapp.ui.level;

import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.models.GrammarPoint;

public interface LevelContract {

    interface View {
        void updateLessons(Map<String, List<GrammarPoint>> pointsByLesson);
    }

    interface Presenter {
        void sortGrammarPoints(String levelStr);
        void arrangeGrammarPoints(Map<String, List<GrammarPoint>> pointsByLesson);
    }
}
