package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**实时天气数据库对应的helper
 * @author dumignwei
 *
 */
public class WeatherCityOpenHelper extends SQLiteOpenHelper {

	public static final int VERSION = 1;
	/**构造函数
	 * @param context
	 */
	public WeatherCityOpenHelper(Context context) {
		super(context, WeatherCityDB.DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
