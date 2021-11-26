package de.infynytyyy.serialmusic;

import jm.audio.Instrument;
import jm.audio.io.SampleOut;
import jm.audio.synth.EnvPoint;
import jm.audio.synth.Envelope;
import jm.audio.synth.Oscillator;
import jm.audio.synth.StereoPan;
import jm.audio.synth.Volume;

public final class SawtoothInst extends Instrument {
    private EnvPoint[] pointArray = new EnvPoint[10];
    private int sampleRate;
    private SampleOut sout;

    public SawtoothInst(int sampleRate) {
        this.sampleRate = sampleRate;
        EnvPoint[] tempArray = new EnvPoint[]{new EnvPoint(0.0F, 0.0F), new EnvPoint(0.02F, 1.0F), new EnvPoint(0.15F, 0.6F), new EnvPoint(0.9F, 0.3F), new EnvPoint(1.0F, 0.0F)};
        this.pointArray = tempArray;
    }

    public void createChain() {
        Oscillator wt = new Oscillator(this, 4, this.sampleRate, 2);
        Envelope env = new Envelope(wt, this.pointArray);
        Volume vol = new Volume(env);
        StereoPan span = new StereoPan(vol);
        if (this.output == 0) {
            this.sout = new SampleOut(span);
        }

    }
}
