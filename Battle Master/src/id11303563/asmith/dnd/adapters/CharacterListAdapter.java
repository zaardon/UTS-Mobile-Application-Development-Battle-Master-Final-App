package id11303563.asmith.dnd.adapters;


import static id11303563.asmith.dnd.characters.CharacterSQLiteHelper.CHARACTER_TABLE;
import static id11303563.asmith.dnd.characters.CharacterSQLiteHelper.CHARACTER_ID;
import id11303563.asmith.dnd.R;
import id11303563.asmith.dnd.characters.CharacterItem;
import id11303563.asmith.dnd.characters.CharacterSQLiteHelper;
import id11303563.asmith.dnd.views.PlayerCharacterItem;
import java.util.ArrayList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Alex
 * 
 * The Class CharacterListAdapter.
 * 
 * @description This class is an adapter for handling character items. It is responsible for
 * handling the adding and deleting methods of the character list array.
 */
public class CharacterListAdapter extends BaseAdapter {	
	/** A TAG labeled with 'Adapter'. */
	private static final String TAG = "Adapter";	
	/** The array of characters objects. */
	private ArrayList<CharacterItem> characters;	
	/** The database of character objects. */
	private SQLiteDatabase database;	
	/** The class' context. */
	private Context context;
	
	
	/**
	 * Instantiates a new character list adapter.
	 *
	 * @param characters the characters
	 * @param context the context
	 */
	public CharacterListAdapter(ArrayList<CharacterItem> characters, Context context) {
		super();
		this.characters = characters;
		this.context = context;	
	}
	
	/**
	 * Returns the size of the character adapter
	 */
	public int getCount() {
		return characters.size();
	}

	public Object getItem(int position) {
		return (null == characters) ? null : characters.get(position);
		
	}

	/** 
	 * returns the character's item position
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 *  Creates and inflates the ListView of characters for the class
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		PlayerCharacterItem pci;
		if(null == convertView){
			pci = (PlayerCharacterItem)View.inflate(context, R.layout.character_list_item, null);									
		}
		else{
			pci = (PlayerCharacterItem)convertView;
		}		
		pci.setCharacter(characters.get(position), this);				
		return pci;
	}

	/**
	 * Forces a reload.
	 */
	public void forceReload() {
		notifyDataSetChanged();		
	}
	
	/**
	 * This method is passed a 'character object' that is to be deleted from the 
	 * class's array of character objects. Finally, notifyDataSetChanged is called
	 * to inform the application that data has changed and needs to be refreshed.
	 *
	 * @param c the character that is to be deleted
	 */
	public void deleteCharacter(CharacterItem c)
	{
		try{
		characters.remove(c);
		notifyDataSetChanged();	
		Log.d(TAG, "Character was deleted");
		}
		catch(Exception w)
		{
			Log.w(TAG, "Could not delete character!");
		}
	}
	
	/**
	 * This method is passed a 'character object' that is to be deleted from the character
	 * SQLiteDatabase. The contents of the method contain SQL code that delete a character
	 * by its ID number.
	 *
	 * @param c the character that is to be deleted
	 */
	public void deleteCharacterItemDatabase(CharacterItem c)
	{
		long id = c.getId();
		CharacterSQLiteHelper helper = new CharacterSQLiteHelper(context);
		database = helper.getWritableDatabase();	
		database.delete(CHARACTER_TABLE, CHARACTER_ID+"="+id, null);
	}
}
