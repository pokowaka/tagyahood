package com.rwin.tag;

import java.io.ByteArrayOutputStream;

import org.openpaper.paint.action.BrushStroke;
import org.openpaper.paint.drawing.PaperView;
import org.openpaper.paint.drawing.brush.BezierBrush;

import com.rwin.tag.data.TagPoster;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CreateTag extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag);
        Button btn = (Button) findViewById(R.id.btnCreate);
        final PaperView pv = (PaperView) findViewById(R.id.paperView);

        int stroke = (int) Math.max(pv.getWidth() * 0.2f, 8);
        BezierBrush bb = new BezierBrush(0.2f, stroke, Color.YELLOW);
        pv.getActionQueue().addAction(new BrushStroke(bb));
        
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TagPoster tp = new TagPoster(Settings.getServer());
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                pv.getBitmap().compress(CompressFormat.PNG, 75, os);
                tp.createUser("rwin", "1234", os.toByteArray());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_tag, menu);
        return true;
    }

}
