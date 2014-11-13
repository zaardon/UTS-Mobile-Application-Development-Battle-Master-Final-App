package id11303563.asmith.dnd;


import id11303563.asmith.dnd.characters.CharacterItem;
import id11303563.asmith.dnd.characters.CharacterSQLiteHelper;
import id11303563.asmith.dnd.characters.MinionItem;
import id11303563.asmith.dnd.characters.MinionSQLiteHelper;
import static id11303563.asmith.dnd.characters.CharacterSQLiteHelper.*;
import static id11303563.asmith.dnd.characters.MinionSQLiteHelper.*;
import java.util.ArrayList;
import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * The Class GameApplication.
 *
 * @author Alex
 * 
 * This class handles the character and minion ArrayLists AND the character and minion
 * databases that are used throughout the program's life.
 */

public class GameApplication extends Application {		
	
	/** The character list. */
	private ArrayList<CharacterItem> characterList; 	
	/** The character database. */
	private SQLiteDatabase database;	
	/** The minion database. */
	private SQLiteDatabase m_database;	
	/** The minion list. */
	private ArrayList<MinionItem> minionList; 

	@Override
	public void onCreate() 
	{		
		super.onCreate();
		CharacterSQLiteHelper helper = new CharacterSQLiteHelper(this);
		//Creates a writeable SQL database for characters- compared to readable
		database = helper.getWritableDatabase();		
		MinionSQLiteHelper m_helper = new MinionSQLiteHelper(this);
		//Creates a writeable SQL database for minions- compared to readable
		m_database = m_helper.getWritableDatabase();	
		//If there is no current array of characters, one will be created
		if(null == characterList)
		{
			loadCharacters();
		}
		//If there is no current array of minions, one will be created
		if(null == minionList)
		{
			loadMinions();
		}
	}
	
	/**
	 * This method will load the currently stored character data and place it inside an
	 * array of characters.
	 */
	private void loadCharacters() {
		characterList = new ArrayList<CharacterItem>();
		Cursor characterCursor = database.query(
				CHARACTER_TABLE, new String[]{
					CHARACTER_ID, 
					CHARACTER_CLASS,
					CHARACTER_NAME,
					CHARACTER_GENDER, 
					CHARACTER_LEVEL,
					CHARACTER_MAXHP,
					CHARACTER_GP,
					CHARACTER_DESCRIPTION
					}, 
					null, null, null, null,
					String.format("%s,%s,%s,%s,%s", CHARACTER_CLASS, CHARACTER_NAME, CHARACTER_GENDER, CHARACTER_LEVEL, CHARACTER_MAXHP));
		characterCursor.moveToFirst();		
		CharacterItem c;
		if (! characterCursor.isAfterLast()){
			do{
				long id = characterCursor.getLong(0);
				String charClass = characterCursor.getString(1);
				String name = characterCursor.getString(2);
				String gender = characterCursor.getString(3);						
				String level = characterCursor.getString(4);
				String maxhp = characterCursor.getString(5);
				String gp = characterCursor.getString(6);
				String description = characterCursor.getString(7);
				c = new CharacterItem(charClass, name, gender, level, maxhp,gp, description); 
				c.setId(id);
				characterList.add(c);
			}
			while(characterCursor.moveToNext());
		}
		characterCursor.close();
		
	}

	/**
	 * Gets the character list.
	 *
	 * @return the character list
	 */
	public ArrayList<CharacterItem> getCharacterList() {
		return characterList;
	}

	/**
	 * Sets the character list.
	 *
	 * @param characterList the new character list
	 */
	public void setCharacterList(ArrayList<CharacterItem> characterList) {
		this.characterList = characterList;
	}
	
	/**
	 * This method is passed in a character object. This object will then have its various characteristics
	 * added to the database under appropriate columns. Once the object has been stripped into the database, 
	 * it is placed into an array of characters so that it may be displayed to the user of the system.
	 * 
	 * @param c the character
	 */
	public void addCharacter(CharacterItem c){
		assert(null != c);		
		ContentValues values = new ContentValues();
		values.put(CHARACTER_CLASS, c.getClassName());
		values.put(CHARACTER_NAME, c.getName());
		values.put(CHARACTER_GENDER, c.getGender());
		values.put(CHARACTER_LEVEL, c.getLevel());
		values.put(CHARACTER_MAXHP, c.getMaxHp());
		values.put(CHARACTER_GP, c.getGp());
		values.put(CHARACTER_DESCRIPTION, c.getDescription());
		c.setId(database.insert(CHARACTER_TABLE, null, values));		
		characterList.add(c);
	}
	
	/**
	 * Delete characters from the SQLite database.
	 *
	 * @param position the position
	 */
	public void deleteCharacters(int position)
	{	
		CharacterItem c = characterList.get(position);
		long id = c.getId();
		database.delete(CHARACTER_TABLE, CHARACTER_ID+"="+id, null);
	}
	
	/**
	 * This method will load the currently stored minion data and place it inside an
	 * array of minions.
	 */
	private void loadMinions() {
		minionList = new ArrayList<MinionItem>();
		Cursor minionCursor = m_database.query(
				MINION_TABLE, new String[]{
					MINION_ID, 
					MINION_NAME,
					MINION_RACE, 
					MINION_LEVEL,
					MINION_MAXHP,
					MINION_LOOT
					}, 
					null, null, null, null,
					String.format("%s,%s,%s,%s", MINION_NAME, MINION_RACE, MINION_LEVEL, MINION_MAXHP));
		minionCursor.moveToFirst();		
		MinionItem m;
		if (! minionCursor.isAfterLast()){
			do{
				long id = minionCursor.getLong(0);
				String name = minionCursor.getString(1);
				String race = minionCursor.getString(2);						
				String level = minionCursor.getString(3);
				String maxhp = minionCursor.getString(4);
				String loot = minionCursor.getString(5);
				m = new MinionItem(name, race, level, maxhp, loot); 
				m.setId(id);
				minionList.add(m);
			}
			while(minionCursor.moveToNext());
		}
		minionCursor.close();
		
	}

	/**
	 * Gets the minion list.
	 *
	 * @return the minion list
	 */
	public ArrayList<MinionItem> getMinionList() {
		return minionList;
	}

	/**
	 * Sets the minion list.
	 *
	 * @param minionList the new minion list
	 */
	public void setMinionList(ArrayList<MinionItem> minionList) {
		this.minionList = minionList;
	}
	
	/**
	 * This method is passed in a minion object. This object will then have its various characteristics
	 * added to the database under appropriate columns. Once the object has been stripped into the database, 
	 * it is placed into an array of minions so that it may be displayed to the user of the system.
	 * 
	 * @param m the minion
	 */
	public void addMinion(MinionItem m){
		assert(null != m);	
		ContentValues values = new ContentValues();
		values.put(MINION_NAME, m.getName());
		values.put(MINION_RACE, m.getRace());
		values.put(MINION_LEVEL, m.getLevel());
		values.put(MINION_MAXHP, m.getMaxHp());
		values.put(MINION_LOOT, m.getLoot());
		m.setId(m_database.insert(MINION_TABLE, null, values));		
		minionList.add(m);
	}
	
	/**
	 * Deletes a minion from the database.
	 *
	 * @param position the position
	 */
	public void deleteMinion(int position)
	{	
		MinionItem m = minionList.get(position);
		long id = m.getId();
		m_database.delete(MINION_TABLE, MINION_ID+"="+id, null);
	}
	

}
