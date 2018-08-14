package md.sjd.androidutils;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sajjad Mistri on 15-03-2017.
 */

public class JSONUtils {

    public static JSONArray removeObject(JSONArray jsonArray, int removeIndex) throws JSONException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            jsonArray.remove(removeIndex);
            return jsonArray;
        } else {
            JSONArray array = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                if (i != removeIndex) {
                    array.put(jsonArray.get(i));
                }
            }
            return array;
        }
    }

    public static boolean containsObject(JSONArray jsonArray, JSONObject jsonObject) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).equals(jsonObject))
                return true;
        }
        return false;
    }

    public static boolean containsContentEqualObject(JSONArray jsonArray, JSONObject jsonObject) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).toString().contentEquals(jsonObject.toString()))
                return true;
        }
        return false;
    }
}
