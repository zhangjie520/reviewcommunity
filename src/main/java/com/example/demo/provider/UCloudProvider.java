package com.example.demo.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import cn.ucloud.ufile.http.OnProgressListener;
import com.alibaba.fastjson.JSON;
import com.example.demo.exception.CustomErrorCode;
import com.example.demo.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author: codeape
 * @Date: 2021/1/28 23:52
 * @Version: 1.0
 */
@Component
public class UCloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;
    @Value("${ucloud.ufile.private-key}")
    private String privateKey;
    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName="xiaoying";
    @Value("${ucloud.ufile.region}")
    private String region;
    @Value("${ucloud.ufile.suffix}")
    private String suffix;

    public String upload(InputStream fileStream,String  fileName, String mimeType){
        //generate file namel
        String[] filePaths = fileName.split("\\.");
        String generatedFileName;
        if (filePaths.length>1){
            generatedFileName=UUID.randomUUID().toString()+"."+filePaths[filePaths.length-1];
        }else {
            return null;
        }
        ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(publicKey, privateKey);
        ObjectConfig config = new ObjectConfig(region, suffix);
        try {

            PutObjectResultBean response = UfileClient.object(OBJECT_AUTHORIZER, config)
                    .putObject(fileStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();
                    if (response!=null&&response.getRetCode()==0){
                        //return file by downloading
                        String url = UfileClient.object(OBJECT_AUTHORIZER, config)
                                .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, 24*60*60*365*10)
                                .createUrl();
                        return url;
                    }else{
                        throw new CustomException(CustomErrorCode.FILE_UPLOAD_FILE);
                    }
        } catch (UfileClientException e) {
            throw new CustomException(CustomErrorCode.FILE_UPLOAD_FILE);
        } catch (UfileServerException e) {
            throw new CustomException(CustomErrorCode.FILE_UPLOAD_FILE);
        }
    }
}
