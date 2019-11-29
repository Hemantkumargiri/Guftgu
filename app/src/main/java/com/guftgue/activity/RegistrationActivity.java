package com.guftgue.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.others.MyStateview;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import static com.guftgue.others.ApiServices.send_otp;

public class RegistrationActivity extends AppCompatActivity implements ProgressClickListener {

    private Toolbar mToolBar;
    private EditText medt_uName,medt_email,medt_mobilenumber,medtpassword,medtconfirmpassword;
    private Button mbtn_submit;
    private MyStateview mStateview;
    private ScrollView scrollbar;
    private String deviceToken;
    private TextView mTxtToolbarTitle, mTextLoginReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        medt_uName=findViewById(R.id.etNameReg);
        medt_email=findViewById(R.id.etEmailReg);
        medt_mobilenumber=findViewById(R.id.etMobileReg);
        medtpassword=findViewById(R.id.etPasswordReg);
        medtconfirmpassword=findViewById(R.id.etConfirmPassReg);
        mbtn_submit=findViewById(R.id.btnSignUpReg);
        scrollbar=findViewById(R.id.scrollRegPage);
        mTextLoginReg=findViewById(R.id.textLoginReg);
        mTxtToolbarTitle=findViewById(R.id.toolbarTitle);
        mStateview = new MyStateview(this, scrollbar);
        prepareToolbar();
        setListener();


    }

    private void setListener() {
        mTextLoginReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
        mbtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOnline()){
                    if(validation()){
                        validate();
                    }
                }
            }
        });
    }

    private void prepareToolbar() {
        setSupportActionBar(mToolBar); // Setting/replace toolbar as the ActionBar
        mTxtToolbarTitle.setText("SIGN UP");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void validate(){
        mStateview.showLoading();

        Log.d("mobile","-------------------"+medt_mobilenumber.getText().toString().trim());
        Log.d("password","-------------------"+medtpassword.getText().toString().trim());
        Log.d("first_name","-------------------"+medt_uName.getText().toString().trim());
        Log.d("email","-------------------"+medt_email.getText().toString().trim());
        Log.d("deviceToken","-------------------"+deviceToken);

        String SenderName=medt_uName.getText().toString().trim();
        Log.d("SenderName "," " +SenderName);
        AndroidNetworking.post(send_otp)
                .addBodyParameter("mobile", medt_mobilenumber.getText().toString().trim())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response","-------------"+response);

                        try {
                            mStateview.showContent();
                            if(response.getString("code").equalsIgnoreCase("200")){
                                String message= response.getString("message");
                                String otp= response.getString("otp");
                                Log.d("otp","--------------------------"+otp);
                                startActivity(new Intent(RegistrationActivity.this,VerifyActivity.class)
                                .putExtra("mobile",medt_mobilenumber.getText().toString().trim())
                                .putExtra("password",medtpassword.getText().toString().trim())
                                .putExtra("email",medt_email.getText().toString().trim())
                                .putExtra("first_name",medt_uName.getText().toString().trim()));
                                overridePendingTransition(0,0);
                                finish();

                            }else{

                                String message= response.getString("message");

                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(RegistrationActivity.this, R.color.colorPrimary));
                                TextView textView1 = (TextView) snakView.findViewById(R.id.snackbar_action);
                                textView1.setTextColor(Color.BLACK);
                                textView1.setTypeface(textView1.getTypeface(),Typeface.BOLD);
                                textView1.setTextSize(16);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                                textView.setTextColor(Color.WHITE);
                                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                                textView.setTextSize( 16 );
                                snackbar.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mStateview.showRetry();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        mStateview.showRetry();
                        Log.d("error","------------------"+anError.getErrorDetail());
                    }
                });

    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.relativeLayoutReg), "No Internet connection", Snackbar.LENGTH_SHORT);
                 /*   .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isOnline()) {
                                validateAllData();
                            }
                        }
                    });*/
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            TextView textView1 = (TextView) snakView.findViewById(R.id.snackbar_action);
            textView1.setTextColor(Color.BLACK);
            textView1.setTypeface(textView1.getTypeface(),Typeface.BOLD);
            textView1.setTextSize(16);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextSize( 16 );
            snackbar.show();
            //Toast.makeText(FragmentDashBoard.this, "No Internet connection!", Toast.LENGTH_LONG).show();
            //  mProgressBarDialog.showLoading();
            return false;
        }
        return true;
    }

    public void snackbar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayoutReg), message, Snackbar.LENGTH_SHORT);
        View snakView = snackbar.getView();
        snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        TextView textView1 = (TextView) snakView.findViewById(R.id.snackbar_action);
        textView1.setTextColor(Color.BLACK);
        textView1.setTypeface(textView1.getTypeface(),Typeface.BOLD);
        textView1.setTextSize(16);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextSize( 16 );
        snackbar.show();
    }
    public boolean validation() {
    if(medt_uName.getText().toString().trim().length()==0){
        snackbar("Please Enter User Name");
        return false;
    }else if(!isValidEmailId(medt_email.getText().toString().trim())){
        snackbar("Please Enter valid Email");
        return false;
    }else if(medt_mobilenumber.getText().toString().trim().length()==0){
        snackbar("Please Enter Valid Mobile Number");
        return false;
    }else if(medtpassword.getText().toString().trim().length()==0){
        snackbar("Please Enter Password");
        return false;
    }else if(medtpassword.getText().toString().trim().length()<6){
        snackbar("Password length must be minimum of 6 chars");
        return false;
    }else if(medtconfirmpassword.getText().toString().trim().length()==0){
        snackbar("Please Enter Confirm Password");
        return false;
    }else if(medtconfirmpassword.getText().toString().trim().length()<6){
        snackbar("Password length must be minimum of 6 chars");
        return false;
    }else if(!(medtpassword.getText().toString().trim()).equals(medtconfirmpassword.getText().toString().trim())){
        snackbar("Password Should be Same");
        return false;
        }
    return true;
    }

    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    @Override
    public void onRetryClick() {

    }
}
