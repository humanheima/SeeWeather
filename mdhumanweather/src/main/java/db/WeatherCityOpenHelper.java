package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**ʵʱ�������ݿ��Ӧ��helper
 * @author dumignwei
 *
 */
public class WeatherCityOpenHelper extends SQLiteOpenHelper {

	public static final int VERSION = 1;
	/**���캯��
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
