package com.geckoapps.raadhetvoorwerp.classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
		// The Android's default system path of your application database.
		private static String DB_PATH = "/data/data/com.geckoapps.raadhetvoorwerp/databases/";

		//private static String DB_NAME = "levels";
		private static String DB_NAME = "levels4";
		
		private SQLiteDatabase myDataBase;

		private final Context myContext;

		// TABLE CATEGORIEEN
		private final String TABLE_LEVELS = "level";
		private final String TABLE_LEVELS_NL = "level_nl";
		private final String TABLE_LEVELS_NL_OLD = "level_nl_old";
		

		private final String TABLE_LEVELS_DE = "level_de";
		private final String TABLE_LEVELS_DE_OLD = "level_de_old";
		
		private final String LEVELS_NUMER = "nummer";
		private final String LEVELS_ANSWER = "answer";
		private final String LEVELS_PLAATJE = "plaatje";
		
		
		 private final String CREATE_TABLE_LEVELS_DE = "CREATE TABLE "
		            + TABLE_LEVELS_DE + "(id INTEGER PRIMARY KEY," + LEVELS_NUMER
		            + " INTEGER," + LEVELS_ANSWER + " TEXT," + LEVELS_PLAATJE
		            + " TEXT" + ")";
		 
		 private final String CREATE_TABLE_LEVELS_DE_OLD = "CREATE TABLE "
		            + TABLE_LEVELS_DE_OLD + "(id INTEGER PRIMARY KEY," + LEVELS_NUMER
		            + " INTEGER," + LEVELS_ANSWER + " TEXT," + LEVELS_PLAATJE
		            + " TEXT" + ")";

		/**
		 * Constructor Takes and keeps a reference of the passed context in order to
		 * access to the application assets and resources.
		 * 
		 * @param context
		 */
		public DatabaseHelper(Context context) {

			super(context, DB_NAME, null, 1);
			this.myContext = context;
		}

		/**
		 * Creates a empty database on the system and rewrites it with your own
		 * database.
		 * */
		public void createDataBase() throws IOException {

			boolean dbExist = checkDataBase();

			if (dbExist) {
				// do nothing - database already exist
			} else {

				// By calling this method and empty database will be created into
				// the default system path
				// of your application so we are gonna be able to overwrite that
				// database with our database.
				this.getReadableDatabase();

				try {

					copyDataBase();

				} catch (IOException e) {

					throw new Error("Error copying database");

				}
			}

		}

		/**
		 * Check if the database already exist to avoid re-copying the file each
		 * time you open the application.
		 * 
		 * @return true if it exists, false if it doesn't
		 */
		private boolean checkDataBase() {
			SQLiteDatabase checkDB = null;

			try {
				String myPath = DB_PATH + DB_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READWRITE);

			} catch (SQLiteException e) {

				// database does't exist yet.

			}

			if (checkDB != null) {

				checkDB.close();

			}

			return checkDB != null ? true : false;
		}

		/**
		 * Copies your database from your local assets-folder to the just created
		 * empty database in the system folder, from where it can be accessed and
		 * handled. This is done by transfering bytestream.
		 * */
		private void copyDataBase() throws IOException {

			// Open your local db as the input stream
			InputStream myInput = myContext.getAssets().open(DB_NAME);

			// Path to the just created empty db
			String outFileName = DB_PATH + DB_NAME;

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();

		}

		public void openDataBase() throws SQLException {

			// Open the database
			String myPath = DB_PATH + DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

		}

		@Override
		public synchronized void close() {

			if (myDataBase != null)
				myDataBase.close();

			super.close();

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

		//get LEVEL
		public Level getLevel(int totallevenr, String language) {
			
			String table = TABLE_LEVELS;
			if (language.equals("nl")) {
				table = TABLE_LEVELS_NL;
			}
			else{
				table = TABLE_LEVELS_DE;
			}
			
			Cursor cursor;
			try {
				// ask the database object to create the cursor.
				cursor = myDataBase.query(table, new String[] { LEVELS_NUMER,
						LEVELS_ANSWER, LEVELS_PLAATJE}, 
						LEVELS_NUMER + " = " + totallevenr, null, null, null, null);

				// move the cursor's pointer to position zero.
				cursor.moveToFirst();

				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast()) {
					do {

						return new Level(cursor.getInt(0), cursor.getString(1), cursor.getString(2) );
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			} catch (SQLException e) {
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
			// return the ArrayList that holds the data collected from
			// the database.
			return null;
		}
		
	
		
		
		
		//get all LEVEL
		public ArrayList<Level> getAllLevel() {
			
			ArrayList<Level> levels = null;
			
			Cursor cursor;
			try {
				levels = new ArrayList<Level>();
				// ask the database object to create the cursor.
				cursor = myDataBase.query(TABLE_LEVELS_NL_OLD, new String[] { LEVELS_NUMER,
						LEVELS_ANSWER, LEVELS_PLAATJE}, 
						null, null, null, null, null);

				// move the cursor's pointer to position zero.
				cursor.moveToFirst();

				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast()) {
					do {
						levels.add( new Level( cursor.getInt(0) , cursor.getString(1), cursor.getString(2)) );
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			} catch (SQLException e) {
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
			// return the ArrayList that holds the data collected from
			// the database.
			return levels;
		}
		
		public void addLevelToDB(int levelNummer, 
				String answer, String plaatje){
									
			ContentValues localContentValues = new ContentValues();
		    localContentValues.put(LEVELS_NUMER, levelNummer);
		    localContentValues.put(LEVELS_ANSWER, answer);
		    localContentValues.put(LEVELS_PLAATJE, plaatje);
		    try
		    {
		      this.myDataBase.insert(TABLE_LEVELS_NL, null, localContentValues);
		      return;
		    }
		    catch (Exception localException)
		    {
		      Log.e("DB ERROR addlevelpacks:", localException.toString());
		      localException.printStackTrace();
		    }
		}
		
		public void addLevelToDBoldTable(int levelNummer, 
				String answer, String plaatje){
									
			ContentValues localContentValues = new ContentValues();
		    localContentValues.put(LEVELS_NUMER, levelNummer);
		    localContentValues.put(LEVELS_ANSWER, answer);
		    localContentValues.put(LEVELS_PLAATJE, plaatje);
		    try
		    {
		      this.myDataBase.insert(TABLE_LEVELS_NL_OLD, null, localContentValues);
		      return;
		    }
		    catch (Exception localException)
		    {
		      Log.e("DB ERROR addlevelpacks:", localException.toString());
		      localException.printStackTrace();
		    }
		}
		
		
		public void de_addLevelToTable(int levelNummer, 
				String answer, String plaatje){
									
			ContentValues localContentValues = new ContentValues();
		    localContentValues.put(LEVELS_NUMER, levelNummer);
		    localContentValues.put(LEVELS_ANSWER, answer);
		    localContentValues.put(LEVELS_PLAATJE, plaatje);
		    try
		    {
		      this.myDataBase.insert(TABLE_LEVELS_DE, null, localContentValues);
		      return;
		    }
		    catch (Exception localException)
		    {
		      Log.e("DB ERROR addlevelpacks:", localException.toString());
		      localException.printStackTrace();
		    }
		}
		
		
		public int countLevelsOld(){
			Cursor mCount= myDataBase.rawQuery("select count(*) from " + TABLE_LEVELS_NL_OLD, null);
			mCount.moveToFirst();
			int count= mCount.getInt(0);
			mCount.close();
			return count;
		}
		
		public int countLevelsNew(){
			Cursor mCount= myDataBase.rawQuery("select count(*) from " + TABLE_LEVELS_NL, null);
			mCount.moveToFirst();
			int count= mCount.getInt(0);
			mCount.close();
			return count;
		}

		public ArrayList<Level> getExtraLevels(int countLevelsNew) {
	ArrayList<Level> levels = null;
			
			Cursor cursor;
			try {
				levels = new ArrayList<Level>();
				// ask the database object to create the cursor.
				cursor = myDataBase.query(TABLE_LEVELS_NL_OLD, new String[] { LEVELS_NUMER,
						LEVELS_ANSWER, LEVELS_PLAATJE}, 
						LEVELS_NUMER +" > " + countLevelsNew, null, null, null, null);

				// move the cursor's pointer to position zero.
				cursor.moveToFirst();

				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast()) {
					do {
						levels.add( new Level( cursor.getInt(0) , cursor.getString(1), cursor.getString(2)) );
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
			} catch (SQLException e) {
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
			// return the ArrayList that holds the data collected from
			// the database.
			return levels;
		}

		public void addGermanTable() {
			 myDataBase.execSQL(CREATE_TABLE_LEVELS_DE);
			 myDataBase.execSQL(CREATE_TABLE_LEVELS_DE_OLD);
		}
}




