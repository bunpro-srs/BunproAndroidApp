package bunpro.jp.bunprosrs.fragments.contract;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.models.Review;


public class StatusDetailController implements StatusDetailContract.Controller {

    private Context mContext;
    private List<GrammarPoint> grammarPoints, levelPoints;
    private List<Review> reviews;
    private Map<String, List<GrammarPoint>> pointsByLesson;

    public StatusDetailController(Context context, String levelStr, List<GrammarPoint> grammarPoints, List<Review> reviews) {
        mContext = context;
        this.grammarPoints = grammarPoints;
        this.reviews = reviews;
        sortGrammarPoints(levelStr);
    }

    private void sortGrammarPoints(String levelStr) {

        levelPoints = new ArrayList<>();
        if (grammarPoints.size() != 0 ) {

            Map<String, List<GrammarPoint>> pointsByLevel = new HashMap<>();
            for (GrammarPoint point : grammarPoints) {
                String level = point.level;
                if (!pointsByLevel.containsKey(level)) {
                    pointsByLevel.put(level, new ArrayList<GrammarPoint>());
                }
                pointsByLevel.get(level).add(point);
            }

            levelPoints = pointsByLevel.get(levelStr);

            pointsByLesson = new HashMap<>();
            for (GrammarPoint point : levelPoints) {
                String lesson = point.lesson_id;
                if (!pointsByLesson.containsKey(lesson)) {
                    pointsByLesson.put(lesson, new ArrayList<GrammarPoint>());
                }
                pointsByLesson.get(lesson).add(point);
            }

        }

    }


    @Override
    public void getLessons(StatusDetailContract.View v) {
        v.updateLessons(pointsByLesson);
    }


}
