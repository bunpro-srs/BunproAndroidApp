package bunpro.jp.bunproapp.ui.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.interactors.GrammarPointInteractor;
import bunpro.jp.bunproapp.interactors.ReviewInteractor;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Review;


public class LevelPresenter implements LevelContract.Presenter {
    private LevelContract.View levelView;
    private ReviewInteractor reviewInteractor;
    private GrammarPointInteractor grammarPointInteractor;

    public LevelPresenter(LevelContract.View levelView) {
        this.levelView = levelView;
        reviewInteractor = new ReviewInteractor(this.levelView.getContext());
        grammarPointInteractor = new GrammarPointInteractor(this.levelView.getContext());
    }

    public void stop() {
        reviewInteractor.close();
    }

    public List<Review> getReviews() {
        return reviewInteractor.loadReviews().findAll();
    }

    public List<GrammarPoint> getLevelGrammarPoints(String level) {
        return grammarPointInteractor.loadLevelGrammarPoints(level).findAll();
    }

    public Map<Integer, List<GrammarPoint>> getLevelGrammarPointsByLessons(String level) {
        List<GrammarPoint> levelGrammarPoints = getLevelGrammarPoints(level);
        Map<Integer, List<GrammarPoint>> levelGrammarPointsByLessons = new HashMap<>();
        for(GrammarPoint levelGrammarPoint : levelGrammarPoints) {
            if (!levelGrammarPointsByLessons.containsKey(levelGrammarPoint.lesson_id)) {
                levelGrammarPointsByLessons.put(levelGrammarPoint.lesson_id, new ArrayList<>());
            }
            levelGrammarPointsByLessons.get(levelGrammarPoint.lesson_id).add(levelGrammarPoint);
        }
        return levelGrammarPointsByLessons;
    }
}
