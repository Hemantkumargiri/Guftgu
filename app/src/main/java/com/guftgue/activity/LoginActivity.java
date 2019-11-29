package com.guftgue.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.others.MyStateview;
import com.guftgue.others.MyToast;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import static com.guftgue.others.ApiServices.Forgotpassword;
import static com.guftgue.others.ApiServices.login;

public class LoginActivity extends AppCompatActivity implements ProgressClickListener {

    private TextView mTxtToolbarTitle, mTvregistration, mTvforgotPassword;
    private Toolbar mToolBar;
    private Button mbtn_register, mBtnForgotPass;
    private EditText medtPassword, medtmobile, mEtMobileForgotPassPopUp;
    private MyStateview mStateview;
    public String user_id;
    private String mobile,password;
    public SharedPreferences sharedPreferences;
    private ScrollView scrollView;
    private String deviceToken;
    private CheckBox saveLoginCheckBox;
    private Boolean saveLogin;
    public String UserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        prepareToolbar();
        setListener();
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        mStateview = new MyStateview(this, scrollView);
    /*    toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

    }

    private void setListener() {
        mTvforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_forget_password, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                mEtMobileForgotPassPopUp = mView.findViewById(R.id.etMobileForgotPassPopUp);
                mBtnForgotPass = mView.findViewById(R.id.btnForgotPass);
                mBtnForgotPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(v);
                        if (validation1()) {
                            dialog.dismiss();
                            SendPassword();
                        }
                    }
                });

                dialog.show();
            }
        });

        mbtn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (isOnline()) {
                    if (validation()) {
                        validate();
                    }
                }
            }
        });

        mTvregistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                overridePendingTransition(0, 0);
            }
        });
    }

    private void init() {
        medtmobile = findViewById(R.id.editTextMobile);
        medtPassword = findViewById(R.id.editTextPassword);
        scrollView = findViewById(R.id.scrollView);
        mTxtToolbarTitle = findViewById(R.id.toolbarTitle);
        mTvregistration = (TextView) findViewById(R.id.textSingUp);
        mTvforgotPassword = (TextView) findViewById(R.id.textForgotPassword);
        mbtn_register = findViewById(R.id.btnLogin);
        saveLoginCheckBox = (CheckBox)findViewById(R.id.checkBoxRemember);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        saveLogin = sharedPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            medtmobile.setText(sharedPreferences.getString("username", ""));
            medtPassword.setText(sharedPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
         /*  //Save the User Name
            String UserName=sharedPreferences.getString("UserName"," ");
           // Log.d(tag, "UserName: ",UserName);
            Log.d("", "UserName" + UserName);*/

        }

    }


    private void prepareToolbar() {
        setSupportActionBar(mToolBar); // Setting/replace toolbar as the ActionBar
        mTxtToolbarTitle.setText("SIGN IN");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void SendPassword() {
        mStateview.showLoading();
        Log.d("email", "-------------------" + mEtMobileForgotPassPopUp.getText().toString().trim());
        AndroidNetworking.post(Forgotpassword)
                .addBodyParameter("user_email", mEtMobileForgotPassPopUp.getText().toString().trim())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "-------------" + response);

                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                String message = response.getString("message");
                                MyToast.display(LoginActivity.this, message);
                         /*       Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
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
                            } else {
                                String message = response.getString("message");
                                MyToast.display(LoginActivity.this, message);
                          /*      Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
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


    public void validate() {
        mStateview.showLoading();

        Log.d("mobile", "-------------------" + medtmobile.getText().toString().trim());
        Log.d("password", "-------------------" + medtPassword.getText().toString().trim());
        Log.d("deviceToken", "-------------------" + deviceToken);

        AndroidNetworking.post(login)
                .addBodyParameter("mobile", medtmobile.getText().toString().trim())
                .addBodyParameter("password", medtPassword.getText().toString().trim())
                .addBodyParameter("device_token", deviceToken)
                .addBodyParameter("device_type", "Android")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "-------------" + response);

                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                String message = response.getString("message");
                                MyToast.display(LoginActivity.this, message);
                           /*     Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
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
                                user_id = response.getJSONObject("user_details").getString("user_id");
                                Log.d("user_id", "------------------" + user_id);

                                sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                                SharedPreferences.Editor ed = sharedPreferences.edit();

                                mobile = medtmobile.getText().toString();
                                password = medtPassword.getText().toString();

                                if (saveLoginCheckBox.isChecked()) {
                                    ed.putBoolean("saveLogin", true);
                                    ed.putString("username", mobile);
                                    ed.putString("password", password);
                                    ed.commit();
                                } else {
                                    ed.clear();
                                    ed.commit();
                                }

                                ed.putString("user_id", user_id);
                                ed.putString("mobile", medtmobile.getText().toString().trim());
                                ed.commit();
                                Log.d("user_id", "---------------" + user_id);
                                startActivity(new Intent(LoginActivity.this, EncryptDecryptDashboardActivity.class)
                                        .putExtra("main", "main2")
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                );
                                finish();
                            } else {
                                String message = response.getString("message");
                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary));
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

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {


            Snackbar snackbar = Snackbar
                    .make(getWindow().getDecorView().getRootView(), "No Internet connection", Snackbar.LENGTH_SHORT);
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

    public boolean validation() {
        if (medtmobile.getText().toString().trim().length() == 0) {
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Mobile Number", Snackbar.LENGTH_SHORT);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
            return false;
        } else if (medtPassword.getText().toString().trim().length() == 0) {
            Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Password", Snackbar.LENGTH_SHORT);
            View snakView = snackbar.getView();
            snakView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
            return false;
        }
        return true;
    }

    public boolean validation1() {
        if (mEtMobileForgotPassPopUp.getText().toString().trim().length() == 0) {
            MyToast.display(LoginActivity.this, "Please Enter Mobile Number");
      /*      Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Mobile Number", Snackbar.LENGTH_SHORT);
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
            snackbar.show();*/
            return false;
        } else if (mEtMobileForgotPassPopUp.getText().toString().trim().length() < 10) {
            MyToast.display(LoginActivity.this, "Please Enter Valid Mobile Number");
      /*      Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Please Enter Mobile Number", Snackbar.LENGTH_SHORT);
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
            snackbar.show();*/
            return false;
        }
        return true;
    }

    @Override
    public void onRetryClick() {

    }
}
