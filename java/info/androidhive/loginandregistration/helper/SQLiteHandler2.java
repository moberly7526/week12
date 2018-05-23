//위시리스트에 필요한 디비
package info.androidhive.loginandregistration.helper;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import info.androidhive.loginandregistration.MyWishItem;

public class SQLiteHandler2 extends SQLiteOpenHelper {

    //태그는 php 파일에서 사용하기 위해서 필요함
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // 모든 Static 변수들
    // 디비 버전
    private static final int DATABASE_VERSION = 1;

    // 디비 이름
    private static final String DATABASE_NAME = "wish_db";

    //로그인 테이블 이름 설정
    private static final String TABLE_WISH = "wish";

    // 로그인 테이블 칼럼 이름
    private static final String KEY_ID = "id";
    private static final String KEY_IMG = "img";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_PRODUCT = "product";
    private String[] allColumns = {KEY_ID, KEY_IMG, KEY_BRAND, KEY_PRODUCT};

    public SQLiteHandler2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 테이블 만들기
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WISH_TABLE = "CREATE TABLE " + TABLE_WISH + "("
                + KEY_ID + " INTEGER PRIMARY KEY autoincrement,"
                + KEY_IMG + " BLOB," + KEY_BRAND + " TEXT," + KEY_PRODUCT + " TEXT UNIQUE"+ ")";

        db.execSQL(CREATE_WISH_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // 디비 수정하기//그래서 옛버전, 현재버전 둘다 가져옴
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 예전 테이블이 있다면 드랍 후
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISH);

        // 테이블 다시 만들기
        onCreate(db);
    }



    public void addWish(Bitmap img, String brand, String product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //비트맵 이미지를 바이트어레이로 변환해 디비에 저장
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imgbyte = stream.toByteArray();

        values.put(KEY_IMG, imgbyte); // 이미지
        values.put(KEY_BRAND, brand); // 브랜드명
        values.put(KEY_PRODUCT, product); // 제품명

        // row삽입
        long id = db.insert(TABLE_WISH, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }   //제품 상세페이지에서 즐찾버튼 클릭시 저장


    /**
     * 디비에서 위시리스트 가지고 오기
     * */
    public ArrayList<MyWishItem> getAllWishes() {
        ArrayList<MyWishItem> mywishes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =db.query(TABLE_WISH, allColumns, null, null, null, null, null);
        // 첫번째 행으로 가서 하나씩 가지고 오넹
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            //blob형 바이트어레이를 비트맵으로 변환
            int imgColumnId = cursor.getColumnIndex(KEY_IMG);
            byte[] imgByteArray = cursor.getBlob(imgColumnId);
            Bitmap b = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);

            MyWishItem wish = new MyWishItem(cursor.getInt(0), b, cursor.getString(2), cursor.getString(3));
            mywishes.add(wish);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();//디비닫기
        //사용자 return
        Log.d(TAG, "Fetching user from Sqlite: " + mywishes.toString());

        return mywishes;
    }   //마이페이지 마이 위시에서 호출



    public boolean checkWish(String pname){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = {pname};
        Cursor cursor = db.query(TABLE_WISH, allColumns, "product = ?", params, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM TABLE_WISH WHERE KEY_PRODUCT = ?", new String[]{pname});
        if (cursor.getCount() > 0){
            return false;
        } else{
            return true;
        }
    }

   //디비에서 위시 삭제
    public void delMyWish(int id){  //아마도 인자를 id가 아니라 제품명으로 바꿔야할듯
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WISH, KEY_ID + " = " + id, null);
    }   //마이페이지 마이 위시에서 호출


    /**
     * 테이블에 행이 있으면 위시리스트 상태를 true로 반환//비어있지않음을 의미
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WISH;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // 행의 수 반환
        return rowCount;
    }

    /**
     * 모든 디비 삭제후 다시 만들기
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // 모든 행 삭제
        db.delete(TABLE_WISH, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
