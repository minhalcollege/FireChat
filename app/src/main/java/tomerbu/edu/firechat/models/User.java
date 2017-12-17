package tomerbu.edu.firechat.models;

/**
 * Created by TomerBu on 17/12/2017.
 */

public class User {
    private String email;
    private String uid;
    private String userId;
    //lastSeen...
    //Display name

    //ctor
    public User(String email, String uid, String userId) {
        this.email = email;
        this.uid = uid;
        this.userId = userId;
    }

    //Required! always a good idea...
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
