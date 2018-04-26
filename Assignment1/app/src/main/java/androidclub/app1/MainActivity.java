package androidclub.app1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button b1;
    TextView t1;
    private static int counter=0;
    private String val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.btn);
        t1 = (TextView)findViewById(R.id.btnCount);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                val = Integer.toString(counter);
                t1.setText(val);
            }
        });
    }
}
