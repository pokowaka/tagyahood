package com.rwin.tag.data;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.rwin.tag.util.geocoding.GeocodeRequest;

public class ReverseGeocoder {

    public interface GeocodeReceiver {
        public void received(final double lat, final double lon,
                GeocodeRequest gr);
    }

    private static final ReverseGeocoder instance = new ReverseGeocoder();

    private AsyncHttpClient httpClient = DataController.getInstance()
            .getHttpClient();
    private static final String NOMATIM_URL = "http://open.mapquestapi.com/nominatim/v1/reverse.php?format=json";

    public static ReverseGeocoder getInstance() {
        return instance;
    }

    public void reverseGeocode(final double lat, final double lon,
            final GeocodeReceiver receiver) {
        if (receiver == null)
            return;

        RequestParams params = new RequestParams();
        params.add("lat", String.valueOf(lat));
        params.add("lon", String.valueOf(lon));

        httpClient.get(NOMATIM_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                    String responseBody) {
                GeocodeRequest result = GeocodeRequest.parse(responseBody);
                receiver.received(lat, lon, result);
            }
        });

    }
}
