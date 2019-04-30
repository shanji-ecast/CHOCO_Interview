package android.interview.drama.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class SampleContentProvider extends ContentProvider {



    private DbHelper mDbHelper = null;
    private SQLiteDatabase db = null;

    private static final int DRAMA = 1;
    private static final int DRAMA_ID = 2;

    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(DramaContract.AUTHORITY, "dramas", DRAMA);
        mMatcher.addURI(DramaContract.AUTHORITY, "dramas/#", DRAMA_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        db = mDbHelper.getWritableDatabase();

        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (mMatcher.match(uri)) {
            case DRAMA:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "_ID ASC";
                }

                return db.query(DramaContract.PATH_DRAMA, projection, selection, selectionArgs,
                        null, null, sortOrder, null);
            case DRAMA_ID:
                selection = selection + "_ID = " + uri.getLastPathSegment();
                return db.query(DramaContract.PATH_DRAMA, projection, selection, selectionArgs,
                        null, null, sortOrder, null);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (mMatcher.match(uri)) {
            case DRAMA:
                return DramaContract.Dramas.CONTENT_TYPE;
            case DRAMA_ID:
                return DramaContract.Dramas.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        switch (mMatcher.match(uri)) {
            case DRAMA:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                long insertId = db.insert(DramaContract.PATH_DRAMA, null, values);

                if (insertId > 0) {
                    Uri tempUri = ContentUris.withAppendedId(DramaContract.Dramas.CONTENT_URI, insertId);
                    getContext().getContentResolver().notifyChange(uri, null);

                    return tempUri;
                }
            case DRAMA_ID:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int count;
        final Context context = getContext();
        if (context == null) {
            return 0;
        }

        switch (mMatcher.match(uri)) {
            case DRAMA:
                count = db.delete(DramaContract.PATH_DRAMA, selection, selectionArgs);
                break;
            case DRAMA_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(DramaContract.PATH_DRAMA, "_id = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int count;
        final Context context = getContext();
        if (context == null) {
            return 0;
        }

        switch (mMatcher.match(uri)) {
            case DRAMA:
                count = db.update(DramaContract.PATH_DRAMA, values, selection, selectionArgs);
                break;
            case DRAMA_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(DramaContract.PATH_DRAMA, values, "_id = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
