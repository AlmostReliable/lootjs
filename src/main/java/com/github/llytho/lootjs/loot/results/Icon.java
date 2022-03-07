package com.github.llytho.lootjs.loot.results;

public enum Icon {
    SUCCEED("\u2714\uFE0F"),
    FAILED("\u274C"),
    ACTION("\uD83D\uDCDD"),
    WRENCH("\uD83D\uDD27"),
    BOLT("\uD83D\uDD29"),
    DICE("\uD83C\uDFB2");

    private final String icon;

    Icon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return icon;
    }
}
