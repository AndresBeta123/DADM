package com.example.unal.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "DB";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_TABLE = "USER";
    static final String USER_ID = "_ID";
    static final String NOMBRE_EMPRESA = "nombreEmpresa";
    static final String URLE = "urlE";
    static final String TELEFONO = "telefono";
    static final String EMAIL = "email";
    static final String PRODUCTOS = "productos";
    static final String CLASIFICACION = "clasificacion";
    private static final String CREATE_DB_QUERY =
            "CREATE TABLE "+ DATABASE_TABLE +
                    "( "+USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +NOMBRE_EMPRESA+" TEXT NOT NULL, "
                    +URLE+" TEXT, "
                    +TELEFONO+" TEXT, "
                    +EMAIL+" TEXT, "
                    +PRODUCTOS+" TEXT, "
                    +CLASIFICACION+" TEXT"
                    +" );";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+DATABASE_TABLE);

    }
}
