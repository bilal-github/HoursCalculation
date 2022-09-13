package com.example.hourscalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String HOURS_DATA = "Hoursdata";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private ContentValues _contentValues;

    public DBHelper(Context context) {
        super(context, HOURS_DATA + ".db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "" +
                "CREATE TABLE " + HOURS_DATA + "(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "DATE TEXT NOT NULL," +
                "NUMBER_OF_HOURS REAL NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE if exists " + HOURS_DATA);
    }

    public Boolean insert(LocalDate date, Float numberOfHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        _contentValues = new ContentValues();
        _contentValues.put("DATE", date.format(DATE_FORMATTER));
        _contentValues.put("NUMBER_OF_HOURS", numberOfHours);

        long result;
        try {
            result = db.insertOrThrow(HOURS_DATA, null, _contentValues);
        } catch (SQLException e) {
            throw e;
        }

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean update(LocalDate date, Float numberOfHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        _contentValues = new ContentValues();
        String dateAsString = date.format(DATE_FORMATTER);
        _contentValues.put("DATE", dateAsString);
        _contentValues.put("NUMBER_OF_HOURS", numberOfHours);

        try (Cursor cursor = db.rawQuery("SELECT * from " + HOURS_DATA + " where DATE = ?", new String[]{dateAsString})) {
            if (cursor.getCount() > 0) {
                long result = db.update(HOURS_DATA, _contentValues, "Date = ?", new String[]{dateAsString});
                return result == 1;
            }
        } catch (RuntimeException e) {
            throw e;
        }
        return false;
    }

    public Boolean delete(String date, Float numberOfHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * from " + HOURS_DATA + " where DATE = ?", new String[]{date})) {
            if (cursor.getCount() > 0) {
                long result = db.delete(HOURS_DATA, "Date = ?", new String[]{date});

                return result == 1;
            }
        } catch (RuntimeException e) {
            throw e;
        }
        return false;
    }

    public Boolean dateExists(LocalDate date) {
        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT * from " + HOURS_DATA + " where DATE = ?",
                new String[]{date.format(DATE_FORMATTER)})) {
            if (cursor.getCount() > 0) {
                return true;
            }
        } catch (RuntimeException e) {
            throw e;
        }
        return false;
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * from " + HOURS_DATA, null);
    }
}
