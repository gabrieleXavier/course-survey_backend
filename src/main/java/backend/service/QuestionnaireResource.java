package backend.service;

import backend.dao.AnswerDao;
import backend.dao.CourseDao;
import backend.dao.QuestionnaireDao;
import backend.model.course.Course;
import backend.model.questionnaire.Questionnaire;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.ws.rs.*;

@Path("/questionnaire")
@Produces({"application/json"})
public class QuestionnaireResource {

    @Inject
    private QuestionnaireDao questionnaireDao;

    @Inject
    private CourseDao courseDao;

    @Inject
    private AnswerDao answerDao;

    @GET
    public String getQuestionnaire(@QueryParam("id") Long id) {
        Questionnaire questionnaire = questionnaireDao.findById(id);
        JsonElement questionnaireJson = questionnaireDao.questionnaireToJson(questionnaire);

        return questionnaireJson.toString();
    }

    @POST
    public void saveQuestionnaire(String jsonQuestionnaire){
        Questionnaire questionnaire = questionnaireDao.jsonToQuestionnaire(jsonQuestionnaire);

        Course course = courseDao.findById(new Long(questionnaire.getCourseId()));
        course.getQuestionnaires().add(questionnaire);

        questionnaireDao.save(questionnaire);
        courseDao.save(course);
    }

    @GET
    @Path("/statistic")
    public String getStatisitc(@QueryParam("questionnaire") Long questionnaire, @QueryParam("number") Integer number) {
        JsonElement statisticJson = answerDao.getStatistics(questionnaire, number);

        return statisticJson.toString();
    }
}
