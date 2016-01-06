package ee321.elbruz.soundfilter;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class MySoundRecorder {

    //Constants
    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private boolean isRecording = false;
    private int bufferSize;

    public MySoundRecorder(int bufferSize){

        this.bufferSize = bufferSize;


        //Init recorder
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLE_RATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufferSize);

    }

    public MySoundRecorder(){

        bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);

        //Init recorder
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLE_RATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, bufferSize);

    }



    public void startRecording(){
        recorder.startRecording();
        isRecording = true;
    }

    public void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public short[] read(){
        short sData[] = new short[bufferSize];
        recorder.read(sData, 0, bufferSize);
        return sData;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public boolean isRecording() {
        return isRecording;
    }

}