package info.androidhive.loginandregistration.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import info.androidhive.loginandregistration.MyReview;

public class WriteReviewHandler extends SQLiteOpenHelper{
    public static final String DB = "memos.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "myreviews";
    public static final String COL1 = "brandname";
    public static final String COL2 = "productname";
    public static final String COL3 = "pimgbyte";
    public static final String COL4 = "uid";
    public static final String COL5 = "rating";
    public static final String COL6 = "contents";
    public static final String TABLE_CREATE = "create table "+TABLE+"("+COL1+" text not null, "+COL2+" text not null, "+COL3+" blob not null, "+
            COL4+" text not null, "+COL5+" float not null, "+COL6+" text not null);";
    private static final String TAG = WriteReviewHandler.class.getSimpleName();

    public WriteReviewHandler(Context context){
        super(context, DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    } //테이블 생성

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE); //이전 테이블 삭제
        onCreate(db); //테이블 재생성
    }


    public void addReview(String bname, String pname, byte[] pimg, String uid, float rating, String contents){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL1, bname);
        values.put(COL2, pname);
        values.put(COL3, pimg);
        values.put(COL4, uid);
        values.put(COL5, rating);
        values.put(COL6, contents);

        long id = db.insert(TABLE, null, values);
        db.close();
        Log.d(TAG, "New Review inserted into sqlite : " + id);
    }


    public void deleteReview(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, BaseColumns._ID + " = " + id, null);
    }


    public ArrayList<MyReview> getAllMyReviews(){
        ArrayList<MyReview> myReviews = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            //blob을 비트맵으로 변환
            int imgcolumnid = cursor.getColumnIndex(COL3);
            byte[] imgbyte = cursor.getBlob(imgcolumnid);
            Bitmap pimg = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);

            //MyReview review = new MyReview(cursor.getColumnIndex(BaseColumns._ID), cursor.getString(0), cursor.getString(1), pimg, cursor.getString(3), cursor.getFloat(4), cursor.getString(5));
            //myReviews.add(review);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return myReviews;
    }   //내가 쓴 리뷰 가져오기. 마이페이지에서 리스트에 뿌려줄 때 사용


}
