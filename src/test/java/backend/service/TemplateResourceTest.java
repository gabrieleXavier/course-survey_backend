package backend.service;

import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class TemplateResourceTest extends ServiceTest {
    @Test
    public void getTemplate() {
        get("/template?matriculation={matriculation}", "PROF000").
                then().
                statusCode(200).
                body("templates.id", hasItems(7)).
                body("templates.title", hasItems("Course survey template")).
                body("templates.creator", hasItems("Mario Rossi")).
                body("templates.questions.questionType", equalTo(Arrays.asList(Arrays.asList("essay")))).
                body("templates.questions.questionId", equalTo(Arrays.asList(Arrays.asList(1))));
    }

    @Test
    public void postTemplate() {
        String templateJson = "{\"id\":69.88421484201712,\"title\":\"Course survey template\"," +
                "\"creator\":\"Mario Rossi\",\"public\":true," +
                "\"questions\":[{\"question\":\"Report here suggestions about this course\"," +
                "\"questionType\":\"essay\",\"max_len\":10}]}";

        given().
                contentType("application/json").
                body(templateJson).
                when().post("/template").then().
                statusCode(204);
    }
}
