package com.example.sarah.baait_version1;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.media.AudioManager;
        import android.media.MediaPlayer;
        import android.media.RingtoneManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.text.Editable;
        import android.text.TextWatcher;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.CompoundButton;
        import android.widget.TextView;
        import android.widget.EditText;
        import android.widget.Button;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.math.BigDecimal;
        import java.math.RoundingMode;
        import java.util.Set;
        import java.util.UUID;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ToggleButton;

        import org.w3c.dom.Text;


/**
 * Created by sarah on 4/3/2018.
 */

public class TabOne extends Fragment implements graphControl

{

    TextView myLabel; //Label to display the temperature
    //Controls the bluetooth connection
    EditText myTextbox;
    BluetoothAdapter mBluetoothAdapter; //
    BluetoothSocket mmSocket;//takes the connection
    BluetoothDevice mmDevice;//contains th device
    OutputStream mmOutputStream;//to send data
    InputStream mmInputStream;//to receive data
    Thread workerThread;//contains the thread to built the data
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;


    ToggleButton celsiusButton; //toggles between celsius and Fahrenheit
    boolean isCelsius = true; // determines if a conversion occurs or not
    TextView CvF; //displays C or F for displayed value
    double thresh = 38.9;//in// celcius threshold for danger for the child
    double userthresh = 100;//user input threshold
    boolean ignore = false; //alert accepted
    TextView errors; //displays random messages as needed
    EditText threshinput; // takes the threshold input
    TextView threshC; //C or F depending on input type
    Button threshConfirm; //input threshold
    int places = 1;// rounding location


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //defines all objects in the UI
        View view = inflater.inflate(R.layout.tab_one, container, false);
        Button openButton = (Button)view.findViewById(R.id.open); //open bluetooth
        Button closeButton = (Button)view.findViewById(R.id.close); //close bluetooth
        final TextView CvF = (TextView) view.findViewById(R.id.textView2);//display C of F next to the the data
        myLabel = (TextView)view.findViewById(R.id.label); //display temp data
        errors = (TextView) view.findViewById(R.id.textView9);//displays random messages as needed
        threshC = (TextView) view.findViewById(R.id.textView10);//C or F depending on input type
        threshinput = (EditText) view.findViewById(R.id.editText2);// takes the threshold input
        threshConfirm = (Button)view.findViewById(R.id.button4);//input threshold


        threshConfirm.setOnClickListener(new View.OnClickListener() {//defines the behaviour for taking in the user defined threshold
            @Override
            public void onClick(View view) {
                double input = 0;
                try{//in case of none number input
                    input = Double.parseDouble(threshinput.getText().toString());
                }
                catch(Exception e){
                    errors.setText("Please Input Numbers Only");
                }
                if(!isCelsius){
                    input = (input - 32) * (5.0/9.0);//conversion if input is in celcius, stored in celcius
                }
                if(input < thresh){
                    userthresh = input;
                    String temp = "Current Threshold is " + toFah(Double.toString(userthresh)) ;
                    if(isCelsius){
                        temp += "C";
                    }
                    else{
                        temp += "F";
                    }
                    errors.setText(temp);

                }
                ignore  = false;//set alarm to occur new that threshold is set
            }
        });
        myLabel.addTextChangedListener(new TextWatcher() {//defines behavior for the object checking the input for exceeding the threshold
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {//defined for clarity
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            double compare;//value to use to compare against current
            if(userthresh > thresh ){//check to see if user threshold is lower than hardware threshold,
                compare = thresh;
            }
            else{
                compare = userthresh;
            }
            //uses the lowest of the thresholds
            double current = 0;
                //try/catch in case of non number in display
                try{
                current = Double.parseDouble(charSequence.toString());
            }catch(Exception e){
                    current = -100000;// if exception set current below threshold
                }
            if(!isCelsius){
                current = (current - 32) * (5.0/9.0); //set current to celsius
            }
            if((current >= compare) && (!ignore)){//builds alert if threshold reached and the user has acknowledged a previous alert
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //builder.setCancelable(true);
                //MEMES: will change later
                builder.setMessage("Temperature Exceeded Threshold");
                builder.setTitle("Check Your Baby!");
                AudioManager alarmManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);//build alarm objects
                final MediaPlayer alarm = MediaPlayer.create(getContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
                ignore = true;
               alarm.start();
                //cancel button
                builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        //errors.setText("Threshold maintained");
                        ignore = false;
                       alarm.stop();
                    }
                });
                //ok button
                builder.setPositiveButton("Im Going!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       ignore = true;
                       errors.setText("Please Input New Threshold");
                       alarm.stop();

                    }
                });
                builder.show(); //show to the alert
            }
            }

            @Override
            public void afterTextChanged(Editable editable) {//defined for clarity
            }
        });
        myLabel.setGravity(Gravity.CENTER);//set label text to center
        ToggleButton celsiusButton = (ToggleButton) view.findViewById(R.id.celsius); //define the toggleButton in XML
        celsiusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){//define behavior for the togglebutton
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //set mode to Fahrenheit
                    isCelsius = false;
                    CvF.setText("F");
                    threshC.setText("F");
                } else {
                    //set mode to Celcius
                    isCelsius = true;
                    CvF.setText("C");
                    threshC.setText("C");
                }
            }
        });
        //Open Button
        openButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    findBT();
                    openBT();
                }
                catch (IOException ex) { }
            }
        });


        //Close button
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    closeBT();
                }
                catch (IOException ex) { }
            }
        });
    return(view);
    }

    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            errors.setText("No bluetooth adapter available");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-06"))//name of the device to pair to
                {
                    mmDevice = device;
                    errors.setText("Bluetooth Device Found");// set error text when connected
                    break;
                }
            }
        }
        else{
            errors.setText("Bluetooth Device Not Found");
        }

    }

    void openBT() throws IOException// opens all of the input and output steams
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

        errors.setText("Bluetooth Opened");// set text when connection opened
    }

    @Override
    public void addToGraph(String s){
        try {
            Activity a = getActivity().getParent();
            ((graphControl) a).addToGraph(s);
        }
        catch(NullPointerException e){

        }
    }

    final String toFah(String s){//converts a string of C or F to the opposite
        double data = 0;
        //try catch if there isnt a number in the label
        try {
            data = Double.parseDouble(s);
        }
        catch(Exception e){
            return (s);
        }
        if(!isCelsius){//convert to fahrenhiet, data received in celsius
            data = data * 1.8 + 32;
        }
        //this is all for rounding to a software determined decimal place  (1)
        BigDecimal bd = new BigDecimal(Double.toString(data));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return Double.toString(bd.doubleValue());
    }

    void beginListenForData()//get data from the bluetooth connection
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character
        //threads to handle the UART connection style
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();//try to open the input stream
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];//fill arroay with incoming bytes
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];//convert bytes to ascii -> string
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = (new String(encodedBytes, "US-ASCII"));
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {


                                            myLabel.setText(toFah(data));// set the text of the label with conversion if necessary


                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b; // add data to buffer if not a complete byte
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }



    void closeBT() throws IOException//cancel bluetooth connection
    {
        stopWorker = true;//stop working
        //closes all of the input and output streams
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        errors.setText("Bluetooth Closed");//set the text when closed
        myLabel.setText("---");

                }



                }

