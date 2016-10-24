package comet.thanhtikesoe.com.trafficmyanmar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import comet.thanhtikesoe.com.trafficmyanmar.Gallery.CustomGallery;
import comet.thanhtikesoe.com.trafficmyanmar.Gallery.CustomGalleryAdapter;

public class TrafficViolationActivity extends AppCompatActivity{

    public final static String EXTRA_MESSAGE = "THANKS FOR SUBMISSION";
    public static final String ACTION_MULTIPLE_PICK = "cunoraz.ACTION_MULTIPLE_PICK";
    private Button btn_submit;
    private ImageButton btn_capture;
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

    GridView gridGallery;
    Handler handler;
    CustomGalleryAdapter adapter;
    ArrayList<String> imagePaths;
    ImageButton btnGalleryPickMul;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_traffic_violation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Traffic Violation");

        galleryLayout = (LinearLayout)findViewById(R.id.my_gallery);
        directoryPath = getAlbumStorageDir("images");

        btnGalleryPickMul = (ImageButton)findViewById(R.id.btn_gallery);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initImageLoader();

            }
        });

        initImageLoader();
        init();
//                if(directoryPath.exists() && directoryPath.isDirectory()) {
//            try {
//                FileUtils.cleanDirectory(directoryPath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        resourceImages = new int[]{R.drawable.photo_one, R.drawable.photo_two, R.drawable.photo_one, R.drawable.photo_two,R.drawable.photo_one,R.drawable.photo_two};
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
            Toast.makeText(TrafficViolationActivity.this, "Whoops!!!, There is no file saved in the external storage", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(TrafficViolationActivity.this, "Current index " + currentIndex, Toast.LENGTH_LONG).show();
                }
            });
            galleryLayout.addView(addImageView);
        }
        btn_capture = (ImageButton) findViewById(R.id.capture_image);
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_submit = (Button) findViewById(R.id.submit_button);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        TrafficViolationActivity.this);
                builder.setTitle("Submission is successful...");
                builder.setMessage("You have 3 points");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();

                            }
                        });
                builder.setCancelable(false);
                builder.show();
            }
        });


        spinnerSetup();

    }

    private void initImageLoader() {
        // for universal image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private void init() {

        handler = new Handler();
        gridGallery = (GridView) findViewById(R.id.gridGallery);
        gridGallery.setFastScrollEnabled(true);
        adapter = new CustomGalleryAdapter(getApplicationContext(), imageLoader);
        adapter.setMultiplePick(false);
        gridGallery.setAdapter(adapter);

        gridGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(TrafficViolationActivity.this,""+imagePaths.get(i),Toast.LENGTH_LONG).show();
            }
        });


        btnGalleryPickMul = (ImageButton) findViewById(R.id.btn_gallery);
        btnGalleryPickMul.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Manifest recognize our multiple request by this way
                Intent i = new Intent(ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePaths = new ArrayList<String>();
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] all_path = data.getStringArrayExtra("all_path");

            ArrayList<CustomGallery> dataT = new ArrayList<CustomGallery>();

            for (String string : all_path) {
                CustomGallery item = new CustomGallery();
                item.sdcardPath = string;
                imagePaths.add(string);
                dataT.add(item);
            }

            adapter.addAll(dataT);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Spinner spinnerDay;

    private void spinnerSetup() {

        MaterialSpinner spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner.setItems("Case One", "Case Two", "Case Three","Case Four");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(getApplicationContext(), "Clicked " + item, Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(TrafficViolationActivity.this, "Sorry!!!, you can't use this app without granting this permission", Toast.LENGTH_LONG).show();
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
            Toast.makeText(TrafficViolationActivity.this, "There is no external storage in your device or not writable", Toast.LENGTH_LONG).show();
            return;
        }
        String filename = "thumbnail" + String.valueOf(index) + ".jpg";
        storeDirectory = new File(directoryPath, filename);
        // Add permission for camera and let user grant the permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TrafficViolationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
