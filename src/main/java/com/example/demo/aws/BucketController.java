package com.example.demo.aws;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@CrossOrigin
@RestController
public class BucketController {

    private final AmazonClient amazonClient;

    @Autowired
    BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }


    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public void uploadFile(@RequestParam("file") MultipartFile file) {
      this.amazonClient.uploadFile(file);
     // this.amazonClient.detectTextOnImg(amazonClient.generateFileName(file));
      }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonClient.deleteFileFromS3Bucket(fileUrl);
    }

    @GetMapping("myData/{img}")
    public List<String> getData(@PathVariable String img){
        System.out.println("Imma here");

      return amazonClient.detectTextOnImg(img);
   }
}