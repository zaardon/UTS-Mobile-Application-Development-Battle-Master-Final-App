package id11303563.asmith.dnd;


import id11303563.asmith.dnd.characters.CharacterItem;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author Alex
 * 
 * The Class CreateCharacterActivity.
 *
 * This activity is used to implement the user's character data into the application.
 * This data includes: character name, level, health points(HP), gold, description and gender values.
 */
public class CreateCharacterActivity extends GameActivity{	
	/** The add button. */
	private Button addButton;	
	/** The cancel button. */
	private Button cancelButton;	
	/** The character name. */
	private EditText characterName;	
	/** The character level. */
	private EditText level;	
	/** The character hp. */
	private EditText hp;	
	/** The character gold. */
	private EditText gold;	
	/** The character description. */
	private EditText description;
	/** The gender group. */
	private RadioGroup genderGroup;
	/** The class name spinner. */
	private Spinner classNameSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_character);
		setUpViews();
	}

	/**
	 * Sets up the views of the class (5 EditTexts, 1 RadioGroup, 1 Spinner and 2 Buttons).
	 */
	public void setUpViews()
	{		
		addButton = (Button)findViewById(R.id.add_button);
		addButton.setOnClickListener(new OnClickListener() {
			//When pressed, it will implement the input data in the form of a character object
			public void onClick(View arg0) {
				addCharacter();				
			}
		});
		
		cancelButton = (Button)findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {	
			//When pressed, it will exit the activity
			public void onClick(View v) {
				finish();				
			}
		});
		characterName = (EditText)findViewById(R.id.character_name);		
		level = (EditText)findViewById(R.id.level);
		hp = (EditText)findViewById(R.id.max_hp);
		description = (EditText)findViewById(R.id.description);
		gold = (EditText)findViewById(R.id.character_gold);		
		setUpClass();
		setUpGender();		
	}

	
	/**
	 * Sets up the character's gender.
	 */
	private void setUpGender() {
		genderGroup = (RadioGroup)findViewById(R.id.gender);
		RadioButton radioButton = (RadioButton)findViewById(R.id.male);
		radioButton.setChecked(true);
	}


	/**
	 * Sets up the character's class.
	 */
	private void setUpClass() {
	classNameSpinner = (Spinner)findViewById(R.id.class_name);
	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.class_list, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		classNameSpinner.setAdapter(adapter);
		
	}
	
	/**
	 * Adds the character to the adapter/application.
	 */
	private void addCharacter(){
		String s = getClassSpinnerPosition(classNameSpinner.getSelectedItemPosition());
		String n = characterName.getText().toString();
		String g = getRadioButton(genderGroup.getCheckedRadioButtonId());
		String l = level.getText().toString();
		String h = hp.getText().toString();
		String gp = gold.getText().toString();
		String d = description.getText().toString();		
		//If an EditText box is not blank, this will run
		if(checkBlank(n)&&checkBlank(l)&&checkBlank(h)&&checkBlank(gp)&&checkBlank(d))
		{			
			CharacterItem c = new CharacterItem(s,n,g,l,h,gp,d);
			getPlayerApplication().addCharacter(c);						
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
	 * Gets the user selected radio button for a gender value.
	 *
	 * @param checkedRadioButton the checked radio button
	 * @return the radio button
	 */
	private String getRadioButton(int checkedRadioButton){
		String gender = "";
		switch(checkedRadioButton)
		{
		case R.id.male : gender = "Male";
			break;
		case R.id.female : gender = "Female";
			break;
		}
		return gender;
	}
	
	/**
	 * Checks if the text is blank.
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
	 * Gets the class' spinner position.
	 *
	 * @param spinnerPosition the spinner position
	 * @return the class spinner position
	 */
	private String getClassSpinnerPosition(int spinnerPosition){
		String responsibility = "";
		responsibility = classNameSpinner.getItemAtPosition(spinnerPosition).toString();
		return responsibility;
	}
}
