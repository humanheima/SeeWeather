package util;

import android.app.Activity;
import android.app.ProgressDialog;

import com.example.humanweather.R;

/**
 * չʾ���ȶԻ���
 * @author human
 *
 */
public class ProDialogUtil {

	private  ProgressDialog progressDialog;
	private Activity activity;
	public ProDialogUtil(Activity activity) {
		this.activity=activity;
	}
	public ProgressDialog showProgressDialog(){
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(activity);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage(activity.getString(R.string.wait));
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
		return progressDialog;
	}
	/**
	 * �رս��ȶԻ���
	 */
	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
}
