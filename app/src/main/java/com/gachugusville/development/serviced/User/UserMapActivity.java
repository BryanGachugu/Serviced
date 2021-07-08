package com.gachugusville.development.serviced.User;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.gachugusville.development.serviced.R;
import com.gachugusville.development.serviced.Utils.Provider;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class UserMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location lastLocation;
    Provider provider;
    LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);
        getAssignedProvider();
        //drawRoute();
    }

    private Marker providerMarker;

    private void getAssignedProvider() {

        Intent intent = getIntent();
        provider = (Provider) intent.getSerializableExtra("provider_data");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Providers").document(provider.getDocumentId())
                .addSnapshotListener((value, error) -> {
                    double providerLat;
                    double providerLong;

                    if (error != null) {
                        Log.d("LocationListenFailed.", error.getLocalizedMessage());
                        Toast.makeText(UserMapActivity.this, "Connection Failed", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (value != null && value.exists()) {
                        providerLat = value.getDouble("latitude");
                        providerLong = (double) value.get("longitude");
                        LatLng providerLatLong = new LatLng(providerLat, providerLong);
                        Log.d("FetchedLat", String.valueOf(providerLat));
                        provider.setLatitude(providerLat);
                        provider.setLongitude(providerLong);
                        Log.d("FetchedLong", String.valueOf(providerLong));
                        providerMarker = mMap.addMarker(new MarkerOptions().position(providerLatLong).title(provider.getBrand_name() + provider.getUser_name()));
                    }
                });
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("NEWLAT", String.valueOf(location.getLatitude()));
        Log.d("NEWLONG", String.valueOf(location.getLongitude()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /*
    TODO  UNCOMMENT THE BELOW FUNCTION ONCE YOU ADDED BILLING ACCOUNT  TO GOOGLE CLOUD
    TODO REMOVE THE API KEY
     */

    /*
    public void drawRoute() {
        GoogleDirection.withServerKey("AIzaSyCMdgQOCZ-DfLRKgQswbOUfs2aaKe49rTI")
                .from(new LatLng(provider.getLatitude(), -provider.getLongitude()))
                .to(new LatLng(User.getInstance().getLatitude(), -User.getInstance().getLongitude()))
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(@Nullable @org.jetbrains.annotations.Nullable Direction direction) {
                        if (direction != null && direction.isOK()) {
                        } else {
                            Toast.makeText(UserMapActivity.this, "Route draw error", Toast.LENGTH_SHORT).show();
                            assert direction != null;
                            Log.d("RouteErrorONSUCCESS", direction.getErrorMessage());
                        }
                    }

                    @Override
                    public void onDirectionFailure(@NonNull @NotNull Throwable t) {
                        Log.d("RouteError", t.getLocalizedMessage());
                    }
                });
    }
     */
}