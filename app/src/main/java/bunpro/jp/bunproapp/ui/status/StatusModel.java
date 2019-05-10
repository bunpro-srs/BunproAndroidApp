package bunpro.jp.bunproapp.ui.status;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.Status;

public class StatusModel {

    private String userName;
    private List<Lesson> lessons;
    private List<Status> status;
    private List<Review> reviews;
    private List<GrammarPoint> grammarPoints;

    public StatusModel() {
        this.userName = "";
        this.lessons = new ArrayList<>();
        this.status = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.grammarPoints = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Status> getStatus() {
        return status;
    }
    public void setStatus(List<Status> status) {
        this.status = status;
    }

    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<GrammarPoint> getGrammarPoints() {
        return grammarPoints;
    }
    public void setGrammarPoints(List<GrammarPoint> grammarPoints) {
        this.grammarPoints = grammarPoints;
    }
}
