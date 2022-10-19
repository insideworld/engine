/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
