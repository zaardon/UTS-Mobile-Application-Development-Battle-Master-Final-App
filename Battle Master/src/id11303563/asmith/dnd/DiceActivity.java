package id11303563.asmith.dnd;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * @author Alex
 * 
 * The Class DiceActivity.
 * 
 * This class provides the means to roll 6 different types of sided dice (4,6,8,10,12 and 20 sides).
 * This class uses animations, sounds effects and an accelerometer to meet this need. When a user shakes
 * their phone when using this screen, any selected dice will animate to a RANDOMISED result (paired with 
 * a sound effect).
 */
public class DiceActivity extends Activity implements SensorEventListener {
	/** The d4 image view. */
	private ImageView d4ImageView;	
	/** The sensor manager. */
	private SensorManager sensorManager;	
	/** The accelerometer. */
	private Sensor imageAccelerometer;	
	/** The d6 image view. */
	private ImageView d6ImageView;	
	/** The d8 image view. */
	private ImageView d8ImageView;	
	/** The d10 image view. */
	private ImageView d10ImageView;	
	/** The d12 image view. */
	private ImageView d12ImageView;	
	/** The d20 image view. */
	private ImageView d20ImageView;	
	/** The sound pool. */
	private SoundPool soundPool;	
	/** The sounds map. */
	private HashMap<Integer, Integer> soundsMap;	
	/** The rolling sound. */
	private int ROLL_SOUND = 1;	
	/** The d4 rolling image view. */
	private ImageView d4sImageView;	
	/** The d6 rolling image view. */
	private ImageView d6sImageView;	
	/** The d8 rolling image view. */
	private ImageView d8sImageView;	
	/** The d10 rolling image view. */
	private ImageView d10sImageView;	
	/** The d12 rolling image view. */
	private ImageView d12sImageView;	
	/** The d20 rolling image view. */
	private ImageView d20sImageView;	
	/** The rotation animation */
	private RotateAnimation rotate;	
	/** The alpha animation. */
	private AlphaAnimation alpha;	
	/** Boolean to detect if the d4 is clicked */
	private boolean d4Clicked;	
	/** Boolean to detect if the d6 is clicked*/
	private boolean d6Clicked;	
	/** Boolean to detect if the d8 is clicked */
	private boolean d8Clicked;	
	/** Boolean to detect if the d10 is clicked */
	private boolean d10Clicked;	
	/** Boolean to detect if the d12 is clicked */
	private boolean d12Clicked;	
	/** Boolean to detect if the d20 is clicked */
	private boolean d20Clicked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dice_screen);		
		setUpViews();
	}		

	/**
	 * Sets up the dice views (6 different dice, accelerometer and sound effects)
	 */
	public void setUpViews()
	{
		setUpSound();
        setUpAccelerometer();
        setUpD4();
		setUpD6();
		setUpD8();
		setUpD10();
		setUpD12();
		setUpD20();
	}
	
	/**
	 * Sets up the accelerometer.
	 */
	public void setUpAccelerometer()
	{
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        imageAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, imageAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	/**
	 * Sets up the sound for the dice activity.
	 */
	public void setUpSound()
	{
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundsMap = new HashMap<Integer, Integer>();
        soundsMap.put(ROLL_SOUND, soundPool.load(this, R.raw.roll, 1));
	}
	
	/**
	 * Players the rolling dice sound when called.
	 */
	public void playSound() {
        AudioManager mgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;   
        soundPool.play(soundsMap.get(ROLL_SOUND), volume, volume, 1, 0, 1f);      
	}

    /** 
     * Re-registers the accelerometer upon resume
     */
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, imageAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Unregisters the accelerometer when paused
     */
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    
    /**
     * Unregisters the accelerometer when stopped
     */
    @Override
    protected void onStop()
    {
    	super.onStop();
    	sensorManager.unregisterListener(this);
        
    }

	/**
	 * Unimplemented accuracy sensor
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	/**
	 * When the phone is moved in a motion that makes either of the X, Y or Z axis go
	 * beyond 10m/s, it will trigger the dice to roll and play a rolling sound.
	 */
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];		
        if (x>10 || y>10 || z>10) {
        	playSound();
        	setD4();
    		setD6();
    		setD8();
    		setD10();
    		setD12();
    		setD20();  		
        }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
        	sensorManager.unregisterListener(this);
        }		
	}
	
	/**
	 * Sets the d4's NEW image (roll) based on its personal random number function
	 * called within a switch statement..
	 */
	public void setD4()
	{
		switch(d4Result()){
		case 1: d4sImageView.setImageResource(R.drawable.d4s1);			
				break;
		case 2: d4sImageView.setImageResource(R.drawable.d4s2);
				break;
		case 3: d4sImageView.setImageResource(R.drawable.d4s3);
				break;
		case 4: d4sImageView.setImageResource(R.drawable.d4s4);
				break;
		}
		D4RollAnimation();				
	}
	
	/**
	 * D4 result.
	 *
	 * @return the random result
	 */
	public int d4Result()
	{
		int result;
		Random random = new Random();
		result = random.nextInt(4) + 1;		
		return result;
	}
	
	/**
	 * Sets the d6's NEW image (roll) based on its personal random number function
	 * called within a switch statement..
	 */
	public void setD6()
	{		
		switch(d6Result()){
		case 1: d6sImageView.setImageResource(R.drawable.d6s1);
				break;
		case 2: d6sImageView.setImageResource(R.drawable.d6s2);
				break;
		case 3: d6sImageView.setImageResource(R.drawable.d6s3);	
				break;
		case 4: d6sImageView.setImageResource(R.drawable.d6s4);
				break;
		case 5: d6sImageView.setImageResource(R.drawable.d6s5);
				break;
		case 6: d6sImageView.setImageResource(R.drawable.d6s6);
				break;
		}
		D6RollAnimation();
	}
	
	/**
	 * D6 result.
	 *
	 * @return the random result
	 */
	public int d6Result()
	{
		int result;
		Random random = new Random();
		result = random.nextInt(6) + 1;		
		return result;
	}
	
	/**
	 * Sets the d8's NEW image (roll) based on its personal random number function
	 * called within a switch statement..
	 */
	public void setD8()
	{
		switch(d8Result()){
		case 1: d8sImageView.setImageResource(R.drawable.d8s1);
				break;
		case 2: d8sImageView.setImageResource(R.drawable.d8s2);
				break;
		case 3: d8sImageView.setImageResource(R.drawable.d8s3);	
				break;
		case 4: d8sImageView.setImageResource(R.drawable.d8s4);
				break;
		case 5: d8sImageView.setImageResource(R.drawable.d8s5);
				break;
		case 6: d8sImageView.setImageResource(R.drawable.d8s6);
				break;
		case 7: d8sImageView.setImageResource(R.drawable.d8s7);
				break;
		case 8: d8sImageView.setImageResource(R.drawable.d8s8);
				break;
		}
		D8RollAnimation();
	}
	
	/**
	 * D8 result.
	 *
	 * @return the random result
	 */
	public int d8Result()
	{
		int result;
		Random random = new Random();
		result = random.nextInt(8) + 1;		
		return result;
	}
	
	/**
	 * Sets the d10's NEW image (roll) based on its personal random number function
	 * called within a switch statement..
	 */
	public void setD10()
	{
		switch(d10Result()){
		case 1: d10sImageView.setImageResource(R.drawable.d10s1);
				break;
		case 2: d10sImageView.setImageResource(R.drawable.d10s2);
				break;
		case 3: d10sImageView.setImageResource(R.drawable.d10s3);	
				break;
		case 4: d10sImageView.setImageResource(R.drawable.d10s4);
				break;
		case 5: d10sImageView.setImageResource(R.drawable.d10s5);
				break;
		case 6: d10sImageView.setImageResource(R.drawable.d10s6);
				break;
		case 7: d10sImageView.setImageResource(R.drawable.d10s7);
				break;
		case 8: d10sImageView.setImageResource(R.drawable.d10s8);
				break;
		case 9: d10sImageView.setImageResource(R.drawable.d10s9);
				break;
		case 10: d10sImageView.setImageResource(R.drawable.d10s10);
				break;
		}
		D10RollAnimation();
	}
	
	/**
	 * D10 result.
	 *
	 * @return the random result
	 */
	public int d10Result()
	{
		int result;
		Random random = new Random();
		result = random.nextInt(10) + 1;		
		return result;
	}
	
	/**
	 * Sets the d12's NEW image (roll) based on its personal random number function
	 * called within a switch statement..
	 */
	public void setD12()
	{
		switch(d12Result()){
		case 1: d12sImageView.setImageResource(R.drawable.d12s1);
				break;
		case 2: d12sImageView.setImageResource(R.drawable.d12s2);
				break;
		case 3: d12sImageView.setImageResource(R.drawable.d12s3);	
				break;
		case 4: d12sImageView.setImageResource(R.drawable.d12s4);
				break;
		case 5: d12sImageView.setImageResource(R.drawable.d12s5);
				break;
		case 6: d12sImageView.setImageResource(R.drawable.d12s6);
				break;
		case 7: d12sImageView.setImageResource(R.drawable.d12s7);
				break;
		case 8: d12sImageView.setImageResource(R.drawable.d12s8);
				break;
		case 9: d12sImageView.setImageResource(R.drawable.d12s9);
				break;
		case 10: d12sImageView.setImageResource(R.drawable.d12s10);
				break;
		case 11: d12sImageView.setImageResource(R.drawable.d12s12);
				break;
		case 12: d12sImageView.setImageResource(R.drawable.d12s12);
				break;
		}
		D12RollAnimation();
	}
	
	/**
	 * D12 result.
	 *
	 * @return the random result
	 */
	public int d12Result()
	{
		int result;
		Random random = new Random();
		result = random.nextInt(12) + 1;		
		return result;
	}
	
	/**
	 * Sets the d20's NEW image (roll) based on its personal random number function
	 * called within a switch statement.
	 */
	public void setD20()
	{
		switch(d20Result()){
		case 1: d20sImageView.setImageResource(R.drawable.d20s1);
				break;
		case 2: d20sImageView.setImageResource(R.drawable.d20s2);
				break;
		case 3: d20sImageView.setImageResource(R.drawable.d20s3);	
				break;
		case 4: d20sImageView.setImageResource(R.drawable.d20s4);
				break;
		case 5: d20sImageView.setImageResource(R.drawable.d20s5);
				break;
		case 6: d20sImageView.setImageResource(R.drawable.d20s6);
				break;
		case 7: d20sImageView.setImageResource(R.drawable.d20s7);
				break;
		case 8: d20sImageView.setImageResource(R.drawable.d20s8);
				break;
		case 9: d20sImageView.setImageResource(R.drawable.d20s9);
				break;
		case 10: d20sImageView.setImageResource(R.drawable.d20s10);
				break;
		case 11: d20sImageView.setImageResource(R.drawable.d20s11);
				break;
		case 12: d20sImageView.setImageResource(R.drawable.d20s12);
				break;
		case 13: d20sImageView.setImageResource(R.drawable.d20s13);
				break;
		case 14: d20sImageView.setImageResource(R.drawable.d20s14);
				break;
		case 15: d20sImageView.setImageResource(R.drawable.d20s15);
				break;
		case 16: d20sImageView.setImageResource(R.drawable.d20s16);
				break;
		case 17: d20sImageView.setImageResource(R.drawable.d20s17);
				break;
		case 18: d20sImageView.setImageResource(R.drawable.d20s18);
				break;
		case 19: d20sImageView.setImageResource(R.drawable.d20s19);
				break;
		case 20: d20sImageView.setImageResource(R.drawable.d20s20);
				break;
		}
		D20RollAnimation();
	}
	
	/**
	 * D20 result.
	 *
	 * @return the random result
	 */
	public int d20Result()
	{
		int result;
		Random random = new Random();
		result = random.nextInt(20) + 1;		
		return result;
	}
	
	/**
	 * Sets up the d4.
	 */
	public void setUpD4()
	{
		d4ImageView = (ImageView)findViewById(R.id.d4);		
		d4ImageView.setImageResource(R.drawable.d4s4);
		d4sImageView = (ImageView)findViewById(R.id.d4s);		
		d4sImageView.setImageResource(R.drawable.d4s4);		
		d4ImageView.setOnClickListener(new OnClickListener() {	
			
			/* 
			 * If clicked, it will trigger for the rolling dice to appear below and will
			 * trigger the pressed dice to disappear in the form of alpha animations. 
			 */
			public void onClick(View arg0) {						
				alpha = new AlphaAnimation(1,0);
				alpha.setDuration(500);
				d4ImageView.startAnimation(alpha);
				d4ImageView.setVisibility(View.INVISIBLE);
				d4ImageView.setOnClickListener(null);				
				alpha = new AlphaAnimation(0,1);
				alpha.setDuration(500);
				d4sImageView.startAnimation(alpha);
				d4sImageView.setVisibility(View.VISIBLE);
				d4Clicked = true;
			}
		});	
		
		/*
		 * When clicked, it will set the rolling dice animation to be that of a 2160 degree rotation
		 * that is triggered when pressed or when the accelerometer is triggered. It also plays a rolling
		 * sound when either occurs.
		 */
		d4sImageView.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				D4RollAnimation();		
				playSound();
			}
		});
	}		

	/**
	 * Sets up the d6.
	 */
	public void setUpD6()
	{
		d6ImageView = (ImageView)findViewById(R.id.d6);
		d6ImageView.setImageResource(R.drawable.d6s6);
		d6sImageView = (ImageView)findViewById(R.id.d6s);
		d6sImageView.setImageResource(R.drawable.d6s6);
		
		d6ImageView.setOnClickListener(new OnClickListener() {
			
			/* 
			 * If clicked, it will trigger for the rolling dice to appear below and will
			 * trigger the pressed dice to disappear in the form of alpha animations. 
			 */
			public void onClick(View arg0) {					
				alpha = new AlphaAnimation(1,0);
				alpha.setDuration(500);
				d6ImageView.startAnimation(alpha);
				d6ImageView.setVisibility(View.INVISIBLE);
				d6ImageView.setOnClickListener(null);				
				alpha = new AlphaAnimation(0,1);
				alpha.setDuration(500);
				d6sImageView.startAnimation(alpha);
				d6sImageView.setVisibility(View.VISIBLE);
				d6Clicked=true;
			}
		});	
		
		/*
		 * When clicked, it will set the rolling dice animation to be that of a 2160 degree rotation
		 * that is triggered when pressed or when the accelerometer is triggered. It also plays a rolling
		 * sound when either occurs.
		 */
		d6sImageView.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				D6RollAnimation();
				playSound();
			}
		});
	}
	
	/**
	 * Sets up the d8.
	 */
	public void setUpD8()
	{
		d8ImageView = (ImageView)findViewById(R.id.d8);
		d8ImageView.setImageResource(R.drawable.d8s8);
		d8sImageView = (ImageView)findViewById(R.id.d8s);
		d8sImageView.setImageResource(R.drawable.d8s8);
		d8ImageView.setOnClickListener(new OnClickListener() {	
			
			/* 
			 * If clicked, it will trigger for the rolling dice to appear below and will
			 * trigger the pressed dice to disappear in the form of alpha animations. 
			 */
			public void onClick(View arg0) {				
				alpha = new AlphaAnimation(1,0);
				alpha.setDuration(500);
				d8ImageView.startAnimation(alpha);
				d8ImageView.setVisibility(View.INVISIBLE);
				d8ImageView.setOnClickListener(null);				
				alpha = new AlphaAnimation(0,1);
				alpha.setDuration(500);
				d8sImageView.startAnimation(alpha);
				d8sImageView.setVisibility(View.VISIBLE);
				d8Clicked=true;
			}
		});		
		
		/*
		 * When clicked, it will set the rolling dice animation to be that of a 2160 degree rotation
		 * that is triggered when pressed or when the accelerometer is triggered. It also plays a rolling
		 * sound when either occurs.
		 */
		d8sImageView.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				D8RollAnimation();
				playSound();
			}
		});
	}
	
	/**
	 * Sets up the d10.
	 */
	public void setUpD10()
	{
		d10ImageView = (ImageView)findViewById(R.id.d10);
		d10ImageView.setImageResource(R.drawable.d10s10);
		d10sImageView = (ImageView)findViewById(R.id.d10s);
		d10sImageView.setImageResource(R.drawable.d10s10);
		d10ImageView.setOnClickListener(new OnClickListener() {	
			
			/* 
			 * If clicked, it will trigger for the rolling dice to appear below and will
			 * trigger the pressed dice to disappear in the form of alpha animations. 
			 */
			public void onClick(View arg0) {				
				alpha = new AlphaAnimation(1,0);
				alpha.setDuration(500);
				d10ImageView.startAnimation(alpha);
				d10ImageView.setVisibility(View.INVISIBLE);
				d10ImageView.setOnClickListener(null);				
				alpha = new AlphaAnimation(0,1);
				alpha.setDuration(500);
				d10sImageView.startAnimation(alpha);
				d10sImageView.setVisibility(View.VISIBLE);
				d10Clicked=true;
			}
		});		
		
		/*
		 * When clicked, it will set the rolling dice animation to be that of a 2160 degree rotation
		 * that is triggered when pressed or when the accelerometer is triggered. It also plays a rolling
		 * sound when either occurs.
		 */
		d10sImageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				D10RollAnimation();
				playSound();
			}
		});
	}
	
	/**
	 * Sets up the d12.
	 */
	public void setUpD12()
	{
		d12ImageView = (ImageView)findViewById(R.id.d12);
		d12ImageView.setImageResource(R.drawable.d12s12);
		d12sImageView = (ImageView)findViewById(R.id.d12s);
		d12sImageView.setImageResource(R.drawable.d12s12);		
		d12ImageView.setOnClickListener(new OnClickListener() {
			/* 
			 * If clicked, it will trigger for the rolling dice to appear below and will
			 * trigger the pressed dice to disappear in the form of alpha animations. 
			 */
			public void onClick(View arg0) {				
				alpha = new AlphaAnimation(1,0);
				alpha.setDuration(500);
				d12ImageView.startAnimation(alpha);
				d12ImageView.setVisibility(View.INVISIBLE);
				d12ImageView.setOnClickListener(null);				
				alpha = new AlphaAnimation(0,1);
				alpha.setDuration(500);
				d12sImageView.startAnimation(alpha);
				d12sImageView.setVisibility(View.VISIBLE);
				d12Clicked=true;
			}
		});	
		
		/*
		 * When clicked, it will set the rolling dice animation to be that of a 2160 degree rotation
		 * that is triggered when pressed or when the accelerometer is triggered. It also plays a rolling
		 * sound when either occurs.
		 */
		d12sImageView.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				D12RollAnimation();
				playSound();				
			}
		});
	}
	
	/**
	 * Sets up the d20.
	 */
	public void setUpD20()
	{
		d20ImageView = (ImageView)findViewById(R.id.d20);
		d20ImageView.setImageResource(R.drawable.d20s20);
		d20sImageView = (ImageView)findViewById(R.id.d20s);
		d20sImageView.setImageResource(R.drawable.d20s20);	
		d20ImageView.setOnClickListener(new OnClickListener() {	
			
			/* 
			 * If clicked, it will trigger for the rolling dice to appear below and will
			 * trigger the pressed dice to disappear in the form of alpha animations. 
			 */
			public void onClick(View arg0) {				
				alpha = new AlphaAnimation(1,0);
				alpha.setDuration(500);
				d20ImageView.startAnimation(alpha);
				d20ImageView.setVisibility(View.INVISIBLE);
				d20ImageView.setOnClickListener(null);				
				alpha = new AlphaAnimation(0,1);
				alpha.setDuration(500);
				d20sImageView.startAnimation(alpha);
				d20sImageView.setVisibility(View.VISIBLE);
				d20Clicked=true;
			}
		});
		
		/*
		 * When clicked, it will set the rolling dice animation to be that of a 2160 degree rotation
		 * that is triggered when pressed or when the accelerometer is triggered. It also plays a rolling
		 * sound when either occurs.
		 */
		d20sImageView.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				D20RollAnimation();
				playSound();
			}
		});
	}
	
	
	/**
	 * D4 roll animation that is of a rotation of 2160 degrees.
	 */
	public void D4RollAnimation()
	{
		if(d4Clicked){
		rotate=new RotateAnimation(0, 2160);
		rotate.setDuration(2000);			
		d4sImageView.bringToFront();
		d4sImageView.startAnimation(rotate);
		}
	}
	
	/**
	 * D6 roll animation that is of a rotation of 2160 degrees.
	 */
	public void D6RollAnimation()
	{
		if(d6Clicked){
		rotate=new RotateAnimation(0, 2160);
		rotate.setDuration(2000);			
		d6sImageView.bringToFront();
		d6sImageView.startAnimation(rotate);
		}
	}
	
	/**
	 * D8 roll animation that is of a rotation of 2160 degrees.
	 */
	public void D8RollAnimation()
	{
		if(d8Clicked){
		rotate=new RotateAnimation(0, 2160);
		rotate.setDuration(2000);			
		d8sImageView.bringToFront();
		d8sImageView.startAnimation(rotate);
		}
	}
	
	/**
	 * D10 roll animation that is of a rotation of 2160 degrees.
	 */
	public void D10RollAnimation()
	{
		if(d10Clicked){
		rotate=new RotateAnimation(0, 2160);
		rotate.setDuration(2000);			
		d10sImageView.bringToFront();
		d10sImageView.startAnimation(rotate);
		}
	}
	
	/**
	 * D12 roll animation that is of a rotation of 2160 degrees.
	 */
	public void D12RollAnimation()
	{
		if(d12Clicked){
		rotate=new RotateAnimation(0, 2160);
		rotate.setDuration(2000);
		d12sImageView.bringToFront();
		d12sImageView.startAnimation(rotate);
		}
	}
	
	/**
	 * D20 roll animation that is of a rotation of 2160 degrees.
	 */
	public void D20RollAnimation()
	{
		if(d20Clicked){
		rotate=new RotateAnimation(0, 2160);
		rotate.setDuration(2000);			
		d20sImageView.bringToFront();
		d20sImageView.startAnimation(rotate);
		}
	}
}
