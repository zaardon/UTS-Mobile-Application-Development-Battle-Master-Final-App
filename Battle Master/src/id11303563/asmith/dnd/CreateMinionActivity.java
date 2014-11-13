package id11303563.asmith.dnd;

import java.util.Random;
import id11303563.asmith.dnd.characters.MinionItem;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * @author Alex
 * 
 * The Class CreateMinionActivity.
 * 
 * This activity is used to implement the user's minion data into the application.
 * This data includes: minion name, level, health points(HP), loot, race and randomisation buttons
 * for these values.
 */
public class CreateMinionActivity extends GameActivity{	
	/** The add button. */
	private Button addButton;	
	/** The cancel button. */
	private Button cancelButton;	
	/** The minion name. */
	private EditText minionName;	
	/** The minion level. */
	private EditText level;	
	/** The minion hp. */
	private EditText hp;	
	/** The minion loot. */
	private EditText loot;	
	/** The minion race. */
	private EditText race;	
	/** The random names. */
	private String[] randomNames={"Foomi","Terrah","Slar","Zaardon","Wik"};	
	/** The random races. */
	private String[] randomRaces={"Human", "Elf", "Dwarf", "Dark Elf", "Orc"};	
	/** The random name button. */
	private Button randomName;	
	/** The random race button. */
	private Button randomRace;	
	/** The random level and hp low button. */
	private Button randomLevelandHPLow;
	/** The random level and hp medium button. */
	private Button randomLevelandHPMedium;	
	/** The random level and hp high button. */
	private Button randomLevelandHPHigh;
	/** The random loot low button. */
	private Button randomLootLow;	
	/** The random loot medium button. */
	private Button randomLootMedium;	
	/** The random loot high button. */
	private Button randomLootHigh;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_minion);
		setUpViews();
	}
	
	/**
	 * Sets up the class views (10 buttons, 5 EditTexts.)
	 */
	public void setUpViews()
	{	
		addButton = (Button)findViewById(R.id.add_button);
		addButton.setOnClickListener(new OnClickListener() {
			//When pressed, it will implement the input data in the form of a minion object
			public void onClick(View arg0) {
				addMinion();				
			}
		});		
		cancelButton = (Button)findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {		
			//When pressed, it will exit the activity
			public void onClick(View v) {
				finish();				
			}
		});		
		minionName = (EditText)findViewById(R.id.minion_name);		
		level = (EditText)findViewById(R.id.minion_level);
		hp = (EditText)findViewById(R.id.minion_max_hp);
		loot = (EditText)findViewById(R.id.minion_loot);
		race = (EditText)findViewById(R.id.minion_race);	
		randomNameButton();
		randomRaceButton();
		randomLevelAndHPButtons();
		randomLootButtons();	
	}

	
	/**
	 * Adds the minion.
	 */
	private void addMinion(){
		String n = minionName.getText().toString();
		String r = race.getText().toString();
		String l = level.getText().toString();
		String h = hp.getText().toString();
		String lt = loot.getText().toString();		
		//If an EditText box is not blank, this will run
		if(checkBlank(n)&&checkBlank(r)&&checkBlank(l)&&checkBlank(h)&&checkBlank(lt))
		{		
			MinionItem m = new MinionItem(n,r,l,h,lt);
			getPlayerApplication().addMinion(m);						
			finish();
		}
		else
		{
			//A toast is run to indicate that a field is not filled out
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, "Please fill in ALL fields", duration);
			toast.show();
		}
	}
	
	
	/**
	 * Checks if text is blank.
	 *
	 * @param text the text
	 * @return true, if successful
	 */
	public boolean checkBlank(String text){
		//If the text is blank, true is returned.
		if(!text.equals(""))
		{
			return true;
		}
		//If the text is not blank, false is returned
		else
		{
			return false;
		}
	}
	
	
	/**
	 * Random loot buttons. Sets the 3 different random loot buttons by calling in a personalised
	 * random function that sets the class' loot text view.
	 */
	public void randomLootButtons()
	{
		randomLootHigh = (Button)findViewById(R.id.random_loot_button_high);
		randomLootHigh.setOnClickListener(new OnClickListener() {			
			//Sets the values for a HIGH random loot value when pressed
			public void onClick(View v) {				
				int gp;
				Random r = new Random();
				gp = r.nextInt(500) + 500;
				loot.setText(""+gp);				
			}
		});
		
		randomLootMedium = (Button)findViewById(R.id.random_loot_button_medium);
		randomLootMedium.setOnClickListener(new OnClickListener() {		
			//Sets the values for a MEDIUM random loot value when pressed
			public void onClick(View v) {
				int gp;
				Random r = new Random();
				gp = r.nextInt(250) + 250;
				loot.setText(""+gp);					
			}
		});
		
		randomLootLow = (Button)findViewById(R.id.random_loot_button_low);
		randomLootLow.setOnClickListener(new OnClickListener() {
			//Sets the values for a LOW random loot value when pressed			
			public void onClick(View v) {
				int gp;
				Random r = new Random();
				gp = r.nextInt(125);
				loot.setText(""+gp);					
			}
		});
	}
	
	/**
	 * Random level and HP buttons. Sets the 3 different random level and HP buttons by calling in a 
	 * personalised random function that sets the class' level and HP text views.
	 */
	public void randomLevelAndHPButtons()
	{
		randomLevelandHPLow = (Button)findViewById(R.id.random_hp_and_level_button_low);
		randomLevelandHPLow.setOnClickListener(new OnClickListener() {
			//Sets the values for a LOW random health and level value when pressed
			public void onClick(View v) {				
				int rlevel;
				Random r1 = new Random();
				rlevel = r1.nextInt(4) + 1;					
				int rHP;
				Random r2 = new Random();
				rHP = r2.nextInt(100) + 50;					
				switch(rlevel){
				case 0 :level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 1 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 2 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 3 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 4 : level.setText(""+rlevel);
					hp.setText(""+rHP);
				}
			}
		});				
		randomLevelandHPMedium = (Button)findViewById(R.id.random_hp_and_level_button_medium);
		randomLevelandHPMedium.setOnClickListener(new OnClickListener() {	
			//Sets the values for a MEDIUM random health and level value when pressed
			public void onClick(View v) {				
				int rlevel;
				Random r1 = new Random();
				rlevel = r1.nextInt(5) + 5;					
				int rHP;
				Random r2 = new Random();
				rHP = r2.nextInt(100) + 100;					
				switch(rlevel){
				case 5 :level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 6 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 7 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 8 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 9 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 10 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				}				
			}
		});
		randomLevelandHPHigh = (Button)findViewById(R.id.random_hp_and_level_button_high);
		randomLevelandHPHigh.setOnClickListener(new OnClickListener() {		
			//Sets the values for a HIGH random health and level value when pressed
			public void onClick(View v) {				
				int rlevel;
				Random r1 = new Random();
				rlevel = r1.nextInt(5) + 10;					
				int rHP;
				Random r2 = new Random();
				rHP = r2.nextInt(100) + 150;					
				switch(rlevel){
				case 11 :level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 12 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 13 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 14 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				case 15 : level.setText(""+rlevel);
					hp.setText(""+rHP);
					break;
				}				
			}
		});
	}	
	
	/**
	 * Random name button. Triggers a random name from an array of string's into the class' name text view
	 */
	public void randomNameButton()
	{
		randomName = (Button)findViewById(R.id.random_name_button);
		randomName.setOnClickListener(new OnClickListener() {
			//Sets the values for a random name value via an array of names when pressed
			public void onClick(View v) {
				int result;
				Random random = new Random();
				result = random.nextInt(4) + 1;						
				switch(result){
				case 0 : minionName.setText(randomNames[result]);
					break;
				case 1 : minionName.setText(randomNames[result]);
					break;
				case 2 : minionName.setText(randomNames[result]);
					break;
				case 3 : minionName.setText(randomNames[result]);
					break;
				case 4 : minionName.setText(randomNames[result]);
					break;					
				}				
			}
		});
	}
	
	/**
	 * Random race button. Triggers a random race from an array of string's into the class' race text view
	 */
	public void randomRaceButton()
	{
		randomRace = (Button)findViewById(R.id.random_race_button);
		randomRace.setOnClickListener(new OnClickListener() {	
			//Sets the values for a random race value via an array of races when pressed
			public void onClick(View v) {
				int result;
				Random random = new Random();
				result = random.nextInt(4) + 1;						
				switch(result){
				case 0 : race.setText(randomRaces[result]);
					break;
				case 1 : race.setText(randomRaces[result]);
					break;
				case 2 : race.setText(randomRaces[result]);
					break;
				case 3 : race.setText(randomRaces[result]);
					break;
				case 4 : race.setText(randomRaces[result]);
					break;
				}
			}
		});
	}
}
