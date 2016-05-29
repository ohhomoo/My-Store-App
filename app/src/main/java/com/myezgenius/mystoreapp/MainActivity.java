package com.myezgenius.mystoreapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private MyManage myManage;
    private static final String urlJSON = "http://swiftcodingthai.com/Moo/get_user_moo.php";
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;
    private String[] loginStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        userEditText = (EditText) findViewById(R.id.editText5);
        passwordEditText = (EditText) findViewById(R.id.editText6);

        myManage = new MyManage(this);

        //myManage.addUser("name", "surname", "user", "pass");

        deleteAllSQLite();

        synMySQLtoSQLite();

    }   // Main Method

    @Override
    protected void onResume() {
        super.onResume();
        deleteAllSQLite();
        synMySQLtoSQLite();
    }

    public void clickSignIn(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (userString.equals("")||passwordString.equals("")) {
            Toast.makeText(this, "กรุณากรอกทุกช่องคะ", Toast.LENGTH_SHORT).show();

        } else {
            checkUserAnPass();
        }

    }   //clickSignIn

    private void checkUserAnPass() {

        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM userTABLE WHERE User = " + "'" + userString + "'", null);
            cursor.moveToFirst();

            loginStrings = new String[cursor.getColumnCount()];
            for (int i=0;i<cursor.getColumnCount();i++){

                loginStrings[i] = cursor.getString(i);

            }   //for
            cursor.close();

            //Check Password
            if (passwordString.equals(loginStrings[4])) {
                //Password True
                Toast.makeText(this,"ยินดีต้อนรับ "+ loginStrings[1]+"" + loginStrings[2],
                        Toast.LENGTH_SHORT).show();

            } else {
                //Password False
                Toast.makeText(this, "กรุณาพิมพ์ Password ใหม่ Password ผิด",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

            Toast.makeText(this,"ไม่มี"+userString+"ในฐานข้อมูลของเรา",
                    Toast.LENGTH_SHORT).show();
        }

    }   //checkUserAnPass

    // Create Inner Class
    public class ConnectedUserJSON extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("Store", "myerror = " + e.toString());
                return null;
            }

        }   // doInBack

        @Override
        protected void onPostExecute(String strJSON) {
            super.onPostExecute(strJSON);

            Log.d("Store", "strJSON ==> " + strJSON);

            try {

                JSONArray jsonArray = new JSONArray(strJSON);
                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String strName = jsonObject.getString(MyManage.column_name);
                    String strSurname = jsonObject.getString(MyManage.column_surname);
                    String strUser = jsonObject.getString(MyManage.column_user);
                    String strPassword = jsonObject.getString(MyManage.column_password);
                    myManage.addUser(strName, strSurname, strUser, strPassword);

                }   //for

            } catch (Exception e) {
                e.printStackTrace();
            }


        }   // onPost



    }   // Connected Class


    private void synMySQLtoSQLite() {
        ConnectedUserJSON connectedUserJSON = new ConnectedUserJSON();
        connectedUserJSON.execute();


    }

    private void deleteAllSQLite() {

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                MODE_PRIVATE, null);
        sqLiteDatabase.delete(MyManage.user_table, null, null);
    }

    public void clickSignUpMain(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }

}   // Main Class
