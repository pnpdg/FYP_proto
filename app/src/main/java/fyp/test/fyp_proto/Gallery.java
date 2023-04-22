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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.util.Base64;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.model.DocumentCollections;
import com.google.firebase.firestore.model.DocumentKey;
import com.google.firebase.firestore.model.DocumentSet;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.Document;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.io.FileUtils;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import droidninja.filepicker.FilePickerActivity;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.FilePickerUtils;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Gallery extends BaseActivity implements EasyPermissions.PermissionCallbacks, RetrieveAdp.OnItemClickListener {

    private static final int DELETE_REQUEST_CODE = 13;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 99;
    ImageButton galleryOptions;
    FloatingActionButton imagePickBtn;
    FloatingActionButton cameraBtn;
    FloatingActionButton testUpload;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Uri> arrayList = new ArrayList<Uri>();
    ArrayList<Uri> tempImgList = new ArrayList<Uri>();
    //ArrayList<Uri> tempList = new ArrayList<Uri>();
    //ArrayList<Uri> lastUpdatedList = new ArrayList<Uri>();
    //ArrayList<Uri> latestList = new ArrayList<Uri>();
    ArrayList<UploadGallery> newarrayList = new ArrayList<UploadGallery>();
    ArrayList<UploadGallery> tempList = new ArrayList<UploadGallery>();
    ArrayList<Uri> moveFilesList = new ArrayList<Uri>();
    ArrayList<String> moveFilesList2 = new ArrayList<String>();
    ArrayList<String> updatedList = new ArrayList<String>();
    ArrayList<Uri> normalList = new ArrayList<Uri>();

    ArrayList<String> filePath = new ArrayList<>();
    //GalleryAdp adapter;
    RetrieveAdp newadapter;
    RetrieveAdp newadapter2;
    Uri imageUri;
    ArrayList<Uri> hideList = new ArrayList<Uri>();
    File dir;
    Bitmap bitmapMain;

    ArrayList<Uri> urlsList;

    //Database variables
    FirebaseFirestore db ;
    StorageReference ref;
    FirebaseStorage mStorage;
    DocumentReference dr;
    FirebaseUser currentUser;

    //For encryption
    private final static int READ_WRITE_BLOCK_BUFFER = 1024;
    private final static String ALGO_IMAGE_ENCRYPTION = "AES/CBC/PKCS5Padding";
    private final static String ALGO_SECRET_KEY = "AES";
    File myDir;
    File receiveDir;
    File output;
    String my_key;
    String my_spec_key;
    String FILE_NAME_ENC;
    String newObjUri;
    ArrayList<Uri> encryptList = new ArrayList<Uri>();
    long numOfPass;
    long num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        myDir = new File(Environment.getExternalStorageDirectory().toString() + "/encrypted_images");
        receiveDir = new File(Environment.getExternalStorageDirectory().toString() + "/fromDatabase");
        my_key=currentUser.getUid();
        my_spec_key = currentUser.getUid();
        /*if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }*/

        galleryOptions = findViewById((R.id.gallery_menu_btn));
        imagePickBtn = findViewById(R.id.add_gallery_btn);
        cameraBtn = findViewById(R.id.camera_btn);
        recyclerView = findViewById(R.id.gallery_recycler_view);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new GalleryAdp(arrayList);
        newadapter = new RetrieveAdp(Gallery.this,newarrayList);
        //newadapter = new RetrieveAdp(Gallery.this,normalList);
        recyclerView.setAdapter(newadapter);
        newadapter.setOnItemClickListener(Gallery.this);

        //recyclerView.setAdapter(newadapter);
        //firebase instance
        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorage.setMaxDownloadRetryTimeMillis(60000);  // wait 1 min for downloads
        mStorage.setMaxOperationRetryTimeMillis(10000);  // wait 10s for normal ops
        mStorage.setMaxUploadRetryTimeMillis(120000);  // wait 2 mins for uploads
        ref = mStorage.getReference();
        urlsList = new ArrayList<>();

        //refreshGallery();
        //Get Gallery From Firestore;
        db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    UploadGallery obj = d.toObject(UploadGallery.class);
                    //obj.setDocID(d.getId());
                    //tempList.add(obj);
                    String objUri = obj.getImageUrl();
                    try {
                        newObjUri = decrypt(objUri, currentUser.getUid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    obj.setImageUrl(newObjUri);
                    newarrayList.add(obj);
                }
                newadapter.notifyDataSetChanged();
            }
        });

        /*try {
            convertUritoFile(tempList);
        } catch (IOException e) {
            e.printStackTrace();
        }*/



        /*db.collection("Gallery").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                newarrayList.clear();
                for(DocumentSnapshot d : value.getDocuments()){

                }
                for(DocumentChange dc : value.getDocuments()){
                    switch (dc.getType()){
                        case REMOVED:

                    }
                }
                List<DocumentSnapshot> list = value.getDocuments();
                for (DocumentSnapshot d : list){
                    UploadGallery obj = d.toObject(UploadGallery.class);
                    newarrayList.add(obj);
                }
                newadapter.notifyDataSetChanged();
            }
        });*/

        //db.collection("Gallery").addSnapshotListener(new EventListener<DocumentSnapshot>()){

        //}
        numOfPass();

        galleryOptions.setOnClickListener(view->{

            PopupMenu pm = new PopupMenu(Gallery.this, galleryOptions);
            pm.getMenu().add("Main Page");
            //if(num < 1){
            pm.getMenu().add("Set or Update password");
            if(num > 0){
                pm.getMenu().add("Delete password");
            }
            //}else{
               // pm.getMenu().add("Set password").setEnabled(false);
                //pm.getMenu().add("Update password");
            //}

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

                    if(menuItem.getTitle() == "Set or Update password"){
                        //if(num < 1){
                            startActivity(new Intent(Gallery.this,setPassGallery.class));
                        //}else{
                            //Toast.makeText(getApplicationContext(), "Password already exists", Toast.LENGTH_SHORT).show();
                        //}
                        //finish();
                        return true;
                    }

                    if(menuItem.getTitle() == "Delete password"){
                        deletePass();
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
            String[] strings = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

    @Override
    public void onBackPressed(){
        startActivity(new Intent(Gallery.this,ChooseFunction.class));
    }

    public void numOfPass(){
        db.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if(task.isSuccessful()){
                            AggregateQuerySnapshot snapshot = task.getResult();
                            num = snapshot.getCount();
                            Log.i("Show number of entries in gallery pass", String.valueOf(num));
                        }
                    }
                });
        //return numOfPass;
    }

    public void deletePass(){
        db.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    String documentID = documentSnapshot.getId();
                    db.collection("Password").document(currentUser.getUid()).collection("Gallery Pass").document(documentID).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Password Deleted", Toast.LENGTH_SHORT).show();
                                    Log.i("Deletion of password","Success");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to delete password", Toast.LENGTH_SHORT).show();
                                    Log.i("Deletion of password","Failure");
                                }
                            });
                }
            }
        });
    }

    public String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    public String encrypt (String Data, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedvalue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedvalue;
    }

    public SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[]  bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }
    private void convertUritoFile(ArrayList<UploadGallery> ls) throws IOException {
        if(!receiveDir.exists()){
            receiveDir.mkdir();
        }
        for( int i = 0; i< ls.size(); i++){
            //Uri ug = Uri.parse(ls.get(i).getImageUrl());
            /*InputStream inputStream = null;
            inputStream = getContentResolver().openInputStream(ug);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            String FILE_NAME_ENC = String.valueOf(System.currentTimeMillis());
            File output = new File(receiveDir, FILE_NAME_ENC);
            OutputStream outputStream = new FileOutputStream(output);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();*/

            /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ug);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            InputStream is = new ByteArrayInputStream(stream.toByteArray());
            String FILE_NAME_ENC = String.valueOf(System.currentTimeMillis());
            File output = new File(receiveDir, FILE_NAME_ENC);
            OutputStream out = new FileOutputStream(output);
            int count = 0;
            byte[] buffer = new byte[READ_WRITE_BLOCK_BUFFER];
            while ((count = is.read(buffer))> 0)
                out.write(buffer, 0, count);

            out.close();*/
            //String ug = ls.get(i).getImageUrl();
            FirebaseStorage storageTest = FirebaseStorage.getInstance();
            String name = (ls.get(i)).getName();
            String finalName = name.substring(name.lastIndexOf("/")+1).trim();
            StorageReference sr = storageTest.getReference(name);

            //String FILE_NAME_ENC = String.valueOf(System.currentTimeMillis());
            output = new File(receiveDir, finalName);
            sr.getFile(output)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            decryptMethod(output.toString(), receiveDir, finalName);
                            //Toast.makeText(getApplicationContext(), "File downloaded", Toast.LENGTH_SHORT).show();
                            Log.i("convertUriToFile", "Local item file created");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getApplicationContext(), "File download failed", Toast.LENGTH_SHORT).show();
                            Log.i("convertUriToFile", "Local item file not created");
                        }
                    });
            /*String ug = ls.get(i).getImageUrl();
            String FILE_NAME_ENC = String.valueOf(System.currentTimeMillis());
            File output = new File(receiveDir, FILE_NAME_ENC);
            FileOutputStream out = new FileOutputStream(output);
            //FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);
            File file = new File(ug);
            //byte[] bytes = compress(FileUtils.readFileToByteArray(file), Deflater.BEST_COMPRESSION, false);
            byte[] bytes = getBytesFromFile(file);
            out.write(bytes);
            out.close();*/
        }
    }

    private void refreshGallery() {
        db.collection("Gallery").orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    UploadGallery obj = d.toObject(UploadGallery.class);
                    newarrayList.add(obj);
                }
                newadapter.notifyDataSetChanged();
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
                //layoutManager = new GridLayoutManager(this, 2);
                //recyclerView.setLayoutManager(layoutManager);
                //recyclerView.setLayoutManager(new LinearLayoutManager(this));
                //Remove duplicates when uploading to database
                //latestList.addAll(arrayList);
                //latestList.removeAll(lastUpdatedList);
                /*try {
                    tempImgList.addAll(encryptMethod(arrayList));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                /*try {
                    unblurImg(arrayList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newadapter.notifyDataSetChanged();*/



                tempImgList.addAll(arrayList);
                //moveFilesList.addAll(arrayList);
                //Store to database
                UploadImages(tempImgList);
                deleteFile(arrayList);
                /*File[] files = myDir.listFiles();
                for(File f: files){
                    if(f.isFile()){
                        f.delete();
                    }
                }*/
                /*for (int i = 0; i< arrayList.size(); i++){
                    Uri uri = getContentUriId(Gallery.this, arrayList.get(i));
                    File f = new File(uri.toString());
                    if ( f.exists()){
                        try {
                            FileUtils.forceDelete(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }*/
                /*for (int i = 0; i< arrayList.size(); i++){
                    if(arrayList.get(i).getScheme().startsWith("content")){
                        Uri uri = getContentUriId(Gallery.this, arrayList.get(i));
                        ContentResolver resolver = this.getContentResolver();
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.R){
                            List<Uri> uriList = new ArrayList<>();
                            Collections.addAll(uriList, uri);
                            PendingIntent pendingIntent = MediaStore.createDeleteRequest(resolver, uriList);
                            try {
                                ((Activity)this).startIntentSenderForResult(pendingIntent.getIntentSender(), DELETE_REQUEST_CODE, null, 0,0, 0, null);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                        if(requestCode = DELETE_REQUEST_CODE){

                        }
                    }
                }*/
                //StoreLinks(tempImgList);
                //Hide gallery
                //hideList.addAll(arrayList);
                //tempList.addAll(arrayList);
                /*try {
                    hideImg(hideList);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DataFormatException e) {
                    e.printStackTrace();
                }*/
                //waitTask wt = new waitTask();
                //wt.execute();

                //newadapter2 = new RetrieveAdp(Gallery.this,newarrayList);
                //newadapter2 = new RetrieveAdp(Gallery.this,newarrayList);

                /*for(int i = 0 ; i<arrayList.size();i++){
                    File directory = new File("/data/user/0/fyp.test.fyp_proto/files/");
                    File[] files = directory.listFiles(File::isFile);
                    long lastModifiedTime = Long.MIN_VALUE;
                    File chosenFile = null;

                    if (files != null)
                    {
                        for (File file : files)
                        {
                            if (file.lastModified() > lastModifiedTime)
                            {
                                chosenFile = file;
                                lastModifiedTime = file.lastModified();
                            }
                        }
                    }
                    String hiddenUri = filesFromHidden(chosenFile);
                    moveFilesList.add(hiddenUri);
                }
                newarrayList.addAll(moveFilesList);
                moveFilesList.clear();*/


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
                //tempImgList.add(result.getUri());
                //imageUri = result.getUri();
                //arrayList.add(imageUri);
                //Send to database
                //UploadImages(tempImgList);
                //deleteFile(arrayList);
                //StoreLinks(tempImgList);
                //Hide gallery
                /*hideList.add(result.getUri());
                try {
                    hideImg(hideList);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                //For error handling
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            //lastUpdatedList.addAll(arrayList);
            //Add the arraylist to main arraylist
            /*for(int i = 0; i< arrayList.size(); i++){
                newarrayList.add(arrayList.get(i).toString());
            }*/
            //newarrayList.addAll(getAllFiles());
            //filePath.clear();
            arrayList.clear();

            //recyclerView.setAdapter(new RetrieveAdp(Gallery.this,newarrayList));;
            //recyclerView.setAdapter(newadapter2);;
            //recyclerView.setAdapter(new RetrieveAdp(Gallery.this,newarrayList));
            //recyclerView.setAdapter(new GalleryAdp(arrayList));
            //Send to recycleView to display

            /*layoutManager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(layoutManager);
            newadapter = new RetrieveAdp(Gallery.this,newarrayList);
            recyclerView.setAdapter(newadapter);
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
            });*/


            //refreshGallery();


            //newadapter = new RetrieveAdp(Gallery.this,newarrayList);

            //ewarrayList.clear();
            //deleteFile(hideList);
        }
    }

    /*private void deleteFile(Uri uriLink){

            if(uriLink.getScheme().startsWith("content")){
                try{
                    ContentResolver resolver = this.getContentResolver();
                    Uri uri = getContentUriId(Gallery.this, uriLink);
                    resolver.delete(uri, null, null);
                }catch (SecurityException e){
                    PendingIntent pendingIntent = null;
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
                        ArrayList<Uri> uris = new ArrayList<>();
                        uris.add(uri);
                        pendingIntent = MediaStore.createDeleteRequest(getContentResolver(), uris);
                    }
                }
                /*Uri uri = hidingList.get(i);
                ContentResolver resolver = this.getContentResolver();
                String documentId = DocumentsContract.getDocumentId(uri);
                Uri parentUri = DocumentsContract.buildDocumentUriUsingTree(uri, documentId.split(":")[0]);
                resolver.delete(parentUri, null, null);*/
                /*ContentResolver resolver = this.getContentResolver();
                Uri uri = getContentUriId(Gallery.this, hidingList.get(i));
                resolver.delete(uri, null, null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                this.sendBroadcast(intent);
                scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()});
            }
            else{
                ContentResolver resolver = this.getContentResolver();
                String uri = getRealPathFromUri(Gallery.this,hidingList.get(i));
                File f = new File(uri);
                resolver.delete(hidingList.get(i), MediaStore.Images.Media.DATA + "=?", new String[]{f.getAbsolutePath()});
                //resolver.delete(hidingList.get(i), MediaStore.Images.Media.DATA + "=?", new String[]{f.getAbsolutePath()});
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                this.sendBroadcast(intent);
                scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()});
            }



        //hidingList.clear();

    }*/
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
    public ArrayList<Uri> encryptMethod(ArrayList<Uri> al) throws IOException {
        if(!myDir.exists()){
            myDir.mkdir();
        }
        for(int i = 0; i < al.size(); i++){
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), al.get(i));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            InputStream is = new ByteArrayInputStream(stream.toByteArray());
            FILE_NAME_ENC = String.valueOf(System.currentTimeMillis());
            File outputFileEnc = new File(myDir, FILE_NAME_ENC);
            try{
                encryptToFile(my_key, my_spec_key, is, new FileOutputStream(outputFileEnc));
                File fileUpload = new File(myDir, String.valueOf(FILE_NAME_ENC));
                Uri imageUri = Uri.fromFile(fileUpload);
                encryptList.add(imageUri);
                Toast.makeText(Gallery.this, "Encrypted", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return encryptList;
    }
    public static void encryptToFile(String keyStr, String specStr, InputStream in, OutputStream out) throws IOException {
        try{
            IvParameterSpec iv = new IvParameterSpec(specStr.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes("UTF-8"), ALGO_SECRET_KEY);
            Cipher c = Cipher.getInstance((ALGO_IMAGE_ENCRYPTION));
            c.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            out = new CipherOutputStream(out, c);
            int count = 0;
            byte[] buffer = new byte[READ_WRITE_BLOCK_BUFFER];
            while ((count = in.read(buffer))> 0)
                out.write(buffer, 0, count);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } finally{
            out.close();
        }
    }

    public void decryptMethod(String path, File rootPath, String name){
        try{
            File outputFileDir = new File(rootPath, name + ".jpg");
            decryptToFile(my_key, my_spec_key, new FileInputStream(path), new FileOutputStream(outputFileDir));
            Log.i("decryptMethod", "Image successfully decrypted");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("decryptMethod", "Decryption error");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*private ArrayList<Uri> decryptMethod(ArrayList<Uri> al) throws IOException {
        if(!myDir.exists()){
            myDir.mkdir();
        }
        for(int i = 0; i < al.size(); i++){
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), al.get(i));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            InputStream is = new ByteArrayInputStream(stream.toByteArray());
            String FILE_NAME_ENC = String.valueOf(System.currentTimeMillis());
            File outputFileEnc = new File(myDir, FILE_NAME_ENC);
            try{
                decryptToFile(my_key, my_spec_key, is, new FileOutputStream(outputFileEnc));
                File fileUpload = new File(myDir, String.valueOf(FILE_NAME_ENC));
                Uri imageUri = Uri.fromFile(fileUpload);
                encryptList.add(imageUri);
                Toast.makeText(Gallery.this, "Encrypted", Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return encryptList;
    }*/
    public static void decryptToFile(String keyStr, String specStr, InputStream in, OutputStream out) throws IOException {
        try{
            IvParameterSpec iv = new IvParameterSpec(specStr.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes("UTF-8"), ALGO_SECRET_KEY);
            Cipher c = Cipher.getInstance((ALGO_IMAGE_ENCRYPTION));
            c.init(Cipher.DECRYPT_MODE, keySpec, iv);
            out = new CipherOutputStream(out, c);
            int count = 0;
            byte[] buffer = new byte[READ_WRITE_BLOCK_BUFFER];
            while ((count = in.read(buffer))> 0)
                out.write(buffer, 0, count);
        }catch (Exception e) {
            Log.i("Decryption", "DecryptToFile Error:" + e.getMessage());
        } finally{
            out.close();
        }
    }

    //Upload to storage
    private void UploadImages(ArrayList<Uri> ImgList){
        for (int i =0; i < ImgList.size(); i++){
            Uri individualImg = ImgList.get(i);
            if (individualImg != null) {
                if (ImgList.get(i).getScheme().startsWith("file")){
                    StorageReference imgFolder = FirebaseStorage.getInstance().getReference().child("Gallery").child(currentUser.getUid());
                    final StorageReference imgName = imgFolder.child(System.currentTimeMillis() + "." + getCropFileExtension(individualImg));
                    imgName.putFile(individualImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    /*String splitUri = uri.toString();
                                    int index = splitUri.indexOf("/", splitUri.indexOf("/", splitUri.indexOf("/") + 1) + 1);
                                    splitUri = splitUri.substring(index).trim();
                                    String newLink = "https://ik.imagekit.io/60gjeavuh/tr:bl-20";
                                    String finalUrl = newLink.concat(splitUri);
                                    urlsList.add(Uri.parse(finalUrl));*/
                                    urlsList.add(uri);
                                    //urlsList.add(String.valueOf(uri));
                                    try {
                                        StoreLinks(urlsList, imgName.toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }
                else{
                    StorageReference imgFolder = FirebaseStorage.getInstance().getReference().child("Gallery").child(currentUser.getUid());
                    final StorageReference imgName = imgFolder.child(System.currentTimeMillis() + "." + getFileExtension(individualImg));
                    imgName.putFile(individualImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    /*String splitUri = uri.toString();
                                    int index = splitUri.indexOf("/", splitUri.indexOf("/", splitUri.indexOf("/") + 1) + 1);
                                    splitUri = splitUri.substring(index).trim();
                                    String newLink = "https://ik.imagekit.io/60gjeavuh/tr:bl-20";
                                    String finalUrl = newLink.concat(splitUri);
                                    urlsList.add(Uri.parse(finalUrl));*/
                                    urlsList.add(uri);
                                    //urlsList.add(String.valueOf(uri));
                                    try {
                                        StoreLinks(urlsList, imgName.toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }

            }
        }

    }

    //To firestore
    private void StoreLinks(ArrayList<Uri> UriList, String imgName) throws Exception {
        UploadGallery model;
        //String Name = imgName.getText().toString();
        for (int i =0; i < UriList.size(); i++){
            Uri uri = UriList.get(i);
            String newUri = encrypt(uri.toString(), currentUser.getUid());
            //model = new UploadGallery(imgName, uri.toString());
            model = new UploadGallery(imgName, newUri);
            UploadGallery finalModel = model;
            db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    //updatedList.add(uri.toString());
                    //finalModel.setDocID(documentReference.getId());
                    finalModel.setDocId(documentReference.getId());
                    db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").document(finalModel.getDocId())
                                    .set(finalModel, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //tempList.add(finalModel);
                                    String objUri = finalModel.getImageUrl();
                                    try {
                                        newObjUri = decrypt(objUri, currentUser.getUid());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    finalModel.setImageUrl(newObjUri);
                                    newarrayList.add(finalModel);
                                    newadapter.notifyDataSetChanged();
                                    Toast.makeText(Gallery.this, "Successfully uploaded to database", Toast.LENGTH_SHORT ).show();
                                }
                            });
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
        //encryptList.clear();
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

    private Uri getContentUriId(Context context,Uri imageUri){
        long id = 0;
        String[] proj = {MediaStore.MediaColumns._ID};
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                proj,
                MediaStore.MediaColumns.DATA + "=?", new String[]{imageUri.getPath()}, null);
        if(cursor!= null){

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            }
        }
        cursor.close();
        //return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "/");
        return Uri.parse((MediaStore.Images.Media.EXTERNAL_CONTENT_URI).toString() + "/" + id);

    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            //Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri){
        String result = null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            try{
                if (cursor!= null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally{
                cursor.close();
            }
        }
        if(result ==null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut!= -1){
                result = result.substring(cut+1);
            }
        }
        return result;
    }

    public static void scanFile(Context context, String[] path) {
        MediaScannerConnection.scanFile(context.getApplicationContext(), path, null, null);
    }

    /*private void renameFile(ArrayList<Uri> hidingList){
        for (int i =0; i < hidingList.size(); i++){
            String uri = getRealPathFromUri(Gallery.this,hidingList.get(i));
            String newUri = uri.substring(0, uri.lastIndexOf("."));
            File file = new File(uri);
            file.renameTo(new File(newUri));
        }
        hidingList.clear();
    }*/

    private void deleteFile(ArrayList<Uri> hidingList){
        for (int i =0; i < hidingList.size(); i++) {
            /*if(hidingList.get(i).getScheme().startsWith("content")){
                try{
                    ContentResolver resolver = this.getContentResolver();
                    Uri uri = getContentUriId(Gallery.this, hidingList.get(i));
                    resolver.delete(uri, null, null);
                }catch (SecurityException e){
                    PendingIntent pendingIntent = null;
                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
                        ArrayList<Uri> uris = new ArrayList<>();
                        uris.add(uri);
                        pendingIntent = MediaStore.createDeleteRequest(getContentResolver(), uris);
                    }
                }*/
                /*Uri uri = hidingList.get(i);
                ContentResolver resolver = this.getContentResolver();
                String documentId = DocumentsContract.getDocumentId(uri);
                Uri parentUri = DocumentsContract.buildDocumentUriUsingTree(uri, documentId.split(":")[0]);
                resolver.delete(parentUri, null, null);*/

            ContentResolver resolver = this.getContentResolver();
            resolver.delete(hidingList.get(i), null, null);
            //resolver.delete(hidingList.get(i), MediaStore.Images.Media.DATA + "=?", new String[]{f.getAbsolutePath()});
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            this.sendBroadcast(intent);
            scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()});
        }
        //hidingList.clear();

    }
    //private ArrayList<String> hideImg(ArrayList<Uri> hidingList) throws IOException, DataFormatException {

        //long lastModifiedTime = Long.MIN_VALUE;
        //String hidden_folder_url="/storage/emulated/0/.gallery dir/";
        /*File hiddenDir = getApplicationContext().getDir(".gallery dir",Context.MODE_PRIVATE);
        //File path = new File(this.getFilesDir(), hidden_folder_url);
        if (!hiddenDir.exists()){
            hiddenDir.mkdirs();
            for (int i =0; i < hidingList.size(); i++){
                String uri = getRealPathFromUri(Gallery.this,hidingList.get(i));
                File f = new File(uri);
                String img_name = f.getName();
                img_name = new StringBuilder().append(hiddenDir).append(img_name).toString();
                Path temp = Files.move
                        (Paths.get(f.getAbsolutePath()), Paths.get(img_name));
            }
            hidingList.clear();
        }
        else{
            for (int i =0; i < hidingList.size(); i++){
                String uri = getRealPathFromUri(Gallery.this,hidingList.get(i));
                File f = new File(uri);
                String img_name = f.getName();
                img_name = new StringBuilder().append(hidden_folder_url).append(img_name).toString();
                Path temp = Files.move
                        (Paths.get(f.getAbsolutePath()), Paths.get(img_name));
            }
            hidingList.clear();
        }*/
        //String hidden_folder_url="content://com.android.providers.downloads.documents/document/downloads";
        /*for (int i =0; i < hidingList.size(); i++) {
            //String uri = getRealPathFromURI(hidingList.get(i));
            String uri = getRealPathFromUri(Gallery.this, hidingList.get(i));
            String name = getFileName(hidingList.get(i));
            FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);
            File file = new File(uri);
            //byte[] bytes = compress(FileUtils.readFileToByteArray(file), Deflater.BEST_COMPRESSION, false);
            byte[] bytes = getBytesFromFile(file);
            fos.write(bytes);
            fos.close();*/
            //Toast.makeText(getApplicationContext(), "File saved in: " + getFilesDir() + "/" + name, Toast.LENGTH_SHORT).show();
            //deleteFile(hidingList.get(i));
            //Get last modified file
            //File lastModFile = new File(getLastModified("/data/user/0/fyp.test.fyp_proto/files"));
            /*File directory = new File("/data/user/0/fyp.test.fyp_proto/files");
            File[] files = directory.listFiles(File::isFile);
            long lastModifiedTime = Long.MIN_VALUE;
            File chosenFile = null;

            if (files != null)
            {
                for (File file2 : files)
                {
                    if (file2.lastModified() > lastModifiedTime)
                    {
                        chosenFile = file2;
                        lastModifiedTime = file2.lastModified();
                    }
                }
            }*/

            //File lastModFile = getLastModified("/data/user/0/fyp.test.fyp_proto/files");
            /*byte[] decompressData = decompress(FileUtils.readFileToByteArray(lastModFile), false);
            File f = new File("");
            OutputStream os = new FileOutputStream(f);
            os.write(decompressData);
            os.close();*/
            //Get the uri from the hidden folder
            //String hiddenUri = filesFromHidden(chosenFile);
            /*Uri uriPath = Uri.fromFile(lastModFile);
            InputStream inputStream = null;
            try {
                assert uriPath != null;
                inputStream = getContentResolver().openInputStream(uriPath);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }

            BitmapFactory.decodeStream(inputStream);*/

            //String hiddenUri = filesFromHidden(lastModFile);
            //String hiddenUri = filesFromHidden(f);
            //moveFilesList.add(hiddenUri);


            //String uri = hidingList.get(i).toString();
            /*File f = new File(uri, ".nomedia");
            if (!f.exists()) {
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    out.flush();
                    out.close();
                    scanFile(Gallery.this, new String[]{ f.getAbsolutePath() });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            //String absolute = f.getAbsolutePath();
            //File f = new File(uri);
            //String img_name = f.getName();
            //Uri urifromFile = Uri.fromFile(f);
            //img_name = new StringBuilder().append(hidden_folder_url).append(img_name).toString();
            //Path temp = Files.move
                   //(Paths.get(uri), Paths.get(img_name));
            //Path temp = Files.move
                    //(Paths.get(f.getAbsolutePath()), Paths.get(img_name));
            //Path temp = Files.move
                    //(Paths.get(urifromFile.toString()).toAbsolutePath(), Paths.get(img_name));
            /*File f = new File(hidingList.get(i).toString());
            //String absolute = f.getAbsolutePath();
            String img_name = f.getName();
            //Uri urifromFile = Uri.fromFile(f);
            img_name = new StringBuilder().append(hidden_folder_url).append(img_name).toString();
            Path temp = Files.move
                    (Paths.get(String.valueOf(hidingList.get(i))).toAbsolutePath(), Paths.get(img_name));
            ContentResolver resolver = getContentResolver();
            file = resolver.openInputStream(uri);*/
        //}
        //scanFile(Gallery.this, null, null, null);
        //newarrayList.addAll(moveFilesList);
        //moveFilesList.clear();
        //hidingList.clear();


        //recyclerView.setAdapter(new RetrieveAdp(Gallery.this,newarrayList));;
        //newadapter2.notifyDataSetChanged();

    //}

    private byte[] getBytesFromFile(File file) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(file);
        return data;
    }

   /*private ArrayList<String> getAllFiles(){
        String path = "/data/user/0/fyp.test.fyp_proto/files";
        //Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        //Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {

            //Paths.get(files[i].toString()).toUri()
            //String uriPath = getRealPathFromUri(Gallery.this, Uri.fromFile(files[i]));
            Uri uriPath = Uri.fromFile(files[i]);
            InputStream inputStream = null;
            try {
                assert uriPath != null;
                inputStream = getContentResolver().openInputStream(uriPath);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }

            BitmapFactory.decodeStream(inputStream);
            filePath.add(uriPath.toString());
            //Log.d("Files", "FileName:" + files[i].getName());
        }
        return filePath;
    }*/
    private String filesFromHidden(File f) throws IOException {
        //String path = "/data/user/0/fyp.test.fyp_proto/files";
        //Log.d("Files", "Path: " + path);
        //File directory = new File(path);
        //File[] files = directory.listFiles();
        //Log.d("Files", "Size: "+ files.length);

            //Paths.get(files[i].toString()).toUri()
            //String uriPath = getRealPathFromUri(Gallery.this, Uri.fromFile(files[i]));
        Uri uriPath = Uri.fromFile(f);
        InputStream inputStream = null;
        try {
            assert uriPath != null;
            inputStream = getContentResolver().openInputStream(uriPath);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        BitmapFactory.decodeStream(inputStream);
        inputStream.close();
        //filePath.add(uriPath.toString());
        //Log.d("Files", "FileName:" + files[i].getName());

        return uriPath.toString();
    }
    public static File getLastModified(String directoryFilePath) throws IOException {
        /*File directory = new File(directoryFilePath);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null)
        {
            for (File file : files)
            {
                if (file.lastModified() > lastModifiedTime)
                {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        return chosenFile;*/

        Path directoryPath = Paths.get(directoryFilePath);
        Path latestFile = Files.list(directoryPath)
                .map(Path::toAbsolutePath)
                .max(Comparator.comparingLong(path-> getFileCreationTime(path)))
                .orElse(null);

        return latestFile.toFile();
    }

    private static long getFileCreationTime(Path path) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            return attributes.creationTime().toMillis();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file attributes: " + path, e);
        }
    }


    public static byte[] compress(byte[] input, int compressionLevel,
                                  boolean GZIPFormat) throws IOException {
        Deflater compressor = new Deflater(compressionLevel, GZIPFormat);
        compressor.setInput(input);
        compressor.finish();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] readBuffer = new byte[1024];
        int readCount = 0;
        while (!compressor.finished()) {
            readCount = compressor.deflate(readBuffer);
            if (readCount > 0) {
                bao.write(readBuffer, 0, readCount);
            }
        }
        compressor.end();
        return bao.toByteArray();
    }
    public static byte[] decompress(byte[] input, boolean GZIPFormat)
            throws IOException, DataFormatException {
        Inflater decompressor = new Inflater(GZIPFormat);
        decompressor.setInput(input);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] readBuffer = new byte[1024];
        int readCount = 0;
        while (!decompressor.finished()) {
            readCount = decompressor.inflate(readBuffer);
            if (readCount > 0) {
                bao.write(readBuffer, 0, readCount);
            }
        }
        decompressor.end();
        return bao.toByteArray();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    //Delete from storage
    @Override
    public void onDeleteClick(int position) {

        UploadGallery selectedItem = newarrayList.get(position);
        StorageReference imageRef = mStorage.getReferenceFromUrl((selectedItem.getImageUrl()));
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                deleteFireStore(selectedItem);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to delete from storage", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, "Delete click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    //Delete from firestore
    public void deleteFireStore(UploadGallery item){
        db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").document(item.getDocId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                newarrayList.clear();
                db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list){
                            UploadGallery obj = d.toObject(UploadGallery.class);
                            //obj.setDocID(d.getId());
                            String objUri = obj.getImageUrl();
                            try {
                                newObjUri = decrypt(objUri, currentUser.getUid());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            obj.setImageUrl(newObjUri);
                            newarrayList.add(obj);
                        }
                        newadapter.notifyDataSetChanged();
                    }
                });
                Toast.makeText(getApplicationContext(), "Image successfully deleted", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to delete image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDelUnhideClick(int position) {
        UploadGallery selectedItem = newarrayList.get(position);;
        String fullFileName = selectedItem.getName();
        String fileName = fullFileName.substring(fullFileName.lastIndexOf("/")+1).trim();
        StorageReference sr = FirebaseStorage.getInstance().getReference().child("Gallery/").child(currentUser.getUid() + "/" + fileName);
        long SIZE = 1024 * 1024 * 5;
        sr.getBytes(SIZE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        createImage(Gallery.this, bitmap);
                        onDeleteClick(position);
                    }
                });
    }

    public void createImage(Context context, Bitmap bitmap){
        ContentResolver cr = getContentResolver();
        String title = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String savedURL = MediaStore.Images.Media.insertImage(cr, bitmap, title, title);
        Toast.makeText(Gallery.this, savedURL, Toast.LENGTH_SHORT).show();

        /*OutputStream fos;
        Uri uri = null;
        ContentResolver contentResolver = context.getContentResolver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }else{
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        if(uri == null){
            uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }

        String img = String.valueOf(Calendar.getInstance().getTimeInMillis());
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, img + ".jpg");
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/");
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri finalUri = contentResolver.insert(uri, cv);
        try{
            fos = contentResolver.openOutputStream(Objects.requireNonNull(finalUri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Objects.requireNonNull(fos);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }

    /*class waitTask extends AsyncTask<Void, Void, Void>{

        ProgressDialog pd;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(Gallery.this);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //newarrayList.add(uri.toString());
            createImage(Gallery.this, bitmapMain);
            try {
                Thread.sleep(5000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused){
            super.onPostExecute(unused);
            //layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            //recyclerView.setLayoutManager(layoutManager);
            //recyclerView.setAdapter(new RetrieveAdp(Gallery.this,newarrayList));
            pd.dismiss();
        }
    }*/

    public void blurImg(ArrayList<Uri> list) throws IOException {
        for( int i =0; i< list.size(); i++){
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), list.get(i));
            Bitmap outputBitmap = Bitmap.createBitmap(bitmap);
            RenderScript renderScript = RenderScript.create(this);
            ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
            Allocation output = Allocation.createFromBitmap(renderScript, outputBitmap);
            intrinsicBlur.setRadius(25);
            intrinsicBlur.setInput(input);
            intrinsicBlur.forEach(output);
            output.copyTo(outputBitmap);

            Uri uri = bitmapToUri(getApplicationContext(), outputBitmap);
            normalList.add(uri);
        }
    }

    public void unblurImg(ArrayList<Uri> list) throws IOException {
        for( int i =0; i< list.size(); i++){
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), list.get(i));
            RenderScript renderScript = RenderScript.create(this);
            ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            intrinsicBlur.setRadius(0.1f);
            Allocation input = Allocation.createFromBitmap(renderScript, bitmap);
            Type type = input.getType();
            Allocation output = Allocation.createTyped(renderScript, type);
            intrinsicBlur.setInput(input);
            intrinsicBlur.forEach(output);
            Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), bitmap.getConfig());
            output.copyTo(outputBitmap);

            Uri uri = bitmapToUri(getApplicationContext(), outputBitmap);
            normalList.add(uri);
        }
    }

    public Uri bitmapToUri(Context context, Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }
}