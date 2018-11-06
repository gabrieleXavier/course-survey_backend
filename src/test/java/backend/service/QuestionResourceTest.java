package backend.service;

import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class QuestionResourceTest extends ServiceTest {

    @Test
    public void getQuestion() {
        get("/question?type={type}&id={id}", "LinQuestion", 3).
                then().
                statusCode(200).
                body("id", equalTo(3)).
                body("question", equalTo("How do you judge the laboratory lessons?")).
                body("max", equalTo(6)).
                body("min", equalTo(0));
    }

    @Test
    public void postEssayAnswer() {
        String answerJson = "[{\"questionnaireId\": \"5\",\n" +
                "\"questionText\": \"Report here suggestions about this course\",\n" +
                "\"collected\": [\"The course is not interesting\"]}]";

        given().
                contentType("application/json").
                body(answerJson).
                when().post("/question/answer").then().
                statusCode(204);
    }

    @Test
    public void postLinAnswer() {
        String answerJson = "[{\"questionnaireId\": \"5\",\n" +
                "\"questionText\": \"How do you judge the laboratory lessons?\",\n" +
                "\"collected\": [\"5\"]}]";

        given().
                contentType("application/json").
                body(answerJson).
                when().post("/question/answer").then().
                statusCode(204);
    }

    @Test
    public void postMultipleAnswer() {
        String answerJson = "[{\"questionnaireId\": \"5\",\n" +
                "\"questionText\": \"Which language do you prefer?\",\n" +
                "\"collected\": [\"Italian\"]}]";

        given().
                contentType("application/json").
                body(answerJson).
                when().post("/question/answer").then().
                statusCode(204);
    }
}
