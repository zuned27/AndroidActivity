package androidclub.assignment2;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    private TextView txtmsg,macid;
    EditText regno;
    Button btn;
    //ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //pb=(ProgressBar)findViewById(R.id.progressBar);
        //pb.setVisibility(View.GONE);
        macid = (TextView) findViewById(R.id.MacAddress);
        txtmsg = (TextView) findViewById(R.id.httpsreq);
        btn = (Button) findViewById(R.id.btn);
        regno = (EditText) findViewById(R.id.regno);
        final StringBuilder res1 = new StringBuilder();
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {

                }
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
            }
        } catch (Exception ex) {
        }
        macid.setText("MAC: "+res1.toString());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String r =regno.getText().toString();
                String mac =res1.toString();
                new serve().execute(r,mac);

            }
        });
    }
    public class serve extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //pb.setVisibility(View.VISIBLE);
            txtmsg.setVisibility(View.GONE);
            //pb.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
           // pb.setProgress(10);
        }

        @Override
        protected String doInBackground(String... input) {
            String r=input[0];
            String m=input[1];
            String u="https://android-club-project.herokuapp.com/upload_details?reg_no="+r.trim()+"&mac="+m.trim();
            try {
                URL url=new URL(u);

                HttpsURLConnection httpURLConnection= (HttpsURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");





                //InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
                InputStream inputStreamReader=new BufferedInputStream(httpURLConnection.getInputStream());
                //Log.d("what hapen", inputStreamReader.toString());
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStreamReader));
                StringBuilder stringBuilder = new StringBuilder();

                //Log.d("what url",u);
                String reading;
                //Log.d("what buffer",reader.readLine());
                if((reading=reader.readLine())!=null)
                    stringBuilder.append(reading);
                reader.close();
                String res=stringBuilder.toString().substring(0,m.length());
                httpURLConnection.disconnect();

                return res;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Not successful";
        }

        @Override
        protected void  onPostExecute(String res){
            super.onPostExecute(res);
            //pb.setVisibility(View.GONE);
            txtmsg.setVisibility(View.VISIBLE);
            txtmsg.setText(res);
        }

    }

}