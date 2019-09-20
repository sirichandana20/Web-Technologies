package com.example.siri.placessearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PlaceDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "placeDB.db";
    public static final String TABLE_NAME = "place";

    public PlaceDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(place_id VARCHAR, place_icon VARCHAR, place_name VARCHAR, place_vicinity VARCHAR)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Place> loadPlaces() {
        List<Place> result = new ArrayList<>();
        String query = "Select * FROM " + TABLE_NAME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Place place = new Place();
            place.setPlaceId(cursor.getString(cursor.getColumnIndex("place_id")));
            place.setPlaceName(cursor.getString(cursor.getColumnIndex("place_name")));
            place.setPlaceIcon(cursor.getString(cursor.getColumnIndex("place_icon")));
            place.setPlaceVicinity(cursor.getString(cursor.getColumnIndex("place_vicinity")));
            result.add(place);
        }
        cursor.close();
        db.close();
        return result;
    }

    public void addPlace(Place place) {
        ContentValues values = new ContentValues();

        values.put("place_id", place.getPlaceId());
        values.put("place_icon", place.getPlaceIcon());
        values.put("place_name", place.getPlaceName());
        values.put("place_vicinity", place.getPlaceVicinity());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    public Place getPlace(String place_id) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE place_id = " + "'" + place_id + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Place place = new Place();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            place.setPlaceId(cursor.getString(cursor.getColumnIndex("place_id")));
            place.setPlaceName(cursor.getString(cursor.getColumnIndex("place_name")));
            place.setPlaceIcon(cursor.getString(cursor.getColumnIndex("place_icon")));
            place.setPlaceVicinity(cursor.getString(cursor.getColumnIndex("place_vicinity")));
            cursor.close();
        } else {
            place = null;
        }
        db.close();
        return place;
    }

    public boolean deletePlace(String place_id) {
        boolean result = false;
        String query = "DELETE FROM " + TABLE_NAME + " WHERE place_id = "+ "'" + place_id + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();
        db.close();
        return result;
    }
}
