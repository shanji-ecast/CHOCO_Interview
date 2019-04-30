package android.interview.drama.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.interview.drama.model.DramaList;
import android.util.Log;

import com.google.gson.Gson;

public class Utils {

    /**
     * Save last time query keyword
     */
    public static void saveLastQuery(Context context, String keyword) {
        SharedPreferences preferences = context.getSharedPreferences("last_query", Context.MODE_PRIVATE);
        preferences.edit().putString("last_query", keyword).apply();
    }

    /**
     * Get last time query keyword
     */
    public static String getLastQuery(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("last_query", Context.MODE_PRIVATE);
        return preferences.getString("last_query", "");
    }

    /**
     * Save selected Drama Item
     */
    public static void saveSelectedDrama(Context context, DramaList.Drama drama) {
        SharedPreferences preferences = context.getSharedPreferences("selected_drama", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = null;
        if (drama != null) {
            json = gson.toJson(drama);
        }
        Log.v("TAG", "saveSelectedDrama "+json);

        preferences.edit().putString("last_query", json).apply();
    }

    /**
     * Get selected Drama Item
     */
    public static DramaList.Drama getSelectedDrama(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("selected_drama", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString("last_query", null);

        Log.v("TAG", "getSelectedDrama "+json);

        if (json != null) {
            DramaList.Drama drama = gson.fromJson(json, DramaList.Drama.class);
            return drama;

        }
        return null;
    }

    public static String covertDateString(String originTime) {
        //e.g. 2017-11-23T02:04:39.000Z
        final String pattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
        String newString = originTime.substring(0, 10);

        if(newString.matches(pattern)) {
            return newString;
        }
        return originTime;
    }
}
