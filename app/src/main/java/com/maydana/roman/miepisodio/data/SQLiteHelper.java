package com.maydana.roman.miepisodio.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class SQLiteHelper extends SQLiteOpenHelper  {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String titulo,String lugar, String fecha,String categoria,String descripcion,String rutaImagen){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO DB_MI_EPISODIO VALUES (NULL, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,titulo);
        statement.bindString(2,lugar);
        statement.bindString(3,fecha);
        statement.bindString(4,categoria);
        statement.bindString(5,descripcion);
        statement.bindString(6,rutaImagen);
        statement.executeInsert();
    }

    public Cursor  getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);

    }

    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM DB_MI_EPISODIO WHERE id = ? ";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1,(double)id);
        statement.execute();
        database.close();
    }
    public void updateData(String titulo,String lugar, String fecha,String categoria,String descripcion,String rutaImagen,int id){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE DB_MI_EPISODIO SET titulo = ?, lugar = ?, fecha = ?, categoria = ?, descripcion = ?, rutaImagen = ? WHERE id = ?" ;
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,titulo);
        statement.bindString(2,lugar);
        statement.bindString(3,fecha);
        statement.bindString(4,categoria);
        statement.bindString(5,descripcion);
        statement.bindString(6,rutaImagen);
        statement.bindDouble(7,(double)id);
        statement.execute();
        database.close();
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
