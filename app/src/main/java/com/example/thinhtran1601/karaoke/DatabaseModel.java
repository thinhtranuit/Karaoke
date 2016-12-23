package com.example.thinhtran1601.karaoke;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by thinh on 23/12/2016.
 */
public class DatabaseModel {
    private static final String DB_Path = "/databases/";

    /**
     * This method check there is DATABASE_NAME in system, if there isn't copy it from assests
     *
     * @param context       such as Activity
     * @param DATABASE_NAME is a String name of Database file
     */
    public static void setupDatabase(Context context, String DATABASE_NAME) {
        File dbFile = new File(context.getApplicationInfo().dataDir + DB_Path + DATABASE_NAME);
        if (!dbFile.exists()) {
            copyDatabaseFromAsset(context, DATABASE_NAME);
        }
    }

    /**
     * This method copy Database from assests to system
     *
     * @param context       such as Activity
     * @param DATABASE_NAME is a String name of Database file
     */
    private static void copyDatabaseFromAsset(Context context, String DATABASE_NAME) {
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = context.getApplicationInfo().dataDir + DB_Path + DATABASE_NAME;

            // if the path doesn't exist first, create it
            File f = new File(context.getApplicationInfo().dataDir + DB_Path);
            if (!f.exists()) {
                f.mkdir();
            }

            OutputStream outputStream = new FileOutputStream(outFileName);

            //
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
        }
    }

    /**
     * @param database  SQLiteDatabase connect to database
     * @param tableName database table name
     * @param songs     an arraylist contains all item
     * @param num       varangs number is positon of variable in database table
     */
    public static void loadItemFromDatabase(SQLiteDatabase database,
                                            String tableName,
                                            ArrayList<Song> songs,
                                            int... num) {
        String querry = "select * from " + tableName;
        Cursor cursor = database.rawQuery(querry, null);
        while (cursor.moveToNext()) {
            String code = cursor.getString(num[0]);
            String nameOfSong = cursor.getString(num[1]);
            String singer = cursor.getString(num[2]);
            int isLiked = cursor.getInt(num[3]);
            if (isLiked == 0) {
                Song newSong = new Song(code, nameOfSong, singer);
                songs.add(newSong);
            } else {
                Song newSong = new Song(code, nameOfSong, singer, true);
                songs.add(newSong);
            }
        }
        cursor.close();
    }
}
