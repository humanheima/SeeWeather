package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.FeatureWeatherModel;

/**
 * 未来天气模型
 */
public class FeatureWeatherDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "Feature_weather";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static FeatureWeatherDB featureWeatherDB;

    /**
     * 数据库
     */
    private SQLiteDatabase db;

    /**
     * 构造方法私有化
     *
     * @param context
     */
    private FeatureWeatherDB(Context context) {
        FeatureWeatherOpenHelper featureDBHelper = new FeatureWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = featureDBHelper.getWritableDatabase();
    }

    /**
     * 得到数据库实例的方法
     *
     * @param context
     * @return
     */
    public synchronized static FeatureWeatherDB getInstance(Context context) {

        if (featureWeatherDB == null) {
            featureWeatherDB = new FeatureWeatherDB(context);
        }
        return featureWeatherDB;
    }

    /**
     * 保存FeatureWeathermodel实例,
     */
    public void saveFeatureWeathermodel(FeatureWeatherModel featureWeathermodel) {
        if (featureWeathermodel != null) {
            ContentValues values = new ContentValues();
            values.put("days", featureWeathermodel.getDays());
            values.put("week", featureWeathermodel.getWeek());
            values.put("citynm", featureWeathermodel.getCitynm());
            values.put("temperature", featureWeathermodel.getTemperature());
            values.put("weather", featureWeathermodel.getWeather());
            //values.put("wind", featureWeathermodel.getWind());
            //values.put("winp", featureWeathermodel.getWinp());

            // 插入到FeatureWeather表中
            db.insert("FeatureWeather", null, values);

        }

    }

    /**
     * 删除数据库中所有的记录
     */
    public void deleteDBRecode(String citynm) {

        db.delete("FeatureWeather", "citynm = ?", new String[]{citynm});
    }

    /*
     * /** 从数据库读取所有未来的天气信息
     * @return
     */
    public List<FeatureWeatherModel> loadFeatureOneDayWeather(String citynm) {
        List<FeatureWeatherModel> list = new ArrayList<FeatureWeatherModel>();
        //Cursor cursor = db.query("FeatureWeather", null, null, null, null,null, null);
        Log.e("TAG", "in FeatureWeatherDB" + citynm);
        Cursor cursor = db.query("FeatureWeather", null, " citynm = ? ", new String[]{citynm}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                FeatureWeatherModel featureWeathermodel = new FeatureWeatherModel();

                featureWeathermodel.setDays(cursor.getString(cursor.getColumnIndex("days")));
                // // 将日期2015-10-10 改成 10-10
                // String string =
                // cursor.getString(cursor.getColumnIndex("days"));
                // String[] strings = string.split("20..-");
                //
                // String days = strings[1];
                // featureWeathermodel.setDays(days);

                featureWeathermodel.setWeek(cursor.getString(cursor.getColumnIndex("week")));

                featureWeathermodel.setCitynm(cursor.getString(cursor.getColumnIndex("citynm")));

                featureWeathermodel.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));

                featureWeathermodel.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
                list.add(featureWeathermodel);

            } while (cursor.moveToNext());
        }
        return list;
    }

}
