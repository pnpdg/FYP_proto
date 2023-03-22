package fyp.test.fyp_proto;

public class User {

    String FullName,UserEmail,Role1,Role2,Uid;

    public User(){}

    public User(String fullName, String userEmail, String role1,String role2, String uid) {
        this.FullName = fullName;
        this.UserEmail = userEmail;
        this.Role1 = role1;
        this.Role2 = role2;
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

    public String getRole1() {
        return Role1;
    }

    public void setRole1(String role1) {
        Role1 = role1;
    }

    public String getRole2() {
        return Role2;
    }

    public void setRole2(String role2) {
        Role2 = role2;
    }

    public String getUid() {
        return  Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
