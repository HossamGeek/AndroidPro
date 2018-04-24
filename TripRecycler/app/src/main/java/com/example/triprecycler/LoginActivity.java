package com.example.triprecycler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText name,pass;
    TextView Register;
    Button Login;
    Intent nextIntent;

    public  static  final  String Email_user ="Email";
    public  static  final String MY_PREFS_NAME="File";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener logined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        name =(EditText)findViewById(R.id.Email);
        pass = (EditText)findViewById(R.id.Pass);

        Register = (TextView)findViewById(R.id.Register);
        Login = (Button)findViewById(R.id.Login);


        SharedPreferences sharedPreferences =  getSharedPreferences(MY_PREFS_NAME, 0);

        if(sharedPreferences.contains("email")){
            finish();
            nextIntent = new Intent(LoginActivity.this,MainActivity.class);
            String Emailusr= sharedPreferences.getString("email", "");

            nextIntent.putExtra(Email_user,Emailusr);
            //Log.i(TAG, "email main : "+Emailusr);

            startActivity(nextIntent);

        }

        firebaseAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(this);
        Login.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v == Register){
            nextIntent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(nextIntent);
        }
        if(v == Login){
            userLogin();

        }
    }

    private void userLogin() {
        final String email = name.getText().toString().trim();
        final String password = pass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(LoginActivity.this, "please enter email", Toast.LENGTH_SHORT).show();

            return;

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "please enter password", Toast.LENGTH_SHORT).show();

            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information


                            Toast.makeText(LoginActivity.this, "Succc.",
                                    Toast.LENGTH_SHORT).show();

                            SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME, 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("email", email);
                            editor.putString("pass", password);
                            editor.commit();


                            nextIntent = new Intent(LoginActivity.this,MainActivity.class);

                            startActivity(nextIntent);


                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
}
