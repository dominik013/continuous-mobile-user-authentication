package at.reichinger.cuaverification.hmm.math

fun Double.almostOne(): Boolean {
    return this > 0.99 && this < 1.01
}