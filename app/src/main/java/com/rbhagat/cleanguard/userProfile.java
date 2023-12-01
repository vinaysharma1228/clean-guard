package com.rbhagat.cleanguard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class userProfile extends AppCompatActivity {

    ImageView backArrow,logOut,plusBtn;
    CircleImageView profile_img;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView fullName,pFullName,reward,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        backArrow=findViewById(R.id.back_arrow);
        logOut=findViewById(R.id.logOutBtn);
        plusBtn=findViewById(R.id.plusBtn);
        profile_img=findViewById(R.id.profile_img);
        fullName=findViewById(R.id.fullName);
        pFullName=findViewById(R.id.p_fullName);
        reward=findViewById(R.id.rewardPoint);
        email=findViewById(R.id.emailAddress);


        //  setting data for particular users

        database.getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {



                        Users users=snapshot.getValue(Users.class);   //may be getting error here
                        if(users==null)
                        {
                            Toast.makeText(userProfile.this, "cannot load data", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Picasso.get()
                                    .load(users.getProfileImage())
                                    .placeholder(R.drawable.profile)
                                    .into(profile_img);

                            fullName.setText(users.getuName());
                            pFullName.setText(users.getuName());
                            email.setText(users.getuEmail());

                            int rp=users.getRewardPoint();
                            String rewardPoint= String.valueOf(rp);

                            reward.setText(rewardPoint);

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



       //
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImagePicker.with(userProfile.this)
                        .crop()
                        .galleryOnly()

                        //Crop image(Optional), Check Customization for more option
                      //  .compress(1024)			//Final image size will be less than 1 MB(Optional)
                     //   .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(userProfile.this,MainActivity.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(userProfile.this,login.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if(data!=null)
        {

            Uri imageUri =data.getData();
            profile_img.setImageURI(imageUri);

            final StorageReference reference=storage.getReference().child("userProfileUploads")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profileImage").setValue(uri.toString());

                        }
                    });
                }
            });
        }
//        Uri uri= data.getData();
//        profile_img.setImageURI(uri);

    }
}