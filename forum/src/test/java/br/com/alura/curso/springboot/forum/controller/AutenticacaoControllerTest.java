package br.com.alura.curso.springboot.forum.controller;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasLength;
import static org.springframework.http.HttpStatus.*;

public class AutenticacaoControllerTest {
    @BeforeClass
    public void config() {
        baseURI ="http://localhost:8082";
    }

    @Test
    void autentica_usuario_with_body_then_return_success() {

        given()
                .contentType(JSON)
                .body("{\"login\":\"aluno@email.com\",\"senha\":\"123456\"}")
                .when()
                    .post("/auth")
                .then()
                    .statusCode(OK.value())
                    .contentType(JSON)
                    .body("tipo",containsString("Bearer"))
                    .body("token",hasLength(157))
                .assertThat()
        .body(matchesJsonSchemaInClasspath("schemas/schema_autenticacao.json"));
    }
    @Test
    void autentica_usuario_without_body_then_return_bad_request() {
        given()
                .contentType(JSON)
                .when()
                .post("/auth")
                .then()
                .statusCode(BAD_REQUEST.value())
                .contentType(JSON)
                .assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/schema_autenticacao_bad_request.json"));
    }

    @Test
    void autentica_usuario_with_body_then_return_bad(){
        given()
                .contentType(JSON)
                .when()
                .post("/auths")
                .then()
                    .statusCode(FORBIDDEN.value())
                    .contentType(JSON)
                    ;
    }


}
