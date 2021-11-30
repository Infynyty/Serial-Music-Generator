package de.infynyty.serialmusic;

import jm.JMC;
import jm.constants.ProgramChanges;
import jm.constants.Volumes;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import lombok.Getter;

import java.util.Collections;
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
     * Schritt 1: Grundton = 1. Ton der 1. Grundreihe Schritt 2: Umkehrung berechnen: Differenz zwischen größerem und
     * kleineren Ton bilden Schritt 3: Wert drauf addieren
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
        return inversionMatrix[0];
    }

    private int[] getOrderedInverseBaseRows() {
        return matrix[0];
    }

    private int[] getOrderedLengthRows(final int[][] matrix) {
        final int[] reverseRow = new int[MATRIX_SIZE];
        System.arraycopy(matrix[0], 0, reverseRow, 0, MATRIX_SIZE);
        Collections.reverse(List.of(reverseRow));
        return reverseRow;
    }

    private int[] getAllLengths() {
        final int[] lengthRows = getOrderedLengthRows(matrix);
        final int[] allLengths = new int[MATRIX_SIZE * MATRIX_SIZE];
        for (int i = 0; i < lengthRows.length; i++) {
            final int[] reverseRow = new int[MATRIX_SIZE];
            System.arraycopy(matrix[0], 0, reverseRow, 0, MATRIX_SIZE);
            Collections.reverse(List.of(reverseRow));
            System.arraycopy(reverseRow, 0, allLengths, MATRIX_SIZE * i, reverseRow.length);
        }
        return allLengths;
    }

    /**
     * Returns all notes in the correct order according to the rules of serial music.
     *
     * @return All notes in the correct order.
     */
    private Note[] getAllOrderedNotes() {
        final int[] orderedBaseRows = getOrderedBaseRows();
        final int[] allNotes = new int[MATRIX_SIZE * MATRIX_SIZE];
        for (int i = 0; i < orderedBaseRows.length; i++) {
            System.arraycopy(getBaseRowByRowNumber(i), 0, allNotes, MATRIX_SIZE * i, getBaseRowByRowNumber(i).length);
        }
        return getNotesByIndices(allNotes);
    }

    /**
     * Returns a list of all dynamics according to the rules of serial music.
     *
     * @param matrix The matrix that should be read for the values.
     *
     * @return A list of all indices for the dynamics.
     */
    private int[] getAllDynamics(final int[][] matrix) {
        final int[] allDynamics = new int[MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            allDynamics[i] = matrix[MATRIX_SIZE - i - 1][i];
        }
        return allDynamics;
    }

    /**
     * Returns a complete phrase for piano one. This phrase contains all notes with their correct length and dynamic.
     *
     * @return A phrase for piano one with all notes.
     */
    public Score getCompletePianoOneScore() {
        final Part part = new Part(ProgramChanges.PIANO, 0);
        final Score score = new Score();
        final Note[] allNotes = getAllOrderedNotes();
        final int[] allLengths = getAllLengths();
        final int[] allDynamics = getAllDynamics(matrix);
        for (int phraseNumber = 0; phraseNumber < MATRIX_SIZE; phraseNumber++) {
            final Phrase phrase = new Phrase();
            for (int note = 0; note < MATRIX_SIZE; note++) {
                allNotes[phraseNumber * MATRIX_SIZE + note].setLength(NoteLengths.values()[allLengths[phraseNumber * MATRIX_SIZE + note]].getNoteLength());

                phrase.addNote(allNotes[phraseNumber * MATRIX_SIZE + note]);
            }
            phrase.setDynamic(Dynamics.values()[allDynamics[phraseNumber]].getDynamic());
            part.setTitle("Phrase " + phraseNumber);
            part.appendPhrase(phrase);
        }
        score.addPart(part);
        return score;
    }
}
