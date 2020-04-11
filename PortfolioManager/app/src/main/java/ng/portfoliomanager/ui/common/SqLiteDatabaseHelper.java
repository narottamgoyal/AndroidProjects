package ng.portfoliomanager.ui.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqLiteDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "portfolioManager.db";
    public static final String TABLE_NAME = "stockDetails";
    public static final String COL_Id = "Id";
    public static final String COL_Name = "Name";
    public static final String COL_Date = "Date";

    public SqLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Date TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String purchasedDate, String fFood) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_Name, name);
        contentValues.put(COL_Date, purchasedDate);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) return false;
        else return true;
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
