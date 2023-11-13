package com.example.unal.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import java.util.List;
import java.util.ArrayList;
public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    public DatabaseManager(Context ctx){
        context = ctx;
    }

    public DatabaseManager open() throws SQLException {
        this.dbHelper = new DatabaseHelper(context);
        this.database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void insert(DataEmpresa dataEmpresa){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.NOMBRE_EMPRESA,dataEmpresa.nombreEmpresa);
        contentValues.put(DatabaseHelper.URLE,dataEmpresa.urlE);
        contentValues.put(DatabaseHelper.TELEFONO,dataEmpresa.telefono);
        contentValues.put(DatabaseHelper.EMAIL,dataEmpresa.email);
        contentValues.put(DatabaseHelper.PRODUCTOS,dataEmpresa.productos);
        contentValues.put(DatabaseHelper.CLASIFICACION,dataEmpresa.clasificacion);

        database.insert(DatabaseHelper.DATABASE_TABLE,null,contentValues);
    }
    public Cursor fetch(String nombreEmpresaFiltro, String clasificacionFiltro){
        String [] columns = new String[] {DatabaseHelper.USER_ID,
                DatabaseHelper.NOMBRE_EMPRESA,DatabaseHelper.URLE,
                DatabaseHelper.TELEFONO,DatabaseHelper.EMAIL,
                DatabaseHelper.PRODUCTOS,DatabaseHelper.CLASIFICACION
        };

        String selection = null;
        List<String> selectionArgsList = new ArrayList<>();

        if (nombreEmpresaFiltro != "") {
            selection = DatabaseHelper.NOMBRE_EMPRESA + " LIKE ?";
            selectionArgsList.add("%" + nombreEmpresaFiltro + "%");
        }

        if (clasificacionFiltro != "") {
            if (selection != null) {
                selection += " AND ";
            }
            selection += DatabaseHelper.CLASIFICACION + " LIKE ?";
            selectionArgsList.add("%" + clasificacionFiltro + "%");
        }

        String[] selectionArgs = selectionArgsList.toArray(new String[0]);

        Cursor cursor = database.query(DatabaseHelper.DATABASE_TABLE, columns, selection, selectionArgs, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public int update(long _id,DataEmpresa dataEmpresa){
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.NOMBRE_EMPRESA,dataEmpresa.nombreEmpresa);
        contentValues.put(DatabaseHelper.URLE,dataEmpresa.urlE);
        contentValues.put(DatabaseHelper.TELEFONO,dataEmpresa.telefono);
        contentValues.put(DatabaseHelper.EMAIL,dataEmpresa.email);
        contentValues.put(DatabaseHelper.PRODUCTOS,dataEmpresa.productos);
        contentValues.put(DatabaseHelper.CLASIFICACION,dataEmpresa.clasificacion);

        int ret = database.update(DatabaseHelper.DATABASE_TABLE,contentValues,DatabaseHelper.USER_ID+"="+_id,null);
        return ret;
    }

    public void delete(long _id){
        database.delete(DatabaseHelper.DATABASE_TABLE,DatabaseHelper.USER_ID+"="+_id,null);
    }
}
