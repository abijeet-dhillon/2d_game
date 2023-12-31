package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Entity.Player;
import Object.SuperObject;
import Tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
	
	// screen settings 
	final int originalTileSize = 16; 
	final int scale = 3;
	public final int tileSize = originalTileSize * scale;
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol;  // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow;  // 576 pixels
	
	// world settings
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
//	public final int worldWidth = tileSize + maxWorldCol;
//	public final int worldHeight = tileSize + maxWorldRow;

	// FPS
	int FPS = 60;
	
	// system
	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Sound music = new Sound();
	Sound soundEffects = new Sound();
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssestSetter aSetter = new AssestSetter(this);
	public UI uI = new UI(this);
	Thread gameThread;
	
	//entity & object
	public Player player = new Player(this,keyH);
	public SuperObject obj[] = new SuperObject[10];
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void setupGame() {
		aSetter.setObject();
		playMusic(0);
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
//	public void run() {
//		double drawInterval = 1000000000/FPS;
//		double nextDrawTime = System.nanoTime() + drawInterval;
//		while(gameThread != null) {
//			update();
//			repaint();
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime();
//				remainingTime /= 1000000;
//				if(remainingTime < 0) {
//					remainingTime = 0;
//				}
//				Thread.sleep((long)remainingTime);
//				nextDrawTime += drawInterval;
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	public void run() {
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount= 0;
		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
//			if(timer >= 1000000000) {
//				System.out.println("FPS: " + drawCount);
//				drawCount = 0;
//				timer = 0;
//			}
		}
	}
	
	public void update() {
		player.update();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		// debug
		long drawStart = 0;
		if(keyH.checkDrawTime == true) {
			drawStart = System.nanoTime();
		}
		
		// tile
		tileM.draw(g2);
		// object
		for(int i = 0; i < obj.length; i++) {
			if(obj[i] != null) {
				obj[i].draw(g2, this);
			}
		}
		// player
		player.draw(g2);
		// UI
		uI.draw(g2);
		// debug
		if(keyH.checkDrawTime == true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			g2.setColor(Color.white);
			g2.drawString("Draw Time: " + passed, 10, 400);
			System.out.println("Draw Time: " + passed);
			}
		g2.dispose();
	}
	
	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void soundEffects(int i) {
		soundEffects.setFile(i);
		soundEffects.play();
	}

}
