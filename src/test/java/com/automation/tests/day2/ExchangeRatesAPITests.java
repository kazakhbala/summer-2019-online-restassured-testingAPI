package com.automation.tests.day2;

import org.junit.jupiter.api.Test;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*; //this helps to access directly
import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRatesAPITests {
    //resources: https://exchangeratesapi.io/    and  https://github.com/openratesapi/openrates
    private String baseURI="http://api.openrates.io/";

    @Test
    public void test1(){
        Response response = given().baseUri(baseURI+"latest").get();
        System.out.println(response.getHeaders()); // we can print header
        System.out.println(response.prettyPrint());// to print body, easiest way
        assertEquals(200,response.getStatusCode());
    }

    @Test
    public void test2(){
        Response response = given().get(baseURI+"latest");
        //verify that content type is json
        assertEquals(200, response.getStatusCode());
        //verify that data is coming as json
        assertEquals("application/json", response.getHeader("Content-Type"));
        //or like this
        assertEquals("application/json", response.getContentType());
    }
    //GET https://api.exchangeratesapi.io/latest?base=USD HTTP/1.1
    //base it's a query parameter that will ask web service to change currency from eu to something else

    @Test
    public  void test3(){
        //#TASK: get currency exchange rate for dollar. By default it's euro.
     //   Response response = given().get(baseURI+"latest?base=USD"); or we can do like that
        Response response = given().
                baseUri(baseURI+"latest").
                queryParam("base", "USD").
                get();
        //https://api.exchangeratesapi.io/latest?symbols=USD,GBP from documentation
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    //    #TASK: verify that response body, for latest currency rates, contains today's date (2020-01-23 | yyyy-MM-dd)
    @Test
    public void test4() {
        Response response = given().
                baseUri(baseURI + "latest").
                queryParam("base", "GBP").
                get();
        String todaysDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("Today's date: " + todaysDate);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains(todaysDate));
    }


    //let's get currency exchange rate for year 2000
    //GET https://api.exchangeratesapi.io/history?start_at=2018-01-01&end_at=2018-09-01&base=USD&symbols=ILS,JPY
    @Test
    public void test5(){
        Response response = given().
                baseUri(baseURI+"history").queryParam("start_at","2000-01-01").
                queryParam("end_at", "2000-12-31").
                queryParam("base", "USD").
                queryParam("symbols", "EUR","GBP","JPY").get();
        System.out.println(response.prettyPrint());
        assertEquals(200, response.getStatusCode());
    }

    /**
     * Given request parameter "base" is "USD"
     * When user sends request to "api.openrates.io"
     * Then response code should be 200
     * And response body must contain ""base": "USD""
     */
    //GET https://api.exchangeratesapi.io/latest?base=USD
    @Test
    public void test6(){
        Response response = given().
                baseUri(baseURI+"latest").queryParam("base","USD").get();
         String body = response.getBody().asString();
        assertEquals(200,response.getStatusCode());
        assertTrue(body.contains("\"base\":\"USD"));
        System.out.println(response.getStatusCode());
        System.out.println(body);
    }


    @Test  //GET https://api.exchangeratesapi.io/latest
    public void test7(){
        Response response = given().baseUri(baseURI+"latest").get();
        assertEquals(200,response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    @Test //GET https://api.exchangeratesapi.io/2010-01-12
    public void test8(){
        String todaysDate=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Response response = given().baseUri(baseURI+todaysDate).get();
        String baseEuro=response.getBody().asString();
        assertTrue(baseEuro.contains("EUR"));
        assertEquals(200, response.getStatusCode());
        System.out.print(response.prettyPrint());
    }

    @Test //GET https://api.exchangeratesapi.io/latest?base=USD
    public void test9(){
        Response response = given().baseUri(baseURI+"latest/").
                queryParam("base","USD").get();
        assertEquals(200,response.getStatusCode());
        String body = response.getBody().asString();
        assertTrue(body.contains("\"base\":\"USD"));
        System.out.println(body);
    }

    @Test //GET https://api.exchangeratesapi.io/latest?symbols=USD,GBP
    public void test10(){
        Response response = given().baseUri(baseURI+"latest").
                queryParam("symbols","USD","GBP").get();
        assertEquals(200,response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    @Test  //GET https://api.exchangeratesapi.io/history?start_at=2018-01-01&end_at=2018-09-01
    public void test11(){
        Response response = given().baseUri(baseURI+"history").
                queryParam("start_at","2018-01-01").
                queryParam("end_at","2018-09-01").
                queryParam("base","USD").
                queryParam("symbols","USD","GBP").get();
        assertEquals(200,response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    @Test // GET https://api.exchangeratesapi.io/history?start_at=2018-01-01&end_at=2018-09-01&base=USD
    public void test12(){
        Response response = given().baseUri(baseURI+"history").
                queryParam("start_at","2018-01-01").
                queryParam("end_at","2018-09-01").
                queryParam("base","USD").
                queryParam("symbols","USD","GBP").get();
        assertEquals(200,response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

}
