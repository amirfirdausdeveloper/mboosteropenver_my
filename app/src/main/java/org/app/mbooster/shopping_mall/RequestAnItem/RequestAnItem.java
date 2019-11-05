package org.app.mbooster.shopping_mall.RequestAnItem;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.app.mbooster.Dialog.DialogFragmentPhoto;
import org.app.mbooster.Dialog.DialogFragmentUniversal;
import org.app.mbooster.Google.AnalyticsApplication;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MyAddressEdit;
import org.app.mbooster.model_folder.NetworkStateReceiver;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RequestAnItem extends AppCompatActivity implements DialogFragmentPhoto.onSubmitListener, DialogFragmentUniversal.onSubmitListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private EditText itemname, itemdesc, pricerange;
    private TextView tvitemname, tvitemdesc, tvpricerange, tvitemimage, toolbar_title, tvsubmit;
    private String itemnamestr, itemdescstr, pricerangestr, userId;
    private Toolbar toolbar;
    private ImageView uploadimage, imghistory;
    private final static int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private DialogFragmentPhoto fragment1;
    private DialogFragmentUniversal fragment2;
    private String base64 = "";
    private RelativeLayout submit;

    private NetworkStateReceiver networkStateReceiver;
    static Activity activity;
    private Context context;
    private int network = 0;

    private Tracker mTracker;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_an_item);
        context = activity = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Helper.CheckMaintenance(this);

        // firebase analytic
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);

        //toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userId = SavePreferences.getUserID(this);

        submit = (RelativeLayout) findViewById(R.id.submit);
        itemname = (EditText) findViewById(R.id.itemname);
        itemdesc = (EditText) findViewById(R.id.itemdesc);
        pricerange = (EditText) findViewById(R.id.pricerange);

        tvitemname = (TextView) findViewById(R.id.tvitemname);
        tvitemdesc = (TextView) findViewById(R.id.tvitemdesc);
        tvpricerange = (TextView) findViewById(R.id.tvpricerange);
        tvitemimage = (TextView) findViewById(R.id.tvitemimage);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        tvsubmit = (TextView) findViewById(R.id.textView4);
        imghistory = (ImageView) findViewById(R.id.imghistory);

//        if (SavePreferences.getApplanguage(RequestAnItem.this).equals("CN")) {
//            tvitemname.setText("商品名称");
//            tvitemdesc.setText("商品描述");
//            tvpricerange.setText("价格范围");
//            tvitemimage.setText("商品图像");
//            toolbar_title.setText("请求商品");
//            tvsubmit.setText("提交");
//
//        }

        imghistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestAnItem.this, RequestAnItemHistory.class));
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemname.getText().toString().trim().length() == 0 || itemdesc.getText().toString().trim().length() == 0 || pricerange.getText().toString().trim().length() == 0) {
//                    if (SavePreferences.getApplanguage(RequestAnItem.this).equals("CN")) {
//                        Toast.makeText(RequestAnItem.this, "请填入所有空格", Toast.LENGTH_SHORT).show();
//
//                    } else {
                    Toast.makeText(RequestAnItem.this, getString(R.string.please_fill_in_all_blanks), Toast.LENGTH_SHORT).show();

                    // }
                } else if (base64.isEmpty()) {
//                    if (SavePreferences.getApplanguage(RequestAnItem.this).equals("CN")) {
//                        Toast.makeText(RequestAnItem.this, "请上传照片", Toast.LENGTH_SHORT).show();
//
//                    } else {
                    Toast.makeText(RequestAnItem.this, R.string.please_upload_an_image, Toast.LENGTH_SHORT).show();

//                    }
                } else {


                    String arg = getString(R.string.request_this_item_q);
                    String arg2 = getString(R.string.request_an_item);
                    String arg3 = getString(R.string.confirm);
                    String arg4 = getString(R.string.cancel);
//                        if (SavePreferences.getApplanguage(RequestAnItem.this).equals("CN")) {
//                            arg = "请求这项物品吗?";
//                            arg2 = "请求物品";
//                            arg3 = "确定";
//                            arg4 = "取消";
//                        }

                    fragment2 = new DialogFragmentUniversal();
                    fragment2 = DialogFragmentUniversal.newInstance(arg, arg2, arg3, arg4);
                    fragment2.mListener = RequestAnItem.this;
                    fragment2.show(getFragmentManager(), "");
                }
            }


        });

        uploadimage = (ImageView) findViewById(R.id.uploadimage);
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String arg = getString(R.string.take_photo);
                String arg2 = getString(R.string.choose_photo);
                String arg3 = getString(R.string.cancel);
//                if (SavePreferences.getApplanguage(RequestAnItem.this).equals("CN")) {
//                    arg = "拍照";
//                    arg2 = "选择相片";
//                    arg3 = "取消";
//                }
                fragment1 = DialogFragmentPhoto.newInstance(arg, arg2, arg3);
                fragment1.mListener = RequestAnItem.this;
                fragment1.show(getFragmentManager(), "");

            }
        });

        checkAddrStatus();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void setOnSubmitListener(String arg) {

        if (arg.equals(getString(R.string.take_photo))) {
            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
            if (!hasPermissions(RequestAnItem.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(RequestAnItem.this, PERMISSIONS, REQUEST_CAMERA);
            } else {
                cameraIntent();
            }
        } else if (arg.equals(getString(R.string.choose_photo))) {

            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE } ;
            if (!hasPermissions(RequestAnItem.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(RequestAnItem.this, PERMISSIONS, SELECT_FILE);
            } else {
                selectImageFromGallery();
            }
        } else if (arg.equals(getString(R.string.confirm))) {
            requestanitem();
        } else {
            //fragment1.dismiss();
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    cameraIntent();
                } else {
                    // no permissions granted.
                    Toast.makeText(RequestAnItem.this,  R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case SELECT_FILE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    selectImageFromGallery();
                } else {
                    // no permissions granted.
                    Toast.makeText(RequestAnItem.this,  R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }

    }

    private void checkAddrStatus(){
        class getinfo extends AsyncTask<String,String,JSONObject>{
            ACProgressFlower flowerDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(RequestAnItem.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();

                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }

            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                try{
                    if(jsonObject.getString("success").equals("0")){
//                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        finish();

                        final Dialog dialog = new Dialog(activity);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.dialog_msg);

                        TextView text = (TextView) dialog.findViewById(R.id.msg);
                        text.setText("You haven't added your Default Shipping or Billing Address.");

                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(RequestAnItem.this, MyAddressEdit.class));
                                finish();
                                dialog.dismiss();
                            }
                        });

                        ImageView close_btn = (ImageView) dialog.findViewById(R.id.close_btn);
                        close_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... strings) {
                return new urlLink().checkAddrStatusURL(SavePreferences.getUserID(RequestAnItem.this), SavePreferences.getApplanguage(RequestAnItem.this));
            }
        }
        new getinfo().execute();
    }


    public void requestanitem() {
        itemnamestr = itemname.getText().toString();
        itemdescstr = itemdesc.getText().toString();
        pricerangestr = pricerange.getText().toString();

        class getinfo extends AsyncTask<String, String, JSONObject> {
            ACProgressFlower flowerDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                flowerDialog = new ACProgressFlower.Builder(RequestAnItem.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(getResources().getColor(R.color.colorbutton))
                        .fadeColor(Color.GRAY).build();
                if (!flowerDialog.isShowing()) {
                    flowerDialog.show();
                }
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (flowerDialog.isShowing()) {
                    flowerDialog.dismiss();
                }
                try {

                    if (jsonObject.getString("success").equals("1")) {
//                        if (SavePreferences.getApplanguage(RequestAnItem.this).equals("CN")) {
//                            Toast.makeText(RequestAnItem.this, "您的请求已被提交", Toast.LENGTH_SHORT).show();
//                        } else {
                        Toast.makeText(RequestAnItem.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
                        finish();
                    } else {
//                        if (SavePreferences.getApplanguage(RequestAnItem.this).equals("CN")) {
//                            Toast.makeText(RequestAnItem.this, "无法提交您的请求", Toast.LENGTH_SHORT).show();
//                        } else {
                        Toast.makeText(RequestAnItem.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                urlLink url = new urlLink();

                return url.requestanitem(userId, itemnamestr, itemdescstr, pricerangestr, base64 ,SavePreferences.getApplanguage(RequestAnItem.this));

            }
        }
        getinfo ge = new getinfo();
        ge.execute();
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        File f = new File(Environment.getExternalStorageDirectory().toString());
        for (File temp : f.listFiles()) {
            if (temp.getName().equals("temp.jpg")) {
                f = temp;
                File photo = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                //pic = photo;
                break;
            }
        }
        try {
            Bitmap bitmap;
            bitmap = getCorrectlyOrientedImage(RequestAnItem.this, Uri.fromFile(f), 600);

            base64 = BitmapToString(bitmap);
            uploadimage.setImageBitmap(bitmap);

        } catch (Exception e) {

            e.printStackTrace();

        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        Uri uri = data.getData();
        File imageFile = new File(getRealPathFromURI(uri));
        if (data != null) {
            //get orientation right by bitmap
            try {
                bm = getCorrectlyOrientedImage(RequestAnItem.this, uri, 600);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            //get base64/String for upload to server
//            pictureBase64 = BitmapToString(bm);
//            baseimgarray.add(pictureBase64);
//            Log.i("baseimagearray", baseimgarray.toString());
//
//            //get bitmap for display on phone
//            imgarray.add(bm);
//          //  gvadapter.notifyDataSetChanged();
//
//          //  uploadpic += 1;
            base64 = BitmapToString(bm);
            uploadimage.setImageBitmap(bm);

        }
    }

    public String BitmapToString(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //100是原本

            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();

            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;

        } catch (NullPointerException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static int getOrientation(Context context, Uri photoUri) {

        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor == null || cursor.getCount() != 1) {
            return 90;  //Assuming it was taken portrait
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * Rotates and shrinks as needed
     */
    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri, int maxWidth)
            throws IOException {

        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();


        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            Log.d("ImageUtil", "Will be rotated");
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        Log.d("ImageUtil", String.format("rotatedWidth=%s, rotatedHeight=%s, maxWidth=%s",
                rotatedWidth, rotatedHeight, maxWidth));
        if (rotatedWidth > maxWidth || rotatedHeight > maxWidth) {
            float widthRatio = ((float) rotatedWidth) / ((float) maxWidth);
            float heightRatio = ((float) rotatedHeight) / ((float) maxWidth);
            float maxRatio = Math.max(widthRatio, heightRatio);
            Log.d("ImageUtil", String.format("Shrinking. maxRatio=%s",
                    maxRatio));

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            Log.d("ImageUtil", String.format("No need for Shrinking. maxRatio=%s",
                    1));

            srcBitmap = BitmapFactory.decodeStream(is);
            Log.d("ImageUtil", String.format("Decoded bitmap successful"));
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    @Override
    protected void onStart() {
        super.onStart();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            this.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        String pagename = "(Android) Request An Item";
        mTracker.setScreenName(pagename);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mFirebaseAnalytics.setCurrentScreen(this, pagename, null /* class override */);


    }

    @Override
    public void networkAvailable() {
        network = 1;


    }

    @Override
    public void networkUnavailable() {

        network = 0;

        if (network == 0) {
            new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                    .setTitle(getString(R.string.no_network_notification))
                    .setCancelable(false)

                    .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (isOnline() == false) {
                                networkUnavailable();
                            } else {
                                dialog.dismiss();

                            }

                        }
                    })
                    .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) context).finish();
                        }
                    })
                    .show();

        }
    }


    public boolean isOnline() {


        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();


        } catch (Exception e) {

            return false;
        }

    }
}
