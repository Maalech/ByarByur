package com.byarbyur.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateBusinessActivity extends AppCompatActivity {
    private Button simpanBtn;
    ImageView imgB;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String currentPhotoPath;
    public Uri contentUri;

    private Button upBtn;
    private EditText namaB;
    private EditText alamatB, descB;
    private TextView urltv;
    private EditText hargaB;
    private EditText contactB;
    private FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseAuth fAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_business);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Seller");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        simpanBtn = findViewById(R.id.simpan_btn);
        upBtn = findViewById(R.id.up_btn);
        namaB = findViewById(R.id.nama_usaha);
        descB = findViewById(R.id.desc_usaha);
        alamatB = findViewById(R.id.alamat_usaha);
        hargaB = findViewById(R.id.harga_usaha);
        contactB = findViewById(R.id.contact_create);


        imgB = findViewById(R.id.img_create);

        Intent data = getIntent();
        final String id = data.getStringExtra("idU");

        if (id != null) {

            db.collection("seller").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        namaB.setText(documentSnapshot.getString("nama"));
                        alamatB.setText(documentSnapshot.getString("alamat"));
                        descB.setText(documentSnapshot.getString("desc"));
                        hargaB.setText(documentSnapshot.get("harga").toString());
                        contactB.setText(documentSnapshot.get("contact").toString());
                        final StorageReference Ref = storageReference.child(namaB.getText()  + "/Profile.jpg");
                        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).fit().placeholder(R.mipmap.ic_launcher)
                                        .centerCrop().into(imgB);

                            }
                        });
                    }
                }
            });

        }


        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }


        });

        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nama = namaB.getText().toString().trim();
                String desc = descB.getText().toString().trim();
                String alamat = alamatB.getText().toString().trim();
                String hrg = hargaB.getText().toString().trim();
                String contact = contactB.getText().toString().trim();


                if (!validateInputs(nama, desc, alamat, hrg, contact)) {
                    final ProgressDialog progressDialog = new ProgressDialog(CreateBusinessActivity.this);

                    progressDialog.setMessage("Creating...");
                    progressDialog.show();

                    Map<String, Object> file = new HashMap<>();
                    file.put("id", userId);
                    file.put("nama", namaB.getText().toString());
                    file.put("desc", descB.getText().toString());
                    file.put("contact", contactB.getText().toString());
                    file.put("harga", Integer.parseInt(String.valueOf(hargaB.getText())));
                    file.put("alamat", alamatB.getText().toString());
                    db.collection("seller").document(userId)
                            .set(file)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CreateBusinessActivity.this, "Usaha Ditambahkan", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateBusinessActivity.this, ManageBusinessActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                }
            }


            private boolean validateInputs(String nama, String desc, String alamat, String hrg, String contact) {
                if (nama.isEmpty()) {
                    namaB.setError("Nama Harus Diisi!");
                    namaB.requestFocus();
                    return true;
                }

                if (desc.isEmpty()) {
                    descB.setError("Alamat Harus Diisi!");
                    descB.requestFocus();
                    return true;
                }

                if (alamat.isEmpty()) {
                    alamatB.setError("Alamat Harus Diisi!");
                    alamatB.requestFocus();
                    return true;
                }

                if (hrg.isEmpty()) {
                    hargaB.setError("Harga Harus Diisi!");
                    hargaB.requestFocus();
                    return true;
                }

                if (contact.isEmpty()) {
                    contactB.setError("Contact Harus Diisi!");
                    contactB.requestFocus();
                    return true;
                }
                return false;
            }

        });

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateBusinessActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    askCameraPermissions();
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean askCameraPermissions() {
        int ExtstorePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    CAMERA_PERM_CODE);
            return false;
        } else {
            dispatchTakePictureIntent();
        }
        return true;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                imgB.setImageURI(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                uploadImageToFirebase(f.getName(), contentUri);
            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                imgB.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName, contentUri);
            }

        }


    }

    private void uploadImageToFirebase(String name, Uri contentUri) {

        final StorageReference Ref = storageReference.child(namaB.getText() + "/Profile.jpg");
        Ref.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateBusinessActivity.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.byarbyur.myapplication.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(CreateBusinessActivity.this, ManageBusinessActivity.class));
        finish();
    }
}