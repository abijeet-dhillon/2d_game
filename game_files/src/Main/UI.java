package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import Object.OBJ_Key;

public class UI {
	
	GamePanel gp;
	Font arial_20, arial_50;
	BufferedImage keyImage;
	public boolean messageOn = false;
	public String message = "";
	int messageCount = 0;
	public boolean gameFinished = false;
	double playTime;
	DecimalFormat dFormat = new DecimalFormat("#0.00");
	
	public UI(GamePanel gp) {
		this.gp = gp;
		arial_20 = new Font("Arial", Font.BOLD, 20);
		arial_50 = new Font("Arial", Font.BOLD, 50);
		OBJ_Key key = new OBJ_Key(gp);
		keyImage = key.image;
	}
	
	public void displayMessage(String text) {
		message = text;
		messageOn = true;
	}
	
	public void draw(Graphics2D g2) {
		if(gameFinished == true) {
			g2.setFont(arial_20);
			g2.setColor(Color.WHITE);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN));
			String text = "You found the treasure!";
			int textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			int x = gp.screenWidth/2 - textLength/2;
			int y = gp.screenHeight/2 - (gp.tileSize*2);
			g2.drawString(text, x, y);
			text = "Your time is: "+ dFormat.format(playTime) + "!";
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 + (gp.tileSize*3);
			g2.drawString(text, x, y);
			g2.setFont(arial_50);
			g2.setColor(Color.yellow);
			text = "Congratulations";
			textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 + (gp.tileSize);
			g2.drawString(text, x, y);
			gp.gameThread = null;
		} else {
			g2.setFont(arial_20);
			g2.setColor(Color.WHITE);
			g2.drawImage(keyImage, gp.tileSize, gp.tileSize, gp.tileSize/2, gp.tileSize/2, null);
			g2.drawString("x " + gp.player.hasKey, 74, 65);
			playTime += (double)1/60;
			g2.drawString("Time: " + dFormat.format(playTime), gp.tileSize*12, 65);
			if(messageOn == true) {
				g2.setFont(g2.getFont().deriveFont(Font.PLAIN));
				g2.drawString(message, 24, 550);
				messageCount++;
				if(messageCount > 120) {
					messageCount = 0;
					messageOn = false;
				}
			}
		}
	}
	

}
