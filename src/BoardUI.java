import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.geom.Line2D;

import static java.awt.BasicStroke.*;
public class BoardUI extends JComponent {
	
	private Board board;
	private NetTicTacToe nttt;
	
	public BoardUI(NetTicTacToe nttt){
		
		this.nttt = nttt;
		board = new Board(nttt);
		addMouseListener(new MouseHandler());
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2, CAP_SQUARE ,JOIN_BEVEL));
		for(int i = 1; i < nttt.gameInfo.getWidth(); i++){
			Line2D l = new Line2D.Double((double)i*getWidth()/nttt.gameInfo.getWidth(), 5,
					(double)i*getWidth()/nttt.gameInfo.getWidth(), (double)getHeight() - 5);
			g2.draw(l);
		}
		for(int i = 1; i < nttt.gameInfo.getHeight(); i++){
			Line2D l = new Line2D.Double(5,
					(double)i*getHeight()/nttt.gameInfo.getHeight(), 
					(double)getWidth() - 5, (double)i*getHeight()/nttt.gameInfo.getHeight());
			g2.draw(l);
		}
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
        		  RenderingHints.VALUE_ANTIALIAS_ON);
		for(int i = 0; i < nttt.gameInfo.getWidth(); i++){
			for(int j = 0; j < nttt.gameInfo.getHeight(); j++){
				if(board.getPosition(i, j) != 0)
					paintSymbol(g2, board.getPosition(i, j), 
							i*getWidth()/nttt.gameInfo.getWidth(),
							j*getHeight()/nttt.gameInfo.getHeight(),
							getWidth()/nttt.gameInfo.getWidth(),
							getHeight()/nttt.gameInfo.getHeight());
			}
		}
	}
	
	private void paintSymbol(Graphics2D g2, int p, double x, double y, 
			double width, double height) {
		if(p == 1){
			Line2D l1 = new Line2D.Double(x + width/4, y + height/4,
					x + width*3/4, y + height*3/4);
			Line2D l2 = new Line2D.Double(x + width/4, y + height*3/4,
					x + width*3/4, y + height/4);
			g2.draw(l1);
			g2.draw(l2);
		}
		else if(p == 2){
			Ellipse2D e = new Ellipse2D.Double(x + width/5, y + height/5,
					width*3/5, height*3/5);
			g2.draw(e);
		}
		else if(p % 2 == 1){
			y += height/(p*5);
			GeneralPath path = new GeneralPath();
			double angle = -Math.PI/2;
			path.moveTo(x + width/2 + width * 0.4 * Math.cos(angle), 
					y + height/2 + height * 0.4 * Math.sin(angle));
			for(int i = 1; i < p; i++){
				angle += Math.PI*(p-1)/p;
				path.lineTo(x + width/2 + width * 0.4 * Math.cos(angle), 
						y + height/2 + height * 0.4 * Math.sin(angle));
				
			}
			path.closePath();
	        g2.draw(path);
		}
		else {
			GeneralPath path = new GeneralPath();
			double angle = -Math.PI/2;
			path.moveTo(x + width/2 + width * 0.4 * Math.cos(angle), 
					y + height/2 + height * 0.4 * Math.sin(angle));
			for(int i = 1; i < p/ 2+1; i++){
				angle += Math.PI*(p-2)/p;
				path.lineTo(x + width/2 + width * 0.4 * Math.cos(angle), 
						y + height/2 + height * 0.4 * Math.sin(angle));
				
			}
			angle = Math.PI/2;
			path.moveTo(x + width/2 + width * 0.4 * Math.cos(angle), 
					y + height/2 + height * 0.4 * Math.sin(angle));
			for(int i = 1; i < p / 2 + 1; i++){
				angle += Math.PI*(p-2)/p;
				path.lineTo(x + width/2 + width * 0.4 * Math.cos(angle), 
						y + height/2 + height * 0.4 * Math.sin(angle));
				
			}
	        g2.draw(path);
		}
		
		  
	}
	public void tryPosn(int x, int y, int pid)
	  {  board.tryPosn(x, y, pid);  }
	
	public void Clear(){
		board.Clear();
		repaint();
	}
	
	private class MouseHandler extends MouseAdapter
	{
	   public void mousePressed(MouseEvent event)
	   {
		   int x = event.getX()*nttt.gameInfo.getWidth()/getWidth();
		   int y = event.getY()*nttt.gameInfo.getHeight()/getHeight();
		   if(board.getPosition(x, y) == 0)
		   nttt.tryMove(event.getX()*nttt.gameInfo.getWidth()/getWidth(),
				   event.getY()*nttt.gameInfo.getHeight()/getHeight());
	   }
	}
}

