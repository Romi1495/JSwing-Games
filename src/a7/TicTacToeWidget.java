package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TicTacToeWidget extends JPanel implements ActionListener, SpotListener {

	// Enum to identify player

	private enum Player {
		BLACK, WHITE
	};

	private JSpotBoard _board;
	private JLabel _message;
	private boolean _game_won;
	private Player _next_to_play;
	private String player_name = null;
	private String next_player_name = null;
	private Color player_color = null;

	public TicTacToeWidget() {

		// Create a 3x3 board with a blank message

		_board = new JSpotBoard(3, 3);
		_message = new JLabel();
		for (Spot spot : _board) {
			spot.setBackground(new Color(0.5f, 0.5f, 0.5f));
		}
		setLayout(new BorderLayout());
		add(_board, BorderLayout.CENTER);

		/* Create subpanel for message area and reset button. */

		JPanel reset_message_panel = new JPanel();
		reset_message_panel.setLayout(new BorderLayout());

		/* Reset button. Add ourselves as the action listener. */

		JButton reset_button = new JButton("Restart");
		reset_button.addActionListener(this);
		reset_message_panel.add(reset_button, BorderLayout.EAST);
		reset_message_panel.add(_message, BorderLayout.CENTER);

		/* Add subpanel in south area of layout. */

		add(reset_message_panel, BorderLayout.SOUTH);

		/*
		 * Add ourselves as a spot listener for all of the spots on the spot board.
		 */
		_board.addSpotListener(this);

		/* Reset game. */
		resetGame();
	}

	private void resetGame() {
		/*
		 * Clear all spots on board. Uses the fact that SpotBoard implements
		 * Iterable<Spot> to do this in a for-each loop.
		 */

		for (Spot s : _board) {
			s.clearSpot();
			s.setSpotColor(s.getBackground());
		}

		/* Reset game won and next to play fields */
		_game_won = false;
		_next_to_play = Player.WHITE;

		/* Display game start message. */

		_message.setText("Welcome to Tic Tac Toe. White to play");
	}

	@Override
	public void spotClicked(Spot spot) {

		if (_game_won) {
			return;
		}

		if (spot.isEmpty()) {

			if (_next_to_play == Player.WHITE) {
				player_color = Color.WHITE;
				player_name = "White";
				next_player_name = "Black";
				_next_to_play = Player.BLACK;
			} else {
				player_color = Color.BLACK;
				player_name = "Black";
				next_player_name = "White";
				_next_to_play = Player.WHITE;

			}
			spot.setSpotColor(player_color);
			spot.toggleSpot();
		}  
		for (int i = 0; i < 3; i++) {
			if ((_board.getSpotAt(i, 0).getSpotColor() == (_board.getSpotAt(i, 1).getSpotColor()))
					&& (_board.getSpotAt(i, 0).getSpotColor() == (_board.getSpotAt(i, 2).getSpotColor()))) {
				_game_won = true;
			}
			if ((_board.getSpotAt(0, i).getSpotColor() == (_board.getSpotAt(1, i).getSpotColor()))
					&& (_board.getSpotAt(0, i).getSpotColor() == (_board.getSpotAt(2, i).getSpotColor()))) {
				_game_won = true;
			}
		}
		if (((_board.getSpotAt(0, 0).getSpotColor() == _board.getSpotAt(1, 1).getSpotColor())
				&& (_board.getSpotAt(0, 0).getSpotColor() == _board.getSpotAt(2, 2).getSpotColor()))
				|| ((_board.getSpotAt(2, 0).getSpotColor() == _board.getSpotAt(1, 1).getSpotColor())
						&& (_board.getSpotAt(2, 0).getSpotColor() == _board.getSpotAt(0, 2).getSpotColor()))) {
			_game_won = true;
		}

		boolean _game_draw = true;
		for (Spot s : _board) {
			if (s.isEmpty()) {
				_game_draw = false;
			}
		}
		if (_game_won) {
			_message.setText("GAME OVER: " + player_name + " Won!");
		} else if (_game_draw) {
			_message.setText("GAME OVER: Draw");
		} else {
			_message.setText(next_player_name + " to play next");
		}

	}

	@Override
	public void spotEntered(Spot spot) {
		if (_game_won) {
			return;
		}
		if (spot.isEmpty()) {
			spot.highlightSpot();
		}

	}

	@Override
	public void spotExited(Spot spot) {
		spot.unhighlightSpot();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}
}
