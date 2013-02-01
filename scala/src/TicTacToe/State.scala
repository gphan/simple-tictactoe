package TicTacToe

import TicTacToe.Board._

case class State(board: Board = new Board, currentPlayer: Player = X, computerPlayer: Player = O) {

  def getWinner: Player = {
    board.findWinner
  }

  def isHumanTurn: Boolean = currentPlayer != computerPlayer

  def move(position: Int) = {
    val nextBoard = board.makeMove(position)
    State(nextBoard, currentPlayer.nextPlayer, computerPlayer)
  }

  def branches: List[State] = board.freeMoves.map { m => move(m) }

  def alphaBeta(player:Player = currentPlayer, alpha:Int = Int.MinValue, beta: Int = Int.MaxValue): Int = {
    val winner = board.findWinner

    winner match {
      case Board.NoPlayer => {
        var a = alpha
        var b = beta
        player match {
          case X => {
            branches.foreach({
              s: State =>
                val score = s.alphaBeta(O, a, b)
                if (score > a) a = score
                if (a > b) return a
            })
            a
          }

          case O => {
            branches.foreach({
              s: State =>
                val score = s.alphaBeta(X, a, b)
                if (score < b) b = score
                if (a > b) return b
            })
            b
          }
        }
      }
      case other => other.minMaxValue
    }
  }
}
