package rejasupotaro.rebuild.activities;

import android.os.Bundle;
import android.view.Menu;

import rejasupotaro.rebuild.R;
import roboguice.activity.RoboFragmentActivity;

public class MainActivity extends RoboFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
