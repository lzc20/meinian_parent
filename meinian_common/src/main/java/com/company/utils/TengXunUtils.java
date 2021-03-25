package com.company.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.InputStream;

;


public class TengXunUtils {


    public static String secretId = "AKID3dJwR5OWREr7IKFtSr6BwYDj5INJmOel";
    public static String secretKey = "HfeXGqLiKpCSYVaXxJj0883wQm0U5Zu5";
    public static String bucketName = "1130-1305243143";





    public static void upload(InputStream fileInputStream, String fileName){

        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-nanjing");
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType("application/pdf");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName,fileInputStream, objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        String etag = putObjectResult.getETag();


    }


}
