package android.interview.drama.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "drama_database.db";

    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DramaContract.PATH_DRAMA
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DramaContract.Dramas.COLUMN_DRAMA_ID + " INTEGER UNIQUE,"
                + DramaContract.Dramas.COLUMN_NAME + " TEXT,"
                + DramaContract.Dramas.COLUMN_TOTAL_VIEWS + " INTEGER,"
                + DramaContract.Dramas.COLUMN_RATING + " DOUBLE,"
                + DramaContract.Dramas.COLUMN_CREATED_AT + " TEXT,"
                + DramaContract.Dramas.COLUMN_THUMBNAIL + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
