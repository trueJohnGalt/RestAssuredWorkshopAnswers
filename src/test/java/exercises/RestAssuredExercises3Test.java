package exercises;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredExercises3Test {

	private static RequestSpecification requestSpec;
	private static ResponseSpecification responseSpec;
	private static String ninthDriverId;


	@BeforeClass
	public static void setup() {
		createRequestSpecification();
		createResponseSpecification();
		getNinthDriverId();
	}


	public static void createRequestSpecification() {

		requestSpec = new RequestSpecBuilder().
			setBaseUri("http://localhost").
			setPort(9876).
			addFilter(new ResponseLoggingFilter(LogDetail.ALL)).
			setBasePath("/api/f1").
			build();
	}
		
	/*******************************************************
	 * Create a static ResponseSpecification that checks whether:
	 * - the response has statusCode 200
	 * - the response contentType is JSON
	 * - the value of MRData.CircuitTable.Circuits.circuitName[0]
	 *   is equal to 'Albert Park Grand Prix Circuit'
	 ******************************************************/
	

	public static void createResponseSpecification() {
		responseSpec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                expectBody("MRData.CircuitTable.Circuits.circuitName[0]", equalTo("Albert Park Grand Prix Circuit")).
                build();
	}
	
	/*******************************************************
	 * Retrieve the list of 2016 Formula 1 drivers and store
	 * the driverId for the ninth mentioned driver in a
	 * static String variable
	 * Use /2016/drivers.json
	 ******************************************************/
	

	public static void getNinthDriverId() {
        ninthDriverId = given().
                            spec(requestSpec).
						when().
							get("/2016/drivers.json").
						then().
                            extract().
                            path("MRData.DriverTable.Drivers.driverId[8]");
	}

	/*******************************************************
	 * Retrieve the circuit data for the first race in 2014
	 * Use the previously created ResponseSpecification to
	 * execute the specified checks
	 * Use /2014/1/circuits.json
	 * Additionally, check that the circuit is located in Melbourne
	 ******************************************************/
	
	@Test
	public void useResponseSpecification() {
		
		given().
                spec(requestSpec).
		when().
                get("/2014/1/circuits.json").
		then().
                assertThat().
                spec(responseSpec).
                body("MRData.CircuitTable.Circuits.Location.locality[0]", equalTo("Melbourne"));
	}
	
	/*******************************************************
	 * Retrieve the driver data for the ninth mentioned driver
	 * Use the previously extracted driverId to do this
	 * Use it as a path parameter to /drivers/<driverId>.json
	 * Check that the driver is German
	 ******************************************************/
	
	@Test
	public void useExtractedDriverId() {
		
		given().
                spec(requestSpec).
                pathParam("driverId", ninthDriverId).
		when().
                get("/drivers/{driverId}.json").
		then().
                assertThat().
                body("MRData.DriverTable.Drivers.nationality[0]", equalTo("German"));
	}
}