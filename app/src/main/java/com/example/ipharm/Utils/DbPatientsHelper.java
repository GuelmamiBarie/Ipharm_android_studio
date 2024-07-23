package com.example.ipharm.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.ipharm.Models.Patient;

public class DbPatientsHelper extends SQLiteOpenHelper
{
    private static final String TAG = "DbPatientsHelper";

    private static final String DATABASE_NAME="patient.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_PATIENTS ="PATIENTS_TABLE";

    public static final String COL0="idP";
    public static final String COL1="nameP";
    public static final String COL2="phoneNumberP";
    public static final String COL3="sizeP";
    public static final String COL4="weightP";
    public static final String COL5="bodySurfaceP";
    public static final String COL6="profilePhoto";

    public DbPatientsHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql ="create table " +
                                TABLE_NAME_PATIENTS + " ( " +
                                COL0 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                COL1 + " TEXT," +
                                COL2 + " TEXT," +
                                COL3 + " DOUBLE, " +
                                COL4 + " DOUBLE, " +
                                COL5 + " DOUBLE, " +
                                COL6 + " TEXT ) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
       /* db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME_PATIENTS);
        onCreate(db);*/

        String delete_query = "DROP table "+TABLE_NAME_PATIENTS;
        db.execSQL(delete_query);
        onCreate(db);
    }

    /*
    * Insert new patient into the database.
    * */
    public boolean addPatient(Patient patient)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1 ,patient.getNameP());
        contentValues.put(COL2 ,patient.getPhonenumber());
        contentValues.put(COL3 ,patient.getSizeP());
        contentValues.put(COL4 ,patient.getWeightP());
        contentValues.put(COL5 ,patient.getBodySurfaceP());
        contentValues.put(COL6 ,patient.getProfileImageP());

        long result = db.insert(TABLE_NAME_PATIENTS,null,contentValues);

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
    * Retrieve all patient from database.
    * */
    public Cursor getAllPatients()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM "+ TABLE_NAME_PATIENTS , null);
    }

    /*
    * Update the patient where id = @pram 'id'.
    * */
    public boolean updatePatient(Patient patient, int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1 ,patient.getNameP());
        contentValues.put(COL2 ,patient.getPhonenumber());
        contentValues.put(COL3 ,patient.getSizeP());
        contentValues.put(COL4 ,patient.getWeightP());
        contentValues.put(COL5 ,patient.getBodySurfaceP());
        contentValues.put(COL6 ,patient.getProfileImageP());

        int update = db.update(TABLE_NAME_PATIENTS,contentValues, COL0 + "=?", new String[]{String.valueOf(id)});

        if (update != 1)
            return false;
        else
            return true;
    }

    /*
    * Retrieve the patient unique ID from the database.
    * */
    public Cursor getPatientId(Patient patient)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_PATIENTS +
                " WHERE " + COL1 + " = '" + patient.getNameP() + "'" +
                " AND " + COL2 + " = '" + patient.getPhonenumber() +"'";

        return db.rawQuery(sql, null);
    }

    /*
    * Delete patient from the database.
    * */
    public Integer deletePatient (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_PATIENTS,"idP =?", new String[]{String.valueOf(id)});
    }
}
