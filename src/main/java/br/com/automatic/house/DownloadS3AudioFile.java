package br.com.automatic.house;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;

public class DownloadS3AudioFile {

	public void download(){
		AmazonS3 s3 = new AmazonS3Client();
		com.amazonaws.services.s3.model.S3Object object = s3.getObject(new GetObjectRequest(Constants.BUCKET_NAME, Constants.COGNITO_POOL_ID));
		object.getObjectContent();
	}	
	
}
