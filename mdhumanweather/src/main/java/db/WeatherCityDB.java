package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.humanweather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.WeatherCityModel;

/**实时天气模型对应的数据库
 *
 */
public class WeatherCityDB {

	private Context context;
	/**
	 * 数据库名
	 */
	public static final String DB_NAME = "Weather_city.db";
	/**
	 * 数据库版本
	 */

	private SQLiteDatabase db;

	private WeatherCityOpenHelper helper;

	private final int BUFFER_SIZE = 4096;// 4KB的大小
	public static final String PACKAGE_NAME = "com.example.humanweather";

	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME + "/databases";

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public WeatherCityDB(Context context) {

		this.context = context;
		helper = new WeatherCityOpenHelper(this.context);
		copyDatabase();
	}

	/**
	 * 把res目录下的raw目录下的数据库文件复制到指定位置
	 * 
	 * @return
	 */
	public boolean copyDatabase() {

		File filedb = new File(DB_PATH + "/" + DB_NAME);
		if (!filedb.exists()) {
			File file = new File(DB_PATH);
			if (!file.isDirectory())
				file.mkdir();
			String dbfile = DB_PATH + "/" + DB_NAME;

			InputStream is = null;
			FileOutputStream fos = null;
			try {
				if (new File(dbfile).length() == 0) {

					is = this.context.getResources().openRawResource(
							R.raw.weather_city);
					fos = new FileOutputStream(dbfile);

					byte[] buffer = new byte[BUFFER_SIZE];
					int len = -1;
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}

				}
			} catch (Exception e) {

				e.printStackTrace();
				return false;
			} finally {
				try {
					if (fos != null) {
						fos.close();
						fos.flush();
					}
					if (is != null) {
						is.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
					return false;
				}
			}
			return true;

		} else {

			return true;
		}

	}

	/**
	 * 从数据库里查询相应的weaid
	 * 
	 * @param citynm
	 * @return
	 */
	public String queryWeaid(String citynm) {

		db = helper.getReadableDatabase();
		String weaid = "";
		String sql = "select weaid from WeatherCity where citynm =?";
		Cursor cr = db.rawQuery(sql, new String[] { citynm });
		if (cr.moveToFirst()) {
			
			weaid = cr.getString(cr.getColumnIndex("weaid"));
			Log.e("TAG", "weaid====" + weaid);
		}
		cr.close();
		//cr.moveToFirst();
		return weaid;
	}

	/**
	 * 从数据库读取所有城市的信息
	 * 
	 * @return
	 */
	public List<WeatherCityModel> loadCity() {

		db = helper.getReadableDatabase();
		List<WeatherCityModel> list = new ArrayList<WeatherCityModel>();
		Cursor cursor = db.query("WeatherCity", new String[] { "weaid",
				"citynm", "cityno" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				WeatherCityModel weatherCityModel = new WeatherCityModel();
				// weatherCityModel.setId(cursor.getInt(cursor.getColumnIndex("id")));

				weatherCityModel.setWeaid(cursor.getString(cursor
						.getColumnIndex("weaid")));

				// weatherCityModel.setCityid(cursor.getString(cursor.getColumnIndex("cityid")));

				weatherCityModel.setCitynm(cursor.getString(cursor
						.getColumnIndex("citynm")));

				weatherCityModel.setCityno(cursor.getString(cursor
						.getColumnIndex("cityno")));

				list.add(weatherCityModel);

			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 将WeatherCityModel实例存到数据库
	 * 
	 * @param weatherCityModel
	 */
	// public void saveWeatherCityModel(WeatherCityModel weatherCityModel) {
	//
	// if (weatherCityModel != null) {
	//
	// ContentValues values = new ContentValues();
	// values.put("weaid", weatherCityModel.getWeaid());
	// values.put("cityid", weatherCityModel.getCityid());
	// values.put("citynm", weatherCityModel.getCitynm());
	// values.put("cityno", weatherCityModel.getCityno());
	//
	// db.insert("WeatherCity", null, values);
	// }
	// }

}
