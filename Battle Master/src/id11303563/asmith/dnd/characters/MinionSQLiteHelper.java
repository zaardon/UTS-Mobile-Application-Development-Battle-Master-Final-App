package id11303563.asmith.dnd.characters;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Alex
 * 
 * The Class MinionSQLiteHelper.
 * 
 * This class controls the handling of the SQLiteDatabase for minion items
 */
public class MinionSQLiteHelper extends SQLiteOpenHelper {

	/** The Constant DB_NAME. */
	public static final String DB_NAME = "minions.sqlite";	
	/** The Constant VERSION. */
	public static final int VERSION = 1;	
	/** The Constant MINION_TABLE. */
	public static final String MINION_TABLE = "characters";	
	/** The Constant MINION_ID. */
	public static final String MINION_ID = "id";	
	/** The Constant MINION_RACE. */
	public static final String MINION_RACE = "race";	
	/** The Constant MINION_LEVEL. */
	public static final String MINION_LEVEL = "level";	
	/** The Constant MINION_MAXHP. */
	public static final String MINION_MAXHP = "maxhp";	
	/** The Constant MINION_NAME. */
	public static final String MINION_NAME = "name";	
	/** The Constant MINION_LOOT. */
	public static final String MINION_LOOT = "loot";
	
	/**
	 * Instantiates a new minion sq lite helper.
	 *
	 * @param context the context
	 */
	public MinionSQLiteHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		
	}
	
	/**
	 * When the table is created, it will create a table that contains an automatically
	 * assigned ID number, a minion name, race, level, HP and loot columns. 
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
			"create table " + MINION_TABLE + " (" +
			MINION_ID + " integer primary key autoincrement not null," +
			MINION_NAME + " text, " +
			MINION_RACE + " text, " +
			MINION_LEVEL + " text, " +
			MINION_MAXHP + " text, " +
			MINION_LOOT + " text " +
			");"
		);
	}
	
	/**
	 * Unimplemented database function.
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
