package android.interview.drama.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.interview.drama.model.DramaList;
import android.media.tv.TvContract;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class DramaContractUtils {

    private static final String TAG = DramaContractUtils.class.getSimpleName();
    private static final boolean DEBUG = true;

    @WorkerThread
    public static void updateDramas(Context context, String inputId, List<DramaList.Drama> dramaList) {

        // Create a map from original drama ID to drama row ID for existing channels.
        SparseArray<Long> dramaMap = new SparseArray<>();

        ContentResolver resolver = context.getContentResolver();
        Uri.Builder builder = DramaContract.Dramas.CONTENT_URI.buildUpon();
        if (inputId != null) {
            builder.appendQueryParameter(DramaContract.Dramas.COLUMN_DRAMA_ID, inputId);
        }
        Uri dramaUri = builder.build();

        String[] projection = {DramaContract.Dramas._ID, DramaContract.Dramas.COLUMN_DRAMA_ID};
        Cursor cursor = null;
        try {
            cursor = resolver.query(dramaUri, projection, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                long rowId = cursor.getLong(0);
                int dramaId = cursor.getInt(1);
                dramaMap.put(dramaId, rowId);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        for (DramaList.Drama drama : dramaList) {
            ContentValues cv = new ContentValues();
            cv.put(DramaContract.Dramas.COLUMN_DRAMA_ID, drama.drama_id);

            cv.put(DramaContract.Dramas.COLUMN_NAME, drama.name);
            cv.put(DramaContract.Dramas.COLUMN_CREATED_AT, drama.created_at);
            cv.put(DramaContract.Dramas.COLUMN_RATING, drama.rating);
            cv.put(DramaContract.Dramas.COLUMN_TOTAL_VIEWS, drama.total_views);
            cv.put(DramaContract.Dramas.COLUMN_THUMBNAIL, drama.thumb);

            Long rowId = dramaMap.get(drama.drama_id);

            Uri uri;
            if (rowId == null) {
                if (DEBUG) {
                    Log.d(TAG, "insert row ");
                }
                uri = resolver.insert(DramaContract.Dramas.CONTENT_URI, cv);

            } else {
                if (DEBUG) {
                    Log.d(TAG, "update row " + rowId);
                }
                cv.put(DramaContract.Dramas._ID, rowId);
                uri = DramaContract.buildDramaUri(rowId);
                resolver.update(uri, cv, null, null);
                dramaMap.remove(drama.drama_id);
            }
            assert uri != null;
        }

        for (int i = 0; i < dramaMap.size(); ++i) {
            Long rowId = dramaMap.valueAt(i);
            if (DEBUG) {
                Log.d(TAG, "Deleting row " + rowId);
            }
            resolver.delete(DramaContract.buildDramaUri(rowId), null, null);

        }
    }

    @WorkerThread
    public static List<DramaList.Drama> getDramaList(Context context) {

        List<DramaList.Drama> dramaList = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(DramaContract.Dramas.CONTENT_URI, DramaContract.Dramas.PROJECTION, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                DramaList.Drama drama = new DramaList.Drama();

                long rowId = cursor.getLong(0);
                drama.drama_id = cursor.getInt(1);
                drama.name = cursor.getString(2);
                drama.created_at = cursor.getString(3);
                drama.rating = cursor.getDouble(4);
                drama.total_views = cursor.getInt(5);
                drama.thumb = cursor.getString(6);

                dramaList.add(drama);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return dramaList;
    }

    @WorkerThread
    public static List<DramaList.Drama> queryDramaName(Context context, String name) {
        List<DramaList.Drama> dramaList = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        String selection = DramaContract.Dramas.COLUMN_NAME + " LIKE ?" ;
        String selectionArg[] = {
                "%"+name+"%",
        };

        Cursor cursor = null;
        try {
            cursor = resolver.query(DramaContract.Dramas.CONTENT_URI, DramaContract.Dramas.PROJECTION, selection, selectionArg, null);
            while (cursor != null && cursor.moveToNext()) {
                DramaList.Drama drama = new DramaList.Drama();

                long rowId = cursor.getLong(0);
                drama.drama_id = cursor.getInt(1);
                drama.name = cursor.getString(2);
                drama.created_at = cursor.getString(3);
                drama.rating = cursor.getDouble(4);
                drama.total_views = cursor.getInt(5);
                drama.thumb = cursor.getString(6);

                dramaList.add(drama);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return dramaList;
    }

    @WorkerThread
    public static DramaList.Drama queryDramaId(Context context, int dramaId) {
        DramaList.Drama drama = null;

        ContentResolver resolver = context.getContentResolver();
        String selection = DramaContract.Dramas.COLUMN_DRAMA_ID + " = ?" ;
        String selectionArg[] = {
                Integer.toString(dramaId),
        };

        Cursor cursor = null;
        try {
            cursor = resolver.query(DramaContract.Dramas.CONTENT_URI, DramaContract.Dramas.PROJECTION, selection, selectionArg, null);
            if (cursor != null && cursor.moveToNext()) {

                drama = new DramaList.Drama();

                long rowId = cursor.getLong(0);
                drama.drama_id = cursor.getInt(1);
                drama.name = cursor.getString(2);
                drama.created_at = cursor.getString(3);
                drama.rating = cursor.getDouble(4);
                drama.total_views = cursor.getInt(5);
                drama.thumb = cursor.getString(6);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return drama;
    }
}
