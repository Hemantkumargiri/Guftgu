package com.guftgue.others;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guftgue.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodFactory {
    public static boolean isSelected;
    public static String setStatus;



    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        try {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }
    public static Uri getImageUri(Context applicationContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }



    public static int getPosInList(String mCountryName, List<String> myList) {
        if (myList != null || !myList.isEmpty()) {
            for (int i = 0; i < myList.size(); ++i) {
                if (myList.get(i).equalsIgnoreCase(mCountryName)) {
                    return i;
                }
            }
        }
        return -1;

    }



    public static void show(Context ctx, View view, String msg) {

        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorPrimaryDark));
        TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setTypeface(Typeface.SANS_SERIF);
        snackbar.show();
    }

    public static void closeApp(final Activity activity, View view) {

        Snackbar snackbar = Snackbar.make(view, "Press exit to close the App.", Snackbar.LENGTH_LONG).setAction("exit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.finish();
                activity.startActivity(intent);
            }
        });
        View snakView = snackbar.getView();
        snakView.setBackgroundColor(ContextCompat.getColor(activity, R.color.black));
        snakView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        tv.setTextColor(ContextCompat.getColor(activity, R.color.black));
        tv.setTextSize(16);
        tv.setAllCaps(false);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        snackbar.show();
    }

    public static int getRandomColor() {

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.parseColor("#47AD5E"));
        colors.add(Color.parseColor("#5D8AAB"));
        colors.add(Color.parseColor("#c21010"));
        colors.add(Color.parseColor("#f4b241"));

        return colors.get(new Random().nextInt(colors.size()));
    }




    public static String getStringFromList(ArrayList<String> mArrayList) {
        String tempString = mArrayList.toString().replaceAll("\\[|\\]", "").replaceAll(" ", "").replace(",,", ",");
        String res = "";
        if (!TextUtils.isEmpty(tempString)) {
            if (tempString.charAt(tempString.length() - 1) == ',') {
                tempString = tempString.substring(0, tempString.length() - 1);
            }
            return tempString;
        }
        return "";
    }

    // Login & signup   Validation
    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPasswdAlphaNumeric(String password) {
        boolean isValid = false;
        String expression = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,17}$";
        String expression1 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
        CharSequence inPasswd = password;

        Pattern pattern = Pattern.compile(expression1, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inPasswd);
        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;


    }


    public static boolean isPasswdValid(String password) {
        boolean isValid = false;
        String expression = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        String expression1 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}";
        CharSequence inPasswd = password;

        Pattern pattern = Pattern.compile(expression1, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inPasswd);
        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;


    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isValidPass(CharSequence target) {

        if (TextUtils.isEmpty(target)) {
            return false;
        } else if (target.length() < 6) {
            return false;
        } else {
            return true;
        }
    }


    public static final boolean isPasswdord8ChargsLong(String passwd) {
        if (TextUtils.isEmpty(passwd)) {
            return false;
        } else if (passwd.length() < 8) {
            return false;
        } else {
            return true;
        }
    }

    public final static boolean isValidTelephone(String target) {

        if (TextUtils.isEmpty(target)) {
            return false;
        } else if (target.length() < 10) {
            return false;
        } else
            return true;

    }

    public static boolean validateName(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        } else if (!name.matches("[A-Z][a-zA-Z]*"))
            return false;
        else return true;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static String getTimeStamp(String date_time, String format) {

        DateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getDefault());
        Date datee;
        try {
            datee = formatter.parse(date_time);
            Log.e("", "Today is  : " + datee.getTime());
            String timestamp = "" + datee.getTime();
            if (timestamp.length() > 10) {
                timestamp = "" + Long.parseLong(timestamp) / 1000L;
            }
            return timestamp;
        } catch (ParseException pe) {
            pe.printStackTrace();
            return "";
        }
    }

    public static void closeKeyboard(final Context context, final View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean checkIfMobile(String value) {
        try {
            Long.parseLong(value);
        } catch (Exception ep) {
            ep.printStackTrace();


            return false;
        }

        return false;
    }

    public static void showAlert(Context mActivity, String finishMessage) {

        AlertDialog alert = new AlertDialog.Builder(mActivity)
                .setMessage(finishMessage)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alert.show();
        // Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        // nbutton.setTextColor(mActivity.getResources().getColor(R.color.black));
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(mActivity.getResources().getColor(R.color.colorPrimaryDark));

    }



    public static float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float) level / (float) scale) * 100.0f;
    }


    public static void disableEnableControls(boolean enable, ViewGroup vg) {

        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
            if (enable) {
                if (child instanceof EditText) {
                    ((EditText) child).setSelection(((EditText) child).getText().toString().length());
                }
            }
        }
    }





    public static void importAnyFile(Fragment context, int PICKFILE_REQUEST_CODE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      /*  intent.setType("file/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
       */
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            context.startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    PICKFILE_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog

        }

    }

    public static Bitmap createScaledBitmap(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, 0, 0);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return scaledBitmap;

    }

    public static File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 20;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=100;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
