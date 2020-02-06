package com.automation.tests.MyPractice;
import com.automation.pojos.Room;
import com.automation.pojos.Spartan;
import com.automation.utilities.APIUtilities;
import com.automation.utilities.ExcelUtil;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;

import com.automation.pojos.Job;
import com.automation.pojos.Location;
import com.automation.utilities.ConfigurationReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class PracticeBookit {

    @BeforeAll
    public static void setup(){
        baseURI=ConfigurationReader.getProperty("bookit.qa1");
    }

    /**
     * Given accept content type as JSON
     * When user sends get requests to /api/rooms
     * Then user should get 422 status code
     */
    @Test
    @DisplayName("")
    public void test1(){
        given().
                accept(ContentType.JSON).
         when().
                get("/api/rooms").prettyPeek().
          then().
                assertThat().statusCode(422);
    }

    @Test
    @DisplayName("Verify that system accept valid token")
    public void test2(){
        given().
                accept(ContentType.JSON).
                auth().oauth2("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1Mzc2IiwiYXVkIjoic3R1ZGVudC10ZWFtLWxlYWRlciJ9.DoFI744aMLxUaf0GcjVOEDkJ3Wh7RlKDx-TYp8_sJpU").
                when().get("/api/rooms").prettyPeek().
                then().assertThat().statusCode(200);
    }
}
