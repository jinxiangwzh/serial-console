package android_serialport_api;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity{
    /** Called when the activity is first created. */
    EditText mReception;
    FileOutputStream mOutputStream;
    FileInputStream mInputStream;
    SerialPort sp;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button buttonSetup = (Button)findViewById(R.id.button_1);
        buttonSetup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mReception = (EditText) findViewById(R.id.recvText);

                try {
                    sp = new SerialPort(new File("/dev/ttyS2"),9600,0);
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mOutputStream=(FileOutputStream) sp.getOutputStream();
                mInputStream=(FileInputStream) sp.getInputStream();

                Toast.makeText(getApplicationContext(), "open",
                        Toast.LENGTH_SHORT).show();

            }
        });

        final Button buttonsend = (Button)findViewById(R.id.button_2);
        buttonsend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    mOutputStream.write(new String("send").getBytes());
                    mOutputStream.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "send",
                        Toast.LENGTH_SHORT).show();
            }
        });

        final Button buttonrec= (Button)findViewById(R.id.button_3);
        buttonrec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int size;

                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

            }
        });
    }
    void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                    mReception.append(new String(buffer, 0, size));
                }
            }
        });
    }


}