package md.sjd.androidutils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Sajjad Mistri on 24-01-2017.
 * sjd753@gmail.com
 */

public class AppFileManager {

    public static synchronized File makeAppDir(String appName) {
        File storageDir = Environment.getExternalStorageDirectory();

        File appDir = new File(storageDir + "/" + appName);

        boolean exists = appDir.exists();

        if (!exists)
            exists = appDir.mkdir();
        if (exists)
            return appDir;

        return null;
    }

    public static synchronized File createImageFile(String appName) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File appDir = makeAppDir(appName);

        if (appDir != null) {
            return File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    appDir      /* directory */
            );
        }
        return null;
    }

    public static synchronized File copyFileToAppDir(String appName, Uri fileUri) throws Exception {
        File appDir = makeAppDir(appName);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_" + fileUri.getLastPathSegment();

        File saveFile = new File(appDir, imageFileName);

        FileInputStream inStream = new FileInputStream(new File(fileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

        return saveFile;
    }

    public static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteRecursive(child);
            return fileOrDirectory.delete();
        }
        return false;
    }

    public static boolean deleteDirectory(File fileOrDir) {
        if (fileOrDir.exists()) {
            if (fileOrDir.isDirectory()) {
                File[] files = fileOrDir.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        return file.delete();
                    }
                }
            }
            return fileOrDir.delete();
        }
        return false;
    }

    public static synchronized long downloadFile(Context context, String appName, String url, String filename, String extension) {
        Uri uri = Uri.parse(url);
        //The file to be downloaded
        File file = new File(makeAppDir(appName), filename + "." + extension);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle(filename + "." + extension);
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading " + filename + "." + extension + " to " + file.getPath());
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationUri(Uri.fromFile(file));

        request.allowScanningByMediaScanner();

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        //Enqueue a new download and same the referenceId
        return downloadManager != null ? downloadManager.enqueue(request) : 0;
    }
}
