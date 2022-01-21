package insideworld.engine.integration.tests.entities.generated;

import insideworld.engine.actions.keeper.output.OutputTable;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestRead {

    @Test
    public void test() {
        RestAssured.given()
            .body("{}")
//            .body("{\"memberName\":\"Activity member\",\"terFrom\":\"10/01/2021\"}")
            .contentType(ContentType.JSON)
            .when().post("/actions/read.read.read")
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
