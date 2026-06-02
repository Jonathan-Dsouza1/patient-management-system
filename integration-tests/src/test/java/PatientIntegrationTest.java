import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PatientIntegrationTest {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnPatientsWithValidToken() {
        String loginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
            """;

        String token = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("token");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/patients")
                .then()
                .statusCode(200)
                .body("patients", notNullValue());
    }

    @Test
    public void shouldReturnOKOnCreatePatientWithValidToken() {
        String loginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
            """;

        String token = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("token");

        String email = "testuser" + System.currentTimeMillis() + "@test.com";

        String patientDetails = """
                {
                    "name": "TestUser",
                    "email": "%s",
                    "address": "123 main street",
                    "dateOfBirth": "1995-05-10",
                    "registeredDate": "2026-06-02"
                }
            """.formatted(email);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(patientDetails)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("TestUser"))
                .body("email", equalTo(email));
    }

    @Test
    public void shouldReturnOKOnUpdatePatientWithValidToken() {
        String loginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
            """;

        String token = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("token");

        String email = "testuser" + System.currentTimeMillis() + "@test.com";

        String patientDetails = """
                {
                    "name": "TestUser",
                    "email": "%s",
                    "address": "123 main street",
                    "dateOfBirth": "1995-05-10",
                    "registeredDate": "2026-06-02"
                }
            """.formatted(email);

        String patientId = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(patientDetails)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id");

        String updatedEmail = "testuser" + System.currentTimeMillis() + "@test.com";

        String updatePatientDetails = """
                {
                    "name": "UpdatedTestUser",
                    "email": "%s",
                    "address": "123 main street",
                    "dateOfBirth": "1996-06-11"
                }
            """.formatted(updatedEmail);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(updatePatientDetails)
                .when()
                .put("/api/patients/" + patientId)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("UpdatedTestUser"))
                .body("email", equalTo(updatedEmail));
    }

    @Test
    public void shouldReturnOKOnDeletePatientWithValidToken() {
        String loginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                }
            """;

        String token = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("token");

        String email = "testuser" + System.currentTimeMillis() + "@test.com";

        String patientDetails = """
                {
                    "name": "TestUser",
                    "email": "%s",
                    "address": "123 main street",
                    "dateOfBirth": "1995-05-10",
                    "registeredDate": "2026-06-02"
                }
            """.formatted(email);

        String patientId = given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(patientDetails)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/api/patients/" + patientId)
                .then()
                .statusCode(204);
    }
}
