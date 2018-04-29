package com.example.sarah.baait_version1;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.content.DialogInterface;
        import android.content.Intent;
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

public class TabOne extends Fragment

{

    TextView myLabel;
    EditText myTextbox;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    ToggleButton celsiusButton;
    boolean isCelsius = true;
    TextView CvF;
    double thresh = 38.9;//in// celcius
    double userthresh = 100;
    boolean inputCel = true;
    boolean ignore = false;
    TextView errors;
    EditText threshinput;
    TextView threshC;
    Button threshConfirm;
    int places = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.tab_one, container, false);
        Button openButton = (Button)view.findViewById(R.id.open);
        Button closeButton = (Button)view.findViewById(R.id.close);
        final TextView CvF = (TextView) view.findViewById(R.id.textView2);
        myLabel = (TextView)view.findViewById(R.id.label);
        errors = (TextView) view.findViewById(R.id.textView9);
        threshC = (TextView) view.findViewById(R.id.textView10);
        threshinput = (EditText) view.findViewById(R.id.editText2);
        threshConfirm = (Button)view.findViewById(R.id.button4);
        threshConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double input = 0;
                try{
                    input = Double.parseDouble(threshinput.getText().toString());
                }
                catch(Exception e){
                    errors.setText("Please Input Numbers Only");
                }
                if(!isCelsius){
                    input = (input - 32) * (5.0/9.0);
                }
                if(input < thresh){
                    userthresh = input;
                    errors.setText("Input Accepted");

                }
                ignore  = false;
            }
        });
        myLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            double compare;
            if(userthresh > thresh ){
                compare = thresh;
            }
            else{
                compare = userthresh;
            }
            double current = 0;
                try{
                current = Double.parseDouble(charSequence.toString());
            }catch(Exception e){
                    current = -99;
                }
            if(!isCelsius){
                current = (current - 32) * (5.0/9.0);
            }
            if((current >= compare) && (!ignore)){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setMessage("Temperature Exceeded Threshold");
                builder.setTitle("Check Your Baby!");
                ignore = true;
                builder.setNegativeButton("Let Him/Her Die", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        errors.setText("You are a Terrible Parent");
                        ignore = false;
                    }
                });
                builder.setPositiveButton("Im Going!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       ignore = true;
                       errors.setText("Please Input New Threshold");

                    }
                });
                builder.show();
            }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        myLabel.setGravity(Gravity.CENTER);
        ToggleButton celsiusButton = (ToggleButton) view.findViewById(R.id.celsius);
        celsiusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCelsius = false;
                    CvF.setText("F");
                    threshC.setText("F");
                } else {
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
                if(device.getName().equals("HC-06"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        errors.setText("Bluetooth Device Found");
    }

    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

        errors.setText("Bluetooth Opened");
    }

    final String toFah(String s){
        double data = 0;
        try {
            data = Double.parseDouble(s);
        }
        catch(Exception e){
            return (s);
        }
        if(!isCelsius){
            data = data * 1.8 + 32;
        }
        BigDecimal bd = new BigDecimal(Double.toString(data));
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return Double.toString(bd.doubleValue());
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

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
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = (new String(encodedBytes, "US-ASCII"));
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {


                                            myLabel.setText(toFah(data));


                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
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



    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        errors.setText("Bluetooth Closed");
        myLabel.setText("---");
        //CvF.setText("---");
    }



}

