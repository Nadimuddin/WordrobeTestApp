package com.example.wardrobetestapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.wardrobetestapp.model.Outfit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nadimuddin on 4/11/19.
 */
public class DBUtil extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WardrobeDatabase";
    private static final String OUTFIT_TABLE_NAME = "Outfit";
    private static final String FAVOURITE_TABLE_NAME = "Favourite";
    private static final String OUTFIT_ID = "outfitId";
    private static final String OUTFIT_PATH = "outfitPath";
    private static final String IS_TOP = "isTop";
    private static final String FAVOURITE_ID = "favouriteId";
    private static final String FAVOURITE_TOP = "favouriteTop";
    private static final String FAVOURITE_BOTTOM = "favouriteBottom";

    public DBUtil(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String outfitTableQuery = "create table " + OUTFIT_TABLE_NAME + " ("
                + OUTFIT_ID + " integer primary key autoincrement, "
                +  OUTFIT_PATH + " text, " + IS_TOP + " integer)";

        sqLiteDatabase.execSQL(outfitTableQuery);

        String favouriteTableQuery = "create table " + FAVOURITE_TABLE_NAME + " ("
                + FAVOURITE_ID + " integer primary key autoincrement, "
                +  FAVOURITE_TOP + " integer, " + FAVOURITE_BOTTOM + " integer)";
        sqLiteDatabase.execSQL(favouriteTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertOutfit(String path, boolean isTop) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTFIT_PATH, path);
        contentValues.put(IS_TOP, isTop ? 1 : 0);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long value = sqLiteDatabase.insert(OUTFIT_TABLE_NAME, null, contentValues);
        System.out.println("Insert outfit " + value);
        sqLiteDatabase.close();
    }

    public void insertFavourite(int topId, int bottomId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAVOURITE_TOP, topId);
        contentValues.put(FAVOURITE_BOTTOM, bottomId);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long value = sqLiteDatabase.insert(FAVOURITE_TABLE_NAME, null, contentValues);
        System.out.println("Insert outfit " + value);
        sqLiteDatabase.close();
    }

    public boolean isFavourite(int topId, int bottomId) {
        SQLiteDatabase database = getReadableDatabase();
        String query = "select * from " + FAVOURITE_TABLE_NAME + " where " + FAVOURITE_TOP + " = " + topId + " AND " + FAVOURITE_BOTTOM + " = " + bottomId;
        Cursor cursor = database.rawQuery(query, null);
        System.out.println("isFavourite count: " + cursor.getCount());
        return cursor.getCount() > 0;
    }

    public List<Outfit> getAllOutfit(Boolean isTop) {
        List<Outfit> outfits = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String query = "select * from " + OUTFIT_TABLE_NAME;
        if(isTop != null) {
            query = query + " where " + IS_TOP + " = " + (isTop ? 1 : 0);
        }
        Cursor cursor = database.rawQuery(query, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                Outfit outfit = new Outfit();
                outfit.id(cursor.getInt(cursor.getColumnIndex(OUTFIT_ID)));
                outfit.path(cursor.getString(cursor.getColumnIndex(OUTFIT_PATH)));
                outfit.isTop(cursor.getInt(cursor.getColumnIndex(IS_TOP)) == 1);
                outfits.add(outfit);
            } while (cursor.moveToNext());
        }
        return outfits;
    }

    public void deleteFavourite(int topId, int bottomId) {
        SQLiteDatabase db = getWritableDatabase();
        int value = db.delete(FAVOURITE_TABLE_NAME, FAVOURITE_TOP + " = ? AND " + FAVOURITE_BOTTOM + " = ?", new String[]{String.valueOf(topId), String.valueOf(bottomId)});
        System.out.println("Deleted rows " + value);
        db.close();
    }
}
