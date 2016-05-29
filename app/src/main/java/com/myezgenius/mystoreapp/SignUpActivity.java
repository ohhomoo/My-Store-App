package com.myezgenius.mystoreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    //Explicit
    private EditText nameEditText, surnameEditText,
            userEditText, passwordEditText;
    private String nameString, surnameString, userString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Bind Widget
        nameEditText = (EditText) findViewById(R.id.editText);
        surnameEditText = (EditText) findViewById(R.id.editText2);
        userEditText = (EditText) findViewById(R.id.editText3);
        passwordEditText = (EditText)findViewById(R.id.editText4);


    }   // Main Method

    public void clickSignUpSign(View view) {

        nameString = nameEditText.getText().toString().trim();
        surnameString = surnameEditText.getText().toString().trim();
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check Space
        if (checkSpace()) {
            //Have Space
            Toast.makeText(this, "กรุณากรอกให้ครบ ทุกช่องคะ", Toast.LENGTH_SHORT).show();
        } else {
            //No Space

        }


    }   // clickSign

    private boolean checkSpace() {

        boolean bolResult = true;

        bolResult = nameString.equals("") || surnameString.equals("") ||
                userString.equals("") || passwordString.equals("");

        return bolResult;
    }

}   // Main Class
