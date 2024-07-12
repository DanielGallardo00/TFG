package com.woola.woola;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserRestControllerTests {
    
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8443;
        RestAssured.basePath = "/api/users";
    }

    @Test
    public void testGetProfileByAdmin() {
        given()
            .auth().basic("admin", "password")
        .when()
            .get("/admin/{id}", 1)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(1));
    }

    @Test
    public void testGetCurrentUser() {
        given()
            .auth().basic("user", "password")
        .when()
            .get("/me")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("username", equalTo("user"));
    }

    @Test
    public void testModifyCurrentUser() {
        given()
            .auth().basic("user", "password")
            .contentType(ContentType.JSON)
            .body("{ \"newName\": \"newUserName\", \"newEmail\": \"newemail@example.com\" }")
        .when()
            .patch("/me")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("username", equalTo("newUserName"))
            .body("email", equalTo("newemail@example.com"));
    }

    @Test
    public void testRegisterUser() {
        String userJson = "{ \"username\": \"newuser\", \"password\": \"password\", \"email\": \"newuser@example.com\", \"rol\": \"BASE\" }";

        given()
            .contentType(ContentType.JSON)
            .body(userJson)
        .when()
            .post("/")
        .then()
            .statusCode(201)
            .header("Location", containsString("/api/users/admin/"));
    }

    @Test
    public void testModifyPassword() {
        String passwordJson = "{ \"oldPassword\": \"oldpassword\", \"newPassword\": \"newpassword\" }";

        given()
            .auth().basic("user", "password")
            .contentType(ContentType.JSON)
            .body(passwordJson)
        .when()
            .patch("/me/password")
        .then()
            .statusCode(200);
    }

    @Test
    public void testDeleteUserByAdmin() {
        given()
            .auth().basic("admin", "password")
        .when()
            .delete("/admin/{id}", 2)
        .then()
            .statusCode(200);
    }

    @Test
    public void testGetUserFavoritesByAdmin() {
        given()
            .auth().basic("admin", "password")
        .when()
            .get("/admin/{id}/favorites", 1)
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetMyFavorites() {
        given()
            .auth().basic("user", "password")
        .when()
            .get("/me/favorites")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testAddFavoriteRoute() {
        given()
            .auth().basic("user", "password")
        .when()
            .post("/me/favorites/{id}", 1)
        .then()
            .statusCode(200);
    }

    @Test
    public void testRemoveFavoriteRoute() {
        given()
            .auth().basic("user", "password")
        .when()
            .delete("/me/favorites/{id}", 1)
        .then()
            .statusCode(200);
    }

    @Test
    public void testGetAllUsers() {
        given()
            .auth().basic("admin", "password")
        .when()
            .get("/all")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("size()", greaterThan(0));
    }
}
