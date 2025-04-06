import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PatientIntegrationTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnPatientsWithValidToken() {
        String loginPayLoad = """
                    {
                        "email" : "testuser@test.com",
                        "password" : "password123"
                    }
                """;

        String token = given()
                .contentType("application/json")
                .body(loginPayLoad)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("token");

      //We get the token value from the above test and then that token will be used to perform the below test
      RestAssured.given()
              .header("Authorization", "Bearer " + token)
              .when()
              .get("/api/patients")
              .then()
              .statusCode(200)
              .body("patients", notNullValue());
    }
}
