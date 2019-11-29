package com.guftgue.activity;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.others.AppConstants;
import com.guftgue.others.MyStateview;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.adapter.CreatedViewRVAdapter;
import com.guftgue.model.Model1;
import com.guftgue.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.guftgue.others.ApiServices.get_my_encryption_setting;

public class EncryptionViewActivity extends AppCompatActivity implements ProgressClickListener {

    private Toolbar mToolBar;
    private RecyclerView mRVCretedVIew;
    private ArrayList<Model1> arrayList = new ArrayList<>();
    private CreatedViewRVAdapter adapter;
    private String encryption_view;
    private MyStateview mStateview;
    public String user_id;
    private TextView mTvtoolbar,TvView;
    private String enlogic;
    private String encryption_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption_view);
        mStateview = new MyStateview(this, null);
        encryption_view= this.getIntent().getStringExtra(AppConstants.EN_VIEW);
        encryption_type= this.getIntent().getStringExtra("encryption_type");
        Log.d("encryption_view","-----------------------"+encryption_view);
        Log.d("encryption_type","-----------------------"+encryption_type);
        //mTvtoolbar=findViewById(R.id.Tvtoolbar);
        //mTvtoolbar.setText(encryption_view);
        SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id","");
        Log.d("user_id","----------------"+user_id);
        validateAllData();
        mToolBar = findViewById(R.id.main_activity_toolbar);
        mTvtoolbar = findViewById(R.id.toolbarTitle);
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRVCretedVIew = (RecyclerView) findViewById(R.id.RVCretedVIew);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void validateAllData() {
        mStateview.showLoading();
        Log.d("user_id1","----------------------"+user_id);
        Log.d("encryption_type","----------------------"+encryption_type);

        AndroidNetworking.post(get_my_encryption_setting)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("encription_type", encryption_type)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                String message= response.getString("message");
                   /*             Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(EncryptionViewActivity.this, R.color.colorPrimary));
                                TextView textView1 = (TextView) snakView.findViewById(R.id.snackbar_action);
                                textView1.setTextColor(Color.BLACK);
                                textView1.setTypeface(textView1.getTypeface(),Typeface.BOLD);
                                textView1.setTextSize(16);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                                textView.setTextSize( 16 );
                                snackbar.show();*/
                                JSONArray categoryJesonArry = response.getJSONArray("userencrSettingList");

                                for (int i = 0; i < categoryJesonArry.length(); i++) {
                                    JSONObject object = categoryJesonArry.getJSONObject(i);
                                   enlogic=object.getString("encryp_logic");
                                   String [] enlogin1 = enlogic.split(",");
                                    for(String asset: enlogin1){
                                       Model1 data = new Model1();
                                       data.setmTvCreatedView(asset);
                                       arrayList.add(data);
                                   }
                                }
                                mRVCretedVIew.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                                adapter = new CreatedViewRVAdapter(EncryptionViewActivity.this, arrayList, mRVCretedVIew);
                                mRVCretedVIew.setAdapter(adapter);
                                //}
                            }else{
                                mStateview.showContent();
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

    @Override
    public void onRetryClick() {

    }
}
