package backend.dao;

import backend.model.measurment.Choice;
import backend.model.question.Question;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao implements Dao<Question, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Question findById(Long questionID) {
        try {
            return entityManager.find(Question.class, questionID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Question> findAll() {
        List<Question> result = entityManager.createQuery("SELECT q FROM Question q").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    public void save(Question question) {
        if (findById(question.getId()) == null) {
            if (question.getId() != null) {
                entityManager.persist(question);
            } else {
                throw new IllegalArgumentException("id cannot be null");
            }
        } else {
            entityManager.merge(question);
        }
    }

    public JsonElement questionToJson(Question q) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = new Gson();

        JsonElement customJson = null;

        if (q.getClass().toString().contains("MultipleQuestion")) {
            Type choicesListType = new TypeToken<List<Choice>>() {
            }.getType();

            JsonSerializer<List<Choice>> serializer = (List<Choice> src, Type typeOfSrc, JsonSerializationContext context) -> {
                JsonArray jsonChoices = new JsonArray();

                for (Choice choice : src) {
                    jsonChoices.add("" + choice.getText());
                }

                return jsonChoices;
            };

            gsonBuilder.registerTypeAdapter(choicesListType, serializer);

            Gson customGson = gsonBuilder.create();
            customJson = customGson.toJsonTree(q);

        }

        if (q.getClass().toString().contains("LinQuestion")) {

            customJson = gson.toJsonTree(q);

            JsonArray valuesJ = customJson.getAsJsonObject().getAsJsonArray("choices");
            List<Integer> values = new ArrayList<>();

            for (int i = 0; i < valuesJ.size(); i++) {
                values.add(Integer.parseInt(valuesJ.get(i).getAsJsonObject().get("value").toString()));
            }

            int max = 0;
            int min = 1000;

            for (int i = 0; i < values.size(); i++) {
                if (i > max) {
                    max = i;
                }
                if (i < min) {
                    min = i;
                }
            }

            customJson.getAsJsonObject().addProperty("max", max);
            customJson.getAsJsonObject().addProperty("min", min);
            customJson.getAsJsonObject().remove("choices");
        }

        if (q.getClass().toString().contains("EssayQuestion")) {
            customJson = gson.toJsonTree(q);
        }

        return customJson;
    }

}
