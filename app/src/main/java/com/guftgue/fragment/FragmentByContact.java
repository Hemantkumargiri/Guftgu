package com.guftgue.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.others.MyStateview;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.R;
import com.guftgue.adapter.UserReceivedRecyclerViewAdapter;
import com.guftgue.model.DataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

import static com.guftgue.others.ApiServices.get_Received_encription_key;

public class FragmentByContact extends Fragment implements ProgressClickListener, UserReceivedRecyclerViewAdapter.ItemListener {
    private RecyclerView recyclerView;
    private ArrayList<DataModel> arrayList = new ArrayList<>();
    private UserReceivedRecyclerViewAdapter adapter;
    private Toolbar toolbar;
    private MyStateview mStateview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mobile;
    private View view1;
    private TextView mTvnote;
    private View mview;
    private String encryption;
    private String Name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bycontacts, container, false);
        //Imgfilter.setVisibility(View.INVISIBLE);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        view1 = view.findViewById(R.id.view);
        mTvnote = view.findViewById(R.id.Tvnote);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        mobile = sharedPreferences.getString("mobile", "");

        Log.d("user_id", "----------------" + mobile);
        validateAllData();


        recyclerView = (RecyclerView) view.findViewById(R.id.RvbyContacts);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    private void validateAllData() {
        //tvUserlist.setEnabled(false);
        mStateview.showLoading();
        Log.d("mobile", "---------------------" + mobile);
        AndroidNetworking.post(get_Received_encription_key)
                .addBodyParameter("mobile", mobile)
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
                                    //tvUserlist.setEnabled(true);
                                    mTvnote.setVisibility(View.INVISIBLE);
                                    arrayList = new ArrayList<>();
                                    JSONArray categoryJesonArry = response.getJSONArray("encrKey");
                                    if (categoryJesonArry != null) {
                                        for (int i = 0; i < categoryJesonArry.length(); i++) {
                                            JSONObject object = categoryJesonArry.getJSONObject(i);
                                            DataModel data = new DataModel();
                                            data.setName(object.getString("first_name"));
                                            data.setEncription_type(object.getString("encription_type"));
                                            data.setImage(object.getString("image"));
                                            arrayList.add(data);
                                        }
                                        if (getActivity() != null) {
                                            adapter = new UserReceivedRecyclerViewAdapter(getActivity(), arrayList, recyclerView, FragmentByContact.this);
                                            recyclerView.setAdapter(adapter);
                                        }
                                    }

                                }
                            } else {

                                mTvnote.setVisibility(View.VISIBLE);
                                view1.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        mStateview.showContent();
                    }
                });

    }


    @Override
    public void onRetryClick() {

    }

    @Override
    public void onItemClick(String encryption_type,String name) {
        encryption=encryption_type;
        Name=name;
        Log.d("encription_type","------------------"+encryption);
        Log.d("Name","------------------"+Name);
        Bundle bundle=new Bundle();
        bundle.putString("main", "main");
        bundle.putString("encryption",encryption);
        EncryptDecryptFragment fragment = new EncryptDecryptFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).addToBackStack(null).commit();

    }
}
