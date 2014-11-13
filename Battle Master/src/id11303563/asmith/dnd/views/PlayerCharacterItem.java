package id11303563.asmith.dnd.views;

import id11303563.asmith.dnd.R;
import id11303563.asmith.dnd.adapters.CharacterListAdapter;
import id11303563.asmith.dnd.characters.CharacterItem;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Alex
 * 
 * The Class PlayerCharacterItem.
 * 
 * This class gets and sets values from the character_list_item.xml.
 * Each character list item will have its values displayed from this class
 */
public class PlayerCharacterItem extends LinearLayout{

	/** The character. */
	private CharacterItem character;	
	/** The context. */
	private Context context;	
	/** The character's name. */
	private TextView name;
	/** The character's gender. */
	private TextView gender;	
	/** The character's level. */
	private TextView level;	
	/** The character's max hp. */
	private TextView maxHP;	
	/** The character's description. */
	private TextView description;	
	/** The character adapter. */
	private CharacterListAdapter adapter;	
	/** The character's class name. */
	private TextView className;	
	/** The character's gp. */
	private TextView gp;	
	/** The character's delete image. */
	private ImageView cross;

	/**
	 * Instantiates a new player character item.
	 *
	 * @param context the context
	 * @param attrs the attributes
	 */
	public PlayerCharacterItem(Context context, AttributeSet attrs) {
		super(context, attrs);   
		this.context = context;
	}
	
	/**
	 * Creates a ListView with the following inflated items: 6 TextViews and 1 ImageView.
	 */
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		className = (TextView)findViewById(R.id.c_class);
		name = (TextView)findViewById(R.id.c_name);
		gender = (TextView)findViewById(R.id.c_gender);
		level = (TextView)findViewById(R.id.c_level);
		maxHP = (TextView)findViewById(R.id.c_mhp);
		gp = (TextView)findViewById(R.id.c_gold);
		description = (TextView)findViewById(R.id.c_description);				
		cross = (ImageView)findViewById(R.id.cross);
		cross.setClickable(true);
		cross.setHapticFeedbackEnabled(true);
		cross.setOnClickListener(new OnClickListener() {
			//When clicked, it will delete the character of which it belongs to
			public void onClick(View arg0) {
				removeCharacter();				
			}
		});	
		
		
	}
	
	/**
	 * Gets the character.
	 *
	 * @return the character
	 */
	public CharacterItem getCharacter(){
		return character;
	}		
	
	/**
	 * Collects character data and sets the class' TextViews and ImageView with these 
	 * values.
	 * 
	 * @param c the character
	 * @param adapter the adapter
	 */
	public void setCharacter(CharacterItem c, CharacterListAdapter adapter) {
		this.character = c;
		this.adapter = adapter;
		name.setText(character.getName());
		name.setBackgroundColor(Color.BLUE);
		className.setText("Class: " + character.getClassName() + "");
		className.setBackgroundColor(Color.rgb(72,120,0));
		gender.setText("Gender: " + character.getGender() + " ");
		level.setText("Level: " + character.getLevel() + " ");
		maxHP.setText("HP: " + character.getMaxHp() + " ");
		gp.setText("Gold Pieces: " + character.getGp() + " ");
		description.setText("Description: " + character.getDescription() + " ");
	}

	/**
	 * Creates a remove character confirmation dialogue when a character's delete icon
	 * is clicked via a listView row.
	 * 
	 * Upon a confirmation of "Yes", a task object will be delete from both the
	 * ArrayList of characters AND the SQLiteDatabase.
	 */
	protected void removeCharacter() {		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Delete character?")
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   adapter.deleteCharacter(character);
	        	   adapter.deleteCharacterItemDatabase(character);
	           }
	       })
	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.cancel();
	           }
	       });
	 AlertDialog alert = builder.create();
	 alert.show();
	}

	
}
