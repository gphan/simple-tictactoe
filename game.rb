require './state'

class Game
  def initialize state = nil, computer_player = :o
    if state.nil?
      state = State.new nil, :x, computer_player
    end
    @state = state
    @computer_player = computer_player
  end

  def play
    loop do
      if @state.winner?
        winner = @state.winner?
        @state.board.print
        case winner
          when @computer_player
            puts "The AI beat you!"
          when :draw
            puts "Cats Game (Draw)!"
          else
            puts "You are the winner!"
        end
        return winner
      end

      move = nil
      if @state.human_turn?
        puts "Your turn:"
        move = get_human_move
      else
        puts "AI's turn..."
        move = find_best_move
      end

      @state = @state.move move
    end
  end

  private

  def get_human_move
    free_moves = @state.board.free_moves
    puts "Current board"
    @state.board.print

    loop do
      puts "Valid Moves: #{free_moves.to_s}"

      move = gets.chomp
      m = move.to_i

      return m if free_moves.include? m

      puts "Invalid move!"
    end
  end

  def find_best_move
    moves = @state.branches
    scores = moves.map { |m| m.alpha_beta }
    merge = moves.zip(scores).shuffle

    best_move = nil
    if @computer_player == :x
      best_move = merge.max { |a, b| a[1] <=> b[1] }
    else
      best_move = merge.min { |a, b| a[1] <=> b[1] }
    end

    # Debug, print move and score
    # debug = merge.clone.map do |a|
    #   b = a.clone
    #   b[0] = a[0].board.last_move
    #   b
    # end
    # puts debug.to_s

    best_move[0].board.last_move
  end
end

if __FILE__ == $0
  scores = Hash.new(0)
  computer_player = :o
  loop do
    game = Game.new nil, computer_player
    winner = game.play

    # Add scores
    if winner == computer_player
      scores[:computer] += 1
    elsif winner == :draw
      scores[:draw] += 1
    else
      scores[:human] += 1
    end

    puts "You - #{scores[:human]} , Computer - #{scores[:computer]}, Draw - #{scores[:draw]}"
    puts "Play another? y/n"
    choice = gets.chomp
    if choice != "y"
      puts "Good game!"
      break
    end

    computer_player = computer_player == :o ? :x : :o
  end
end
