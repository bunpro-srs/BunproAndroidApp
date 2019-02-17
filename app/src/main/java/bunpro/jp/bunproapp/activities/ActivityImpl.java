package bunpro.jp.bunproapp.activities;

import androidx.fragment.app.Fragment;

import java.util.List;

import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;
import bunpro.jp.bunproapp.models.SupplementalLink;

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

    void setArrangedGrammarPoints(List<List<GrammarPoint>> grammarPoints);
    List<List<GrammarPoint>> getArrangedGrammarPoints();

    void setGrammarPoint(GrammarPoint point);
    GrammarPoint getGrammarPoint();

    void setExampleSentense(ExampleSentence sentense);
    void setExampleSentences(List<ExampleSentence> sentences);
    List<ExampleSentence> getExampleSentences();
    ExampleSentence getExampleSentence();

    void setSupplimentalLinks(List<SupplementalLink> links);
    List<SupplementalLink> getSupplimentalLinks();

    void addFragment(Fragment fragment);
    void popFragment();
}
