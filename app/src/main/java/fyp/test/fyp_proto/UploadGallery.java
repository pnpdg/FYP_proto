package fyp.test.fyp_proto;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.net.URI;

public class UploadGallery {
    private String mName;
    private String mImageUrl;
    private String docID;

    public UploadGallery(){
        //empty constructor needed
    }

    public UploadGallery(String name, String imageUrl){

        mName = name;
        mImageUrl = imageUrl;
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
}
