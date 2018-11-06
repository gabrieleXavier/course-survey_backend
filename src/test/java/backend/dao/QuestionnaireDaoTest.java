package backend.dao;


import backend.model.questionnaire.Questionnaire;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runners.model.InitializationError;

import static junit.framework.TestCase.assertEquals;

public class QuestionnaireDaoTest extends DaoTest {
    private QuestionnaireDao questionnaireDao;
    private Questionnaire questionnaire, anotherQuestionnaire;

    @Override
    protected void init() throws InitializationError {
        questionnaire = new Questionnaire(new Long(100));
        questionnaire.setProfessor("Marco Fau");

        anotherQuestionnaire = new Questionnaire(new Long(101));
        anotherQuestionnaire.setProfessor("Gabriele Pas");

        questionnaireDao = new QuestionnaireDao();

        try {
            FieldUtils.writeField(questionnaireDao, "entityManager", entityManager, true);
        } catch (IllegalAccessException e) {
            throw new InitializationError(e);
        }
    }

    @Test
    public void testSave() {
        questionnaireDao.save(anotherQuestionnaire);

        assertEquals(anotherQuestionnaire.getProfessor(), entityManager
                .createQuery("from Questionnaire where id =:ID", Questionnaire.class)
                .setParameter("ID", anotherQuestionnaire.getId())
                .getSingleResult().getProfessor());

    }

    @Test
    public void testSaveUpdate() {
        questionnaire = entityManager.find(Questionnaire.class, anotherQuestionnaire.getId());
        String changedQuestionnaire = "Paolo Cioni";
        questionnaire.setProfessor(changedQuestionnaire);

        questionnaireDao.save(questionnaire);

        assertEquals(changedQuestionnaire, entityManager
                .createQuery("from Questionnaire where id =:ID", Questionnaire.class)
                .setParameter("ID", questionnaire.getId())
                .getSingleResult()
                .getProfessor());

    }

    @Test
    public void testFindById() {
        questionnaireDao.save(questionnaire);

        Questionnaire result = questionnaireDao.findById(questionnaire.getId());

        assertEquals(questionnaire.getId(), result.getId());
        assertEquals(questionnaire.getProfessor(), result.getProfessor());
    }
}
