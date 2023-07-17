package com.example.edynamixapprenticeship.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edynamixapprenticeship.R;

import java.util.Random;
import java.util.Scanner;

public class Register extends AppCompatActivity {

    //public static Scanner sc = new Scanner(System.in);
    public static Random rnd = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView login_redirect = findViewById(R.id.login_redirect);
        String login_redirect_value = login_redirect.getText().toString();

        SpannableString ss = new SpannableString(login_redirect_value);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {

                Toast.makeText(Register.this, "LOGIN", Toast.LENGTH_LONG).show();
//                Intent register_info = new Intent(this, Log.class);
//                startActivity(register_info);

            }
        };

        ss.setSpan(clickableSpan, 26,31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login_redirect.setText(ss);
        login_redirect.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void register (View v) {

        Button button_register = (Button) v;
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        String email_value = email.getText().toString();
        String password_value = password.getText().toString();

        Log.d("Test_MK", "Email:" + email_value);
        Log.d("Test_MK", "Password:" + password_value);

        String alert_message = "";

        if(valid_email(email_value) && valid_password(password_value)){

            alert_message = "Successfully registered user !";
            Toast.makeText(this, alert_message, Toast.LENGTH_LONG).show();

//            Intent register_info = new Intent(this, Log.class);
//            startActivity(register_info);

        } else {

            alert_message = "Please enter a valid email address!";
            Toast.makeText(this, alert_message, Toast.LENGTH_LONG).show();

        }

    }

    public  boolean valid_email (String email) {

        return  true;
    }

    public  boolean valid_password (String password) {

        int password_length = password.length();

        if(password_length >= 12) {

            return true;

        }else {

            return false;

        }

    }

}