package bunpro.jp.bunproapp.ui.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bunpro.jp.bunproapp.models.GrammarPoint;


public class LevelPresenter implements LevelContract.Presenter {
    private LevelContract.View levelView;

    public LevelPresenter(LevelContract.View levelView) {
        this.levelView = levelView;
    }

    public void sortGrammarPoints(String levelStr) {
        List<GrammarPoint> grammarPoints = GrammarPoint.getGrammarPointList();

        List<GrammarPoint> levelPoints = new ArrayList<>();
        if (grammarPoints.size() != 0 ) {

            Map<String, List<GrammarPoint>> pointsByLevel = new HashMap<>();
            for (GrammarPoint point : grammarPoints) {
                String level = point.level;
                if (!pointsByLevel.containsKey(level)) {
                    pointsByLevel.put(level, new ArrayList<GrammarPoint>());
                }
                pointsByLevel.get(level).add(point);
            }
            Map<String, List<GrammarPoint>> pointsByLesson = new HashMap<>();
            if (pointsByLevel.containsKey(levelStr)) {
                levelPoints = pointsByLevel.get(levelStr);

                if (levelPoints.size() > 0) {
                    for (GrammarPoint point : levelPoints) {
                        String lesson = String.valueOf(point.lesson_id);
                        if (!pointsByLesson.containsKey(lesson)) {
                            pointsByLesson.put(lesson, new ArrayList<GrammarPoint>());
                        }
                        pointsByLesson.get(lesson).add(point);
                    }
                }
            }
            GrammarPoint.setPointsByLessonMap(pointsByLesson);
        }

    }

    public void arrangeGrammarPoints(Map<String, List<GrammarPoint>> pointsByLesson) {

        List<String> mapKeys = new ArrayList<>(pointsByLesson.keySet());
        List<Integer> mapKeys_integer = new ArrayList<>();
        for (String key : mapKeys) {
            mapKeys_integer.add(Integer.parseInt(key));
        }
        Collections.sort(mapKeys_integer);
        mapKeys = new ArrayList<>();
        for (int k : mapKeys_integer) {
            mapKeys.add(String.valueOf(k));
        }

        List<List<GrammarPoint>> sortedGrammarPoints = new ArrayList<>();
        for (String key : mapKeys) {
            sortedGrammarPoints.add(pointsByLesson.get(key));
        }

        GrammarPoint.setArrangedGrammarPointList(sortedGrammarPoints);
    }
}
