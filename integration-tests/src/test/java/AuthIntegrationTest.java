import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

//These integrations test are going to separate from the internals of the microservice architectures and
//does not run inside of a docker context
public class AuthIntegrationTest {

    //Below Test will happen before all other tests
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    //Every company have different convention of naming the test methods
    @Test
    public void shouldReturnOKWithValidToken() {
        //1. Arrange
        String loginPayLoad = """
                    {
                        "email":"testuser@test.com",
                        "password":"password123"
                    }
                """;
        //2. Act
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(loginPayLoad)
                .when()
                .post("/auth/login")
        //3. assert
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        System.out.println("Generated Token : " + response.jsonPath().getString("token"));
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        //1. Arrange
        String loginPayLoad = """
                    {
                        "email":"testinvaliduser@test.com",
                        "password":"wrongpassword123"
                    }
                """;
        //2. Act
        RestAssured.given()
                .contentType("application/json")
                .body(loginPayLoad)
                .when()
                .post("/auth/login")
                //3. assert
                .then()
                .statusCode(401);
    }
}
