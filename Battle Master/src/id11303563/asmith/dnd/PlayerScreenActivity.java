package id11303563.asmith.dnd;


import id11303563.asmith.dnd.adapters.CharacterListAdapter;
import id11303563.asmith.dnd.characters.CharacterItem;
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
 * The Class PlayerScreenActivity.
 * 
 * This class handles the options for what a 'player' of dungeons and dragons can do.
 * This class allows a player to add a character, and furthermore, select a character to be used
 * for a bluetooth battle game. 
 */
public class PlayerScreenActivity extends ListActivity {

	/** The create character button. */
	private Button createCharacterButton;	
	/** The application class. */
	private GameApplication app;	
	/** The adapter. */
	private CharacterListAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_player_screen);
		setUpViews();
		//Sets the adapter up in order to collect character data
        app = (GameApplication)getApplication();
        adapter = new CharacterListAdapter(app.getCharacterList(), this);
        setListAdapter(adapter); 		
	}		
	
	/**
	 * Sets up the various views, buttons and onClickListeners for this activity
	 */
	public void setUpViews()
	{		
		createCharacterButton = (Button)findViewById(R.id.createCharacter);
		createCharacterButton.setOnClickListener(new OnClickListener() {
			//When clicked, starts a new activity to create a new character
			public void onClick(View arg0) {
				Intent intent = new Intent(PlayerScreenActivity.this, CreateCharacterActivity.class);
				startActivity(intent);				
			}
		});
				
	}
	
	/**
     * When a character item is clicked, the minion object has its data SENT to the BluetoothGameActivity 
     * class for its use.    
     */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(PlayerScreenActivity.this, BluetoothGameActivity.class); 
	    CharacterItem c = app.getCharacterList().get(position);
	    intent.putExtra("name", c.getName());
	    intent.putExtra("level", c.getLevel());
	    intent.putExtra("hp", c.getMaxHp());
	    intent.putExtra("loot", c.getGp());
	    setResult(RESULT_OK, intent);
	    startActivity(intent);
	 }
		
	/**
	 * When the screen is resumed, force a reload of list item data
	 */
    protected void onResume(){
    	super.onResume();    	  
    	adapter.forceReload();
    	
    }
}