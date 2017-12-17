package tomerbu.edu.firechat.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by TomerBu on 17/12/2017.
 */

public class Topic {
    //in each table we also need to store the (id)
    private String title;
    private String ownerUID;
    private String ownerDisplayName;
    private String topicUID;

    //all models must:
    //1)Public getters and setters
    //2)Default constructor

    //MUST HAVE:
    public Topic() {
    }

    public Topic(String title, String topicUID, FirebaseUser user) {
        this.title = title;
        this.topicUID = topicUID;

        this.ownerUID = user.getUid();
        this.ownerDisplayName = user.getEmail();
    }

    //
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getOwnerUID() {
        return ownerUID;
    }
    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }
    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }
    public void setOwnerDisplayName(String ownerDisplayName) {
        this.ownerDisplayName = ownerDisplayName;
    }
    public String getTopicUID() {
        return topicUID;
    }
    public void setTopicUID(String topicUID) {
        this.topicUID = topicUID;
    }

    @Override
    public String toString() {
        return "Topic{" +
                "title='" + title + '\'' +
                ", ownerUID='" + ownerUID + '\'' +
                ", ownerDisplayName='" + ownerDisplayName + '\'' +
                ", topicUID='" + topicUID + '\'' +
                '}';
    }
}
