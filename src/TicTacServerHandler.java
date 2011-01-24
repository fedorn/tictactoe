import java.net.*;
import java.io.*;
import java.util.*;

public class TicTacServerHandler implements Runnable {
	private TicTacServer server;
	private Socket clientSock;
	private Scanner in;
	private PrintWriter out;

	private int playerID;

	public TicTacServerHandler(Socket s, TicTacServer serv) {
		clientSock = s;
		server = serv;
		System.out.println("Player connection request");
		try {
			in = new Scanner(clientSock.getInputStream());
			out = new PrintWriter(clientSock.getOutputStream(), true);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void run() {
		playerID = server.addPlayer(this);
		if (playerID != -1) {
			sendMessage("ok " + playerID + " "
					+ server.gameInfo.getNumOfPlayers() + " "
					+ server.gameInfo.getWidth() + " "
					+ server.gameInfo.getHeight() + " "
					+ server.gameInfo.getWinNum());
			System.out.println("Player " + playerID + " connected");
			if (server.enoughPlayers()) {
				System.out.println("New game started");
				sendMessage("newgame");
				server.tellOther(playerID, "newgame");
			}

			processPlayerInput();

			server.removePlayer(playerID);
			server.tellOther(playerID, "left");
		} else
			sendMessage("full");

		try {
			clientSock.close();
			System.out.println("Player " + playerID + " connection closed");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void processPlayerInput() {
		String line;
		boolean done = false;
		while (!done) {
			if ((line = in.nextLine()) == null)
				done = true;
			else {
//				System.out.println("Player " + playerID + " msg: " + line);
				if (line.trim().equals("disconnect"))
					done = true;
				else
					doRequest(line);
			}
		}
	}

	private void doRequest(String line) {
		StringTokenizer tokens = new StringTokenizer(line);
		if (tokens.nextToken().equals("try")) {
			try {
				int x = Integer.parseInt(tokens.nextToken());
				int y = Integer.parseInt(tokens.nextToken());
				System.out.println("Player " + playerID
						+ " occupy position " + x + " " + y);

				server.tellOther(playerID, "other " + playerID + " " + x + " "
						+ y);
			} catch (IllegalArgumentException e) {
				System.out.println(e);
			}
		}
	}

	synchronized public void sendMessage(String msg) {
		try {
			out.println(msg);
		} catch (Exception e) {
			System.out.println("Handler for player " + playerID + "\n" + e);
		}
	}
}