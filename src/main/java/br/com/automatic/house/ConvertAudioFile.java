package br.com.automatic.house;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ConvertAudioFile {

	public void convertToWav(String path){
		String apiKey = "ac68ae6007cd3167ee8605ce863ff83d139029ec";
        String endpoint = "https://sandbox.zamzar.com/v1/jobs";
        String sourceFile = path;
        System.out.println(sourceFile);
        String targetFormat = "wav";

        // Create HTTP client and request object
        CloseableHttpClient httpClient = getHttpClient(apiKey);
        HttpEntity requestContent = MultipartEntityBuilder.create()
            .addPart("source_file", new FileBody(new File(sourceFile)))
            .addPart("target_format", new StringBody(targetFormat, ContentType.TEXT_PLAIN))
            .build();
        HttpPost request = new HttpPost(endpoint);
        request.setEntity(requestContent);

        try{
	        // Make request
	        CloseableHttpResponse response = httpClient.execute(request);
	
	        // Extract body from response
	        HttpEntity responseContent = response.getEntity();
	        String result = EntityUtils.toString(responseContent, "UTF-8");
	
	        // Parse result as JSON
	        JSONObject json = new JSONObject(result);
	
	        // Print result
	        System.out.println(json);
	
	        // Finalise response and client
	        response.close();
	        httpClient.close();
	        System.out.println(json.getInt("id"));
	        JSONObject verifyJson = null;
	        
	        do{
	        	verifyJson = verifyFile(json.getInt("id"));
	        } while(!verifyJson.getString("status").equals("successful"));
	        
	        convertToWav(verifyJson.getJSONArray("target_files").getJSONObject(0).getInt("id"));
        }
        catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static JSONObject verifyFile(int jobId){
        String apiKey = "ac68ae6007cd3167ee8605ce863ff83d139029ec";
        String endpoint = "https://sandbox.zamzar.com/v1/jobs/" + jobId;

        // Create HTTP client and request object
        CloseableHttpClient httpClient = getHttpClient(apiKey);
        HttpGet request = new HttpGet(endpoint);
        
		try{
			// Make request
	        CloseableHttpResponse response = httpClient.execute(request);

	        // Extract body from response
	        HttpEntity responseContent = response.getEntity();
	        String result = EntityUtils.toString(responseContent, "UTF-8");

	        // Parse result as JSON
	        JSONObject json = new JSONObject(result);

	        // Print result
	        System.out.println(json);

	        // Finalise response and client
	        response.close();
	        httpClient.close();	 
	        return json;
		}
		catch (Exception e) {
			e.printStackTrace();
		}      
		return null;
	}
	
	private static void convertToWav(int fileId){
        String apiKey = "ac68ae6007cd3167ee8605ce863ff83d139029ec";
        String endpoint = "https://sandbox.zamzar.com/v1/files/" + fileId + "/content";
        String localFilename = "/home/fellipe/Projetos/recording.wav";

        // Create HTTP client and request object
        CloseableHttpClient httpClient = getHttpClient(apiKey);
        HttpGet request = new HttpGet(endpoint);

        try{
        	// Make request
            CloseableHttpResponse response = httpClient.execute(request);

            // Extract body from response
            HttpEntity responseContent = response.getEntity();

            // Save response content to file on local disk
            BufferedInputStream bis = new BufferedInputStream(responseContent.getContent());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFilename));
            int inByte;
            while((inByte = bis.read()) != -1) {
                bos.write(inByte);
            }

            // Print success message
            System.out.println("File downloaded");

            // Finalise response, client and streams
            response.close();
            httpClient.close();
            bos.close();
            bis.close();
        }
        catch (Exception e) {
			e.printStackTrace();
		}        
	}
	
	private static CloseableHttpClient getHttpClient(String apiKey) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(apiKey, ""));

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        return httpClient;
    }
}
