package com.afh.busbay.acitivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afh.busbay.R;
import com.afh.busbay.adapters.TravelDetailsAdapter;
import com.afh.busbay.models.FeeDetails;
import com.afh.busbay.models.TravelDetails;
import com.afh.busbay.utils.AppUtils;
import com.afh.busbay.utils.FirebaseUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StudentHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StudentHome";

    private FloatingActionButton fabLocateBus;
    private ArrayList<TravelDetails> travelDetailsArrayList;
    private RecyclerView travelDetailsRecycView;
    private TravelDetailsAdapter travelDetailsAdapter;
    private TextView feesPaidTxtView;
    private TextView feesBalanceTxtView;
    private TextView userNameTxtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        initialize();
        fetchFeeDetails();
        fetchTravelDetails();
    }

    private void fetchTravelDetails() {
        DatabaseReference travelDetailsReference = FirebaseDatabase.getInstance().getReference(FirebaseUtils.getUserExpensesPath(FirebaseAuth.getInstance().getUid()));
        travelDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    TravelDetails travelDetails = snapshot.getValue(TravelDetails.class);
                    if (travelDetails != null) {
                        travelDetails.setDate(snapshot.getKey());
                        travelDetailsArrayList.add(travelDetails);
                    }
                }
                Collections.reverse(travelDetailsArrayList);
                travelDetailsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchFeeDetails() {
        DatabaseReference feeDetailsReference = FirebaseDatabase.getInstance().getReference(FirebaseUtils.getFeeDetailsPath(FirebaseAuth.getInstance().getUid()));
        feeDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FeeDetails feeDetails = dataSnapshot.getValue(FeeDetails.class);
                if (feeDetails != null) {
                    DecimalFormat decimalFormat = new DecimalFormat(".##");


                    feesPaidTxtView.setText(String.valueOf(feeDetails.getPaid()));
                    feesBalanceTxtView.setText(decimalFormat.format(feeDetails.getBalance()));
                } else {
                    displayFeeDetailsFetchError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                displayFeeDetailsFetchError();
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void displayFeeDetailsFetchError() {
        feesPaidTxtView.setText("Couldn't fetch");
        feesBalanceTxtView.setText("Couldn't fetch");
    }

    private void initialize() {
        fabLocateBus = findViewById(R.id.fab_locate_bus);
        userNameTxtView = findViewById(R.id.txt_view_user_name);
        String loggedInUser = "Logged in as " + AppUtils.getCurrentUser(this);
        userNameTxtView.setText(loggedInUser);
        fabLocateBus.setOnClickListener(this);
        travelDetailsRecycView = findViewById(R.id.rec_view_travel_details);
        feesPaidTxtView = findViewById(R.id.txt_view_fees_paid);
        feesBalanceTxtView = findViewById(R.id.txt_view_fees_balance);
        travelDetailsArrayList = new ArrayList<>();
        travelDetailsAdapter = new TravelDetailsAdapter();
        travelDetailsAdapter.setTravelDetailsArrayList(travelDetailsArrayList);
        LinearLayoutManager travelDetailsLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        travelDetailsRecycView.setLayoutManager(travelDetailsLayoutManager);
        travelDetailsRecycView.setAdapter(travelDetailsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about:
                // Show about us screen
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.menu_logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(StudentHomeActivity.this, SplashActivity.class));
                                finish();
                            }
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == fabLocateBus.getId()) {
            startActivity(new Intent(this, BusTrackerActivity.class));
        }
    }
}
