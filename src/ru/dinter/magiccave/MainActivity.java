package ru.dinter.magiccave;

import java.util.Timer;
import java.util.TimerTask;

import ru.dinter.magiccave.IO.ResourceLoader;
import ru.dinter.magiccave.view.CandleView;
import ru.dinter.magiccave.view.CaveView;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    private static final int UPDATE_RATE = 1000 / 30;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ResourceLoader.setResources(getResources());
        CandleView.loadBitmaps(ResourceLoader.newInstance());
        
        final CaveView caveView = (CaveView) findViewById(R.id.caveView1);
        final Handler handler = new Handler();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            
            @Override
            public void run() {
                handler.post(new Runnable() {
                    
                    @Override
                    public void run() {
                        caveView.invalidate();
                    }
                });
            }
        }, 0, UPDATE_RATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
