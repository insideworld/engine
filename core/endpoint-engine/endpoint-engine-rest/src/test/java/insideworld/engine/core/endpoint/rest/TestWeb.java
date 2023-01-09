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

package insideworld.engine.core.endpoint.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import javax.inject.Inject;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestWeb {

    @Inject
    public TestWeb() {

    }

    @Test
    final void test() {
        RestAssured.given()
            .body("\"qwe\"")
            .contentType(ContentType.JSON)
            .when()
            .post("/actions/test")
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
            .body("{" +
                  "\"some\": \"onetwo\"" +
                  "}")
            .contentType(ContentType.JSON)
            .when()
            .post("/actions/data")
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
//        RestAssured.given()
//            .body("{}")
//            .contentType(ContentType.JSON)
//            .when()
//            .post("/actions/exception")
//            .then()
//            .statusCode(200)
//            .body(new BaseMatcher<Object>() {
//                @Override
//                public boolean matches(Object o) {
//                    System.out.println("Matching");
//                    return true;
//                }
//
//                @Override
//                public void describeTo(Description description) {
//
//                }
//            });
        System.out.println("End");
    }


}
