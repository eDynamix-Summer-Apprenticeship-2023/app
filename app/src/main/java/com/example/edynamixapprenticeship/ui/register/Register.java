package com.example.edynamixapprenticeship.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Contacts;
import android.text.Highlights;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edynamixapprenticeship.R;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.*;
import io.realm.RealmList;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class Register extends AppCompatActivity {

    //public static Scanner sc = new Scanner(System.in);
    //public static Random rnd = new Random();

//    Realm realm;
//    String Appid = "edynamix-cqcwz";
//    App app;
    String email_value = "";
    String password_value = "";
    String confirm_password_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        realm = Realm.getDefaultInstance();
//        Realm.init(this);
//        app = new App(new AppConfiguration.Builder(Appid).build());

        TextView login_redirect = findViewById(R.id.login_redirect);
        String login_redirect_value = login_redirect.getText().toString();

        SpannableString ss = new SpannableString(login_redirect_value);

        ss.setSpan(clickableSpan, 26,31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login_redirect.setText(ss);
        login_redirect.setMovementMethod(LinkMovementMethod.getInstance());

    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View view) {

            Log.d("Test_MK", "Login");
            Toast.makeText(Register.this, "LOGIN", Toast.LENGTH_LONG).show();

//                Intent register_info = new Intent(this, Log.class);
//                startActivity(register_info);

        }
    };

    public void register (View v) {

        Button button_register = (Button) v;
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText confirm_password = findViewById(R.id.confirm_password);

        email_value = email.getText().toString();
        password_value = password.getText().toString();
        confirm_password_value = confirm_password.getText().toString();

        Log.d("Test_MK", "Email: " + email_value);
        Log.d("Test_MK", "Password: " + password_value);
        Log.d("Test_MK", "Confirm Password: " + confirm_password_value);


        String alert_message = "";

        if(valid_email(email_value) && valid_password(password_value, confirm_password_value)){

            alert_message = "Successfully registered user !";
            Toast.makeText(this, alert_message, Toast.LENGTH_LONG).show();

//            Intent register_info = new Intent(this, Log.class);
//            startActivity(register_info);

        } else {

            if(valid_email(email_value)){

                normalize_text(email, email_value);
                alert_message = "Please enter a valid password!";

            } else {

                underline_text(email, email_value);
                alert_message = "Please enter a valid email address!";

            }

            Toast.makeText(this, alert_message, Toast.LENGTH_LONG).show();

        }

        Log.d("Test_MK", "---------------------------------------");

    }

    public boolean valid_email (String email) {

        String regex ="^(?<username>[\\w\\-\\.]+)@(?<mailServer>[\\w\\-.]+)\\.(?<domain>[\\w-]{2,4})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        boolean confirm = matcher.find();

        Log.d("Test_MK", "Email valid: " + confirm);

        return confirm;
    }

    public boolean valid_password (String password, String confirm_password) {

        int password_length = password.length();

        boolean confirm = password_length <= 12 && password_length >= 1 && password.equals(confirm_password);

        Log.d("Test_MK", "Password valid: " + confirm);

        return confirm;

    }

    public void underline_text(EditText editText, String text){

        SpannableString ss = new SpannableString(text);

        ss.setSpan(new UnderlineSpan(), 0, text.length(),0);
        editText.setText(ss);
        editText.setTextColor(Color.parseColor("#ff3300"));

    }

    public void normalize_text(EditText editText, String text){

        editText.setText(text);
        editText.setTextColor(Color.parseColor("#000000"));

    }

}