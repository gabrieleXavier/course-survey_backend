package backend.dao;

import backend.model.measurment.Value;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ValueDao implements Dao<Value, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Value findById(Long valueID) {
        try {
            return entityManager.find(Value.class, valueID);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Value> findAll() {
        List<Value> result = entityManager.createQuery("SELECT q FROM Value q").getResultList();

        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @Override
    public void save(Value value) {
        if (findById(value.getId()) == null) {
            if (value.getId() != null) {
                entityManager.persist(value);
            } else {
                throw new IllegalArgumentException("id cannot be null");
            }
        } else {
            entityManager.merge(value);
        }
    }
}
