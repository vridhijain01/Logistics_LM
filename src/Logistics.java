import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Logistics 
{
	int id;
	@Test
	@BeforeTest
	public int placeOrder()
	{
	RestAssured.baseURI = "http://localhost:51544/v1";
	
	RequestSpecification request = RestAssured.given();
	request.header("Content-Type", "application/json");
	request.body("{\"stops\":[{\"lat\":22.344674,\"lng\":114.124651},{\"lat\":22.375384,\"lng\":114.182446},{\"lat\":22.385669,\"lng\":114.186962}]}");
	Response response = request.post("/orders");
	JsonPath jsonPathEvaluator = response.jsonPath();
	id = jsonPathEvaluator.get("id");
	int statusCode = response.getStatusCode();
	Assert.assertEquals(statusCode, 201);
	String post_response = response.body().asString();
	System.out.println("Response body: " + post_response);
	System.out.println(id);
	return id;
	}
	
	@Test
	public void fetchOrder()
	{
	RestAssured.baseURI = "http://localhost:51544/v1";
	RequestSpecification request = RestAssured.given();
	Response response1 = request.get("/orders/"+ id);
	int statusCode1 = response1.getStatusCode();
	Assert.assertEquals(statusCode1, 200);

	Response response2 = request.get("/orders/1000s");
	int statusCode2 = response2.getStatusCode();
	Assert.assertEquals(statusCode2, 404);
	System.out.println("this is fetch order id " + id);
	}
	
	@Test
	public void assignOrder()
	{
	RestAssured.baseURI = "http://localhost:51544/v1";
	
	RequestSpecification request = RestAssured.given();
	
	Response response1 = request.put("/orders/"+ id+"/take");
	
	int statusCode1 = response1.getStatusCode();
	Assert.assertEquals(statusCode1, 200);
	JsonPath jsonPathEvaluator = response1.jsonPath();
	String assign_status = jsonPathEvaluator.get("status");
	Assert.assertEquals(assign_status, "ONGOING");
	System.out.println(assign_status);
	System.out.println("this is assign order id " + id);
	
	Response response2 = request.put("/orders/1000s/take");
	int statusCode2 = response2.getStatusCode();
	Assert.assertEquals(statusCode2, 404);
	}
	
	@Test
	public void completeOrder()
	{
	RestAssured.baseURI = "http://localhost:51544/v1";
	
	RequestSpecification request = RestAssured.given();
	
	Response response1 = request.put("/orders/"+id+"/complete");
	int statusCode1 = response1.getStatusCode();
	Assert.assertEquals(statusCode1, 200);
	JsonPath jsonPathEvaluator = response1.jsonPath();
	String assign_status = jsonPathEvaluator.get("status");
	Assert.assertEquals(assign_status, "COMPLETED");
	System.out.println(assign_status);
	System.out.println("this is completed order id " + id);
	
	Response response2 = request.put("/orders/1000s/complete");
	int statusCode2 = response2.getStatusCode();
	Assert.assertEquals(statusCode2, 404);
	}
	
	public void cancelOrder()
	{
	RestAssured.baseURI = "http://localhost:51544/v1";
	
	RequestSpecification request = RestAssured.given();
	
	request.header("Content-Type", "application/json");
	request.body("{\"stops\":[{\"lat\":22.344674,\"lng\":114.124651},{\"lat\":22.375384,\"lng\":114.182446},{\"lat\":22.385669,\"lng\":114.186962}]}");
	Response response = request.post("/orders");
	JsonPath jsonPathEvaluator = response.jsonPath();
	int ids = jsonPathEvaluator.get("id");
	int statusCode = response.getStatusCode();
	Assert.assertEquals(statusCode, 201);
	
	Response response1 = request.put("/orders/"+(ids+"1")+"/cancel");
	int statusCode1 = response1.getStatusCode();
	Assert.assertEquals(statusCode1, 200);
	System.out.println("this is cancelled order id " + ids);
	
	Response response2 = request.put("/orders/1000s/cancel");
	int statusCode2 = response2.getStatusCode();
	Assert.assertEquals(statusCode2, 404);
	}

}
