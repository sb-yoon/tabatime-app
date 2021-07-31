package Unlike.tabatmie.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TabatimeDBHelper {

    private static final String DATABASE_NAME = "unlike.tabatime.db";
    private static final int DATABASE_VER = 20;
    private static SQLiteDatabase sqliteDb = null;
    private final Context context;

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    public TabatimeDBHelper(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        if (sqliteDb == null || !sqliteDb.isOpen()) {
            DBHelper dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VER);
            sqliteDb = dbHelper.getWritableDatabase();
            Log.e("TabatimeDBHelper", "open");
        }
    }

    public void close() {
        if (sqliteDb != null) {
            sqliteDb.close();
            sqliteDb = null;
        }
    }

    public void initDatabase() {
        open();
    }
}
