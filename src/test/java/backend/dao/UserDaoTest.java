package backend.dao;


import backend.model.user.User;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import static junit.framework.TestCase.assertEquals;

public class UserDaoTest extends DaoTest {
    private UserDao userDao;
    private User user, anotherUser;

    @Override
    protected void init() throws InitializationError {
        user = new User(new Long(100));
        user.setName("Marco Fau");

        anotherUser = new User(new Long(101));
        anotherUser.setName("Gabriele Pas");

        userDao = new UserDao();

        try {
            FieldUtils.writeField(userDao, "entityManager", entityManager, true);
        } catch (IllegalAccessException e) {
            throw new InitializationError(e);
        }
    }

    @Test
    public void testSave() {
        userDao.save(anotherUser);

        assertEquals(anotherUser.getName(), entityManager
                .createQuery("from User where id =:ID", User.class)
                .setParameter("ID", anotherUser.getId())
                .getSingleResult().getName());

    }

    @Test
    public void testSaveUpdate() {
        user = entityManager.find(User.class, anotherUser.getId());
        String changedUser = "Paolo Cioni";
        user.setName(changedUser);

        userDao.save(user);

        assertEquals(changedUser, entityManager
                .createQuery("from User where id =:ID", User.class)
                .setParameter("ID", user.getId())
                .getSingleResult()
                .getName());
    }

    @Test
    public void testFindById() {
        userDao.save(user);

        User result = userDao.findById(user.getId());

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
    }
}
