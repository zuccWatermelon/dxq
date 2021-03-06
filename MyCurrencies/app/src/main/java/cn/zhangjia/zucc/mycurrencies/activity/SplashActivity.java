package cn.zhangjia.zucc.mycurrencies.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cn.zhangjia.zucc.mycurrencies.util.JSONParser;
import cn.zhangjia.zucc.mycurrencies.R;

public class SplashActivity extends AppCompatActivity {

    //url to currency codes used in this application
    //Uniform Resource Locator 统一资源定位符；这个应用中被使用的汇率代码的url
    public static final String URL_CODES = "http://openexchangerates.org/api/currencies.json";
    public static final String KEY_ARRAYLIST = "key_arraylist";
    //ArrayList of currencies that will be fetched and passed into MainActivity
    //被获取并反馈发送给MainActivity的汇率数组链表
    private ArrayList<String> mCurrencies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        new FetchCodesTask().execute(URL_CODES);

    }

    private class FetchCodesTask extends AsyncTask<String, Void, JSONObject> {//异步

        @Override
        protected JSONObject doInBackground(String... params) {
            return new JSONParser().getJSONFromUrl(params[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject == null) {
                    throw new JSONException("no data available.");
                }
                Iterator iterator = jsonObject.keys(); //iterator 迭代器，迭代程序;
                String key = "";
                mCurrencies = new ArrayList<String>();
                while (iterator.hasNext()) {
                    key = (String) iterator.next();
                    mCurrencies.add(key + " | " + jsonObject.getString(key));
                    Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                    mainIntent.putExtra(KEY_ARRAYLIST,mCurrencies);
                    startActivity(mainIntent);
                }
                finish();
            } catch (JSONException e) {
                Toast.makeText(
                        SplashActivity.this,
                        "There has been a JSON exception: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                e.printStackTrace();
                finish();
            }
        }
    }


}

