package com.example.phonebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String CONTACT_TABLE = "CONTACT_TABLE";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";

    public DataBaseHelper(Context context) {
        super(context, "contact.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CONTACT_TABLE + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + PHONE_NUMBER + " TEXT )";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CONTACT_TABLE);
        onCreate(db);
    }

    public boolean addContact(Contact contact)  {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(NAME, contact.getName());
        cv.put(PHONE_NUMBER, contact.getPhoneNumber());

        long insert = db.insert(CONTACT_TABLE, null, cv);
        if (insert == -1)   {
            return false;
        }else {
            return true;
        }
    }

    public ArrayList<Contact> getPhoneBook()   {
        ArrayList<Contact> contacts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+CONTACT_TABLE+ " ORDER BY "+NAME+" ASC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())   {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phoneNumber = cursor.getString(2);

                contacts.add(new Contact(id, name, phoneNumber));
            }while (cursor.moveToNext());
        }
        cursor.close();
        firstLetterNotification(contacts);
        return contacts;
    }

    public boolean deleteContact(int id)   {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(CONTACT_TABLE, ID +" = ?", new String[]{String.valueOf(id)});
        if (delete == 1)    {
            return true;
        }else  {
            return false;
        }
    }

    public boolean editContact(int id, String name, String phoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(PHONE_NUMBER, phoneNumber);

        int update = db.update(CONTACT_TABLE, cv, ID +" = ?", new String[]{String.valueOf(id)});
        if (update == 1)    {
            return true;
        }else {
            return false;
        }
    }

    public Contact getContactById(int id)   {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ CONTACT_TABLE+" WHERE "+ID+" = "+id, null);
        if (cursor.moveToFirst())   {
            String name = cursor.getString(1);
            String phoneNumber = cursor.getString(2);

            return new Contact(id, name, phoneNumber);
        }
        return null;
    }

    private void firstLetterNotification(ArrayList<Contact> list)   {
        String first = "";

        for (Contact c: list
        ) {
            if  (!String.valueOf(c.getName().charAt(0)).equals(first))  {
                first = String.valueOf(c.getName().charAt(0));
                c.setFirstLetter(true);
            }
        }
    }

    public ArrayList<Contact> search(String input) {
        ArrayList<Contact> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+CONTACT_TABLE+" WHERE "+NAME+" LIKE "+"'%"+input+"%'", null);
        if (cursor.moveToFirst())   {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phoneNum = cursor.getString(2);

                results.add(new Contact(id, name, phoneNum));
            }while (cursor.moveToNext());
        }
        cursor.close();
        clearFirstLetter(results);
        return results;
    }

    private void clearFirstLetter(ArrayList<Contact> contacts) {
        for (Contact c: contacts
             ) {
            c.setFirstLetter(false);
        }
    }
}
