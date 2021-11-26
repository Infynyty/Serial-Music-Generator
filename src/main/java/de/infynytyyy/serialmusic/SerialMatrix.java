package de.infynytyyy.serialmusic;

import jm.music.data.Note;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SerialMatrix {
    final static private int MATRIX_SIZE = 12;

    final private Note[] baseNotes;
    @Getter
    final private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

    public SerialMatrix(final Note[] baseNotes) {
        this.baseNotes = baseNotes;
        randomlyFillMatrix();

    }

    /**
     * Randomly fills the matrix. The first row and column get numbered according to their position relative to the
     * starting point.
     */
    private void randomlyFillMatrix() {
        final Random random = new Random();
        for (int column = 0; column < MATRIX_SIZE; column++) {
            final List<Integer> availableColumnNumbers = IntStream.of(
                IntStream.rangeClosed(1, 10).toArray()).boxed().collect(Collectors.toList()
            );
            for (int row = 0; row < MATRIX_SIZE; row++) {
                final List<Integer> availableRowNumbers = IntStream.of(
                    IntStream.rangeClosed(1, 10).toArray()).boxed().collect(Collectors.toList()
                );
                if (row == 0) {
                    matrix[row][column] = column;
                    availableRowNumbers.remove((Integer) column);
                } else if (column == 0) {
                    matrix[row][column] = row;
                    availableColumnNumbers.remove((Integer) row);
                } else {
                    int number;
                    do {
                        number = availableRowNumbers.get(random.nextInt(availableRowNumbers.size() - 1));
                    } while (!(availableRowNumbers.contains(number) && !(availableColumnNumbers.contains(number))));
                    matrix[row][column] = number;
                    availableRowNumbers.remove((Integer) number);
                    availableColumnNumbers.remove((Integer) number);
                }
            }
        }
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

    private int[] getOrderedBaseRows() {
        return null; // FIXME: 26.11.2021 Add
    }
}
