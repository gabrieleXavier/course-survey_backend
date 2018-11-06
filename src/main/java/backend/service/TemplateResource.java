package backend.service;

import backend.dao.CourseDao;
import backend.dao.QuestionnaireDao;
import backend.model.course.Course;
import backend.model.questionnaire.Questionnaire;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/template")
@Produces({"application/json"})
public class TemplateResource {

    @Inject
    private QuestionnaireDao questionnaireDao;

    @GET
    public String getTemplate(@QueryParam("matriculation") String matriculation) {
        List<Questionnaire> templates = questionnaireDao.findAllTemplates(matriculation);

        JsonArray templatesJson = new JsonArray();

        for (Questionnaire t : templates) {
            templatesJson.add(questionnaireDao.templateToJson(t));
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("templates", templatesJson);

        return jsonObject.toString();
    }

    @POST
    public void saveTemplate(String jsonTemplate){
        Questionnaire template = questionnaireDao.jsonToTemplate(jsonTemplate);

        questionnaireDao.save(template);
    }
}
