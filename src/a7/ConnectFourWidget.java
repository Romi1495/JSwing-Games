package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ConnectFourWidget extends JPanel implements ActionListener, SpotListener {

	private enum Player {
		RED, BLACK
	};

	private JSpotBoard _board;
	private JLabel _message;
	private boolean _game_won;
	private Player _next_to_play;
	private Spot[] _winning_match = new Spot[4];
	private String player_name = null;
	private String next_player_name = null;
	private Color player_color = null;
	private Spot available_spot = null;
	private boolean column_space;

	public ConnectFourWidget() {
		/* Create SpotBoard and message label. */

		_board = new JSpotBoard(7, 6);
		_message = new JLabel();

		/* Set layout and place SpotBoard at center. */

		setLayout(new BorderLayout());
		add(_board, BorderLayout.CENTER);
		for (Spot spot : _board) {
			if (spot.getSpotX() % 2 == 0) {
				spot.setBackground(new Color(0.5f, 0.5f, 0.5f));
			} else {
				spot.setBackground(new Color(0.8f, 0.8f, 0.8f));
			}
		}

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
		for (Spot s : _board) {
			s.clearSpot();
			s.setSpotColor(s.getBackground());
			s.unhighlightSpot();
		}

		_game_won = false;
		_next_to_play = Player.RED;

		_message.setText("Welcome to Connect Four. Red to play");

	}

	@Override
	public void spotClicked(Spot spot) {

		if (_game_won) {
			return;
		}
		column_space = false;
		// Find empty spot if it exists
		for (int i = 0; i < 6; i++) {

			if (_board.getSpotAt(spot.getSpotX(), i).isEmpty()) {
				column_space = true;
				available_spot = _board.getSpotAt(spot.getSpotX(), i);
			}
		}

		// Place spot
		if (column_space) {

			if (_next_to_play == Player.RED) {
				player_color = Color.RED;
				player_name = "Red";
				next_player_name = "Black";
				_next_to_play = Player.BLACK;
			} else {
				player_color = Color.BLACK;
				player_name = "Black";
				next_player_name = "Red";
				_next_to_play = Player.RED;

			}
			available_spot.setSpotColor(player_color);
			available_spot.toggleSpot();
		}

		/*
		 * Vertical check
		 * 
		 * 
		 */
		for (int x = 0; x < 7; x++) {
			for (int y = 0; y < 3; y++) {

				if ((_board.getSpotAt(x, y).getSpotColor() == (_board.getSpotAt(x, y + 1).getSpotColor()))
						&& (_board.getSpotAt(x, y).getSpotColor() == (_board.getSpotAt(x, y + 2).getSpotColor()))
						&& (_board.getSpotAt(x, y).getSpotColor() == (_board.getSpotAt(x, y + 3).getSpotColor()))) {
					_game_won = true;
					for (int i = 0; i < 4; i++) {
						_winning_match[i] = _board.getSpotAt(x, y + i);
					}
					for (int i = 0; i < 6; i++) {

						_board.getSpotAt(spot.getSpotX(), i).unhighlightSpot();

					}
				}
			}

		}
		/*
		 * Horizontal Check
		 * 
		 * 
		 */
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 6; y++) {
				if ((_board.getSpotAt(x, y).getSpotColor() == (_board.getSpotAt(x + 1, y).getSpotColor()))
						&& (_board.getSpotAt(x, y).getSpotColor() == (_board.getSpotAt(x + 2, y).getSpotColor()))
						&& (_board.getSpotAt(x, y).getSpotColor() == (_board.getSpotAt(x + 3, y).getSpotColor()))) {
					_game_won = true;
					for (int i = 0; i < 4; i++) {
						_winning_match[i] = _board.getSpotAt(x + i, y);
					}
					for (int i = 0; i < 6; i++) {

						_board.getSpotAt(spot.getSpotX(), i).unhighlightSpot();

					}
				}
			}
		}
		/*
		 * Diagonal Check
		 * 
		 * 
		 */
		for (int i = 0; i < 4; i++) {
			for (int y = 0; y < 3; y++) {
				if ((_board.getSpotAt(i, y).getSpotColor() == (_board.getSpotAt(i + 1, y + 1).getSpotColor()))
						&& (_board.getSpotAt(i, y).getSpotColor() == (_board.getSpotAt(i + 2, y + 2).getSpotColor()))
						&& (_board.getSpotAt(i, y).getSpotColor() == (_board.getSpotAt(i + 3, y + 3).getSpotColor()))) {
					_game_won = true;
					for (int j = 0; j < 4; j++) {
						_winning_match[j] = _board.getSpotAt(i + j, y + j);
					}
					for (int j = 0; j < 6; j++) {

						_board.getSpotAt(spot.getSpotX(), j).unhighlightSpot();

					}
				}
			}

		}
		for (int i = 6; i > 2; i--) {
			for (int y = 0; y < 3; y++) {
				if ((_board.getSpotAt(i, y).getSpotColor() == (_board.getSpotAt(i - 1, y + 1).getSpotColor()))
						&& (_board.getSpotAt(i, y).getSpotColor() == (_board.getSpotAt(i - 2, y + 2).getSpotColor()))
						&& (_board.getSpotAt(i, y).getSpotColor() == (_board.getSpotAt(i - 3, y + 3).getSpotColor()))) {
					_game_won = true;
					for (int j = 0; j < 4; j++) {
						_winning_match[j] = _board.getSpotAt(i - j, y + j);
					}
					for (int j = 0; j < 6; j++) {

						_board.getSpotAt(spot.getSpotX(), j).unhighlightSpot();

					}
				}
			}

		}
		boolean _game_draw = true;
		for (Spot s : _board) {
			if (s.isEmpty()) {
				_game_draw = false;
			}
		}
		if (_game_won) {
			for (Spot winner : _winning_match) {
				winner.highlightSpot();
			}
			_message.setText("GAME OVER: " + player_name + " Won!");
		} else if (_game_draw){
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
		for (int i = 0; i < 6; i++) {
			if (_board.getSpotAt(spot.getSpotX(), i).isEmpty()) {
				_board.getSpotAt(spot.getSpotX(), i).highlightSpot();
			}
		}
	}

	@Override
	public void spotExited(Spot spot) {
		if (_game_won) {
			return;
		}
		for (int i = 0; i < 6; i++) {

			_board.getSpotAt(spot.getSpotX(), i).unhighlightSpot();

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		resetGame();
	}

}
