package DB;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageReader {

    public static Bitmap imageToBitmap(Context c, String imgname) {
        AssetManager assetManager = c.getAssets();
        try {
            InputStream istr = assetManager.open("images/" + imgname + ".png");
            Bitmap map = BitmapFactory.decodeStream(istr);
            istr.close();
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            stream.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] imageToByte(Context c, String imgname) {
        return bitmapToByte(imageToBitmap(c, imgname));
    }

    public static Bitmap byteArrayToBitmap(byte[] image) {
        //turn byte[] to bitmap
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
