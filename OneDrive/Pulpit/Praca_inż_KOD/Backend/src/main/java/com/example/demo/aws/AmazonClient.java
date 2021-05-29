package com.example.demo.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DetectDocumentTextResult;
import com.amazonaws.services.textract.model.S3Object;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Data
@Slf4j
public class AmazonClient {

    private AmazonS3 s3client;

    @Value("${aws.endpointUrl}")
    private String endpointUrl;

    @Value("${aws.bucketName}")
    private String bucketName;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.textractUrl}")
    private String textractUrl;

    private final AWSCredentialsProvider creds = new AWSCredentialsProvider() {
        @Override
        public AWSCredentials getCredentials() {
            return new AWSCredentials() {
                @Override
                public String getAWSAccessKeyId() {
                    return accessKey;
                }

                @Override
                public String getAWSSecretKey() {
                    return secretKey;
                }
            };
        }

        @Override
        public void refresh() {

        }
    };

    @PostConstruct
    private void initializeAmazon() {
        log.info("Initialization of Amazon Service");
        this.s3client = AmazonS3ClientBuilder.standard().withCredentials(creds).withRegion(region).build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        log.info("Converting multipart to file");
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public String generateFileName(MultipartFile multiPart) {
        String randomSuffix = UUID.randomUUID().toString().substring(0, 4);
        return Objects.requireNonNull(multiPart.getOriginalFilename()).replaceAll(" ", "_").concat(randomSuffix);
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        log.info("Uploading file to s3 bucket");
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile) {

        String fileName = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName(multipartFile);
            uploadFileTos3bucket(fileName, file);
        } catch (Exception e) {
            log.error("error during uploading image to bucket");
            e.printStackTrace();
        }
        return fileName;
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
        return "Successfully deleted";
    }

    public List<String> detectTextOnImg(String photo) {

        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
                textractUrl, region);
        AmazonTextract client = AmazonTextractClientBuilder.standard().withCredentials(creds)
                .withEndpointConfiguration(endpoint).build();
        log.info("start detecting text on image");

        com.amazonaws.services.textract.model.DetectDocumentTextRequest request = new
                com.amazonaws.services.textract.model.DetectDocumentTextRequest()
                .withDocument(new
                        com.amazonaws.services.textract.model.Document().withS3Object(new S3Object().withName(photo).withBucket(bucketName)));
        log.info("getting request from text recognize");
        DetectDocumentTextResult result = client.detectDocumentText(request);
        var blocks = result.getBlocks();

        log.info("gets recognized blocks");

        List<String> list = new ArrayList<>();
        for (Block b : blocks) {
            String text = "";
            if (b.getBlockType().equals("LINE")) {
                text += b.getText();

            }
            if (!text.isEmpty())
                list.add(text);
        }
        return list;
    }
}
