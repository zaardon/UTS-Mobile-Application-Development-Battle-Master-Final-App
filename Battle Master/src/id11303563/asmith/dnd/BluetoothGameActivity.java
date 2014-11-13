package id11303563.asmith.dnd;

import java.util.HashMap;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Alex
 * 
 * The Class BluetoothGameActivity.
 * 
 * This activity handles the bluetooth game interaction between two opposing people. Players can fight each other
 * as DM vs Player, Player vs Player, and/or DM vs DM. This class also contains sound data that is played throughout
 * the interaction between the two players.
 */
public class BluetoothGameActivity extends Activity {			
    /** The Constant BT_STATE_CHANGE. */
    public static final int BT_STATE_CHANGE = 1;   
    /** The Constant BT_INCOMING. */
    public static final int BT_INCOMING = 2;    
    /** The Constant BT_OUTGOING. */
    public static final int BT_OUTGOING = 3;   
    /** The Constant BT_DEVICE_NAME. */
    public static final int BT_DEVICE_NAME = 4;    
    /** The Constant MESSAGE_TOAST. */
    public static final int MESSAGE_TOAST = 5;   
    /** The Constant DEVICE_NAME. */
    public static final String DEVICE_NAME = "device_name";    
    /** The Constant TOAST. */
    public static final String TOAST = "toast";   
    /** The Constant REQUEST_CONNECT_DEVICE. */
    private static final int REQUEST_CONNECT_DEVICE = 2;    
    /** The Constant REQUEST_ENABLE_BT. */
    private static final int REQUEST_ENABLE_BT = 3;    
    /** The bluetooth's conversation view. */
    private ListView bt_ConversationView;  
    /** The bluetooth's message send button. */
    private Button bt_SendButton;   
    /** The bluetooth's connected device name. */
    private String bt_ConnectedDeviceName = null;   
    /** The bluetooth's conversation array adapter. */
    private ArrayAdapter<String> bt_ConversationArrayAdapter;   
    /** The bluetooth's out string buffer. */
    private StringBuffer bt_OutStringBuffer;    
    /** The bluetooth's adapter. */
    private BluetoothAdapter bluetoothAdapter = null;  
    /** The bluetooth's service. */
    private BluetoothService bluetoothService = null;	
	/** The character's name. */
	private TextView characterName;	
	/** The character's level. */
	private TextView characterlevel;	
	/** The character' max hp text. */
	private TextView characterMaxHPText;	
	/** The current's hp. */
	private int currentHP;	
	/** The character's current hp. */
	private TextView characterCurrentHP;	
	/** The attack button. */
	private Button attackButton;	
	/** The heal button. */
	private Button healButton;	
	/** The roll button. */
	private ImageView rollButton;	
	/** The battle text. */
	private TextView battleText;	
	/** The battle label. */
	private TextView battleLabel;	
	/** The enemy's name. */
	private TextView enemyName;	
	/** The enemy's level. */
	private TextView enemylevel;	
	/** The enemy's current hp. */
	private TextView enemyCurrentHP;	
	/** The attack amount. */
	private EditText attackAmount;	
	/** The heal amount. */
	private EditText healAmount;	
	/** The game in session boolean. */
	private boolean gameInSession = false;	
	/** The game in progress dialogue. */
	private AlertDialog gameInProgressDialogue;	
	/** The character's max hp max. */
	private TextView characterMaxHPMax;	
	/** The enemy's max hp text. */
	private TextView enemyMaxHPText;	
	/** The enemy's max hp max. */
	private TextView enemyMaxHPMax;	
	/** The enemy's name. */
	private String eName;	
	/** The win sound. */
	private int WIN_SOUND =1;	
	/** The lose sound. */
	private int LOSE_SOUND = 2;	
	/** The hit sound. */
	private int HIT_SOUND =3;
	/** The hurt sound. */
	private int HURT_SOUND = 4;	
	/** The heal sound. */
	private int HEAL_SOUND = 5;	
	/** The start sound. */
	private int START_SOUND = 6;	
	/** The sound pool. */
	private SoundPool soundPool;	
	/** The sounds map. */
	private HashMap<Integer, Integer> soundsMap;	
	/** The heal icon. */
	private ImageView heal_icon;	
	/** The sword icon. */
	private ImageView sword_icon;
	/** The user's image icon */
	private ImageView characterImage;
	/** The enemy's image icon */
	private ImageView enemyImage;

	/**
	 * Called upon activity creation
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_screen);		
        //Obtain a bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the device cannot use bluetooth, cancel the activity
        if (bluetoothAdapter == null) 
        {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }       
		setUpViews();		
	}

	/**
	 * Sets up the various class' views (10 15 TextViews, 2 EditTexts, 4 Buttons).
	 */
	private void setUpViews() {
		setUpSound();		
		setUpBattleData();
		setUpCharacterData();
		setUpEnemyData();	
		setUpButtons();
	}
	
		
    /**
	* Called when the activity starts
	*/
    @Override
    public void onStart() 
    {
        super.onStart();
        //Check if bluetooth is turned on
        if (!bluetoothAdapter.isEnabled()) 
        {
        	//Turn it on
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } 
        else 
        {
        	//Begin battle session
            if (bluetoothService == null){ 
            	//Calls to set up the bluetooth functionality
            	setUpBluetooth();
            }
        }
    }

    /**
     * Called when the activity is resumed. This function checks if the bluetooth game has
     * commenced or not by checking its state
     */
    @Override
    public synchronized void onResume() 
    {
        super.onResume();
         if (bluetoothService != null) 
        {
            if (bluetoothService.getState() == BluetoothService.STATE_NONE) 
            {
              bluetoothService.start();
              
            }
        }
    }

    /**
     * Sets up the bluetooth specific views.
     */
    private void setUpBluetooth() 
    {
    	//An array adapter to store the interaction between the devices
        bt_ConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        bt_ConversationView = (ListView) findViewById(R.id.in);
        bt_ConversationView.setAdapter(bt_ConversationArrayAdapter);
        bt_SendButton = (Button) findViewById(R.id.button_send);
        bt_SendButton.setOnClickListener(new OnClickListener() {
        	/**When pressed, the button is hidden, and various aspects and elements of the GUI are hidden or displayed. 
        	*THEN a message is sent to the opposing device containing the user's character data in a specially designed
        	*syntax that will convert it. 
        	*/
            public void onClick(View v) 
            {
            	if(bluetoothService.getState() == BluetoothService.STATE_CONNECTED){
    			attackButton.setVisibility(View.VISIBLE);
    			attackAmount.setVisibility(View.VISIBLE);
    			sword_icon.setVisibility(View.VISIBLE);
    			characterImage.setVisibility(View.VISIBLE);
    			healButton.setVisibility(View.VISIBLE);
    			healAmount.setVisibility(View.VISIBLE);
    			heal_icon.setVisibility(View.VISIBLE);
    			rollButton.setVisibility(View.VISIBLE);
    			battleText.setVisibility(View.VISIBLE);
    			battleLabel.setVisibility(View.VISIBLE);
    			enemyName.setVisibility(View.VISIBLE);
    			enemylevel.setVisibility(View.VISIBLE);
    			enemyMaxHPText.setVisibility(View.VISIBLE);
    			enemyCurrentHP.setVisibility(View.VISIBLE);
    			enemyMaxHPMax.setVisibility(View.VISIBLE);
    			enemyImage.setVisibility(View.VISIBLE);
    			bt_SendButton.setVisibility(View.GONE);  			
    			//Sends the character's data
    			String message = characterData();
                sendMessage(message);
    			//The game is now in session
    			gameInSession = true;
            	}
            	else{
            		Toast.makeText(BluetoothGameActivity.this, R.string.not_connected,
    	                    Toast.LENGTH_SHORT).show();               
            	}            
            }
        });

        //Obtain a bluetooth service
        bluetoothService = new BluetoothService(this, mHandler);
        //The buffer for outgoing messages
        bt_OutStringBuffer = new StringBuffer("");
    }

    /**
     * Unchanged method
     */
    @Override
    public synchronized void onPause() { super.onPause(); }

    /**
     * Unchanged method
     */
    @Override
    public void onStop() { super.onStop(); }

    /**
     * Called when the activity requires the bluetooth service to be stopped
     */
    @Override
    public void onDestroy() 
    {
        super.onDestroy();
        if (bluetoothService != null) bluetoothService.stop();
    }

    /**
     * This method makes the user's device discoverable to other devices
     */
    private void ensureDiscoverable() 
    {
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) 
        {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * This function handles the sending of messages to other devices
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) 
    {
    	//If not connected, stop the function.
        if (bluetoothService.getState() != BluetoothService.STATE_CONNECTED) 
        {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        //If the message containts data...
        if (message.length() > 0) 
        {
            //send the message
            byte[] send = message.getBytes();
            bluetoothService.write(send);
        }
    }

    /**
     * Unimplemented method
     */
    private final void setStatus(int resId) {}

    /**
     * Unimplemented method
     */
    private final void setStatus(CharSequence subTitle) {}

    /** 
     * This handler obtains messages to and from the bluetooth service
     */
    private final Handler mHandler = new Handler() 
    {
    	//Temporary string to hold incoming message parts
    	String tempWord = "";
    	//Temporary ints for loops
        int j = 0, i = 0;		
        @Override
        public void handleMessage(Message msg) 
        {
            switch (msg.what) 
            {
            
            //If the state of the connection is between states, messages are displayed to
            //the user to inform them
            case BT_STATE_CHANGE:
                switch (msg.arg1) 
                {
                case BluetoothService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, bt_ConnectedDeviceName));
                    bt_ConversationArrayAdapter.clear();
                    break;
                case BluetoothService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
                
                //If the state of message is a write-out...
            case BT_OUTGOING:
                byte[] writeBuf = (byte[]) msg.obj;
                
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                
                
                /*
                * If the message that is sent out starts with &, it means the battle has commenced
                *And the user will be informed
                */
                if(writeMessage.startsWith("&")){
                	bt_ConversationArrayAdapter.add("The battle has commenced!");
                }                
                
                /*
                 * If the message starts with *, it signals that the message is an attack on an opposing device.
                 * The string is stripped of its data and appended to the TextViews regarding the enemy's character
                 */
                if(writeMessage.startsWith("*")){               	
                	for(i=0; i < writeMessage.length(); i++)
                	{
                		if(writeMessage.charAt(i) != '*' && writeMessage.charAt(i) != '|')
                		{
                			tempWord = tempWord + writeMessage.charAt(i);
                		}
                		else if(writeMessage.charAt(i) == '|')
                		{
                			int damage = Integer.parseInt(tempWord.toString());
                			//User is informed of their attack
        					bt_ConversationArrayAdapter.add("You hit "+eName+" for " + damage + " damage!");
        					//A hitting sound is played
        					playSound(HIT_SOUND);
                		}	
                	}
                	tempWord="";
                	gameOver();
                }
                
                /*
                 * If the message starts with ^, it signals that the message is a heal on the user's character.
                 * The string is stripped of its data and appended to the TextViews regarding the user's character
                 */
                if(writeMessage.startsWith("^")){
                	
                	for(i=0; i < writeMessage.length(); i++)
                	{
                		if(writeMessage.charAt(i) != '^' && writeMessage.charAt(i) != '|')
                		{
                			tempWord = tempWord + writeMessage.charAt(i);
                		}
                		else if(writeMessage.charAt(i) == '|')
                		{
        					int heal = Integer.parseInt(tempWord.toString());
        					//User is informed of their heal
        					bt_ConversationArrayAdapter.add("You heal yourself for " + heal + " health!");
        					//Healing sound is played
        					playSound(HEAL_SOUND);
                		}	
                	}
                	tempWord="";
                	//Check if the game is over
                	gameOver();
                }                                           
                break;
                
                
                //If the state of the message is read-in
            case BT_INCOMING:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);               
                
                /*
                * If the message that is sent out starts with &, it means the battle has commenced
                *The message string will be stripped between "|" characters and the information obtained
                *will be placed inside the relevant enemy information TextViews
                */
                if(readMessage.startsWith("&")){
                	
                	for(i=0; i < readMessage.length(); i++)
                	{
                		if(readMessage.charAt(i) != '&' && readMessage.charAt(i) != '|')
                		{
                			tempWord = tempWord + readMessage.charAt(i);
                		}
                		else if(readMessage.charAt(i) == '|')
                		{
                			switch(j){
                			case 0:
                				enemyName.setText("Enemy name: " + tempWord.toString());
                				eName = tempWord.toString();
                				break;
                			case 1:
                				enemylevel.setText("Enemy level: " + tempWord.toString());
                				break;
                			case 2:		
                				enemyCurrentHP.setText(""+tempWord);
                				enemyMaxHPText.setText("Enemy hp: ");
                				enemyMaxHPMax.setText("/" +tempWord.toString());				
                				break;
                			}
                			tempWord = "";
                			j++;
                			
                		}	
                	}
                	//Play game commencing sound
                	playSound(START_SOUND);
                	//Check if the game is over
                	gameOver();
                }
                
                /*
                 * If the message starts with *, it signals that the message is an attack on the user's character.
                 * The string is stripped of its data and appended to the TextViews regarding the user's character
                 */
                if(readMessage.startsWith("*")){
                	
                	for(i=0; i < readMessage.length(); i++)
                	{
                		if(readMessage.charAt(i) != '*' && readMessage.charAt(i) != '|')
                		{
                			tempWord = tempWord + readMessage.charAt(i);
                		}
                		else if(readMessage.charAt(i) == '|')
                		{
        					int damage = Integer.parseInt(tempWord.toString());
        					int currentHealth = Integer.parseInt(characterCurrentHP.getText().toString());
        					characterCurrentHP.setText(""+(currentHealth-damage));
        					bt_ConversationArrayAdapter.add(eName+" hits you for " + damage + " damage!");
        					tempWord = "";
        					//Hurt sound is played
        					playSound(HURT_SOUND);
        	                //Triggers a translation animation on the enemy's icon to imply an attack has been made
        	                //on the user's character
        	                TranslateAnimation trans = new TranslateAnimation(0,20,0,20);	 
        	                trans.setDuration(500);
        	                enemyImage.startAnimation(trans);
                		}	
                	}
                	gameOver();
                	
                }
                
                /*
                 * If the message starts with ^, it signals that the message is a heal on the enemy's character.
                 * The string is stripped of its data and appended to the TextViews regarding the enemy's character
                 */
                if(readMessage.startsWith("^")){
                	
                	for(i=0; i < readMessage.length(); i++)
                	{
                		if(readMessage.charAt(i) != '^' && readMessage.charAt(i) != '|')
                		{
                			tempWord = tempWord + readMessage.charAt(i);
                		}
                		else if(readMessage.charAt(i) == '|')
                		{
        					int heal = Integer.parseInt(tempWord.toString());
        					int currentHealth = Integer.parseInt(enemyCurrentHP.getText().toString());
        					enemyCurrentHP.setText(""+(currentHealth+heal));
        					bt_ConversationArrayAdapter.add(eName+" heals themselves for " + heal + " health!");
        					tempWord = "";
        					//Healing sound is played
        					playSound(HEAL_SOUND);
        	                //Triggers an alpha animation on the enemy's icon to imply a heal has been made
        	                //on the enemy
        	                AlphaAnimation alpha = new AlphaAnimation(0,100);	 
        	                alpha.setDuration(500);
        	                enemyImage.startAnimation(alpha);
                		}	
                	}
                	gameOver();
                }
                
                /*
                 * If the message starts with $, the battle has ended. The user is informed 
                 * of the loot that they have won.
                 */
                if(readMessage.startsWith("$")){             	
                	for(i=0; i < readMessage.length(); i++)
                	{
                		if(readMessage.charAt(i) != '$' && readMessage.charAt(i) != '|')
                		{
                			tempWord = tempWord + readMessage.charAt(i);
                		}
                		else if(readMessage.charAt(i) == '|')
                		{
        					String loot = tempWord;
        					bt_ConversationArrayAdapter.add(eName+" drops " + loot + " gold pieces!");
        					//Changes the enemy's player icon to one that shows that it has been defeated
        					enemyImage.setImageResource(R.drawable.skeletondead);
        					tempWord = "";   
                		}	
                	}                	
                }               
                break;

                //If the message is in regards to the device name, save it and inform the user
                //of the connection.
            case BT_DEVICE_NAME:
                bt_ConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + bt_ConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };



    /**
     * This function occurs when the user returns from the bluetooth device connection
     * screen.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        switch (requestCode) 
        {
        //If the results are requesting to connect to a device, connect to it
        case REQUEST_CONNECT_DEVICE:          
            if (resultCode == Activity.RESULT_OK) 
            {
                connectDevice(data, false);
            }
            break;
        //If the results are requesting to enable bluetooth, enable it
        case REQUEST_ENABLE_BT:
            if (resultCode == Activity.RESULT_OK) 
            {
                setUpBluetooth();
            } 
            else 
            {
                //Inform the user that something has gone wrong, and the connection has failed
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * This function handles connecting to a device
     *
     * @param data the data
     * @param secure boolean regarding if the connection is secure
     */
    private void connectDevice(Intent data, boolean secure) 
    {
        String address = data.getExtras().getString(BluetoothListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        bluetoothService.connect(device);
    }

    /**
     * This function handles creating and inflating the options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    /**
     * This function handles what to do in regards to an option item being selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        Intent serverIntent = null;
        switch (item.getItemId()) 
        {
        //If the scan option is picked, the device will launch BluetoothListActivity and the 
        //user will be able to search for devices
        case R.id.scan:
            serverIntent = new Intent(this, BluetoothListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        
        //If the discoverable option is chosen, the device will enable itself to be discoverable via bluetooth
        //to other devices
        case R.id.discoverable:
            ensureDiscoverable();
            return true;
        }
        return false;
    }

	/**
	 * Handles what to do in the event the back button is pressed.
	 * Essentially confirms that the user wishes to exit the activity and bluetooth connection
	 */
	@Override
	public void onBackPressed() {	
		if(gameInSession){
		confirmExit();
		}
		else{
			finish();
		}
	}
	
    /**
     * This function provides the user with an alert dialog to double check that they wish to exit
     * the activity and/or bluetooth connection.
     */
    public void confirmExit() { 
    	
		gameInProgressDialogue = new AlertDialog.Builder(this)
		.setTitle("Are you sure you want to quit?")
		.setMessage("Quitting will disrupt the bluetooth connection")
		.setPositiveButton("Yes", new AlertDialog.OnClickListener()
		{
			//If yes is clicked, the activity is closed
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		})
		.setNeutralButton("Cancel", new AlertDialog.OnClickListener()
		{
			//If cancel is clicked, the user is returned to the session
			public void onClick(DialogInterface dialog, int which)
			{
				gameInProgressDialogue.cancel();
			}
		})			
		.create();
		gameInProgressDialogue.show();
    }
    
	/**
	 * In the event that the game is over, this function shall handle disabling aspects of
	 * the user interface.
	 */
	public void gameOver()
	{
		if(isGameOver()){
    		attackButton.setVisibility(View.INVISIBLE);
    		attackAmount.setVisibility(View.INVISIBLE);
    		sword_icon.setVisibility(View.INVISIBLE);
			healButton.setVisibility(View.INVISIBLE);
			healAmount.setVisibility(View.INVISIBLE);
			heal_icon.setVisibility(View.INVISIBLE);
			rollButton.setVisibility(View.INVISIBLE);
			
        }
	}
    
    /**
     * This function checks if the game is over. If it is, it returns a true value.
     *
     * @return true, if game is over
     */
    public boolean isGameOver()
    {
    	//Obtain both the user's character and the enemy's character health
    	int enemyHealth = Integer.parseInt(enemyCurrentHP.getText().toString());
    	int characterHealth = Integer.parseInt(characterCurrentHP.getText().toString());
    	
    	//If the enemy's health should reach below or equal to 0, the user wins and a winning sound is played
    	if(enemyHealth <= 0){
    		bt_ConversationArrayAdapter.add("You have won the battle!");
    		playSound(WIN_SOUND);
    		return true;
    	}
    	
    	//If the user's health should reach below or equal to 0, the enemy wins and a losing sound is played
    	if(characterHealth <= 0){
    		bt_ConversationArrayAdapter.add(eName+" has won the battle. You lose.");
    		bt_ConversationArrayAdapter.add("You have lost "+ getIntent().getExtras().get("loot").toString() +" gold");
    		sendMessage("$"+getIntent().getExtras().get("loot").toString()+"|");
    		//Changes the characters image to one that shows that they are defeated
    		characterImage.setImageResource(R.drawable.knightdead);
    		playSound(LOSE_SOUND);
    		return true;
    	}   	
    	return false;
    }
	

	
	/**
	 * This function sets up the sound for the activity
	 */
	public void setUpSound()
	{
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundsMap = new HashMap<Integer, Integer>();
        soundsMap.put(WIN_SOUND, soundPool.load(this, R.raw.win, 1));
        soundsMap.put(LOSE_SOUND, soundPool.load(this, R.raw.lose, 1));
        soundsMap.put(HIT_SOUND, soundPool.load(this, R.raw.hit, 1));
        soundsMap.put(HURT_SOUND, soundPool.load(this, R.raw.hurt, 1));
        soundsMap.put(START_SOUND, soundPool.load(this, R.raw.start, 1));
        soundsMap.put(HEAL_SOUND, soundPool.load(this, R.raw.heal, 1));        
	}
	
	/**
	 * This function plays a requested sound from the sound pool
	 *
	 * @param sound the sound
	 */
	public void playSound(int sound) {
        AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;  
 
        soundPool.play(soundsMap.get(sound), volume, volume, 1, 0, 1f);
        
	}
	
	/**
	 * This function sets up the battle text and labels
	 */
	public void setUpBattleData()
	{
		battleText = (TextView)findViewById(R.id.battle_text);		
		battleLabel = (TextView)findViewById(R.id.battle_label);
		battleLabel.setText("Battle Log: " + battleText.getText());	
	}
	
	/**
	 * This function sets up the user's character data that will be displayed and used
	 * throughout the battle
	 */
	public void setUpCharacterData()
	{
		characterName = (TextView)findViewById(R.id.bt_name);
		characterName.setText("Character name: " + getIntent().getExtras().get("name").toString());		
		characterlevel = (TextView)findViewById(R.id.bt_level);
		characterlevel.setText("Level: " + getIntent().getExtras().get("level").toString());		
		characterMaxHPText = (TextView)findViewById(R.id.bt_current_text);	
		currentHP = Integer.parseInt(getIntent().getExtras().get("hp").toString());		
		characterMaxHPText.setText("Current HP: ");		
		characterCurrentHP = (TextView)findViewById(R.id.bt_current_number);
		characterCurrentHP.setText(""+ currentHP);
		characterMaxHPMax = (TextView)findViewById(R.id.bt_current_max);
		characterMaxHPMax.setText("/" + getIntent().getExtras().get("hp").toString());	
		characterImage = (ImageView)findViewById(R.id.knight);		
	}
	
	/**
	 * This function sets up the enemy's character data that will be displayed and used
	 * throughout the battle
	 */
	public void setUpEnemyData()
	{
		enemyName = (TextView)findViewById(R.id.enemy_name);
		enemylevel = (TextView)findViewById(R.id.enemy_level);
		enemyMaxHPText =(TextView)findViewById(R.id.enemy_current_text);
		enemyCurrentHP = (TextView)findViewById(R.id.enemy_current_number);
		enemyMaxHPMax = (TextView)findViewById(R.id.enemy_current_max);	
		enemyImage = (ImageView)findViewById(R.id.skeleton);
	}
	
	
	/**
	 * This function sets up the interactive buttons of the activity (Attack, heal and dice interactions)
	 */
	public void setUpButtons()
	{		
		//Sets up the attack icon, EditText and button
		attackAmount = (EditText)findViewById(R.id.damage_amount);
		sword_icon = (ImageView)findViewById(R.id.sword_icon);	
		attackButton = (Button)findViewById(R.id.attack);
		attackButton.setOnClickListener(new OnClickListener() {
			//When pressed, it sends an attack command to the opposite device
			public void onClick(View v) {
				if(attackAmount.length() != 0){
					int damage = Integer.parseInt(attackAmount.getText().toString());
					int currentHealth = Integer.parseInt(enemyCurrentHP.getText().toString());
					enemyCurrentHP.setText(""+(currentHealth-damage));
					//Sends a message that begins with the attack command notation "*"
					String message = "*"+damage+"|";
	                sendMessage(message);
	                //Triggers a translation animation on the character's icon to imply an attack has been made
	                //on the enemy
	                TranslateAnimation trans = new TranslateAnimation(0,20,0,20);	 
	                trans.setDuration(500);
	                characterImage.startAnimation(trans);
				}				
			}
		});
		
		//Sets up the attack icon, EditText and button
		healAmount = (EditText)findViewById(R.id.heal_amount);
		heal_icon = (ImageView)findViewById(R.id.health_icon);				
		healButton = (Button)findViewById(R.id.heal);
		healButton.setOnClickListener(new OnClickListener() {
			//When pressed, it sends a heal command to the opposite device
			public void onClick(View v) {
				if(healAmount.length() != 0){
					int heal = Integer.parseInt(healAmount.getText().toString());
					int currentHealth = Integer.parseInt(characterCurrentHP.getText().toString());
					characterCurrentHP.setText(""+(currentHealth+heal));
					//Sends a message that begins with the attack command notation "^"
					String message = "^"+heal+"|";
	                sendMessage(message);
	                //Triggers an alpha animation on the character's icon to imply a heal has been made
	                //on themselves
	                AlphaAnimation alpha = new AlphaAnimation(0,100);	 
	                alpha.setDuration(500);
	                characterImage.startAnimation(alpha);
				}
			}
		});
		
		//Sets up the dice icon via an ImageView
		rollButton = (ImageView)findViewById(R.id.roll_dice);
		rollButton.setOnClickListener(new OnClickListener() {
			//When pressed, displays a toast message with the random results of 6 different dice
			public void onClick(View v) {
				Toast.makeText(BluetoothGameActivity.this, getRollResults(),
	                    Toast.LENGTH_LONG).show();			
			}
		});
	}
	
	/**
	 * Obtains a random number.
	 *
	 * @param diceNumber the dice number
	 * @return the random number
	 */
	public int getRandNumb(int diceNumber)
	{
		int result;
		Random random = new Random();
		result = random.nextInt(diceNumber) + 1;				
		return result;
	}
	
	/**
	 * Gets the roll results.
	 *
	 * @return the roll results
	 */
	public String getRollResults()
	{
		String results="";		
		results = "You rolled the following dice:\nD6: "+getRandNumb(4)+"\nD8: "+getRandNumb(8)+"\nD10: "+ getRandNumb(10)+"\nD12: "+getRandNumb(12)+"\nD20: "+getRandNumb(20);		
		return results;
	}
	
	/**
	 * This function handles returning all the character's data to be sent as a message to the opposing device.
	 *
	 * @return the character's data
	 */
	public String characterData()
	{
		String data;	
		data = ("&" + getIntent().getExtras().get("name").toString() + "|" + getIntent().getExtras().get("level").toString() + "|" + getIntent().getExtras().get("hp").toString() + "|");		
		return data;	
	}
	
}
