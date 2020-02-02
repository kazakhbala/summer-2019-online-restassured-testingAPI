package com.automation.tests.MyPractice;

import com.automation.pojos.Job_history;
import com.automation.pojos.Location;
import com.automation.utilities.ConfigurationReader;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;


public class PracticeORDS {

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    /**
     * given path parameter is "/regions/{id}"
     * when user makes get request
     * and region id is equals to 1
     * then assert that status code is 200
     * and assert that region name is Europe
     */
    @Test
    public void test1(){
        given().
                accept("application/json").
                pathParam("id",1).
                when().get(baseURI+"/regions/{id}").
                then().assertThat().statusCode(200).
                and().assertThat().body("region_name",is("Europe")).
                log().body();
    }

    @Test
    public void test2(){
        given().accept("application/json").
                get(baseURI+"/employees").
                then().assertThat().statusCode(200).log().ifError();
    }

    @Test
    public void test3(){
        given().accept("application/json").
                pathParam("id",101).
                get(baseURI+"/employees/{id}").
                then().assertThat().statusCode(200).
                assertThat().body("first_name",is("Neena")).
                assertThat().body("manager_id",is(100)).
                and().log().all();
    }

    @Test
    public void test4(){
        given().
                accept("application/json").
                pathParam("id",104).
                get(baseURI+"/employees/{id}").
                then().statusCode(200).
                and().assertThat().body("salary",is(6000)).
                log().body();
    }

    @Test
    public void test5(){
        given().
                accept("application/json").
                pathParam("id","AR").
                get(baseURI+"/countries/{id}").
                then().assertThat().statusCode(200).
                assertThat().body("country_name",is("Argentina")).
                assertThat().body("region_id", is(2)).
                and().log().body();
    }
    @Test
    public void test6(){
        given().
                accept("application/json").
                pathParam("id",3000).
                get(baseURI+"/locations/{id}").
                then().assertThat().statusCode(200).
                assertThat().body("city",is("Bern")).
                log().body();
    }

    @Test
    public void test7(){
        JsonPath json = given().accept("application/json").
                get(baseURI+"/employees/").
                thenReturn().jsonPath();
        String firstName=json.getString("items[0].first_name");
        String phoneNumber=json.getString("items[0].phone_number");
        System.out.println(firstName+" "+phoneNumber);

        String lastName = json.getString("items[1].last_name");
        String hiringDate = json.getString("items[1].hire_date");
        System.out.println(lastName+" "+hiringDate);
    }

    /**
     * given path parameter is "/regions/{id}"
     * when user makes get request
     * and region id is equals to 1
     * then assert that status code is 200
     * and assert that region name is Europe
     */
    @Test
    public void test8(){
        given().
                accept(ContentType.JSON).
                pathParam("id",1).
                when().get("/regions/{id}").
                then().assertThat().statusCode(200).
                assertThat().body("region_name",is("Europe")).
                log().body(true);
    }
    //write a code to
    //get info from /countries as List<Map<String, ?>>
    //prettyPrint() - print json/xml/html in nice format and returns string, thus we cannot retrieve jsonpath without extraction...
    //prettyPeek() does same job, but return Response object, and from that object we can get json path.
    @Test
    public void test9(){
        Response response = given().
                accept(ContentType.JSON).
                when().get("/countries");
        List<Map<String,?>> info = response.jsonPath().getJsonObject("items");
        for (Map<String,?> each : info){
            System.out.println(each);
        }
    }

    // get collection of employee's salaries
    // then sort it
    // and print
    @Test
    public void test10(){
        Response response = given().
                        accept(ContentType.JSON).
                        when().get("/employees");
        List<Integer> salaries = response.jsonPath().getList("items.salary");
        Collections.sort(salaries);
        System.out.println(salaries);
    }
    @Test
    public void test11(){
        Response response = given().
                accept(ContentType.JSON).
                when().get("/employees");
        List<String> names = response.jsonPath().getList("items.first_name");
        List<String> lastnames = response.jsonPath().getList("items.last_name");
        System.out.println(names+" "+lastnames);
    }

    @Test
    public void test12(){
        Response response = given().
                accept(ContentType.JSON).
                when().
                get("/employees/{id}",100);
        Map<String, Object> info = response.jsonPath().getJsonObject("");
        System.out.println(info);
    }
    //get collection of phone numbers, from employees
    //and replace all dots "." in every phone number with dash "-"
    @Test
    public void test13(){
        Response response = given().
                accept(ContentType.JSON).
                when().get("/employees");
        List<String> phoneNumbers = response.jsonPath().getList("items.phone_number");
        phoneNumbers.replaceAll(phone->phone.toString().replace(".","-"));
        System.out.println(phoneNumbers);
    }
    /** ####TASK#####
     *  Given accept type as JSON
     *  And path parameter is id with value 1700
     *  When user sends get request to /locations
     *  Then user verifies that status code is 200
     *  And user verifies following json path information:
     *      |location_id|postal_code|city   |state_province|
     *      |1700       |98199      |Seattle|Washington    |
     */
    @Test
    public void test14(){
         given().
                accept(ContentType.JSON).
                pathParam("id",1700).
                when().get("/locations/{id}").
                then().assertThat().statusCode(200).
                assertThat().body("location_id",is(1700)).
                assertThat().body("city",is("Seattle")).log().all();
    }
    /*
    Given accept type is JSON
    And parameters: q = country_id = US
    When users sends a GET request to "/countries"
    Then status code is 200
    And Content type is application/json
    And country_name from payload is "United States of America"
    {"country_id":"US"}
 */
    @Test
    public void test15(){
        given().
                accept(ContentType.JSON).
                pathParam("country_id","US").
                when().get("/countries/{country_id}").
                then().assertThat().statusCode(200).
                assertThat().contentType(ContentType.JSON).
                assertThat().body("country_name",is("United States of America")).
                assertThat().body("country_id",is("US")).
                log().all();
    }
    /**
     * given path parameter is "/regions" and region id is 2
     * when user makes get request
     * then assert that status code is 200
     * and verify that body returns following country names
     *  |Argentina                |
     *  |Brazil                   |
     *  |Canada                   |
     *  |Mexico                   |
     *  |United States of America |
     */
    @Test
    public void test16(){
        Response response=given().
                accept(ContentType.JSON).
                queryParam("q","{\"region_id\":\"2\"}").
                when().get("/regions");
        response.prettyPrint();
    }
    /*
     * given path parameter is "/employees"
     * when user makes get request
     * then user verifies that status code is 200
     * and user verifies that average salary is grater that 5000
     */
    @Test
    public void test17(){
        Response response=given().
                accept(ContentType.JSON).
                when().get("/employees");
        List<Integer> salaries = response.jsonPath().getList("items.salary");
        Integer avgSalary = 0;
        Integer totalSalary=0;
         for (int i=0; i<salaries.size(); i++){
             totalSalary+=salaries.get(i);
         }
         avgSalary=totalSalary/salaries.size();
        System.out.println("Average salary is: "+avgSalary);
        System.out.println("Total salary: "+totalSalary);

        assertTrue(avgSalary>5000,"Average salary is less then 5000");
    }

    @Test
    @DisplayName("Convert from JSON to Location POJO")
    public void test18(){
        Response response=given().
                accept(ContentType.JSON).
                when().get("/locations/{id}",2900);
        Location location = response.jsonPath().getObject("",Location.class);
        System.out.println(location.getCity());
        System.out.println(location.getStreetAddress());
        System.out.println(location.getStateProvince());
    }
    @Test
    public void test19(){
        Response response = given().
                accept(ContentType.JSON).
                when().get("locations");
        List<Location> locations = response.jsonPath().getList("items",Location.class);
        for (Location each : locations){
            System.out.println(each.getCity()+" "+each.getStateProvince());
        }
    }

    @Test
    @DisplayName("Convert from JSON to Job_history POJO")
    public void test20(){
        Response response = given().
                accept(ContentType.JSON).
                when().get("/job_history");
        List<Job_history> info = response.jsonPath().getList("items",Job_history.class);
        for (Job_history each : info){
            System.out.println(each);
        }
    }











}




































