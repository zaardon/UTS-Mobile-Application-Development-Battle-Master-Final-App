package id11303563.asmith.dnd.views;


import id11303563.asmith.dnd.R;
import id11303563.asmith.dnd.adapters.MinionListAdapter;
import id11303563.asmith.dnd.characters.MinionItem;
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
 * The Class MinionCharacterItem.
 * 
 * This class gets and sets values from the minion_list_item.xml.
 * Each minion list item will have its values displayed from this class
 */
public class MinionCharacterItem extends LinearLayout{
	/** The minion. */
	private MinionItem minion;
	/** The context. */
	private Context context;
	/** The minion's name. */
	private TextView name;
	/** The minion's race. */
	private TextView race;
	/** The minion's level. */
	private TextView level;
	/** The minion's max hp. */
	private TextView maxHP;
	/** The minion's loot. */
	private TextView loot;
	/** The minion's adapter. */
	private MinionListAdapter adapter;
	/** The minion's cross. */
	private ImageView cross;

	/**
	 * Instantiates a new minion character item.
	 *
	 * @param context the context
	 * @param attrs the attributes
	 */
	public MinionCharacterItem(Context context, AttributeSet attrs) {
		super(context, attrs);   
		this.context = context;
	}
	
	/**
	 * Creates a ListView with the following inflated items: 5 TextViews and 1 ImageView.
	 */
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		name = (TextView)findViewById(R.id.m_name);
		race = (TextView)findViewById(R.id.m_race);
		level = (TextView)findViewById(R.id.m_level);
		maxHP = (TextView)findViewById(R.id.m_mhp);
		loot = (TextView)findViewById(R.id.m_loot);		
		cross = (ImageView)findViewById(R.id.cross);
		cross.setClickable(true);
		cross.setHapticFeedbackEnabled(true);		
		cross.setOnClickListener(new OnClickListener() {
			//When clicked, it will delete the minion of which it belongs to
			public void onClick(View arg0) {
				removeMinion();				
			}
		});	
	}
	
	/**
	 * Gets the minion.
	 *
	 * @return the minion
	 */
	public MinionItem getMinion(){
		return minion;
	}
	
	/**
	 * Collects minion data and sets the class' TextViews and ImageView with these 
	 * values.
	 * @param m the minion
	 * @param adapter the adapter
	 */
	public void setMinion(MinionItem m, MinionListAdapter adapter) {
		this.minion = m;
		this.adapter = adapter;
		name.setText(minion.getName());
		name.setBackgroundColor(Color.BLUE);
		race.setText("Race: " + minion.getRace() + " ");
		raceTextColour(m);
		level.setText("Level: " + minion.getLevel() + " ");
		maxHP.setText("HP: " + minion.getMaxHp() + " ");
		loot.setText("Loot: " + minion.getLoot() + " ");
	}

	/**
	 * Creates a remove minion confirmation dialogue when a minion's delete icon
	 * is clicked via a listView row.
	 * 
	 * Upon a confirmation of "Yes", a task object will be delete from both the
	 * ArrayList of minions AND the minion SQLiteDatabase.
	 */
	protected void removeMinion() {		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Delete minion?")
	       .setCancelable(false)
	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   adapter.deleteMinion(minion);
	        	   adapter.deleteMinionItemDatabase(minion);
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
	
	/**
	 * Sets the 'race' TextView's colour based on a minion's race string value
	 *
	 * @param m the minion
	 */
	public void raceTextColour(MinionItem m)
	{
		if(m.getRace().toString() == "Human"){
			race.setBackgroundColor(Color.rgb(139,8,127));
		}
		else if(m.getRace().toString() == "Elf"){
			race.setBackgroundColor(Color.rgb(139,8,0));
		}
		else if(m.getRace().toString() == "Dwarf"){
			race.setBackgroundColor(Color.rgb(4,62,0));
		}
		else if(m.getRace().toString() == "Dark Elf"){
			race.setBackgroundColor(Color.rgb(72,120,0));
		}
		else if(m.getRace().toString() == "Orc"){
			race.setBackgroundColor(Color.rgb(181,97,0));
		}
		else{
			race.setBackgroundColor(Color.rgb(0,46,0));
		}
	}
}