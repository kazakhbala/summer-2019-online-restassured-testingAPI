package com.automation.tests.review;
import static io.restassured.RestAssured.baseURI;

import com.automation.pojos.Movie;
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

public class Feb5Review {

    /**
     * Before static import
     * Utils.revert(var);
     * <p>
     * OK
     * public boolean returnSomething(){
     * boolean var = false;
     * return var;
     * }
     * <p>
     * redundant - something extra without purpose
     * bad
     * public boolean returnSomething(){
     * boolean var = false;
     * if(var == false){
     * return false
     * }else{
     * return true;
     * }
     * }
     * <p>
     * after static import
     * revert(var);
     */
    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("omdb.uri");
    }

    @Test
    @DisplayName("Find movie by title")
    public void search_test() {
        Response response = given().
                accept(ContentType.JSON).
                queryParam("s", "Terminator").
                queryParam("apikey", "9f94d4d0").
                when().
                get().prettyPeek();
        //To get something from payload, in case of JSON, I use JsonPath
        //collect all titles
        List<String> titles = response.jsonPath().getList("Search.Title");

        //collect all poster links
        List<String> posterLinks = response.jsonPath().getList("Search.Poster");

        System.out.println(titles);
        System.out.println(posterLinks);

        String firstTitle = response.jsonPath().getString("Search[0].Title");
        String lastTitle = response.jsonPath().getString("Search[-1].Title");
        System.out.println("First title: "+firstTitle);
        System.out.println("Last title: "+lastTitle);

        Movie firstMovie = response.jsonPath().getObject("Search[0]", Movie.class);

        System.out.println(firstMovie.getTitle());
        System.out.println(firstMovie.getYear());

        List<Movie> movies = response.jsonPath().getList("Search", Movie.class);

        //print only imdb id's of every movie
        for(Movie movie: movies){
            System.out.println(movie.getImdbID());
        }
        //print all titles
        for(Movie movie: movies){
            System.out.println(movie.getTitle());
        }

        //How to deal with nested JSON response?
        //OR
        //How to deserialize nested JSON?
        // we can use List of Map where key is a string, and value is an object or?
        // List<Map<String, ?>> or List<Map<String, Object>>
        //or we can create a POJO by using composition:
        /**
         * public class Person{
         *     private String name;
         *     private String jobTitle;
         *     private Address address;
         *
         *
         *     class Address{
         *         private String street;
         *         private int aptNumber;
         *         private int zipCode;
         *     }
         * }
         *
         * What are the requirements to POJO class?
         *  - private instance variables
         *  - getters/setters
         *  - public no-arguments constructor
         *
         *  Everything else is optional.
         *  We can override toString() method
         *  to see information about object in plain text.
         *  Or, human readable representation of the object.
         *
         *
         *  Also, we can override equals() method to determine if 2 objects are equals
         *  Keep in mind, that variable names should match with JSON variable names.
         *  Otherwise, provide @SerializedName annotation.
         *
         *
         * When one object is using another objects as instance variables,
         * it calls composition. So we are able to use in one object,
         * things from another object without inheritance. So Person in not a Address
         * but Person has a Address. Instead of is-a relationship we have has-a relationship
         * between classes.
         */
        /**
         * given().
         *          content type: JSON, XML, TEXT...
         *          query parameters: api key, queries: q=I_need_this&b=and_this_as_well
         *          authentication: auth().basic("username","password)
         *          header: Authorization: Type encrypted_string
         *          cookies
         *          body() <- request body: POJO, File with JSON ot XML, or anything that you send
         *          Once you provide POJO, RestAssured will automatically serialize that POJO
         *          (convert it from POJO(java object) into stream of bytes, or for example into JSON
         *          RestAssured use Gson library to convert java object into JSON
         *          We hae added Gson as maven dependency
         * when().
         *  To specify:
         *          HTTP verb/method, request:
         *          get(),put(),post(),delete(),patch()
         *
         *          we can specify request with or without path:
         *          get() or get("/students")
         *
         *          with or without path parameters:
         *          get("/students/student/{student_id}", 55)
         * then(). <- for assertions:
         *          - check status code -->
         *            - 200 OK,
         *            - 201 Created,
         *            - 204 No content
         *            - 401 Unauthorized
         *            - 403 Forbidden access
         *            - 500 Internal Server error
         *
         *            Usually status code mean:
         *
         *              - 2XX - means all good
         *              - 4XX - you are in trouble, you did something wrong
         *              - 5XX - something happened with the server
         *
         *          - check payload (body) -->{
         *                   "Search": [
         *                {
         *                  "Title": "Terminator 2: Judgment Day",
         *                  "Year": "1991",
         *                  "imdbID": "tt0103064",
         *                  "Type": "movie",
         *                  "Poster": "https://m.media-amazon.com/300.jpg"
         *                   },
         *                }
         *          - response time
         *          - status line --> HTTP/1.1 200 OK
         *          - header --> Content-Type: application/json; charset=utf-8 or Date: Wed, 05 Feb 2020 19:54:53 GMT
         *
         *          assertThat().
         *          statusCode(200)
         *          body("items[0].title", is("Title that I expect"))
         *
         */
    }
}