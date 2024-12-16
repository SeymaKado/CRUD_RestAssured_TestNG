package Base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.impl.SimpleLogger;
import org.testng.annotations.BeforeClass;

public class BaseTest {


    protected RequestSpecification reqSpec;

    @BeforeClass
    public void setUp() {
        reqSpec = new RequestSpecBuilder()
                .setBaseUri("http://petstore.swagger.io/v2")
                .setContentType(ContentType.JSON)
                .build();
    }


}
