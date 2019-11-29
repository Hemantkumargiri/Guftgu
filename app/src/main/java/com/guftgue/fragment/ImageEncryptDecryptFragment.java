package com.guftgue.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dorianmusaj.cryptolight.CryptoLight;
import com.guftgue.R;
import com.guftgue.helper.AESHelper;
import com.guftgue.helper.PermissionsUtil;
import com.guftgue.model.encrSendlist;
import com.guftgue.others.MyPreferences;
import com.guftgue.others.MyStateview;
import com.guftgue.others.MyToast;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.utils.MyEncrypter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.guftgue.helper.AESHelper.getFile;
import static com.guftgue.others.ApiServices.get_my_encryption_setting;
import static com.guftgue.others.ApiServices.get_user_sended_and_reacive_encription_list;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageEncryptDecryptFragment extends Fragment {

    private static final String FILE_NAME_ENC = "fruit_image_enc";
    private static final String FILE_NAME_DEC = "fruit_image_enc.png";
    String my_key = "kRLsRLp4R9cscZQn";
    String my_spec_key = "BvTceY2xKL6r8shz";
    private ImageView mImgViewEncryption, mImgViewDecryption, mImageView;
    private Button mSelectImageEncryption, mSelectImageDecryption, mBtnCreateKey, mBtnEncrypt, mBtnDecrypt;
    private TextView mTxtBtnEncrypt, mTxtBtnDecrypt, mTxtViewGeneratedKey;
    private Uri mCropImageUri;
    private Uri path;
    private File mFileImageEncrypt, mFileImageDecrypt, myDir;
    public SharedPreferences sharedPreferences;
    View view;
    String mCurrentUser, mByContactUser, mobile, user_id;
    private Spinner mSpinnerUserList;
    private String selectedItemType;
    public static final String mypreference = "mypref";
    public static final String Name = "first_name";

    public ImageEncryptDecryptFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_encrypt_decrypt, container, false);
        init(view);
        setListener();

        Dexter.withActivity(getActivity())
                .withPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                })
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        mBtnEncrypt.setEnabled(true);
                        mBtnDecrypt.setEnabled(true);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(getActivity(), "You must enable permission.", Toast.LENGTH_SHORT).show();
                    }
                }).check();
        return view;
    }


    private void setListener() {

        mBtnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//convert drawable to bitmap
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fruits);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

                //create file
                File outPutFileEnc = new File(myDir, FILE_NAME_ENC);
                try {
                    try {
                        MyEncrypter.encryptToFile(my_key, my_spec_key, inputStream, new FileOutputStream(outPutFileEnc));
                        Toast.makeText(getActivity(), "Encrypted", Toast.LENGTH_SHORT).show();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mBtnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outPutFileDec = new File(myDir, FILE_NAME_DEC);
                File encFile = new File(myDir,FILE_NAME_ENC);
                try{
                    try {
                        MyEncrypter.decryptToFile(my_key,my_spec_key,new FileInputStream(encFile),new FileOutputStream(outPutFileDec));

                        mImageView.setImageURI(Uri.fromFile(outPutFileDec));

                        Toast.makeText(getActivity(), "Decrypted", Toast.LENGTH_SHORT).show();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
       /* mBtnCreateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               *//* if (selectedItemType == null) {
                    MyToast.display(getActivity(), "Please select encryption User first");

                } else {
                    //hideKeyboard(v);
                    //MyToast.display(getActivity(), "Message has Encrypted Successfully");
                    //parseResultEncrypt();


                }*//*
                Random random = new Random();
                String generatedPassword = String.format("%04d", random.nextInt(10000));
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                Calendar c = Calendar.getInstance();
                String date = sdf.format(c.getTime());
                String text = mCurrentUser + "_" + generatedPassword + "_" + mByContactUser;
                mTxtViewGeneratedKey.setText(text);
            }
        });*/

        /*mSpinnerUserList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    hideKeyboard(view);
                    selectedItemName = listEncrSend.get(position).getEncription_name();
                    selectedItemType = listEncrSend.get(position).getEncription_type();
                    ReceiverId = listEncrSend.get(position).getId();
                    Log.d("selectedItemName", "-----------------" + selectedItemName);
                    Log.d("selectedItemType", "-----------------" + selectedItemType);
                    Log.d("ReceiverId", "-----------------" + ReceiverId);
                    validateAllData1();
                    mTxtViewEncryptionType.setText(selectedItemName);
                } else {
                    selectedItemType = null;
                    selectedItemName = null;

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
       /* mSelectImageEncryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageEncryptClick(view);
            }
        });

        mSelectImageDecryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageDecryptClick(view);
            }
        });

        mTxtBtnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //path = result.getUri();
                Log.d("path", "----------------------" + path);
                mFileImageEncrypt = new File(path.getPath());


                String publicKey = CryptoLight.getPublicKey(getActivity());
                String privateKey = CryptoLight.getPrivateKey(getActivity());

                String filePath = mFileImageEncrypt.getAbsolutePath();

                File myDirectory = new File(Environment.getExternalStorageDirectory().toString() +
                        "/Guftgu/Images");

                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }

                KeyGenerator keyGenerator = null;
                try {
                    keyGenerator = KeyGenerator.getInstance("AES");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                keyGenerator.init(256);
                Key key = keyGenerator.generateKey();

                byte[] content = getFile(mFileImageEncrypt);
                System.out.println("FILE CONTENT" + content);

                byte[] encrypted = AESHelper.encryptPdfFile(key, content);
                System.out.println("encrypted -->" + encrypted);

                byte[] decrypted = AESHelper.decryptPdfFile(key, encrypted);
                System.out.println("decrypted -->" + decrypted);

                Bitmap bmp1 = BitmapFactory.decodeByteArray(decrypted, 0, decrypted.length);
                Bitmap mutableBitmap1 = bmp1.copy(Bitmap.Config.ARGB_8888, true);
                mImgViewDecryption.setImageBitmap(mutableBitmap1);



               *//* Bitmap bmp = BitmapFactory.decodeByteArray(decrypted, 0, decrypted.length);
                Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
                fab.setImageBitmap(mutableBitmap);*//*

                try {
                    AESHelper.saveFile(decrypted, myDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mTxtBtnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    private void onSelectImageDecryptClick(View view) {
        CropImage.startPickImageActivity(getActivity(), ImageEncryptDecryptFragment.this);
    }

    private void onSelectImageEncryptClick(View view) {
        CropImage.startPickImageActivity(getActivity(), ImageEncryptDecryptFragment.this);
    }


    private void init(View view) {

        /*mSelectImageEncryption = view.findViewById(R.id.btnSelectImageEncrypt);
        mSelectImageDecryption = view.findViewById(R.id.btnSelectImageDecrypt);
        mImgViewEncryption = view.findViewById(R.id.ivEncryption);
        mImgViewDecryption = view.findViewById(R.id.ivDecryption);
        mTxtBtnEncrypt = view.findViewById(R.id.txtBtnEncrypt);
        mTxtBtnDecrypt = view.findViewById(R.id.txtBtnDecrypt);
        mBtnCreateKey = view.findViewById(R.id.btnCreateKey);
        mTxtViewGeneratedKey = view.findViewById(R.id.txtViewGeneratedKey);*/
        mImageView = view.findViewById(R.id.imageView);
        mBtnEncrypt = view.findViewById(R.id.btnEncrypt);
        mBtnDecrypt = view.findViewById(R.id.btnDecrypt);

        myDir = new File(Environment.getExternalStorageDirectory().toString() + "/gugtfu_image");


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(mypreference, MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        mobile = sharedPreferences.getString("mobile", "");
        mCurrentUser = sharedPreferences.getString("user_details", "");
        String s = "";
        /*SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("login", MODE_PRIVATE);
        mCurrentUser = sharedPreferences.getString("first_name", "");
        SharedPreferences.Editor ed = sharedPreferences.edit();
        mobile = sharedPreferences.getString("mobile", "");
        //ed.putString("first_name", password);
        ed.commit();*/

        /*sharedPreferences = this.getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        if (sharedPreferences.contains(Name)) {
            mCurrentUser = sharedPreferences.getString(Name, "");
        }*/


        //Log.d("user_id", "----------------" + user_id);
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
                mImgViewEncryption.setImageURI(result.getUri());

            }
            //mImgViewEncryption.setImageURI(result.getUri());

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


}
