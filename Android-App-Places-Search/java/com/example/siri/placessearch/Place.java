package com.example.siri.placessearch;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Place {

    @SerializedName("place_id")
    private String placeId;
    @SerializedName("icon")
    private String placeIcon;
    @SerializedName("vicinity")
    private String placeVicinity;
    @SerializedName("name")
    private String placeName;

    private transient boolean isChecked = false;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceIcon() {
        return placeIcon;
    }

    public void setPlaceIcon(String placeIcon) {
        this.placeIcon = placeIcon;
    }

    public String getPlaceVicinity() {
        return placeVicinity;
    }

    public void setPlaceVicinity(String placeVicinity) {
        this.placeVicinity = placeVicinity;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public boolean isFavoritePlace() {
        return false;
    }

}

class Places {
    @SerializedName("next_page_token")
    private String nextPageToken;
    @SerializedName("places")
    private List<Place> places;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}