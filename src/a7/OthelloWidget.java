package a7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class OthelloWidget extends JPanel implements ActionListener, SpotListener {

	private enum Player {
		WHITE, BLACK
	};

	private JSpotBoard _board;
	private JLabel _message;
	private boolean _game_over;
	private Player _next_to_play;
	private String _player_name = null;
	private String _next_player_name = null;
	private Color _player_color = null;
	private Color _next_color;

	public OthelloWidget() {
		/* Create SpotBoard and message label. */

		_board = new JSpotBoard(8, 8);
		_message = new JLabel();

		/* Set layout and place SpotBoard at center. */

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

	@Override
	public void spotClicked(Spot spot) {
		ArrayList<Spot> flipped = new ArrayList<Spot>();
		flipped = find_flipped(spot);
		boolean _turn_skipped = false;
		if (_game_over || !spot.isEmpty() || !(flipped.size() > 0)) {
			return;
		}

		// Place spot

		if (flipped.size() > 0) {

			if (_next_to_play == Player.WHITE) {
				_player_color = Color.WHITE;
				_player_name = "White";
				_next_player_name = "Black";
				_next_to_play = Player.BLACK;
				_next_color = Color.black;
			} else {
				_player_color = Color.BLACK;
				_player_name = "Black";
				_next_player_name = "White";
				_next_to_play = Player.WHITE;
				_next_color = Color.WHITE;

			}
			for (Spot captured : flipped) {
				captured.setSpotColor(_player_color);
			}
			spot.setSpotColor(_player_color);
			spot.toggleSpot();
		}

		_game_over = true;
		for (Spot s : _board) {
			if (s.isEmpty()) {
				if (find_flipped(s).size() > 0) {
					_game_over = false;
				}
			}
		}
		if (_game_over) {
			if (_next_to_play == Player.WHITE) {
				_player_color = Color.WHITE;
				_player_name = "White";
				_next_player_name = "Black";
				_next_to_play = Player.BLACK;
				_next_color = Color.black;
			} else {
				_player_color = Color.BLACK;
				_player_name = "Black";
				_next_player_name = "White";
				_next_to_play = Player.WHITE;
				_next_color = Color.WHITE;

			}
			_turn_skipped = true;
			_message.setText(_player_name + "'s turn skipped. " + _next_player_name + " to play next");
		}
		for (Spot s : _board) {
			if (s.isEmpty()) {
				if (find_flipped(s).size() > 0) {
					_game_over = false;
				}
			}
		}

		if (_game_over) {
			int _count_black = 0;
			int _count_white = 0;
			for (Spot s : _board) {
				if (s.getSpotColor() == Color.BLACK) {
					_count_black++;
				}
				if (s.getSpotColor() == Color.WHITE) {
					_count_white++;
				}
			}
			if (_count_white > _count_black) {
				_message.setText("GAME OVER: White Won! Score: White: " + _count_white + " Black: " + _count_black);
			} else if (_count_black > _count_white) {
				_message.setText("GAME OVER: Black Won! Score: White: " + _count_white + " Black: " + _count_black);
			} else {
				_message.setText("GAME OVER: draw! Score: White: " + _count_white + " Black: " + _count_black);
			}

		} else if (!_turn_skipped) {
			_message.setText(_next_player_name + " to play next");
		}

	}

	@Override
	public void spotEntered(Spot spot) {
		if (!spot.isEmpty()) {
			return;
		}
		if (find_flipped(spot).size() > 0) {
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

	public void resetGame() {
		for (Spot s : _board) {
			s.clearSpot();
			s.setSpotColor(s.getBackground());
		}
		_board.getSpotAt(3, 3).setSpotColor(Color.WHITE);
		_board.getSpotAt(3, 3).toggleSpot();
		_board.getSpotAt(4, 4).setSpotColor(Color.WHITE);
		_board.getSpotAt(4, 4).toggleSpot();
		_board.getSpotAt(3, 4).setSpotColor(Color.BLACK);
		_board.getSpotAt(3, 4).toggleSpot();
		_board.getSpotAt(4, 3).setSpotColor(Color.BLACK);
		_board.getSpotAt(4, 3).toggleSpot();

		_game_over = false;
		_next_to_play = Player.BLACK;
		_next_color = Color.BLACK;
		_message.setText("Welcome to Connect Four. Black to play");
	}

	public ArrayList<Spot> find_flipped(Spot spot) {
		ArrayList<Spot> will_flip = new ArrayList<Spot>();

		// Search right
		for (int i = spot.getSpotX() + 2; i < 8; i++) {
			boolean empty = false;
			if (_board.getSpotAt(i, spot.getSpotY()).getSpotColor() == _next_color) {
				for (int j = spot.getSpotX() + 1; j < i; j++) {
					if ((_board.getSpotAt(j, spot.getSpotY()).isEmpty())
							|| (_board.getSpotAt(j, spot.getSpotY()).getSpotColor() == _next_color)) {
						empty = true;
					}
				}
				if (!empty) {
					for (int j = spot.getSpotX() + 1; j < i; j++) {
						if (_board.getSpotAt(j, spot.getSpotY()).getSpotColor() != _next_color) {
							will_flip.add(_board.getSpotAt(j, spot.getSpotY()));
						}
					}

				}
				break;
			}

		}
		// Search left
		for (int i = spot.getSpotX() - 2; i >= 0; i--) {
			boolean empty = false;
			if (_board.getSpotAt(i, spot.getSpotY()).getSpotColor() == _next_color) {
				for (int j = spot.getSpotX() - 1; j > i; j--) {
					if ((_board.getSpotAt(j, spot.getSpotY()).isEmpty())
							|| (_board.getSpotAt(j, spot.getSpotY()).getSpotColor() == _next_color)) {
						empty = true;
					}

				}
				if (!empty) {
					for (int j = spot.getSpotX() - 1; j > i; j--) {
						if (_board.getSpotAt(j, spot.getSpotY()).getSpotColor() != _next_color) {
							will_flip.add(_board.getSpotAt(j, spot.getSpotY()));
						}
					}
				}
				break;
			}

		}
		// Search Up
		for (int i = spot.getSpotY() - 2; i >= 0; i--) {
			boolean empty = false;
			if (_board.getSpotAt(spot.getSpotX(), i).getSpotColor() == _next_color) {
				for (int j = spot.getSpotY() - 1; j > i; j--) {
					if ((_board.getSpotAt(spot.getSpotX(), j).isEmpty())
							|| (_board.getSpotAt(spot.getSpotX(), j).getSpotColor() == _next_color)) {
						empty = true;

					}
				}
				if (!empty) {
					for (int j = spot.getSpotY() - 1; j > i; j--) {
						if (_board.getSpotAt(spot.getSpotX(), j).getSpotColor() != _next_color) {
							will_flip.add(_board.getSpotAt(spot.getSpotX(), j));
						}

					}
				}

				break;
			}

		}
		// Search Down
		for (int i = spot.getSpotY() + 2; i < 8; i++) {
			boolean empty = false;
			if (_board.getSpotAt(spot.getSpotX(), i).getSpotColor() == _next_color) {
				for (int j = spot.getSpotY() + 1; j < i; j++) {
					if ((_board.getSpotAt(spot.getSpotX(), j).isEmpty())
							|| (_board.getSpotAt(spot.getSpotX(), j).getSpotColor() == _next_color)) {
						empty = true;
					}

				}
				if (!empty) {
					for (int j = spot.getSpotY() + 1; j < i; j++) {
						if (_board.getSpotAt(spot.getSpotX(), j).getSpotColor() != _next_color) {
							will_flip.add(_board.getSpotAt(spot.getSpotX(), j));
						}
					}
				}

				break;
			}

		}
		// Search down-right diagonal
		for (int i = 2; i < 8; i++) {
			boolean empty = false;
			if ((spot.getSpotX() + i > 7) || (spot.getSpotY() + i > 7)) {
				break;
			}
			if (_board.getSpotAt(spot.getSpotX() + i, spot.getSpotY() + i).getSpotColor() == _next_color) {
				for (int j = 1; j < i; j++) {
					if ((_board.getSpotAt(spot.getSpotX() + j, spot.getSpotY() + j).isEmpty()) || (_board
							.getSpotAt(spot.getSpotX() + j, spot.getSpotY() + j).getSpotColor() == _next_color)) {
						empty = true;
					}
				}
				if (!empty) {
					for (int j = 1; j < i; j++) {
						if (_board.getSpotAt(spot.getSpotX() + j, spot.getSpotY() + j).getSpotColor() != _next_color) {
							will_flip.add(_board.getSpotAt(spot.getSpotX() + j, spot.getSpotY() + j));
						}
					}

				}
			}
		}
		// Search down-left diagonal
		for (int i = 2; i < 8; i++) {
			boolean empty = false;
			if ((spot.getSpotX() - i < 0) || (spot.getSpotY() + i > 7)) {
				break;
			}
			if (_board.getSpotAt(spot.getSpotX() - i, spot.getSpotY() + i).getSpotColor() == _next_color) {
				for (int j = 1; j < i; j++) {
					if ((_board.getSpotAt(spot.getSpotX() - j, spot.getSpotY() + j).isEmpty()) || (_board
							.getSpotAt(spot.getSpotX() - j, spot.getSpotY() + j).getSpotColor() == _next_color)) {
						empty = true;
					}
				}
				if (!empty) {
					for (int j = 1; j < i; j++) {
						if (_board.getSpotAt(spot.getSpotX() - j, spot.getSpotY() + j).getSpotColor() != _next_color) {
							will_flip.add(_board.getSpotAt(spot.getSpotX() - j, spot.getSpotY() + j));
						}
					}

				}
			}
		}
		// Search up-right diagonal
		for (int i = 2; i < 8; i++) {
			boolean empty = false;
			if ((spot.getSpotX() + i > 7) || (spot.getSpotY() - i < 0)) {
				break;
			}
			if (_board.getSpotAt(spot.getSpotX() + i, spot.getSpotY() - i).getSpotColor() == _next_color) {
				for (int j = 1; j < i; j++) {
					if ((_board.getSpotAt(spot.getSpotX() + j, spot.getSpotY() - j).isEmpty()) || (_board
							.getSpotAt(spot.getSpotX() + j, spot.getSpotY() - j).getSpotColor() == _next_color)) {
						empty = true;
					}
				}
				if (!empty) {
					for (int j = 1; j < i; j++) {
						if (_board.getSpotAt(spot.getSpotX() + j, spot.getSpotY() - j).getSpotColor() != _next_color) {
							will_flip.add(_board.getSpotAt(spot.getSpotX() + j, spot.getSpotY() - j));
						}
					}

				}
			}
		}
		// Search up-left diagonal
		for (int i = 2; i < 8; i++) {
			boolean empty = false;
			if ((spot.getSpotX() - i < 0) || (spot.getSpotY() - i < 0)) {
				break;
			}
			if (_board.getSpotAt(spot.getSpotX() - i, spot.getSpotY() - i).getSpotColor() == _next_color) {
				for (int j = 1; j < i; j++) {
					if ((_board.getSpotAt(spot.getSpotX() - j, spot.getSpotY() - j).isEmpty()) || (_board
							.getSpotAt(spot.getSpotX() - j, spot.getSpotY() - j).getSpotColor() == _next_color)) {
						empty = true;
					}
				}
				if (!empty) {
					for (int j = 1; j < i; j++) {
						if (_board.getSpotAt(spot.getSpotX() - j, spot.getSpotY() - j).getSpotColor() != _next_color) {
							will_flip.add(_board.getSpotAt(spot.getSpotX() - j, spot.getSpotY() - j));
						}
					}

				}
			}
		}
		return will_flip;
	}
}
