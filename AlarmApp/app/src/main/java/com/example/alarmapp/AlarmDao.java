package com.example.alarmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class AlarmDao {

    private SQLiteDatabase db;

    public AlarmDao(Context context) {
        AlarmDbHelper dbHelper = new AlarmDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insertAlarm(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(AlarmDbHelper.COLUMN_HOUR, alarm.getHour());
        values.put(AlarmDbHelper.COLUMN_MINUTE, alarm.getMinute());
        values.put(AlarmDbHelper.COLUMN_ENABLED, alarm.isEnabled() ? 1 : 0);
        return db.insert(AlarmDbHelper.TABLE_NAME, null, values);
    }

    public int updateAlarm(Alarm alarm) {
        ContentValues values = new ContentValues();
        values.put(AlarmDbHelper.COLUMN_HOUR, alarm.getHour());
        values.put(AlarmDbHelper.COLUMN_MINUTE, alarm.getMinute());
        values.put(AlarmDbHelper.COLUMN_ENABLED, alarm.isEnabled() ? 1 : 0);
        return db.update(AlarmDbHelper.TABLE_NAME, values, AlarmDbHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(alarm.getId())});
    }

    public void deleteAlarm(int id) {
        db.delete(AlarmDbHelper.TABLE_NAME, AlarmDbHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<>();
        Cursor cursor = db.query(AlarmDbHelper.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDbHelper.COLUMN_ID));
            int hour = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDbHelper.COLUMN_HOUR));
            int minute = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDbHelper.COLUMN_MINUTE));
            boolean isEnabled = cursor.getInt(cursor.getColumnIndexOrThrow(AlarmDbHelper.COLUMN_ENABLED)) == 1;

            alarms.add(new Alarm(id, hour, minute, isEnabled));
        }
        cursor.close();
        return alarms;
    }
}
