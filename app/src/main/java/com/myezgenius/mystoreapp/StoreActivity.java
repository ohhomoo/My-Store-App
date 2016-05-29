package com.myezgenius.mystoreapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

public class StoreActivity extends AppCompatActivity {

    private ListView listView;
    private String[] nameStrings, descripStrings,
            image1Strings, image2Strings, image3Strings,
            image4Strings,image5Strings, apkStrings;
    private String urlJSON = "http://swiftcodingthai.com/Moo/get_store_moo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        listView = (ListView) findViewById(R.id.listView);

        synMySQLStore();


    }   //Main Method

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

            } catch (Exception e) {

                e.printStackTrace();
            }

        }   // onPost
    }   //Connected Class


    private void synMySQLStore() {

        ConnectedStore connectedStore = new ConnectedStore();
        connectedStore.execute();

    }


}   // Main Class
