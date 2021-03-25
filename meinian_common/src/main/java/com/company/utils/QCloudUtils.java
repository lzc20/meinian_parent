package com.company.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.io.InputStream;

public class QCloudUtils {
    public  static String secretId = "AKIDDK7ZXycPbv0rlAxlfX2IRM3X8cqdBVYN";
    public  static String secretKey = "qm5kANM5hldGhy5fsqa6Ld9RSRBhwwgH";
    public  static String bucket = "1130java-1305236362";

    public static void uploadQCloud(String localFilePath,String fileName){
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
// 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
// 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 指定要上传的文件
        File localFile = new File(localFilePath);
// 指定要上传到 COS 上对象键
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,fileName,localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

    }
    public static void uploadQCloud(InputStream fileInputStream, String fileName){
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred,clientConfig);
        ObjectMetadata objectMetadata = new ObjectMetadata();
//      设置输入流长度为500
//      objectMetadata.setContentLength(1024*1024);
//      设置 Content type, 默认是 application/octet-stream
        objectMetadata.setContentType("image/jpeg");
        PutObjectResult putObjectResult = cosClient.putObject(bucket,fileName,fileInputStream,objectMetadata);
        String etag = putObjectResult.getETag();
    }


//    //上传文件
//    public static void uploadQCloud(byte[] bytes, String fileName){
//        //构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone2());
//        //...其他参数参考类注释
//        UploadManager uploadManager = new UploadManager(cfg);
//
//        //默认不指定key的情况下，以文件内容的hash值作为文件名
//        String key = fileName;
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//        try {
//            Response response = uploadManager.put(bytes, key, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }
//    }

    //删除文件
    public static void deleteFileFromQCloud(String fileName){
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        cosClient.deleteObject(bucket,fileName);

//        //构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone2());
//        String key = fileName;
//        Auth auth = Auth.create(accessKey, secretKey);
//        BucketManager bucketManager = new BucketManager(auth, cfg);
//        try {
//            bucketManager.delete(bucket, key);
//        } catch (QiniuException ex) {
//            //如果遇到异常，说明删除失败
//            System.err.println(ex.code());
//            System.err.println(ex.response.toString());
//        }
    }

    /*
    @Test
    public void test2() throws IOException {
        //QiniuUtils.upload2Qiniu("D:\\temp\\90\\jjy94.jpg","jjy94.jpg");

        //QiniuUtils.deleteFileFromQiniu("jjy94.jpg");

        File file = new File("D:\\temp\\90\\jjy94.jpg");
        //init array with file length
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray); //read file into bytes[]
        QiniuUtils.upload2Qiniu(bytesArray,"jjy94.jpg");
    }*/
}
