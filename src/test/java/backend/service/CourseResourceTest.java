package backend.service;

import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

public class CourseResourceTest extends ServiceTest{

    @Test
    public void getCourse() {
        get("/course?code={code}", 12).
                then().
                statusCode(200).
                body("code", equalTo(12)).
                body("name", equalTo("Deep Networks")).
                body("cfu", equalTo(9)).
                body("year", equalTo("2017/2018")).
                body("degree", equalTo("Master")).
                body("active", equalTo("true")).
                body("questionnaires", equalTo(Arrays.asList("5")));
    }
}
