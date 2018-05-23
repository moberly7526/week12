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
    }   //������

    public void open() throws SQLException{
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }


    public ArrayList<RItem> getAllRecords(){
        ArrayList<RItem> records = new ArrayList<>();
        Cursor cursor = db.query(RecordOpenHelper.TABLE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();   //ù��° ���ڵ�� �̵�
        while(!cursor.isAfterLast()){
            //blob�� ����Ʈ��̸� ��Ʈ������ ��ȯ
            int imgColumnId = cursor.getColumnIndex(RecordOpenHelper.COL3);
            byte[] imgByteArray = cursor.getBlob(imgColumnId);
            Bitmap bmp = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);

            RItem record = new RItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), bmp, cursor.getFloat(4),cursor.getString(5));
            records.add(record);
            cursor.moveToNext();
        }
        cursor.close();
        return records;
    }   //db���� ������ ���� �����ֱ�


    public RItem addRecord(String a, String b, Bitmap c){
        ContentValues values = new ContentValues();

        //��Ʈ���� blob�� �ν��ϴ� bytearray�� ��ȯ
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        c.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageInByte = stream.toByteArray();

        values.put(RecordOpenHelper.COL1, a);
        values.put(RecordOpenHelper.COL2, b);
        values.put(RecordOpenHelper.COL3, imageInByte);

        long id = db.insert(RecordOpenHelper.TABLE, null, values);  //db �߰��ϰ� id �ޱ�
        RItem item = new RItem(id, a, b, c);
        return  item;
    } //���ø���Ʈ���� �ҷ��� ������ ������ ���̺� �߰��� ȣ��


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
    }   //���� ��� �߰� ��ư ���� ȣ��


    public void editRecord(long i, float f, String s){
        ContentValues values = new ContentValues();
        values.put(RecordOpenHelper.COL4, f);
        values.put(RecordOpenHelper.COL5, s);
        db.update(RecordOpenHelper.TABLE, values, BaseColumns._ID + " = " + i, null);
    }   //db ����


    public void delRecord(long id){
        db.delete(RecordOpenHelper.TABLE, BaseColumns._ID + " = " + id, null);
    } //db ����


    private void isDBNull(){
        String sql = "select * from "+ RecordOpenHelper.TABLE;
        count = db.rawQuery(sql, null).getCount();
    }


}

//���� ������ http://resumet.tistory.com/13