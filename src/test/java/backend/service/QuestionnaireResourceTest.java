package backend.service;

import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class QuestionnaireResourceTest extends ServiceTest {

    @Test
    public void getQuestionnaire() {
        get("/questionnaire?id={id}", 5).
                then().
                statusCode(200).
                body("id", equalTo(5)).
                body("title", equalTo("course survey")).
                body("gps", equalTo("false")).
                body("deadline", equalTo("05/07/2018 - 10:00")).
                body("activation", equalTo("05/06/2018 - 10:30")).
                body("courseId", equalTo("12")).
                body("professor", equalTo("Mario Rossi")).
                body("questions.questionType", hasItems("essay", "multiple", "lin")).
                body("questions.questionId", hasItems(1, 2, 3)).
                body("questions.num", hasItems(0, 1, 2));

    }

    @Test
    public void getStatistic() {
        get("/questionnaire/statistic?questionnaire={questionnaire}&number={number}", 5, 1).
                then().
                statusCode(200).
                body("questionnaireId", equalTo(5)).
                body("numberOfTheQuestion", equalTo(1)).
                body("collected", equalTo(Arrays.asList("Italian", "Italian"))).
                body("statistics.statistic", hasItems("histogram")).
                body("statistics.value", hasItems(Arrays.asList(0, 2)));
    }

    @Test
    public void postQuestionnaire() {
        String questionnaireJson = "{\"id\":14.851835594463466,\"title\":\"Test Post\"," +
                "\"deadline\":\"21/10/2018 - 23:0\",\"activation\":\"31/9/2018 - 9:0\",\"professor\":\"Mario Rossi\"," +
                "\"course\":12,\"public\":true,\"questions\":[{\"question\":\"Question post\"," +
                "\"questionType\":\"essay\",\"max_len\":10}],\"gps\":\"false\"}";

        given().
                contentType("application/json").
                body(questionnaireJson).
                when().post("/questionnaire").then().
                statusCode(204);
    }
}
