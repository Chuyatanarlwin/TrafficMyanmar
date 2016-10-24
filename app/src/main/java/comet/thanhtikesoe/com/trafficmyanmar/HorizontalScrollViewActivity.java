package comet.thanhtikesoe.com.trafficmyanmar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class HorizontalScrollViewActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout galleryLayout;
    private String photoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "photos";
    private String[] fileList;
    private Uri[] mUrls;
    private int currentIndex;
    private int[]resourceImages;
    private File directoryPath;
    private final int REQUEST_CAMERA_PERMISSION = 200;
    private File storeDirectory;
    protected Bitmap resourceBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_scroll_view);
        int[] largeDrawableImages = new int[]{R.drawable.photo_one, R.drawable.photo_one, R.drawable.photo_one, R.drawable.photo_one,R.drawable.photo_one,R.drawable.photo_one};
        galleryLayout = (LinearLayout)findViewById(R.id.my_gallery);
        directoryPath = getAlbumStorageDir("images");
        /*if(directoryPath.exists() && directoryPath.isDirectory()) {
            try {
                FileUtils.cleanDirectory(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        resourceImages = new int[]{R.drawable.photo_one, R.drawable.photo_one, R.drawable.photo_one, R.drawable.photo_one,R.drawable.photo_one,R.drawable.photo_one};
        for(int i = 0; i < resourceImages.length; i++){
            resourceBitmap = resourceToImageBitmap(resourceImages[i]);
            saveFileInExternalStorage(resourceBitmap, i);
        }
        System.out.println("Files number: " + directoryPath.listFiles().length);
        File[] filterStoredFiles =  directoryPath.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return ((filename.endsWith(".jpg"))||(filename.endsWith(".png")));
            }
        });
        if(filterStoredFiles.length <= 0){
            Toast.makeText(HorizontalScrollViewActivity.this, "Whoops!!!, There is no file saved in the external storage", Toast.LENGTH_LONG).show();
            return;
        }
        fileList = new String[filterStoredFiles.length];
        for(int i = 0; i < fileList.length; i++){
            fileList[i] = filterStoredFiles[i].getAbsolutePath();
        }
        mUrls = new Uri[fileList.length];
        for(int i=0; i < fileList.length; i++){
            mUrls[i] = Uri.parse(fileList[i]);
        }
        for(int j = 0; j < mUrls.length; j++){
            String imageAbsolutePath = mUrls[j].toString();
            ImageView addImageView = getNewImageView(imageAbsolutePath);
            final int indexJ = j;
            addImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentIndex = indexJ;
                    Toast.makeText(HorizontalScrollViewActivity.this, "Current index " + currentIndex, Toast.LENGTH_LONG).show();
                }
            });
            galleryLayout.addView(addImageView);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(HorizontalScrollViewActivity.this, "Sorry!!!, you can't use this app without granting this permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != galleryLayout){
            galleryLayout.removeAllViews();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if(storeDirectory.exists()){
            storeDirectory.delete();
        }
        System.out.println("Files " + storeDirectory.getAbsolutePath());
        FileOutputStream out = null;
        try {
            storeDirectory.createNewFile();
            out = new FileOutputStream(storeDirectory);
            resourceBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveFileInExternalStorage(Bitmap bitmap, int index){
        if(!isExternalStorageWritable() && !isExternalStorageReadable()){
            Toast.makeText(HorizontalScrollViewActivity.this, "There is no external storage in your device or not writable", Toast.LENGTH_LONG).show();
            return;
        }
        String filename = "thumbnail" + String.valueOf(index) + ".jpg";
        storeDirectory = new File(directoryPath, filename);
        // Add permission for camera and let user grant the permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HorizontalScrollViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            return;
        }
        if(storeDirectory.exists()){
            storeDirectory.delete();
        }
        //System.out.println("Files " + storeDirectory.getAbsolutePath());
        Log.i(TAG, "Directory not created");
        FileOutputStream out = null;
        try {
            storeDirectory.createNewFile();
            out = new FileOutputStream(storeDirectory);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap resourceToImageBitmap(int fileResource){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), fileResource);
        return bitmap;
    }
    private ImageView getNewImageView(String photoPath){
        Bitmap bm = decodeFile(photoPath);
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(300, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
        return imageView;
    }
    public static Bitmap decodeFile(String photoPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferQualityOverSpeed = true;
        return BitmapFactory.decodeFile(photoPath, options);
    }
    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
