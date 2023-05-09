package fyp.test.fyp_proto;

import com.google.firebase.Timestamp;

public class Note {
    String title;
    String content;
    String password;
    Timestamp timestamp;
    String docID;

    public Note() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String tile) {
        this.title = tile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getID(){ return docID; }

    public void setID(String id){ this.docID = id;}
}