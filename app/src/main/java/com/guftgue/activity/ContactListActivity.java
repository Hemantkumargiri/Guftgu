package com.guftgue.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.guftgue.model.EncryptModel;
import com.guftgue.others.AppConstants;
import com.guftgue.others.MyStateview;
import com.guftgue.others.MyToast;
import com.guftgue.others.ProgressClickListener;
import com.guftgue.adapter.RecyclerAdapter;
import com.guftgue.model.ContactListModel;
import com.guftgue.model.Contacts;
import com.guftgue.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.guftgue.others.ApiServices.Chek_my_encryption_setting;
import static com.guftgue.others.ApiServices.Remove_sended_encription;
import static com.guftgue.others.ApiServices.add_encrption;
import static com.guftgue.others.ApiServices.get_sended_mobile_encription_key;
import static com.guftgue.others.ApiServices.get_user_list;

public class ContactListActivity extends AppCompatActivity implements RecyclerAdapter.SelectItem, ProgressClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    private MyStateview mStateview;
    private Toolbar mToolBar;
    Cursor phones;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Switch mSwEncryption3;
    private Switch mSwEncryption2;
    private Switch mSwEncryption1;
    private String encription_type;
    String id;
    private String user_id;
    private Button mbtnAdd;
    private Button btnDelete;
    private String mobile_no;
    private String receiver_userId;
    private String name;
    private String Encryption_type;
    private ArrayList<String> enList = new ArrayList<>();
    private String Mobile_encrKey;
    //private Toolbar toolbar;
    private String mobile;
    private EditText edtContactSearch;
    ArrayList<Contacts> removed = new ArrayList<>();
    ArrayList<Contacts> contacts = new ArrayList<>();
    Activity mActivity;
    private ContactListModel contactListModel;
    private List<ContactListModel.UserList> contactList = new ArrayList<>();
    private ArrayList<String> encriptionNameList = new ArrayList<>();
    private String encription_name;
    private TextView TvEncryption1;
    private TextView TvEncryption2, mTxtToolbarTitle;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView TvEncryption3;
    private View v;
    private ArrayList<EncryptModel> modelList = new ArrayList<>();
    private Dialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        prepareToolbar();

        //toolbar = findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mStateview = new MyStateview(this, mSwipeRefreshLayout);
        edtContactSearch = findViewById(R.id.edtContactSearch);
        mToolBar = findViewById(R.id.main_activity_toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbarTitle);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");

        Log.d("user_id", "----------------" + user_id);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                ContactList();
            }
        });

        if (isOnline()) {
            ContactList();
        }
        recyclerView = (RecyclerView) findViewById(R.id.contacts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContactListActivity.this, LinearLayoutManager.VERTICAL, false));

        fabRecyclerViewCoodinator(recyclerView);
        edtContactSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = edtContactSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

        //showContacts();
    }

    private void prepareToolbar() {
        mToolBar = findViewById(R.id.main_activity_toolbar);
        mTxtToolbarTitle = findViewById(R.id.toolbarTitle);
        setSupportActionBar(mToolBar); // Setting/replace toolbar as the ActionBar
        mTxtToolbarTitle.setText("CONTACT LIST");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void ContactList() {
        mStateview.showLoading();
        AndroidNetworking.post(get_user_list)
                .addBodyParameter("user_id", user_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ContactList_response", "---------------------" + response);
                        Gson gson = new Gson();
                        contactListModel = gson.fromJson(response.toString(), ContactListModel.class);

                        if (contactListModel != null) {
                            if (contactListModel.getCode().equalsIgnoreCase("200")) {
                                mStateview.showContent();
                                contactList = contactListModel.getUserList();
                                adapter = new RecyclerAdapter(ContactListActivity.this, contactList, ContactListActivity.this);
                                recyclerView.setAdapter(adapter);
                            } else {
                                try {
                                    MyToast.display(getApplicationContext(), response.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mStateview.showContent();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }
/*
    private void showContacts() {
            // Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            } else {
                // Android version is lesser than 6.0 or the permission is already granted.
                phones = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                LoadContact loadContact = new LoadContact();
                loadContact.execute();
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                               @NonNull int[] grantResults) {

            if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showContacts();
                } else {
                    Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
                }
            }
        }*/

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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    public void onClick(String position, String position1, String receiver_userid) {
        name = position;
        mobile_no = position1;
        receiver_userId = receiver_userid;
        setEncryptionName();
    }

    @Override
    public void onRetryClick() {
    }


    private void setEncryptionName() {
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
                        modelList.clear();
                        Log.d("Response", "-----------" + response);
                        try {
                            mStateview.showContent();
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                if (response.getString("success").equalsIgnoreCase("1")) {
                                    CallDialog();
                                    JSONArray categoryJesonArry = response.getJSONArray("CheckSettingList");
                                    Log.d("categoryJesonArry", "================" + categoryJesonArry);
                                    for (int i = 0; i < categoryJesonArry.length(); i++) {
                                        JSONObject object = categoryJesonArry.getJSONObject(i);
                                        encription_name = object.getString("encription_name");
                                        encriptionNameList.add(object.getString("encription_name"));


                                        EncryptModel encryptModel = new EncryptModel();
                                        encryptModel.setEncryptName(object.getString("encription_name"));
                                        encryptModel.setEncypType(object.getString("encription_type"));
                                        encryptModel.setSetting_id(object.getString("setting_id"));

                                        modelList.add(encryptModel);
                                    }
                                    for (int i = 0; i < modelList.size(); i++) {

                                        TextView encryptTYextView = getTextView(modelList.get(i).getEncypType());
                                        if (encryptTYextView != null) {
                                            encryptTYextView.setText(modelList.get(i).getEncryptName());
                                            //  SettingId= modelList.get(i).getSetting_id();

                                        }
                                    }

                                }
                            } else {
                                mStateview.showContent();
                                MyToast.display(ContactListActivity.this, "Please Create Any Encryption First");
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

    public void CheckEncryption() {
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
                                    JSONArray categoryJesonArry = response.getJSONArray("CheckSettingList");
                                    Log.d("categoryJesonArry", "================" + categoryJesonArry);

                                    for (int i = 0; i < categoryJesonArry.length(); i++) {
                                        JSONObject object = categoryJesonArry.getJSONObject(i);
                                        Encryption_type = object.getString("encription_type");
                                        enList.add(object.getString("encription_type"));
                                        String list = String.valueOf(enList.add(object.getString("encription_type")));
                                        Log.d("list", "================" + list);
                                    }
                                }
                            } else {
                                mStateview.showContent();
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

    /*
        @SuppressLint("StaticFieldLeak")
        class LoadContact extends AsyncTask<Void, Void, Void> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }
                @Override
                protected Void doInBackground(Void... voids) {
                    // Get Contact list from Phone
                    String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.PHOTO_URI};
                    phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

              */
/*      if (phones != null) {
                    Log.e("count", "" + phones.getCount());
                    if (phones.getCount() > 0) {

                    }*//*

                String lastPhoneName = " ";
                HashSet<String> mobileNoSet = new HashSet<String>();

                if (phones.getCount() > 0) {

                    while (phones.moveToNext()) {
                        while (phones.moveToNext()) {
                            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String contactId = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                            String photoUri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                            Contacts selectUser = new Contacts();
                            if (!mobileNoSet.contains(phoneNumber)) {
                                selectUser.setName(name);
                                selectUser.setMobile(phoneNumber);
                                selectUser.setPhoto(photoUri);
                                selectUsers.add(selectUser);
                            }
                        }
                    }
                }
                else {
                    Log.e("Cursor close 1", "----------------");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // sortContacts();
                int count=selectUsers.size();

                for(int i=0;i<selectUsers.size();i++){
                    Contacts inviteFriendsProjo = selectUsers.get(i);
                    if(inviteFriendsProjo.getName().matches("\\d+(?:\\.\\d+)?")||inviteFriendsProjo.getName().trim().length()==0){
                        removed.add(inviteFriendsProjo);
                        Log.d("Removed Contact",new Gson().toJson(inviteFriendsProjo));
                    }else{
                        contacts.add(inviteFriendsProjo);
                    }
                }
                contacts.addAll(removed);
                selectUsers=contacts;
                adapter = new RecyclerAdapter(inflater, selectUsers, ContactListActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
            }
        }
*/
    public void validate() {
        mStateview.showLoading();
        Log.d("user_id", "--------------------------" + user_id);
        Log.d("mobile_no", "--------------------------" + mobile);
        Log.d("name", "--------------------------" + name);
        Log.d("encription_type", "--------------------------" + encription_type);
        Log.d("receiver_userId", "--------------------------" + receiver_userId);
        AndroidNetworking.post(add_encrption)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("mobile", mobile)
                .addBodyParameter("name", name)
                .addBodyParameter("encription_type", encription_type)
                .addBodyParameter("reciever_id", receiver_userId)
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
                                Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_SHORT);
                                View snakView = snackbar.getView();
                                snakView.setBackgroundColor(ContextCompat.getColor(ContactListActivity.this, R.color.colorPrimary));
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
                                startActivity(new Intent(ContactListActivity.this, EncryptDecryptDashboardActivity.class)
                                        .putExtra("tag", "tocontacts")
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                                finish();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mStateview.showRetry();
                        }
                    }


                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "----------------------------" + anError.getErrorDetail());
                        mStateview.showRetry();

                    }
                });
    }

    private void validateAllData() {
        Log.d("user_id", "--------------------" + user_id);
        Log.d("mobile_no", "--------------------" + mobile);
        mStateview.showLoading();
        AndroidNetworking.post(get_sended_mobile_encription_key)
                .addBodyParameter("user_id", user_id)
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
                                    String messsage = response.getString("message");
                                    Log.d("message", "---------------" + messsage);
                                    Mobile_encrKey = response.getString("Mobile_encrKey");
                                    Log.d("Mobile_encrKey", "--------------------------" + Mobile_encrKey);

                                    if (Mobile_encrKey.equals("encryption1")) {
                                        mSwEncryption1.setChecked(true);
                                        btnDelete.setVisibility(View.VISIBLE);
                                    }
                                    if (Mobile_encrKey.equals("encryption2")) {
                                        mSwEncryption2.setChecked(true);
                                        btnDelete.setVisibility(View.VISIBLE);
                                    }
                                    if (Mobile_encrKey.equals("encryption3")) {
                                        mSwEncryption3.setChecked(true);
                                        btnDelete.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                btnDelete.setVisibility(View.GONE);
                                mSwEncryption1.setChecked(false);
                                mSwEncryption2.setChecked(false);
                                mSwEncryption3.setChecked(false);

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

    public void fabRecyclerViewCoodinator(final RecyclerView view) {
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.canScrollVertically(1)) {
                }
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) {
                    hideKeyboard(view);
                }
            }
        });
    }

    public void onClick(View view) {
    }

    public void CallDialog() {
        mobile = mobile_no.replace("+91", "").replace(" ", "");
        Log.d("mobile", "======================" + mobile);
        Log.d("receiver_userId", "======================" + receiver_userId);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ContactListActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_select_encryption, null);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        mSwEncryption3 = mView.findViewById(R.id.SwEncryption3);
        mSwEncryption2 = mView.findViewById(R.id.SwEncryption2);
        mSwEncryption1 = mView.findViewById(R.id.SwEncryption1);
        TvEncryption1 = mView.findViewById(R.id.TvEncryption1);
        TvEncryption2 = mView.findViewById(R.id.TvEncryption2);
        TvEncryption3 = mView.findViewById(R.id.TvEncryption3);
        mbtnAdd = mView.findViewById(R.id.btnAdd);
        btnDelete = mView.findViewById(R.id.btnDelete);

        CheckEncryption();
        validateAllData();
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContactListActivity.this);
                alertDialogBuilder.setMessage("Are you sure, you want to delete Encryption ?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dialog.dismiss();
                                DeleteEncryption();

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alertDialog.dismiss();

                            }
                        });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        mbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwEncryption1.isChecked() || mSwEncryption2.isChecked() || mSwEncryption3.isChecked()) {
                    if (enList.contains(encription_type)) {
                        dialog.dismiss();
                        validate();
                    } else {
                        Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Please Create " + encription_type, Snackbar.LENGTH_LONG);
                        View snakView = snackbar.getView();
                        snakView.setBackgroundColor(ContextCompat.getColor(ContactListActivity.this, R.color.colorPrimary));
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
                } else {

                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), "Please Select Any Encryption first", Snackbar.LENGTH_LONG);
                    View snakView = snackbar.getView();
                    snakView.setBackgroundColor(ContextCompat.getColor(ContactListActivity.this, R.color.colorPrimary));
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
            }
        });

        mSwEncryption1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mSwEncryption1.isChecked()) {
                    encription_type = AppConstants.ENCRYPTION_ONE;
                    mSwEncryption2.setChecked(false);
                    mSwEncryption3.setChecked(false);
                }
            }
        });
        mSwEncryption2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mSwEncryption2.isChecked()) {
                    encription_type = AppConstants.ENCRYPTION_TWO;
                    mSwEncryption1.setChecked(false);
                    mSwEncryption3.setChecked(false);
                }


            }
        });
        mSwEncryption3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mSwEncryption3.isChecked()) {
                    encription_type = AppConstants.ENCRYPTION_THREE;
                    mSwEncryption2.setChecked(false);
                    mSwEncryption1.setChecked(false);
                }
            }
        });
        dialog.show();
    }

    public void DeleteEncryption() {
        mStateview.showLoading();
        Log.d("user_id", "----------------" + user_id);
        Log.d("receiver_userId", "----------------" + receiver_userId);
        AndroidNetworking.post(Remove_sended_encription)
                .addBodyParameter("user_id", user_id)
                .addBodyParameter("receiver_id", receiver_userId)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ContactList_response", "---------------------" + response);
                        Gson gson = new Gson();
                        contactListModel = gson.fromJson(response.toString(), ContactListModel.class);

                        try {
                            if (response.getString("code").equalsIgnoreCase("200")) {
                                mStateview.showContent();
                                MyToast.display(ContactListActivity.this, response.getString("message"));
                            } else {
                                mStateview.showContent();
                                MyToast.display(ContactListActivity.this, response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (contactListModel != null) {

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

    }

}



