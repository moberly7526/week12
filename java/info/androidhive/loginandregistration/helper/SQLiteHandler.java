//고객정보가 들어갈 디비
package info.androidhive.loginandregistration.helper;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper {

	//태그는 php 파일에서 사용하기 위해서 필요함
	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// 모든 Static 변수들
	// 디비 버전
	private static final int DATABASE_VERSION = 1;

	// 디비 이름
	private static final String DATABASE_NAME = "android_api";

	//로그인 테이블 이름 설정
	private static final String TABLE_LOGIN = "login";

	// 로그인 테이블 칼럼 이름
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_PROFILE = "profile";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 테이블 만들기
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE,"
				+ KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT,"
				+ KEY_PROFILE + " TEXT"+")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// 디비 수정하기//그래서 옛버전, 현재버전 둘다 가져옴
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 예전 테이블이 있다면 드랍 후
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

		// 테이블 다시 만들기
		onCreate(db);
	}

	/**
	 * 디비에 사용자 정보 저장하기//닉네임, 이메일, 고객번호
	 * */
	public void addUser(String name, String email, String uid, String created_at, String profile) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // 이름
		values.put(KEY_EMAIL, email); // 이메일
		values.put(KEY_UID, uid); // 고객번호
		values.put(KEY_CREATED_AT, created_at); // Created At
		values.put(KEY_PROFILE, profile);//꽃이름

		// row삽입
		long id = db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * 디비에서 고객정보 가지고 오기
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// 첫번째 행으로 가서 하나씩 가지고 오넹
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
			user.put("profile", cursor.getString(5));
	}
		cursor.close();
		db.close();
		//사용자 return
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * 테이블에 행이 있으면 사용자 로그인 상태를 true로 반환
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
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
		db.delete(TABLE_LOGIN, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
