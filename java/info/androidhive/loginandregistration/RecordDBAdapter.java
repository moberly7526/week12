package info.androidhive.loginandregistration;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RecordDBAdapter{
    private SQLiteDatabase db;
    private RecordOpenHelper dbHelper;
    private String[] allColumns = {RecordOpenHelper.ID, RecordOpenHelper.COL1, RecordOpenHelper.COL2, RecordOpenHelper.COL3, RecordOpenHelper.COL4, RecordOpenHelper.COL5};
    public int count;

    public RecordDBAdapter(Context context){
        dbHelper = new RecordOpenHelper(context);
    }   //생성자

    public void open() throws SQLException{
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }


    public ArrayList<RItem> getAllRecords(){
        ArrayList<RItem> records = new ArrayList<>();
        Cursor cursor = db.query(RecordOpenHelper.TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();   //첫번째 레코드로 이동
        while(!cursor.isAfterLast()){
            //blob형 바이트어레이를 비트맵으로 변환
            int imgColumnId = cursor.getColumnIndex(RecordOpenHelper.COL3);
            byte[] imgByteArray = cursor.getBlob(imgColumnId);
            Bitmap bmp = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);

            RItem record = new RItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), bmp, cursor.getFloat(4),cursor.getString(5));
            records.add(record);
            cursor.moveToNext();
        }
        cursor.close();
        return records;
    }   //db에서 꺼내서 전부 보여주기


    public RItem addRecord(String a, String b, Bitmap c){
        ContentValues values = new ContentValues();

        //비트맵을 blob이 인식하는 bytearray로 변환
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        c.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();

        values.put(RecordOpenHelper.COL1, a);
        values.put(RecordOpenHelper.COL2, b);
        values.put(RecordOpenHelper.COL3, imageInByte);

        long id = db.insert(RecordOpenHelper.TABLE, null, values);  //db 추가하고 id 받기
        RItem item = new RItem(id, a, b, c);
        return  item;
    } //위시리스트에서 불러온 아이템 시향기록 테이블에 추가시 호출


    public void addRecord(RItem itm){
        ContentValues values = new ContentValues();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        itm.getProductImg().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();

        values.put(RecordOpenHelper.COL1, itm.getBrandName());
        values.put(RecordOpenHelper.COL2, itm.getProductName());
        values.put(RecordOpenHelper.COL3, imageInByte);
        values.put(RecordOpenHelper.COL4, itm.getrBar());
        values.put(RecordOpenHelper.COL5, itm.getrEcord());
        db.insert(RecordOpenHelper.TABLE, null, values);
    } //????


    public RItem addRecord(String a, String b, byte[] c, float d, String e){
        ContentValues values = new ContentValues();
        values.put(RecordOpenHelper.COL1, a);
        values.put(RecordOpenHelper.COL2, b);
        values.put(RecordOpenHelper.COL3, c);
        values.put(RecordOpenHelper.COL4, d);
        values.put(RecordOpenHelper.COL5, e);
        long id = db.insert(RecordOpenHelper.TABLE, null, values);

        Bitmap bmp = BitmapFactory.decodeByteArray(c, 0, c.length);

        RItem item = new RItem(id, a, b, bmp, d, e);
        return item;
    }   //시향 기록 추가 버튼 사용시 호출


    public void editRecord(long i, float f, String s){
        ContentValues values = new ContentValues();
        values.put(RecordOpenHelper.COL4, f);
        values.put(RecordOpenHelper.COL5, s);
        db.update(RecordOpenHelper.TABLE, values, BaseColumns._ID + " = " + i, null);
    }   //db 수정


    public void delRecord(long id){
        db.delete(RecordOpenHelper.TABLE, BaseColumns._ID + " = " + id, null);
    } //db 삭제


    private void isDBNull(){
        String sql = "select * from "+ RecordOpenHelper.TABLE;
        count = db.rawQuery(sql, null).getCount();
    }


}

//참고 페이지 http://resumet.tistory.com/13