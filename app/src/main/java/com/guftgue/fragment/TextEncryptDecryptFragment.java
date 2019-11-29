package com.guftgue.fragment;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.R;
import com.guftgue.model.encrSendlist;
import com.guftgue.others.MyStateview;
import com.guftgue.others.MyToast;
import com.guftgue.others.ProgressClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.guftgue.others.ApiServices.get_my_encryption_setting;
import static com.guftgue.others.ApiServices.get_user_sended_and_reacive_encription_list;

/**
 * A simple {@link Fragment} subclass.
 */
public class TextEncryptDecryptFragment extends Fragment implements ProgressClickListener, View.OnClickListener {

    private TextView mTextBtnCopyEncrypt, mTextBtnPasteEncrypt, mTextBtnCopyDecrypt, mTextBtnPasteDecrypt;
    private ImageButton mImgBtnEncryptClear, mImgBtnShareEncryptMsg, mImgBtnDecryptClear, mImgBtnShareDecryptMsg;

    private String mTextMessage = "";
    private ClipboardManager myClipboard;
    private ClipData myClip;
    private Context context;

    private Spinner mSpinnerUserList;
    private MyStateview mStateview;
    private String user_id = "";
    private String encryption;
    private String name;
    private String enlogic;
    private String encp;
    private String mobile;
    private String ReceiverId;
    private String selectedItemName;
    private String selectedItemType;
    List<String> Tospinner = new ArrayList<>();
    List<encrSendlist> listEncrSend;
    private TextView mTxtViewEncryptionType, mTxtBtnEncrypt, mTxtBtnDecrypt;
    private EditText mEditTextEncryptMsg, mEditTextDecryptMsg;

    public TextEncryptDecryptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_encrypt_decrypt, container, false);
        init(view);
        setListener();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        mobile = sharedPreferences.getString("mobile", "");
        Log.d("user_id", "----------------" + user_id);
        encryption = getArguments().getString("encryption");
        name = getArguments().getString("name");
        //validateAllData();
        selctUserData();

        return view;
    }


    private void setListener() {
        mTextBtnCopyEncrypt.setOnClickListener(this);
        mTextBtnPasteEncrypt.setOnClickListener(this);
        mImgBtnEncryptClear.setOnClickListener(this);
        mImgBtnShareEncryptMsg.setOnClickListener(this);
        mTextBtnCopyDecrypt.setOnClickListener(this);
        mTextBtnPasteDecrypt.setOnClickListener(this);
        mImgBtnDecryptClear.setOnClickListener(this);
        mImgBtnShareDecryptMsg.setOnClickListener(this);
        if (mSpinnerUserList.getCount() == 0) {

        }

        mTxtBtnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemType == null) {
                    MyToast.display(getActivity(), "Please select encryption User first");

                } else if (TextUtils.isEmpty(mEditTextEncryptMsg.getText().toString().trim())) {
                    MyToast.display(getActivity(), "Please Enter message");

                } else {
                    hideKeyboard(v);
                    MyToast.display(getActivity(), "Message has Encrypted Successfully");
                    parseResultEncrypt();


                }
            }
        });

        mTxtBtnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemType == null) {
                    MyToast.display(getActivity(), "Please select decryption User first");

                } else if (TextUtils.isEmpty(mEditTextDecryptMsg.getText().toString().trim())) {
                    MyToast.display(getActivity(), "Please Enter message");

                } else {
                    hideKeyboard(v);
                    MyToast.display(getActivity(), "Message has Decrypted Successfully");
                    parseResultDecrypt();


                }
            }
        });

        mSpinnerUserList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    hideKeyboard(view);
                    selectedItemName = listEncrSend.get(position).getEncription_name();
                    selectedItemType = listEncrSend.get(position).getEncription_type();
                    ReceiverId = listEncrSend.get(position).getId();
                    //Get the Target User Name
                    String selectedTargetUser =mSpinnerUserList.getSelectedItem().toString();

                    Log.d("selectedTargetUser", "-----------------" + selectedTargetUser);
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
        });
    }

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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void init(View view) {
        mStateview = new MyStateview(this, view);
        mSpinnerUserList = view.findViewById(R.id.spinnerUserList);
        mTxtViewEncryptionType = view.findViewById(R.id.txtViewEncryptionType);
        mTxtBtnEncrypt = view.findViewById(R.id.txtBtnEncrypt);
        mTxtBtnDecrypt = view.findViewById(R.id.txtBtnDecrypt);
        mEditTextEncryptMsg = view.findViewById(R.id.editTextEncryptMsg);
        mEditTextDecryptMsg = view.findViewById(R.id.editTextDecryptMsg);
        mTextBtnCopyEncrypt = view.findViewById(R.id.textBtnCopyEncrypt);
        mTextBtnPasteEncrypt = view.findViewById(R.id.textBtnPasteEncrypt);
        mImgBtnEncryptClear = view.findViewById(R.id.imgBtnEncryptClear);
        mImgBtnShareEncryptMsg = view.findViewById(R.id.imgBtnShareEncryptMsg);
        mTextBtnCopyDecrypt = view.findViewById(R.id.textBtnCopyDecrypt);
        mTextBtnPasteDecrypt = view.findViewById(R.id.textBtnPasteDecrypt);
        mImgBtnDecryptClear = view.findViewById(R.id.imgBtnDecryptClear);
        mImgBtnShareDecryptMsg = view.findViewById(R.id.imgBtnShareDecryptMsg);

    }

    private void parseResultEncrypt() {

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

        String typedValue = mEditTextEncryptMsg.getText().toString().trim();
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
            mEditTextDecryptMsg.setText(arrayList.toString().replace("[", "")
                    .replace("]", "")
                    .replace(",", "")
                    .replace(" ", "")
                    .replace("null", " "));
        }


    }

    private void parseResultDecrypt() {

        HashMap<String, String> mDataMap = new HashMap<>();

        String[] res = enlogic.trim().replaceAll(" ", "").split(",");

        for (int i = 0; i < res.length; i++) {
            String tempString = res[i].trim();
            Log.e("TEMP", "-----------" + tempString);
            String key = tempString.substring(3);//4
            String value = tempString.substring(0, 1); //^
            Log.d("key", "-------------------" + key);
            Log.d("value", "-------------------" + value);
            mDataMap.put(key, value);

        }


        String typedValue = mEditTextDecryptMsg.getText().toString().trim();
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

            mEditTextEncryptMsg.setText(arrayList.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .replace(",", "")
                    .replace(" ", "")
                    .replace("null", " "));
        }
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
                                            mSpinnerUserList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Tospinner));
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


    @Override
    public void onRetryClick() {

    }

    @Override
    public void onClick(View view) {
        if (view == mTextBtnCopyEncrypt) {
            mTextMessage = mEditTextEncryptMsg.getText().toString().trim();
            if (mTextMessage.equals("")) {
                Toast.makeText(getContext(), "No Text for copied", Toast.LENGTH_LONG).show();
            } else {
                setClipboard(getContext(), mTextMessage);
                Toast.makeText(getContext(), "Text copied", Toast.LENGTH_LONG).show();
            }

        } else if (view == mTextBtnPasteEncrypt) {
            readFromClipboardToEncrypt();
            //mEditTextEncryptMsg.setText(mTextMessage);
            Toast.makeText(getContext(), "Text paste successfully.", Toast.LENGTH_LONG).show();
        } else if (view == mImgBtnEncryptClear) {
            mTextMessage = mEditTextEncryptMsg.getText().toString().trim();
            if (mTextMessage.equals("")) {
                Toast.makeText(getContext(), "Text is not available for deleting.", Toast.LENGTH_LONG).show();
            } else {
                //mTextMessage = mEditTextEncryptMsg.getText().toString().trim();
                //mEditTextEncryptMsg.setText("");
                mEditTextEncryptMsg.getText().clear();
                Toast.makeText(getContext(), "Text has been clear.", Toast.LENGTH_LONG).show();
            }
        } else if (view == mImgBtnShareEncryptMsg) {
            mTextMessage = mEditTextEncryptMsg.getText().toString().trim();
            if (mTextMessage.equals("")) {
                Toast.makeText(getContext(), "Text is not available for sharing.", Toast.LENGTH_LONG).show();
            } else {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, mTextMessage);
                startActivity(Intent.createChooser(share, "Share Text"));
            }

        } else if (view == mTextBtnCopyDecrypt) {
            mTextMessage = mEditTextDecryptMsg.getText().toString().trim();
            if (mTextMessage.equals("")) {
                Toast.makeText(getContext(), "No Text for copied", Toast.LENGTH_LONG).show();
            } else {
                setClipboard(getContext(), mTextMessage);
                Toast.makeText(getContext(), "Text copied", Toast.LENGTH_LONG).show();
            }

        } else if (view == mTextBtnPasteDecrypt) {
            readFromClipboardToDecrypt();
            //mEditTextDecryptMsg.setText(mTextMessage);
            Toast.makeText(getContext(), "Text paste successfully.", Toast.LENGTH_LONG).show();
        } else if (view == mImgBtnDecryptClear) {
            mTextMessage = mEditTextDecryptMsg.getText().toString().trim();
            if (mTextMessage.equals("")) {
                Toast.makeText(getContext(), "Text is not available for deleting.", Toast.LENGTH_LONG).show();
            } else {
                //mTextMessage = mEditTextEncryptMsg.getText().toString().trim();
                //mEditTextEncryptMsg.setText("");
                mEditTextDecryptMsg.getText().clear();
                Toast.makeText(getContext(), "Text has been clear.", Toast.LENGTH_LONG).show();
            }
        } else if (view == mImgBtnShareDecryptMsg) {
            mTextMessage = mEditTextDecryptMsg.getText().toString().trim();
            if (mTextMessage.equals("")) {
                Toast.makeText(getContext(), "Text is not available for sharing.", Toast.LENGTH_LONG).show();
            } else {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, mTextMessage);
                startActivity(Intent.createChooser(share, "Share Text"));
            }
        }
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    /*public String readFromClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            android.content.ClipDescription description = clipboard.getPrimaryClipDescription();
            android.content.ClipData data = clipboard.getPrimaryClip();
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                String text = String.valueOf(data.getItemAt(0).getText());
                //mEditTextEncryptMsg.setText(text);
                return text;
            }

        }
        return null;
    }*/

    private String readFromClipboardToEncrypt() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            ClipData.Item item = clip.getItemAt(clip.getItemCount() - 1);
            String text = item.getText().toString();
            mEditTextEncryptMsg.setText(text);
            return text;
        }
        return "";

    }

    private String readFromClipboardToDecrypt() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            ClipData.Item item = clip.getItemAt(clip.getItemCount() - 1);
            String text = item.getText().toString();
            mEditTextDecryptMsg.setText(text);
            return text;
        }
        return "";

    }
}
