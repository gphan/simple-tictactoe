# Represents an instance of the game board
class Board
  attr_reader :last_move

  def initialize(board = Array.new(9), move_count = 0, player = nil, last_move = nil)
    @board = board
    @move_count = move_count
    @player = player
    @last_move = last_move
  end

  # Makes a move and returns a new board
  def make_move player, move
    clone = @board.clone
    clone[move] = player
    move_count = @move_count + 1
    Board.new(clone, move_count, player, move)
  end

  # Checks for a winner
  def winner?
    if @player && winner
      return @player
    end

    if @move_count == 9
      return :draw
    end
    false
  end

  def print
    @board.each_with_index do |val, i|
      Kernel::print val ? val.to_s.upcase : "_"
      Kernel::print (i % 3 == 2) ? "\n" : " "
    end
  end

  def free_moves
    @board.map.each_with_index.map { |x, i| x.nil? ? i : nil }.compact
  end

  private
  # Checks for a winner at the most recent x and y
  def winner
    x = @last_move % 3
    y = @last_move / 3
    player = @player

    # Check column
    for i in 0..2
      if @board[x + 3 * i] != player
        break
      end

      if i == 2
        return player
      end
    end

    # Check row
    for i in 0..2
      if @board[i + 3 * y] != player
        break
      end

      if i == 2
        return player
      end
    end

    # Check diag
    for i in 0..2
      if @board[i + 3 * i] != player
        break
      end

      if i == 2
        return player
      end
    end

    # Check anti diag
    for i in 0..2
      if @board[(2 - i) + (3 * i)] != player
        break
      end

      if i == 2
        return player
      end
    end
  end
end

