package com.example.siri.placessearch;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // each data item is just a string in this case
    public TextView placeName;
    public TextView placeVicinity;
    public CheckBox favoritePlace;
    public ImageView placeIcon;
    public View layout;

    private WeakReference<ClickListener> listenerRef;

    public PlacesViewHolder(View view, ClickListener clickListener) {
        super(view);
        layout = view;
        placeIcon = view.findViewById(R.id.placeIcon);
        placeName = view.findViewById(R.id.placeName);
        placeVicinity = view.findViewById(R.id.placeAddress);
        favoritePlace = view.findViewById(R.id.favoritePlaceCheckBox);

        listenerRef = new WeakReference<>(clickListener);

        view.setOnClickListener(this);
        favoritePlace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                listenerRef.get().onCheckBoxClicked(getAdapterPosition(), b);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                listenerRef.get().onPositionClicked(getAdapterPosition());
                Intent intent = new Intent(view.getContext(), PlaceActivity.class);
                view.getContext().startActivity(intent);
                break;
        }
    }
}
