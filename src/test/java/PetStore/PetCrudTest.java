package PetStore;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import Base.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PetCrudTest extends BaseTest {
    Faker faker = new Faker();
    int petId;

    @Test(priority = 1, groups = {"positiveTests"})
    public void createPetPositive() {
        Map<String, Object> pet = new HashMap<>();
        petId = faker.number().numberBetween(1000, 9999);
        pet.put("id", petId);
        pet.put("name", faker.animal().name());
        pet.put("status", "available");

        given()
                .spec(reqSpec)
                .body(pet)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("status", equalTo("available"));
    }

    @Test(priority = 2, groups = {"negativeTests"})
    public void createPetNegative() {
        String invalidJson = "{\"status\":\"invalidStatus\",\"id\":}";

        given()
                .spec(reqSpec)
                .body(invalidJson)
                .when()
                .post("/pet")
                .then()
                .log().all()
                .statusCode(400);
    }


    @Test(priority = 3, dependsOnMethods = "createPetPositive", groups = {"positiveTests"})
    public void getPetByIdPositive() {
        given()
                .spec(reqSpec)
                .when()
                .get("/pet/" + petId)
                .then()
                .statusCode(200)
                .body("id", equalTo(petId));
    }

    @Test(priority = 4, groups = {"negativeTests"})
    public void getPetByIdNegative() {
        int invalidId = faker.number().numberBetween(10000, 99999);

        given()
                .spec(reqSpec)
                .when()
                .get("/pet/" + invalidId)
                .then()
                .statusCode(404)
                .body("message", equalTo("Pet not found"));
    }

    @Test(priority = 5, dependsOnMethods = "createPetPositive", groups = {"positiveTests"})
    public void updatePetPositive() {
        Map<String, Object> updatedPet = new HashMap<>();
        updatedPet.put("id", petId);
        updatedPet.put("name", "UpdatedPet");
        updatedPet.put("status", "sold");

        given()
                .spec(reqSpec)
                .body(updatedPet)
                .when()
                .put("/pet")
                .then()
                .statusCode(200)
                .body("id", equalTo(petId))
                .body("status", equalTo("sold"));
    }

    @Test(priority = 6, groups = {"negativeTests"})
    public void updatePetNegative() {
        String invalidJson = "{\"id\":12345, \"name\":\"InvalidPet\", \"status\": ";

        given()
                .spec(reqSpec)
                .body(invalidJson)
                .when()
                .put("/pet")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test(priority = 7, dependsOnMethods = "createPetPositive", groups = {"positiveTests"})
    public void deletePetPositive() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/pet/" + petId)
                .then()
                .statusCode(200)
                .body("message", equalTo(String.valueOf(petId)));
    }

    @Test(priority = 8, groups = {"negativeTests"})
    public void deletePetNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/pet/invalidId")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", containsString("NumberFormatException"));
    }
}
