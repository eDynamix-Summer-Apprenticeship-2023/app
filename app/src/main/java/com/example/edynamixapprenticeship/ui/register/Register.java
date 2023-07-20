package com.example.edynamixapprenticeship.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
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

import org.bson.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class Register extends AppCompatActivity {

    //public static Scanner sc = new Scanner(System.in);
    //public static Random rnd = new Random();

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    String appID = "edynamix-cqcwz";
    App app;
    User user;
    MongoCollection<Document> mongoCollection;
    String emailValue = "";
    String passwordValue = "";
    String confirmPasswordValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        clickableText();

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(appID).build());

        Credentials credentials = Credentials.emailPassword("gamermartinbg@gmail.com", "MartK-0546");
        app.loginAsync(credentials, it->{});

        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("eDynamix_Test");
        mongoCollection = mongoDatabase.getCollection("Email/Password");

    }

    public void register (View v) {

        Button buttonRegister = (Button) v;
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText confirmPassword = findViewById(R.id.confirm_password);

        emailValue = email.getText().toString();
        passwordValue = password.getText().toString();
        confirmPasswordValue = confirmPassword.getText().toString();

        String alertMessage = "";

        if(validEmail(emailValue) && validPassword(passwordValue, confirmPasswordValue)){

            alertMessage = "Successfully registered user !";
            Toast.makeText(this, alertMessage, Toast.LENGTH_LONG).show();

            mongoCollection.insertOne(new Document("userID", user.getId()).append("Email/password", emailValue + " " + passwordValue)).getAsync(it->{});

            //loginRedirect();

        } else {

            if(validEmail(emailValue)){

                normalizeText(email, emailValue);
                alertMessage = "Please enter a valid password!";

            } else {

                underlineText(email, emailValue);
                alertMessage = "Please enter a valid email address!";

            }

            Toast.makeText(this, alertMessage, Toast.LENGTH_LONG).show();

        }

    }


    public void clickableText() {

        TextView loginRedirect = findViewById(R.id.login_redirect);
        String loginRedirectValue = loginRedirect.getText().toString();

        SpannableString spannableString = new SpannableString(loginRedirectValue);

        spannableString.setSpan(clickableSpan, 26,31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginRedirect.setText(spannableString);
        loginRedirect.setMovementMethod(LinkMovementMethod.getInstance());

    }

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View view) {

            //loginRedirect();

        }
    };

    public boolean validEmail(String email) {

        String regex ="^(?<username>[\\w\\-\\.]+)@(?<mailServer>[\\w\\-.]+)\\.(?<domain>[\\w-]{2,4})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        boolean confirm = matcher.find();

        return confirm;
    }

    public boolean validPassword(String password, String confirmPassword) {

        //Using A-Z a-z 0-9 - _ . , 8 - 12 char
        String regex = "^(?<password>[\\w\\d\\S\\-\\_\\.\\,]{8,12})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        boolean confirm = matcher.find() && password.equals(confirmPassword);

        return confirm;

    }

    public void underlineText(EditText editText, String text){

        SpannableString spannableString = new SpannableString(text);

        spannableString.setSpan(new UnderlineSpan(), 0, text.length(),0);
        editText.setText(spannableString);
        editText.setTextColor(Color.parseColor("#ff3300"));

    }

    public void normalizeText(EditText editText, String text){

        editText.setText(text);
        editText.setTextColor(Color.parseColor("#000000"));

    }

    public void loginRedirect(){

        Intent register_info = new Intent(this, Log.class);
        startActivity(register_info);

    }

}