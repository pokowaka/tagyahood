package com.rwin.tag.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;
import org.osmdroid.util.BoundingBoxE6;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rwin.tag.datamodel.ArtPiece;
import com.rwin.tag.datamodel.Marker;
import com.rwin.tag.util.Util;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author erwinj
 * 
 */
public class DataController {

    private static final double E6 = 1000000d;

    private static final String TAG = "com.rwin.tag.data.DataModel";

    private static DataController instance = new DataController();

    private AsyncHttpClient httpClient = new AsyncHttpClient();
    private String baseUrl = "http://192.168.1.178:8080";
    private DataModel model = new DataModel();

    public AsyncHttpClient getHttpClient() {
        return httpClient;
    }

    public static DataController getInstance() {
        return instance;
    }

    public DataModel getModel() {
        return model;
    }

    public void createUser(String name, String passwd, byte[] data) {
        InputStream myInputStream = new ByteArrayInputStream(data);
        RequestParams params = new RequestParams();
        params.put("picture", myInputStream);
        params.put("name", name);
        params.put("passwd", passwd);
        String uri = baseUrl + "/v1/user";

        httpClient.post(uri, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Log.e(TAG, "Got the response: " + response);
            };

            @Override
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers, String responseBody,
                    Throwable e) {
                Log.e(TAG, "Got bad news: " + statusCode + ", responseBody: "
                        + responseBody + ", e: " + e);
            };
        });
    }

    public void updateLocation(BoundingBoxE6 tile) {
        String uri = baseUrl + "/v1/marker/";
        RequestParams params = new RequestParams();
        params.put("latn", String.valueOf(tile.getLatNorthE6() / E6));
        params.put("lonw", String.valueOf(tile.getLonWestE6() / E6));
        params.put("lats", String.valueOf(tile.getLatSouthE6() / E6));
        params.put("lone", String.valueOf(tile.getLonEastE6() / E6));
        httpClient.get(uri, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String response) {
                Log.i(TAG, "Got the response: " + response);
                // Let's try to parse the response
                try {
                    Collection<Marker> markers = Util.parse(response,
                            new TypeReference<Collection<Marker>>() {
                            });
                    for (Marker m : markers) {
                        DataController.this.model.addArt(m.art);
                    }
                    DataController.this.model.setMarkers(markers);
                } catch (Exception e) {
                    Log.e(TAG, "Cannot process response error:" + e);
                }
            };

            @Override
            public void onFailure(int statusCode,
                    org.apache.http.Header[] headers, String responseBody,
                    Throwable e) {
                Log.e(TAG, "Got bad news: " + statusCode + ", responseBody: "
                        + responseBody + ", e: " + e);
            };

        });

    }

    public void loadArtPieceIntoView(String id, Context context,
            ImageView artView) {
        ArtPiece art = this.model.getArt(id);
        if (art != null) {
            Picasso.with(context).load(art.url).into(artView);
        }
    }

}
