package com.rwin.tag.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rwin.tag.R;
import com.rwin.tag.data.ReverseGeocoder;
import com.rwin.tag.datamodel.Marker;
import com.rwin.tag.util.geocoding.GeocodeRequest;
import com.squareup.picasso.Picasso;

public class MarkerView extends LinearLayout {

    public MarkerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.marker_details, this);
    }

    public MarkerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.marker_details, this);
    }

    public MarkerView(Context context) {
        super(context);
        View.inflate(context, R.layout.marker_details, this);
    }

    public void setMarker(Marker m) {
        ImageView artView = (ImageView) findViewById(R.id.imgArt);
        TextView user = (TextView) findViewById(R.id.txtName);
        final TextView address = (TextView) findViewById(R.id.txtAddress);

        Picasso.with(getContext()).load(m.art.url).into(artView);
        user.setText(m.owner.name);

        ReverseGeocoder.getInstance().reverseGeocode(m.lat, m.lon,
                new ReverseGeocoder.GeocodeReceiver() {

                    @Override
                    public void received(double lat, double lon,
                            GeocodeRequest gr) {
                        address.setText(gr.getDisplay_name());
                    }
                });

    }
}
