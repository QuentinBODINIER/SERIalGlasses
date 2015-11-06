package com.example.sg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;




public class SG extends Activity {

	public static Manager manager;
	public static BluetoothAdapter mBluetoothAdapter;
	int REQUEST_ENABLE_BT = 1;
	int REQUEST_CHOICES_MENU = 1;
	Button btnBT;
	ProgressBar progressBarBT;
	TextView tvFail;
	public static String DEVICE_IDENTIFIER = "ZbouboGlass1";
	public ConnectThread connectThread;
	public static ConnectedThread connectedThread;
	public static IncomingSMSReceiver incomingSMSReceiver;
	public static DateDisplayer dateDisplayer;
	
	
	
	
    // A function to manage Bluetooth 
    protected void enableBT(){
    	
    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {
    		//No bluetooth on device
    	}
    	else {
    		if (!mBluetoothAdapter.isEnabled()) {
    		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    		}
    		else{
    			connectToGlasses();
    		}
    	}
    }
    
    protected void connectToGlasses(){
    	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    	boolean success = false;
    	String DeviceName ="";
    	Context context = getApplicationContext();
		CharSequence text = "Looking for Smart Glasses";
		int duration = Toast.LENGTH_SHORT;
		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
    	if (pairedDevices.size() > 0) {
    	    // Loop through paired devices
    	    for (BluetoothDevice device : pairedDevices) {
    	    	DeviceName = device.getName();
    	    	Log.d("Paired Device", DeviceName);
    	        if(DeviceName.equals(DEVICE_IDENTIFIER)){
    	        	success = true;
    	        	connectThread = new ConnectThread(device);
    	        	connectThread.start();
    	        	break;
    	        }
    	    }
    	}
    	
    	if(!success){
    		context = getApplicationContext();
			text = "Please pair with Smart Glasses before running the app";
			duration = Toast.LENGTH_SHORT;
			
			toast = Toast.makeText(context, text, duration);
			toast.show();
			btnBT.setVisibility(View.VISIBLE);
			progressBarBT.setVisibility(View.GONE);
    	}
    	
    	
          
    }
    
    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data){
    	
    	if(requestCode == REQUEST_ENABLE_BT){
    		if(resultCode == RESULT_OK){
    			Context context = getApplicationContext();
    			CharSequence text = "Bluetooth Activated";
    			int duration = Toast.LENGTH_SHORT;

    			Toast toast = Toast.makeText(context, text, duration);
    			//btnBT.setVisibility(View.INVISIBLE);
    			progressBarBT.setVisibility(View.VISIBLE);
    			toast.show();
    			connectToGlasses();
    		}
    		else{
    			Context context = getApplicationContext();
    			CharSequence text = "Error enabling Bluetooth";
    			int duration = Toast.LENGTH_SHORT;

    			Toast toast = Toast.makeText(context, text, duration);
    			toast.show();
    			btnBT.setVisibility(View.VISIBLE);
    			btnBT.setOnClickListener(new View.OnClickListener() {
    	    	    @Override
    	    	    //enableBT();
    	    	    public void onClick(View v) {
    	    	    	startActivityForResult(   new Intent(getApplicationContext(), com.example.sg.ChoicesActivity.class),
    	    	                REQUEST_CHOICES_MENU);
    	    	    }
    	    	});
    			progressBarBT.setVisibility(View.GONE);
    		}
		}
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        manager = new Manager((Activity) this);
        incomingSMSReceiver = new IncomingSMSReceiver();
        dateDisplayer=new DateDisplayer();
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(),
          PhoneStateListener.LISTEN_CALL_STATE);
        //On instancie des trucs cool
    	progressBarBT = (ProgressBar) findViewById(R.id.progressBarConnect);
        enableBT();
        
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
    	enableBT();
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
     
        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) { }
            mmSocket = tmp;
        }
     
        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();
     
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.start();
            dateDisplayer.start();
          	startActivity(new Intent(getApplicationContext(), com.example.sg.ChoicesActivity.class));
    	    return;
    	    
            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);
        }
     
        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
    
    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
     
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
     
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
     
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
     
        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
     
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                           // .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
     
        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }
     
        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
        
        
    }
}


