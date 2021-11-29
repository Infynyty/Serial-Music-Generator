package de.infynyty.serialmusic;

import jm.JMC;
import jm.constants.Durations;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NoteLengths implements JMC {
    THIRTY_SECOND_NOTE(THIRTYSECOND_NOTE),
    SIXTEENTH_NOTE(Durations.SIXTEENTH_NOTE),
    DOTTED_SIXTEENTH_NOTE(Durations.DOTTED_SIXTEENTH_NOTE),
    EIGHTH_NOTE(Durations.EIGHTH_NOTE),
    EIGHT_PLUS_THIRTYSECOND_NOTE(Durations.EIGHTH_NOTE + Durations.THIRTYSECOND_NOTE),
    EIGHT_DOTTED(Durations.DOTTED_EIGHTH_NOTE),
    EIGHT_DOUBLE_DOTTED(Durations.DOUBLE_DOTTED_EIGHTH_NOTE),
    CROTCHET(Durations.CROTCHET),
    CROTCHET_PLUS_THIRTYSECOND(Durations.CROTCHET + Durations.THIRTYSECOND_NOTE),
    CROTCHET_PLUS_SIXTEENTH(Durations.CROTCHET + Durations.SIXTEENTH_NOTE),
    CROTCHET_PLUS_SIXTEENTH_DOTTED(Durations.CROTCHET + Durations.DOTTED_SIXTEENTH_NOTE),
    DOTTED_CROTCHET(Durations.DOTTED_CROTCHET)
    ;
    @Getter
    final private double noteLength;
}
