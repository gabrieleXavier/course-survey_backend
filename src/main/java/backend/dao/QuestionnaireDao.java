package backend.dao;

import backend.model.answer.Answer;
import backend.model.measurment.Choice;
import backend.model.measurment.Value;
import backend.model.question.EssayQuestion;
import backend.model.question.LinQuestion;
import backend.model.question.MultipleQuestion;
import backend.model.question.Question;
import backend.model.questionnaire.Questionnaire;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class QuestionnaireDao implements Dao<Questionnaire, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private QuestionDao questionDao;
    @Inject
    private ChoiceDao choiceDao;
    @Inject
    private ValueDao valueDao;

    @Override
    public Questionnaire findById(Long questionnaireID) {
        try {
            return entityManager.find(Questionnaire.class, questionnaireID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Questionnaire> findAll() {
        List<Questionnaire> result = entityManager.createQuery("SELECT q FROM Questionnaire q").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    @Transactional
    public void save(Questionnaire questionnaire) {
        if (findById(questionnaire.getId()) == null) {
            if (questionnaire.getId() != null) {
                entityManager.merge(questionnaire);
            } else {
                throw new IllegalArgumentException("id cannot be null");
            }
        } else {
            entityManager.merge(questionnaire);
        }
    }

    public List<Questionnaire> findAllTemplates(String matriculation) {
        List<Questionnaire> result = entityManager.createQuery("SELECT q FROM Questionnaire q join User u ON q.professor = u.name where u.matriculation = " + "'" + matriculation + "' and isTemplate = 'true'").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public List<Questionnaire> findAllTemplates() {
        List<Questionnaire> result = entityManager.createQuery("SELECT q FROM Questionnaire q where isTemplate = 'true'").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    public JsonElement questionnaireToJson(Questionnaire q) {

        GsonBuilder gsonBuilder = new GsonBuilder();

        Type questionnaireListType = new TypeToken<List<Question>>() {
        }.getType();

        JsonSerializer<List<Question>> serializer = (List<Question> src, Type typeOfSrc, JsonSerializationContext context) -> {
            JsonArray jsonQuestions = new JsonArray();

            for (int i = 0; i < src.size(); i++) {
                JsonObject jsonQuestion = new JsonObject();

                if (src.get(i).getClass().getName().contains("EssayQuestion")) {
                    jsonQuestion.addProperty("questionType", "essay");
                }
                if (src.get(i).getClass().getName().contains("LinQuestion")) {
                    jsonQuestion.addProperty("questionType", "lin");
                }
                if (src.get(i).getClass().getName().contains("MultipleQuestion")) {
                    jsonQuestion.addProperty("questionType", "multiple");
                }
                jsonQuestion.addProperty("questionId", src.get(i).getId());
                jsonQuestion.addProperty("num", i);
                jsonQuestions.add(jsonQuestion);
            }

            return jsonQuestions;
        };

        gsonBuilder.registerTypeAdapter(questionnaireListType, serializer);

        Gson customGson = gsonBuilder.create();
        JsonElement customJSON = customGson.toJsonTree(q);

        customJSON.getAsJsonObject().remove("isTemplate");
        customJSON.getAsJsonObject().remove("creator");

        return customJSON;
    }

    public Questionnaire jsonToQuestionnaire(String questionnaireJson) {
        Gson gson = new Gson();

        JsonElement jsonElement = new JsonParser().parse(questionnaireJson);

        JsonElement course = jsonElement.getAsJsonObject().get("course");
        jsonElement.getAsJsonObject().remove("course");
        jsonElement.getAsJsonObject().add("courseId", course);
        jsonElement.getAsJsonObject().addProperty("isTemplate", "false");

        JsonArray jsonQuestions = jsonElement.getAsJsonObject().get("questions").getAsJsonArray();

        List<Question> questions = new ArrayList<>();
        List<Question> questionsDB = questionDao.findAll();

        for (int i = 0; i < jsonQuestions.size(); i++) {
            Question q = null;

            if (jsonQuestions.get(i).getAsJsonObject().get("questionType").getAsString().equals("essay")) {
                q = new EssayQuestion();
                q.setQuestionText(jsonQuestions.get(i).getAsJsonObject().get("question").getAsString());
                ((EssayQuestion) q).setMaxLenght(jsonQuestions.get(i).getAsJsonObject().get("max_len").getAsInt());
            }
            if (jsonQuestions.get(i).getAsJsonObject().get("questionType").getAsString().equals("lin")) {
                q = new LinQuestion();
                q.setQuestionText(jsonQuestions.get(i).getAsJsonObject().get("question").getAsString());
                List<Value> values = new ArrayList<>();
                List<Value> valuesDB = valueDao.findAll();

                Integer min = jsonQuestions.get(i).getAsJsonObject().get("min").getAsInt();
                Integer max = jsonQuestions.get(i).getAsJsonObject().get("max").getAsInt();

                for (int k = min; k <= max; k++) {
                    Value v = new Value();
                    v.setValue(k);

                    for (Value vDB : valuesDB) {
                        if (vDB.getValue() == v.getValue()) {
                            v.setId(vDB.getId());
                        }
                    }
                    if (v.getId() == null) {
                        v.setId(valuesDB.get(valuesDB.size() - 1).getId() + 1L + new Long(k));
                    }

                    values.add(v);
                }

                ((LinQuestion) q).setChoices(values);
            }
            if (jsonQuestions.get(i).getAsJsonObject().get("questionType").getAsString().equals("multiple")) {
                q = new MultipleQuestion();
                q.setQuestionText(jsonQuestions.get(i).getAsJsonObject().get("question").getAsString());
                List<Choice> choices = new ArrayList<>();
                List<Choice> choicesDB = choiceDao.findAll();

                JsonArray choicesJa = jsonQuestions.get(i).getAsJsonObject().get("choices").getAsJsonArray();

                for (int j = 0; j < choicesJa.size(); j++) {
                    Choice c = new Choice();
                    c.setText(choicesJa.get(j).getAsString());

                    for (Choice cDB : choicesDB) {
                        if (cDB.getText().equals(c.getText())) {
                            c.setId(cDB.getId());
                        }
                    }
                    if (c.getId() == null) {
                        c.setId(choicesDB.get(choicesDB.size() - 1).getId() + 1L + new Long(j));
                    }

                    choices.add(c);
                }

                ((MultipleQuestion) q).setChoices(choices);
            }

            for (Question qDB : questionsDB) {
                if (qDB.getQuestionText().equals(q.getQuestionText())) {
                    q.setId(qDB.getId());
                }
            }
            if (q.getId() == null) {
                q.setId(questionsDB.get(questionsDB.size() - 1).getId() + 1L + new Long(i));
            }

            questions.add(q);
        }

        jsonElement.getAsJsonObject().remove("questions");

        Questionnaire questionnaire = gson.fromJson(jsonElement, Questionnaire.class);
        questionnaire.setQuestions(questions);
        questionnaire.setId(null);

        List<Questionnaire> questionnairesDB = this.findAll();

        for (Questionnaire qDB : questionnairesDB) {
            if (qDB.getTitle().equals(questionnaire.getTitle())) {
                questionnaire.setId(qDB.getId());
            }
        }
        if (questionnaire.getId() == null) {
            questionnaire.setId(questionnairesDB.get(questionnairesDB.size() - 1).getId() + 1L);
        }

        return questionnaire;
    }

    public JsonElement templateToJson(Questionnaire q) {

        Gson gson = new Gson();

        JsonElement jsonElement = gson.toJsonTree(q);

        jsonElement.getAsJsonObject().remove("gps");
        jsonElement.getAsJsonObject().remove("deadline");
        jsonElement.getAsJsonObject().remove("activation");
        jsonElement.getAsJsonObject().remove("courseId");
        jsonElement.getAsJsonObject().remove("professor");
        jsonElement.getAsJsonObject().remove("questions");
        jsonElement.getAsJsonObject().remove("isTemplate");

        JsonArray jsonQuestions = new JsonArray();

        List<Question> questions = q.getQuestions();

        for (Question quest : questions) {
            JsonObject jsonQuestion = new JsonObject();
            if (quest.getClass().getName().contains("EssayQuestion")) {
                jsonQuestion.addProperty("questionType", "essay");
            }
            if (quest.getClass().getName().contains("LinQuestion")) {
                jsonQuestion.addProperty("questionType", "lin");
            }
            if (quest.getClass().getName().contains("MultipleQuestion")) {
                jsonQuestion.addProperty("questionType", "multiple");
            }

            jsonQuestion.addProperty("questionId", quest.getId());
            jsonQuestions.add(jsonQuestion);
        }

        jsonElement.getAsJsonObject().add("questions", jsonQuestions);

        return jsonElement;
    }

    public Questionnaire jsonToTemplate(String templateJson) {
        Gson gson = new Gson();

        JsonElement jsonElement = new JsonParser().parse(templateJson);

        jsonElement.getAsJsonObject().addProperty("isTemplate", "true");
        jsonElement.getAsJsonObject().addProperty("professor", jsonElement.getAsJsonObject().get("creator").getAsString());
        jsonElement.getAsJsonObject().remove("public");

        JsonArray jsonQuestions = jsonElement.getAsJsonObject().get("questions").getAsJsonArray();

        List<Question> questions = new ArrayList<>();
        List<Question> questionsDB = questionDao.findAll();

        for (int i = 0; i < jsonQuestions.size(); i++) {
            Question q = null;

            if (jsonQuestions.get(i).getAsJsonObject().get("questionType").getAsString().equals("essay")) {
                q = new EssayQuestion();
                q.setQuestionText(jsonQuestions.get(i).getAsJsonObject().get("question").getAsString());
                ((EssayQuestion) q).setMaxLenght(jsonQuestions.get(i).getAsJsonObject().get("max_len").getAsInt());
            }
            if (jsonQuestions.get(i).getAsJsonObject().get("questionType").getAsString().equals("lin")) {
                q = new LinQuestion();
                q.setQuestionText(jsonQuestions.get(i).getAsJsonObject().get("question").getAsString());
                List<Value> values = new ArrayList<>();
                List<Value> valuesDB = valueDao.findAll();

                Integer min = jsonQuestions.get(i).getAsJsonObject().get("min").getAsInt();
                Integer max = jsonQuestions.get(i).getAsJsonObject().get("max").getAsInt();

                for (int k = min; k <= max; k++) {
                    Value v = new Value();
                    v.setValue(k);

                    for (Value vDB : valuesDB) {
                        if (vDB.getValue() == v.getValue()) {
                            v.setId(vDB.getId());
                        }
                    }
                    if (v.getId() == null) {
                        v.setId(valuesDB.get(valuesDB.size() - 1).getId() + 1L + new Long(k));
                    }

                    values.add(v);
                }

                ((LinQuestion) q).setChoices(values);
            }
            if (jsonQuestions.get(i).getAsJsonObject().get("questionType").getAsString().equals("multiple")) {
                q = new MultipleQuestion();
                q.setQuestionText(jsonQuestions.get(i).getAsJsonObject().get("question").getAsString());
                List<Choice> choices = new ArrayList<>();
                List<Choice> choicesDB = choiceDao.findAll();

                JsonArray choicesJa = jsonQuestions.get(i).getAsJsonObject().get("choices").getAsJsonArray();

                for (int j = 0; j < choicesJa.size(); j++) {
                    Choice c = new Choice();
                    c.setText(choicesJa.get(j).getAsString());

                    for (Choice cDB : choicesDB) {
                        if (cDB.getText().equals(c.getText())) {
                            c.setId(cDB.getId());
                        }
                    }
                    if (c.getId() == null) {
                        c.setId(choicesDB.get(choicesDB.size() - 1).getId() + 1L + new Long(j));
                    }

                    choices.add(c);
                }

                ((MultipleQuestion) q).setChoices(choices);
            }

            for (Question qDB : questionsDB) {
                if (qDB.getQuestionText().equals(q.getQuestionText())) {
                    q.setId(qDB.getId());
                }
            }
            if (q.getId() == null) {
                q.setId(questionsDB.get(questionsDB.size() - 1).getId() + 1L + new Long(i));
            }

            questions.add(q);
        }

        jsonElement.getAsJsonObject().remove("questions");

        Questionnaire template = gson.fromJson(jsonElement, Questionnaire.class);
        template.setQuestions(questions);
        template.setId(null);

        List<Questionnaire> templatesDB = this.findAll();

        if (templatesDB != null) {
            for (Questionnaire tDB : templatesDB) {
                if (tDB.getTitle().equals(template.getTitle())) {
                    template.setId(tDB.getId());
                }
            }
        }
        if (template.getId() == null) {
            template.setId(templatesDB.get(templatesDB.size() - 1).getId() + 1L);
        }

        return template;
    }
}
