package com.example.ipharm.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.ipharm.Models.Selled;

public class DbSalesHelpler extends SQLiteOpenHelper
{

    private static final String TAG = "DbSalesHelpler";

    private static final String DATABASE_NAME = "sales.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_SALES ="SALES_TABLE";

    private static final String COL0 = "idS";
    private static final String COL1 = "nameMS";
    private static final String COL2 = "dateOfStoringS";
    private static final String COL3 = "stabilityMS";
    private static final String COL4 = "nbBottlesS";
    private static final String COL5 = "remainingPartMS";
    private static final String COL6 = "theAmount";

    public DbSalesHelpler(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql ="create table " +
                TABLE_NAME_SALES + " ( " +
                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT," +
                COL2 + " TEXT," +
                COL3 + " INTEGER, " +
                COL4 + " INTEGER, " +
                COL5 + " DOUBLE, " +
                COL6 + " DOUBLE ) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String delete_query = "DROP table "+TABLE_NAME_SALES;
        db.execSQL(delete_query);
        onCreate(db);
    }

    /*
     * Insert new sell into the database.
     * */
    public boolean addsell(Selled sell)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1 ,sell.getNameMS());
        contentValues.put(COL2 ,sell.getDateOfStoringS());
        contentValues.put(COL3 ,sell.getStabilityMS());
        contentValues.put(COL4 ,sell.getNbBottlesS());
        contentValues.put(COL5 ,sell.getRemainingPartMS());
        contentValues.put(COL6 ,sell.getTheAmount());

        long result = db.insert(TABLE_NAME_SALES,null,contentValues);

        if (result == -1 )
        {
            return false;
        }else
        {
            return true;
        }
    }

    /*
     * Retrieve all Sales from database.
     * */
    public Cursor getAllSales()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+ TABLE_NAME_SALES, null);
    }

    /*
     * Retrieve the sell unique ID from the database.
     * */
    public Cursor getSellId(Selled sell)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_SALES +
                " WHERE " + COL1 + " = '" + sell.getNameMS() + "'" +
                " AND " + COL2 + " = '" + sell.getDateOfStoringS() +"'";

        return db.rawQuery(sql, null);
    }

    /*
     * Delete sell from the database.
     * */
    public Integer deleteSelled (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_SALES,"idS =?", new String[]{String.valueOf(id)});
    }
}
