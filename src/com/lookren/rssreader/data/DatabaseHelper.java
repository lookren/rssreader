package com.lookren.rssreader.data;

import com.lookren.rssreader.model.Post;
import com.lookren.rssreader.model.Subscription;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public final class DatabaseHelper extends SQLiteOpenHelper {

    public static final String[] TABLE_NAMES = new String[] {Columns.SubscriptionColumns.TABLE_NAME, Columns.PostColumns.TABLE_NAME};
    public static final String sDbName = "Rss.db";
    public static final int DATABASE_VERSION = 3;
    Context mContext;
    private SQLiteDatabase mDatabase;

    private static DatabaseHelper sInstance;

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, sDbName, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSubscriptionTable(db);
        createPostTable(db);
        createDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String each : TABLE_NAMES) {
            db.execSQL("drop table if exists " + each);
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public synchronized SQLiteDatabase getDatabase() {
        if (mDatabase != null) {
            return mDatabase;
        }

        mDatabase = getWritableDatabase();
        return mDatabase;
    }

    private static void createSubscriptionTable(SQLiteDatabase db) {
        String columns = " (" + Columns._ID + " integer primary key autoincrement, "
                + Columns.SubscriptionColumns.DISPLAY_NAME + " text, "
                + Columns.SubscriptionColumns.URL + " text "
                + ");";
        db.execSQL("create table " + Columns.SubscriptionColumns.TABLE_NAME + columns);
    }

    private static void createPostTable(SQLiteDatabase db) {
        String columns = " (" + Columns._ID + " integer primary key autoincrement, "
                + Columns.PostColumns.DISPLAY_NAME + " text, "
                + Columns.PostColumns.SUBSCRIPTION + " integer, "
                + Columns.PostColumns.DESCRIPTION + " text, "
                + Columns.PostColumns.LINK + " text "
                + ");";
        db.execSQL("create table " + Columns.PostColumns.TABLE_NAME + columns);
    }

    private static void createDefaultData(SQLiteDatabase db) {
        Subscription subscription = new Subscription("Rolling Stone", "http://www.rollingstone.com/siteServices/rss/movieReviews");
        db.insert(Columns.SubscriptionColumns.TABLE_NAME, null, subscription.toContentValues());

        subscription = new Subscription("PCWorld", "http://www.pcworld.com/index.rss");
        db.insert(Columns.SubscriptionColumns.TABLE_NAME, null, subscription.toContentValues());

        subscription = new Subscription("National Geographic", "http://news.nationalgeographic.com/index.rss");
        db.insert(Columns.SubscriptionColumns.TABLE_NAME, null, subscription.toContentValues());
    }

    public Cursor getSubscriptions(String whereClause, String[] whereArgs, String orderBy) {
        return getDatabase()
                .query(Columns.SubscriptionColumns.TABLE_NAME, Columns.SubscriptionColumns.CONTENT_PROJECTION,
                        whereClause, whereArgs,
                        null, null, orderBy);
    }

    public Cursor getPosts(String whereClause, String[] whereArgs, String orderBy) {
        return getDatabase()
                .query(Columns.PostColumns.TABLE_NAME, Columns.PostColumns.CONTENT_PROJECTION,
                        whereClause, whereArgs,
                        null, null, orderBy);
    }

    public List<Subscription> getSubscripitonList() {
        Cursor cursor = null;
        List<Subscription> result = new ArrayList<Subscription>();
        try {
            cursor = getSubscriptions(null, null, Subscription.ORDER_BY_ID);
            while (cursor.moveToNext()) {
                Subscription subscription = new Subscription(
                        cursor.getLong(Columns.SubscriptionColumns.COLUMN_ID_NUMBER),
                        cursor.getString(Columns.SubscriptionColumns.COLUMN_HEADER_NUMBER),
                        cursor.getString(Columns.SubscriptionColumns.COLUMN_URL_NUMBER));
                result.add(subscription);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public Subscription getSubscripiton(long id) {
        Cursor cursor = null;
        try {
            if (id > 0) {
                cursor = getSubscriptions(Subscription.WHERE_ID_CLAUSE, new String[]{String.valueOf(id)}, Subscription.ORDER_BY_ID);
            } else {
                cursor = getSubscriptions(null, null, Subscription.ORDER_BY_ID);
            }
            if (cursor.moveToFirst()) {
                return new Subscription(
                        cursor.getLong(Columns.SubscriptionColumns.COLUMN_ID_NUMBER),
                        cursor.getString(Columns.SubscriptionColumns.COLUMN_HEADER_NUMBER),
                        cursor.getString(Columns.SubscriptionColumns.COLUMN_URL_NUMBER));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public List<Post> getPostList(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = null;
        List<Post> result = new ArrayList<Post>();
        try {
            cursor = getPosts(whereClause, whereArgs, orderBy);
            while (cursor.moveToNext()) {
                Post post = new Post(
                        cursor.getLong(Columns.PostColumns.COLUMN_ID_NUMBER),
                        cursor.getString(Columns.PostColumns.COLUMN_HEADER_NUMBER),
                        cursor.getLong(Columns.PostColumns.COLUMN_SUBSCRIPTION_NUMBER),
                        cursor.getString(Columns.PostColumns.COLUMN_DESCRIPTION_NUMBER),
                        cursor.getString(Columns.PostColumns.COLUMN_LINK_NUMBER));
                result.add(post);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public Post getPost(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = getPosts(whereClause, whereArgs, orderBy);
            if (cursor.moveToFirst()) {
                return new Post(
                        cursor.getLong(Columns.PostColumns.COLUMN_ID_NUMBER),
                        cursor.getString(Columns.PostColumns.COLUMN_HEADER_NUMBER),
                        cursor.getLong(Columns.PostColumns.COLUMN_SUBSCRIPTION_NUMBER),
                        cursor.getString(Columns.PostColumns.COLUMN_DESCRIPTION_NUMBER),
                        cursor.getString(Columns.PostColumns.COLUMN_LINK_NUMBER));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public int deletePosts(String whereClause, String[] whereArgs) {
        return getDatabase().delete(Columns.PostColumns.TABLE_NAME, whereClause, whereArgs);
    }

    public long savePost(Post post, long subscriptionId) {
        ContentValues contentValues = post.toContentValues();
        contentValues.put(Post.SUBSCRIPTION, subscriptionId);
        contentValues.remove(Post._ID);
        return getDatabase().insert(Columns.PostColumns.TABLE_NAME, null, contentValues);
    }

    public long saveSubscription(Subscription subscription) {
        ContentValues contentValues = subscription.toContentValues();
        contentValues.remove(Subscription._ID);
        return getDatabase().insert(Columns.SubscriptionColumns.TABLE_NAME, null, contentValues);
    }
}
