package backend.model;


import backend.model.questionnaire.Questionnaire;
import backend.dao.QuestionnaireDao;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class QuestionnaireTest {
    private Questionnaire e1, e2, e3;

    private static Long id1 = new Long(0);
    private static Long id2 = new Long(1);

    @Before
    public void setUp() {
        e1 = new Questionnaire(id1);
        e2 = new Questionnaire(id2);
        e3 = new Questionnaire(id1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNullID() {
        Questionnaire q = new Questionnaire(null);
        QuestionnaireDao questionnaireDao = new QuestionnaireDao();
        questionnaireDao.save(q);
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
