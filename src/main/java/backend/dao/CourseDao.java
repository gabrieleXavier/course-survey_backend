package backend.dao;

import backend.model.course.Course;
import backend.model.questionnaire.Questionnaire;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

public class CourseDao implements Dao<Course, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Course findById(Long courseID) {
        try {
            return entityManager.find(Course.class, courseID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Course> findAll() {
        List<Course> result = entityManager.createQuery("SELECT c FROM Course c").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    @Transactional
    public void save(Course course) {
        if (findById(course.getId()) == null) {
            if (course.getId() != null) {
                entityManager.persist(course);
            } else {
                throw new IllegalArgumentException("id cannot be null");
            }
        } else {
            entityManager.merge(course);
        }
    }

    public JsonElement courseToJson(Course c) {

        GsonBuilder gsonBuilder = new GsonBuilder();

        Type choicesListType = new TypeToken<List<Questionnaire>>() {
        }.getType();

        JsonSerializer<List<Questionnaire>> serializer = (List<Questionnaire> src, Type typeOfSrc, JsonSerializationContext context) -> {
            JsonArray jsonQuestionnaire = new JsonArray();

            for (Questionnaire questionnaire : src) {
                jsonQuestionnaire.add("" + questionnaire.getId());
            }

            return jsonQuestionnaire;
        };

        gsonBuilder.registerTypeAdapter(choicesListType, serializer);

        Gson customGson = gsonBuilder.create();
        JsonElement customJSON = customGson.toJsonTree(c);

        return customJSON;
    }

}
