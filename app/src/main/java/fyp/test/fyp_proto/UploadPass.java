package fyp.test.fyp_proto;

public class UploadPass {
    private String pass;
    private String docID;

    public UploadPass(){
        //empty constructor needed
    }

    public UploadPass(String password){

        pass = password;
    }

    public String getPass(){
        return pass;
    }

    public void setPass(String password){
        pass = password;
    }

    public String getDocId(){
        return docID;
    }

    public void setDocId(String documentID){
        docID = documentID;
    }
}
