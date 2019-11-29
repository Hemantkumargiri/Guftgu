package com.guftgue.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.others.AppConstants;
import com.guftgue.others.MyStateview;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.R;
import com.guftgue.activity.ContactListActivity;
import com.guftgue.adapter.UserListRecyclerViewAdapter;
import com.guftgue.model.DataModel;
import com.guftgue.others.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.guftgue.fragment.FragmentEncryptions.Imgfilter;
import static com.guftgue.others.ApiServices.Chek_my_encryption_setting;
import static com.guftgue.others.ApiServices.get_user_sended_encription_list;

public class FragmentUserList extends Fragment implements ProgressClickListener,UserListRecyclerViewAdapter.ItemListener {

    private RecyclerView recyclerView;
    private ArrayList<DataModel> arrayList=new ArrayList<>();
    private UserListRecyclerViewAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private MyStateview mStateview;
    private String user_id;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvnote;
    private String encription_type;
    private String name;
    public View view;
    private ArrayList<String> encriptionNameList = new ArrayList<>();
    private String encription_name;
    CheckBox cb1,cb2,cb3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_userlist,container,false);
        //Imgfilter.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                arrayList.clear();
                validateAllData();
                }
        });
        mStateview = new MyStateview(this, mSwipeRefreshLayout);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");
        Log.d("user_id","----------------"+user_id);
        mTvnote=view.findViewById(R.id.Tvnote);
        validateAllData();

        fab= view.findViewById(R.id.fab);


        /*Imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEncryptionName(v);
            }
        });*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),ContactListActivity.class));
                getActivity().overridePendingTransition(0,0);

            }
        });
        toolbar=view.findViewById(R.id.toolbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.RvUserList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        ViewUtil.fabRecyclerViewCoodinator(fab,recyclerView);
        return view;
    }

    private void setEncryptionName(final View v) {
        if (encriptionNameList != null) {
            encriptionNameList.clear();
        }
        mStateview.showLoading();
        AndroidNetworking.post(Chek_my_encryption_setting)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                if (response.getString("success").equalsIgnoreCase("1")) {
                                    popupCall(v);
                                    JSONArray categoryJesonArry = response.getJSONArray("CheckSettingList");
                                    Log.d("categoryJesonArry", "================" + categoryJesonArry);
                                    for (int i = 0; i < categoryJesonArry.length(); i++) {
                                        JSONObject object = categoryJesonArry.getJSONObject(i);
                                        encription_name = object.getString("encription_name");
                                        encriptionNameList.add(object.getString("encription_name"));
                                    }

                                    if (encriptionNameList.size() > 0) {
                                        if (encriptionNameList.get(0).trim().isEmpty()) {
                                            cb1.setText("Encryption 1");
                                        } else {
                                            cb1.setText(encriptionNameList.get(0));
                                        }
                                    }
                                    if (encriptionNameList.size() > 1) {
                                        if (encriptionNameList.get(1).trim().isEmpty()) {
                                            cb2.setText("Encryption 2");
                                        } else {
                                            cb2.setText(encriptionNameList.get(1));
                                        }
                                    }
                                    if (encriptionNameList.size() > 2) {
                                        if (encriptionNameList.get(2).trim().isEmpty()) {
                                            cb3.setText("Encryption 3");
                                        } else {
                                            cb3.setText(encriptionNameList.get(2));

                                        }
                                    }
                                }
                            } else {
                                mStateview.showContent();
                                //Toast.makeText(ContactListActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void validateAllData() {
        mStateview.showLoading();
        AndroidNetworking.post(get_user_sended_encription_list)
                .addBodyParameter("user_id",user_id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                if(response.getString("success").equalsIgnoreCase("1")){
                                mTvnote.setVisibility(View.GONE);
                                arrayList=new ArrayList<>();
                                JSONArray categoryJesonArry = response.getJSONArray("encrSendlist");
                                Log.d("jsonAray","--------------------"+categoryJesonArry);
                                if(categoryJesonArry !=null) {
                                    for (int i = 0; i < categoryJesonArry.length(); i++) {
                                        JSONObject object = categoryJesonArry.getJSONObject(i);
                                        Log.d("object", "--------------" + object);
                                        DataModel data = new DataModel();
                                        data.setName(object.getString("name"));
                                        data.setEncription_type(object.getString("encription_type"));
                                        data.setImage(object.getString("image"));

                                        arrayList.add(data);
                                    }
                                    if (getActivity() != null) {
                                        adapter = new UserListRecyclerViewAdapter(getActivity(), arrayList, recyclerView, FragmentUserList.this);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }

                            }}else{
                                mTvnote.setVisibility(View.VISIBLE);


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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick1(String adapterPosition, String adapterPosition1) {
        encription_type=adapterPosition;
        name = adapterPosition1;
        Log.d("encription_type","------------------"+encription_type);
        Bundle bundle=new Bundle();
        EncryptDecryptFragment fragment = new EncryptDecryptFragment();
        bundle.putString("main", "main");
        bundle.putString("encryption",encription_type);
        fragment.setArguments(bundle);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).addToBackStack(null).commit();
       /* startActivity(new Intent(getActivity(), EncryptActivity.class)
        .putExtra("encryption",encription_type)
        .putExtra("name",name));*/
    }

    public void popupCall(View v){
        final PopupWindow popup = new PopupWindow(getActivity());
        final View layout = getLayoutInflater().inflate(R.layout.fragment_filter, null);
        cb1 = layout.findViewById(R.id.Cbencrption1);
        cb2 = layout.findViewById(R.id.Cbencrption2);
        cb3 = layout.findViewById(R.id.Cbencrption3);
        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb1.isChecked()){
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    arrayList.clear();
                    Log.d("user_id","---------------"+user_id);
                    Log.d("encription_type","---------------"+ AppConstants.ENCRYPTION_ONE);
                    mStateview.showLoading();
                    AndroidNetworking.post(get_user_sended_encription_list)
                            .addBodyParameter("user_id",user_id)
                            .addBodyParameter("encription_type", AppConstants.ENCRYPTION_ONE)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Response", "-----------" + response);
                                    try {
                                        mStateview.showContent();
                                        if(response.getString("success").equalsIgnoreCase("1")){
                                            mTvnote.setVisibility(View.GONE);
                                            arrayList=new ArrayList<>();
                                            JSONArray categoryJesonArry = response.getJSONArray("encrSendlist");
                                            if(categoryJesonArry !=null) {
                                                for (int i = 0; i < categoryJesonArry.length(); i++) {
                                                    JSONObject object = categoryJesonArry.getJSONObject(i);
                                                    DataModel data = new DataModel();
                                                    data.setName(object.getString("name"));
                                                    data.setEncription_type(object.getString("encription_type"));

                                                    //  encription_type=object.getString("encription_type");

                                                    // Log.d("cat_id","================"+cat_id);
                                                    arrayList.add(data);
                                                }
                                                adapter = new UserListRecyclerViewAdapter(getActivity(), arrayList, recyclerView,FragmentUserList.this);
                                                recyclerView.setAdapter(adapter);
                                            }

                                        }

                                        else{

                                            mTvnote.setVisibility(View.VISIBLE);


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

                }else{
                    arrayList.clear();
                    validateAllData();
                }
            }
        });
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2.isChecked()){
                    cb1.setChecked(false);
                    cb3.setChecked(false);
                    arrayList.clear();
                    Log.d("user_id","---------------"+user_id);
                    Log.d("encription_type","---------------"+AppConstants.ENCRYPTION_TWO);
                    mStateview.showLoading();
                    AndroidNetworking.post(get_user_sended_encription_list)
                            .addBodyParameter("user_id",user_id)
                            .addBodyParameter("encription_type",AppConstants.ENCRYPTION_TWO)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Response", "-----------" + response);
                                    try {
                                        mStateview.showContent();
                                        if(response.getString("success").equalsIgnoreCase("1")){
                                            mTvnote.setVisibility(View.GONE);
                                            arrayList=new ArrayList<>();
                                            JSONArray categoryJesonArry = response.getJSONArray("encrSendlist");
                                            if(categoryJesonArry !=null) {
                                                for (int i = 0; i < categoryJesonArry.length(); i++) {
                                                    JSONObject object = categoryJesonArry.getJSONObject(i);
                                                    DataModel data = new DataModel();
                                                    data.setName(object.getString("name"));
                                                    data.setEncription_type(object.getString("encription_type"));

                                                    //  encription_type=object.getString("encription_type");

                                                    // Log.d("cat_id","================"+cat_id);
                                                    arrayList.add(data);
                                                }
                                                adapter = new UserListRecyclerViewAdapter(getActivity(), arrayList, recyclerView,FragmentUserList.this);
                                                recyclerView.setAdapter(adapter);
                                            }

                                        }else{
                                            mTvnote.setVisibility(View.VISIBLE);


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

                }else{
                    arrayList.clear();
                    validateAllData();
                }
            }
        });
        cb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb3.isChecked()){
                    cb1.setChecked(false);
                    cb2.setChecked(false);
                    arrayList.clear();
                    Log.d("user_id","---------------"+user_id);
                    Log.d("encription_type","---------------"+AppConstants.ENCRYPTION_THREE);
                    mStateview.showLoading();
                    AndroidNetworking.post(get_user_sended_encription_list)
                            .addBodyParameter("user_id",user_id)
                            .addBodyParameter("encription_type",AppConstants.ENCRYPTION_THREE)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("Response", "-----------" + response);
                                    try {
                                        mStateview.showContent();
                                        if(response.getString("success").equalsIgnoreCase("1")){
                                            mTvnote.setVisibility(View.GONE);
                                            arrayList=new ArrayList<>();
                                            JSONArray categoryJesonArry = response.getJSONArray("encrSendlist");
                                            if(categoryJesonArry !=null) {
                                                for (int i = 0; i < categoryJesonArry.length(); i++) {
                                                    JSONObject object = categoryJesonArry.getJSONObject(i);
                                                    DataModel data = new DataModel();
                                                    data.setName(object.getString("name"));
                                                    data.setEncription_type(object.getString("encription_type"));

                                                    //  encription_type=object.getString("encription_type");

                                                    // Log.d("cat_id","================"+cat_id);
                                                    arrayList.add(data);
                                                }
                                                adapter = new UserListRecyclerViewAdapter(getActivity(), arrayList, recyclerView,FragmentUserList.this);
                                                recyclerView.setAdapter(adapter);
                                            }

                                        }else{
                                            Log.d("msg","============================");
                                            mTvnote.setVisibility(View.VISIBLE);


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

                }else{
                    arrayList.clear();
                    validateAllData();
                }
            }
        });

        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);;
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(v,-40,20);
    }
}
