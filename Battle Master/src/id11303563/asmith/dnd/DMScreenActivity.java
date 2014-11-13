package id11303563.asmith.dnd;

import id11303563.asmith.dnd.adapters.MinionListAdapter;
import id11303563.asmith.dnd.characters.MinionItem;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author Alex
 * 
 * The Class DMScreenActivity.
 * 
 * This class handles the options for what a 'Dungeon Master (DM)' of dungeons and dragons can do.
 * This class allows a DM to add a minion, and furthermore, select a minion to be used
 * for a bluetooth battle game. 
 */
public class DMScreenActivity extends ListActivity {
	
	/** The minion adapter. */
	private MinionListAdapter adapter;
	/** The application class. */
	private GameApplication app;
	/** The create minion button. */
	private Button createMinionButton;
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_dm_screen);
		setUpViews();
		//Sets the adapter up in order to collect minion data
        app = (GameApplication)getApplication();
        adapter = new MinionListAdapter(app.getMinionList(), this);
        setListAdapter(adapter); 
	}	
	
	/**
	 * Sets up the various views, buttons and onClickListeners for this activity
	 */
	public void setUpViews()
	{
		createMinionButton = (Button)findViewById(R.id.newMinion);
		createMinionButton.setOnClickListener(new OnClickListener() {
			//When clicked, starts a new activity to create a new minion
			public void onClick(View arg0) {
				Intent intent = new Intent(DMScreenActivity.this, CreateMinionActivity.class);
				startActivity(intent);				
			}
		});
	}
	
	/**
	 * When the screen is resumed, force a reload of list item data
	 */
    protected void onResume(){
    	super.onResume();    	  
    	adapter.forceReload();
    	
    }
    

    /**
     * When a minion item is clicked, the minion object has its data SENT to the BluetoothGameActivity 
     * class for its use.    
     */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(DMScreenActivity.this, BluetoothGameActivity.class); 
	    MinionItem m = app.getMinionList().get(position);
	    intent.putExtra("name", m.getName());
	    intent.putExtra("level", m.getLevel());
	    intent.putExtra("hp", m.getMaxHp());
	    intent.putExtra("loot", m.getLoot());
	    setResult(RESULT_OK, intent);
	    startActivity(intent);
	 }
}
