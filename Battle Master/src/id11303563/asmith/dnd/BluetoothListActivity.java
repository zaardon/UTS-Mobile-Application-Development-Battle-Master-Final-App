	package id11303563.asmith.dnd;
	
	import java.util.Set;
	import android.app.Activity;
	import android.bluetooth.BluetoothAdapter;
	import android.bluetooth.BluetoothDevice;
	import android.content.BroadcastReceiver;
	import android.content.Context;
	import android.content.Intent;
	import android.content.IntentFilter;
	import android.os.Bundle;
	import android.view.View;
	import android.view.Window;
	import android.view.View.OnClickListener;
	import android.widget.AdapterView;
	import android.widget.ArrayAdapter;
	import android.widget.Button;
	import android.widget.ListView;
	import android.widget.TextView;
	import android.widget.AdapterView.OnItemClickListener;
	
/**
	 * This activity provides an interface in which the user can attempt to connect
	 * to other devices that are current broadcasting on a bluetooth network.
	 */
	public class BluetoothListActivity extends Activity {
	
	/** A string of device addresses. */
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	/** The bluetooth adapter for the activity. */
	private BluetoothAdapter bt_Adapter;
	
	/** An array of paired devices for the bluetooth adapter. */
	private ArrayAdapter<String> bt_PairedDevicesArrayAdapter;
	
	/** An array of new devices for the bluetooth adapter. */
	private ArrayAdapter<String> bt_NewDevicesArrayAdapter;
	
	/**
	 * Occurs upon the creation of the activity.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setUpViews();
	    registerFilters();
	    setUpAdapter();	    
	}
	
	/**
	 * Sets up the activities views.
	 */
	public void setUpViews()
	{
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    setContentView(R.layout.device_list);
	    setResult(Activity.RESULT_CANCELED);
	
	    Button scanButton = (Button) findViewById(R.id.button_scan);
	    scanButton.setOnClickListener(new OnClickListener() {
	    	//When clicked, it sets the user's device to search for other bluetooth devices
	        public void onClick(View v) {
	            doDiscovery();
	            v.setVisibility(View.GONE);
	        }
	    });
	    //An array of paired devices
	    bt_PairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
	    //An array of new devices
	    bt_NewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);	
	    //Sets pairedListView with items from the paired devices array
	    ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
	    pairedListView.setAdapter(bt_PairedDevicesArrayAdapter);
	    pairedListView.setOnItemClickListener(bt_DeviceClickListener);
	   //Sets newDevicesListView with items from the new devices array
	    ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
	    newDevicesListView.setAdapter(bt_NewDevicesArrayAdapter);
	    newDevicesListView.setOnItemClickListener(bt_DeviceClickListener);	
	}
	
	/**
	 * Registers the class' filters.
	 */
	public void registerFilters()
	{
	    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    this.registerReceiver(bt_Receiver, filter);
	    filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	    this.registerReceiver(bt_Receiver, filter);
	}
	
	/**
	 * Sets the up adapter.
	 */
	public void setUpAdapter()
	{
	
	    //Obtains the default bluetooth adapter
	    bt_Adapter = BluetoothAdapter.getDefaultAdapter();	
	    //Obtain a list of paired devices
	    Set<BluetoothDevice> pairedDevices = bt_Adapter.getBondedDevices();	
	    //Add the paired devices to the array
	    if (pairedDevices.size() > 0) {
	        findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
	        for (BluetoothDevice device : pairedDevices) {
	            bt_PairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	        }
	    } else {
	        String noDevices = getResources().getText(R.string.none_paired).toString();
	        bt_PairedDevicesArrayAdapter.add(noDevices);
	    }
	}
	/**
	 * This function handles what to do in the event that the receiver and
	 * adapter must be terminated.
	 */
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (bt_Adapter != null) {
	        bt_Adapter.cancelDiscovery();
	    }
	    this.unregisterReceiver(bt_Receiver);
	}
	
	/**
	 * This function scans for other bluetooth device signals.
	 */
	private void doDiscovery() {
	    setProgressBarIndeterminateVisibility(true);
	    setTitle(R.string.scanning);
	    findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
	
	    //Cancel the current discovering mode (if it is occuring)
	    if (bt_Adapter.isDiscovering()) {
	        bt_Adapter.cancelDiscovery();
	    }
	    //Begin to search for other devices
	    bt_Adapter.startDiscovery();
	}
	
	/** Handles what to do when a listed device is clicked from a list of discovery results. */
	private OnItemClickListener bt_DeviceClickListener = new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
	        //Stops the discovery process when attempting to connect
	        bt_Adapter.cancelDiscovery();
	        //Obtain the MAC address
	        String info = ((TextView) v).getText().toString();
	        String address = info.substring(info.length() - 17);
	        //Create an intent with the MAC address
	        Intent intent = new Intent();
	        intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
	        setResult(Activity.RESULT_OK, intent);
	        //Return back to the main bluetooth screen
	        finish();
	    }
	};
	
	/** This BroadcastReceiver acts as listener for discovered items. */
	private final BroadcastReceiver bt_Receiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();	
	        //When a device is found...
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            //Obtain the device's intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            //Check if it is already paired
	            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	                bt_NewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	            }
	        //Pair the device once finished
	        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	            setProgressBarIndeterminateVisibility(false);
	            setTitle(R.string.select_device);
	            if (bt_NewDevicesArrayAdapter.getCount() == 0) {
	                String noDevices = getResources().getText(R.string.none_found).toString();
	                bt_NewDevicesArrayAdapter.add(noDevices);
	            }
	        }
	    }
	};
	
	}