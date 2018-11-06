package backend.dao;


import backend.model.question.EssayQuestion;
import backend.model.question.Question;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import static junit.framework.TestCase.assertEquals;

public class QuestionDaoTest extends DaoTest {

    private QuestionDao questionDao;
    private Question question, anotherQuestion;

    @Override
    protected void init() throws InitializationError {
        question = new EssayQuestion(new Long(100));
        question.setQuestionText("Che ore sono?");

        anotherQuestion = new EssayQuestion(new Long(101));
        anotherQuestion.setQuestionText("Che giorno è?");

        questionDao = new QuestionDao();

        try {
            FieldUtils.writeField(questionDao, "entityManager", entityManager, true);
        } catch (IllegalAccessException e) {
            throw new InitializationError(e);
        }
    }

    @Test
    public void testSave() {
        questionDao.save(anotherQuestion);

        assertEquals(anotherQuestion.getQuestionText(), entityManager
                .createQuery("from Question where id =:ID", Question.class)
                .setParameter("ID", anotherQuestion.getId())
                .getSingleResult().getQuestionText());

    }

    @Test
    public void testSaveUpdate() {
        question = entityManager.find(Question.class, anotherQuestion.getId());
        String changedQuestion = "Che anno è?";
        question.setQuestionText(changedQuestion);

        questionDao.save(question);

        assertEquals(changedQuestion, entityManager
                .createQuery("from Question where id =:ID", Question.class)
                .setParameter("ID", question.getId())
                .getSingleResult()
                .getQuestionText());

    }

    @Test
    public void testFindById() {
        questionDao.save(question);

        Question result = questionDao.findById(question.getId());

        assertEquals(question.getId(), result.getId());
        assertEquals(question.getQuestionText(), result.getQuestionText());
    }
}
