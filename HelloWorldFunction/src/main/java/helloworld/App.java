package helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;
/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

   public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {

	    String bucketName = "foobar";

	    if ( input != null ) {
	    	String requestString = input.getBody();
	    	JSONParser parser = new JSONParser();
	    	JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
	    	if (requestJsonObject != null) {
			if (requestJsonObject.get("bucket") != null) {
				bucketName = requestJsonObject.get("bucket").toString();
			} else {
		    		return response.withStatusCode(404).withBody("Parameter 'bucket' required.");
			}
				
	    	}
	    }
            //final String pageContents = this.getPageContents("https://checkip.amazonaws.com");
	    Region region = Region.US_EAST_1;
	    S3Client s3 = S3Client.builder().region(region).build();
	    //final Map<String,String> query;
	    //if ( input != null ) {
	//	query  = input.getQueryStringParameters();	
	  //  } else {
	//	query = new HashMap<String,String>(); 
	  //  }
	    //String p = "";	    
	    //StringBuilder mapAsString = new StringBuilder("\"");
	    //for (String key : query.keySet()) {
		//mapAsString.append(key + "=" + query.get(key) + ", ");
	    //}
	    //mapAsString.append("\"");
	    //if ( ! query.containsKey("bucket") ) {
	    	//query.put("bucket","wed-green1-bucket");

	//	    return response
	//		    .withStatusCode(404)
	//		    .withBody("Parameter 'bucket' required.");
		//    throw new Exception("query parameter 'bucket' required");
	  //  } 
		    
	    //String bucketName = query.get("bucket");
	    List<String> objects = listBucketObjects(s3, bucketName);
	    //String objs = Arrays.toString(objects);
	    String objs = objects.toString();
	    System.out.println("bucket="+bucketName+"Your objects are:"+objs);
            //String output = String.format("{ \"message\": \"halloween world\", \"location\": \"%s\" }", pageContents);
	    //String output = String.format("[\"body\", \"%s\", \"p\", \"%s\"]", body, mapAsString);
	    //
            //return response
            //        .withStatusCode(200)
            //        .withBody(output);
            return response
                    .withStatusCode(200)
                    .withBody(objs);
        //} catch (IOException e) {
        } catch (Exception e) {
	    System.out.println("ERROR =======> " + e);
            return response
                    .withBody(String.format("{ \"%s\" }", e))
                    .withStatusCode(500);
        }
    }


    public static List<String> listBucketObjects(S3Client s3, String bucketName ) {

        List<String> result = new ArrayList<String>();
        try {
           

            ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                result.add( myValue.key() );
                System.out.print("\n The name of the key is " + myValue.key());
                System.out.print("\n The object is " + calKb(myValue.size()) + " KBs");
                System.out.print("\n The owner is " + myValue.owner());
            }

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            throw e;
            //System.exit(1);
        }
        return result;
    }

    //convert bytes to kbs.
    private static long calKb(Long val) {
        return val/1024;
    }



    private String getPageContents(String address) throws IOException{
        URL url = new URL(address);
        try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
