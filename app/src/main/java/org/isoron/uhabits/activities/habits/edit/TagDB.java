package org.isoron.uhabits.activities.habits.edit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tpootool on 30/10/16.
 */

public class TagDB extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TagListInfo";
    // Tags Table Name
    private static final String TABLE_TAGS = "tags";
    // Tags Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COLOR = "color";

    public TagDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TAGS_TABLE = "CREATE TABLE " + TABLE_TAGS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, " + KEY_COLOR + " INTEGER" + ")";
        db.execSQL(CREATE_TAGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Erase old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        // Recreate tables
        onCreate(db);
    }

    // Add new entry
    public void addTag(Tag tag){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, tag.getId());
        values.put(KEY_NAME, tag.getName());
        values.put(KEY_COLOR, tag.getColor());
        db.insert(TABLE_TAGS, null, values);
//        db.close();
    }

    // Getting one Tag
    public Tag getTag(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAGS, new String[] {KEY_ID, KEY_NAME, KEY_COLOR}, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null) {
            boolean temp = cursor.moveToFirst();
            System.out.println(temp);
        }
        Tag newTag = new Tag(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));

        return newTag;
    }

    // Getting one Tag
    public Tag getTagByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TAGS, new String[] {KEY_ID, KEY_NAME, KEY_COLOR}, KEY_NAME + "=?", new String[] {name}, null, null, null, null);
        if(cursor != null) {
            boolean temp = cursor.moveToFirst();
            System.out.println(temp);
        }
        Tag newTag = new Tag(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        return newTag;
    }

    // Get every tag
    public List<Tag> getAllTags(){
        List<Tag> tagList = new ArrayList<Tag>();

        String selectQuery = "SELECT * FROM " + TABLE_TAGS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Iterate through every row
        if (cursor.moveToFirst()) {
            do {
                Tag tag = new Tag(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)));
                tagList.add(tag);
            } while (cursor.moveToNext());
        }
        return tagList;
    }

    public int getTagCount(){
        int temp;
        String countQuery = "SELECT * FROM " + TABLE_TAGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        temp = cursor.getCount();
        cursor.close();
        return temp;
    }

    protected void deleteTag(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGS, KEY_NAME + "=?", new String[] {name});
        db.close();
    }

    protected void refreshIndex() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TAGS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        int tagIter = 1;

        if (cursor.moveToFirst()) {
            do {
                Tag tag = new Tag(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)));
                changeIDTag(tag, tagIter);
                tagIter++;

            } while (cursor.moveToNext());
        }

        db.close();
    }

    public void deleteTagAndRefresh(String name) {
        deleteTag(name);
        refreshIndex();
    }

    protected void changeIDTag(Tag tag, int newId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, newId);
        values.put(KEY_NAME, tag.getName());
        values.put(KEY_COLOR, tag.getColor());
        db.update(TABLE_TAGS, values, KEY_NAME + "=?", new String[] {tag.getName()});
    }
}