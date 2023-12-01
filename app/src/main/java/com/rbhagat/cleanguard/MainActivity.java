package com.rbhagat.cleanguard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    LottieAnimationView settingBtn;
    TextView submit, warningMessage;
    Uri imageUri;
    ImageView selectedPhotoPlace;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String imageURL;
    EditText desc;

    private boolean locationPrompted = false;

    private final static int REQUEST_CODE = 100;
    private final static int REQUEST_LOCATION_SETTINGS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);

        settingBtn = findViewById(R.id.setting);
        submit = findViewById(R.id.submitBtn);
        warningMessage = findViewById(R.id.ImageSameWarning);
        selectedPhotoPlace = findViewById(R.id.selectedImage);
        desc = findViewById(R.id.description);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, userProfile.class);
                startActivity(intent);
            }
        });


        selectedPhotoPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(MainActivity.this)
                        .crop(320, 300)
                        .cameraOnly()
                        //Crop image(Optional), Check Customization for more option
                        //   .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        //   .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String descriptionText = desc.getText().toString();

                if (imageUri != null && !descriptionText.isEmpty()) {

                      //  askPermission();
                        getLastLocation();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "images and Description is Required", Toast.LENGTH_SHORT).show();
                }


//                Intent intent=new Intent(MainActivity.this,thankYouPage.class);
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);

            }
        });
    }


    // result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if(data!=null)
        {
            progressDialog.show();
            imageUri =data.getData();
            selectedPhotoPlace.setImageURI(imageUri);

            final StorageReference reference2=storage.getReference().child("userClickImage").child("/"+(auth.getUid()));

            reference2.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        reference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                imageURL=uri.toString();
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();


                            }
                        });
                    }

                }
            });
        }
//        if (requestCode == REQUEST_LOCATION_SETTINGS) {
//            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            boolean isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            if (isLocationEnabled) {
//
//                progressDialog.show();
//
//                Handler handler = new Handler();
//                 // Define the code you want to execute after the delay
//                Runnable runnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        // Call your function here
//                        progressDialog.dismiss();
//                        Toast.makeText(MainActivity.this, "Location Turn On SuccessFully.😊", Toast.LENGTH_SHORT).show();
//                    }
//                };

               // Schedule the runnable to be executed after a delay
             //   handler.postDelayed(runnable, 1000); // 1000 milliseconds = 1 second




//            }
//            else {
//                Toast.makeText(this, "Please turn on Location", Toast.LENGTH_SHORT).show();
//            }
//        }


    }

    private void getLastLocation() {

     //   progressDialog.show();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            progressDialog.show();
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location!=null)
                            {
                                Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses= null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);


                                        String currentUserId = auth.getUid();
                                        String city = addresses.get(0).getLocality();
                                        String faddress = addresses.get(0).getAddressLine(0);
                                        String pincode = addresses.get(0).getPostalCode();
                                        double latitude = addresses.get(0).getLatitude();
                                        double longitude = addresses.get(0).getLongitude();

                                        DatabaseReference reference = database.getReference().child("raisedWasteImage").child(city).child(auth.getUid());
                                        AddressInformation information = new AddressInformation(currentUserId, imageURL, desc.getText().toString(), city, faddress, pincode, latitude, longitude);

                                        reference.setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(MainActivity.this, thankYouPage.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });


                                } catch (IOException e) {

                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                }



                            }
                            else {

                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Location was not found", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


        }
        else {
            askPermission();
        }

    }

//    private void askUserToEnableLocation() {
//
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Location services are disabled. Do you want to enable them?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivityForResult(intent, REQUEST_LOCATION_SETTINGS);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//
//
//
//    }



    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Required permission", Toast.LENGTH_SHORT).show();
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}