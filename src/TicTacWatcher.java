import java.awt.*;
import java.util.*;

public class TicTacWatcher implements Runnable {
	private NetTicTacToe nttt;
	private Scanner in;

	public TicTacWatcher(NetTicTacToe nttt, Scanner aIn) {
		this.nttt = nttt;
		in = aIn;
	}

	public void run() {
		String line;
		try {
			boolean done = false;
			while (!done && (line = in.nextLine()) != null) {
				if (line.startsWith("ok"))
					EventQueue.invokeLater(new informGamer(line.substring(3)));
				else if (line.startsWith("newgame"))
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							nttt.startNewGame();
						}
					});
				else if (line.startsWith("full")){
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							nttt.full("Server is full");
						}
					});
					done = true;
				}
				else if (line.startsWith("left"))
					EventQueue.invokeLater(new Runnable() {

						public void run() {
							nttt.tooFew();
						}
						
					});
				else if (line.startsWith("other"))
					EventQueue.invokeLater(new Mover(line.substring(6)));
				else {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							nttt.full("Server access error");
						}
					});
				}
			}
		} catch (Exception e) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					nttt.full("Server access error");
				}
			});
		}
	}

	private class Mover implements Runnable {
		private int playerID;
		private int x;
		private int y;

		public Mover(String s) {
			try {
				StringTokenizer tokens = new StringTokenizer(s);
				playerID = Integer.parseInt(tokens.nextToken());
				x = Integer.parseInt(tokens.nextToken());
				y = Integer.parseInt(tokens.nextToken());
			} catch (IllegalArgumentException e) {
				nttt.showPopupMessage("Error", e.getMessage());
			}
		}

		public void run() {
			nttt.doMove(x, y, playerID);
		}
	}

	private class informGamer implements Runnable {
		private int id;
		private int numOfPlayers;
		private int width;
		private int height;
		private int winNum;

		public informGamer(String s) {
			try {
				StringTokenizer tokens = new StringTokenizer(s);
				id = Integer.parseInt(tokens.nextToken());
				numOfPlayers = Integer.parseInt(tokens.nextToken());
				width = Integer.parseInt(tokens.nextToken());
				height = Integer.parseInt(tokens.nextToken());
				winNum = Integer.parseInt(tokens.nextToken());
			} catch (IllegalArgumentException e) {
				nttt.showPopupMessage("Error", e.getMessage());
			}
		}

		public void run() {
			nttt.gameInfo.setNumOfPlayers(numOfPlayers);
			nttt.gameInfo.setWinNum(winNum);
			nttt.gameInfo.setWidth(width);
			nttt.gameInfo.setHeight(height);
			nttt.setPlayerID(id);
		}
	}
}