package com.example.ipharm.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.ipharm.Models.Medicine;

public class DbMedicinesHelper extends SQLiteOpenHelper
{
    private static final String TAG = "DbMedicinesHelper";

    private static final String DATABASE_NAME = "medicines1.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_MEDICINES ="MEDICINES_TABLE";

    private static final String COL0 = "idM";
    private static final String COL1 = "nameM";
    private static final String COL2 = "laboFabM";
    private static final String COL3 = "phoneNumberLabo";
    private static final String COL4 = "presentBottle";
    private static final String COL5 = "initConst";
    private static final String COL6 = "minConst";
    private static final String COL7 = "maxConst";
    private static final String COL8 = "priceM";
    private static final String COL9 = "remainingPart";
    private static final String COL10 = "stability";
    private static final String COL11 = "quantity";

    public DbMedicinesHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql ="create table " +
                TABLE_NAME_MEDICINES + " ( " +
                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT," +
                COL2 + " TEXT," +
                COL3 + " TEXT, " +
                COL4 + " DOUBLE, " +
                COL5 + " DOUBLE, " +
                COL6 + " DOUBLE, " +
                COL7 + " DOUBLE, " +
                COL8 + " DOUBLE, " +
                COL9 + " DOUBLE, " +
                COL10 + " INTEGER, " +
                COL11 + " INTEGER ) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String delete_query = "DROP table "+TABLE_NAME_MEDICINES;
        db.execSQL(delete_query);
        onCreate(db);
    }

    /*
     * Insert new patient into the database.
     * */
    public boolean addMedicine(Medicine medicine)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1 ,medicine.getNameM());
        contentValues.put(COL2 ,medicine.getLaboFabM());
        contentValues.put(COL3 ,medicine.getPhoneNumberLabo());
        contentValues.put(COL4 ,medicine.getPresentBottle());
        contentValues.put(COL5 ,medicine.getInitConst());
        contentValues.put(COL6 ,medicine.getMinConst());
        contentValues.put(COL7 ,medicine.getMaxConst());
        contentValues.put(COL8 ,medicine.getPriceM());
        contentValues.put(COL9 ,medicine.getRemainingPartM());
        contentValues.put(COL10 ,medicine.getStabilityM());
        contentValues.put(COL11 ,medicine.getQuantityM());

        long result = db.insert(TABLE_NAME_MEDICINES,null,contentValues);

        if (result == -1 )
        {
            db.close();
            return false;
        }else
        {
            db.close();
            return true;
        }
    }

    /*
     * Retrieve all medicines from database.
     * */
    public Cursor getAllMedicines()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+ TABLE_NAME_MEDICINES, null);
    }

    /*
     * Update the patient where id = @pram 'id'.
     * */
    public boolean updateMedicine(Medicine medicine, int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1 ,medicine.getNameM());
        contentValues.put(COL2 ,medicine.getLaboFabM());
        contentValues.put(COL3 ,medicine.getPhoneNumberLabo());
        contentValues.put(COL4 ,medicine.getPresentBottle());
        contentValues.put(COL5 ,medicine.getInitConst());
        contentValues.put(COL6 ,medicine.getMinConst());
        contentValues.put(COL7 ,medicine.getMaxConst());
        contentValues.put(COL8 ,medicine.getPriceM());
        contentValues.put(COL9 ,medicine.getRemainingPartM());
        contentValues.put(COL10 ,medicine.getStabilityM());
        contentValues.put(COL11 ,medicine.getQuantityM());

        int update = db.update(TABLE_NAME_MEDICINES,contentValues, COL0 + "=?", new String[]{String.valueOf(id)});

        if (update != 1)
        {
            return false;
        } else
        {
            return true;
        }
    }

    /*
     * Retrieve the medicine unique ID from the database.
     * */
    public Cursor getMedicineId(Medicine medicine)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_MEDICINES +
                " WHERE " + COL1 + " = '" + medicine.getNameM() + "'" +
                " AND " + COL3 + " = '" + medicine.getPhoneNumberLabo() +"'";

        return db.rawQuery(sql, null);
    }

    /*
     * Delete patient from the database.
     * */
    public Integer deleteMedicine (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_MEDICINES,"idM =?", new String[]{String.valueOf(id)});
    }

}
