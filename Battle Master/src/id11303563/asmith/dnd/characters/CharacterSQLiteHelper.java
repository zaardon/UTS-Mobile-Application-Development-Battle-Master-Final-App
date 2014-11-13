package id11303563.asmith.dnd.characters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Alex
 * 
 * The Class CharacterSQLiteHelper.
 * 
 * This class controls the handling of the SQLiteDatabase for character items
 */
public class CharacterSQLiteHelper extends SQLiteOpenHelper {

	/** The Constant DB_NAME. */
	public static final String DB_NAME = "characters.sqlite";	
	/** The Constant VERSION. */
	public static final int VERSION = 1;	
	/** The Constant CHARACTER_TABLE. */
	public static final String CHARACTER_TABLE = "characters";	
	/** The Constant CHARACTER_ID. */
	public static final String CHARACTER_ID = "id";	
	/** The Constant CHARACTER_CLASS. */
	public static final String CHARACTER_CLASS = "class";	
	/** The Constant CHARACTER_GENDER. */
	public static final String CHARACTER_GENDER = "gender";	
	/** The Constant CHARACTER_LEVEL. */
	public static final String CHARACTER_LEVEL = "level";	
	/** The Constant CHARACTER_MAXHP. */
	public static final String CHARACTER_MAXHP = "maxhp";	
	/** The Constant CHARACTER_NAME. */
	public static final String CHARACTER_NAME = "name";	
	/** The Constant CHARACTER_GP. */
	public static final String CHARACTER_GP = "gp";	
	/** The Constant CHARACTER_DESCRIPTION. */
	public static final String CHARACTER_DESCRIPTION = "description";
	
	/**
	 * Instantiates a new character sq lite helper.
	 *
	 * @param context the context
	 */
	public CharacterSQLiteHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		
	}
	
	/**
	 * When the table is created, it will create a table that contains an automatically
	 * assigned ID number, a character name, gender, level, HP, GP and description column. 
	 * 
	 * This method is only called once: when the database does not currently exist.
	 */
	public void onCreate(SQLiteDatabase db) {		
		try{
		createTable(db);
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * This method is called in the case that the database does not currently exist
	 * within the system. If the database exists, the method is not used
	 * @param db - The database in use
	 */
	protected void createTable(SQLiteDatabase db)
	{
		db.execSQL(
			"create table " + CHARACTER_TABLE + " (" +
			CHARACTER_ID + " integer primary key autoincrement not null," +
			CHARACTER_CLASS + " text," +
			CHARACTER_NAME + " text, " +
			CHARACTER_GENDER + " text, " +
			CHARACTER_LEVEL + " text, " +
			CHARACTER_MAXHP + " text, " +
			CHARACTER_GP + " text, " +
			CHARACTER_DESCRIPTION + " text " +
			");"
		);
	}
	
/**
 * Unimplemented database function.
 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
