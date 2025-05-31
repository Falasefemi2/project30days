package com.femiproject.numbergame;

public enum DifficultyLevel {
    EASY(10),
    MEDIUM(5),
    HARD(3);

    private final int chances;

    DifficultyLevel(int chances) {
        this.chances = chances;
    }

    public int getChances() {
        return chances;
    }
}
