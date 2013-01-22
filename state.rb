require './board'

# Represents a game state
class State
  attr_reader :board

  def initialize board = nil, current_player = :x, computer_player = :o
    if board.nil?
      board = Board.new
    end

    @board = board
    @current_player = current_player
    @computer_player = computer_player
  end

  # Checks if the game state has a winner
  def winner?
    @board.winner?
  end

  # Determines if its a human player's turn
  def human_turn?
    @current_player != @computer_player
  end

  # Possible game states from possible free moves
  def branches
    @board.free_moves.map do |pos|
      move pos
    end.compact
  end

  # Returns the game state after making a move
  def move pos
    new_board = nil
    next_player = @current_player == :x ? :o : :x
    new_board = @board.make_move @current_player, pos
    State.new new_board, next_player, @computer_player
  end

  # Alpha-beta pruning of the min-max game tree
  def alpha_beta player = @current_player, alpha = -Float::INFINITY, beta = Float::INFINITY
    case @board.winner?
      when :x
        return 1
      when :o
        return -1
      when :draw
        return 0
    end

    if player == :x
      branches.each do |branch|
        score = branch.alpha_beta :o, alpha, beta
        alpha = score if score > alpha
        return alpha if alpha >= beta
      end
      return alpha
    else
      branches.each do |branch|
        score = branch.alpha_beta :x, alpha, beta
        beta = score if score < beta
        return beta if alpha >= beta
      end
      return beta
    end
  end
end
