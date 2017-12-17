package tomerbu.edu.firechat.activities;


//ctrl + alt + o

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tomerbu.edu.firechat.R;
import tomerbu.edu.firechat.models.User;

/**
 * A login screen that offers login via email/password.
 * ..Save the user to the database:
 * //1)tools->firebase->add database
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etEmail, etPassword;
    Button btnLoginIn;
    Button btnCreateUser;
    ProgressBar progress;
    ViewGroup loginContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get current user (if not null -> go to chat!!!)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            Intent mainActivityInent = new Intent(this, ChatActivity.class);
            startActivity(mainActivityInent);
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnCreateUser.setOnClickListener(this);

        btnLoginIn = findViewById(R.id.btnLogin);
        btnLoginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //client side validation
                if (isEmailValid()) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    FirebaseAuth.
                            getInstance().
                            signInWithEmailAndPassword(email, password).
                            addOnSuccessListener(successListener).
                            addOnFailureListener(failureListener);
                }
            }
        });
        //alt enter -> Make LoginActivity Implement View.OnClickListener..
        progress = findViewById(R.id.progress);
        loginContainer = findViewById(R.id.loginContainer);
    }

    private void toggleProgress(boolean show) {
        //if we need to show the progressBar -> show it.
        //hide the container:
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        loginContainer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    //Create User Clicked
    @Override
    public void onClick(View view) {
        //if the email and password are valid
        if (!isEmailValid()) {
            return;
        }
        //the email and password are valid
        toggleProgress(true);
        //Finally let's do some firebase:

        //create user with email and password:
        //we have a user...!
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(getEmail(), getPassword()).
                addOnSuccessListener(successListener).
                addOnFailureListener(failureListener);
    }


    OnSuccessListener<AuthResult> successListener = new OnSuccessListener<AuthResult>() {
        @Override
        public void onSuccess(AuthResult authResult) {
            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            toggleProgress(false);

            saveUser();
        }
    };


    OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(LoginActivity.this, "Failed " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            toggleProgress(false);
        }
    };

    private String getEmail() {
        return etEmail.getText().toString();
    }

    private String getPassword() {
        return etPassword.getText().toString();
    }

    private boolean isEmailValid() {
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();
        //email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        boolean valid = email.length() > 6 && email.contains("@");

        if (!valid) {
            etEmail.setError("Invalid Email");
            return false;
        }
        valid = valid && (pass.length() > 3);
        if (!valid) {
            etPassword.setError("Must be at least 3 characters");
        }

        //if the email & password are valid -> Clear the errors.
        if (valid) {
            etEmail.setError(null);
            etPassword.setError(null);
        }
        return valid;
    }

    private void saveUser() {
        //1) get the current user (from firebase)
        FirebaseUser fireUser = FirebaseAuth.getInstance().getCurrentUser();
        //2) push id?!

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").push();

        //save:
        //in order to save -> user
        User user = new User(fireUser.getEmail(), fireUser.getUid(), usersRef.getKey());

        usersRef.setValue(user);
//        //how to save, what to save...
//
//        //Naive:
//
//        //Never save the password... (to much responsibility) (hash)
//
//        //get a reference to the database object. (no new)
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        //reference to node element:
//        DatabaseReference root = database.getReference();
//
//        //reference to a child element:
//        DatabaseReference usersRef = root.child("Users");
//
//        DatabaseReference userNewRowReference = usersRef.push();
//
//        //if Users does not exist, it will be created
//        userNewRowReference.setValue(email);
    }
}

