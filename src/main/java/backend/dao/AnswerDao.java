package backend.dao;

import backend.model.answer.Answer;
import backend.model.answer.EssayAnswer;
import backend.model.answer.LinAnswer;
import backend.model.answer.MultipleAnswer;
import backend.model.measurment.Choice;
import backend.model.measurment.Value;
import backend.model.question.Question;
import backend.model.questionnaire.Questionnaire;
import com.google.gson.*;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class AnswerDao implements Dao<Answer, Long> {

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private QuestionnaireDao questionnaireDao;
    @Inject
    private QuestionDao questionDao;
    @Inject
    private ChoiceDao choiceDao;
    @Inject
    private ValueDao valueDao;

    @Override
    public Answer findById(Long answerID) {
        try {
            return entityManager.find(Answer.class, answerID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Answer> findAll() {
        List<Answer> result = entityManager.createQuery("SELECT a FROM Answer a").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    @Transactional
    public void save(Answer answer) {
        if (findById(answer.getId()) == null) {
            if (answer.getId() != null) {
                entityManager.persist(answer);
            } else {
                throw new IllegalArgumentException("id cannot be null");
            }
        } else {
            entityManager.merge(answer);
        }
    }

    public List<Answer> findAllById(Long questionID, Long questionnaireID) {

        List<Answer> result = entityManager.createQuery("SELECT a FROM Answer a where question_id=" + questionID + " and questionnaire_id=" + questionnaireID).getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }


    public JsonElement getStatistics(Long questionnaireID, Integer num) {
        Gson gson = new Gson();

        Questionnaire questionnaire = questionnaireDao.findById(questionnaireID);

        JsonElement questionnaireJson = gson.toJsonTree(questionnaire);

        JsonElement questionnaireWithNum = gson.toJsonTree(questionnaireDao.questionnaireToJson(questionnaire));

        Long questionID = null;

        for (int n = 0; n < questionnaireWithNum.getAsJsonObject().get("questions").getAsJsonArray().size(); n++) {
            if (questionnaireWithNum.getAsJsonObject().get("questions").getAsJsonArray().get(n).getAsJsonObject().get("num").getAsInt() == num) {
                questionID = questionnaireWithNum.getAsJsonObject().get("questions").getAsJsonArray().get(n).getAsJsonObject().get("questionId").getAsLong();
            }
        }

        JsonElement questions = questionnaireJson.getAsJsonObject().get("questions");
        JsonElement id = questionnaireJson.getAsJsonObject().get("id");

        JsonObject statisticJson = new JsonObject();

        for (int i = 0; i < questions.getAsJsonArray().size(); i++) {

            statisticJson.add("questionnaireId", id);

            JsonArray statistics = new JsonArray();

            // EssayQuestion
            if (questions.getAsJsonArray().get(i).getAsJsonObject().get("id").getAsInt() == questionID.intValue() && questions.getAsJsonArray().get(i).getAsJsonObject().get("max_len") != null) {

                statisticJson.addProperty("numberOfTheQuestion", i);

                // Collected
                List<Answer> answers = this.findAllById(questionID, questionnaireID);
                JsonArray collected = new JsonArray();

                JsonElement customJson;

                for (Answer a : answers) {
                    customJson = gson.toJsonTree(a);

                    JsonElement choice = customJson.getAsJsonObject().get("answer");
                    collected.add(choice);
                }

                statisticJson.add("collected", collected);

                // Histogram
                JsonObject jsonHisto = new JsonObject();
                jsonHisto.addProperty("statistic", "histogram");

                JsonArray values = new JsonArray();

                for (int j = 0; j < collected.size(); j++) {
                    JsonElement choice = collected.get(j);
                    Integer choiceCount = 0;
                    for (int k = 0; k < collected.size(); k++) {
                        if (choice.getAsString().equals(collected.get(k).getAsString())) {
                            choiceCount++;
                        }
                    }
                    values.add(choiceCount);
                }

                jsonHisto.add("value", values);

                statistics.add(jsonHisto);
                statisticJson.add("statistics", statistics);

            } else {
                // Multiple question
                if (questions.getAsJsonArray().get(i).getAsJsonObject().get("id").getAsInt() == questionID.intValue() && questions.getAsJsonArray().get(i).getAsJsonObject().get("choices").getAsJsonArray().get(0).getAsJsonObject().get("text") != null) {

                    statisticJson.addProperty("numberOfTheQuestion", i);

                    // Collected
                    List<Answer> answers = this.findAllById(questionID, questionnaireID);
                    JsonArray collected = new JsonArray();

                    JsonElement customJson;

                    for (Answer a : answers) {
                        customJson = gson.toJsonTree(a);

                        JsonElement choice = customJson.getAsJsonObject().get("choice").getAsJsonObject().get("text");
                        collected.add(choice);
                    }

                    statisticJson.add("collected", collected);

                    // Histogram
                    JsonObject jsonHisto = new JsonObject();
                    jsonHisto.addProperty("statistic", "histogram");

                    JsonArray values = new JsonArray();

                    for (int j = 0; j < questions.getAsJsonArray().get(i).getAsJsonObject().get("choices").getAsJsonArray().size(); j++) {
                        JsonElement choice = questions.getAsJsonArray().get(i).getAsJsonObject().get("choices").getAsJsonArray().get(j).getAsJsonObject().get("text");
                        Integer choiceCount = 0;
                        for (int k = 0; k < collected.size(); k++) {
                            if (choice.getAsString().equals(collected.get(k).getAsString())) {
                                choiceCount++;
                            }
                        }
                        values.add(choiceCount);
                    }

                    jsonHisto.add("value", values);

                    statistics.add(jsonHisto);
                    statisticJson.add("statistics", statistics);

                }

                // Lin question
                if (questions.getAsJsonArray().get(i).getAsJsonObject().get("id").getAsInt() == questionID.intValue() && questions.getAsJsonArray().get(i).getAsJsonObject().get("choices").getAsJsonArray().get(0).getAsJsonObject().get("value") != null) {

                    statisticJson.addProperty("numberOfTheQuestion", i);

                    // Collected
                    List<Answer> answers = this.findAllById(questionID, questionnaireID);
                    JsonArray collected = new JsonArray();

                    JsonElement customJson;

                    for (Answer a : answers) {
                        customJson = gson.toJsonTree(a);

                        JsonElement choice = customJson.getAsJsonObject().get("choice").getAsJsonObject().get("value");
                        collected.add(choice);
                    }

                    statisticJson.add("collected", collected);
                    statisticJson.add("statistics", statistics);

                    // Histogram
                    JsonObject jsonHisto = new JsonObject();
                    jsonHisto.addProperty("statistic", "histogram");

                    JsonArray values = new JsonArray();

                    for (int j = 0; j < questions.getAsJsonArray().get(i).getAsJsonObject().get("choices").getAsJsonArray().size(); j++) {
                        JsonElement choice = questions.getAsJsonArray().get(i).getAsJsonObject().get("choices").getAsJsonArray().get(j).getAsJsonObject().get("value");
                        Integer choiceCount = 0;
                        for (int k = 0; k < collected.size(); k++) {
                            if (choice.getAsInt() == collected.get(k).getAsInt()) {
                                choiceCount++;
                            }
                        }
                        values.add(choiceCount);
                    }

                    jsonHisto.add("value", values);

                    statistics.add(jsonHisto);

                    double[] list = new double[collected.size()];
                    for (int h = 0; h < collected.size(); h++) {
                        list[h] = collected.get(h).getAsDouble();
                    }

                    //Mean
                    JsonObject jsonMean = new JsonObject();
                    jsonMean.addProperty("statistic", "mean");

                    Mean m = new Mean();
                    Double mean = m.evaluate(list);

                    jsonMean.addProperty("value", (Math.floor(mean * 100) / 100));

                    statistics.add(jsonMean);

                    //SD
                    JsonObject jsonSD = new JsonObject();
                    jsonSD.addProperty("statistic", "standard deviation");

                    StandardDeviation sd = new StandardDeviation();
                    Double dev = sd.evaluate(list);

                    jsonSD.addProperty("value", (Math.floor(dev * 100) / 100));

                    statistics.add(jsonSD);

                    statisticJson.add("statistics", statistics);

                }
            }
        }
        return statisticJson;
    }


    public List<Answer> jsonToAnswer(String answerJson) {

        JsonArray jsonArray = new JsonParser().parse(answerJson).getAsJsonArray();

        List<Answer> answers = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {

            String type = null;
            Long questionId = null;

            Answer a = null;

            JsonElement jsonElement = jsonArray.get(i);
            JsonElement questionnaireId = jsonElement.getAsJsonObject().get("questionnaireId");
            JsonElement questionText = jsonElement.getAsJsonObject().get("questionText");
            JsonElement collected = jsonElement.getAsJsonObject().get("collected");

            List<Question> questions = questionDao.findAll();
            Questionnaire questionnaire = questionnaireDao.findById(questionnaireId.getAsLong());

            for (Question q : questions) {
                if (q.getQuestionText().equals(questionText.getAsString())) {
                    questionId = q.getId();
                    type = q.getClass().toString();
                }
            }
            Question question = questionDao.findById(questionId);

            if (type.contains("Essay")) {
                EssayAnswer essayAnswer = new EssayAnswer();
                essayAnswer.setAnswer(collected.getAsJsonArray().get(0).toString());

                a = essayAnswer;
            }

            if (type.contains("Lin")) {
                LinAnswer linAnswer = new LinAnswer();

                List<Value> values = valueDao.findAll();
                Long valueId = null;
                for (Value value : values) {
                    if (value.getValue() == collected.getAsJsonArray().get(0).getAsInt()) {
                        valueId = value.getId();
                    }
                }
                Value value = valueDao.findById(valueId);
                linAnswer.setChoice(value);

                a = linAnswer;
            }

            if (type.contains("Multiple")) {
                MultipleAnswer multipleAnswer = new MultipleAnswer();

                List<Choice> choices = choiceDao.findAll();
                Long choiceId = null;
                for (Choice choice : choices) {
                    if (choice.getText().equals(collected.getAsJsonArray().get(0).getAsString())) {
                        choiceId = choice.getId();
                    }
                }
                Choice choice = choiceDao.findById(choiceId);
                multipleAnswer.setChoice(choice);

                a = multipleAnswer;
            }

            List<Answer> answersDB = this.findAll();
            Long lastId = 0L;

            for (Answer answerDB: answersDB){
                if (lastId < answerDB.getId()){
                    lastId = answerDB.getId();
                }
            }
            System.out.println(lastId);
            a.setId(lastId + i + 1);
            a.setQuestion(question);
            a.setQuestionnaire(questionnaire);

            answers.add(a);
        }
        return answers;
    }
}
