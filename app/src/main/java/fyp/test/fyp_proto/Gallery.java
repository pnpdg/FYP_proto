package fyp.test.fyp_proto;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.DocumentCollections;
import com.google.firebase.firestore.model.DocumentKey;
import com.google.firebase.firestore.model.DocumentSet;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.Document;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import droidninja.filepicker.FilePickerActivity;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FilePickerUtils;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Gallery extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    ImageButton galleryOptions;
    FloatingActionButton imagePickBtn;
    FloatingActionButton cameraBtn;
    FloatingActionButton testUpload;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Uri> arrayList = new ArrayList<Uri>();
    ArrayList<Uri> tempImgList = new ArrayList<Uri>();
    //ArrayList<Uri> lastUpdatedList = new ArrayList<Uri>();
    //ArrayList<Uri> latestList = new ArrayList<Uri>();
    ArrayList<String> newarrayList = new ArrayList<String>();
    //GalleryAdp adapter;
    RetrieveAdp newadapter;
    Uri imageUri;

    ArrayList<Uri> urlsList;

    //Database variables
    FirebaseFirestore db;
    StorageReference ref;
    FirebaseStorage mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galleryOptions = findViewById((R.id.gallery_menu_btn));
        imagePickBtn = findViewById(R.id.add_gallery_btn);
        cameraBtn = findViewById(R.id.camera_btn);
        recyclerView = findViewById(R.id.gallery_recycler_view);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new GalleryAdp(arrayList);
        newadapter = new RetrieveAdp(Gallery.this,newarrayList);
        recyclerView.setAdapter(newadapter);
        //firebase instance
        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        ref = mStorage.getReference();
        urlsList = new ArrayList<>();


        //Get Gallery From Database;
        db.collection("Gallery").orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    UploadGallery obj = d.toObject(UploadGallery.class);
                    newarrayList.add(obj.getImageUrl());
                }
                newadapter.notifyDataSetChanged();
            }
        });

        galleryOptions.setOnClickListener(view->{
            PopupMenu pm = new PopupMenu(Gallery.this, galleryOptions);
            pm.getMenu().add("Main Page");
            pm.getMenu().add("Logout");
            pm.show();

            pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getTitle() == "Main Page"){
                        startActivity(new Intent(Gallery.this,ChooseFunction.class));
                        finish();
                        return true;
                    }

                    if(menuItem.getTitle() == "Logout"){
                        startActivity(new Intent(Gallery.this,LoginActivity.class));
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        });

        imagePickBtn.setOnClickListener(view ->{
            //Define camera and storage permissions
            String[] strings = {Manifest.permission.READ_EXTERNAL_STORAGE};

            //Check condition
            if(EasyPermissions.hasPermissions(this,strings)){
                //imagePicker(()-> StoreLinks(tempImgList));
                imagePicker();

            }else{
                //When permission not granted
                //Request permission
                EasyPermissions.requestPermissions(
                        this,
                        "App needs access to your storage",
                        100,
                        strings
                );
            }


        });

        cameraBtn.setOnClickListener(view->{
            //Define camera and storage permissions
            String[] strings = {Manifest.permission.CAMERA};
            //Check condition
            if(EasyPermissions.hasPermissions(this,strings)){
                takePicture();
                //startCropActivity(imageUri);
            }else{
                //When permission not granted
                //Request permission
                EasyPermissions.requestPermissions(
                        this,
                        "App needs access to your camera",
                        10,
                        strings
                );
            }
        });
    }

    private void imagePicker(){
        //Open picker
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Images")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 2)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN, 3)
                .setActivityTheme(R.style.CustomTheme)
                .enableSelectAll(true)
                .enableCameraSupport(false)
                .pickPhoto(this);
    }

    private void takePicture() {
        //ContentValues for image info
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION,"IMAGE_DETAIL");

        //Save imageuri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //Intent to open camera
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(i, 2);
    }

    private void startCropActivity(Uri imageUri){
        //CropImage.activity(imageUri).start(this);
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setInitialCropWindowPaddingRatio(0)
                .start(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Handles the request result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        //Uri imageUri;

        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if(resultCode == RESULT_OK && data!= null) {
            //When activity contain data
            //Check condition
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                //When request for photo
                //Initialize array list
                arrayList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                //Set layout manager
                layoutManager = new GridLayoutManager(this, 2);
                recyclerView.setLayoutManager(layoutManager);
                //recyclerView.setLayoutManager(new LinearLayoutManager(this));
                //Remove duplicates when uploading to database
                //latestList.addAll(arrayList);
                //latestList.removeAll(lastUpdatedList);
                tempImgList.addAll(arrayList);
                //Store to database
                UploadImages(tempImgList);
                //StoreLinks(tempImgList);

            } else if (requestCode == 2) {
                //Get image from camera and start cropping
                //Bitmap b = (Bitmap) data.getExtras().get("data");
                //imageUri = getImageUri(this, b);
                startCropActivity(imageUri);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //After confirming and clicking the crop button
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                //if (resultCode == RESULT_OK) {
                //imageUri = getImageUri(this,b);
                //tempImgList.add(result.toString());
                tempImgList.add(result.getUri());
                imageUri = result.getUri();
                arrayList.add(imageUri);
                layoutManager = new GridLayoutManager(this, 2);
                recyclerView.setLayoutManager(layoutManager);
                //Send to database
                UploadImages(tempImgList);
                //StoreLinks(tempImgList);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //For error handling
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            //lastUpdatedList.addAll(arrayList);
            //Add the arraylist to main arraylist
            for(int i = 0; i< arrayList.size(); i++){
                newarrayList.add(arrayList.get(i).toString());
            }
            arrayList.clear();

            //recyclerView.setAdapter(new GalleryAdp(arrayList));
            //Send to recycleView to display
            recyclerView.setAdapter(new RetrieveAdp(Gallery.this, newarrayList));
        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //Check condition
        if (requestCode == 100 && perms.size() == 1){
            //When permissions are granted
            //Call method
            //imagePicker(()-> StoreLinks(tempImgList));
            imagePicker();
        }
        else if (requestCode == 10 && perms.size() == 1){
            takePicture();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            //When permissions denied multiple times
            //Open app setting
            new AppSettingsDialog.Builder(this).build().show();
        }else{
            //When permission deny once
            //Display toast
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImages(ArrayList<Uri> ImgList){
        for (int i =0; i < ImgList.size(); i++){
            Uri individualImg = ImgList.get(i);
            if (individualImg != null) {
                if (ImgList.get(i).getScheme().startsWith("file")){
                    StorageReference imgFolder = FirebaseStorage.getInstance().getReference().child("Gallery");
                    final StorageReference imgName = imgFolder.child(System.currentTimeMillis()+"."+getCropFileExtension(individualImg));
                    imgName.putFile(individualImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlsList.add(uri);
                                    //urlsList.add(String.valueOf(uri));
                                    StoreLinks(urlsList, imgName.toString());
                                }
                            });
                        }
                    });
                }
                else{
                    StorageReference imgFolder = FirebaseStorage.getInstance().getReference().child("Gallery");
                    final StorageReference imgName = imgFolder.child(System.currentTimeMillis()+"."+getFileExtension(individualImg));
                    imgName.putFile(individualImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    urlsList.add(uri);
                                    //urlsList.add(String.valueOf(uri));
                                    StoreLinks(urlsList, imgName.toString());
                                }
                            });
                        }
                    });
                }

            }
        }
    }

    private void StoreLinks(ArrayList<Uri> UriList, String imgName){
        UploadGallery model;
        //String Name = imgName.getText().toString();
        for (int i =0; i < UriList.size(); i++){
            model = new UploadGallery(imgName, UriList.get(i).toString());
            db.collection("Gallery").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(Gallery.this, "Successfully uploaded to database", Toast.LENGTH_SHORT ).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Gallery.this, "Uploading Failed", Toast.LENGTH_SHORT ).show();

                }
            });
            }

        tempImgList.clear();
        UriList.clear();

    }
    /*
    //ArrayList<String> ImgList
    private void StoreLinks(ArrayList<Uri> ImgList){
        UploadGallery model;
        //String Name = imgName.getText().toString();
        for (int i =0; i < ImgList.size(); i++){
            if (ImgList.get(i).getScheme().startsWith("file")){
                model = new UploadGallery(System.currentTimeMillis()+"."+getCropFileExtension(ImgList.get(i)), ImgList.get(i).toString());
            }
            else{
                model = new UploadGallery(System.currentTimeMillis()+"."+getFileExtension(ImgList.get(i)), ImgList.get(i).toString());
            }

            db.collection("Gallery 1").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(Gallery.this, "Successfully uploaded to database", Toast.LENGTH_SHORT ).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Gallery.this, "Uploading Failed", Toast.LENGTH_SHORT ).show();

                }
            });
        }
        ImgList.clear();

    }*/

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String getCropFileExtension(Uri uri){
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    }
}