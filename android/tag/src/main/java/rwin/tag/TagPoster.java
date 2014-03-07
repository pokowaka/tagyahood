package com.rwin.tag;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * A TagLoader posts a Tag up to the server
 * 
 * @author erwinj
 * 
 */
public class TagPoster {
    private static final String TAG = "com.rwin.randy.TagLoader";

    AsyncHttpClient myClient = new AsyncHttpClient();
    String url;

    public TagPoster(String url) {
        this.url = url;
    }

    public void createUser(String name, String passwd, byte[] data) {
        InputStream myInputStream = new ByteArrayInputStream(data);
        RequestParams params = new RequestParams();
        params.put("picture", myInputStream);
        params.put("name", name);
        params.put("passwd", passwd);
        String uri = url + "/v1/user";

        myClient.post(uri, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.json.JSONObject response) {
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
}
