package segundoParcialAPI.ej1;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class EJ1 {
    private Response response;
    private String tokenValue;

    @Test
    public void verify_crud_project() {


        JSONObject body = new JSONObject();
        body.put("Email", "patrick1@gmail.com");
        body.put("Password", "1234");
        body.put("FullName", "Patrick");

        // Creacion

        response = given().
                header("Token", this.tokenValue).
                contentType(ContentType.JSON).
                body(body.toString()).
                log().
                all().
                when().
                post("https://todo.ly/api/user.format");

        response.then().
                statusCode(200).
                body("Email", equalTo("Patrick1@gmail.com")).
                body("Password", equalTo("1234")).
                body("FullName", equalTo("Patrick")).
                log().
                all();

        // extraer el valor de una propiedad : Id
        int idUser = response.then().extract().path("Id");

        // Actualizacion
        body.put("Email", "patrick123@gmail.com");
        response = given().
                header("Token", this.tokenValue).
                contentType(ContentType.JSON).
                body(body.toString()).
                log().
                all().
                when().
                put("https://todo.ly/api/user/0.format" + idUser + ".json");

        response.then().
                statusCode(200).
                body("Email", equalTo("patrick123@gmail.com")).
                log().
                all();
    }
}
