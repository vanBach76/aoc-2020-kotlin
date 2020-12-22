import java.io.File
import java.lang.RuntimeException
import java.util.*

fun main() {
    val readText = File("./input/day22.txt").readText()
    val input = readText.split("\r\n\r\n")
//    part1(input)
    part2(input)
}

private fun part1(input : List<String>) {
    val deck1 = getDeck(input[0])
    val deck2 = getDeck(input[1])
    play(deck1, deck2)
    val winningDeck = if(deck1.isEmpty()) deck2 else deck1
    val score = winningDeck.reversed().reduceIndexed { i, acc, cur -> acc + cur * (i + 1) }
    println("score: $score")
}

private fun part2(input : List<String>) {
    val deck1 = getDeck(input[0])
    val deck2 = getDeck(input[1])

    playRecursive(deck1, deck2)

    val winningDeck = if(deck1.isEmpty()) deck2 else deck1
    val score = winningDeck.reversed()
            .map { it.toLong() }
            .reduceIndexed { i, acc, cur -> acc + cur * (i + 1) }

    println("score: $score")
}

private fun playRecursive(deck1: ArrayDeque<Int>, deck2: ArrayDeque<Int>) {
    val previous = mutableListOf<String>()
    while (!deck1.isEmpty() && !deck2.isEmpty()) {

        val roundSignature = "$deck1|$deck2"

        println("\nPlayer 1's deck: $deck1")
        println("Player 2's deck: $deck2")

        val card1 = deck1.pop()
        val card2 = deck2.pop()

        println("Player 1's card: $card1")
        println("Player 2's card: $card2")

        if (previous.contains(roundSignature)) {
            deck2.clear()
        }
        else if (deck1.size < card1 || deck2.size < card2) {
            if (card1 > card2) {
                deck1.add(card1)
                deck1.add(card2)
            } else {
                deck2.add(card2)
                deck2.add(card1)
            }
        }
        else {
            val clone1 = ArrayDeque(deck1.clone().toMutableList().subList(0, card1))
            val clone2 = ArrayDeque(deck2.clone().toMutableList().subList(0, card2))
            playRecursive(clone1, clone2)
            when {
                clone2.isEmpty() -> {
                    deck1.add(card1)
                    deck1.add(card2)
                }
                clone1.isEmpty() -> {
                    deck2.add(card2)
                    deck2.add(card1)
                }
                else -> throw RuntimeException("No deck was empty")
            }
        }

        previous.add(roundSignature)
    }
}

private fun play(deck1: ArrayDeque<Int>, deck2: ArrayDeque<Int>) {
    while (!deck1.isEmpty() && !deck2.isEmpty()) {
        val card1 = deck1.pop()
        val card2 = deck2.pop()
        if (card1 > card2) {
            deck1.add(card1)
            deck1.add(card2)
        } else {
            deck2.add(card2)
            deck2.add(card1)
        }
    }
}

private fun getDeck(input: String): ArrayDeque<Int> {
    val split = input.split("\r\n")
    val player1 = split.subList(1, split.size).map { it.toInt() }
    return ArrayDeque(player1)
}