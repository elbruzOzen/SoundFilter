package ee321.elbruz.soundfilter;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class MySoundPlayer {

    private static final int SAMPLING_RATE = 44100;
    private static final int PLAYER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private static final int PLAYER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    AudioTrack mAudioTrack;
    int bufferSize;

    public MySoundPlayer(int bufferSize){

        this.bufferSize = bufferSize;

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLING_RATE, PLAYER_CHANNELS,
                PLAYER_AUDIO_ENCODING, bufferSize, AudioTrack.MODE_STREAM);

        mAudioTrack.play();

    }

    public void play(short[] arr) {

        mAudioTrack.write(arr, 0, bufferSize/2);
        mAudioTrack.play();
        mAudioTrack.flush();

    }

    public void stopPlaying(){
        if (null != mAudioTrack) {
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public static int getSamplingRate() {
        return SAMPLING_RATE;
    }

}