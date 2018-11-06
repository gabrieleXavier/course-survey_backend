package backend.dao;

import backend.model.measurment.Choice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ChoiceDao implements Dao<Choice, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Choice findById(Long choiceID) {
        try {
            return entityManager.find(Choice.class, choiceID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Choice> findAll() {
        List<Choice> result = entityManager.createQuery("SELECT q FROM Choice q").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    public void save(Choice choice) {
        if (findById(choice.getId()) == null) {
            if (choice.getId() != null) {
                entityManager.persist(choice);
            } else {
                throw new IllegalArgumentException("id cannot be null");
            }
        } else {
            entityManager.merge(choice);
        }
    }
}
