package com.rwin.tag.ui.layout;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import com.rwin.tag.datamodel.Marker;

public class MarkerOverlayItem extends OverlayItem {

    private Marker marker;

    public MarkerOverlayItem(Marker m) {
        super(m.owner.name, "", new GeoPoint(m.lat, m.lon));
        this.marker = m;
    }
}
