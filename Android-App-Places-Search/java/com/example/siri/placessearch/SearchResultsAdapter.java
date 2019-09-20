package com.example.siri.placessearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<PlacesViewHolder> {
    private List<Place> places;
    private ClickListener clickListener;
    private Context mContext;
    private PlaceDBHandler dbHandler;


    public void add(int position, Place item) {
        places.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        places.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchResultsAdapter(Context context, List<Place> myDataset, ClickListener clickListener) {
        mContext = context;
        this.clickListener = clickListener;
        places = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_place, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PlacesViewHolder vh = new PlacesViewHolder(v, clickListener);
        dbHandler = new PlaceDBHandler(mContext);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final PlacesViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Place place = places.get(position);
        holder.placeName.setText(place.getPlaceName());
        holder.placeVicinity.setText(place.getPlaceVicinity());

        if (dbHandler.getPlace(place.getPlaceId()) == null) {
            holder.favoritePlace.setChecked(false);
        } else {
            holder.favoritePlace.setChecked(true);
        }

        ImageRequest request = new ImageRequest(place.getPlaceIcon(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        holder.placeIcon.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        holder.placeIcon.setImageResource(R.drawable.ic_search);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return places.size();
    }

}

class RetrievePlaceIcon extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bm = null;
        InputStream is = null;
        try {
            URL aURL = new URL(urls[0]);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            return bm;
        } catch (IOException e) {

        } catch (Exception e) {
            Log.e("PLACE_ICON_ERROR", "Error getting bitmap", e);
            return null;
        } finally {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bm) {

    }
}
