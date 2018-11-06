package backend.service;

import org.junit.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;


public class AuthenticationResourceTest extends ServiceTest {
    private String matriculation;

    @Test
    public void getAuthentication() {
        matriculation = "PROF000";
        get("/authentication?matriculation={matriculation}", matriculation).
                then().
                statusCode(200).
                body("role", equalTo("professor")).
                body("user.matriculation", equalTo(matriculation)).
                body("user.name", equalTo("Mario Rossi")).
                body("user.courses", equalTo(Arrays.asList("12")));
    }
}
