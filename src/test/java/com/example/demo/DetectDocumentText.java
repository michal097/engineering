package com.example.demo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.S3Object;
import lombok.var;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DetectDocumentText {


    public static void main(String[] args) throws IOException {


        AWSCredentialsProvider creds = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return new AWSCredentials() {
                    @Override
                    public String getAWSAccessKeyId() {
                        return "AKIA4NZEJBT6G7NFGKFX";
                    }

                    @Override
                    public String getAWSSecretKey() {
                        return "+Nn+EDNR9JfT0oCmMs++q4S9rIPbRYNvdoPkoOsg";
                    }
                };
            }

            @Override
            public void refresh() {

            }
        };

        String document = "Slusarczyk2.jpg";
        String bucket = "enigeeringbucket";


        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("https://s3.amazonaws.com", "us-east-2"))
                .build();


        // Get the document from S3
        com.amazonaws.services.s3.model.S3Object s3object = s3client.getObject(bucket, document);
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        BufferedImage image = ImageIO.read(inputStream);

        // Call DetectDocumentText
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
                "https://textract.us-east-2.amazonaws.com", "us-east-2");
        AmazonTextract client = AmazonTextractClientBuilder.standard().withCredentials(creds)
                .withEndpointConfiguration(endpoint).build();


        com.amazonaws.services.textract.model.DetectDocumentTextRequest request = new
                com.amazonaws.services.textract.model.DetectDocumentTextRequest()
                .withDocument(new
                        com.amazonaws.services.textract.model.Document().withS3Object(new S3Object().withName(document).withBucket(bucket)));

        DetectDocumentTextResult result = client.detectDocumentText(request);
        var blocks = result.getBlocks();
        List<String> list = new ArrayList<>();
        for (Block b : blocks) {
            String text = "";
            if (b.getBlockType().equals("LINE")) {
                text += b.getText();
            }
            if(!text.isEmpty())
            list.add(text);
        }
        list.forEach(System.out::println);
    }
}

