package com.cloudelements.userapi;

import com.cloudelements.constants.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.hamcrest.Matchers;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserAPI {
	@BeforeClass
	public void setup() {

		RestAssured.baseURI = ApiBaseConstants.BASE_URL;
	}

	// String baseURL = "https://reqres.in";

	/*
	 * TC001: Verify the BASEURL is up and running before proceeding to the
	 * other tests
	 */
	@Test(priority = 1)
	public void verifybaseURIStatusCode() {

		try {
			RestAssured.get().then().assertThat().log().ifValidationFails().statusCode(200);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * TC002 : Verify Users API is accessible, contentType is JSON and displays
	 * the page 2 details with the expected body It Involves the Path Parameters
	 */
	@Test(priority = 2, dependsOnMethods = { "verifybaseURIStatusCode" })
	public void getUserApiStatusCodeandContentType() {

		RestAssured.get(ApiBaseConstants.USER_CREATION_ENDPOINT + "?page=2").then().assertThat().log()
				.ifValidationFails().statusCode(200).and().contentType(ContentType.JSON).and()
				.body("data.first_name", Matchers.hasItems("Eve", "Charles", "Tracey"));

	}

	/*
	 * TC003 : Get the User details of a given ID
	 */
	@Test(priority = 3, dependsOnMethods = { "verifybaseURIStatusCode" })
	public void getSpecificUserDetails() {

		RestAssured.get(ApiBaseConstants.GET_SPECIFIC_USER_DETAIL, 3).then().assertThat().log().ifValidationFails()
				.statusCode(200).and().contentType(ContentType.JSON).and()
				.body("data.first_name", Matchers.equalTo("Emma"));

	}

	/*
	 * TC004 : Verify the behavior of the application if the user id doesn't
	 * exist
	 */

	@Test(priority = 4, dependsOnMethods = { "verifybaseURIStatusCode" })
	public void getNonExistentUserDetails() {

		RestAssured.get(ApiBaseConstants.GET_SPECIFIC_USER_DETAIL, 103).then().assertThat().log().ifValidationFails()
				.statusCode(404).and().contentType(ContentType.JSON);
		// .and().body("data.first_name", Matchers.equalTo("Emma"));

	}

	/*
	 * TC005 : POST Method - Verify User creation works successfully with post
	 * Method
	 */

	@Test(priority = 5, dependsOnMethods = { "verifybaseURIStatusCode" })

	public void createUser_testingPostMethod() {
  		
        String name = "Ramakrishna", occupation="Engineer";
		String newuser = "{\"name\": " + name + ", \n \"job\": "+ occupation+" }";
		//"{\n" + "\"name\": \"Ramakrishna\", \n" + "        \"job\": \"leader\" " + "}";

		String newuserid =

				RestAssured.given().when().body(newuser).post(ApiBaseConstants.USER_CREATION_ENDPOINT).then()
						.assertThat().log().all().statusCode(201).extract().body().jsonPath().get("id");

		System.out.println("Newly Created User id :" + newuserid);
	}

	/*
	 * TC006 : PUT Method - Verify User update works successfully with put
	 * Method
	 */

	@Test(priority = 6, dependsOnMethods = { "verifybaseURIStatusCode" })
	public void updateUser_testingPutMethod() {

		RestAssured.given().when()
				.body("{\n" + "		\"name\": \"morpheus\", \n" + "        \"job\": \"Enterpreneur\" " + "}")
				.put(ApiBaseConstants.USER_CREATION_ENDPOINT + "/2").then().assertThat().log().all().statusCode(200);
	}

	@AfterClass
	public void teardown() {
		RestAssured.reset();
	}

}
