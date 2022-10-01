package wiremockit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class StubForPost {
	
	public void setStatus() {
				
		stubFor(
				post(urlEqualTo("/api/table/issuesxml"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/XML")
						.withBody("<response><result><id>2000</id></result></response>")
						.withStatus(201)));
		
		stubFor(
				post(urlEqualTo("/api/table/issuesjson"))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/JSON")
						.withBody("{result: \"success\"}")
						.withStatus(200)));
	}
	
	@Test
	public void validateStatus() {
		
		setStatus();		
		
		RestAssured.baseURI = "http://localhost:8080/api/table/issuesjson";
		Response resp = RestAssured.post();
		
		System.out.println(resp.getStatusCode());
		System.out.println(resp.getContentType());
		
		System.out.println(resp.asPrettyString());
	}

}
