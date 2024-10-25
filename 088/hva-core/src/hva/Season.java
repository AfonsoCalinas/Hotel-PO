package hva;

import java.io.Serializable;

public enum Season implements Serializable {
    PRIMAVERA(0) {
        @Override
        public Season nextSeason() {
            return VERAO;
        }
    },
    VERAO(1) {
        @Override
        public Season nextSeason() {
            return OUTONO;
        }
    },
    OUTONO(2) {
        @Override
        public Season nextSeason() {
            return INVERNO;
        }
    },
    INVERNO(3) {
        @Override
        public Season nextSeason() {
            return PRIMAVERA;
        }
    };

    private final int value;

    Season(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public abstract Season nextSeason();
}
