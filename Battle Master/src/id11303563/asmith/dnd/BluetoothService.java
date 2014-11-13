package id11303563.asmith.dnd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
/**
 * @author Alex
 * 
 * The BluetoothServiceClass
 * 
 * This class handles supervising the incoming, outcoming connections that involve bluetooth.
 * It does this through the use of threads and handlers. Messages that it receives or sends out will
 * be sent to the GUI activity class for the service (BluetoothGameActivity). It will also inform 
 * the user when a connection fails, drops, or when a device connects.
 */

public class BluetoothService 
{	
	/** The Constant name for service. */
	private static final String NAME = "Bluetooth";	
	/** The Constant unique UUID used throughout the service. */
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	/** The service's bluetooth adapter. */
	private final BluetoothAdapter bt_Adapter;	
	/** The service's bluetooth handler. */
	private final Handler bt_Handler;  	
	/** The service's bluetooth accepted thread. */
	private AcceptThread bt_AcceptedThread;	
	/** The service's bluetooth connect thread. */
	private ConnectThread bt_ConnectThread;	
	/** The service's bluetooth connected thread. */
	private ConnectedThread bt_ConnectedThread;	
	/** The service's bluetooth state. */
	private int bt_State;	
	/** The Constant value for no bluetooth state. */
	public static final int STATE_NONE = 0;   	
	/** The Constant value for a bluetooth listening state. */
	public static final int STATE_LISTEN = 1;  	
	/** The Constant value for a bluetooth connecting state */
	public static final int STATE_CONNECTING = 2; 	
	/** The Constant value for a bluetooth connected state. */
	public static final int STATE_CONNECTED = 3;  

	/**
	 * Starts a new Bluetooth service
	 * @param context  The class context
	 * @param handler  The class handler for messages
	 */
	public BluetoothService(Context context, Handler handler) {
	    bt_Adapter = BluetoothAdapter.getDefaultAdapter();
	    bt_State = STATE_NONE;
	    bt_Handler = handler;
	}

	/**
	 * This function sets the state of the connection
	 * @param state  the current connection state
	 */
	private synchronized void setState(int state) {
	    bt_State = state;
	    bt_Handler.obtainMessage(BluetoothGameActivity.BT_STATE_CHANGE, state, -1).sendToTarget();
	}

	/**
	 * Returns service's state. 
	 * */
	public synchronized int getState() {
	    return bt_State;
	}

	/**
	 * Commences the Bluetooth service. Will begin to listen for outside connections once set up.
	 * */
	public synchronized void start() {
	    //Cancels threads that may attempt to make a connection
	    if (bt_ConnectThread != null) {
	    	bt_ConnectThread.cancel(); 
	    	bt_ConnectThread = null;
	    	}
	    //Cancels threads that are running a connection
	    if (bt_ConnectedThread != null) {
	    	bt_ConnectedThread.cancel(); 
	    	bt_ConnectedThread = null;
	    	}
	    //The thread now listens for the Bluetooth Service
	    if (bt_AcceptedThread == null) {
	        bt_AcceptedThread = new AcceptThread();
	        bt_AcceptedThread.start();
	    }
	    setState(STATE_LISTEN);
	}

	/**
	 * Attempts to connect to a foreign device.
	 * @param device  The connecting device
	 */
	public synchronized void connect(BluetoothDevice device) {
	    //Cancels threads that may attempt to make a connection
	    if (bt_State == STATE_CONNECTING) {
	        if (bt_ConnectThread != null) {
	        	bt_ConnectThread.cancel(); 
	        	bt_ConnectThread = null;
	        }
	    }
	    //Cancels threads that are running a connection
	    if (bt_ConnectedThread != null) {
	    	bt_ConnectedThread.cancel(); 
	    	bt_ConnectedThread = null;
	    	}
	    //Attempts to connect to a device via a thread
	    bt_ConnectThread = new ConnectThread(device);
	    bt_ConnectThread.start();
	    setState(STATE_CONNECTING);
	}

	/**
	 * Once connected, this will handle maintaining the connection to the foreign
	 * device
	 * @param socket  The BluetoothSocket connection
	 * @param device  The connecting device
	 */
	public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
	    //Cancels the connection thread
	    if (bt_ConnectThread != null) {
	    	bt_ConnectThread.cancel(); 
	    	bt_ConnectThread = null;
	    	}
	    //Cancels any thread that has a connection
	    if (bt_ConnectedThread != null) {
	    	bt_ConnectedThread.cancel(); 
	    	bt_ConnectedThread = null;
	    	}
	    //Terminates the accept thread
	    if (bt_AcceptedThread != null) {
	    	bt_AcceptedThread.cancel();
	    	bt_AcceptedThread = null;
	    	}
	    //Starts the connection thread
	    bt_ConnectedThread = new ConnectedThread(socket);
	    bt_ConnectedThread.start();

	    //Send the name of the device to the GUI
	    Message msg = bt_Handler.obtainMessage(BluetoothGameActivity.BT_DEVICE_NAME);
	    Bundle bundle = new Bundle();
	    bundle.putString(BluetoothGameActivity.DEVICE_NAME, device.getName());
	    msg.setData(bundle);
	    bt_Handler.sendMessage(msg);
	    setState(STATE_CONNECTED);
	}

	/**
	 * Cancels all threads
	 */
	public synchronized void stop() {
	    if (bt_ConnectThread != null) {
	    	bt_ConnectThread.cancel(); 
	    	bt_ConnectThread = null;
	    	}
	    if (bt_ConnectedThread != null) {
	    	bt_ConnectedThread.cancel(); 
	    	bt_ConnectedThread = null;
	    	}
	    if (bt_AcceptedThread != null) {
	    	bt_AcceptedThread.cancel();
	    	bt_AcceptedThread = null;
	    	}
	    setState(STATE_NONE);
	}

	/**
	 * Send a message to the connected thread.
	 * @param out The bytes to write
	 */
	public void write(byte[] out) {
	    ConnectedThread ct;
	    synchronized (this) {
	        if (bt_State != STATE_CONNECTED) return;
	        ct = bt_ConnectedThread;
	    }
	    ct.write(out);
	}

	/**
	 * If the connection should fail, this function shall inform the user via the
	 * GUI of the activity running
	 */
	private void connectionFailed() {
	    setState(STATE_LISTEN);
	    Message msg = bt_Handler.obtainMessage(BluetoothGameActivity.MESSAGE_TOAST);
	    Bundle bundle = new Bundle();
	    bundle.putString(BluetoothGameActivity.TOAST, "Unable to connect device");
	    msg.setData(bundle);
	    bt_Handler.sendMessage(msg);
	}

	/**
	 * If the connection drops, this function shall inform the user via the GUI
	 * of the activity running
	 */
	private void connectionLost() {
	    setState(STATE_LISTEN);
	    Message msg = bt_Handler.obtainMessage(BluetoothGameActivity.MESSAGE_TOAST);
	    Bundle bundle = new Bundle();
	    bundle.putString(BluetoothGameActivity.TOAST, "Device connection was lost");
	    msg.setData(bundle);
	    bt_Handler.sendMessage(msg);
	}

	/**
	 * This class acts as a 'server-side client' and will listen for incoming connections
	 */
	private class AcceptThread extends Thread {
	    
		/**Local server socket*/
	    private final BluetoothServerSocket mmServerSocket;

	    public AcceptThread() {
	        BluetoothServerSocket tmp = null;
	        //Listens for new connections
	        try {
	            tmp = bt_Adapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
	        } catch (IOException e) {
	        }
	        mmServerSocket = tmp;
	    }

	    public void run() {
	        setName("AcceptThread");
	        BluetoothSocket socket = null;

	        // While the state is not connected, listen for connections.
	        while (bt_State != STATE_CONNECTED) {
	            try {
	                socket = mmServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }

	            // If a socket found has been accepted, attempt to synchronise depending on its state
	            if (socket != null) {
	                synchronized (BluetoothService.this) {
	                    switch (bt_State) {
	                    case STATE_LISTEN:
	                    case STATE_CONNECTING:
	                    	//Obtain the remote device
	                        connected(socket, socket.getRemoteDevice());
	                        break;
	                    case STATE_NONE:
	                    case STATE_CONNECTED:
	                        //The socket is closed
	                        try {
	                            socket.close();
	                        } catch (IOException e) {
	                        }
	                        break;
	                    }
	                }
	            }
	        }
	    }

	    /**
	     * Closes the server socket's connection
	     */
	    public void cancel() {
	        try {
	            mmServerSocket.close();
	        } catch (IOException e) {
	        }
	    }
	}


	/**
	 * This class handles what to do with an out going connection.
	 */
	private class ConnectThread extends Thread {
	    private final BluetoothSocket bt_t_Socket;
	    private final BluetoothDevice bt_t_Device;
	    public ConnectThread(BluetoothDevice device) {
	        bt_t_Device = device;
	        BluetoothSocket tmp = null;
	        //Obtain the bluetooth connection for an outside device
	        try {
	            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) {
	        }
	        bt_t_Socket = tmp;
	    }

	    public void run() {
	        setName("ConnectThread");
	        //Cancel the ability to discover connections once connected
	        bt_Adapter.cancelDiscovery();
	        //Connect to the bluetooth socket
	        try {
	            bt_t_Socket.connect();
	        } catch (IOException e) {
	            connectionFailed();
	            //If it fails, close the socket
	            try {
	                bt_t_Socket.close();
	            } catch (IOException e2) {
	            }
	            //Restart the listening service for the application
	            BluetoothService.this.start();
	            return;
	        }
	        synchronized (BluetoothService.this) {
	            bt_ConnectThread = null;
	        }
	        connected(bt_t_Socket, bt_t_Device);
	    }

	    /**
	     * Closes the socket connection
	     */
	    public void cancel() {
	        try {
	            bt_t_Socket.close();
	        } catch (IOException e) {
	        }
	    }
	}

	/**
	 * This class handles all incoming and outgoing messages via an InputStream and
	 * OutputStream
	 */
	private class ConnectedThread extends Thread {
	    private final BluetoothSocket bt_t_Socket;
	    private final InputStream bt_t_InStream;
	    private final OutputStream bt_t_OutStream;

	    public ConnectedThread(BluetoothSocket socket) {
	        bt_t_Socket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	        //Obtain the input and output stream
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) {
	        }
	        bt_t_InStream = tmpIn;
	        bt_t_OutStream = tmpOut;
	    }

	    public void run() {
	        byte[] buffer = new byte[1024];
	        int bytes;

	        while (true) {
	            try {
	                //Collect data from the incoming stream
	                bytes = bt_t_InStream.read(buffer);
	                //Send the obtained data to the GUI
	                bt_Handler.obtainMessage(BluetoothGameActivity.BT_INCOMING, bytes, -1, buffer)
	                        .sendToTarget();
	            } catch (IOException e) {
	                connectionLost();
	                break;
	            }
	        }
	    }

	    /**
	     * Handles the functionality of writing a message outwards.
	     * @param buffer  The bytes to write
	     */
	    public void write(byte[] buffer) {
	        try {
	        	//Outgoing message
	            bt_t_OutStream.write(buffer);
	            //Display the user sent message to their own GUI
	            bt_Handler.obtainMessage(BluetoothGameActivity.BT_OUTGOING, -1, -1, buffer)
	                    .sendToTarget();
	        } catch (IOException e) {
	        }
	    }

	    /**
	     * Closes the socket connection
	     */
	    public void cancel() {
	        try {
	            bt_t_Socket.close();
	        } catch (IOException e) {
	        }
	    }
	}
}
