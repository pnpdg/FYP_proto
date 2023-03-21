package fyp.test.fyp_proto;

public class User {

    String FullName,UserEmail,Role,Uid;

    public User(){}

    public User(String fullName, String userEmail, String role, String uid) {
        this.FullName = fullName;
        this.UserEmail = userEmail;
        this.Role = role;
        this.Uid = uid;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getUid() {
        return  Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
