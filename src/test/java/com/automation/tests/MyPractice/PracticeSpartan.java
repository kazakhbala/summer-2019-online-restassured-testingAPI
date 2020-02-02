package com.automation.tests.MyPractice;

import com.automation.pojos.Job;
import com.automation.pojos.Location;
import com.automation.pojos.Spartan;
import com.automation.utilities.ConfigurationReader;
import com.github.javafaker.Faker;
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

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class PracticeSpartan {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("spartan.uri");
    }

    /**
     * given accept content type as JSON
     * when user sends GET request to /spartans
     * then user verifies that status code is 200
     * and user verifies that content type is JSON
     */
    @Test
    @DisplayName("verify content type")
    public void test1() {
        given().
                accept(ContentType.JSON).
                when().get("/spartans").prettyPeek().
                then().assertThat().statusCode(200).
                assertThat().contentType(ContentType.JSON);
    }

    /**
     * TASK
     * given accept content type as XML
     * when user sends GET request to /spartans
     * then user verifies that status code is 200
     * and user verifies that content type is XML
     */
    @Test
    @DisplayName("Verify XML")
    public void test2() {
        given().
                accept(ContentType.XML).
                when().get("/spartans").prettyPeek().
                then().assertThat().statusCode(200).
                assertThat().contentType(ContentType.XML);
    }

    /**
     * TASK
     * given accept content type as JSON
     * when user sends GET request to /spartans
     * then user saves payload into collection
     */
    @Test
    public void test3() {
        Response response = given().
                accept(ContentType.JSON).
                when().get("/spartans");

        List<Map<String, ?>> info = response.jsonPath().get();
        for (Map<String, ?> each : info) {
            System.out.println(each);
        }
    }

    /**
     * TASK
     * given accept content type as JSON
     * when user sends GET request to /spartans
     * then user saves payload into collection of Spartan
     */
    @Test
    public void test4() {
        Response response = given().
                accept(ContentType.JSON).when().
                get("/spartans");

        List<SpartanPOJO> files = response.jsonPath().getList("", SpartanPOJO.class);
        for (SpartanPOJO each : files) {
            System.out.println(each);
        }
    }

    /**
     * TASK
     * given accept content type as JSON
     * when user sends POST request to /spartans
     * and user should be able to create new spartan
     * |gender|name           |phone     |
     * | male |Mister Twister |5712134235|
     * then user verifies that status code is 201
     * <p>
     * 201 - means created. Whenever you POST something, you should get back 201 status code
     * in case of created record
     */
    @Test
    public void test5() {

        Spartan spartan = new Spartan();
        spartan.setName("Max Motor");
        spartan.setGender("Male");
        spartan.setPhone(77712399980L);

        Response response = given().
                contentType(ContentType.JSON).
                body(spartan).
                when().post("/spartans");
        response.prettyPeek();
        assertEquals(201, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
    }

    @Test
    @DisplayName("delete a student")
    public void test6() {
        int idOfStudent = 1;
        Response response = delete("/spartans/{id}", idOfStudent);
        response.prettyPeek();
    }

    @Test
    public void test7() {
        int id = 15;
        Response response = delete("/spartans/{id}", id);
        response.prettyPeek();
    }

    @Test
    @DisplayName("Delete half of the records")
    public void test8() {

        Response response = given().
                accept(ContentType.JSON).
                when().get("/spartans");
        List<Integer> listOfId = response.jsonPath().getList("id");
        Collections.sort(listOfId, Collections.reverseOrder());
        System.out.println("Before delete: " + listOfId.size());
        for (int i = 0; i <= listOfId.size() / 2; i++) {
            when().delete("/spartans/{i}", i);
        }
        given().accept(ContentType.JSON).when().get("/spartans").prettyPrint();
    }

    /**
     * TASK
     * given accept content type as XML
     * when user sends GET request to /spartans
     * then user verifies that status code is 200
     * and user verifies that content type is XML
     */
    @Test
    public void test9() {
        given().
                accept(ContentType.XML).
                when().get("/spartans").
                then().assertThat().statusCode(200).
                assertThat().contentType(ContentType.XML).
                log().all();
    }

    /**
     * TASK
     * given accept content type as JSON
     * when user sends GET request to /spartans
     * then user saves payload into collection
     */
    @Test
    @DisplayName("Save payload into java collection")
    public void test10() {
        Response response = given().
                accept(ContentType.JSON).
                when().get("/spartans");

        List<Map<String, Object>> info = response.jsonPath().getList("");
        for (Map<String, Object> each : info) {
            System.out.println(each);
        }
    }

    @Test
    public void test11() {
        Response response = given().
                accept(ContentType.JSON).
                when().get("/spartans");
        List<Spartan> info = response.jsonPath().getList("", Spartan.class);
        for (Spartan each : info) {
            if (each.getSpartanId() == 148) {
                System.out.println(each);
            }
        }
    }
        /**
         * TASK
         * given accept content type as JSON
         * when user sends POST request to /spartans
         * and user should be able to create new spartan
         * |gender|name           |phone     |
         * | male |Mister Twister |5712134235|
         * then user verifies that status code is 201
         * <p>
         * 201 - means created. Whenever you POST something, you should get back 201 status code
         * in case of created record
         */
    @Test
    public void test12(){
        Spartan spartan=new Spartan();
        spartan.setName("Aibek");
        spartan.setGender("Male");
        spartan.setPhone(77788899900l);

        Response response=given().
                contentType(ContentType.JSON).
                body(spartan).
                when().post("spartans");
        assertEquals(201,response.statusCode());
        assertEquals("Aibek",spartan.getName());
        System.out.println(spartan);
        response.prettyPrint();
    }

    @Test
    @DisplayName("delete user")
    public void test13(){
        delete("/spartans/161");
    }

    @Test
    @DisplayName("Delete half of the records")
    public void test14(){
        Response response = given().
                when().get("/spartans");
        List<Integer> ListId = response.jsonPath().getList("id");
        for (int i=0; i<ListId.size()/2; i++){
           delete("/spartans/{id}",ListId.get(i));
        }



    }


}
































