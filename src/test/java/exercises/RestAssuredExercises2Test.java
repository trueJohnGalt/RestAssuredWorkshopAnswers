package exercises;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@RunWith(DataProviderRunner.class)
public class RestAssuredExercises2Test {

	private static RequestSpecification requestSpec;

	@BeforeClass
	public static void createRequestSpecification() {

		requestSpec = new RequestSpecBuilder().
			setBaseUri("http://localhost").
			setPort(9876).
			setBasePath("/api/f1").
			build();
	}
	
	/*******************************************************
	 * Create a DataProvider that specifies in which country
	 * a specific circuit can be found (specify that Monza 
	 * is in Italy, for example) 
	 ******************************************************/
	@DataProvider
	public static Object[][] countryDataProvider() {
	    return new Object[][] {
                {"monza", "Italy"},
                {"sepang", "Malaysia"},
        };
    }

	/*******************************************************
	 * Create a DataProvider that specifies for all races
	 * (adding the first four suffices) in 2015 how many  
	 * pit stops Max Verstappen made
	 * (race 1 = 1 pitstop, 2 = 3, 3 = 2, 4 = 2)
	 ******************************************************/
    @DataProvider
	public static Object[][] pitstopDataProvider() {
        return new Object[][] {
                {"1", "1"},
                {"2", "3"},
                {"3", "2"},
                {"4", "2"}
        };
    }
	/*******************************************************
	 * Request data for a specific circuit (for Monza this 
	 * is /circuits/monza.json)
	 * and check the country this circuit can be found in
	 ******************************************************/
	
	@Test
    @UseDataProvider("countryDataProvider")
	public void checkCountryForCircuit(String circuit, String country) {
		given().
                spec(requestSpec).
                pathParam("circuit", circuit).
		when().
                get("/circuits/{circuit}.json").
		then().
                assertThat().body("MRData.CircuitTable.Circuits.Location.country[0]", equalTo(country));
	}
	
	/*******************************************************
	 * Request the pitstop data for the first four races in
	 * 2015 for Max Verstappen (for race 1 this is
	 * /2015/1/drivers/max_verstappen/pitstops.json)
	 * and verify the number of pit stops made
	 ******************************************************/
	
	@Test
    @UseDataProvider("pitstopDataProvider")
	public void checkNumberOfPitstopsForMaxVerstappenIn2015(String race, String amount) {
		given().
                spec(requestSpec).
                pathParam("race", race).
		when().
                get("/2015/{race}/drivers/max_verstappen/pitstops.json").
		then().
                assertThat().
                body("MRData.total[0]", equalTo(amount));
	}
}