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

                    if (jsonObject.has("jlpt_level")) {
                        lesson.jlpt_level = jsonObject.getInt("jlpt_level");
                    }

                    if (jsonObject.has("created_at")) {
                        lesson.created_at = jsonObject.getString("created_at");
                    }

                    if (jsonObject.has("updated_at")) {
                        lesson.updated_at = jsonObject.getString("updated_at");
                    }

                    if (jsonObject.has("grammar_point_ids")) {
                        JSONArray array = jsonObject.getJSONArray("grammar_point_ids");
                        List<Integer> points = new ArrayList<>();
                        if (array.length() > 0) {
                            for (int i=0;i<array.length();i++) {
                                points.add(array.getInt(i));
                            }
                        }

                        lesson.grammar_point_ids = points;
                    }

                    if (jsonObject.has("grammar_points")) {

                        List<GrammarPoint> grammarPoints = new ArrayList<>();
                        JSONArray array = jsonObject.getJSONArray("grammar_points");
                        if (array.length() > 0) {
                            for (int i=0;i<array.length();i++) {
                                GrammarPoint point = new GrammarPoint();
                                JSONObject object = array.getJSONObject(i);

                                if (object.has("id")) {
                                    point.id = object.getInt("id");
                                }

                                if (object.has("title")) {
                                    point.title = object.getString("title");
                                }

                                if (object.has("created_at")) {
                                    point.created_at = object.getString("created_at");
                                }

                                if (object.has("updated_at")) {
                                    point.updated_at = object.getString("updated_at");
                                }

                                if (object.has("alternate")) {
                                    point.alternate = object.getString("alternate");
                                }

                                if (object.has("meaning")) {
                                    point.meaning = object.getString("meaning");
                                }

                                if (object.has("caution")) {
                                    point.caution = object.getString("caution");
                                }

                                if (object.has("structure")) {
                                    point.structure = object.getString("structure");
                                }

                                if (object.has("formal")) {
                                    point.formal = object.optBoolean("formal");
                                }

                                if (object.has("level")) {
                                    point.level = object.getString("level");
                                }

                                if (object.has("lesson_id")) {
                                    point.lesson_id = object.getString("lesson_id");
                                }

                                if (object.has("new_grammar")) {
                                    point.new_grammar = object.optBoolean("new_grammar");
                                }

                                if (object.has("yomikata")) {
                                    point.yomikata = object.getString("yomikata");
                                }

                                if (object.has("nuance")) {
                                    point.nuance = object.getString("nuance");
                                }

                                if (object.has("example_sentences")) {
                                    List<ExampleSentence> sentences = new ArrayList<>();

                                    JSONArray arr = object.getJSONArray("example_sentences");
                                    if (arr.length() > 0) {
                                        for (int j=0;j<arr.length();j++) {
                                            JSONObject obj = arr.getJSONObject(j);
                                            ExampleSentence sentence = new ExampleSentence();
                                            if (obj.has("id")) {
                                                sentence.id = obj.getInt("id");
                                            }
                                            if (obj.has("grammar_point_id")) {
                                                sentence.grammar_point_id = obj.getInt("grammar_point_id");
                                            }
                                            if (obj.has("structure")) {
                                                sentence.structure = obj.getString("structure");
                                            }
                                            if (obj.has("japanese")) {
                                                sentence.japanese = obj.getString("japanese");
                                            }
                                            if (obj.has("english")) {
                                                sentence.english = obj.getString("english");
                                            }
                                            if (obj.has("alternate_japanese")) {
                                                sentence.alternate_japanese = obj.getString("alternate_japanese");
                                            }
                                            if (obj.has("created_at")) {
                                                sentence.created_at = obj.getString("created_at");
                                            }
                                            if (obj.has("updated_at")) {
                                                sentence.updated_at = obj.getString("updated_at");
                                            }
                                            if (obj.has("audio_link")) {
                                                sentence.audio_link = obj.getString("audio_link");
                                            }
                                            if (obj.has("nuance")) {
                                                sentence.nuance = obj.optString("nuance");
                                            }
                                            sentences.add(sentence);
                                        }
                                    }

                                    point.example_sentences = sentences;
                                }

                                if (object.has("supplemental_links")) {
                                    JSONArray arrr = object.getJSONArray("supplemental_links");
                                    List<SupplementalLink> links = new ArrayList<>();

                                    if (arrr.length() > 0) {
                                        for (int j=0;j<arrr.length();j++) {
                                            SupplementalLink link = new SupplementalLink();
                                            JSONObject o = arrr.getJSONObject(j);

                                            if (o.has("id")) {
                                                link.id = o.getInt("id");
                                            }
                                            if (o.has("grammar_point_id")) {
                                                link.grammar_point_id = o.optInt("grammar_point_id");
                                            }
                                            if (o.has("site")) {
                                                link.site = o.optString("site");
                                            }
                                            if (o.has("link")) {
                                                link.link = o.optString("link");
                                            }
                                            if (o.has("description")) {
                                                link.description = o.optString("description");
                                            }
                                            if (o.has("created_at")) {
                                                link.created_at = o.optString("created_at");
                                            }
                                            if (o.has("updated_at")) {
                                                link.updated_at = o.optString("updated_at");
                                            }
                                            links.add(link);
                                        }
                                    }
                                    point.supplemental_links = links;
                                }

                                grammarPoints.add(point);

                            }
                        }

                        lesson.grammar_points = grammarPoints;
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

                    if (object.has("title")) {
                        point.title = object.getString("title");
                    }

                    if (object.has("created_at")) {
                        point.created_at = object.getString("created_at");
                    }

                    if (object.has("updated_at")) {
                        point.updated_at = object.getString("updated_at");
                    }

                    if (object.has("alternate")) {
                        point.alternate = object.getString("alternate");
                    }

                    if (object.has("meaning")) {
                        point.meaning = object.getString("meaning");
                    }

                    if (object.has("caution")) {
                        point.caution = object.getString("caution");
                    }

                    if (object.has("structure")) {
                        point.structure = object.getString("structure");
                    }

                    if (object.has("formal")) {
                        point.formal = object.optBoolean("formal");
                    }

                    if (object.has("level")) {
                        point.level = object.getString("level");
                    }

                    if (object.has("lesson_id")) {
                        point.lesson_id = object.getString("lesson_id");
                    }

                    if (object.has("new_grammar")) {
                        point.new_grammar = object.optBoolean("new_grammar");
                    }

                    if (object.has("yomikata")) {
                        point.yomikata = object.getString("yomikata");
                    }

                    if (object.has("nuance")) {
                        point.nuance = object.getString("nuance");
                    }

                    List<ExampleSentence> sentences = new ArrayList<>();
                    if (object.has("example_sentences")) {
                        JSONArray arr = object.getJSONArray("example_sentences");
                        if (arr.length() > 0) {
                            for (int j=0;j<arr.length();j++) {
                                JSONObject obj = arr.getJSONObject(j);
                                ExampleSentence sentence = new ExampleSentence();
                                if (obj.has("id")) {
                                    sentence.id = obj.getInt("id");
                                }
                                if (obj.has("grammar_point_id")) {
                                    sentence.grammar_point_id = obj.getInt("grammar_point_id");
                                }
                                if (obj.has("structure")) {
                                    sentence.structure = obj.getString("structure");
                                }
                                if (obj.has("japanese")) {
                                    sentence.japanese = obj.getString("japanese");
                                }
                                if (obj.has("english")) {
                                    sentence.english = obj.getString("english");
                                }
                                if (obj.has("alternate_japanese")) {
                                    sentence.alternate_japanese = obj.getString("alternate_japanese");
                                }
                                if (obj.has("created_at")) {
                                    sentence.created_at = obj.getString("created_at");
                                }
                                if (obj.has("updated_at")) {
                                    sentence.updated_at = obj.getString("updated_at");
                                }
                                if (obj.has("audio_link")) {
                                    sentence.audio_link = obj.getString("audio_link");
                                }

                                if (obj.has("nuance")) {
                                    sentence.nuance = obj.optString("nuance");
                                }
                                sentences.add(sentence);
                            }
                        }

                        point.example_sentences = sentences;

                        List<SupplementalLink> links = new ArrayList<>();
                        if (object.has("supplemental_links")) {
                            JSONArray arrr = object.getJSONArray("supplemental_links");


                            if (arrr.length() > 0) {
                                for (int j=0;j<arrr.length();j++) {
                                    SupplementalLink link = new SupplementalLink();
                                    JSONObject o = arrr.getJSONObject(j);

                                    if (o.has("id")) {
                                        link.id = o.getInt("id");
                                    }
                                    if (o.has("grammar_point_id")) {
                                        link.grammar_point_id = o.optInt("grammar_point_id");
                                    }
                                    if (o.has("site")) {
                                        link.site = o.optString("site");
                                    }
                                    if (o.has("link")) {
                                        link.link = o.optString("link");
                                    }
                                    if (o.has("description")) {
                                        link.description = o.optString("description");
                                    }
                                    if (o.has("created_at")) {
                                        link.created_at = o.optString("created_at");
                                    }
                                    if (o.has("updated_at")) {
                                        link.updated_at = o.optString("updated_at");
                                    }
                                    links.add(link);
                                }
                            }

                            point.supplemental_links = links;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                grammarPoints.add(point);
            }
        }
        return grammarPoints;
    }
}
