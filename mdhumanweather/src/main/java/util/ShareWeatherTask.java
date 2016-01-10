package util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.example.humanweather.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShareWeatherTask extends AsyncTask<Void, Void, Boolean>{

	private Activity activity;
	private ProDialogUtil dialog;
	private File file;
	public ShareWeatherTask(Activity activity){
		this.activity=activity;
		dialog=new ProDialogUtil(activity);
	}
	
	@Override
	protected void onPreExecute() {
		dialog.showProgressDialog();
	}
	@Override
	protected Boolean doInBackground(Void... args) {
		// 截取屏幕并保存
		return saveSnapImgWithBar(activity);
	}


	@Override
	protected void onPostExecute(Boolean result) {
		dialog.closeProgressDialog();
		if (result) {
            //如果图片保存成功
            activity.startActivity(Intent.createChooser(createShareIntent(),activity.getString(R.string.weather_share)));
        } else {
            Toast.makeText(activity, activity.getString(R.string.share_failed), Toast.LENGTH_LONG).show();
        }
	}

	
	 private Intent createShareIntent() {
	        Intent shareIntent = new Intent(Intent.ACTION_SEND);
	        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        shareIntent.setType("image/*");
	        // For a file in shared storage.  For data in private storage, use a ContentProvider.
	       // Uri uri = Uri.fromFile(getFileStreamPath(pathToImage));
	        Uri uri = Uri.fromFile(file);
	        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
	        return shareIntent;
	    }

	/**
     * 截图并保存，保存图片.是有标题栏的截图
     */
    private boolean saveSnapImgWithBar(Activity activity) {
        Bitmap bitmap = ScreenUtils.snapShotWithoutStatusBar(activity);
        //Bitmap bitmap = ScreenUtils.snapShotWithStatusBar(activity);
        boolean succeed = false;
        try {
              file = createImageFile();
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            if (null != fos) {
                succeed = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                //Toast.makeText(this, "截屏文件已保存至SDCard/Pic目录下",Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            succeed = false;
        }
        return succeed;

    }


    /**
     * Creates the image file to which the image must be saved.
     *
     * @return
     * @throws IOException
     */
    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }


	
}
	