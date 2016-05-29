package com.myezgenius.mystoreapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StoreActivity extends AppCompatActivity {

    private ListView listView;
    private String[] nameStrings, descripStrings,
            image1Strings, image2Strings, image3Strings,
            image4Strings,image5Strings, apkStrings;
    private String urlJSON = "http://swiftcodingthai.com/Moo/get_store_moo.php";
    private String urlAPK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        listView = (ListView) findViewById(R.id.listView);

        synMySQLStore();


    }   //Main Method

    class InstallTask extends AsyncTask<Void, Void, String> {
        ProgressDialog mProgressDialog;

        Context context;
        String url;

        public InstallTask(Context context, String url) {
            this.context = context;

            this.url = url;

        }

        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(context,
                    "Download", " Downloading in progress..");
        }

        private String downloadapk() {
            String result = "";
            try {
                URL url = new URL(this.url);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, "filename.apk");

                FileOutputStream fileOutput = new FileOutputStream(file);
                InputStream inputStream = urlConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();
                result = "done";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected String doInBackground(Void... params) {
            String result = downloadapk();
            return result;
        }

        protected void onPostExecute(String result) {
            if (result.equals("done")) {
                mProgressDialog.dismiss();
                installApk();
            } else {
                Toast.makeText(context, "Error while downloading",
                        Toast.LENGTH_LONG).show();

            }
        }

        private void installApk() {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(new File("/sdcard/filename.apk"));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }

    }

    public class ConnectedStore extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("StoreV2", "e ==> " + e.toString());
                return null;
            }

        }   //doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                Log.d("StoreV2", "s ==> " + s);
                JSONArray jsonArray = new JSONArray(s);

                nameStrings = new String[jsonArray.length()];
                descripStrings = new String[jsonArray.length()];
                image1Strings = new String[jsonArray.length()];
                image2Strings = new String[jsonArray.length()];
                image3Strings = new String[jsonArray.length()];
                image4Strings = new String[jsonArray.length()];
                image5Strings = new String[jsonArray.length()];
                apkStrings = new String[jsonArray.length()];



                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    nameStrings[i] = jsonObject.getString("NameApp");
                    descripStrings[i] = jsonObject.getString("Description");
                    image1Strings[i] = jsonObject.getString("Image1");
                    image2Strings[i] = jsonObject.getString("Image2");
                    image3Strings[i] = jsonObject.getString("Image3");
                    image4Strings[i] = jsonObject.getString("Image4");
                    image5Strings[i] = jsonObject.getString("Image5");
                    apkStrings[i] = jsonObject.getString("apk");

                }   //for

                MyAdapter myAdapter = new MyAdapter(StoreActivity.this,
                        nameStrings, descripStrings, image1Strings);
                listView.setAdapter(myAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        myIstallApp(nameStrings[i], apkStrings[i]);
                    }
                });

            } catch (Exception e) {

                e.printStackTrace();
            }

        }   // onPost
    }   //Connected Class

    private void myIstallApp(String nameString, String apkString) {
        urlAPK = apkString;
        Log.d("StoreV3", "urlAPK ==> " + urlAPK);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        InstallTask installTask = new InstallTask(StoreActivity.this, urlAPK);
        installTask.execute();

        Toast.makeText(this,"Install" + nameString + "Successful",
                Toast.LENGTH_SHORT).show();;

    }   // myInstall


    private void synMySQLStore() {

        ConnectedStore connectedStore = new ConnectedStore();
        connectedStore.execute();

    }


}   // Main Class
