package de.infynytyyy.serialmusic;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Phrase;
import lombok.Getter;

import javax.swing.JFormattedTextField;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SerialMatrix implements JMC {
    final static private int MATRIX_SIZE = 12;
    final static private int BASE_OCTAVE = C4;

    final private Note[] baseNotes;
    @Getter
    final private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    @Getter
    final private int[][] inversionMatrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    public SerialMatrix(final Note[] baseNotes) {
        this.baseNotes = baseNotes;
        randomlyFillMatrix();
        fillInversionMatrix();
    }

    /**
     * Randomly fills the matrix. The first row and column get numbered according to their position relative to the
     * starting point. A row cannot contain the same number twice.
     */
    private void randomlyFillMatrix() {
        final Random random = new Random();
        for (int row = 0; row < MATRIX_SIZE; row++) {
            final List<Integer> availableRowNumbers = IntStream.of(
                IntStream.rangeClosed(0, MATRIX_SIZE - 1).toArray()).boxed().collect(Collectors.toList()
            );
            for (int column = 0; column < MATRIX_SIZE; column++) {
                if (row == 0) {
                    matrix[row][column] = column;
                } else if (column == 0) {
                    matrix[row][column] = row;
                } else {
                    int number;
                    do {
                        number = availableRowNumbers.get(random.nextInt(availableRowNumbers.size() - 1));
                    } while (!(availableRowNumbers.contains(number)));
                    matrix[row][column] = number;
                    availableRowNumbers.remove((Integer) number);
                }
            }
        }
    }

    /**
     * Schritt 1: Grundton = 1. Ton der 1. Grundreihe
     * Schritt 2: Umkehrung berechnen: Differenz zwischen größerem und kleineren Ton bilden
     * Schritt 3: Wert drauf addieren
     *
     */
    private void fillInversionMatrix() {
        for (int row = 0; row < MATRIX_SIZE; row++) {
            for (int column = 0; column < MATRIX_SIZE; column++) {
                final int noteInversionPitch =
                    getNoteInversionPitch(getNoteByIndex(0).getPitch(), getNoteByIndex(matrix[row][column]).getPitch());
                final int sameOctaveNoteInversionPitch = BASE_OCTAVE + (noteInversionPitch % 12);
                inversionMatrix[row][column] = getIndexByNotePitch(sameOctaveNoteInversionPitch);
            }
        }
    }

    private int getNoteInversionPitch(final int baseNotePitch, final int relativeNotePitch) {
        final int difference = baseNotePitch - relativeNotePitch;
        return baseNotePitch + difference;
    }

    /**
     * Returns a note using its index defined within the first row of the matrix.
     *
     * @param index The index of the note which is used to search {@link SerialMatrix#baseNotes the base notes}.
     *
     * @return The base note.
     */
    private Note getNoteByIndex(final int index) {
        return baseNotes[index];
    }

    /**
     * Returns a list of base notes for a given list of indices.
     *
     * @param indices The list of indices.
     *
     * @return A list of base notes.
     */
    public Note[] getNotesByIndices(final int[] indices) {
        final Note[] notes = new Note[indices.length];
        for (int i = 0; i < indices.length; i++) {
            notes[i] = getNoteByIndex(indices[i]);
        }
        return notes;
    }

    // FIXME: 28.11.2021 put notes all on one octave
    private int getIndexByNotePitch(final int notePitch) {
        for (int i = 0; i < baseNotes.length; i++) {
            if (notePitch == baseNotes[i].getPitch()) {
                return i;
            }
        }
        throw new IllegalArgumentException("Note outside range.");
    }

    /**
     * Returns a new array containing the indices of a given row.
     *
     * @param row The row.
     *
     * @return A new array with all indices of the row.
     */
    public int[] getBaseRowByRowNumber(final int row) {
        final int[] baseRowIndices = new int[MATRIX_SIZE];
        System.arraycopy(matrix[row], 0, baseRowIndices, 0, MATRIX_SIZE);
        return baseRowIndices;
    }

    public int[] getInverseBaseRowByRowNumber(final int row) {
        final int[] baseRowIndices = new int[MATRIX_SIZE];
        System.arraycopy(inversionMatrix[row], 0, baseRowIndices, 0, MATRIX_SIZE);
        return baseRowIndices;
    }

    private int[] getOrderedBaseRows() {
        return null; // FIXME: 26.11.2021 Add
    }

    public Phrase getCompletePianoOnePhrase() {
        final Phrase phrase = new Phrase();
        for(int row = 0; row < MATRIX_SIZE; row++){
            final Note[] notes = getNotesByIndices(getBaseRowByRowNumber(row));
            for (int i = 0; i < notes.length; i++) {
                final Note note = new Note();
            }
        }
        return null;
    }
}
