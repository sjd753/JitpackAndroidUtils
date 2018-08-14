package md.sjd.androidutils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sajjad Mistri on 24-01-2017.
 */

public class BitmapHelper {

    public static synchronized File bitmapToFile(Bitmap bitmap, File dir) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        String suffix = ".jpg";

        //create a file to write bitmap data
        File file = File.createTempFile(imageFileName, suffix, dir);

        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapData = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();

        return file;
    }

    public static synchronized Bitmap generateThumbnail(String filePath) {
        return ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
    }

    public static File saveCompressedBitmap(Bitmap bitmap, int quality, String path) {
        File imageFile = new File(path);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(BitmapHelper.class.getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    public static int findOrientation(File file) {
        int orientation;
        ExifInterface ei;
        try {
            ei = new ExifInterface(file.getAbsolutePath());
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.e("Orientation", orientation + "");
        } catch (IOException e) {
            e.printStackTrace();
            orientation = -1;
            Log.e(BitmapHelper.class.getSimpleName(), "Photo does not exists", e);
        }
        return orientation;
    }

    public static Bitmap rotateBitmap(Bitmap source, int orientation) {
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                if (source != null)
                    return RotateBitmap(source, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                if (source != null)
                    return RotateBitmap(source, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                if (source != null)
                    return RotateBitmap(source, 270);
            default:
                return source;
        }
    }

    private static Bitmap RotateBitmap(Bitmap source, float angle) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            Log.e(BitmapHelper.class.getSimpleName(), "OOM..please try with smaller image", e);
        }
        return source;
    }
}
