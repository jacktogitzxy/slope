package slope.zxy.com.rtmp;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 17120 on 2018/12/4.
 */

public class Constantplay {

    public static String getsPicturePath(Context context) {
        return  context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/EasyPlayer";
    }



    public static String getsMoviePath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) + "/EasyPlayer";
    }
    public static File url2localPosterFile(Context context, String url) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] result = messageDigest.digest(url.getBytes());
            return new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Base64.encodeToString(result, Base64.NO_WRAP | Base64.URL_SAFE));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
}
