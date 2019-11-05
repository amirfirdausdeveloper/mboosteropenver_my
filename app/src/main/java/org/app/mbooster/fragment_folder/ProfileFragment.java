package org.app.mbooster.fragment_folder;

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
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.app.mbooster.Dialog.DialogFragmentUniversal;
import org.app.mbooster.Font.TypefaceUtil;
import org.app.mbooster.Helper.Helper;
import org.app.mbooster.R;
import org.app.mbooster.Dialog.DialogFragmentPhoto;
import org.app.mbooster.activity_folder.MainActivity;
import org.app.mbooster.activity_folder.signInActivity;
import org.app.mbooster.model_folder.ConnectionDetector;
import org.app.mbooster.Helper.JSONParser;
import org.app.mbooster.model_folder.SavePreferences;
import org.app.mbooster.model_folder.urlLink;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

//Pending Checking Class
public class ProfileFragment extends Fragment implements DialogFragmentUniversal.onSubmitListener, DialogFragmentPhoto.onSubmitListener {
    View rootview;
    private TextView username, usercode, useremail, userregion, userphone, userdate, useraddress, profileusername, useric, profileusercompany;
    private TextView tvaddr, tvemail, tvphno, tvmmspotid, tveditprofile, tvlogout;
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

    Context c;

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
        pref = getActivity().getSharedPreferences("MboosterMY", 0); // 0 - for private mode
        editor = pref.edit();
    }

    public void setData() {
        // The reload fragment code here !
        if (!this.isDetached()) {
            getFragmentManager().beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.c = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pref.getString("refreshprofile", "0").equals("1")) {
            setData();
            editor.putString("refreshprofile", "0").commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.NoactionBar);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        rootview = localInflater.inflate(R.layout.fragment_profile, container, false);

        scrollView = (ScrollView) rootview.findViewById(R.id.scrollView);
        progressbar = (ProgressBar) rootview.findViewById(R.id.progressbar);

        version = (TextView) rootview.findViewById(R.id.version);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String current_version = pInfo.versionName;
            version.setText("Mbooster.my "+ current_version);
        }catch (Exception e){
            e.printStackTrace();
        }

        profileusername = (TextView) rootview.findViewById(R.id.profileusername);
        usercode = (TextView) rootview.findViewById(R.id.ucode);
        useremail = (TextView) rootview.findViewById(R.id.uemail);
        userphone = (TextView) rootview.findViewById(R.id.uphone);
        useraddress = (TextView) rootview.findViewById(R.id.uaddress);


        tvmmspotid = (TextView) rootview.findViewById(R.id.tvmmspotid);
        tvphno = (TextView) rootview.findViewById(R.id.tvphno);
        tvemail = (TextView) rootview.findViewById(R.id.tvemail);
        tvaddr = (TextView) rootview.findViewById(R.id.tvaddr);
        tveditprofile = (TextView) rootview.findViewById(R.id.tveditprofile);
        tvlogout = (TextView) rootview.findViewById(R.id.tvlogout);

        profileusercompany = (TextView) rootview.findViewById(R.id.profileusercompany);

        profileimage = (CircleImageView) rootview.findViewById(R.id.profileimage);
        changeavatarly = (LinearLayout) rootview.findViewById(R.id.changeavatarly);
        lyeditprofile = (LinearLayout) rootview.findViewById(R.id.lyeditprofile);

        logout = (LinearLayout) rootview.findViewById(R.id.logout);

        lyeditprofile.setClickable(true);
        lyeditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Edit.class));
            }
        });

        userId = SavePreferences.getUserID(getActivity());

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String arg = getString(R.string.take_photo);
                String arg2 = getString(R.string.choose_photo);
                String arg3 = getString(R.string.cancel);

                fragment1 = DialogFragmentPhoto.newInstance(arg, arg2, arg3);
                fragment1.mListener = ProfileFragment.this;
                fragment1.show(getActivity().getFragmentManager(), "");

            }
        });
        login = (TextView) rootview.findViewById(R.id.login);
        login.setText(Html.fromHtml("<font color='blue'><u>Login</u></font>"));
        login.setVisibility(View.GONE);
        if(SavePreferences.getUserID(getContext()).equals("0")){
            login.setVisibility(View.GONE);
            logout.setClickable(true);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),signInActivity.class));
                }
            });
            tvlogout.setText(getActivity().getString(R.string.login));
            lyeditprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),signInActivity.class));
                }
            });
            profileimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),signInActivity.class));
                }
            });

        }else {
            checkConnection();
            tvlogout.setText(getActivity().getString(R.string.account_logout));
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

                    final Dialog dialog = new Dialog(getActivity(), R.style.AlertDialogTheme);
                    TypefaceUtil.overrideFont(getActivity(), "SERIF", "fonts/gotham_book.otf");
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
                            getActivity().finish();
//                            Intent i = new Intent(getActivity(), signInActivity.class);
                            Intent i = new Intent(getActivity(), MainActivity.class); //open public
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
        Helper.CheckMaintenance(getActivity());
        return rootview;

    }

    private void Logout(){
        SavePreferences.setUserEmail(getContext().getApplicationContext(), "");
        SavePreferences.setUserPassword(getContext().getApplicationContext(), "");
        SavePreferences.setUserName(getContext().getApplicationContext(), "");
        SavePreferences.setUserID(getContext().getApplicationContext(), "0");
        SavePreferences.setUserRole(getContext().getApplicationContext(), "");
    }


    public void checkConnection() {
        //Check Connection
        connectionCheck = new ConnectionDetector(getActivity());
        NetworkOn = connectionCheck.isConnectingToInternet();

        if (!NetworkOn) {

            Toast.makeText(getActivity(), "Currently not connection", Toast.LENGTH_SHORT).show();
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

            String data = new urlLink().submitgetUserData(c);
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

            String data = new urlLink().submituserprofilepic(profilepicbase64, id, SavePreferences.getApplanguage(getContext()));
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

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();


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
        Log.d("arg",arg.toString());
        if (arg.equals(getString(R.string.take_photo)) ) {

            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_CAMERA);
            } else {
                cameraIntent();
            }
        } else if (arg.equals(getString(R.string.choose_photo))) {
            String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE } ;
            if (!hasPermissions(getActivity(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, SELECT_FILE);
            } else {
                selectImageFromGallery();
            }
        } else if (arg == "Yes" || arg == "登出") {
            SavePreferences.clear(getActivity().getApplicationContext());
            startActivity(new Intent(getActivity().getApplicationContext(), signInActivity.class));
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
                    Toast.makeText(getActivity(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(),  R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case SELECT_FILE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    selectImageFromGallery();
                } else {
                    // no permissions granted.
                    Toast.makeText(getActivity(),  R.string.permission_denied, Toast.LENGTH_SHORT).show();
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

        if (requestCode == REQUEST_CAMERA && resultCode == getActivity().RESULT_OK) {
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
//                    Toast.makeText(getActivity().getApplicationContext(), "抱歉, 您的手机不支援裁切动作.", Toast.LENGTH_SHORT).show();
//                }else {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
//                    toast.show();
//                }
            }

        }
        if (requestCode == SELECT_FILE && resultCode == getActivity().RESULT_OK) {
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
//        if (requestCode == getActivity().RESULT_CANCELED) {
//            Toast.makeText(getActivity().getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
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
            bitmap = getCorrectlyOrientedImage(getActivity(), Uri.fromFile(f), 600);

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
                bm = getCorrectlyOrientedImage(getActivity(), uri, 600);
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
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
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
