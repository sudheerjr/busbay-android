package com.afh.busbay.acitivities;

import android.os.Bundle;
import android.widget.Toast;

import com.afh.busbay.R;
import com.afh.busbay.models.BusLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import static com.afh.busbay.utils.FirebaseUtils.BRANCH_BUS_LOCATION;

public class BusTrackerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String markerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_tracker);
        markerTitle = "Loading...";
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        DatabaseReference busLocationReference = FirebaseDatabase.getInstance().getReference(BRANCH_BUS_LOCATION);
        busLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BusLocation busLocation = dataSnapshot.getValue(BusLocation.class);
                if (busLocation != null) {
                    double lat = Double.parseDouble(busLocation.getLat());
                    double lng = Double.parseDouble(busLocation.getLng());
                    LatLng lastBusLocation = new LatLng(lat, lng);
                    mMap.clear();
                    markerTitle = "Bus last stopped at " + busLocation.getLocationName();
                    mMap.addMarker(new MarkerOptions().position(lastBusLocation).title(markerTitle)).showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastBusLocation, 18.0f));
                } else {
                    Toast.makeText(BusTrackerActivity.this, "Bus location service currently unavailable", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(BusTrackerActivity.this, "Bus location service currently unavailable", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
