package com.example.actms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String pcTable = "pcinfo";
    private static final String tpTable = "tpinfo";
    private static final String tiTable = "tiinfo";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, "my.db", null, 2);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("kevin","DBHelper onCreate");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + pcTable + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id INTEGER, " +
                "name TEXT, " +
                "computerName TEXT, " +
                "status TEXT, " +
                "updateTime datetime, " +
                "LastTpName TEXT, " +
                "LastTpresult TEXT, " +
                "LastTpProgress TEXT " +

                ");");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + tpTable + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id INTEGER, " +
                "name TEXT, " +
                "startTime datetime, " +
                "endTime datetime, " +
                "totalSeq INTEGER, " +
                "result TEXT, " +
                "prograss TEXT " +
                ");");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + tiTable + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id int, " +
//                "testProgramRunId TEXT, " +
                "seq TEXT, " +
                "name TEXT, " +
                "extName TEXT, " +
                "startTime datetime, " +
                "endTime datetime, " +
                "result TEXT " +

                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldver, int newver) {
        Log.d("kevin","DBHelper onUpgrade");
//        if (newver > oldver) {
//            sqLiteDatabase.beginTransaction();
//
//            switch (oldver) {
//                default:
//                    oldver = newver;
//                    break;
//                case 1:
//
//                    sqLiteDatabase.execSQL(" DROP  TABLE "+ pcTable);
//                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + pcTable + "( " +
//                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                            "id INTEGER, " +
//                            "name TEXT, " +
//                            "computerName TEXT, " +
//                            "status TEXT, " +
//                            "updateTime datetime, " +
//                            "LastTpName TEXT, " +
//                            "LastTpresult TEXT, " +
//                            "LastTpProgress TEXT " +
//
//                            ");");
//                    sqLiteDatabase.execSQL(" DROP  TABLE "+ tpTable);
//                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + tpTable + "( " +
//                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                            "id INTEGER, " +
//                            "name TEXT, " +
//                            "startTime datetime, " +
//                            "endTime datetime, " +
//                            "totalSeq INTEGER, " +
//                            "prograss TEXT " +
//
//                            ");");
//                    oldver = 2;
//                    break;
//
//            }
//            if (oldver != 0)
//                sqLiteDatabase.setTransactionSuccessful();
//            sqLiteDatabase.endTransaction();
//        } else {
//            onCreate(sqLiteDatabase);
//        }
    }
}
