package com.example.siri.placessearch;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SearchFragment extends Fragment implements View.OnClickListener {
    private Button searchButton, clearButton;
    private EditText keywordEditText, inputLocationText, distanceEditText;
    private TextView keywordError, inputLocationError;
    private RadioGroup locationGroup;
    private Spinner categories;

    private final String searchUrl = "http://phpenv-env.us-east-2.elasticbeanstalk.com";
    private HashMap<String, String> params = new HashMap<String, String>();

    private double latitude=0, longitude=0;

    private LocationManager locationManager;

    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchButton = view.findViewById(R.id.searchButton);
        clearButton = view.findViewById(R.id.clearButton);
        keywordEditText = view.findViewById(R.id.keyword);
        distanceEditText = view.findViewById(R.id.distance);
        inputLocationText = view.findViewById(R.id.inputLocation);
        keywordError = view.findViewById(R.id.keywordError);
        inputLocationError = view.findViewById(R.id.inputLocationError);
        locationGroup = view.findViewById(R.id.locationGroup);
        categories = view.findViewById(R.id.categories);

        searchButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        locationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.otherLocation:
                        inputLocationText.setEnabled(true);
                        break;
                    case R.id.currentLocation:
                        inputLocationText.setEnabled(false);
                        break;
                    default:
                        inputLocationText.setEnabled(false);
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        } else {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("PARAMS", "onLocationChanged: " + location);
                    params.put("lat", Double.toString(latitude));
                    params.put("lon", Double.toString(longitude));
                    params.remove("otherlocation");
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchButton:
                search();
                break;
            case R.id.clearButton:
                clear();
                break;
            default:
                break;
        }
    }

    public void search() {
        if (!isFormValid()) {
            Toast.makeText(getContext(), R.string.invalidForm, Toast.LENGTH_SHORT).show();
        } else {
            params.put("type", "places");
            params.put("kword", keywordEditText.getText().toString());
            params.put("cat", categories.getSelectedItem().toString());
            if (locationGroup.getCheckedRadioButtonId() == R.id.currentLocation) {


            } else {
                params.put("otherlocation", inputLocationText.getText().toString().toLowerCase());
            }
            params.put("dist", TextUtils.isEmpty(distanceEditText.getText().toString()) ? "10": distanceEditText.getText().toString());
            Log.d("PARAMS", "search: " + params.toString());
            JsonObjectRequest req = new JsonObjectRequest(searchUrl, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                                intent.putExtra("response", response.toString());
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(req);

        }
    }

    public void clear() {
        initState();
    }

    public void initState() {
        keywordError.setVisibility(View.GONE);
        keywordEditText.setText("");

        categories.setSelection(0);

        distanceEditText.setText("");

        inputLocationError.setVisibility(View.GONE);
        inputLocationText.setText("");
        locationGroup.check(R.id.currentLocation);

    }

    public boolean isFormValid() {
        boolean isFormValid = true;
        if (TextUtils.isEmpty(keywordEditText.getText().toString())) {
            keywordError.setVisibility(View.VISIBLE);
            isFormValid = false;
        }
        if (locationGroup.getCheckedRadioButtonId() == R.id.otherLocation && TextUtils.isEmpty(inputLocationText.getText().toString())) {
            inputLocationError.setVisibility(View.VISIBLE);
            isFormValid = false;
        }
        return isFormValid;
    }
}
