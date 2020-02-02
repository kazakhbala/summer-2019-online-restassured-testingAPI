package com.automation.tests.day5;
import com.automation.pojos.Job;
import com.automation.pojos.Location;
import com.automation.utilities.ConfigurationReader;
import com.google.gson.Gson;
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

public class LocationTesting {
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    @Test
    @DisplayName("Convert from JSON to Location POJO")
    public void test1() {
        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/locations/{location_id}", 2500);
        Location location = response.jsonPath().getObject("", Location.class);
        System.out.println(location);
        Response response2 = given().
                accept(ContentType.JSON).
                when().
                get("/locations");
        List<Location> locations = response2.jsonPath().getList("items", Location.class);
        for (Location l : locations) {
            System.out.println(l);
        }
    }
}