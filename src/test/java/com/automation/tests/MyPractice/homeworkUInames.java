package com.automation.tests.MyPractice;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


/*
In this assignment, you will test api uinames. This is a free api used to create test users. Documentation
for this api is available at https://uinames.com. You can import Postman collection for this API using link:
https://www.getpostman.com/collections/e1338b73a8be7a5500e6. Automate the given test cases. You
can use any existing project. You can automate all test cases in same class or different classes.
*/

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class homeworkUInames {

    @BeforeAll
    static public void setup(){
        baseURI = "https://uinames.com/";
    }

    @Test
    @DisplayName("Verify that name, surname, gender, region fields have value")
    public void test1(){
        given().
                accept("application/json; charset=utf-8").
                get(baseURI+"api/").
                then().assertThat().statusCode(200).
                assertThat().body("name",is(notNullValue())).
                assertThat().body("surname",is(notNullValue())).
                assertThat().body("gender", is(notNullValue())).
                assertThat().body("region",is(notNullValue()));
              //  and().log().all();
    }

    @Test
    @DisplayName("Verify that value of gender field is same from step 1")
    public void test2(){
        given().accept("application/json; charset=utf-8").
                when().queryParam("gender","female").
                and().get(baseURI+"api/").
                then().assertThat().statusCode(200).
                and().assertThat().body("gender",is("female"));
    }

    @Test
    @DisplayName("Verify that value of region field is same from step 1")
    public void test3(){
        given().
                accept("application/json; charset=utf-8").
                when().queryParam("gender","female").
                and().queryParam("region","Greece").
                and().get(baseURI+"api/").
                then().assertThat().statusCode(200).
                assertThat().body("gender",is("female")).
                assertThat().body("region", is("Greece"));
    }

    @Test
    @DisplayName("Verify that value of error field is Invalid gender")
    public void test4(){
        given().
                when().queryParam("gender","erkek").
                and().get(baseURI+"api/").
                then().assertThat().statusCode(400).
                and().statusLine(containsString("Bad Request")).
                and().assertThat().body("error",is("Invalid gender"));
    }

    @Test
    @DisplayName("Verify that value of error field is Region or language not found")
    public void test5(){
        given().
                queryParam("region","Kazakhstan").
                and().get(baseURI+"api/").
                then().assertThat().statusCode(400).
                and().assertThat().statusLine(containsString("Bad Request")).
                and().assertThat().body("error",is("Region or language not found"));
    }


    /*
    1. Create request by providing query parameters: a valid region and amount (must be bigger than 1)
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that all objects have different name+surname combination
     */
    @Test
    @DisplayName("Verify that all objects have different name+surname combination")
    public void test6(){
        JsonPath json = given().
                accept("application/json; charset=utf-8").
                queryParam("amount",2).
                when().get("api/").jsonPath();

        String name1 = json.getString("[0].name");
        String lastname1 = json.getString("[0].surname");
        String name2 = json.getString("[1].name");
        String lastname2 = json.getString("[1].surname");
        String fullname1=name1+" "+lastname1;
        String fullname2=name2+" "+lastname2;
        System.out.println(fullname1+" "+fullname2);
        assertNotEquals(fullname1,fullname2);
    }
    /*
        3 params test
    1. Create a request by providing query parameters: a valid region, gender and amount (must be bigger than 1)
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that all objects the response have the same region and gender passed in step 1
     */
    @Test
    @DisplayName("")
    public void test7(){
        Response response = given().
                accept(ContentType.JSON).
                queryParam("gender","male").
                queryParam("region","England").
                queryParam("amount",5).
                when().get("/api");
        response.then().assertThat().statusCode(200);
        response.then().assertThat().contentType("application/json; charset=utf-8");
        response.prettyPeek();

        List<String> genders = response.jsonPath().getList("gender");
        for (int i=0; i<genders.size(); i++){
            assertEquals("male",genders.get(i),"gender not equal");
        }
        List<String> regions = response.jsonPath().getList("region");
        for (int i=0; i<regions.size(); i++){
            assertEquals("England", regions.get(i));
        }
    }
    /*
    Amount count test
1. Create a request by providing query parameter: amount (must be bigger than 1)
2. Verify status code 200, content type application/json; charset=utf-8
3. Verify that number of objects returned in the response is same as the amount passed in step 1
     */
    @Test
    @DisplayName("")
    public void test8(){
        Response response = given().
                queryParam("amount",5).
                when().get("/api");
    response.
            then().assertThat().contentType("application/json; charset=utf-8").
                   assertThat().statusCode(200);
    List<String> count = response.jsonPath().getList("name");
    assertEquals(5,count.size());
    response.prettyPrint();

    }



}










