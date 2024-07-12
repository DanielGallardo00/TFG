package com.woola.woola;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class LoginRestControllerTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8443;
        RestAssured.basePath = "/api/auth";
    }

    @Test
    public void testLoginSuccess() {
        String loginJson = "{ \"username\": \"user\", \"password\": \"password\" }";

        given()
            .contentType(ContentType.JSON)
            .body(loginJson)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("status", equalTo("SUCCESS"));
    }

    @Test
    public void testLoginFailure() {
        String loginJson = "{ \"username\": \"wronguser\", \"password\": \"wrongpassword\" }";

        given()
            .contentType(ContentType.JSON)
            .body(loginJson)
        .when()
            .post("/login")
        .then()
            .statusCode(405)
            .contentType(ContentType.JSON)
            .body("status", equalTo("ERROR"));
    }

    @Test
    public void testLogoutSuccess() {
        given()
            .cookie("accessToken", "validAccessToken") // token necesario
            .cookie("refreshToken", "validRefreshToken")
        .when()
            .post("/logout")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("status", equalTo("SUCCESS"));
    }

    @Test
    public void testLogoutWithoutCookies() {
        given()
        .when()
            .post("/logout")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("status", equalTo("SUCCESS"));
    }
}
