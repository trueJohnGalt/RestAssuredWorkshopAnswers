package exercises;

import dataentities.Car;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class RestAssuredExercises6Test {

	private static RequestSpecification requestSpec;

	@BeforeClass
	public static void createRequestSpecification() {

		requestSpec = new RequestSpecBuilder().
			setBaseUri("http://localhost").
			setPort(9876).
			setContentType(ContentType.JSON).
			addFilter(new RequestLoggingFilter(LogDetail.ALL)).
			build();
	}
		
	/*******************************************************
	 * Create a new Car object that represents a 2012 Ford Focus
	 * POST this object to /cars/postcar
	 * Verify that the response HTTP status code is equal to 200
	 ******************************************************/
	
	@Test
	public void checkThatPostingA2012FordFocusReturnsHttp200() {

		Car fordFocus = new Car("Ford", "Focus", 2012);

		given().
				spec(requestSpec).
				body(fordFocus).
		when().
				post("/car/postcar").
		then().
				assertThat().
				statusCode(200);
	}

	/*******************************************************
	 * Perform a GET to /cars/getcar/alfaromeogiulia
	 * Store the response in a Car object
	 * Verify, using that object, that the model year = 2016
	 * Use the standard Assert.assertEquals(expected,actual)
	 * as provided by JUnit for the assertion
	 ******************************************************/

	@Test
	public void checkThatRetrievingAnAlfaRomeoGiuliaShowsModelYear2016() {
		Car carFromResponse =
				given().
						spec(requestSpec).
				when().
						get("/car/getcar/alfaromeogiulia").as(Car.class);
		// Put your assert here
		Assert.assertEquals(2016, carFromResponse.getYear());
	}
}