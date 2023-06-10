package Entity;

import Main.KeyHandler;
import Main.UtilityTool;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class Player extends Entity{
	
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	public int hasKey = 0;
	int standCounter = 0;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		solidArea = new Rectangle(10, 10, 26, 26);
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		setDefaultValues();
		getPlayerImage();
	}
	
	public void setDefaultValues() {
		worldX = gp.tileSize*23;
		worldY = gp.tileSize*21;
		speed = 4;
		direction = "down";
	}
	
	public void getPlayerImage() {
		up1 = setup("boy_up_1");
		up2 = setup("boy_up_2");
		down1 = setup("boy_down_1");
		down2 = setup("boy_down_2");
		left1 = setup("boy_left_1");
		left2 = setup("boy_left_2");
		right1 = setup("boy_right_1");
		right2 = setup("boy_right_2");
	}
	
	public BufferedImage setup(String imageName) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
			image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public void update() {
		if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true) {
			if(keyH.upPressed == true) {
				direction = "up";
			}
			else if(keyH.downPressed == true) {
				direction = "down";
			}
			else if(keyH.leftPressed == true) {
				direction = "left";
			}
			else if(keyH.rightPressed == true) {
				direction = "right";
			}
			// check tile collision
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			// check object collision
			int objIndex = gp.cChecker.checkObject(this, true);
			interactObject(objIndex);
			
			// if collision is false, player can move
			if (collisionOn == false) {
				if(direction == "up") {
					worldY -= speed;
				}
				else if(direction == "down") {
					worldY += speed;
				}
				else if(direction == "left") {
					worldX -= speed;
				}
				else if(direction == "right") {
					worldX += speed;
				}
			}
			
			spriteCounter++;
			if(spriteCounter > 13) {
				if(spriteNum == 1) {
					spriteNum = 2;
				}
				else if(spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}
		else {
			standCounter++;
			if(standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}
	}
	
	public void interactObject(int i) {
		if(i != 999) {
			String objectName = gp.obj[i].name;
			switch(objectName) {
				case "Key":
					hasKey++;
					gp.obj[i] = null;
					gp.soundEffects(1);
					gp.uI.displayMessage("You picked up a key!");
					break;
				case "Door":
					if(hasKey > 0) {
						gp.obj[i] = null;
						gp.soundEffects(3);
						hasKey--;
						gp.uI.displayMessage("You opened a door!");
						break;
					} else {
						gp.uI.displayMessage("You need a key to open this door!");	
						break;
					}
				case "Boots":
					speed += 1;
					gp.obj[i] = null;
					gp.soundEffects(2);
					gp.uI.displayMessage("+1 Speed");
					break;
				case "Chest":
					gp.uI.gameFinished = true;
					gp.stopMusic();
					gp.soundEffects(4);
					break;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		switch(direction) {
		case "up":
			if(spriteNum == 1) {
				image = up1;
			}
			if(spriteNum == 2) {
				image = up2;
			}
			break;
		case "down":
			if(spriteNum == 1) {
				image = down1;
			}
			if(spriteNum ==2) {
				image = down2;
			}
			break;
		case "left":
			if(spriteNum == 1) {
				image = left1;
			}
			if(spriteNum == 2) {
				image = left2;
			}
			break;
		case "right":
			if(spriteNum == 1) {
				image = right1;
			}
			if(spriteNum == 2) {
				image = right2;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, null);
	}

}
