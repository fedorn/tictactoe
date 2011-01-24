public class GameInfo {
		
		public GameInfo(int aPort, int aNumOfPlayers, int aWidth,
				int aHeight, int aWinNum){
			port = aPort;
			numOfPlayers = aNumOfPlayers;
			winNum = aWinNum;
			width = aWidth;
			height = aHeight;
		}
		
		public GameInfo(){
		}
		
		private int port;
		private String host;
		private int numOfPlayers;
		private int winNum;
		private int width = 5;
		private int height = 5;

		public int getPort() {
			return port;
		}
		
		public String getHost() {
			return host;
		}
		public int getNumOfPlayers() {
			return numOfPlayers;
		}

		public int getWinNum() {
			return winNum;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
		
		public void setPort(int port) {
			this.port = port;
		}
		
		public void setHost(String host) {
			this.host = host;
		}
		
		
		public void setNumOfPlayers(int numOfPlayers) {
			this.numOfPlayers = numOfPlayers;
		}

		public void setWinNum(int winNum) {
			this.winNum = winNum;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}