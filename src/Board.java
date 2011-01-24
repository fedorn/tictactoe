public class Board {

	private int nmoves;
	private int[][] positions;

	private NetTicTacToe nttt;

	public Board(NetTicTacToe nttt) {
		positions = new int[nttt.gameInfo.getWidth()][nttt.gameInfo.getHeight()];
		this.nttt = nttt;

		nmoves = 0;
	}

	public int getPosition(int x, int y) {
		return positions[x][y];
	}
	
	public void Clear(){
		nmoves = 0;
		positions = new int[nttt.gameInfo.getWidth()][nttt.gameInfo.getHeight()];
	}

	public void tryPosn(int x, int y, int pid) {
		positions[x][y] = pid;
		nttt.repaint();
		nmoves++;
		if (isWinner(pid)) nttt.gameWon(pid);
		else if (nmoves == nttt.gameInfo.getWidth() * nttt.gameInfo.getHeight())
		nttt.boardFull();
	}

	private boolean isWinner(int p) {
		for (int i = 0; i < nttt.gameInfo.getHeight(); i++) {
			int c = 0;
			for (int j = 0; j < nttt.gameInfo.getWidth(); j++) {
				if (positions[j][i] == p) {
					c++;
					if (c == nttt.gameInfo.getWinNum())
						return true;
				} else
					c = 0;
			}
		}
		for (int i = 0; i < nttt.gameInfo.getWidth(); i++) {
			int c = 0;
			for (int j = 0; j < nttt.gameInfo.getHeight(); j++) {
				if (positions[i][j] == p) {
					c++;
					if (c == nttt.gameInfo.getWinNum())
						return true;
				} else
					c = 0;
			}
		}
		int tr = 0, h = nttt.gameInfo.getHeight(), w = nttt.gameInfo.getWidth();

		if (nttt.gameInfo.getHeight() > nttt.gameInfo.getWidth()){
			tr = 1;
			h = nttt.gameInfo.getWidth();
			w = nttt.gameInfo.getHeight();
		}

		for (int i = 0; i < h - nttt.gameInfo.getWinNum() + 1; i++) {
			int c1 = 0;
			int c2 = 0;
			for (int j = 0; j < h - i; j++) {
				if (transPos(j, i + j, tr) == p) {
					c1++;
					if (c1 == nttt.gameInfo.getWinNum())
						return true;
				} else
					c1 = 0;
				if (transPos(j, h - 1 - (i + j), tr) == p) {
					c2++;
					if (c2 == nttt.gameInfo.getWinNum())
						return true;
				} else
					c2 = 0;
			}
		}

		for (int i = 1; i < w - h; i++) {
			int c1 = 0;
			int c2 = 0;
			for (int j = 0; j < h; j++) {
				if (transPos(i + j, j, tr) == p) {
					c1++;
					if (c1 == nttt.gameInfo.getWinNum())
						return true;
				} else
					c1 = 0;
				if (transPos(i + j, h - 1 - j, tr) == p) {
					c2++;
					if (c2 == nttt.gameInfo.getWinNum())
						return true;
				} else
					c2 = 0;
			}
		}
		
		for (int i = 0; i < h - nttt.gameInfo.getWinNum() + 1; i++) {
			int c1 = 0;
			int c2 = 0;
			for (int j = 0; j < h - i; j++) {
				if (transPos(w-1 - j, i + j, tr) == p) {
					c1++;
					if (c1 == nttt.gameInfo.getWinNum())
						return true;
				} else
					c1 = 0;
				if (transPos(w-1 - j, h - 1 - (i + j), tr) == p) {
					c2++;
					if (c2 == nttt.gameInfo.getWinNum())
						return true;
				} else
					c2 = 0;
			}
		}

		return false;

	}

	private int transPos(int x, int y, int transpose) {
		if (transpose == 0)
			return positions[x][y];
		else
			return positions[y][x];
	}

}