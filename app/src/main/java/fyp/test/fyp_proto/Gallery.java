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
import android.location.LocationManager;
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
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Uri> arrayList = new ArrayList<Uri>();
    ArrayList<Uri> tempImgList = new ArrayList<Uri>();
    ArrayList<UploadGallery> newarrayList = new ArrayList<UploadGallery>();
    ArrayList<UploadGallery> tempList = new ArrayList<UploadGallery>();
    ArrayList<Uri> moveFilesList = new ArrayList<Uri>();
    ArrayList<String> moveFilesList2 = new ArrayList<String>();
    ArrayList<String> updatedList = new ArrayList<String>();
    ArrayList<Uri> normalList = new ArrayList<Uri>();

    ArrayList<String> filePath = new ArrayList<>();
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

        galleryOptions = findViewById((R.id.gallery_menu_btn));
        imagePickBtn = findViewById(R.id.add_gallery_btn);
        cameraBtn = findViewById(R.id.camera_btn);
        recyclerView = findViewById(R.id.gallery_recycler_view);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        newadapter = new RetrieveAdp(Gallery.this,newarrayList);
        recyclerView.setAdapter(newadapter);
        newadapter.setOnItemClickListener(Gallery.this);

        //firebase instance
        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mStorage.setMaxDownloadRetryTimeMillis(60000);  // wait 1 min for downloads
        mStorage.setMaxOperationRetryTimeMillis(10000);  // wait 10s for normal ops
        mStorage.setMaxUploadRetryTimeMillis(120000);  // wait 2 mins for uploads
        ref = mStorage.getReference();
        urlsList = new ArrayList<>();

        //Get Gallery From Firestore;
        db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").orderBy("name", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list){
                    UploadGallery obj = d.toObject(UploadGallery.class);
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

        numOfPass();

        galleryOptions.setOnClickListener(view->{

            PopupMenu pm = new PopupMenu(Gallery.this, galleryOptions);
            pm.getMenu().add("Main Page");
            pm.getMenu().add("Set or Update Folder password");
            if(num > 0){
                pm.getMenu().add("Delete Folder password");
            }

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

                    if(menuItem.getTitle() == "Set or Update Folder password"){
                        startActivity(new Intent(Gallery.this,setPassGallery.class));
                        return true;
                    }

                    if(menuItem.getTitle() == "Delete Folder password"){
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
            String[] strings = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //Check condition
            if(EasyPermissions.hasPermissions(this,strings)){
                takePicture();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Handles the request result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //Check condition
        if(resultCode == RESULT_OK && data!= null) {
            //When activity contain data
            //Check condition
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                //When request for photo
                //Initialize array list
                arrayList = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                tempImgList.addAll(arrayList);

                UploadImages(tempImgList);
                deleteFile(arrayList);
            }
            arrayList.clear();
        }
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
                                    urlsList.add(uri);
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
                                    urlsList.add(uri);
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
        for (int i =0; i < UriList.size(); i++){
            Uri uri = UriList.get(i);
            String newUri = encrypt(uri.toString(), currentUser.getUid());
            model = new UploadGallery(imgName, newUri, "");
            UploadGallery finalModel = model;
            db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    finalModel.setDocId(documentReference.getId());
                    db.collection("Gallery").document(currentUser.getUid()).collection("My Gallery").document(finalModel.getDocId())
                                    .set(finalModel, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
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
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String getCropFileExtension(Uri uri){
        return MimeTypeMap.getFileExtensionFromUrl(uri.toString());
    }

    public static void scanFile(Context context, String[] path) {
        MediaScannerConnection.scanFile(context.getApplicationContext(), path, null, null);
    }

    private void deleteFile(ArrayList<Uri> hidingList){
        for (int i =0; i < hidingList.size(); i++) {
            ContentResolver resolver = this.getContentResolver();
            resolver.delete(hidingList.get(i), null, null);
            //resolver.delete(hidingList.get(i), MediaStore.Images.Media.DATA + "=?", new String[]{f.getAbsolutePath()});
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            this.sendBroadcast(intent);
            scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()});
        }
    }

    @Override
    public void onItemClick(int position) {
        UploadGallery selectedItem = newarrayList.get(position);
        String password = selectedItem.getPass();
        if(password != ""){
            Intent intent = new Intent(Gallery.this, enter_image_pass.class);
            intent.putExtra("password", password);
            intent.putExtra("image", selectedItem.getImageUrl());
            intent.putExtra("docId", selectedItem.getDocId());
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(Gallery.this, FullImage.class);
            intent.putExtra("password", password);
            intent.putExtra("image", selectedItem.getImageUrl());
            intent.putExtra("docId", selectedItem.getDocId());
            startActivity(intent);
        }
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

    @Override
    public void onResetPassClick(int position) {
        UploadGallery selectedItem = newarrayList.get(position);
        Intent intent = new Intent(Gallery.this, ResetPassGalleryFile.class);
        intent.putExtra("docId", selectedItem.getDocId());
        startActivity(intent);
    }

    public void createImage(Context context, Bitmap bitmap){
        ContentResolver cr = getContentResolver();
        String title = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String savedURL = MediaStore.Images.Media.insertImage(cr, bitmap, title, title);
        Toast.makeText(Gallery.this, savedURL, Toast.LENGTH_SHORT).show();
    }
}