package tbs.jumpsnew.utility;

import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;

import tbs.jumpsnew.Game;

public class BeatDetectorByFrequency {
    private static final String TAG = "TEST";
    // FREQS
    private static final int LOW_FREQUENCY = 300;
    private static final int MID_FREQUENCY = 2500;
    private static final int HIGH_FREQUENCY = 10000;
    private Visualizer mVisualizer = null;
    private double mRunningSoundAvg[];
    private double mCurrentAvgEnergyOneSec[];
    private int mNumberOfSamplesInOneSec;
    private long mSystemTimeStartSec;
    private OnBeatDetectedListener onBeatDetectedListener = null;

    public BeatDetectorByFrequency() {
        init();
    }

    private void init() {
        mRunningSoundAvg = new double[3];
        mCurrentAvgEnergyOneSec = new double[3];
        mCurrentAvgEnergyOneSec[0] = -1;
        mCurrentAvgEnergyOneSec[1] = -1;
        mCurrentAvgEnergyOneSec[2] = -1;
    }

    public void link(MediaPlayer player) {
        if (player == null) {
            throw new NullPointerException("Cannot link to null MediaPlayer");
        }
        mVisualizer = new Visualizer(player.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer,
                                              byte[] bytes, int samplingRate) {
                // DO NOTHING
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                         int samplingRate) {
                updateVisualizerFFT(bytes);
            }
        };

        mVisualizer.setDataCaptureListener(captureListener,
                Visualizer.getMaxCaptureRate() / 2, false, true);
        mVisualizer.setEnabled(true);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });
        mSystemTimeStartSec = System.currentTimeMillis();
    }

    public void release() {
        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
            mVisualizer.release();
        }
    }

    public void pause() {
        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
        }
    }

    public void resume() {
        if (mVisualizer != null) {
            mVisualizer.setEnabled(true);
        }
    }

    public void updateVisualizerFFT(byte[] audioBytes) {
        int energySum = 0;
        energySum += Math.abs(audioBytes[0]);
        int k = 2;
        double captureSize = mVisualizer.getCaptureSize() / 2;
        int sampleRate = mVisualizer.getSamplingRate() / 2000;
        double nextFrequency = ((k / 2) * sampleRate) / (captureSize);
        while (nextFrequency < LOW_FREQUENCY) {
            energySum += Math.sqrt((audioBytes[k] * audioBytes[k])
                    * (audioBytes[k + 1] * audioBytes[k + 1]));
            k += 2;
            nextFrequency = ((k / 2) * sampleRate) / (captureSize);
        }
        double sampleAvgAudioEnergy = (double) energySum / ((k * 1.0) / 2.0);

        mRunningSoundAvg[0] += sampleAvgAudioEnergy;
        if ((sampleAvgAudioEnergy > mCurrentAvgEnergyOneSec[0])
                && (mCurrentAvgEnergyOneSec[0] > 0)) {
            fireBeatDetectedLowEvent(sampleAvgAudioEnergy);
        }
        energySum = 0;
        while (nextFrequency < MID_FREQUENCY) {
            energySum += Math.sqrt((audioBytes[k] * audioBytes[k])
                    * (audioBytes[k + 1] * audioBytes[k + 1]));
            k += 2;
            nextFrequency = ((k / 2) * sampleRate) / (captureSize);
        }

        sampleAvgAudioEnergy = (double) energySum / ((k * 1.0) / 2.0);
        mRunningSoundAvg[1] += sampleAvgAudioEnergy;
        if ((sampleAvgAudioEnergy > mCurrentAvgEnergyOneSec[1])
                && (mCurrentAvgEnergyOneSec[1] > 0)) {
            fireBeatDetectedMidEvent(sampleAvgAudioEnergy);
        }
        energySum = Math.abs(audioBytes[1]);

        while ((nextFrequency < HIGH_FREQUENCY) && (k < audioBytes.length)) {
            energySum += Math.sqrt((audioBytes[k] * audioBytes[k])
                    * (audioBytes[k + 1] * audioBytes[k + 1]));
            k += 2;
            nextFrequency = ((k / 2) * sampleRate) / (captureSize);
        }

        sampleAvgAudioEnergy = (double) energySum / ((k * 1.0) / 2.0);
        mRunningSoundAvg[2] += sampleAvgAudioEnergy;
        if ((sampleAvgAudioEnergy > mCurrentAvgEnergyOneSec[2])
                && (mCurrentAvgEnergyOneSec[2] > 0)) {
            fireBeatDetectedHighEvent(sampleAvgAudioEnergy);
        }

        mNumberOfSamplesInOneSec++;
        if ((System.currentTimeMillis() - mSystemTimeStartSec) > 1000) {
            mCurrentAvgEnergyOneSec[0] = mRunningSoundAvg[0]
                    / mNumberOfSamplesInOneSec;
            mCurrentAvgEnergyOneSec[1] = mRunningSoundAvg[1]
                    / mNumberOfSamplesInOneSec;
            mCurrentAvgEnergyOneSec[2] = mRunningSoundAvg[2]
                    / mNumberOfSamplesInOneSec;
            mNumberOfSamplesInOneSec = 0;
            mRunningSoundAvg[0] = 0.0;
            mRunningSoundAvg[1] = 0.0;
            mRunningSoundAvg[2] = 0.0;
            mSystemTimeStartSec = System.currentTimeMillis();
        }
    }

    // USE INTERFACES IN NEXT UPDATE:
    private void fireBeatDetectedLowEvent(double power) {
        // Utility.log("LOW BEAT DETECTED!");
        Game.lowBeat(power);
        if (onBeatDetectedListener != null) {
            onBeatDetectedListener.onBeatDetectedLow();
        }
    }

    private void fireBeatDetectedMidEvent(double power) {
        // Utility.log("MEDIUM BEAT DETECTED!");
        Game.mediumBeat(power);
        if (onBeatDetectedListener != null) {
            onBeatDetectedListener.onBeatDetectedMid();
        }
    }

    private void fireBeatDetectedHighEvent(double power) {
        // Utility.log("HIGH BEAT DETECTED!");
        Game.highBeat(power);
        if (onBeatDetectedListener != null) {
            onBeatDetectedListener.onBeatDetectedHigh();
        }
    }

    public void setOnBeatDetectedListener(OnBeatDetectedListener listener) {
        onBeatDetectedListener = listener;
    }

    public interface OnBeatDetectedListener {
        public abstract void onBeatDetectedLow();

        public abstract void onBeatDetectedMid();

        public abstract void onBeatDetectedHigh();
    }
}
