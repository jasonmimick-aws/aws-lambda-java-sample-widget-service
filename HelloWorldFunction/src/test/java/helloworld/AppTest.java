package helloworld;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class AppTest {
  @Test
  public void successfulResponse() {
    App app = new App();
    APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
    //Map<String,String> query = new HashMap<String,String>();
    //query.put("bucket","wed-green1-bucket");
    String body = " { \"bucket\": \"wed-green1-bucket\" }";
    input.setBody( body );
    APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
    assertEquals(200, result.getStatusCode().intValue());
    assertEquals("application/json", result.getHeaders().get("Content-Type"));
    String content = result.getBody();
    assertNotNull(content);
    //assertTrue(content.contains("\"body\""));
    //assertTrue(content.contains("\"message\""));
    //assertTrue(content.contains("\"halloween world\""));
    //assertTrue(content.contains("\"location\""));
  }
}
