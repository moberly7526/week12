package info.androidhive.loginandregistration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecordOpenHelper extends SQLiteOpenHelper{
    public static final String DB = "records.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "records";
    public static final String ID = "_id";
    public static final String COL1 = "brandName";
    public static final String COL2 = "productName";
    public static final String COL3 = "productImg";
    public static final String COL4 = "ratingBar";
    public static final String COL5 = "comment";
    public static final String CREATE_DB = "create table "+TABLE+"("+ID+" integer primary key autoincrement, "+
            COL1+" text not null, "+COL2+" text not null, "+COL3+" blob, "+COL4+" real, "+COL5+" text);";

    public RecordOpenHelper(Context context){
        super(context, DB, null, DB_VERSION);
    }   //디비 생성자

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }   //테이블 생성

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(RecordOpenHelper.class.getName(), "upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);  //업데이트시 처리
        onCreate(db);
    }   //수정
}
