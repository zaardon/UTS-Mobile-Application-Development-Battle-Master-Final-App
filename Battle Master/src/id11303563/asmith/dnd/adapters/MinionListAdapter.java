package id11303563.asmith.dnd.adapters;


import static id11303563.asmith.dnd.characters.MinionSQLiteHelper.MINION_TABLE;
import static id11303563.asmith.dnd.characters.MinionSQLiteHelper.MINION_ID;
import id11303563.asmith.dnd.R;
import id11303563.asmith.dnd.characters.MinionItem;
import id11303563.asmith.dnd.characters.MinionSQLiteHelper;
import id11303563.asmith.dnd.views.MinionCharacterItem;
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
 * The Class MinionListAdapter.
 * 
 * @description This class is an adapter for handling minion items. It is responsible for
 * handling the adding and deleting methods of the minion list array.
 */
public class MinionListAdapter extends BaseAdapter {	
	/** A TAG labeled with 'Adapter'. */
	private static final String TAG = "Adapter";	
	/** The minions. */
	private ArrayList<MinionItem> minions;
	/** The minion database. */
	private SQLiteDatabase database;	
	/** The context. */
	private Context context;	
	
	/**
	 * Instantiates a new minion list adapter.
	 *
	 * @param minions the minions
	 * @param context the context
	 */
	public MinionListAdapter(ArrayList<MinionItem> minions, Context context) {
		super();
		this.minions = minions;
		this.context = context;	
	}
	
	/**
	 * Returns the size of the minion adapter
	 */
	public int getCount() {
		return minions.size();
	}

	public Object getItem(int position) {
		return (null == minions) ? null : minions.get(position);
		
	}

	/** 
	 * returns the minion's item position
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 *  Creates and inflates the ListView of minions for the class
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		MinionCharacterItem mci;
		if(null == convertView){
			mci = (MinionCharacterItem)View.inflate(context, R.layout.minion_list_item, null);									
		}
		else{
			mci = (MinionCharacterItem)convertView;
		}		
		mci.setMinion(minions.get(position), this);				
		return mci;
	}

	/**
	 * Forces a reload.
	 */
	public void forceReload() {
		notifyDataSetChanged();		
	}
	
	/**
	 * This method is passed a 'minion object' that is to be deleted from the 
	 * class's array of minion objects. Finally, notifyDataSetChanged is called
	 * to inform the application that data has changed and needs to be refreshed.
	 *
	 * @param m the minion that is to be deleted
	 */
	public void deleteMinion(MinionItem m)
	{
		try{
		minions.remove(m);
		notifyDataSetChanged();	
		Log.d(TAG, "Minion was deleted");
		}
		catch(Exception w)
		{
			Log.w(TAG, "Could not delete minion!");
		}
	}
	
	/**
	 * This method is passed a 'minion object' that is to be deleted from the minion
	 * SQLiteDatabase. The contents of the method contain SQL code that delete a minion
	 * by its ID number.
	 *
	 * @param m the minion that is to be deleted
	 */
	public void deleteMinionItemDatabase(MinionItem m)
	{
		long id = m.getId();
		MinionSQLiteHelper helper = new MinionSQLiteHelper(context);
		database = helper.getWritableDatabase();	
		database.delete(MINION_TABLE, MINION_ID+"="+id, null);
	}
}
