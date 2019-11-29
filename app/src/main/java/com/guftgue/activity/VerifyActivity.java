package com.guftgue.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.guftgue.R;
import com.guftgue.others.MethodFactory;
import com.guftgue.others.MyStateview;
import com.guftgue.others.ProgressClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.guftgue.others.ApiServices.registration;
import static com.guftgue.others.ApiServices.send_otp;

public class VerifyActivity extends AppCompatActivity implements ProgressClickListener {
    private EditText edt_otpNumber;
    private Button btn_verifyOtp;
    private Toolbar toolbar;
    private String mobile;
    private VerifyActivity mActivity;
    private MyStateview mStateview;
    private TextView label;
    private String OTP;
    private TextView resendOTPTv;
    private String deviceToken;
    private String password;
    private String email;
    private String first_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mActivity = VerifyActivity.this;
        mStateview = new MyStateview(mActivity, null);
        mobile = this.getIntent().getStringExtra("mobile");
        password = this.getIntent().getStringExtra("password");
        email = this.getIntent().getStringExtra("email");
        first_name = this.getIntent().getStringExtra("first_name");
        //OTP = getIntent().getStringExtra("OTP");
        edt_otpNumber = (EditText) findViewById(R.id.edt_otpNumber);
        btn_verifyOtp = (Button) findViewById(R.id.btn_verifyOtp);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(v);

            }
        });
        label = findViewById(R.id.label);
        label.setText("Please Type the Verification Code send to +91" + mobile);
        resendOTPTv = findViewById(R.id.resendOTPTv);
        resendOTPTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callResendDOTPAPI();
            }
        });
    }

    public void validate() {
        mStateview.showLoading();
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("mobile", "-------------------" + mobile);
        Log.d("password", "-------------------" + password);
        Log.d("first_name", "-------------------" + first_name);
        Log.d("email", "-------------------" + email);
        Log.d("deviceToken", "-------------------" + deviceToken);
        Log.d("otp", "-------------------" + edt_otpNumber.getText().toString().trim());
        AndroidNetworking.post(registration)
                .addBodyParameter("mobile", mobile)
                .addBodyParameter("password", password)
                .addBodyParameter("first_name", first_name)
                .addBodyParameter("email", email)
                .addBodyParameter("device_token", deviceToken)
                .addBodyParameter("device_type", "Android")
                .addBodyParameter("otp", edt_otpNumber.getText().toString().trim())
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
                                startActivity(new Intent(VerifyActivity.this, LoginActivity.class));
                                overridePendingTransition(0, 0);
                                finish();
                            } else {
                                String message = response.getString("message");
                                mStateview.showContent();
                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(VerifyActivity.this, R.color.colorPrimary));
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

    private void validate(View v) {

        if (TextUtils.isEmpty(edt_otpNumber.getText().toString().trim())) {
            MethodFactory.show(mActivity, v, "Please enter OTP");
        } else if (MethodFactory.isOnline(mActivity)) {

            validate();

        } else {
            MethodFactory.show(mActivity, v, "No Internet Connection Avaliable");
        }

    }

    private void callResendDOTPAPI() {
        mStateview.showLoading();
        AndroidNetworking.post(send_otp)
                .addBodyParameter("mobile", mobile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mStateview.showContent();
                        Log.e("Response", "PostData-----------" + response);
                        try {
                            Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("AN_ERROR", "-----" + anError.getResponse().toString() + "--------" + anError.getErrorDetail());
                        mStateview.showRetry();

                    }


                });
    }



/*
    private void callVerifyOTPAPI() {
        mStateview.showLoading();

        AndroidNetworking.post(verify_otp)
                .addBodyParameter("otp", edt_otpNumber.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mStateview.showContent();
                        Log.e("Response", "PostData-----------" + response);
                        try {

                            if (response.has("code")) {
                                if (response.getString("code").equalsIgnoreCase("200")) {

                                    Toast.makeText(mActivity, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(mActivity,LoginActivity.class);
                                    intent.putExtra("mobile", mobile);
                                    intent.putExtra("OTP", OTP);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);


                                } else {
                                    MethodFactory.show(mActivity, btn_verifyOtp, "Invalid OTP");
                                }
                            } else {
                                MethodFactory.show(mActivity, btn_verifyOtp, "Something went wrong");

                            }


                        } catch (Exception e) {
                            mStateview.showRetry();
                            e.printStackTrace();
                            Log.d("Response", "PostData-----------" + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("AN_ERROR", "-----" + anError.getResponse().toString() + "--------" + anError.getErrorDetail());
                        mStateview.showRetry();

                    }


                });
    }

*/

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRetryClick() {
        mStateview.showContent();
    }
}
