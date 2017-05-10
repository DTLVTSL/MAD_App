package com.example.mad.lab2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
/*
import static android.R.attr.phoneNumber;
import static com.example.mad.lab2.R.id.email;
import static com.example.mad.lab2.R.id.name;
import static com.example.mad.lab2.R.id.password;
*/


/**
 * A login screen that offers login via email/password.
 */
public class login_new_user extends AppCompatActivity {

    private static final String TAG = "AndroidBash";
    private User user;
    private EditText name;
    private EditText phoneNumber;
    private EditText email;
    private EditText password;
    private Firebase mRef = new Firebase(Config.FIREBASE_URL);
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    //private login_new_user.UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private ProgressDialog mProgressDialog;

    private Button NewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //DANIEL
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener;

        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.login);

        NewUser = (Button) findViewById(R.id.newuser);
        NewUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(login_new_user.this, Autentification_old_user.class));
                startActivity(new Intent(login_new_user.this, Autentification_old_user.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        name =          (EditText) findViewById(R.id.edit_text_username);
        phoneNumber =   (EditText) findViewById(R.id.edit_text_phone_number);
        email =         (EditText) findViewById(R.id.edit_text_new_email);
        password =      (EditText) findViewById(R.id.edit_text_new_password);
        user = new User();

        Log.d(TAG, "ON START:" + name.getText().toString());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //This method sets up a new User by fetching the user entered details.
    protected void setUpUser() {
        user.setName(name.getText().toString());
        user.setPhoneNumber(phoneNumber.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    public void onSignUpClicked(View view) {
        createNewAccount(email.getText().toString(), password.getText().toString());
        //showProgressDialog();
    }

    private void createNewAccount(String email, String password) {
        Log.d(TAG, "createNewAccount:" + email);
        if (!validateForm()) {
            Toast.makeText(login_new_user.this, getString(R.string.form),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //This method sets up a new User by fetching the user entered details.
        setUpUser();
        //This method  method  takes in an email address and password, validates them and then creates a new user
        // with the createUserWithEmailAndPassword method.
        // If the new account was created, the user is also signed in, and the AuthStateListener runs the onAuthStateChanged callback.
        // In the callback, you can use the getCurrentUser method to get the user's account data.

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        //hideProgressDialog();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Toast.makeText(login_new_user.this, "Authentication failed",
                            Toast.makeText(login_new_user.this, getString(R.string.aut_failed),
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            Log.d(TAG, "getresult:" + task.getResult().toString());
                            Log.d(TAG, "getresult:" + task.getResult().getUser().toString());
                            Log.d(TAG, "getresult:" + task.getResult().getUser().getUid().toString());

                            /*onAuthenticationSucess(task.getResult().getUser());*/
                            onAuthenticationSucess(task.getResult().getUser());
                        }
                    }
                });
    }

    //This method, validates email address and password
    private boolean validateForm() {
        boolean valid = true;

        String userEmail = email.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            email.setError(getString(R.string.error));
            valid = false;
        } else {
            //email.setError(null);
            boolean em = isEmailValid(userEmail);
            if (!em){
                email.setError(getString(R.string.error_invalid_email));
                valid = false;
            }else {
                email.setError(null);
            }
        }

        String userPassword = password.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            password.setError(getString(R.string.error));
            valid = false;
        } else {
            //password.setError(null);
            boolean ps = isPasswordValid(userPassword);
            if (!ps){
                password.setError(getString(R.string.error_invalid_password));
                valid = false;
            }else {
                password.setError(null);
            }
        }

        String userPhon = phoneNumber.getText().toString();
        if (TextUtils.isEmpty(userPhon)) {
            phoneNumber.setError(getString(R.string.error));
            valid = false;
        } else {
            phoneNumber.setError(null);
        }

        String usernam = name.getText().toString();
        if (TextUtils.isEmpty(usernam)) {
            name.setError(getString(R.string.error));
            valid = false;
        } else {
            name.setError(null);
        }


        return valid;
    }//End validate Form

    private void onAuthenticationSucess(FirebaseUser mUser) {
        saveNewUser(mUser.getUid(), user.getName(), user.getPhoneNumber(), user.getEmail(), user.getPassword());
        signOut();

        //startActivity(new Intent(login_new_user.this, Autentification_old_user.class).putExtra("UserID", mUser.getUid().toString()));
        startActivity(new Intent(login_new_user.this, Autentification_old_user.class).putExtra("UserID", mUser.getUid().toString()));
        finish();
    }

    private void saveNewUser(String userId, String name, String phone, String email, String password) {
        User user = new User(userId,name,phone,email,password);

        mRef.child("Users").child(userId).setValue(user);
    }

    private void signOut() {
        mAuth.signOut();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}