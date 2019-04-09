package com.afh.busbay.acitivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afh.busbay.R;
import com.afh.busbay.models.User;
import com.afh.busbay.utils.AppUtils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.afh.busbay.utils.AppUtils.isCurrentUserFaculty;
import static com.afh.busbay.utils.AppUtils.saveUserToSharedPref;
import static com.afh.busbay.utils.FirebaseConstants.BRANCH_USERS;

public class SplashActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "SplashActivity";

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.prgb_splash);

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                loginCheck();
                            }
                        });
                    }
                }, 2000);
    }
    private void loginCheck() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            if (AppUtils.isCurrentUserFaculty(this)) {

                startActivity(new Intent(SplashActivity.this, FacultyHomeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, StudentHomeActivity.class));
            }
            finish();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {
                progressBar.setVisibility(View.VISIBLE);
                fetchUserInfo();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(getString(R.string.sign_in_cancelled));
                    waitAndTerminate();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(getString(R.string.no_internet_connection));
                    waitAndTerminate();
                    return;
                }
                showSnackbar(getString(R.string.unknown_error));
                Log.e(TAG, "Sign-in error: ", response.getError());
                waitAndTerminate();
            }
        }
    }

    private void fetchUserInfo() {
        if (FirebaseAuth.getInstance().getUid() != null) {
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference(BRANCH_USERS).child(FirebaseAuth.getInstance().getUid());
            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User loggedInUser = dataSnapshot.getValue(User.class);
                    if (loggedInUser != null) {
                        loggedInUser.setUserId(FirebaseAuth.getInstance().getUid());
                        saveUserToSharedPref(loggedInUser, SplashActivity.this);
                        if (isCurrentUserFaculty(SplashActivity.this)) {
                            startActivity(new Intent(SplashActivity.this, FacultyHomeActivity.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashActivity.this, StudentHomeActivity.class));
                            finish();
                        }
                    } else {
                        showSnackbar("Invalid user");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showSnackbar("Login process was interrupted or failed!");
                }
            });
        } else {
            showSnackbar("User is not logged in!");
        }


    }

    private void showSnackbar(String message) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.cord_lay_home_parent),
                message, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }

    private void waitAndTerminate() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        //if you need some code to run when the delay expires
                        finish();
                    }
                }, 1500);
    }
}
