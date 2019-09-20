package com.example.siri.placessearch;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SearchResultsAdapter mAdapter;
    private List<Place> input;
    private PlaceDBHandler dbHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);
        dbHandler = new PlaceDBHandler(getContext());

        recyclerView = view.findViewById(R.id.placesRecyclerView);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        input = new ArrayList<>(dbHandler.loadPlaces());

        mAdapter = new SearchResultsAdapter(getContext(), input, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
            }

            @Override
            public void onCheckBoxClicked(int position, boolean isChecked) {
                if (!isChecked) {
                    Toast.makeText(getContext(), input.get(position).getPlaceName() + " removed from favorites.", Toast.LENGTH_SHORT).show();
                    dbHandler.deletePlace(input.get(position).getPlaceId());
                    mAdapter.notifyItemRemoved(position);
                    input.remove(position);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);

        return view;

    }
}
