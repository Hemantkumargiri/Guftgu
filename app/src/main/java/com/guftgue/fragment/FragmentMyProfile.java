package com.guftgue.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dorianmusaj.cryptolight.CryptoLight;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.activity.EncryptDecryptDashboardActivity;
import com.guftgue.helper.AESHelper;
import com.guftgue.helper.PermissionsUtil;
import com.guftgue.others.MyStateview;
import com.guftgue.others.MyToast;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.R;
import com.guftgue.activity.LoginActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.crypto.KeyGenerator;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.guftgue.helper.AESHelper.getFile;
import static com.guftgue.others.ApiServices.get_profile;
import static com.guftgue.others.ApiServices.update_profile;

public class FragmentMyProfile extends Fragment implements ProgressClickListener {
    private EditText medtFulName, medtEmail, medtPhone, medtPassword, medtAddress;
    private ImageView fab;
    private Button mbtnSave, btnlogout;
    public String user_id;
    private MyStateview mStateview;
    //private Toolbar toolbar;
    AlertDialog alertDialog;
    private ImageView mImgSelectProfile;
    private Uri mCropImageUri;
    private Uri path;
    View view;
    Context context;
    private RelativeLayout mRLselectpic;
    public static final int CAMERA_PIC_REQUEST = 1;
    private File profile;

    @Override
    public void onResume() {
        super.onResume();
        /*TvHome.setTextColor(getResources().getColor(R.color.black));
        TvHome.setBackgroundColor(getResources().getColor(R.color.white));
        tvUserlist.setTextColor(getResources().getColor(R.color.black));
        tvUserlist.setBackgroundColor(getResources().getColor(R.color.white));
        Tvmyencryption.setTextColor(getResources().getColor(R.color.black));
        Tvmyencryption.setBackgroundColor(getResources().getColor(R.color.white));
        Tvmyprofile.setTextColor(getResources().getColor(R.color.white));
        Tvmyprofile.setBackgroundColor(getResources().getColor(R.color.colorPrimary));*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
//        toolbar = view.findViewById(R.id.toolbar1);
        btnlogout = view.findViewById(R.id.btnlogout);
        mRLselectpic = view.findViewById(R.id.RLselectpic);
        mImgSelectProfile = view.findViewById(R.id.imgProfile);

        mImgSelectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });
        //  toolbar.inflateMenu(R.menu.option_menu);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Are you sure, you want to logout ?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
                                SharedPreferences.Editor ed = sharedPreferences.edit();
                                ed.clear();
                                ed.commit();
                                Intent a = new Intent(getActivity(), LoginActivity.class);
                                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                                getActivity().finish();

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alertDialog.dismiss();

                            }
                        });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        mStateview = new MyStateview(this, view);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        Log.d("user_id", "----------------" + user_id);
        post(user_id);

        fab = view.findViewById(R.id.btnfab);
        medtFulName = view.findViewById(R.id.edtFulName);
        medtEmail = view.findViewById(R.id.edtEmail);
        medtPhone = view.findViewById(R.id.edtPhone);
        medtPassword = view.findViewById(R.id.edtPassword);
        medtAddress = view.findViewById(R.id.edtAddress);
        mbtnSave = view.findViewById(R.id.btnSave);
        medtFulName.setEnabled(false);
        medtEmail.setEnabled(false);
        medtPhone.setEnabled(false);
        medtPassword.setEnabled(false);
        medtAddress.setEnabled(false);
        mbtnSave.setEnabled(false);
        mbtnSave.getBackground().setAlpha(128);
        mImgSelectProfile.setEnabled(false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medtFulName.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(medtFulName, InputMethodManager.SHOW_IMPLICIT);
                MyToast.display(getContext(),"Now you can update your profile.");
                medtFulName.setEnabled(true);
                medtEmail.setEnabled(true);
                medtPassword.setEnabled(true);
                medtAddress.setEnabled(true);
                mbtnSave.setEnabled(true);
                mbtnSave.getBackground().setAlpha(255);
                mImgSelectProfile.setEnabled(true);
            }
        });
        mbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    update();
                }
            }
        });

        return view;
    }

    public void onSelectImageClick(View v) {
        CropImage.startPickImageActivity(getActivity(), FragmentMyProfile.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }



        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImgSelectProfile.setImageURI(result.getUri());
                path = result.getUri();
                Log.d("path", "----------------------" + path);
                profile = new File(path.getPath());


                String publicKey = CryptoLight.getPublicKey(getActivity());
                String privateKey = CryptoLight.getPrivateKey(getActivity());

                String filePath = profile.getAbsolutePath();

                File myDirectory = new File(Environment.getExternalStorageDirectory().toString()+"/Aqeel/Images");

                if(!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }

                /*File dir = new File("./DCIM/VinodDIR");
                try{
                    if(dir.mkdir()) {
                        System.out.println("Directory created");
                    } else {
                        System.out.println("Directory is not created");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }*/
                KeyGenerator keyGenerator = null;
                try {
                    keyGenerator = KeyGenerator.getInstance("AES");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                keyGenerator.init(256);
                Key key = keyGenerator.generateKey();

                byte[] content = getFile(profile);
                System.out.println("FILE CONTENT" +content);

                byte[] encrypted = AESHelper.encryptPdfFile(key, content);
                System.out.println("encrypted -->" + encrypted);

                byte[] decrypted = AESHelper.decryptPdfFile(key, encrypted);
                System.out.println("decrypted -->" +decrypted);

               /* Bitmap bmp1 = BitmapFactory.decodeByteArray(decrypted, 0, encrypted.length);
                Bitmap mutableBitmap1 = bmp1.copy(Bitmap.Config.ARGB_8888, true);
             //   fab.setImageBitmap(mutableBitmap);*/

                /*Bitmap bmp1 = BitmapFactory.decodeByteArray(decrypted, 0, decrypted.length);
                Bitmap mutableBitmap = bmp1.copy(Bitmap.Config.ARGB_8888, true);
                fab.setImageBitmap(mutableBitmap);
                String str="";*/



                /*Bitmap bmp = BitmapFactory.decodeByteArray(encrypted, 0, encrypted.length);
                Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
                fab.setImageBitmap(mutableBitmap);*/

                try {
                    AESHelper.saveFile(decrypted,myDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }




            }
                ((ImageView) view.findViewById(R.id.imgProfile)).setImageURI(result.getUri());

                Toast.makeText(getActivity(), "Cropped", Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed", Toast.LENGTH_LONG).show();
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissionsuri granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getActivity(), this);
    }

    public void post(String user_id) {
        mStateview.showLoading();
        Log.d("user_id", "--------------------" + user_id);
        AndroidNetworking.post(get_profile)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "PostData1-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("success").equalsIgnoreCase("1")) {
                                if (response.getString("code").equalsIgnoreCase("200")) {
                                    String msg = response.getString("message");
                                    Log.d("message", "---------------" + msg);
                                    medtFulName.setText(response.getJSONObject("user_profile").getString("first_name"));
                                    medtPhone.setText(response.getJSONObject("user_profile").getString("phone"));
                                    medtEmail.setText(response.getJSONObject("user_profile").getString("email"));
                                    medtPassword.setText(response.getJSONObject("user_profile").getString("password"));
                                    Log.e("ADDRESS", "----" + response.getJSONObject("user_profile").getString("address"));
                                    if (!response.getJSONObject("user_profile").getString("address").equalsIgnoreCase("null")) {
                                        medtAddress.setText(response.getJSONObject("user_profile").getString("address"));

                                    } else {
                                        medtAddress.setText("");

                                    }
                                    Picasso.get().load(response.getJSONObject("user_profile").getString("image"))
                                            .placeholder(R.drawable.logo)
                                            .into(mImgSelectProfile);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Response", "PostData2-----------" + e.getMessage());
                            mStateview.showRetry();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        mStateview.showRetry();
                    }
                });
    }

    public void update() {
        mStateview.showLoading();
        Log.d("user_id", "--------------------" + user_id);
        Log.d("first_name", "--------------------" + medtFulName.getText().toString().trim());
        Log.d("mobile", "--------------------" + medtPhone.getText().toString().trim());
        Log.d("email", "--------------------" + medtEmail.getText().toString().trim());
        Log.d("password", "--------------------" + medtPassword.getText().toString().trim());
        Log.d("image", "--------------------" + profile);

        AndroidNetworking.upload(update_profile)
                .addMultipartParameter("user_id", user_id)
                .addMultipartParameter("first_name", medtFulName.getText().toString().trim())
                .addMultipartParameter("mobile", medtPhone.getText().toString().trim())
                .addMultipartParameter("email", medtEmail.getText().toString().trim())
                .addMultipartParameter("password", medtPassword.getText().toString().trim())
                .addMultipartParameter("address", medtAddress.getText().toString().trim())
                .addMultipartFile("image", profile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "PostData1-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("success").equalsIgnoreCase("1")) {
                                if (response.getString("code").equalsIgnoreCase("200")) {
                                    medtFulName.setEnabled(false);
                                    medtEmail.setEnabled(false);
                                    medtPassword.setEnabled(false);
                                    medtAddress.setEnabled(false);
                                    mbtnSave.setEnabled(false);
                                    mImgSelectProfile.setEnabled(false);
                                    String msg = response.getString("message");
                                    Log.d("message", "---------------" + msg);
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Response", "PostData2-----------" + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Response", "PostData3-----------" + anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void onRetryClick() {

    }

    public boolean validation() {
        if (medtFulName.getText().toString().trim().isEmpty()) {
            MyToast.display(getActivity(), "Name Should not be Empty");
            return false;
        } else if (!isValidEmailId(medtEmail.getText().toString().trim())) {
            MyToast.display(getActivity(), "Please Enter Valid Email ID");
            return false;
        } else if (medtPassword.getText().toString().isEmpty()) {
            MyToast.display(getActivity(), "Password Should not be Empty");
            return false;
        }
        return true;
    }

    private boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
