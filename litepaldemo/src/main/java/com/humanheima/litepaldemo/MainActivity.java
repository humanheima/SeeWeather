package com.humanheima.litepaldemo;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn1, btn2;
    String result = "";
    Gson gson;
    JsonReader jsonReader;
    List<CityInfo> cityInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.bt2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        gson = new Gson();
        AssetManager manager = getAssets();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1://
                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        jsonReader = new JsonReader(new StringReader(getFromAssets("cityid.json")));
                        jsonReader.setLenient(true);
                        cityInfoList = gson.fromJson(jsonReader, new TypeToken<CityInfo>() {
                        }.getType());
                        if (cityInfoList.size() > 0) {
                            Log.e("hehe", "read success");
                            return true;
                        } else {
                            Log.e("hehe", "read error");
                            return false;
                        }
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if (aBoolean) {
                            //存数据库
                            Log.e("hehe", "save data");
                            // SQLiteDatabase db= Connector.getWritableDatabase();
                            DataSupport.saveAll(cityInfoList);
                        }
                    }
                }.execute();
                break;
            case R.id.bt2:
                break;
        }
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
