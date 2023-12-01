package com.rbhagat.cleanguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class signUp extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextView signInBtn,signUpBtn;
    EditText userName,userEmail,userPassword,cPassword;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    int reward=0;
    String imageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(true);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        signUpBtn=findViewById(R.id.signUpBtn);
        signInBtn=findViewById(R.id.signIn);

        userName=findViewById(R.id.userName);
        userEmail=findViewById(R.id.userEmail);
        userPassword=findViewById(R.id.userPassword);
        cPassword=findViewById(R.id.cPassword);




        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp.this,login.class);
                startActivity(intent);
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                progressDialog.show();

                String pass=userPassword.getText().toString();
                int len= pass.length();

                if (userName.getText().toString().isEmpty() || userEmail.getText().toString().isEmpty() ||  userPassword.getText().toString().isEmpty()) {
                        progressDialog.dismiss();
                        Toast.makeText(signUp.this, "Please Enter all Data", Toast.LENGTH_SHORT).show();

                }else if(!pass.equals(cPassword.getText().toString())) {

                    progressDialog.dismiss();
                    Toast.makeText(signUp.this, "Password doesn't match", Toast.LENGTH_SHORT).show();

                }
                else if(len<6)
                {
                    progressDialog.dismiss();
                    Toast.makeText(signUp.this, "password must be greater than 6 character", Toast.LENGTH_SHORT).show();

                }

                else {



                    auth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                imageURI="https://firebasestorage.googleapis.com/v0/b/clean-guard-274b4.appspot.com/o/profile.png?alt=media&token=4f1553aa-c71d-460a-b54d-9a1e137c3a72";
                                DatabaseReference reference = database.getReference().child("users").child(auth.getUid());
                                Users users=new Users(userName.getText().toString(),userPassword.getText().toString(),userEmail.getText().toString(), auth.getUid(),imageURI,reward);

                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(signUp.this, MainActivity.class);
                                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }

                                    }
                                });





                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(signUp.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


                }


            }
        });


    }
}