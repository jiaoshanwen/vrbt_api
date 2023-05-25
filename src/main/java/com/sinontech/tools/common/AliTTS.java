package com.sinontech.tools.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.aliyun.dyvmsapi20170525.Client;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsRequest;
import com.aliyun.dyvmsapi20170525.models.SingleCallByTtsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;

import net.sf.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AliTTS {
	private static  String appkey = "oTmPGfN7OFUB1QOR";
	private final static String accessKeyId = "LTAI4G6rz7Bsvf8AN9u2nKwu";
	private final static String accessKeySecret = "lMauHlJcxqbgBuq5yy102fR07TxPKS";
    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dyvmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dyvmsapi.aliyuncs.com";
        return new com.aliyun.dyvmsapi20170525.Client(config);
    }
    
    public static PageData callPhoneWhenSystemWarn(String callCode,String callPhone,String jsonStr){
        PageData rePd = new PageData();
       
        RuntimeOptions runtime = new RuntimeOptions();
        try {
        	 Client client = createClient(accessKeyId, accessKeySecret);
             SingleCallByTtsRequest singleCallByTtsRequest = new SingleCallByTtsRequest()
                     .setTtsCode(callCode)
                     .setCalledNumber(callPhone)
                     .setTtsParam(jsonStr);
            SingleCallByTtsResponse singleCallByTtsResponse = client.singleCallByTtsWithOptions(singleCallByTtsRequest, runtime);
            JSONObject resultBody = JSONObject.fromObject(singleCallByTtsResponse).getJSONObject("body");
            if ( "OK".equals(resultBody.getString("code")) ){
                rePd.put("code", "0");
                rePd.put("mag","呼叫成功");
            }else {
            	 rePd.put("code", "-1");
                 rePd.put("mag","呼叫失败，原因：" + resultBody.toString());
            }
        } catch (Exception error) {
            error.printStackTrace();
            rePd.put("code", "-1");
            rePd.put("mag",error.toString());
            return rePd;
        }
        return rePd;
    }
    
    public static void main(String[] args) {
//    	System.out.println(AliTTS.processPOSTRequest("今天是个好天气，欢迎来到欣网卓信,西湖国际大厦是在文二路391号，古翠路和文二路交叉口。","f://test.mp3", "mp3", "Xiaogang", "0", "50"));
//    	String accessToken = getToken();
//    	System.out.println(accessToken);
    	JSONObject json = new JSONObject();
    	json.put("name", "湖南视频彩铃");
    	System.out.println(callPhoneWhenSystemWarn("TTS_269670008","17316908160",json.toString()));
	}
}