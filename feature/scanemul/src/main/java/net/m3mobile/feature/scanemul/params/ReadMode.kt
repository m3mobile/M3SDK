package net.m3mobile.feature.scanemul.params

public enum class ReadMode(internal val value: Int) {
    ASYNC(0),
    SYNC(1),
    CONTINUE(2),
    MULTIPLE(3),
    PRESENTATION(4),
    AIMING_AND_RELEASE(5)
}