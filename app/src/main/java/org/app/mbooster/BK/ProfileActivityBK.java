package org.app.mbooster.BK;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.app.mbooster.Dialog.DialogFragmentPhoto;
import org.app.mbooster.Dialog.DialogFragmentUniversal;
import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.fragment_folder.Edit;
import org.app.mbooster.model_folder.ConnectionDetector;
import org.app.mbooster.Helper.JSONParser;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivityBK extends AppCompatActivity implements DialogFragmentUniversal.onSubmitListener, DialogFragmentPhoto.onSubmitListener {
    private Toolbar toolbar;

    private TextView username, usercode, useremail, userregion, userphone, userdate, useraddress, profileusername, useric, profileusercompany;
    private TextView tvaddr, tvemail, tvphno, tvmmspotid, tveditprofile, tvlogout;
    private TextView tvedit_profile;
    private CircleImageView profileimage;
    LinearLayout changepasswordly, addressbooksly, changeavatarly, logout, lyeditprofile;
    private String userId;
    ProgressDialog progressDialog;
    ConnectionDetector connectionCheck;
    boolean NetworkOn = false;
    DialogFragmentPhoto fragment1;
    private String profilepicbase64;
    private static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private TextView version;

    private DialogFragmentUniversal dialog;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private ScrollView scrollView;
    private ProgressBar progressbar;

    private TextView login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Log.i("qweqweqwe", "qweqweqwe");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gotham_book.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        ToobarSetting();
        pref = getSharedPreferences("MboosterMY", 0); // 0 - for private mode
        editor = pref.edit();

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        version = (TextView) findViewById(R.id.version);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String current_version = pInfo.versionName;
            version.setText("Mbooster.my " + current_version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        profileusername = (TextView) findViewById(R.id.profileusername);
        usercode = (TextView) findViewById(R.id.ucode);
        useremail = (TextView) findViewById(R.id.uemail);
        userphone = (TextView) findViewById(R.id.uphone);
        useraddress = (TextView) findViewById(R.id.uaddress);
        tvedit_profile = (TextView) findViewById(R.id.tvedit_profile);

        tvmmspotid = (TextView) findViewById(R.id.tvmmspotid);
        tvphno = (TextView) findViewById(R.id.tvphno);
        tvemail = (TextView) findViewById(R.id.tvemail);
        tvaddr = (TextView) findViewById(R.id.tvaddr);
        tveditprofile = (TextView) findViewById(R.id.tvedit_profile);
        tvlogout = (TextView) findViewById(R.id.tvlogout);

        profileusercompany = (TextView) findViewById(R.id.profileusercompany);

        profileimage = (CircleImageView) findViewById(R.id.profileimage);
        changeavatarly = (LinearLayout) findViewById(R.id.changeavatarly);
        lyeditprofile = (LinearLayout) findViewById(R.id.lyeditprofile);

        logout = (LinearLayout) findViewById(R.id.logout);

        tvedit_profile.setText(Html.fromHtml("<u>" + getString(R.string.edit_profile) + "</u>"));

        lyeditprofile.setClickable(true);
        lyeditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivityBK.this, Edit.class));
            }
        });
        tvedit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("profile edit", "clicked");
                startActivity(new Intent(ProfileActivityBK.this, Edit.class));
            }
        });


        userId = SavePreferences.getUserID(ProfileActivityBK.this);

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String arg = getString(R.string.take_photo);
                String arg2 = getString(R.string.choose_photo);
                String arg3 = getString(R.string.cancel);

                fragment1 = DialogFragmentPhoto.newInstance(arg, arg2, arg3);
                fragment1.mListener = ProfileActivityBK.this;
                fragment1.show(getFragmentManager(), "");

            }
        });
        login = (TextView) findViewById(R.id.login);
        login.setText(Html.fromHtml("<font color='blue'><u>Login</u></font>"));
        login.setVisibility(View.GONE);
        Log.i("id ", SavePreferences.getUserID(ProfileActivityBK.this));
        if (SavePreferences.getUserID(ProfileActivityBK.this).equals("0")) {
            login.setVisibility(View.GONE);
            logout.setClickable(true);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivityBK.this, signInActivity.class));
                }
            });
            tvlogout.setText(getString(R.string.login));
            lyeditprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivityBK.this, signInActivity.class));
                }
            }); //temp hidden
            tvedit_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivityBK.this, signInActivity.class));
                }
            });
            profileimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ProfileActivityBK.this, signInActivity.class));
                }
            });

        } else {
            checkConnection();
            tvlogout.setText(getString(R.string.account_logout));
            logout.setClickable(true);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String arg = getString(R.string.areyousurelogout);
                    String arg2 = getString(R.string.logging_out);
                    String arg3 = getString(R.string.confirm);
                    String arg4 = getString(R.string.cancel);
//                if (SavePreferences.getApplanguage(getContext()).equals("CN")) {
//                    arg = "您确定登出吗?";
//                    arg2 = "登出";
//                    arg3 = "登出";
//                    arg4 = "不";
//                }

                    final Dialog dialog = new Dialog(ProfileActivityBK.this, R.style.AlertDialogTheme);
                    TypefaceUtil.overrideFont(ProfileActivityBK.this, "SERIF", "fonts/gotham_book.otf");
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    dialog.setContentView(R.layout.fragment_dialog_universal);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                    TextView content = (TextView) dialog.findViewById(R.id.content);
                    TextView title = (TextView) dialog.findViewById(R.id.title);
                    title.setText(arg2);
                    Button btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                    Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                    btnConfirm.setText(arg3);
                    btnCancel.setText(arg4);


                    content.setText(arg);
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Logout();
                            finish();
//                            Intent i = new Intent(ProfileActivityBK.this, signInActivity.class);
                            Intent i = new Intent(ProfileActivityBK.this, MainActivity.class); //open public
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        }
                    });


                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }


        Helper.CheckMaintenance(ProfileActivityBK.this);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void ToobarSetting() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        //        final Drawable back_arrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        final Drawable back_arrow = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);

        back_arrow.setColorFilter(getResources().getColor(R.color.colorbutton), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setData() {
        startActivity(getIntent());
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pref.getString("refreshprofile", "0").equals("1")) {
            setData();
            editor.putString("refreshprofile", "0").commit();
        }
    }


    private void Logout() {
        SavePreferences.setUserEmail(ProfileActivityBK.this, "");
        SavePreferences.setUserPassword(ProfileActivityBK.this, "");
        SavePreferences.setUserName(ProfileActivityBK.this, "");
        SavePreferences.setUserID(ProfileActivityBK.this, "0");
        SavePreferences.setUserRole(ProfileActivityBK.this, "");
    }


    public void checkConnection() {
        //Check Connection
        connectionCheck = new ConnectionDetector(ProfileActivityBK.this);
        NetworkOn = connectionCheck.isConnectingToInternet();

        if (!NetworkOn) {

            Toast.makeText(ProfileActivityBK.this, "Currently not connection", Toast.LENGTH_SHORT).show();
            Log.i("Internet Connection", "Currently not connection");
        } else {
            profileImage(userId);
            Log.i("Internet Connection", "Currently connecting");
        }

    }

    //********************************************************************************************************************************************************//
    //load ProfileFragment function part
    //********************************************************************************************************************************************************//

    public void profileImage(final String id) {

        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {

            String data = new urlLink().submitgetUserData(ProfileActivityBK.this);
            String response;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressbar.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jsonObject = new JSONParser().getJSONFromUrl(new urlLink().URLgetUser, data);

                Log.i("json", jsonObject.toString());
                return jsonObject;

            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                progressbar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Log.i("Running onpost", jsonObject.toString());
                try {
                    response = jsonObject.getString("success");
                    if (response.equals("1")) {

                        String companyname = jsonObject.getString("user_username");
                        String ownername = jsonObject.getString("user_fname");
                        String companycode = jsonObject.getString("user_dealer");
                        String companyemail = jsonObject.getString("user_email");
                        String companyphone = jsonObject.getString("user_contact_tel");
                        String companyregister_date = jsonObject.getString("register_date");
                        String companyregion = jsonObject.getString("user_region");
                        String companyuser_new_ic = jsonObject.getString("user_ic");
                        String company_address = jsonObject.getString("user_address");

                        String image = jsonObject.getString("user_avatar");
                        if (image.equals("")) {
                            profileimage.setImageResource(R.drawable.people);
                        } else {
//                            Log.i("url", jsonObject.getString("user_avatar"));
                            ImageLoader.getInstance().displayImage(jsonObject.getString("user_avatar"), new ImageViewAware(profileimage, false));
                        }
//                        profileusercompany.setText(companyname);
//                        profileusername.setText(ownername);
                        profileusername.setText(companyname);
                        usercode.setText(companycode);
                        useremail.setText(companyemail);
                        userphone.setText(companyphone);
                        useraddress.setText(company_address);

                        if(jsonObject.has("dataz")){
                            JSONObject dataz = jsonObject.getJSONObject("dataz");
                            SavePreferences.setMMSPOTNAME(getApplicationContext(), dataz.getString("fullname"));
                            SavePreferences.setMMSPOTEMAIL(getApplicationContext(), dataz.getString("email"));
                            SavePreferences.setMMSPOTCONTACT(getApplicationContext(), dataz.getString("alternateno"));
                        }
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }


        }

        new AsyncTaskRunner().execute();
    }


    //********************************************************************************************************************************************************//
    //load ProfileFragment function part
    //********************************************************************************************************************************************************//

    public void uploadavatar(final String profilepicbase64, final String id) {

        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {

            String data = new urlLink().submituserprofilepic(profilepicbase64, id, SavePreferences.getApplanguage(ProfileActivityBK.this));
            String response, message;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jsonObject = new JSONParser().getJSONFromUrl(new urlLink().user_changeavatar, data);

                Log.i("json", jsonObject.toString());
                return jsonObject;

            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                Log.i("Running on", jsonObject.toString());
                try {
                    response = jsonObject.getString("success");
                    message = jsonObject.getString("message");
                    if (response.equals("1")) {

                        //profileImage(userId);

                        Toast.makeText(ProfileActivityBK.this, message, Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }


        }

        new AsyncTaskRunner().execute();
    }

    /***********************************************************************
     * Function part
     **********************************************************************/

    @Override
    public void setOnSubmitListener(String arg) {
        Log.d("arg", arg.toString());
        if (arg.equals(getString(R.string.take_photo))) {

            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            if (!hasPermissions(ProfileActivityBK.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(ProfileActivityBK.this, PERMISSIONS, REQUEST_CAMERA);
            } else {
                cameraIntent();
            }
        } else if (arg.equals(getString(R.string.choose_photo))) {
            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            if (!hasPermissions(ProfileActivityBK.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(ProfileActivityBK.this, PERMISSIONS, SELECT_FILE);
            } else {
                selectImageFromGallery();
            }
        } else if (arg == "Yes" || arg == "登出") {
            SavePreferences.clear(ProfileActivityBK.this.getApplicationContext());
            startActivity(new Intent(ProfileActivityBK.this.getApplicationContext(), signInActivity.class));
        } else {
            try {
                fragment1.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ProfileActivityBK.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProfileActivityBK.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case SELECT_FILE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    selectImageFromGallery();
                } else {
                    // no permissions granted.
                    Toast.makeText(ProfileActivityBK.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }

    }

//    private void selectImageFromGallery() {
//        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
//    }

    //Select Image
    public void selectImageFromGallery() {


        // TODO Auto-generated method stub
        Intent intent = new Intent();
        // call android default gallery
        intent.setType("image/*");
        if (android.os.Build.MODEL.startsWith("HTC"))
            intent.putExtra("folderType", "com.htc.HTCAlbum.ALL_PHOTOS");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // ******** code for crop image
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        try {

            intent.putExtra("return-data", true);
            startActivityForResult(Intent.createChooser(intent,
                    "Complete action using"), SELECT_FILE);

        } catch (ActivityNotFoundException e) {
            // Do nothing for now
        }


    }

    //Take photo
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            // create instance of File with same name we created before to get
            // image from storage
//            File file = new File(Environment.getExternalStorageDirectory()
//                    + File.separator + ".jpg");
            File file = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            // Crop the captured image using an other intent
            try {
                /* the user’s device may not support cropping */
                cropCapturedImage(Uri.fromFile(file));
            } catch (ActivityNotFoundException aNFE) {
                // display an error message if user device doesn’t support
                String errorMessage = getString(R.string.cannot_crop);
//                if(SavePreferences.getApplanguage(getContext()).equals("CN")){
//                    Toast.makeText(ProfileActivityBK.this.getApplicationContext(), "抱歉, 您的手机不支援裁切动作.", Toast.LENGTH_SHORT).show();
//                }else {
                Toast toast = Toast.makeText(ProfileActivityBK.this, errorMessage, Toast.LENGTH_SHORT);
//                    toast.show();
//                }
            }

        }
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Bundle extras2 = data.getExtras();
            if (extras2 != null) {
                Bitmap photo = extras2.getParcelable("data");

                // Resize the bitmap to 400x400 (width x height)
                Bitmap bMapScaled;
                bMapScaled = Bitmap.createScaledBitmap(photo, 200, 200, true);
                // Set The Bitmap Data To ImageView
                profileimage.setImageBitmap(bMapScaled);

                //  profileImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                profilepicbase64 = BitmapToString(bMapScaled);
                //changeImage = true;

                uploadavatar(profilepicbase64, userId);
            }
        }
        if (requestCode == 3) {
            try {
                // Create an instance of bundle and get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap from extras
                Bitmap thePic = extras.getParcelable("data");

                // Resize the bitmap to 400x400 (width x height)
                Bitmap bMapScaled = Bitmap.createScaledBitmap(thePic, 200, 200, true);
                // Set The Bitmap Data To ImageView
                profileimage.setImageBitmap(bMapScaled);
                //  profileImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                profilepicbase64 = BitmapToString(bMapScaled);
                uploadavatar(profilepicbase64, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
//        if (requestCode == ProfileActivityBK.this.RESULT_CANCELED) {
//            Toast.makeText(ProfileActivityBK.this.getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
//        }
    }

    public void cropCapturedImage(Uri picUri) {
        // call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        // set crop properties
        cropIntent.putExtra("crop", "true");
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity – we handle returning in onActivityResult
        startActivityForResult(cropIntent, 3);
    }

    //
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
            bitmap = getCorrectlyOrientedImage(ProfileActivityBK.this, Uri.fromFile(f), 600);

            profilepicbase64 = BitmapToString(bitmap);
            profileimage.setImageBitmap(bitmap);

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
                bm = getCorrectlyOrientedImage(ProfileActivityBK.this, uri, 600);
            } catch (Exception e) {
                e.printStackTrace();
            }

            profilepicbase64 = BitmapToString(bm);
            profileimage.setImageBitmap(bm);

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
}
