package com.travel.enjoyindanang.utils.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.travel.enjoyindanang.constant.Constant;
import com.travel.enjoyindanang.utils.JsonUtils;
import com.travel.enjoyindanang.utils.PermissionUtils;
import com.travel.enjoyindanang.utils.event.LocationConnectListener;
import com.travel.enjoyindanang.utils.event.OnFindLastLocationCallback;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Author: Tavv
 * Created on 27/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class LocationHelper implements PermissionUtils.PermissionResultCallback {
    private static final String TAG = LocationHelper.class.getSimpleName();
    private static final long serialVersionUID = 7536482295622776147L;

    private static final float WIDTH_PATH = 16f;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private Context context;
    private Activity current_activity;
    private boolean isPermissionGranted;

    private Location mLastLocation;

    // Google client to interact with Google API

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private FusedLocationProviderClient mFusedLocationClient;

    private SettingsClient mSettingsClient;

    private LocationSettingsRequest mLocationSettingsRequest;
    // list of permissions

    private ArrayList<String> permissions = new ArrayList<>();
    private PermissionUtils permissionUtils;

    private GoogleMap mGoogleMap;

    private final static int PLAY_SERVICES_REQUEST = 1000;
    public final static int REQUEST_CHECK_SETTINGS = 2000;

    private OnFindLastLocationCallback findCallback;

    private LocationManager mLocationManager;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    public LocationHelper(Context context, OnFindLastLocationCallback findCallback) {
        this.context = context;
        this.findCallback = findCallback;
        this.current_activity = (Activity) context;
        permissionUtils = new PermissionUtils(context, this);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);
    }


    public LocationHelper(Context context) {
        this.context = context;
        this.current_activity = (Activity) context;
        permissionUtils = new PermissionUtils(context, this);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);
    }


    public LocationHelper(Context context, OnFindLastLocationCallback findCallback, boolean needCheckPermission) {
        this.context = context;
        this.findCallback = findCallback;
        this.current_activity = (Activity) context;
        if (needCheckPermission) {
            permissionUtils = new PermissionUtils(context, this);
        }
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
    }


    /**
     * Method to check the availability of location permissions
     */

    public void checkpermission() {
        permissionUtils.check_permission(permissions, "Need GPS permission for getting your location", 1);
    }

    public boolean isPermissionGranted() {
        return isPermissionGranted;
    }

    public void setPermissionGranted(boolean hasPermission) {
        this.isPermissionGranted = hasPermission;
    }

    /**
     * Method to verify google play services on the device
     */

    public boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(current_activity, resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                showToast("This device is not supported.");
            }
            return false;
        }
        return true;
    }

    /**
     * Method to display the location on UI
     */

    public Location getLocation() {

        if (isPermissionGranted()) {

            try {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);

                return mLastLocation;
            } catch (SecurityException e) {
                e.printStackTrace();

            }

        }

        return null;

    }

    public Address getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Address getAddress(LatLng latLng) {
        if(latLng != null){
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                return addresses.get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public String getFullInfoByAddress(Address address) {
        if (address != null) {
            String addressLine = address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0).concat(", ") : "";
            String locality = StringUtils.isNotBlank(address.getLocality()) ? address.getLocality().concat(", ") : "";
            return addressLine + locality + address.getCountryName();
        }
        return StringUtils.EMPTY;
    }

    public String getFullInfoAddress(Address address) {
        if (address != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append(", ");
            }
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }


    /**
     * Method used to build GoogleApiClient
     */


    public void simpleBuildGoogleApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public synchronized GoogleApiClient buildGoogleApiStandard(LocationConnectListener locationConnectListener) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(locationConnectListener)
                    .addOnConnectionFailedListener(locationConnectListener)
                    .addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
        }
        return mGoogleApiClient;
    }

    public LocationRequest buildLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(UPDATE_INTERVAL);
            mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        }
        return mLocationRequest;
    }

    public LocationSettingsRequest.Builder buildLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(buildLocationRequest());
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        mSettingsClient = LocationServices.getSettingsClient(context);
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest);
        return builder;
    }

    /* Show Location Access Dialog */
    public void showSettingDialog(LocationConnectListener locationConnectListener) {
        if (mGoogleApiClient == null) {
            buildGoogleApiStandard(locationConnectListener);
        }
        if (mLocationRequest == null) {
            buildLocationRequest();
        }
        if (mLocationSettingsRequest == null) {
            buildLocationSettings();
        }
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(current_activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    // Trigger new location updates at interval
    public void startLocationUpdates(LocationConnectListener locationConnectListener) {
        if (mLocationRequest != null) {
            try {
                mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                        .addOnSuccessListener(current_activity, new OnSuccessListener<LocationSettingsResponse>() {
                            @Override
                            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                                //noinspection MissingPermission
                                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                        locationCallback, Looper.myLooper());
                                getLastFusedLocation();

                            }
                        });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "LocationRequest is null !");
        }
    }

    /**
     * Method used to connect GoogleApiClient
     */
    public void connectApiClient() {
        mGoogleApiClient.connect();
    }

    /**
     * Method used to get the GoogleApiClient
     */
    public GoogleApiClient getGoogleApiCLient() {
        return mGoogleApiClient;
    }


    /**
     * Handles the permission results
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Handles the activity results
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        mLastLocation = getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        EventBus.getDefault().post(Constant.LOCATION_NOT_FOUND);
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION", "GRANTED");
        isPermissionGranted = true;
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
    }


    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    public String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service


        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
    }

    public void getLastLocationClient() {
        // Get last known recent location using new Google Play Services SDK (v11+)

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLastLocation = location;
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    public LatLng onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        return new LatLng(location.getLatitude(), location.getLongitude());
    }


    public void downloadAndParse(String url) {
        getJsonLocation(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(JSONObject result) {
                        parseJsonLocation(result).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<List<HashMap<String, String>>>>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(List<List<HashMap<String, String>>> lists) {
                                        if (lists != null) {
                                            PolylineOptions polylineOptions = drawRoute(lists);
                                            if (polylineOptions != null && mGoogleMap != null) {
                                                mGoogleMap.addPolyline(polylineOptions);
                                            }
                                        }
                                    }
                                });
                    }
                });
    }

    private Observable<JSONObject> getJsonLocation(final String url) {
        return Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                try {
                    JSONObject json = JsonUtils.getJsonResponse(url);
                    subscriber.onNext(json);
                    subscriber.onCompleted();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    private Observable<List<List<HashMap<String, String>>>> parseJsonLocation(final JSONObject jsonRoute) {
        return Observable.create(new Observable.OnSubscribe<List<List<HashMap<String, String>>>>() {
            @Override
            public void call(Subscriber<? super List<List<HashMap<String, String>>>> subscriber) {
                if (jsonRoute != null) {
                    DirectionsJSONParser parser = new DirectionsJSONParser();
                    List<List<HashMap<String, String>>> result = parser.parse(jsonRoute);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable("Json is empty"));
                }
            }
        });
    }

    private PolylineOptions drawRoute(List<List<HashMap<String, String>>> resultCallback) {
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        for (int i = 0; i < resultCallback.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = resultCallback.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(WIDTH_PATH);
            lineOptions.color(Color.BLUE);
            lineOptions.geodesic(true);
        }
        return lineOptions;
    }


    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        this.mLocationRequest = locationRequest;
    }

    public LocationRequest getLocationRequest() {
        return mLocationRequest;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
    }

    public GoogleMap getGoogleMap() {
        return mGoogleMap;
    }

    public String getUrlThumbnailLocation(double longtitude, double latitude) {
        return "http://maps.google.com/maps/api/staticmap?center=" + latitude + "," + longtitude + "&zoom=18&size=600x300&sensor=false&markers=color:red|Clabel:|" + latitude + "," + longtitude;
    }

    public void stopLocationUpdates() {
        if (locationCallback != null) {
            LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(locationCallback);
        }
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            // do work here
            mLastLocation = locationResult.getLastLocation();
            findCallback.onFound(locationResult.getLastLocation());
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
        }
    };

    public void updateMarkerWithCircle(Circle circle, Marker marker, LatLng position) {
        circle.setCenter(position);
        marker.setPosition(position);
    }

    public void drawMarkerWithCircle(Circle circle, Marker marker, LatLng position) {
        double radiusInMeters = 100.0;
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        circle = mGoogleMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        marker = mGoogleMap.addMarker(markerOptions);
    }

    public void getLastFusedLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i(TAG, "" + valueResult + "   KM  " + kmInDec + " Meter   " + meterInDec);

        return Radius * c;
    }

}
