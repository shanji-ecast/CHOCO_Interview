package android.interview.drama.database;

import android.content.ContentUris;
import android.media.tv.TvContract;
import android.net.Uri;

public class DramaContract {

    public static final String AUTHORITY = "android.interview.drama.provider";

    public static final String PATH_DRAMA = "dramas";


    public static Uri buildDramaUri(long dramaId) {
        return ContentUris.withAppendedId(Dramas.CONTENT_URI, dramaId);
    }

    public static final class Dramas {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/dramas");

        /**
         * The MIME type of a directory of Dramas.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.android.interview.provider.dramas";

        /**
         * The MIME type of a single Dramas.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.android.interview.provider.dramas";

        public static final String _ID = "_id";
        public static final String COLUMN_DRAMA_ID = "drama_id";
        public static final String COLUMN_NAME = "drama_name";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_TOTAL_VIEWS = "total_views";
        public static final String COLUMN_THUMBNAIL = "thumbnail";

        public static final String[] PROJECTION = getProjection();

        private static String[] getProjection() {

            return new String[]{
                    _ID,
                    COLUMN_DRAMA_ID,
                    COLUMN_NAME,
                    COLUMN_CREATED_AT,
                    COLUMN_RATING,
                    COLUMN_TOTAL_VIEWS,
                    COLUMN_THUMBNAIL,
            };
        }
    }
}
