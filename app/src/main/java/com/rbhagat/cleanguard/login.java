package com.rbhagat.cleanguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText userEmail,userPassword;
    TextView forgetPass,loginSignIn,loginSignUp;
    FirebaseAuth auth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();

        userEmail=findViewById(R.id.userEmailLogin);
        userPassword=findViewById(R.id.userPasswordLogin);
        forgetPass=findViewById(R.id.forgetPassword);
        loginSignIn=findViewById(R.id.loginSignInBtn);
        loginSignUp=findViewById(R.id.loginSignUpBtn);

        progressDialog=new ProgressDialog(this);

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);



        loginSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent intent =new Intent(login.this,signUp.class);
                startActivity(intent);

            }
        });

        loginSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if(userEmail.getText().toString().isEmpty() || userPassword.getText().toString().isEmpty())
                {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
                else {

                    auth.signInWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                Intent intent = new Intent(login.this,MainActivity.class);
                                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(login.this, "Wrong Email and Password", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });




                }
            }
        });


    }
}