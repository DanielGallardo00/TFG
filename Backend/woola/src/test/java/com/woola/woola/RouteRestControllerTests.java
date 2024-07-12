package com.woola.woola;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class RouteRestControllerTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8443;
        RestAssured.basePath = "/api/routes";
    }

    @Test
    public void testGetRouteSuccess() {
        given()
        .when()
            .get("/{id}", 1)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1));
    }

    @Test
    public void testGetRouteNotFound() {
        given()
        .when()
            .get("/{id}", 999)
        .then()
            .statusCode(404);
    }

    @Test
    public void testDeleteRouteSuccess() {
        given()
            .auth().basic("admin", "password") // Asegúrate de que el usuario tenga los permisos necesarios
        .when()
            .delete("/{id}", 1)
        .then()
            .statusCode(200);
    }

    @Test
    public void testDeleteRouteNotFound() {
        given()
            .auth().basic("admin", "password")
        .when()
            .delete("/{id}", 999)
        .then()
            .statusCode(404);
    }

    @Test
    public void testCreateRouteSuccess() throws SQLException, IOException {
        // Asegúrate de tener un archivo de imagen en la ruta especificada
        byte[] imageBytes = getClass().getResourceAsStream("/path/to/image.jpg").readAllBytes();

        given()
            .auth().basic("user", "password") // Usuario con rol BASE
            .contentType("multipart/form-data")
            .multiPart("newImage", "image.jpg", imageBytes)
            .formParam("name", "Test Route")
            .formParam("description", "Test Description")
        .when()
            .post("/")
        .then()
            .statusCode(201)
            .header("Location", containsString("/api/routes/"));
    }

    @Test
    public void testEditRouteSuccess() {
        given()
            .auth().basic("admin", "password") // Asegúrate de que el usuario tenga los permisos necesarios
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Updated Route\",\"description\":\"Updated Description\"}")
        .when()
            .put("/{id}", 1)
        .then()
            .statusCode(200)
            .body("name", equalTo("Updated Route"))
            .body("description", equalTo("Updated Description"));
    }

    @Test
    public void testEditRouteNotFound() {
        given()
            .auth().basic("admin", "password")
            .contentType(ContentType.JSON)
            .body("{\"name\":\"Updated Route\",\"description\":\"Updated Description\"}")
        .when()
            .put("/{id}", 999)
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetAllRoutes() {
        given()
        .when()
            .get("/all")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testLikeRouteSuccess() {
        given()
            .auth().basic("user", "password") // Asegúrate de que el usuario esté registrado
        .when()
            .post("/like/{id}", 1)
        .then()
            .statusCode(200);
    }

    @Test
    public void testDislikeRouteSuccess() {
        given()
            .auth().basic("user", "password") // Asegúrate de que el usuario esté registrado
        .when()
            .post("/dislike/{id}", 1)
        .then()
            .statusCode(200);
    }

    @Test
    public void testSetImageSuccess() throws SQLException, IOException {
        byte[] imageBytes = getClass().getResourceAsStream("/path/to/image.jpg").readAllBytes();

        given()
            .auth().basic("user", "password") // Usuario con rol BASE
            .contentType("multipart/form-data")
            .multiPart("newImage", "image.jpg", imageBytes)
        .when()
            .put("/image/{id}", 1)
        .then()
            .statusCode(200);
    }

    @Test
    public void testGetImageSuccess() {
        given()
        .when()
            .get("/image/{id}", 1)
        .then()
            .statusCode(200)
            .header("Content-Type", "image/jpeg");
    }

    @Test
    public void testGetImageNotFound() {
        given()
        .when()
            .get("/image/{id}", 999)
        .then()
            .statusCode(404);
    }
}
