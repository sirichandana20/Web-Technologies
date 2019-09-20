package com.example.siri.placessearch;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchResultsFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<Place> input;
    private PlaceDBHandler dbHandler;

    public SearchResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);
        Gson gson = new GsonBuilder().create();
        Places places = gson.fromJson(getArguments().getString("response"), Places.class);
        dbHandler = new PlaceDBHandler(getContext());

        recyclerView = view.findViewById(R.id.placesRecyclerView);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        input = new ArrayList<>(places.getPlaces());
        mAdapter = new SearchResultsAdapter(getContext(), input, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
            }
            @Override
            public void onCheckBoxClicked(int position, boolean isChecked) {
                if (!isChecked) {
                    dbHandler.deletePlace(input.get(position).getPlaceId());
                    Toast.makeText(getContext(), input.get(position).getPlaceName() + " removed from favorites.", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHandler.getPlace(input.get(position).getPlaceId()) == null) {
                        Toast.makeText(getContext(), input.get(position).getPlaceName() + " added to favorites.", Toast.LENGTH_SHORT).show();
                        dbHandler.addPlace(input.get(position));
                    }
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}

interface ClickListener {
    void onPositionClicked(int position);
    void onCheckBoxClicked(int position, boolean isChecked);
}