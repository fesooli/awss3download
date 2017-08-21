package br.com.automatic.house;

import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.util.IOUtils;

public class DownloadS3AudioFile {

	public static String download(){
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("fellipe.oliveira").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/home/fellipe/.aws/credentials), and is in valid format.",
                    e);
        }
        AmazonS3 s3 = new AmazonS3Client(credentials);
		try {
			com.amazonaws.services.s3.model.S3Object object = s3.getObject(new GetObjectRequest(Constants.BUCKET_NAME, "fala.txt"));
			String fala = IOUtils.toString(object.getObjectContent());
			return fala;
			//transformToText(data);
		} catch (Exception e) {
			//e.printStackTrace();
			verifyS3File();
		}
		return null;
	}
	
	public static void verifyS3File(){
		download();
	}
	
	/*public static void transformToText(byte[] data){
		try{
			// Instantiates a client
		    SpeechClient speech = SpeechClient.create();
		    ByteString audioBytes = ByteString.copyFrom(data);

		    // Builds the sync recognize request
		    RecognitionConfig config = RecognitionConfig.newBuilder()
		        .setEncoding(AudioEncoding.LINEAR16)
		        .setSampleRateHertz(48000)
		        .setLanguageCode("pt-BR")
		        .build();
		    RecognitionAudio audio = RecognitionAudio.newBuilder()
		        .setContent(audioBytes)
		        .build();

		    // Performs speech recognition on the audio file
		    RecognizeResponse response = speech.recognize(config, audio);
		    List<SpeechRecognitionResult> results = response.getResultsList();

		    for (SpeechRecognitionResult result: results) {
		      List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
		      for (SpeechRecognitionAlternative alternative: alternatives) {
		    	  KeyWords keys = new KeyWords();
		    	  for(String k : keys.getKeysWords().keySet()){
		    		  if(alternative.getTranscript().toLowerCase().contains(k)){
		    			  //System.out.println(keys.getKeysWords().get(k)); 
		    		  }
		    	  }
		    	  
		        System.out.printf("Transcription: %s%n", alternative.getTranscript());
		      }
		    }
		    speech.close();
		    
		    AWSCredentials credentials = null;
	        try {
	            credentials = new ProfileCredentialsProvider("fellipe.oliveira").getCredentials();
	        } catch (Exception e) {
	            throw new AmazonClientException(
	                    "Cannot load the credentials from the credential profiles file. " +
	                    "Please make sure that your credentials file is at the correct " +
	                    "location (/home/fellipe/.aws/credentials), and is in valid format.",
	                    e);
	        }
		    AmazonS3 s3 = new AmazonS3Client(credentials);
			com.amazonaws.services.s3.model.S3Object object = s3.getObject(new GetObjectRequest(Constants.BUCKET_NAME, "recording.wav"));
		    //s3.deleteObject(new DeleteObjectRequest(Constants.BUCKET_NAME, "recording.3gp"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}*/
	
	public static void textValidation(String text){
		if(text != null){
			text = text.toLowerCase();
			if(text.contains("ligar") || text.contains("desligar")){
				executeCommand("irsend device SEND_ONCE KEY_POWER");
			}
		}
	}
	
	public static void executeCommand(String command){
		Runtime run = Runtime.getRuntime();
		try {
			Process process = run.exec(command); //comando a ser executado
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		long tempoInicio = System.currentTimeMillis();
		String fala = download();
		textValidation(fala);
		//ConvertAudioFile convert = new ConvertAudioFile();
		//convert.convertToWav("/home/fellipe/Projetos/recording.3gp");
		//transformToText("/home/fellipe/Projetos/recording.wav");
		
		System.out.println("Tempo Total: " + (System.currentTimeMillis()-tempoInicio) /1000);
	}
}
