package backend.dao;

import backend.model.user.Role;
import backend.model.user.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class UserDao implements Dao<User, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findById(Long userID) {
        try {
            return entityManager.find(User.class, userID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public User findByMatriculation(String matriculation) {
        List<User> result = entityManager.createQuery("SELECT u FROM User u where u.matriculation =" + "'" + matriculation + "'").getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<User> findAll() {
        List<User> result = entityManager.createQuery("SELECT u FROM User u").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    public void save(User user) {
        if (findById(user.getId()) == null) {
            if (user.getId() != null) {
                entityManager.persist(user);
            } else {
                throw new IllegalArgumentException("id cannot be null");
            }
        } else {
            entityManager.merge(user);
        }
    }

    public JsonElement authenticationToJson(User u) {

        Gson gson = new Gson();

        JsonElement jsonElement = gson.toJsonTree(u);

        if (u.getRole() == Role.STUDENT) {

            JsonArray valuesJ = jsonElement.getAsJsonObject().getAsJsonArray("courses");
            JsonArray questionnaires = new JsonArray();

            for (int i = 0; i < valuesJ.size(); i++) {
                JsonArray je = valuesJ.get(i).getAsJsonObject().getAsJsonArray("questionnaires");
                for (int j = 0; j < je.size(); j++) {
                    questionnaires.add(je.get(j).getAsJsonObject().get("id").toString());
                }
            }

            jsonElement.getAsJsonObject().add("questionnaires", questionnaires);
            jsonElement.getAsJsonObject().remove("role");
            jsonElement.getAsJsonObject().remove("id");
            jsonElement.getAsJsonObject().remove("password");
            jsonElement.getAsJsonObject().remove("courses");

        }

        if (u.getRole() == Role.PROFESSOR) {

            JsonArray valuesJ = jsonElement.getAsJsonObject().getAsJsonArray("courses");
            JsonArray corses = new JsonArray();

            for (int i = 0; i < valuesJ.size(); i++) {
                corses.add(valuesJ.get(i).getAsJsonObject().get("code").toString());
            }

            jsonElement.getAsJsonObject().remove("role");
            jsonElement.getAsJsonObject().remove("id");
            jsonElement.getAsJsonObject().remove("password");
            jsonElement.getAsJsonObject().remove("courses");
            jsonElement.getAsJsonObject().add("courses", corses);

        }

        JsonObject auth = new JsonObject();

        auth.addProperty("role", u.getRole().toString().toLowerCase());
        auth.add("user", jsonElement);

        return auth;
    }

}
