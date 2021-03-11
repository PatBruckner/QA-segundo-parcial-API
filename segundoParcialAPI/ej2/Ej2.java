package segundoParcial.ej2;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Ej2{
    private Response response;
    private String tokenValue;

    @Before
    public void beforeGetToken(){
        response =  given().
                auth().
                preemptive().
                basic("patrick@upb.com","1234").
                contentType(ContentType.JSON).
                log().
                all().
                when().
                get("http://todo.ly/api/authentication/token.json");

        response.then().
                statusCode(200);

        tokenValue=response.then().extract().path("TokenString");
    }

    @Test
    public void verify_crud_project(){


        JSONObject body = new JSONObject();
        body.put("Content","RESTAssured");
        body.put("Icon","4");

        // Creacion

        response =  given().
                header("Token",this.tokenValue).
                contentType(ContentType.JSON).
                body(body.toString()).
                log().
                all().
                when().
                post("http://todo.ly/api/projects.json");

        response.then().
                statusCode(200).
                body("Content", equalTo("RESTAssured")).
                body("Icon", equalTo(4)).
                body("Deleted", equalTo(false)).
                log().
                all();

        // extraer el valor de una propiedad : Id
        int idProject=response.then().extract().path("Id");

        // Actualizacion
        body.put("Content","RESTAssured UPDATE");
        response =  given().
                header("Token",this.tokenValue).
                contentType(ContentType.JSON).
                body(body.toString()).
                log().
                all().
                when().
                put("http://todo.ly/api/projects/"+idProject+".json");

        response.then().
                statusCode(200).
                body("Content", equalTo("RESTAssured UPDATE")).
                body("Icon", equalTo(4)).
                body("Deleted", equalTo(false)).
                log().
                all();

        // Get
        response =  given().
                header("Token",this.tokenValue).
                contentType(ContentType.JSON).
                log().
                all().
                when().
                get("http://todo.ly/api/projects/"+idProject+".json");

        response.then().
                statusCode(200).
                body("Content", equalTo("RESTAssured UPDATE")).
                body("Icon", equalTo(4)).
                body("Deleted", equalTo(false)).
                log().
                all();

        // Borrado
        response =  given().
                header("Token",this.tokenValue).
                contentType(ContentType.JSON).
                log().
                all().
                when().
                delete("http://todo.ly/api/projects/"+idProject+".json");

        response.then().
                statusCode(200).
                body("Content", equalTo("RESTAssured UPDATE")).
                body("Icon", equalTo(4)).
                body("Deleted", equalTo(true)).
                log().
                all();
    }

}