package ee321.elbruz.soundfilter;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private MySoundRecorder soundRecord;
    private MySoundPlayer soundPlay;
    private TextView statusLabel;
    private TextView cutOffLabel;
    private TextView centerLabel;
    private TextView degreeLabel;
    private SeekBar cutOffBar;
    private SeekBar centerBar;
    private SeekBar degreeBar;
    private Handler poster;
    private Thread readThread;
    private int filterType = 0;
    private Filter filter;
    private int cutoffFreq;
    private int centerFreq;
    private int degree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        statusLabel = (TextView)findViewById(R.id.status_label);

        cutOffBar = (SeekBar)findViewById(R.id.cutofseek);
        centerBar = (SeekBar)findViewById(R.id.centerseek);
        degreeBar = (SeekBar)findViewById(R.id.degree_seek);

        cutOffLabel = (TextView)findViewById(R.id.cut_off_label);
        centerLabel = (TextView)findViewById(R.id.center_label);
        degreeLabel = (TextView)findViewById(R.id.degree_label);


        cutoffFreq = 1000;
        centerFreq = 10000;
        degree = 5;

        //Update components first
        cutOffBar.setProgress(cutoffFreq);
        centerBar.setProgress(centerFreq);
        degreeBar.setProgress(degree);

        cutOffLabel.setText("Cutoff Freq: " + cutoffFreq);
        centerLabel.setText("Center Freq: " + centerFreq);
        degreeLabel.setText("Filt Degree: " + degree);

        cutOffBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cutoffFreq = progress;
                cutOffLabel.setText("Cutoff Freq: " + cutoffFreq);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        centerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                centerFreq = progress;
                centerLabel.setText("Center Freq: " + centerFreq);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        degreeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                degree = progress;
                degreeLabel.setText("Filt Degree: " + degree);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        poster = new Handler();



        soundRecord = new MySoundRecorder();
        soundPlay = new MySoundPlayer(soundRecord.getBufferSize());
        filter = new Filter();

        soundRecord.startRecording();

        readThread = new Thread(new Runnable() {
            @Override
            public void run() {

                short[] arr = new short[soundRecord.getBufferSize()];
                short[] out = new short[arr.length];

                while(true) {

                    arr = soundRecord.read();

                    if(filterType == 0){
                        //out = arr;
                        soundPlay.play(arr);
                    }else if(filterType == 1){
                        out = filter.lpf(arr,cutoffFreq,44100,degree);
                        soundPlay.play(out);
                    }
                    else if(filterType == 2){
                        out = filter.hpf(arr,cutoffFreq,44100,degree);
                        soundPlay.play(out);
                    }
                    else if(filterType == 3){
                        out = filter.bpf(arr,cutoffFreq,centerFreq,44100,degree);
                        soundPlay.play(out);
                    }
                    else if(filterType == 4){
                        out = filter.bsf(arr, cutoffFreq, centerFreq, 44100, degree);
                        soundPlay.play(out);
                    }

                    /*final short inputMaxDis = findMaxDistance(arr);
                    final short outMaxDis = findMaxDistance(out);
                    final double gain = calculateDB(inputMaxDis,outMaxDis);

                    poster.post(new Runnable() {
                        @Override
                        public void run() {
                            inputLabel.setText("I: " + inputMaxDis);
                            outLabel.setText("O: "+outMaxDis);
                            gainLabel.setText("G: "+gain);
                        }
                    });*/

                }

            }

        });

        readThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*@Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        soundPlay.stopPlaying();
        soundRecord.stopRecording();
        finish();
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.no_filter:
                if (checked)
                    filterType = 0;
                    break;
            case R.id.lpf:
                if (checked)
                    filterType = 1;
                    break;
            case R.id.hpf:
                if (checked)
                    filterType = 2;
                break;
            case R.id.bpf:
                if (checked)
                    filterType = 3;
                break;
            case R.id.bsf:
                if (checked)
                    filterType = 4;
                break;
        }

        if (checked)
            statusLabel.setText("Filter Type: " + filterType);

    }

    public short findMaxDistance(short[] arr){

        short min = 0;
        short max = 0;

        for(int i = 0 ; i < arr.length ; i++){
            if(arr[i] > max)
                max = arr[i];
            else if(arr[i] < min)
                min = arr[i];
        }

        return (short)(max-min);

    }

    public double calculateDB(short first, short second){
        double a = (double)first;
        double b = (double)second;
        return 20 * Math.log(a/b);
    }

}
