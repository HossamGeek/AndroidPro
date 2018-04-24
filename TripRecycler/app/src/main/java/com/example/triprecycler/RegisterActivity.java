package com.example.triprecycler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mail,pass;
    TextView Login;
    Button Register;
    Intent incomingIntent;
    Intent nextIntent;

    public  static  final  String Email_user ="Email";
    public  static  final String MY_PREFS_NAME="File";

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        incomingIntent = getIntent();

        mail =(EditText)findViewById(R.id.Email);
        pass = (EditText)findViewById(R.id.Pass);
        Login = (TextView)findViewById(R.id.Login);
        Register =(Button)findViewById(R.id.Register);
        firebaseAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == Login){
            incomingIntent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(incomingIntent);
        }

        if(v == Register){
            registerUser();
        }
    }

    private void registerUser() {
        final String email = mail.getText().toString().trim();
        final String password = pass.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(RegisterActivity.this,"please enter email",Toast.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(RegisterActivity.this,"please enter password",Toast.LENGTH_SHORT).show();

            return;
        }
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //profile
                    Toast.makeText(RegisterActivity.this,"Success Rigester",Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = getSharedPreferences(MY_PREFS_NAME, 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", email);
                    editor.putString("pass", password);
                    editor.commit();


                    nextIntent = new Intent(RegisterActivity.this,MainActivity.class);
                    finish();
                    startActivity(nextIntent);
                }else{
                    Toast.makeText(RegisterActivity.this,"Field Rigester",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
