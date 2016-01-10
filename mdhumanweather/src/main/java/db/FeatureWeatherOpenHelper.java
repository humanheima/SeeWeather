package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**δ���������ݿ��Ӧ�����ݿ�helper
 * ʵ����SQLiteOpenHelper��
 * @author dumignwei
 *
 */
public class FeatureWeatherOpenHelper extends SQLiteOpenHelper {

	/**
	 * CREATE_FEATURE_WEATHERINFO �������
	 */
	public static final String CREATE_FEATURE_WEATHERINFO = "create table FeatureWeather ("
			+ "id integer primary key autoincrement,"
			+ "days text,"
			+ "week text,"
			+ "citynm text,"
			+ "temperature text,"
			+ "weather text,"
			+ "wind text,"
			+ "winp text)";

	/**
	 * ���캯��
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public FeatureWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_FEATURE_WEATHERINFO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
