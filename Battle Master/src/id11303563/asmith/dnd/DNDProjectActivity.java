package id11303563.asmith.dnd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author Alex
 * 
 * The Class DNDProjectActivity.
 * 
 * The class is the first activity that occurs when the program is run. It presents
 * the user with three options: To use the application as a DM or a Player and provides
 * the means to have a rolling dice interface.
 */
public class DNDProjectActivity extends Activity {
	/** The DM option button. */
	private Button DMButton;	
	/** The player option button. */
	private Button playerButton;	
	/** The dice option button. */
	private ImageView diceButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setUpViews();
	}	
	
	/**
	 * Sets up the class' views. 3 Buttons, one for player activities, one for DM activities
	 * and one for dice activities. 
	 */
	public void setUpViews()
	{
		DMButton = (Button)findViewById(R.id.DMButton);
		playerButton = (Button)findViewById(R.id.PlayerButton);
		DMButton.setOnClickListener(new OnClickListener() {
			//Opens the dungeon master screen activity
			public void onClick(View v) {
				Intent intent = new Intent(DNDProjectActivity.this, DMScreenActivity.class);
				startActivity(intent);
			}
		});		
		playerButton.setOnClickListener(new OnClickListener() {
			//Opens the player screen activity
			public void onClick(View v) {
				Intent intent = new Intent(DNDProjectActivity.this, PlayerScreenActivity.class);
				startActivity(intent);
			}
		});	
		diceButton = (ImageView)findViewById(R.id.dice);
		diceButton.setOnClickListener(new OnClickListener() {
			//Opens the dice screen activity
			public void onClick(View arg0) {
				Intent intent = new Intent(DNDProjectActivity.this, DiceActivity.class);
				startActivity(intent);
					
			}
		});
		
	}
}