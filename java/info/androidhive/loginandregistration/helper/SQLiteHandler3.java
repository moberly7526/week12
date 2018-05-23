//마이페이지 최근
package info.androidhive.loginandregistration.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import info.androidhive.loginandregistration.MyRecentItem;

public class SQLiteHandler3 extends SQLiteOpenHelper{
    private static final String TAG = SQLiteHandler3.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mypage_db";
    private static final String TABLE_MY_RECENT = "recent";
    private static final String _ID = "id";
    private static final String PIMG = "pimg";
    private static final String BNAME = "bname";
    private static final String PNAME = "pname";
    private String[] allRColumns = {_ID, PIMG, BNAME, PNAME};

    int count, firstid;

    public SQLiteHandler3(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }   //DB 생성

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MYRECENT_TABLE = "CREATE TABLE " + TABLE_MY_RECENT + " ("
                + _ID + " INTEGER PRIMARY KEY autoincrement, "
                + PIMG + " BLOB, " + BNAME + " TEXT, " + PNAME + " TEXT)";

        db.execSQL(CREATE_MYRECENT_TABLE);
        Log.d(TAG, "Database tables created");
    }   //테이블 생성

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_RECENT);
        onCreate(db);
    }   //수정



    public void addRecent(Bitmap a, String b, String c){
        SQLiteDatabase db = this.getWritableDatabase();
        check();

        if(count > 10){
            db.delete(TABLE_MY_RECENT, _ID+" = "+firstid, null);
        }

        ContentValues values = new ContentValues();

        //비트맵 이미지를 바이트어레이로 변환해 디비에 저장
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        a.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imgbyte = stream.toByteArray();

        values.put(PIMG, imgbyte);
        values.put(BNAME, b);
        values.put(PNAME, c);

        long id = db.insert(TABLE_MY_RECENT, null, values);
        db.close();
    }


    private void check(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MY_RECENT, allRColumns, null, null, null, null, null);
        count =  cursor.getCount();

        cursor.moveToFirst();
        firstid = cursor.getColumnIndex(_ID);
        cursor.close();
        //db.close();
    }


    public ArrayList<MyRecentItem> getRecent(){
        ArrayList<MyRecentItem> recent = new ArrayList<MyRecentItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MY_RECENT, allRColumns, null, null, null, null, null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            int imgColumnId = cursor.getColumnIndex(PIMG);
            byte[] imgByteArray = cursor.getBlob(imgColumnId);
            Bitmap b = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);

            MyRecentItem item = new MyRecentItem(b, cursor.getString(2), cursor.getString(3));
            recent.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return recent;
    }





}
