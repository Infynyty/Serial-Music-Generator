package de.infynyty.serialmusic;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;

import java.util.Arrays;

public class SerialMusic implements JMC {

    public static void main(String[] args) {

        final Note[] notes = new Note[12];
        for(int i=0;i<12;i++){
            Note n = new Note(C4+i, CROTCHET);
            notes[i] = n;
        }
        Score s = new Score("JMDemo1 - Scale");
        Part p = new Part("Flute", FLUTE, 0);

        final SerialMatrix serialMatrix = new SerialMatrix(notes);
        for(int i=0;i<12;i++){
            System.out.println(Arrays.toString(serialMatrix.getBaseRowByRowNumber(i)));
        }

        System.out.println("Trennlinie");


        for(int i=0;i<12;i++){
            System.out.println(Arrays.toString(serialMatrix.getInverseBaseRowByRowNumber(i)));
        }

        p.add(serialMatrix.getCompletePianoOnePhrase());
        s.addPart(p);
        View.notation(s);
        Write.midi(s, "ChromaticScale.mid");
    }
}
