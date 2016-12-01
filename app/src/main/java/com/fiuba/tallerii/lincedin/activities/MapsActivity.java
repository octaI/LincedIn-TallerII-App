package com.fiuba.tallerii.lincedin.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import static com.fiuba.tallerii.lincedin.R.string.location;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int ACCESS_COARSE_GPS = 23;
    private static final int ACCESS_FINE_GPS = 24;
    private static final long MIN_TIME_BW_UPDATES = 60 * 1000 * 2;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static double lat;
    private static double lo;
    private boolean permission;
    private GoogleMap mMap;
    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        locationManager  = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_COARSE_GPS);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_GPS);
        }
        permission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},ACCESS_COARSE_GPS);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},ACCESS_FINE_GPS);
        }

        if (!isLocationEnabled()){
            showAlert();
        }
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

           if (!isNetworkEnabled || !isGPSEnabled || permission) {
               lat = -34.6037;
               lo = -58.3816;
           } else {
               if (isNetworkEnabled && locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                   lat = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
                   lo = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
                   LincedInRequester.sendUserCoordinates(getApplicationContext(), new Response.Listener<JSONObject>() {
                               @Override
                               public void onResponse(JSONObject response) {
                                   Toast.makeText(getApplicationContext(), "Guardando su nueva localización.", Toast.LENGTH_SHORT).show();
                                   Log.d("GEOLOC", "Succesfully sent coordinates");
                                   finish();

                               }
                           },
                           new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {
                                   error.printStackTrace();
                                   Toast.makeText(getApplicationContext(),"Error al enviar su ubicación.Revisa tu conexión.",Toast.LENGTH_SHORT).show();

                               }
                           },new LatLng(lat,lo));
               }

               if (isGPSEnabled && permission) {
                   if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                       lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                       lo = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                       LincedInRequester.sendUserCoordinates(getApplicationContext(), new Response.Listener<JSONObject>() {
                                   @Override
                                   public void onResponse(JSONObject response) {
                                       Toast.makeText(getApplicationContext(), "Guardando su nueva localización.", Toast.LENGTH_SHORT).show();
                                       Log.d("GEOLOC", "Succesfully sent coordinates");
                                       finish();

                                   }
                               },
                               new Response.ErrorListener() {
                                   @Override
                                   public void onErrorResponse(VolleyError error) {
                                       error.printStackTrace();
                                       Toast.makeText(getApplicationContext(),"Error al enviar su ubicación.Revisa tu conexión.",Toast.LENGTH_SHORT).show();

                                   }
                               },new LatLng(lat,lo));
                   }
               }
           }
        mMap = googleMap;
        LatLng initialPos = new LatLng(lat,lo);
        mMap.addMarker(new MarkerOptions().position(initialPos).title("Aquí está usted"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(initialPos));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                LincedInRequester.sendUserCoordinates(getApplicationContext(), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "Guardando su nueva localización.", Toast.LENGTH_SHORT).show();
                                Log.d("GEOLOC", "Succesfully sent coordinates");
                                finish();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(),"Error al enviar su ubicación.Revisa tu conexión.",Toast.LENGTH_SHORT).show();

                            }
                        },latLng);
            }

        });



    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Habilitar localizacinó")
                .setMessage("Sus opciones de localización están deshabilitadas.\nPor favor habilitelas " +
                        "para usar esta funcionalidad")
                .setPositiveButton("Opciones de localización", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


}
