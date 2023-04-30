package fyp.test.fyp_proto;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.net.URI;

public class UploadGallery {
    private String mName;
    private String mImageUrl;
    private String docID;
    private String pass;

    public UploadGallery(){
        //empty constructor needed
    }

    public UploadGallery(String name, String imageUrl, String password){

        mName = name;
        mImageUrl = imageUrl;
        pass = password;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }

    public String getDocId(){
        return docID;
    }

    public void setDocId(String documentID){
        docID = documentID;
    }

    public String getPass(){
        return pass;
    }

    public void setPass(String password){
        pass = password;
    }
}
