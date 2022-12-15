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

package insideworld.engine.example.quarkus.common;

import insideworld.engine.frameworks.quarkus.test.database.DatabaseResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
public class RestTest {

    @Test
    final void test() {
        RestAssured.given()
            .relaxedHTTPSValidation()
            .header("Authorization", "Bearer token")
            .body("{}")
            .contentType(ContentType.JSON)
            .when()
            .post("/actions/somedata.read")
            .then()
            .statusCode(200)
            .body(new BaseMatcher<Object>() {
                @Override
                public boolean matches(Object o) {
                    System.out.println("Matching");
                    return true;
                }

                @Override
                public void describeTo(Description description) {

                }
            });
        RestAssured.given()
            .relaxedHTTPSValidation()
            .header("Authorization", "Bearer toke1n")
            .body("{}")
            .contentType(ContentType.JSON)
            .when()
            .post("/actions/somedata.read")
            .then()
            .statusCode(200)
            .body(new BaseMatcher<Object>() {
                @Override
                public boolean matches(Object o) {
                    System.out.println("Matching");
                    return true;
                }

                @Override
                public void describeTo(Description description) {

                }
            });
        RestAssured.given()
            .relaxedHTTPSValidation()
            .body("{}")
            .contentType(ContentType.JSON)
            .when()
            .post("/actions/exception")
            .then()
            .statusCode(200)
            .body(new BaseMatcher<Object>() {
                @Override
                public boolean matches(Object o) {
                    System.out.println("Matching");
                    return true;
                }

                @Override
                public void describeTo(Description description) {

                }
            });
        System.out.println("End");
    }
}
