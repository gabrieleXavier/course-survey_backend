package backend.dao;


import backend.model.answer.Answer;
import backend.model.answer.EssayAnswer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import static junit.framework.TestCase.assertEquals;

public class AnswerDaoTest extends DaoTest {
    private AnswerDao answerDao;
    private Answer answer, anotherAnswer;

    @Override
    protected void init() throws InitializationError {

        answer = new EssayAnswer(new Long(100));
        ((EssayAnswer) answer).setAnswer("Answer");
        anotherAnswer = new EssayAnswer(new Long(101));
        ((EssayAnswer) anotherAnswer).setAnswer("Another answer");

        answerDao = new AnswerDao();

        try {
            FieldUtils.writeField(answerDao, "entityManager", entityManager, true);
        } catch (IllegalAccessException e) {
            throw new InitializationError(e);
        }
    }

    @Test
    public void testSave() {
        answerDao.save(anotherAnswer);

        assertEquals(anotherAnswer.getId(), entityManager
                .createQuery("from Answer where id =:ID", Answer.class)
                .setParameter("ID", anotherAnswer.getId())
                .getSingleResult().getId());

    }

    @Test
    public void testFindById() {
        answerDao.save(answer);
        Answer result = answerDao.findById(answer.getId());

        assertEquals(answer.getId(), result.getId());
        assertEquals(answer.getId(), result.getId());
    }
}
