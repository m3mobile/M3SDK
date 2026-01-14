package net.m3mobile.feature.scanemul.params

public enum class OutputMode(internal val value: Int) {

    COPY_AND_PASTE(0),
    KEY_EMULATION(1),
    COPY_TO_CLIPBOARD(2),
    COMMIT_TEXT(3)
}