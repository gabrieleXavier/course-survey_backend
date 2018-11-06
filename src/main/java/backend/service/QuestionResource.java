package backend.service;

import backend.dao.AnswerDao;
import backend.dao.QuestionDao;
import backend.model.answer.Answer;
import backend.model.answer.EssayAnswer;
import backend.model.question.Question;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/question")
@Produces({"application/json"})
public class QuestionResource {

    @Inject
    private QuestionDao questionDao;
    @Inject
    private AnswerDao answerDao;

    @GET
    public String getQuestion(@QueryParam("type") String type, @QueryParam("id") Long id) {
        Question question = questionDao.findById(id);
        JsonElement questionJson = questionDao.questionToJson(question);

        return questionJson.toString();
    }

    @POST
    @Path("/answer")
    public void saveAnswer(String jsonAnswer){

        List<Answer> answers = answerDao.jsonToAnswer(jsonAnswer);
        for(Answer answer: answers){
            answerDao.save(answer);
        }

    }
}
