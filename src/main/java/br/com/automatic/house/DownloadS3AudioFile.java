package br.com.automatic.house;

import java.io.FileOutputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.util.IOUtils;

public class DownloadS3AudioFile {

	public static void download(){
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/home/fellipe/.aws/credentials), and is in valid format.",
                    e);
        }
        AmazonS3 s3 = new AmazonS3Client(credentials);
		com.amazonaws.services.s3.model.S3Object object = s3.getObject(new GetObjectRequest(Constants.BUCKET_NAME, "recording.3gp"));
		try {
			IOUtils.copy(object.getObjectContent(), new FileOutputStream("C:/Users/Fellipe/Desktop/recording.3gp"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {
		download();
	}
}
