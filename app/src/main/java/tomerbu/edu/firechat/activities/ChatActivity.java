package tomerbu.edu.firechat.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tomerbu.edu.firechat.R;
import tomerbu.edu.firechat.models.Topic;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etMessage;
    RecyclerView rvChat;
    private FirebaseUser currentUser;
    private ChatTopicAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        etMessage = findViewById(R.id.etMessage);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fab.setOnClickListener(this);
        rvChat = findViewById(R.id.rvChatTopics);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatTopicAdapter();
        rvChat.setAdapter(adapter);
        read();
    }

    @Override
    public void onClick(View v) {
        //save data:
        //1) create a model class (best practice)
        //2) ref to a table. -> push()
        DatabaseReference newTopic = FirebaseDatabase.getInstance().getReference("Topics").push();
        //3) init the model class
        String title = etMessage.getText().toString();
        Topic topic = new Topic(title, newTopic.getKey(), currentUser);
        //4) setValue
        newTopic.setValue(topic);
    }

    private void read() {
        //1) ref to a table.
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("Topics");
        //2)ref -> addListener
        topicsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot d, String s) {
                Topic topic = d.getValue(Topic.class);//Simillar to casting, actually Firebase does Parsing for us...
                data.add(topic);
                adapter.notifyItemInserted(data.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ArrayList<Topic> data = new ArrayList<>();

    //inflater, data
    public class ChatTopicAdapter extends RecyclerView.Adapter<ChatTopicAdapter.ChatViewHolder> {
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.chat_item, parent, false);
            return new ChatViewHolder(v);
        }

        public void onBindViewHolder(ChatViewHolder holder, int position) {
            Topic topic = data.get(position);
            holder.tvChatTopic.setText(topic.getTitle());
        }

        public int getItemCount() {
            return data.size();
        }

        //findViewById
        public class ChatViewHolder extends RecyclerView.ViewHolder {
            TextView tvChatTopic;

            public ChatViewHolder(View v) {
                super(v);
                tvChatTopic = v.findViewById(R.id.tvTopicItem);
            }
        }
    }
}
