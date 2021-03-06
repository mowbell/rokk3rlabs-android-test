package com.rokk3rlabs.rockkerlabsbrandssearch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Mauricio on 06/04/2016.
 */
public class AppDatabaseTable {
    private static final String DATABASE_NAME = "ROKK3R_LABS_BRANDS";

    private static final String FTS_VIRTUAL_TABLE_BRAND = "FTS_BRAND";

    public static final String COL_NAME = "NAME";

    public static final String COL_BRAND_NAME = "BRAND";
    public static final String COL_CLOTHING_NAME = "CLOTHING_TYPE";

    private static final String FTS_VIRTUAL_TABLE_CLOTHING = "FTS_CLOTHING_TYPE";

    private static final String FTS_VIEW_MIXED= "FTS_MIXED";

    private static final int DATABASE_VERSION = 1;

    public final DatabaseOpenHelper mDatabaseOpenHelper;

    public AppDatabaseTable(Context context) {
        this.mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private static final String FTS_TABLE_BRAND_CREATE =
                "CREATE TABLE " + FTS_VIRTUAL_TABLE_BRAND +
                        " ( _id, " +
                        COL_BRAND_NAME + ")";

        private static final String FTS_TABLE_CLOTHING_TYPE_CREATE =
                "CREATE TABLE " + FTS_VIRTUAL_TABLE_CLOTHING +
                        " (_id, " +
                        COL_CLOTHING_NAME + ")";

        private static final String FTS_VIEW_MIXED_CREATE =
                "CREATE VIEW IF NOT EXISTS " + FTS_VIEW_MIXED +
                        " AS SELECT "+COL_BRAND_NAME+" , "+COL_CLOTHING_NAME+" FROM "+FTS_VIRTUAL_TABLE_BRAND+" INNER JOIN "+FTS_VIRTUAL_TABLE_CLOTHING;

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        public DatabaseOpenHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_BRAND_CREATE);


            mDatabase.execSQL("INSERT INTO "+FTS_VIRTUAL_TABLE_BRAND+ " ("+COL_BRAND_NAME+") VALUES ('Gap'),('Banana Republic'),('Boss'),('Hugo Boss'),('Taylor'),('Rebecca Taylor')");

            /*ContentValues initialValues = new ContentValues();
            initialValues.put(COL_NAME, "GAP");
            mDatabase.insert(FTS_TABLE_BRAND_CREATE, null, initialValues);*/

            mDatabase.execSQL(FTS_TABLE_CLOTHING_TYPE_CREATE);

            mDatabase.execSQL("INSERT INTO "+FTS_VIRTUAL_TABLE_CLOTHING+ " ("+COL_CLOTHING_NAME+") VALUES ('Denim'),('Pants'),('Sweaters'),('Skirts'),('Dresses')");

            Log.v("MOW","QUERY VIEW "+FTS_VIEW_MIXED_CREATE);
            mDatabase.execSQL(FTS_VIEW_MIXED_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP VIEW IF EXISTS " + FTS_VIEW_MIXED);
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_BRAND);
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE_CLOTHING);

        }


    }





    public Cursor getWord(String brand) {
        //String selection = COL_NAME + " LIKE ? OR "+KEY_DEFINITION + " LIKE ? ";
        /*String selection = COL_NAME + " LIKE ? ";
        String[] selectionArgs = new String[] {"%"+brand+"%"};*/

        /*String selection = "instr(?,"+COL_NAME+") AS resultado";
        String[] selectionArgs = new String[] {brand};
        String[] columns = new String[]{"resultado"};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE_BRAND);
        Log.v("MOW Query", builder.buildQuery(columns, selection, selectionArgs, null, null, null, null));
        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);*/


        //String[] columns = new String[]{COL_NAME, "length(trim(replace('"+brand+"',"+COL_NAME+",''))) AS resultado"};


        String[] columns = new String[]{"length('"+brand+"') as A", "length(replace('"+brand+"',"+COL_BRAND_NAME+",'')) AS B", "length(replace('"+brand+"',"+COL_CLOTHING_NAME+",'')) AS C",COL_BRAND_NAME,COL_CLOTHING_NAME};


        String selection = "(length(replace('"+brand+"',"+COL_BRAND_NAME+",'')) != length('"+brand+"')) || (length(replace('"+brand+"',"+COL_CLOTHING_NAME+",'')) != length('"+brand+"'))";

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIEW_MIXED);
        Log.v("MOW Query", builder.buildQuery(columns, selection, null, null, null, null, null));
        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, null, null, null, null);
        return cursor;
    }






    public Cursor getResults(String brand) {


        String[] columns = new String[]{
                "(SELECT "+COL_BRAND_NAME+" FROM "+FTS_VIRTUAL_TABLE_BRAND+" WHERE (length(replace('"+brand+"',"+COL_BRAND_NAME+",'')) != length('"+brand+"')) LIMIT 1) AS BRAND_FOUND ",
                "(SELECT "+COL_CLOTHING_NAME+" FROM "+FTS_VIRTUAL_TABLE_CLOTHING+" WHERE (length(replace('"+brand+"',"+COL_CLOTHING_NAME+",'')) != length('"+brand+"')) LIMIT 1) AS CLOTHING_FOUND "
        };


        //String selection = "(length(replace('"+brand+"',"+COL_BRAND_NAME+",'')) != length('"+brand+"')) || (length(replace('"+brand+"',"+COL_CLOTHING_NAME+",'')) != length('"+brand+"'))";

        /*SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        //builder.setTables(FTS_VIEW_MIXED);
        Log.v("MOW Query", builder.buildQuery(columns, null, null, null, null, null, null));
        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, null, null, null, null, null);*/
        /*String query="SELECT NULL AS _id, '"+brand+"' AS QUERY, "+COL_BRAND_NAME+" BRAND_FOUND, "+COL_CLOTHING_NAME+" CLOTHING_FOUND ";
        query+=" FROM ( SELECT "+COL_BRAND_NAME+", "+COL_CLOTHING_NAME+" FROM (SELECT "+COL_BRAND_NAME+" FROM "+FTS_VIRTUAL_TABLE_BRAND+" ";
        query+="               WHERE (length(replace('"+brand+"',"+COL_BRAND_NAME+",'')) != length('"+brand+"') ) ) JOIN ";
        query+="      (SELECT "+COL_CLOTHING_NAME+" FROM "+FTS_VIRTUAL_TABLE_CLOTHING+" ";
        query+="                WHERE (length(replace('"+brand+"',"+COL_CLOTHING_NAME+",'')) != length('"+brand+"'))  )  )";
        Log.v("MOW Query", query);*/

        String query="SELECT NULL AS _id, '"+brand+"' AS QUERY, (SELECT "+COL_BRAND_NAME+" FROM "+FTS_VIRTUAL_TABLE_BRAND+" WHERE (length(replace('"+brand+"',"+COL_BRAND_NAME+",'')) != length('"+brand+"')) LIMIT 1) AS BRAND_FOUND, "+ "(SELECT "+COL_CLOTHING_NAME+" FROM "+FTS_VIRTUAL_TABLE_CLOTHING+" WHERE (length(replace('"+brand+"',"+COL_CLOTHING_NAME+",'')) != length('"+brand+"')) LIMIT 1) AS CLOTHING_FOUND ";
        Log.v("MOW Query", query);
        Cursor cursor = mDatabaseOpenHelper.getReadableDatabase().rawQuery(query, null);

        return cursor;
    }

}
