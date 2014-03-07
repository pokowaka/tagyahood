package com.rwin.tag.ui.maps;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;

/**
 * @author erwinj
 * 
 */
public class BitmapUtils {

	/**
	 * Creates a transparant bitmap of the given size.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createBitmap(int width, int height) {
		Bitmap empty = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// Clear the canvas and make it transparant
		Canvas canvas = new Canvas(empty);
		canvas.drawColor(Color.TRANSPARENT);
		return empty;
	}

	public static Bitmap loadTile(BitmapDrawable tile) {
		int dx = tile.getBitmap().getWidth();
		int dy = tile.getBitmap().getHeight();

		Bitmap bitmap = Bitmap.createBitmap(dx, dy, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, dx, dy);
		tile.draw(canvas);
		canvas = null;
		return bitmap;
	}

	public static Bitmap colorizeBitmap(Bitmap original, int color) {
		int dx = original.getWidth();
		int dy = original.getHeight();

		Bitmap bmpGrayscale = createBitmap(dx, dx);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);

		c.drawBitmap(original, 0, 0, paint);

		// Now let's treat the greyscale image as an alpha channel for our
		// color...
		Paint p = new Paint();
		p.setShader(new BitmapShader(bmpGrayscale, TileMode.CLAMP,
				TileMode.CLAMP));
		p.setColorFilter(new PorterDuffColorFilter(color, Mode.MULTIPLY));
		p.setXfermode(new PorterDuffXfermode(Mode.SCREEN));

		Bitmap result = createBitmap(dx, dx);
		Canvas cResult = new Canvas(result);
		cResult.drawRect(0, 0, dx, dy, p);
		cResult = null;
		c = null;
		p = null;
		bmpGrayscale.recycle();
		bmpGrayscale = null;
		return result;
	}
}
