package com.demo.models

interface GameResult {
    fun next(): String?
    fun count(): Int
    fun results(): List<Game>
}

interface Game {
    fun name(): String
    fun backgroundImage(): String?
    fun id(): Int
    fun ratings(): List<Rating>
    fun shotScreenShoot(): List<ShortScreenShoot>
    fun added(): Int
    fun released(): String
    fun rating(): Double
}

interface Rating {
    fun id(): Int
    fun title(): String
    fun percent(): Double
}

interface ShortScreenShoot {
    fun id(): Int
    fun image(): String
}
