package com.afh.busbay.acitivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afh.busbay.R;
import com.afh.busbay.adapters.AttendanceAdapter;
import com.afh.busbay.models.Attendance;
import com.afh.busbay.utils.FirebaseUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.afh.busbay.utils.AppConstants.ATTENDANCE_DATA_REQUEST_URL;

public class FacultyHomeActivity extends AppCompatActivity {

    private static final String TAG = "FacultyHome";

    private ArrayList<Attendance> attendanceArrayList;
    private RecyclerView attendanceRecyclerView;
    private AttendanceAdapter attendanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);
        initialize();
        requestAttendanceData();
    }

    private void initialize() {
        attendanceArrayList = new ArrayList<>();
        attendanceRecyclerView = findViewById(R.id.rec_view_attendance);
    }

    private void requestAttendanceData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String requestUrl = ATTENDANCE_DATA_REQUEST_URL + FirebaseUtils.getFormattedDate();
        Log.i(TAG, "requestAttendanceData: " + requestUrl);
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, requestUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());
                        Gson gson = new Gson();
                        String jsonString = response.toString();
                        Attendance[] attendanceList = gson.fromJson(jsonString, Attendance[].class);
                        Log.i(TAG, "onResponse: " + attendanceList[0].toString());
                        //TODO: show list in view
                        showAttendanceList(attendanceList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    private void showAttendanceList(Attendance[] attendanceList) {
        attendanceArrayList.addAll(Arrays.asList(attendanceList));
        attendanceAdapter = new AttendanceAdapter();
        attendanceAdapter.setAttendanceArrayList(attendanceArrayList);
        LinearLayoutManager attendanceLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        attendanceRecyclerView.setLayoutManager(attendanceLayoutManager);
        attendanceRecyclerView.setAdapter(attendanceAdapter);
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
                                startActivity(new Intent(FacultyHomeActivity.this, SplashActivity.class));
                                finish();
                            }
                        });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
