package com.guftgue.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.activity.CreateImageEncryptionActivity;
import com.guftgue.model.EncryptModel;
import com.guftgue.others.AppConstants;
import com.guftgue.others.MyStateview;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.R;
import com.guftgue.activity.CreateActivity;
import com.guftgue.activity.EncryptionViewActivity;
import com.guftgue.adapter.RecyclerViewAdapter;
import com.guftgue.model.DataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.guftgue.others.ApiServices.Chek_my_encryption_setting;

public class FragmentMyEncription extends Fragment implements ProgressClickListener {

    private RecyclerView recyclerView;
    private ArrayList<DataModel> arrayList;
    private RecyclerViewAdapter adapter;
    private Button mbtnCreate, mbtnView1, mbtnView2;
    private MyStateview mStateview;
    private String user_id;
    private String Encryption_type;
    private ArrayList<String> enList = new ArrayList<>();
    private ArrayList<String> settingList = new ArrayList<>();
    private ArrayList<String> encriptionNameList = new ArrayList<>();
    private ArrayList<EncryptModel> modelList = new ArrayList<>();
    private Button btnEdit, btnEdit1, btnEdit2;
    private String setting_id;
    private String encription_name;
    private TextView TvEncryption1, TvEncryption2, TvEncryption3;
    private String SettingId;
    /*private Button btnView3;
    private Button btnEdit3;*/

    @Override
    public void onResume() {
        super.onResume();
        /*TvHome.setTextColor(getResources().getColor(R.color.black));
        TvHome.setBackgroundColor(getResources().getColor(R.color.white));
        tvUserlist.setTextColor(getResources().getColor(R.color.black));
        tvUserlist.setBackgroundColor(getResources().getColor(R.color.white));
        Tvmyencryption.setTextColor(getResources().getColor(R.color.white));
        Tvmyencryption.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Tvmyprofile.setTextColor(getResources().getColor(R.color.black));
        Tvmyprofile.setBackgroundColor(getResources().getColor(R.color.white));*/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_encryption, container, false);
        mStateview = new MyStateview(this, view);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");

        Log.d("user_id", "----------------" + user_id);

        mbtnCreate = view.findViewById(R.id.btnCreate);
        mbtnView1 = view.findViewById(R.id.btnView1);
        mbtnView2 = view.findViewById(R.id.btnView2);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnEdit1 = view.findViewById(R.id.btnEdit1);
        btnEdit2 = view.findViewById(R.id.btnEdit2);
        TvEncryption3 = view.findViewById(R.id.TvEncryption3);
        TvEncryption2 = view.findViewById(R.id.TvEncryption2);
        TvEncryption1 = view.findViewById(R.id.TvEncryption1);
        /*btnView3 = view.findViewById(R.id.btnView3);
        btnEdit3 = view.findViewById(R.id.btnEdit3);*/

        /*btnView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CreateImageEncryptionActivity.class));

            }
        });
        btnEdit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CreateImageEncryptionActivity.class));

            }
        });*/

        //    mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        //   mStateview = new MyStateview(this, mSwipeRefreshLayout);
        isOnline();
        validateAllData();
    /*    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                validateAllData();
            }
        });*/


        mbtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbtnCreate.getText().equals("View")) {
                    String EncrptionName= getEncryptionName(AppConstants.ENCRYPTION_ONE);
                    startActivity(new Intent(getActivity(), EncryptionViewActivity.class)
                            .putExtra(AppConstants.EN_VIEW,EncrptionName )
                            .putExtra("encryption_type", AppConstants.ENCRYPTION_ONE )
                            .putExtra("type", "view"));
                } else {
                    startActivity(new Intent(getActivity(), CreateActivity.class)
                            .putExtra(AppConstants.EN_TYPE, AppConstants.ENCRYPTION_ONE)
                            .putExtra("type", "view"));
                }
            }
        });
        mbtnView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbtnView1.getText().equals("View")) {
                    String EncrptionName= getEncryptionName(AppConstants.ENCRYPTION_TWO);
                    startActivity(new Intent(getActivity(), EncryptionViewActivity.class)
                            .putExtra(AppConstants.EN_VIEW, EncrptionName)
                            .putExtra("encryption_type", AppConstants.ENCRYPTION_TWO)
                            .putExtra("type", "view"));
                } else {
                    startActivity(new Intent(getActivity(), CreateActivity.class)
                            .putExtra(AppConstants.EN_TYPE, AppConstants.ENCRYPTION_TWO)
                            .putExtra("type", "view"));
                }
            }
        });
        mbtnView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbtnView2.getText().equals("View")) {
                    String EncrptionName= getEncryptionName(AppConstants.ENCRYPTION_THREE);
                    startActivity(new Intent(getActivity(), EncryptionViewActivity.class)
                            .putExtra(AppConstants.EN_VIEW, EncrptionName)
                            .putExtra("encryption_type", AppConstants.ENCRYPTION_THREE)
                            .putExtra("type", "view"));
                } else {
                    startActivity(new Intent(getActivity(), CreateActivity.class)
                            .putExtra(AppConstants.EN_TYPE, AppConstants.ENCRYPTION_THREE)
                            .putExtra("type", "view"));
                }
            }
        });
        return view;
    }

    private String getEncryptionName(String Type) {

        if(modelList!=null&&modelList.size()>0)
        {
            for (int i=0;i<modelList.size();i++){
                if(modelList.get(i).getEncypType().equalsIgnoreCase(Type)){
                    return modelList.get(i).getEncryptName();
                }
            }

        }
        return "";
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) Objects.requireNonNull(getActivity()).getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Snackbar snackbar = Snackbar
                    .make(getActivity().getWindow().getDecorView().getRootView(), "No Internet connection", Snackbar.LENGTH_SHORT);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            TextView textView1 = (TextView) snakView.findViewById(R.id.snackbar_action);
            textView1.setTextColor(Color.BLACK);
            textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
            textView1.setTextSize(16);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize(16);
            snackbar.show();
            //Toast.makeText(FragmentDashBoard.this, "No Internet connection!", Toast.LENGTH_LONG).show();
            //  mProgressBarDialog.showLoading();
            return false;
        }
        return true;
    }

    private void validateAllData() {
        mStateview.showLoading();
        AndroidNetworking.post(Chek_my_encryption_setting)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        modelList.clear();


                        Log.d("Response", "-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                if (response.getString("success").equalsIgnoreCase("1")) {
                                    JSONArray categoryJesonArry = response.getJSONArray("CheckSettingList");
                                    Log.d("categoryJesonArry", "================" + categoryJesonArry);
                                    for (int i = 0; i < categoryJesonArry.length(); i++) {
                                        JSONObject object = categoryJesonArry.getJSONObject(i);
                                        Encryption_type = object.getString("encription_type");
                                        setting_id = object.getString("setting_id");
                                        encription_name = object.getString("encription_name");
                                        enList.add(object.getString("encription_type"));
                                        settingList.add(object.getString("setting_id"));
                                        encriptionNameList.add(object.getString("encription_name"));
                                        Log.d("name", "------------------" + settingList);
                                        String list = String.valueOf(enList.add(object.getString("encription_type")));
                                        Log.d("list", "================" + list);


                                        EncryptModel encryptModel = new EncryptModel();
                                        encryptModel.setEncryptName(object.getString("encription_name"));
                                        encryptModel.setEncypType(object.getString("encription_type"));
                                        encryptModel.setSetting_id(object.getString("setting_id"));

                                        modelList.add(encryptModel);


                                    }



                                    for ( int i = 0; i < modelList.size(); i++) {

                                        TextView encryptTYextView = getTextView(modelList.get(i).getEncypType());
                                        if (encryptTYextView != null) {
                                            encryptTYextView.setText(modelList.get(i).getEncryptName());
                                            SettingId= modelList.get(i).getSetting_id();

                                        }
                                    }
                                    btnEdit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String settingID = getSettingID("encryption1");

                                            Log.d("Setting_id","----------"+SettingId);
                                            startActivity(new Intent(getActivity(), CreateActivity.class)
                                                    .putExtra(AppConstants.EN_TYPE, AppConstants.ENCRYPTION_ONE)
                                                    .putExtra("type", "edit")
                                                    .putExtra("setting_id", settingID));
                                        }
                                    });
                                    btnEdit1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String settingID = getSettingID("encryption2");

                                            startActivity(new Intent(getActivity(), CreateActivity.class)
                                                    .putExtra(AppConstants.EN_TYPE, AppConstants.ENCRYPTION_TWO)
                                                    .putExtra("type", "edit")
                                                    .putExtra("setting_id",settingID));
                                        }
                                    });
                                    btnEdit2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String settingID = getSettingID("encryption1");

                                            startActivity(new Intent(getActivity(), CreateActivity.class)
                                                    .putExtra(AppConstants.EN_TYPE, AppConstants.ENCRYPTION_THREE)
                                                    .putExtra("type", "edit")
                                                    .putExtra("setting_id", settingID));
                                        }
                                    });



                                    setUpDFata(enList);
                                }
                            } else {
                                setUpDFata(enList);
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

    private String getSettingID(String type) {

        if(modelList!=null&&modelList.size()>0)
        {
            for (int i=0;i<modelList.size();i++){
                if(modelList.get(i).getEncypType().equalsIgnoreCase(type)){
                    return modelList.get(i).getSetting_id();
                }
            }

        }
        return "";
    }

    private TextView getTextView(String encypType) {

        switch (encypType) {
            case "encryption1":
                return TvEncryption1;

            case "encryption2":

                return TvEncryption2;

            case "encryption3":

                return TvEncryption3;


        }

        return null;
    }

    private void setUpDFata(ArrayList<String> enList) {
        if (enList != null && enList.size() > 0) {

            if (enList.contains(AppConstants.ENCRYPTION_ONE)) {
                mbtnCreate.setText("View");
                btnEdit.setVisibility(View.VISIBLE);
            } else {
                mbtnCreate.setText("Create");
            }
            if (enList.contains(AppConstants.ENCRYPTION_TWO)) {
                mbtnView1.setText("View");
                btnEdit1.setVisibility(View.VISIBLE);

            } else {
                mbtnView1.setText("Create");
            }
            if (enList.contains(AppConstants.ENCRYPTION_THREE)) {
                mbtnView2.setText("View");
                btnEdit2.setVisibility(View.VISIBLE);
            } else {
                mbtnView2.setText("Create");
            }

        } else {
            mbtnCreate.setText("Create");
            mbtnView1.setText("Create");
            mbtnView2.setText("Create");
        }

    }


    @Override
    public void onRetryClick() {

    }
}
