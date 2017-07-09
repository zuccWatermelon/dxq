package cn.zhangjia.zucc.mycurrencies.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import cn.zhangjia.zucc.mycurrencies.database.DBManager;
import cn.zhangjia.zucc.mycurrencies.module.ExchangeRecord;
import cn.zhangjia.zucc.mycurrencies.util.JSONParser;
import cn.zhangjia.zucc.mycurrencies.util.PrefsMgr;
import cn.zhangjia.zucc.mycurrencies.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //define members that correspond to Views in our layout
    //在布局中定义对视图做出反馈的成员
    private Button mCalcButton;
    private Button historyButton;
    private TextView mConvertedTextView;
    private EditText mAmountEditText;
    private Spinner mForSpinner,mHomSpinner;
    private String[] mCurrencies;
    private DBManager database;

    public static final String FOR = "FOR_CURRENCY";
    public static final String HOM = "HOM_CURRENCY";

    //this will contain my developers key
    // mKey变量表示我的开发者密钥
    private String mKey;
    //used to fetch the 'rates' json object from openexchangerates.org
    //rates变量用来返回网站上的汇率javascript object notation 对象
    public static final String RATES = "rates";
    public static final String URL_BASE =
            "http://openexchangerates.org/api/latest.json?app_id=";
    //used to format data from openexchangerates.org
    //DecimalFormat 用来规范获取的数据
    private static final DecimalFormat DECIMAL_FORMAT = new
            DecimalFormat("#,##0.00000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DBManager(this);
        //unpack ArrayList from the bundle and convert to array
        //从一堆数据中解析出ArrayList，并且转换成数组
        ArrayList<String> arrayList = ((ArrayList<String>)
                getIntent().getSerializableExtra(SplashActivity.KEY_ARRAYLIST));
        Collections.sort(arrayList);
        mCurrencies = arrayList.toArray(new String[arrayList.size()]);

        //assign references to our Views;
        //给视图里的引用赋值
        mConvertedTextView = (TextView) findViewById(R.id.txt_converted);
        mAmountEditText = (EditText) findViewById(R.id.edt_amount);
        mCalcButton = (Button) findViewById(R.id.btn_calc);
        mForSpinner = (Spinner) findViewById(R.id.spn_for);
        mHomSpinner = (Spinner) findViewById(R.id.spn_hom);

        //controller:mediates model and view
        //控制器：调解模式和视图
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                //context 上下文
                this,
                //view:layout you see when the spinner is closed
                //视图：当下拉框关闭时输出你所看到的
                R.layout.spinner_closed,
                //model:the array of Strings 模式：字符串的数组
                mCurrencies
        );
        //view:layout you see when the spinner is open
        //视图：当下拉框打开时输出你所看到的
        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        //assign adapters to spinners 把适配器的值赋值给下拉框
        mHomSpinner.setAdapter(arrayAdapter);
        mForSpinner.setAdapter(arrayAdapter);

        mHomSpinner.setOnItemSelectedListener(this);
        mForSpinner.setOnItemSelectedListener(this);

        //set to shared-preferences or pull from shared-preferences on restart
        //开始分享引用或者重启时把分享引用拉过来
        if(savedInstanceState == null && (PrefsMgr.getString(this,FOR) == null && PrefsMgr.getString(this,HOM) == null)){
            mForSpinner.setSelection(findPositionGivenCode("USD",mCurrencies));
            mHomSpinner.setSelection(findPositionGivenCode("CNY",mCurrencies));

            PrefsMgr.setString(this,FOR,"USD");
            PrefsMgr.setString(this,HOM,"CNY");
        }else {
            mForSpinner.setSelection(findPositionGivenCode(PrefsMgr.getString(this,FOR),mCurrencies));
            mHomSpinner.setSelection(findPositionGivenCode(PrefsMgr.getString(this,HOM),mCurrencies));
        }
        mCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CurrencyConverterTask().execute(URL_BASE+mKey);
            }
        });
        mKey = getKey("open_key");

        historyButton = (Button)findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("查询记录");
                String recordString = "";
                List<ExchangeRecord> exchangeRecords = database.queryAllExchangeRecord();
                for(ExchangeRecord exchangeRecord : exchangeRecords){
                    recordString += exchangeRecord.getForeignName() + " " + exchangeRecord.getForeignCurrency() + " to " + exchangeRecord.getHomeName() + " " + exchangeRecord.getHomeCurrency() + "\n";
                }
                builder.setMessage(recordString);
                builder.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //应用的最后一个Activity关闭时应该释放DB
        database.closeDB();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.mnu_invert:
                //TODO define behavior here
                invertCurrencies();
                break;
            case R.id.mnu_codes:
                //TODO define behavior here
                launchBrowser(SplashActivity.URL_CODES);
                break;
            case R.id.mnu_exit:
                finish();
                break;
        }
        return true;
        // return super.onPrepareOptionsMenu(item);返回
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    private void launchBrowser(String strUri) {
        if (isOnline()) {
            Uri uri = Uri.parse(strUri);
            //call an implicit intent
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
    private void invertCurrencies() {
        int nFor = mForSpinner.getSelectedItemPosition();
        int nHom = mHomSpinner.getSelectedItemPosition();
        mForSpinner.setSelection(nHom);
        mHomSpinner.setSelection(nFor);
        mConvertedTextView.setText("");

        PrefsMgr.setString(this, FOR, extractCodeFromCurrency((String)
                mForSpinner.getSelectedItem()));
        PrefsMgr.setString(this, HOM, extractCodeFromCurrency((String)
                mHomSpinner.getSelectedItem()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.spn_for:
                PrefsMgr.setString(this, FOR,
                        extractCodeFromCurrency((String)mForSpinner.getSelectedItem()));
                break;

            case R.id.spn_hom:
                PrefsMgr.setString(this, HOM,
                        extractCodeFromCurrency((String)mHomSpinner.getSelectedItem()));
                break;

            default:
                break;
        }

        mConvertedTextView.setText("");

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int findPositionGivenCode(String code, String[] currencies) {

        for (int i = 0; i < currencies.length; i++) {
            if (extractCodeFromCurrency(currencies[i]).equalsIgnoreCase(code)) {
                return i;
            }
        }
        //default
        return 0;
    }

    private String extractCodeFromCurrency(String currency){
        return (currency).substring(0,3);
    }

    private String getKey(String keyName){
        AssetManager assetManager = this.getResources().getAssets();
        Properties properties = new Properties();
        try {
            InputStream inputStream = assetManager.open("keys.properties");
            properties.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  properties.getProperty(keyName);

    }
    private class CurrencyConverterTask extends AsyncTask<String, Void, JSONObject> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Calculating Result...");
            progressDialog.setMessage("One moment please...");
            progressDialog.setCancelable(true);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CurrencyConverterTask.this.cancel(true);
                            progressDialog.dismiss();
                        }
                    });
            progressDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            return new JSONParser().getJSONFromUrl(params[0]);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            double dCalculated = 0.0;
            String strForCode =
                    extractCodeFromCurrency(mCurrencies[mForSpinner.getSelectedItemPosition()]);
            String strHomCode = extractCodeFromCurrency(mCurrencies[mHomSpinner.
                    getSelectedItemPosition()]);
            String strAmount = mAmountEditText.getText().toString();
            try {
                if (jsonObject == null){
                    throw new JSONException("no data available.");
                }
                JSONObject jsonRates = jsonObject.getJSONObject(RATES);
                if (strHomCode.equalsIgnoreCase("USD")){
                    dCalculated = Double.parseDouble(strAmount) / jsonRates.getDouble(strForCode);
                } else if (strForCode.equalsIgnoreCase("USD")) {
                    dCalculated = Double.parseDouble(strAmount) * jsonRates.getDouble(strHomCode) ;
                }
                else {
                    dCalculated = Double.parseDouble(strAmount) * jsonRates.getDouble(strHomCode)
                            / jsonRates.getDouble(strForCode) ;
                }
            } catch (JSONException e) {
                Toast.makeText(
                        MainActivity.this,
                        "There's been a JSON exception: " + e.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                mConvertedTextView.setText("");
                e.printStackTrace();
            }
            mConvertedTextView.setText(DECIMAL_FORMAT.format(dCalculated) + " " + strHomCode);
            progressDialog.dismiss();

            //for testing
            //if (mCurrencyTaskCallback != null) {
                //mCurrencyTaskCallback.executionDone();
            //}
            ExchangeRecord exchangeRecord = new ExchangeRecord();
            exchangeRecord.setForeignName(strForCode);
            exchangeRecord.setForeignCurrency(Double.parseDouble(strAmount));
            exchangeRecord.setHomeName(strHomCode);
            exchangeRecord.setHomeCurrency(dCalculated);
            database.addExchangeRecord(exchangeRecord);
        }
    }

}
