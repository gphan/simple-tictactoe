package TicTacToe

import util.Random

case class Game(var state: State = new State, computerPlayer: Board.Player = Board.O) {
  def play(): Board.Player = {
    var winner: Board.Player = state.getWinner

    while (winner == Board.NoPlayer) {
      var move: Int = 0
      if (state.isHumanTurn) {
        move = getHumanMove
      } else {
        move = getComputerMove
      }

      state = state.move(move)
      winner = state.getWinner
    }

    // Display final game board
    println(state.board)

    winner
  }

  def getHumanMove: Int = {
    val availableMoves = state.board.freeMoves
    println("Current board")
    println(state.board)

    var move: Int = -1
    while (!availableMoves.contains(move)) {
      println("Valid moves: " + availableMoves.mkString("[", ",", "]"))
      try {
        move = readInt()
      } catch {
        case _: Throwable => println("That's not a number")
      }
    }

    move
  }

  def getComputerMove: Int = {
    val moves = state.branches
    val scores = moves.map(_.alphaBeta())
    val merged = Random.shuffle(moves.zip(scores))

    computerPlayer match {
      case Board.X => merged.maxBy(_._2)._1.board.lastMove
      case Board.O => merged.minBy(_._2)._1.board.lastMove
    }
  }
}

object Game extends App {
  import scala.util.control.Breaks._

  var computerPlayer: Board.Player = Board.O
  var firstPlayer: Board.Player = Board.X
  var computerWins = 0
  var humanWins = 0
  var draws = 0

  breakable {
    while (true) {
      val board = new Board()
      val state = new State(board, firstPlayer, computerPlayer)
      val game = new Game(state, computerPlayer)
      val winner = game.play()

      winner match {
        case c if c == computerPlayer => println("You lost!"); computerWins += 1
        case Board.Draw => println("Cats game!"); draws += 1
        case _ => println("You won!"); humanWins += 1
      }

      println(s"You - $humanWins | Computer - $computerWins | Draws - $draws")
      println("Play another? y/n")
      val choice = readLine()

      if (choice.trim() != "y") {
        println("Good game!")
        break()
      }

      if (firstPlayer != computerPlayer)
        firstPlayer = computerPlayer.nextPlayer

      computerPlayer = computerPlayer.nextPlayer
    }
  }
}
