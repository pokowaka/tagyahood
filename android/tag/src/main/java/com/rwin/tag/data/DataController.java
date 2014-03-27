package com.rwin.tag.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mapbox.mapboxsdk.format.GeoJSON;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
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

    public String getLocationUrl(BoundingBox tile) {
        if (tile == null)
            return "";

        return String.format(baseUrl + "/v1/marker?latn=%f&lonw=%f&lats=%f&lone=%f", tile.getLatNorth(), tile.getLonWest(),
        tile.getLatSouth(),tile.getLonEast());
    }

    public void loadArtPieceIntoView(String id, Context context,
            ImageView artView) {
        ArtPiece art = this.model.getArt(id);
        if (art != null) {
            Picasso.with(context).load(art.url).into(artView);
        }
    }

}
