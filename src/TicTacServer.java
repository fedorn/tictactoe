import java.net.*;
import java.util.*;

public class TicTacServer {
	public GameInfo gameInfo;
	private TicTacServerHandler[] handlers;
	private int numPlayers;

	public TicTacServer(int aPort, int aNumOfPlayers, int aWidth, int aHeight,
			int aWinNum) {
		gameInfo = new GameInfo(aPort, aNumOfPlayers, aWidth, aHeight, aWinNum);
		handlers = new TicTacServerHandler[gameInfo.getNumOfPlayers()];
		numPlayers = 0;
		ConsoleHandler ch = new ConsoleHandler();
		new Thread(ch).start();
		try {
			ServerSocket serverSock = new ServerSocket(gameInfo.getPort());
			Socket clientSock;
			while (true) {
				System.out.println("Waiting for a client...");
				clientSock = serverSock.accept();
				TicTacServerHandler p = new TicTacServerHandler(clientSock,
						this);
				new Thread(p).start();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public boolean enoughPlayers() {
		return (numPlayers == gameInfo.getNumOfPlayers());
	}

	public int addPlayer(TicTacServerHandler h) {
		for (int i = 0; i < gameInfo.getNumOfPlayers(); i++)
			if (handlers[i] == null) {
				handlers[i] = h;
				numPlayers++;
				return i + 1;
			}
		return -1;
	}

	public void removePlayer(int playerID) {
		handlers[playerID - 1] = null;
		numPlayers--;
	}

	public void tellOther(int playerID, String msg) {
		for (int i = 0; i < gameInfo.getNumOfPlayers(); i++) {
			if (i != playerID - 1 && handlers[i] != null)
				handlers[i].sendMessage(msg);
		}
	}

	public static void main(String args[]) {
		new TicTacServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
				Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer
						.parseInt(args[4]));
	}

	private class ConsoleHandler implements Runnable {
		public void run() {
			Scanner in = new Scanner(System.in);
			String command;
			while(!(command = in.nextLine().trim()).equals("exit")){
				if (command.equals("reset"))
					if(enoughPlayers()){
						for (int i = 0; i < gameInfo.getNumOfPlayers(); i++)
							handlers[i].sendMessage("newgame");
						System.out.println("Restarted");
					}
					else {
						System.out.println("Not enough players");
					}
			}
			System.exit(0);
			
		}
	}
}