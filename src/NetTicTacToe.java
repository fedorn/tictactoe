import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

public class NetTicTacToe extends JFrame {

	public GameInfo gameInfo;
	public static final int SQUARE_WIDTH = 50;
	public static final int SQUARE_HEIGHT = 50;

	public BoardUI bUI;
	private JTextField messages;

	private PrintWriter out;
	private Socket sock;
	private int playerID;
	private int currPlayer;
	private boolean isDisabled;
	private boolean isEnded;
	private boolean isLefted;

	public NetTicTacToe() {

		setTitle("Tic-Tac-Toe");
		setLocationByPlatform(true);
		gameInfo = new GameInfo();
		bUI = new BoardUI(this);
		add(bUI, BorderLayout.CENTER);
		messages = new JTextField();
		messages.setText("Hello!");
		messages.setEditable(false);
		add(messages, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					out.println("disconnect");
					sock.close();
				} catch (Exception err) {
					showPopupMessage("Error", err.getMessage());
				}
				System.exit(0);
			}
		});
		setVisible(true);
		setSize(gameInfo.getWidth() * SQUARE_WIDTH, gameInfo.getHeight()
				* SQUARE_HEIGHT + messages.getHeight());
		full(null);
	}

	private class ConnectionDialog extends JDialog {

		private boolean ok;
		private JButton okButton;
		private JTextField host;
		private JTextField port;

		public ConnectionDialog() {
			super(NetTicTacToe.this, "Connect", true);
			setLayout(new BorderLayout());
			JPanel panel = new JPanel();
			JPanel panelLabel = new JPanel(new GridLayout(2, 1));
			panelLabel.add(new JLabel("Host:"));
			panelLabel.add(new JLabel("Port:"));
			JPanel panelField = new JPanel(new GridLayout(2, 1));
			panelField.add(host = new JTextField(9));
			panelField.add(port = new JTextField(9));
			panel.add(panelLabel);
			panel.add(panelField);
			add(panel, BorderLayout.CENTER);

			JPanel okPanel = new JPanel();
			okButton = new JButton("Ok");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					ok = true;
					setVisible(false);
				}
			});
			okPanel.add(okButton);
			add(okPanel, BorderLayout.SOUTH);
			getRootPane().setDefaultButton(okButton);
			pack();
			setLocationByPlatform(true);
			setResizable(false);
		}

		public boolean showDialog() {
			ok = false;

			setVisible(true);
			return ok;
		}

		public String getHost() {
			return host.getText();
		}

		public int getPort() {
			return Integer.parseInt(port.getText());
		}
	}

	private void makeContact() {
		try {
			sock = new Socket(gameInfo.getHost(), gameInfo.getPort());
			Scanner in = new Scanner(sock.getInputStream());
			out = new PrintWriter(sock.getOutputStream(), true);

			TicTacWatcher ttw = new TicTacWatcher(this, in);
			new Thread(ttw).start();
		} catch (Exception e) {
			full("Cannot contact the server");
		}
	}

	public void tryMove(int x, int y) {
		if (isDisabled) {
			showMessage("Not enough players for game");
		}
		else if(isEnded) {
			showMessage("Game is over, you cannot move");
		} else {
			if (playerID == currPlayer){
				out.println("try " + x + " " + y);
				doMove(x, y, playerID);
			}
		}
	}

	public void doMove(int x, int y, int pid) {
		bUI.tryPosn(x, y, pid);
		currPlayer = currPlayer % gameInfo.getNumOfPlayers() + 1;
		if(!isEnded && !isDisabled)
		if (currPlayer == playerID)
			showMessage("It's your turn now");
		else
			showMessage("Player " + currPlayer + " turn");
	}

	public void tooFew() {
		if(!isLefted) {
			isLefted = true;
			isDisabled = true;
			showPopupMessage("Error", "One of players has left");
			bUI.Clear();
		}
	}

	public void full(String errMsg) {
		ConnectionDialog cd = new ConnectionDialog();
		if(errMsg != null)showPopupMessage("Error", errMsg);
		boolean done = false;
		while (!done) {
			done = true;
			if (!cd.showDialog()) {
				dispose();
				done = true;
			} else {
				try {
					gameInfo.setHost(cd.getHost());
					gameInfo.setPort(cd.getPort());
				} catch (IllegalArgumentException e) {
					showPopupMessage("Error", "Invalid inputs");
					done = false;
				}

				if (done)
					makeContact();

			}
		}
	}

	public void gameWon(int pid) {
		isEnded = true;
		if (pid == playerID)
			showPopupMessage("Game over", "You've won");
		else
			showPopupMessage("Game over", "Player " + pid + " has won");
	}

	public void boardFull() {
		isEnded = true;
		showPopupMessage("Game over", "Board is full");
	}

	public void startNewGame() {
		isDisabled = false;
		isEnded = false;
		isLefted = false;
		currPlayer = 1;
		
		bUI.Clear();
		if (currPlayer == playerID)
			showPopupMessage("New game", "You start the game");
		else
			showPopupMessage("New game", "Player 1 starts the game");
	}

	public void setPlayerID(int id) {
		playerID = id;
		bUI.Clear();
		setSize(gameInfo.getWidth() * SQUARE_WIDTH, gameInfo.getHeight()
				* SQUARE_HEIGHT + messages.getHeight());
		setTitle("Tic-Tac-Toe. Player " + id);
		showMessage("Waiting for other players");
	}

	public void showMessage(String mesg) {
		messages.setText(mesg);
	}

	public void showPopupMessage(String title, String mesg) {
		showMessage(mesg);
		JOptionPane.showMessageDialog(this, mesg, title,
				JOptionPane.PLAIN_MESSAGE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new NetTicTacToe();
			}
		});
	}
}