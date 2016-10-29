package com.humanheima.camerademo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.img)
    ImageView img;
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 3;
    private static final int CROP_PHOTO = 2;
    private Uri photoURI;
    private String mCurrentPhotoPath;
    private File photoFile = null;
    private File desFile = null;
    private Uri desUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button1)
    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("tag", "图片文件创建出错" + ex.getMessage());
                return;
            }
            if (photoFile != null) {
                photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO);
            }
        }
    }

    @OnClick(R.id.button2)
    public void chooseFromAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item fromthe data
        intent.setType("image/*");//从所有图片中进行选择
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        desFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e("tag", "图片文件创建出错" + ex.getMessage());
                        return;
                    }
                    if (desFile != null) {
                        if (copyImage(photoFile, desFile)) {
                            desUri = Uri.fromFile(desFile);
                            Intent intent = new Intent("com.android.camera.action.CROP");
                            intent.setDataAndType(desUri, "image/*");
                            intent.putExtra("scale", true);
                            intent.putExtra("crop", true);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
                            startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序*/
                        }
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    /*使用这种方式获取imgPath 因为手机返回的url类型不一致
                    * content://media/external/images/media/101
                     file:///storage/emulated/0/DCIM/Camera/IMG_20161028_125534.jpg*/
                    String imgPath;
                    if (data.getData().toString().startsWith("content")) {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(data.getData(), proj, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        imgPath = cursor.getString(column_index);
                        cursor.close();
                    } else {
                        imgPath = data.getData().getPath();
                    }
                    try {
                        desFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("tag", "图片文件创建出错" + e.getMessage());
                        return;
                    }
                    if (desFile != null) {
                        if (copyImage(new File(imgPath), desFile)) {
                            desUri = Uri.fromFile(desFile);
                            Intent intent = new Intent("com.android.camera.action.CROP");
                            intent.setDataAndType(desUri, "image/*");
                            intent.putExtra("scale", true);
                            intent.putExtra("crop", true);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
                            startActivityForResult(intent, CROP_PHOTO); // 启动裁剪程序*/
                        }
                    }

                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    galleryAddPic(desUri);
                    Picasso.with(this).load(desUri).into(img);// 将裁剪后的照片显示出来
                }
                break;
            default:
                break;
        }
    }

    /**
     * 发送广播添加到系统图库
     *
     * @param uri
     */
    private void galleryAddPic(Uri uri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(uri);
        MainActivity.this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    /**
     * 把图片复制一份用来剪裁，不然会修改原图
     */
    public boolean copyImage(File oriFile, File desFile) {
        BufferedOutputStream bufos = null;
        BufferedInputStream bufis = null;
        try {
            //源文件
            bufis = new BufferedInputStream(new FileInputStream(oriFile));
            //目标文件
            bufos = new BufferedOutputStream(new FileOutputStream(desFile));
            int len;
            //使用缓冲流循环进行读写操作
            while ((len = bufis.read()) != -1) {
                bufos.write(len);
            }
            bufos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bufos != null)
                    bufos.close();
                if (bufis != null)
                    bufis.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
