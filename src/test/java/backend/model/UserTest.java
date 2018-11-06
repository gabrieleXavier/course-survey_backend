package backend.model;


import backend.model.user.User;
import backend.dao.UserDao;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserTest {
    private User e1, e2, e3;

    private static Long id1 = new Long(0);
    private static Long id2 = new Long(1);

    @Before
    public void setUp() {
        e1 = new User(id1);
        e2 = new User(id2);
        e3 = new User(id1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullID() {
        User u = new User(null);
        UserDao userDao = new UserDao();
        userDao.save(u);
    }

    @Test
    public void testEquals() {
        assertEquals(e1.getId(), e1.getId());
        assertEquals(e1.getId(), e3.getId());
        assertEquals(e3.getId(), e3.getId());
    }

    @Test
    public void testHashCode() {
        assertEquals(e1.hashCode(), e1.hashCode());
        assertEquals(e2.hashCode(), e2.hashCode());
        assertEquals(e3.hashCode(), e3.hashCode());
    }

    @Test
    public void testNotEquals() {
        assertNotEquals(e1.getId(), e2.getId());
        assertNotEquals(e3.getId(), e2.getId());

        assertNotEquals(e1.getId(), null);
        assertNotEquals(e1.getId(), "Ciao");
    }
}
