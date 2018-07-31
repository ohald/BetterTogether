package DB;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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

    public static Bitmap base64ToScaledBitmap(String image){
        return byteArrayToScaledBitmap(Base64.decode(image, Base64.DEFAULT));
    }

    public static Bitmap byteArrayToScaledBitmap(byte[] image) {
        //turn byte[] to bitmap
        Bitmap b = BitmapFactory.decodeByteArray(image, 0, image.length);
        Bitmap scaled = Bitmap.createScaledBitmap(b, 100, 100, false);
        b.recycle();
        return scaled;
    }

}
