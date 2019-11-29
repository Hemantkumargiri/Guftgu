package com.guftgue.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.guftgue.R;
import com.guftgue.fragment.EncryptDecryptFragment;
import com.guftgue.fragment.FragmentEncryptions;
import com.guftgue.fragment.FragmentMyEncription;
import com.guftgue.fragment.FragmentMyProfile;
import com.guftgue.fragment.HelpFragment;
import com.guftgue.fragment.MyEncryptionFragment;
import com.guftgue.fragment.ProfileFragment;
import com.guftgue.helper.AESHelper;
import com.guftgue.helper.Const;
import com.guftgue.helper.PermissionsUtil;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class EncryptDecryptDashboardActivity extends AppCompatActivity {

    private static final String TAG = EncryptDecryptDashboardActivity.class.getSimpleName();
    private TextView mTxtToolbarTitle;
    private Toolbar mToolBar;
    private ImageView mProfileImage;
    private Context mContext;
    private BottomNavigationView mNavigation;
    private Dialog dialog;
    //private View mLayoutTxtEncrypt, mLayoutImgEncrypt;
    public static EncryptDecryptDashboardActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_decrypt_dashboard);
        instance=this;

        PermissionsUtil.askPermissions(EncryptDecryptDashboardActivity.this,
                PermissionsUtil.CAMERA, PermissionsUtil.STORAGE,
                new PermissionsUtil.PermissionListener() {
                    @Override
                    public void onPermissionResult(boolean isGranted) {
                        if (isGranted) {

                        }
                    }
                });
        try {
            String password = "password";
            String message = "hello world";
            String encryptedMsg="";
            try {
                 encryptedMsg = AESCrypt.encrypt(password, message);
            }catch (GeneralSecurityException e){
                //handle error
            }

            String password1 = "password";
           // String encryptedMsg = "2B22cS3UC5s35WBihLBo8w==";
            try {
                String messageAfterDecrypt = AESCrypt.decrypt(password1, encryptedMsg);
                String s="";
            }catch (GeneralSecurityException e){
                //handle error - could be due to incorrect password or tampered encryptedMsg
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        init();
        prepareToolbar();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, new EncryptDecryptFragment(), "").commit();
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setListener();

    }

    private void setListener() {
        mProfileImage = findViewById(R.id.toolbar_profile_image);
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMyProfile fragment= new FragmentMyProfile();
                profileFragment(fragment);
            }
        });
        /*mLayoutTxtEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextEncryptDecryptFragment fragment = new TextEncryptDecryptFragment();
                TextEncryDecryFragment(fragment);
            }
        });*/
        /*BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(EncryptDecryptDashboardActivity.this, "You have click on Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_user_list:
                        Toast.makeText(EncryptDecryptDashboardActivity.this, "You have click on User List", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_my_encryption:
                        Toast.makeText(EncryptDecryptDashboardActivity.this, "You have click on My Encryption", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_help:
                        Toast.makeText(EncryptDecryptDashboardActivity.this, "You have click on Help", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_exit:
                        Toast.makeText(EncryptDecryptDashboardActivity.this, "You have click on Exit", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });*/
    }

    private void TextEncryDecryFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        mTxtToolbarTitle.setText("TEXT ENCRYPT / DECRYPT");
        transaction.commit();
    }

    private void profileFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        mTxtToolbarTitle.setText("PROFILE VIEW");
        transaction.commit();
    }

    private void prepareToolbar() {
        setSupportActionBar(mToolBar); // Setting/replace toolbar as the ActionBar
        mTxtToolbarTitle.setText("ENCRYPT / DECRYPT");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void init() {
        mToolBar = findViewById(R.id.main_activity_toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbarTitle);
        mNavigation = findViewById(R.id.bottom_navigation);
        mProfileImage = findViewById(R.id.toolbar_profile_image);
        //mLayoutTxtEncrypt = findViewById(R.id.relativeLayoutTextEncrypt);
        //mLayoutImgEncrypt = findViewById(R.id.relativeLayoutImageEncrypt);
        mProfileImage.setVisibility(View.VISIBLE);
        mTxtToolbarTitle.setVisibility(View.VISIBLE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    mTxtToolbarTitle.setText("ENCRYPT / DECRYPT");
                    mProfileImage.setVisibility(View.VISIBLE);
                    changeFragment(new EncryptDecryptFragment(), Const.Tag.TEXT_IMAGE_DASHBOARD, false);

                    return true;
                case R.id.action_user_list:
                    mTxtToolbarTitle.setText("ENCRYPTIONS");
                    mProfileImage.setVisibility(View.GONE);
                    changeFragment(new FragmentEncryptions(), Const.Tag.USER_LIST, false);

                    return true;
                case R.id.action_my_encryption:
                    mTxtToolbarTitle.setText("MY ENCRYPTIONS");
                    mProfileImage.setVisibility(View.GONE);
                    changeFragment(new FragmentMyEncription(), Const.Tag.MY_ENCRYPTION, false);

                    return true;
                case R.id.action_help:
                    mTxtToolbarTitle.setText("HELP");
                    mProfileImage.setVisibility(View.GONE);
                    changeFragment(new HelpFragment(), Const.Tag.HELP, false);

                    return true;

                case R.id.toolbar_profile_image:
                    mTxtToolbarTitle.setText("PROFILE");
                    mProfileImage.setVisibility(View.GONE);
                    changeFragment(new FragmentMyProfile(), Const.Tag.PROFILE_VIEW, false);

                    return true;

                    case R.id.action_exit:
                        showBottomDialog();
                    return true;
            }
            return false;
        }
    };

    private void showBottomDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView1 = getLayoutInflater().inflate(R.layout.dialog_exit, null);
        TextView TvExit = mView1.findViewById(R.id.TvExit);
        TextView TvClose = mView1.findViewById(R.id.TvClose);
        TvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                finish();
            }
        });
        TvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBuilder.setView(mView1);
        dialog = mBuilder.create();
        dialog.show();
    }

    public void changeFragment(final Fragment fragmentToAdd, final String tag, final boolean addToBackStack)
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                int container_fragment = R.id.frame_container;
                // if (fragmentToAdd != null && !fragmentToAdd.isAdded())
                try
                {
                    Log.i(TAG,"onChangeFragment() method called ");

                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                    if (fragment == null && fragmentToAdd != null && tag != null) // if fragment is null it means it's not added
                    {

                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(container_fragment,fragmentToAdd,tag);
                        if (addToBackStack)
                            fragmentTransaction.addToBackStack(tag);
                        fragmentTransaction.commit();

                        Log.i(TAG,"fragment added successfully ");
                    }
           /* else
                Log.i(TAG,"fragment is already added");*/


                    if (fragment != null && fragmentToAdd != null && tag != null) // if fragment is not null it means it may be added
                    {
                        if (!fragment.isAdded())
                        {
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(container_fragment, fragmentToAdd, tag);
                            if (addToBackStack)
                                fragmentTransaction.addToBackStack(tag);

                            fragmentTransaction.commit();

                            Log.i(TAG, "fragment added successfully ");
                        }
                /*else
                Log.i(TAG,"fragment is already added");*/
                    }
            /*else
                Log.i(TAG,"fragment1 is null");*/

                }
                catch (Exception e)
                {
                    Log.e(TAG,"Error in onChangeFragment() method , "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            mDrawerLayout.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);

    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
