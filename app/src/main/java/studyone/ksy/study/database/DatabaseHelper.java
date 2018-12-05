//4/27

package studyone.ksy.study.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "things";

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Creating Tables
    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create capsule table
        db.execSQL( ThingsDB.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ThingsDB.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

//    public void insertDiary(/*int capsule_id, */Double latitude, Double longitude, String create_date, String content, String picture, String title) {
//        // 읽고 쓰기가 가능하게 DB 열기
//        SQLiteDatabase db = getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put( ThingsDB.COLUMN_THING_ID, latitude);
//        values.put( ThingsDB.COLUMN_LONGITUDE, longitude);
//        values.put( ThingsDB.COLUMN_CREATEDATE, create_date);
//        values.put( ThingsDB.COLUMN_CONTENT, content);
//        values.put( ThingsDB.COLUMN_PICTURE, picture);
//        values.put( ThingsDB.COLUMN_TITLE, title);
//
//        // DB에 입력한 값으로 행 추가
//        db.execSQL("INSERT INTO capsule VALUES(" + null + ", " + latitude + ", " + longitude + ", '" + create_date + "', '" + content + "', '" + picture + "', '" + title + "')");
//        db.close();
//    }



//    public ArrayList<Thing> getAllDiary() {
//        ArrayList<Thing> capsuleList = new ArrayList<>();
//        Thing thing;
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM capsule ORDER BY " + ThingsDB.COLUMN_CAPSULEID + " DESC", null);
//
//        while (cursor.moveToNext()) {
//            capsule = new Capsule();
//            capsule.setCapsule_id(cursor.getInt(0));
//            capsule.setLatitude(cursor.getDouble(1));
//            capsule.setLongitude(cursor.getDouble(2));
//            capsule.setCreate_date(cursor.getString(3));
//            capsule.setContent(cursor.getString(4));
//            capsule.setPicture(cursor.getString(5));
//            capsule.setTitle(cursor.getString(6));
//            capsuleList.add(capsule);
//        }
//        cursor.close();
//
//        return capsuleList;
//    }
//
//    public void delete(int idx) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE FROM capsule WHERE " + ThingsDB.COLUMN_CAPSULEID + " = " + idx);
//        db.close();
//    }

}