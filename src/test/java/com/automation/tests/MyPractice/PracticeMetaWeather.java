package com.automation.tests.MyPractice;
import com.automation.utilities.ConfigurationReader;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.baseURI;

import com.automation.utilities.ConfigurationReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeAll;

public class PracticeMetaWeather {
    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("meta.weather.uri");
    }
    /**
     * TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'New'
     * Then user verifies that payload contains 5 objects
     */
    @Test
    public void test1(){
        given().
                accept(ContentType.JSON).
                queryParam("query","New").
                when().get("/search").
                then().assertThat().statusCode(200).
                assertThat().body("",hasSize(5)).
                log().body();
    }
    /**
     *TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is New
     * Then user verifies that 1st object has following info:
     *  |title   |location_type|woeid  |latt_long          |
     *  |New York|City         |2459115|40.71455,-74.007118|
     */
    @Test
    public void test2(){
        given().
                accept(ContentType.JSON).
                queryParam("query","New").
                when().get("/search").
                then().assertThat().body("title[0]",is("New York")).
                assertThat().body("location_type[0]",is("City")).
                assertThat().body("woeid[0]",is(2459115)).
                assertThat().body("latt_long[0]",is("40.71455,-74.007118")).
                log().body(true);
    }
    /**
     *TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'Las'
     * Then user verifies that payload  contains following titles:
     *  |Glasgow  |
     *  |Dallas   |
     *  |Las Vegas|
     *
     */
    @Test
    public void test3(){
        given().
                accept(ContentType.JSON).
                queryParam("query","Las").
                when().get("/search").
                then().assertThat().statusCode(200).
                assertThat().body("title",contains("Glasgow","Dallas","Las Vegas")).
                log().all(true);
    }

    /**
     *TASK
     * Given accept type is JSON
     * When users sends a GET request to "/location"
     * And path parameter is '44418'
     * Then verify following that payload contains weather forecast sources
     * |BBC                 |
     * |Forecast.io         |
     * |HAMweather          |
     * |Met Office          |
     * |OpenWeatherMap      |
     * |Weather Underground |
     * |World Weather Online|
     */
    @Test
    public void test4(){
        List<String> expected = List.of("BBC", "Forecast.io", "HAMweather", "Met Office", "OpenWeatherMap", "Weather Underground", "World Weather Online");
        List<String> actual = given().
                accept(ContentType.JSON).
                get("/44418").thenReturn().jsonPath().getList("sources.title");
        assertEquals(expected,actual);

    }


}
