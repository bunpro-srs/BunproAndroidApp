package bunpro.jp.bunproapp.service;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bunpro.jp.bunproapp.models.ExampleSentence;
import bunpro.jp.bunproapp.models.GrammarPoint;
import bunpro.jp.bunproapp.models.Lesson;
import bunpro.jp.bunproapp.models.History;
import bunpro.jp.bunproapp.models.Review;
import bunpro.jp.bunproapp.models.SupplementalLink;

public class JsonParser {

    private Context mContext;

    public static JsonParser instance = null;
    private JsonParser(Context context) {
        mContext = context;
    }

    public static JsonParser getInstance(Context context) {
        if (instance == null) {
            instance = new JsonParser(context);
        }
        return instance;
    }

    public List<Review> parseReviews(JSONArray jsonArray) {

        List<Review> reviews = new ArrayList<>();

        if (jsonArray.length() > 0) {

            for (int k=0;k<jsonArray.length();k++) {

                Review review = new Review();
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(k);
                    if (jsonObject.has("id")) {
                        review.id = jsonObject.getInt("id");
                    } else {
                        review.id = -1;
                    }

                    if (jsonObject.has("user_id")) {
                        review.user_id = jsonObject.getInt("user_id");
                    } else {
                        review.user_id = -1;
                    }

                    if (jsonObject.has("study_question_id")) {
                        review.study_question_id = jsonObject.getInt("study_question_id");
                    } else {
                        review.study_question_id = -1;
                    }

                    if (jsonObject.has("grammar_point_id")) {
                        review.grammar_point_id = jsonObject.getInt("grammar_point_id");
                    } else {
                        review.grammar_point_id = -1;
                    }

                    if (jsonObject.has("times_correct")) {
                        review.times_correct = jsonObject.getInt("times_correct");
                    } else {
                        review.times_correct = 0;
                    }

                    if (jsonObject.has("times_incorrect")) {
                        review.times_incorrect = jsonObject.getInt("times_incorrect");
                    } else {
                        review.times_incorrect = 0;
                    }

                    if (jsonObject.has("streak")) {
                        review.streak = jsonObject.getInt("streak");
                    } else {
                        review.streak = 0;
                    }

                    if (jsonObject.has("next_review")) {
                        review.next_review = jsonObject.getString("next_review");
                    } else {
                        review.next_review = null;
                    }

                    if (jsonObject.has("created_at")) {
                        review.created_at = jsonObject.getString("created_at");
                    } else {
                        review.created_at = null;
                    }

                    if (jsonObject.has("updated_at")) {
                        review.updated_at = jsonObject.getString("updated_at");
                    } else {
                        review.updated_at = null;
                    }

                    if (jsonObject.has("complete")) {
                        review.complete = jsonObject.getBoolean("complete");
                    } else {
                        review.complete = false;
                    }

                    if (jsonObject.has("last_studied_at") || !jsonObject.isNull("last_studied_at")) {
                        review.last_studied_at = jsonObject.getString("last_studied_at");
                    } else {
                        review.last_studied_at = null;
                    }

                    if (jsonObject.has("was_correct") || !jsonObject.isNull("was_correct"))
                        review.was_correct = jsonObject.optBoolean("was_correct");

                    if (jsonObject.has("self_study") || !jsonObject.isNull("self_study")) {
                        review.self_study = jsonObject.getBoolean("self_study");
                    }

                    if (jsonObject.has("review_misses")) {
                        review.review_misses = jsonObject.getInt("review_misses");
                    }

                    if (jsonObject.has("history")) {
                        List<History> histories = new ArrayList<>();
                        JSONArray array = jsonObject.getJSONArray("history");
                        if (array.length() > 0) {
                            History history = new History();
                            for (int i=0;i<array.length();i++) {

                                JSONObject obj = array.getJSONObject(i);
                                history.id = obj.getInt("id");
                                history.time = obj.getString("time");
                                history.status = obj.getBoolean("status");
                                history.attempts = obj.getInt("attempts");
                                history.streak = obj.getInt("streak");

                                histories.add(history);
                            }

                            review.history = histories;
                        }
                    } else {
                        review.history = new ArrayList<>();
                    }

                    if (jsonObject.has("missed_question_ids") || !jsonObject.isNull("missed_question_ids")) {
                        JSONArray array = jsonObject.getJSONArray("missed_question_ids");
                        List<Integer> misses = new ArrayList<>();

                        if (array.length() > 0) {
                            for (int i=0;i<array.length();i++) {
                                misses.add(array.getInt(i));
                            }
                        }
                        review.missed_question_ids = misses;
                    } else {
                        review.missed_question_ids = new ArrayList<>();
                    }

                    if (jsonObject.has("studied_question_ids") || !jsonObject.isNull("studied_question_ids")) {
                        JSONArray array = jsonObject.getJSONArray("studied_question_ids");
                        List<Integer> questions = new ArrayList<>();

                        if (array.length() > 0) {
                            for (int i=0;i<array.length();i++) {
                                questions.add(array.getInt(i));
                            }
                        }
                        review.studied_question_ids = questions;
                    } else {
                        review.studied_question_ids = new ArrayList<>();
                    }

                    if (jsonObject.has("review_type") || !jsonObject.isNull("review_type")) {
                        review.review_type = jsonObject.getString("review_type");
                    } else {
                        review.review_type = null;
                    }

                    reviews.add(review);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

        return reviews;

    }

    public List<Lesson> parseLessons(JSONArray jsonArray) {
        List<Lesson> lessons = new ArrayList<>();

        if (jsonArray.length() > 0) {
            for (int k=0;k<jsonArray.length();k++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(k);
                    Lesson lesson = new Lesson();

                    if (jsonObject.has("id")) {
                        lesson.id = jsonObject.getInt("id");
                    }

                    if (jsonObject.has("attributes")) {
                        lesson.jlpt_level = jsonObject.getJSONObject("attributes").getInt("jlpt-level");

                    }

                    lessons.add(lesson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return lessons;
    }

    public List<GrammarPoint> parseGrammarPoints(JSONArray jsonArray) {
        List<GrammarPoint> grammarPoints = new ArrayList<>();
        if (jsonArray.length() > 0) {
            for (int i=0;i<jsonArray.length();i++) {
                GrammarPoint point = new GrammarPoint();
                JSONObject object = null;
                try {
                    object = jsonArray.getJSONObject(i);
                    if (object.has("id")) {
                        point.id = object.getInt("id");
                    }

                    if (object.has("attributes")) {
                        point.title = object.getJSONObject("attributes").getString("title");
                        point.meaning = object.getJSONObject("attributes").getString("meaning");
                        point.caution = object.getJSONObject("attributes").getString("caution");
                        point.structure = object.getJSONObject("attributes").getString("structure");
                        point.level = object.getJSONObject("attributes").getString("level");
                        point.lesson_id = object.getJSONObject("attributes").getInt("lesson_id");
                        point.yomikata = object.getJSONObject("attributes").getString("yomikata");
                        point.nuance = object.getJSONObject("attributes").getString("nuance");
                        point.incomplete = object.getJSONObject("attributes").getBoolean("incomplete");
                        point.grammar_order = object.getJSONObject("attributes").getInt("grammar-order");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                grammarPoints.add(point);
            }
        }
        return grammarPoints;
    }

    public List<ExampleSentence> parseExampleSentences(JSONArray jsonArray) {

        List<ExampleSentence> exampleSentences = new ArrayList<>();
        if (jsonArray.length() > 0) {
            for (int i=0;i<jsonArray.length();i++) {
                ExampleSentence sentence = new ExampleSentence();
                JSONObject object = null;
                try {
                    object = jsonArray.getJSONObject(i);
                    if (object.has("id")) {
                        sentence.id = object.getInt("id");
                    }

                    if (object.has("type")) {
                        sentence.type = object.getString("type");
                    }

                    if (object.has("attributes")) {
                        sentence.grammar_point_id = object.getJSONObject("attributes").getInt("grammar-point-id");
                        sentence.japanese = object.getJSONObject("attributes").getString("japanese");
                        sentence.english = object.getJSONObject("attributes").getString("english");
                        sentence.nuance = object.getJSONObject("attributes").getString("nuance");
                        sentence.sentence_order = object.getJSONObject("attributes").getInt("sentence-order");
                        sentence.audio_link = object.getJSONObject("attributes").getString("audio-link");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                exampleSentences.add(sentence);

            }
        }

        return exampleSentences;

    }

    public List<SupplementalLink> parseSupplimentalLinks(JSONArray jsonArray) {
        List<SupplementalLink> links = new ArrayList<>();
        if (jsonArray.length() > 0) {
            for (int i=0;i<jsonArray.length();i++) {
                SupplementalLink link = new SupplementalLink();
                JSONObject object = null;
                try {
                    object = jsonArray.getJSONObject(i);
                    if (object.has("id")) {
                        link.id = object.getInt("id");
                    }
                    if (object.has("type")) {
                        link.type = object.getString("type");
                    }

                    if (object.has("attributes")) {
                        link.grammar_point_id = object.getJSONObject("attributes").getInt("grammar-point-id");
                        link.site = object.getJSONObject("attributes").getString("site");
                        link.link = object.getJSONObject("attributes").getString("link");
                        link.description = object.getJSONObject("attributes").getString("description");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                links.add(link);
            }
        }

        return links;
    }
}
