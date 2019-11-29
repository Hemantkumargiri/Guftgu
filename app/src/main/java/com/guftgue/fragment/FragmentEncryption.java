package com.guftgue.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.activity.ShowEncryptedImageActivity;
import com.guftgue.others.MyStateview;
import com.guftgue.others.MyToast;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.R;
import com.guftgue.model.encrSendlist;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.guftgue.others.ApiServices.get_my_encryption_setting;
import static com.guftgue.others.ApiServices.get_user_sended_and_reacive_encription_list;

public class FragmentEncryption extends Fragment implements ProgressClickListener {
    private Spinner spin1;
    private MyStateview mStateview;
    private String user_id = "";
    private String encryption;
    private String enlogic;
    private EditText medtmessage;
    private Button mbtnencrypt;
    private Button btnImageencrypt;
    private TextView mTvencryptmsg, mTvencryptionType;
    private String encp;
    List<String> Tospinner = new ArrayList<>();
    List<encrSendlist> listEncrSend;
    private String encryption_type;
    private String name;
    private String selectedItemName;
    private String selectedItemType;
    private LinearLayout llencrypy;
    private String ReceiverId;
    private Switch SwChange;
    private LinearLayout llForTextEncryption;
    private LinearLayout llImageEncryption;
    private LinearLayout llSelectImage;
    private ImageView SelectedImage;
    private Uri path;
    private File profile;
    private String image = "";
    private Uri mCropImageUri;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_encryption, container, false);
        mStateview = new MyStateview(this, view);
        medtmessage = view.findViewById(R.id.edtmessage);
        mbtnencrypt = view.findViewById(R.id.btnencrypt);
        mTvencryptmsg = view.findViewById(R.id.Tvencryptmsg);
        llencrypy = view.findViewById(R.id.llencrypy);
        mTvencryptionType = view.findViewById(R.id.TvencryptionType);
        SwChange = view.findViewById(R.id.SwChange);
        llImageEncryption = view.findViewById(R.id.llImageEncryption);
        llForTextEncryption = view.findViewById(R.id.llForTextEncryption);
        SelectedImage = view.findViewById(R.id.SelectedImage);
        llSelectImage = view.findViewById(R.id.llSelectImage);
        btnImageencrypt = view.findViewById(R.id.btnImageencrypt);


        btnImageencrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ShowEncryptedImageActivity.class));
            }
        });
        llSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });


        SwChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    llForTextEncryption.setVisibility(View.GONE);
                    llImageEncryption.setVisibility(View.VISIBLE);
                } else {
                    llForTextEncryption.setVisibility(View.VISIBLE);
                    llImageEncryption.setVisibility(View.GONE);
                }
            }
        });
        spin1 = view.findViewById(R.id.Spinner);
        llencrypy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        if (spin1.getCount() == 0) {

        }
        mbtnencrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedItemType == null) {
                    MyToast.display(getActivity(), "Please select encryption User first");

                } else if (TextUtils.isEmpty(medtmessage.getText().toString().trim())) {
                    MyToast.display(getActivity(), "Please Enter message");

                } else {
                    hideKeyboard(v);
                    MyToast.display(getActivity(), "Message has Encrypted Successfully");
                    parseResult();


                }


            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        Log.d("user_id", "----------------" + user_id);
        encryption = getArguments().getString("encryption");
        name = getArguments().getString("name");
        //validateAllData();
        selctUserData();

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    mTvencryptionType.setText(selectedItemName);
                } else {
                    selectedItemType = null;
                    selectedItemName = null;

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // mTvencryptionType.setText(encryption);
        return view;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void selctUserData() {
        mStateview.showLoading();
        AndroidNetworking.post(get_user_sended_and_reacive_encription_list)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SelectUserResponse", "-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                if (response.getString("success").equalsIgnoreCase("1")) {
                                    listEncrSend = new ArrayList<>();
                                    JSONArray categoryJesonArry = response.getJSONArray("All_list");
                                    if (categoryJesonArry != null) {
                                        encrSendlist dataone = new encrSendlist();
                                        dataone.setName("");
                                        dataone.setMobile("");
                                        dataone.setEncription_type("");
                                        dataone.setCreated_date("");
                                        Tospinner.add("Select");
                                        listEncrSend.add(dataone);
                                        for (int i = 0; i < categoryJesonArry.length(); i++) {
                                            encrSendlist data = new encrSendlist();
                                            JSONObject object = categoryJesonArry.getJSONObject(i);
                                            data.setName(object.getString("name"));
                                            //     data.setMobile(object.getString("mobile"));
                                            data.setEncription_type(object.getString("encription_type"));
                                            data.setEncription_name(object.getString("encription_name"));
                                            data.setId(object.getString("id"));
                                            //     data.setCreated_date(object.getString("created_date"));
                                            listEncrSend.add(data);
                                            Tospinner.add(object.getString("name"));
                                        }
                                        if (getActivity() != null) {
                                            spin1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Tospinner));
                                        }
                                    }

                                }
                            } else {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mStateview.showRetry();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        mStateview.showRetry();
                    }
                });

    }
/*    private void validateAllData() {
        TvHome.setEnabled(false);
        mStateview.showLoading();
        Log.d("user_id","----------------------"+user_id);
        Log.d("encryption","----------------------"+encryption);
        AndroidNetworking.post("http://alobhatechnology.com/new/encription_app/Api/get_my_encryption_setting")
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("encription_type", encryption)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SettingResponse", "-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                String message= response.getString("message");
                                TvHome.setEnabled(true);
                                JSONArray categoryJesonArry = response.getJSONArray("userencrSettingList");
                                for (int i = 0; i < categoryJesonArry.length(); i++) {
                                    JSONObject object = categoryJesonArry.getJSONObject(i);
                                    enlogic=object.getString("encryp_logic");
                                    Log.e("ENCCCRYPT","-------------"+enlogic);
                                    Log.e("ENCCCRYPT","-------------"+enlogic.trim().replaceAll(" ",""));
                                }

                                //}
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }

                });
    }*/

    private void validateAllData1() {
        mStateview.showLoading();
        Log.d("ReceiverId", "----------------------" + ReceiverId);
        Log.d("encryption", "----------------------" + selectedItemType);

        AndroidNetworking.post(get_my_encryption_setting)
                .addBodyParameter("user_id", ReceiverId)
                .addBodyParameter("encription_type", selectedItemType)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                String message = response.getString("message");
                                JSONArray categoryJesonArry = response.getJSONArray("userencrSettingList");

                                for (int i = 0; i < categoryJesonArry.length(); i++) {
                                    JSONObject object = categoryJesonArry.getJSONObject(i);
                                    enlogic = object.getString("encryp_logic");
                                    Log.e("ENCCCRYPT", "-------------" + enlogic);
                                    Log.e("ENCCCRYPT", "-------------" + enlogic.trim().replaceAll(" ", ""));
                                }

                                //}
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }

                });
    }

    private void parseResult() {

        HashMap<String, String> mDataMap = new HashMap<>();

        String[] res = enlogic.trim().replaceAll(" ", "").split(",");

        for (int i = 0; i < res.length; i++) {
            String tempString = res[i].trim();
            Log.e("TEMP", "-----------" + tempString);
            String key = tempString.substring(0, 1);//4
            String value = tempString.substring(3);//^
            Log.d("key", "-------------------" + key);
            Log.d("value", "-------------------" + value);
            mDataMap.put(key, value);
        }

        String typedValue = medtmessage.getText().toString().trim();
        Log.d("typevalue", "------------------" + typedValue);
        char[] array = typedValue.toCharArray();
        Log.d("array", "==================" + Arrays.toString(array));
        ArrayList<String> arrayList = new ArrayList<>();
        Log.d("data", "-----------------" + arrayList.toString());

        for (char item : array) {
            Log.d("item", "=================" + item);
            encp = mDataMap.get(Character.toString(item));
            arrayList.add(encp);
            Log.d("ecp", "" + arrayList);
            mTvencryptmsg.setText(arrayList.toString().replace("[", "")
                    .replace("]", "")
                    .replace(",", "")
                    .replace(" ", "")
                    .replace("null", " "));
        }


    }

    @Override
    public void onRetryClick() {

    }

    public void onSelectImageClick(View v) {
        CropImage.startPickImageActivity(getActivity(), FragmentEncryption.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
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
                SelectedImage.setImageURI(result.getUri());
                path = result.getUri();
                Log.d("path", "----------------------" + path);
                profile = new File(path.getPath());
                image = String.valueOf(profile);

                ((ImageView) view.findViewById(R.id.SelectedImage)).setImageURI(result.getUri());
                Toast.makeText(getActivity(), "Cropped", Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
