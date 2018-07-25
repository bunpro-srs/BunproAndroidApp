package bunpro.jp.bunprosrs.activities;

import android.support.v4.app.Fragment;

import java.util.List;

import bunpro.jp.bunprosrs.models.GrammarPoint;
import bunpro.jp.bunprosrs.models.Lesson;
import bunpro.jp.bunprosrs.models.Review;
import bunpro.jp.bunprosrs.models.Status;

public interface ActivityImpl {

    void replaceFragment(Fragment fragment);

    void setjlptLevel(List<Status> levels);
    List<Status> getjlptLevel();

    void setReviews(List<Review> reviews);
    List<Review> getReviews();
    void setLessons(List<Lesson> lessons);
    List<Lesson> getLessons();
    void setGrammarPoints(List<GrammarPoint> grammarPoints);
    List<GrammarPoint> getGrammarPoints();

    void addFragment(Fragment fragment);

}
