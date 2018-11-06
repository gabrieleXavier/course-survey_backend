package backend.dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, Id extends Serializable> {

    List<T> findAll();
    T findById(Id id);
    void save(T t);

}