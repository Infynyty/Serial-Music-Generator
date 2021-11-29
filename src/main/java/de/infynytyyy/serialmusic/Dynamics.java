package de.infynytyyy.serialmusic;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Dynamics {
    FOUR_PIANO(jm.constants.Dynamics.PPP - 5),
    THREE_PIANO(jm.constants.Dynamics.PPP),
    TWO_PIANO(jm.constants.Dynamics.PP),
    PIANO(jm.constants.Dynamics.PP),
    QUASI_PIANO(jm.constants.Dynamics.MEZZO_PIANO - 5),
    MEZZO_PIANO(jm.constants.Dynamics.MEZZO_PIANO),
    MEZZO_FORTE(jm.constants.Dynamics.MEZZO_FORTE),
    QUASI_FORTE(jm.constants.Dynamics.FORTE - 5),
    FORTE(jm.constants.Dynamics.FORTE - 5),
    TWO_FORTE(jm.constants.Dynamics.FORTISSIMO),
    THREE_FORTE(jm.constants.Dynamics.FFF),
    FOUR_FORTE(jm.constants.Dynamics.FORTISSIMO + 5)
    ;
    final int dynamic;
}
