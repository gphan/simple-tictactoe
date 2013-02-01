package TicTacToe

object Board {
  trait Player {
    val nextPlayer: Player
    val boardPiece: String
    val minMaxValue: Int
  }

  case object X extends Player {
    val nextPlayer = O
    val boardPiece = "X"
    val minMaxValue = 1
  }

  case object O extends Player {
    val nextPlayer = X
    val boardPiece = "O"
    val minMaxValue = -1
  }

  case object NoPlayer extends Player {
    val nextPlayer = NoPlayer
    val boardPiece = "_"
    val minMaxValue = 0
  }

  // Used for winner method
  case object Draw extends Player {
    val nextPlayer = Draw
    val boardPiece = ""
    val minMaxValue = 0
  }
}

import Board.Player

case class Board(boardData: Array[Board.Player] = Array.fill(9) { Board.NoPlayer },
                 lastPlayer: Board.Player = Board.O, lastMove: Int = -1, moveCount: Int = 0) {

  def makeMove(position: Int): Board = {
    val nextBoard = boardData.clone()
    nextBoard(position) = lastPlayer.nextPlayer
    Board(nextBoard, lastPlayer.nextPlayer, position, moveCount + 1)
  }

  override def toString: String = {
    val buffer = new StringBuilder
    boardData.zipWithIndex.foreach {
      case(player, idx) => {
        buffer ++= player.boardPiece
        if (idx % 3 == 2) buffer ++= "\n"
      }
    }
    buffer.toString()
  }

  def freeMoves: List[Int] = {
    boardData.zipWithIndex.filter({case (n, i) => n == Board.NoPlayer }).map({case (n, i) => i}).toList
  }

  def findWinner: Player = {
    lastMove match {
      case -1 => Board.NoPlayer
      case x => {
        val xloc = x % 3
        val yloc = x / 3

        lazy val matchColumn = boardData.view.zipWithIndex.filter({ case (v, i) => i % 3 == xloc })
          .forall({ case (n, i) => n == lastPlayer })

        lazy val matchRow = boardData.view.zipWithIndex.filter({ case (v, i) => i / 3 == yloc })
          .forall({ case (n, i) => n == lastPlayer })

        lazy val matchDiag = boardData.view.zipWithIndex.filter({ case (v, i) => i / 3 == i % 3})
          .forall({ case (n, i) => n == lastPlayer })

        lazy val matchAntiDiag = boardData.view.zipWithIndex.filter({ case (v, i) => i % 3 == (2 - (i / 3))})
          .forall({ case (n, i) => n == lastPlayer })

        if (matchColumn || matchRow || matchDiag || matchAntiDiag)
          lastPlayer
        else if (moveCount == 9)
          Board.Draw
        else
          Board.NoPlayer
      }
    }
  }
}
