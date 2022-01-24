package insideworld.engine.integration.tests.entities.generated;

import insideworld.engine.actions.keeper.output.OutputTable;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.Map;
import org.apache.groovy.util.Maps;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestRead {

    @Test
    public void test() {
        final Map<String, String> data = new HashMap<>();
        RestAssured.given().body("{\"some\": \"TestSome\"}")
            .contentType(ContentType.JSON).when()
                .post("/actions/child.write")
            .then()
            .statusCode(200)
            .body(new BaseMatcher<OutputTable>() {
                @Override
                public boolean matches(Object body) {
                    final JsonArray objects = new JsonArray(body.toString());
                    JsonObject object = objects.getJsonObject(0);
                    data.put("childId", object.getString("id"));
                    return body != null;
                }

                @Override
                public void describeTo(Description description) {

                }
            });
        RestAssured.given()
            .body("{\"id\": "+ data.get("childId") + "}")
            .contentType(ContentType.JSON)
            .when().post("/actions/child.read")
            .then()
            .statusCode(200)
            .body(new BaseMatcher<OutputTable>() {
                @Override
                public boolean matches(Object body) {
                    return body != null;
                }

                @Override
                public void describeTo(Description description) {

                }
            });
    }

}
