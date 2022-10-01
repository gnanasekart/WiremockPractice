package wiremockit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Transformer {
	
	@Rule
	public WireMockRule wm = new WireMockRule(wireMockConfig().port(8085)
			.extensions(new ResponseTemplateTransformer(false)));
	
	@Before
	public void stubForTransformer() {
		stubFor(post(urlEqualTo("/services"))
				.willReturn(aResponse()
						.withStatus(201)
						.withBody("{result: Listening to port {{request.requestLine.port}}")
						.withTransformers("response-template")));
		
		stubFor(post(urlEqualTo("/services-port"))
				.willReturn(aResponse()
						.withStatus(201)
						.withBody("Listening to port {{.request.description}}")
						.withTransformers("response-template")));
	}
	
	@Test
	public void postServices() {
		RestAssured.baseURI = "http://localhost:8085/services-port";
		Response resp = RestAssured.given().log().all()
									.when()
									.body("{ \"description\" : \"new text\"}")
									.post();
		resp.prettyPrint();
	}
}
