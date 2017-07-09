package cn.zhangjia.zucc.mycurrencies.util;

/**
 * Created by Administrator on 2017/7/1.
 */
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JSONParser {
    //JSON(JavaScript Object Notation, JS 对象标记) 是一种轻量级的数据交换格式。
    //Parser 解析
    static InputStream sInputStream = null;
    static JSONObject sReturnJsonObject = null;
    static String sRawJsonString = "";
    //BufferedReader是缓冲流, INputStream是字节流
    public JSONParser() {}
    public JSONObject getJSONFromUrl(String url) {
    //attempt to get response from server 视图从服务器获取反馈
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            sInputStream = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    //read stream into string-builder 从string-builder读取流
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    sInputStream, "iso-8859-1"), 8);//Buffered 缓冲区
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            sInputStream.close();
            sRawJsonString = stringBuilder.toString();
        } catch (Exception e) {
            Log.e("Error read from Buffer:" + e.toString(), this.getClass().getSimpleName());
        }   //输出缓冲区里的错误信息
        try {
            sReturnJsonObject = new JSONObject(sRawJsonString);
        } catch (JSONException e) {
            Log.e("Parser", "Error when parsing data " + e.toString());
        }//输出解析数据时的发生错误
    //return json object 返回jason对象
        return sReturnJsonObject;
    }
}
