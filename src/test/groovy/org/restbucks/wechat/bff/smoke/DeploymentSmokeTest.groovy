package org.restbucks.wechat.bff.smoke

import io.restassured.RestAssured
import org.junit.Before
import org.junit.Test

import java.util.concurrent.Callable

import static io.restassured.RestAssured.given
import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await
import static org.hamcrest.Matchers.equalTo

class DeploymentSmokeTest {

    @Before
    void setup() {
        RestAssured.baseURI = getBaseUri()
        RestAssured.port = getPort()
    }

    @Test
    void itShouldBeRunningWithExpectedBuildVersion() {

        await().
                atMost(90, SECONDS).pollInterval(10, SECONDS).
                until(getActualBuildVersion(), equalTo(expectedBuildVersion()))
    }

    private static Callable<String> getActualBuildVersion() {
        return new Callable<String>() {
            String call() throws Exception {
                try {
                    // @formatter:off
                    given().
                        log().all().
                    when().
                        get("/info").
                    then().
                        log().all().extract().body().jsonPath().getString("build.version")
                    // @formatter:on
                } catch (ConnectException e) {
                    e.printStackTrace()
                    return e.getMessage()
                }
            }
        }
    }

    private static String getBaseUri() {
        System.getenv("APP_BASE_URI")
    }

    private static int getPort() {
        System.getenv("APP_PORT").toInteger()
    }

    private static String expectedBuildVersion() {
        Properties properties = new Properties()
        properties.load(DeploymentSmokeTest.class.getClassLoader().getResourceAsStream("META-INF/build-info.properties"))
        properties."build.version"

    }

}