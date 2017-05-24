/*
* TODO
* Add Snake title to main menu
* Save separate high score for each difficulty/size combination
* Show score and highscore when game is over
* When game is over, don't reset window location
* 
* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Benjamin
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;
	private Snake sf;
	private int highscore;
	private volatile boolean gameStarted;
	// For best results, WINDOW_DIMENSION should be a multiple of Snake.SIZE
	public static int WINDOW_DIMENSION = 300;

	private JButton newGameBtn = new JButton("New Game");
	private JButton difficultyButton = null;
	private JButton windowSizeBtn = null;

	private JPanel menuPanel = null;
	private JPanel gamePanel = null;

	public Window() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		pack();

		displayMenu();
	}

	public void displayMenu() {
		gameStarted = false;

		menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(Window.WINDOW_DIMENSION, Window.WINDOW_DIMENSION));
		menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 1000, 10));

		newGameBtn.setPreferredSize(new Dimension(100, 100));
		newGameBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameStarted = true;
			}
		});

		if (difficultyButton == null) {
			difficultyButton = new JButton("Difficulty: Normal");
			difficultyButton.setPreferredSize(new Dimension(100, 100));
			difficultyButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					difficultyButton.setText(setSpeed());
				}
			});
		}

		if (windowSizeBtn == null) {
			windowSizeBtn = new JButton("Window Size: Very Small");
			windowSizeBtn.setPreferredSize(new Dimension(100, 100));
			windowSizeBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					windowSizeBtn.setText(setGameWindowSize());
					menuPanel.setPreferredSize(new Dimension(WINDOW_DIMENSION, WINDOW_DIMENSION));
					pack();
					setLocationRelativeTo(null);
				}
			});
		}

		newGameBtn.setPreferredSize(new Dimension(200, 50));
		difficultyButton.setPreferredSize(new Dimension(200, 50));
		windowSizeBtn.setPreferredSize(new Dimension(200, 50));

		menuPanel.add(newGameBtn);
		menuPanel.add(difficultyButton);
		menuPanel.add(windowSizeBtn);

		add(menuPanel);

		pack();

		// because sometimes it doesn't work the first time ¯\_(ツ)_/¯
		if (getContentPane().getWidth() > WINDOW_DIMENSION) {
			pack();
		}
		
		setLocationRelativeTo(null);

		while (!gameStarted)
			;

		if (gameStarted) {
			initGame();
		}

		menuPanel.setVisible(false);
	}

	public String setGameWindowSize() {
		String text = "Window Size: Very Small";
		if (WINDOW_DIMENSION >= 700) {
			WINDOW_DIMENSION = 300;
		} else {
			WINDOW_DIMENSION += 100;
			if (WINDOW_DIMENSION == 400) {
				text = "Window Size: Small";
			} else if (WINDOW_DIMENSION == 500) {
				text = "Window Size: Medium";
			} else if (WINDOW_DIMENSION == 600) {
				text = "Window Size: Large";
			} else {
				text = "Window Size: Very Large";
			}
		}
		return text;
	}

	public String setSpeed() {
		if (Snake.SPEED <= 25) {
			Snake.SPEED = 100;
			return "Difficulty: Easy";
		} else {
			Snake.SPEED -= 25;
			if (Snake.SPEED == 75) {
				return "Difficulty: Normal";
			} else if (Snake.SPEED == 50) {
				return "Difficulty: Hard";
			} else {
				return "Difficulty: Very Hard";
			}
		}
	}

	public void initGame() {
		sf = new Snake();

		Collectible.randomizeLocation(getContentPane().getWidth(), getContentPane().getHeight(), sf);

		File file = new File("snake.config");
		if (file.exists()) {
			FileReader fr;
			try {
				fr = new FileReader(file);
				BufferedReader bf = new BufferedReader(fr);
				highscore = Integer.parseInt(bf.readLine());
				bf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			highscore = 0;
		}

		gamePanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			Font font = new Font("TimesRoman", Font.PLAIN, 20);

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				for (Point point : sf.getSnake()) {
					g.setColor(Color.BLACK);
					g.fillRect(point.x, point.y, Snake.SIZE, Snake.SIZE);
				}
				g.setColor(Color.RED);
				g.fillRect(Collectible.X, Collectible.Y, Snake.SIZE, Snake.SIZE);

				g.setFont(font);
				FontMetrics fontMetrics = g.getFontMetrics();
				String count = Integer.toString(Collectible.COLLECTED_COUNTER);
				String hscount = Integer.toString(highscore);
				g.drawString("Score: " + count, Window.WINDOW_DIMENSION - fontMetrics.stringWidth("Score: " + count) - 5, 20);
				g.drawString("High Score: " + hscount, Window.WINDOW_DIMENSION - fontMetrics.stringWidth("High Score: " + hscount) - 5, 40);
			}
		};
		gamePanel.setPreferredSize(new Dimension(Window.WINDOW_DIMENSION, Window.WINDOW_DIMENSION));
		add(gamePanel);

		pack();

		// because sometimes it doesn't work the first time ¯\_(ツ)_/¯
		if (getContentPane().getWidth() > WINDOW_DIMENSION) {
			pack();
		}

		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		gamePanel.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN && sf.getDirection() != SnakeFront.FACING_UP) {
					sf.setDirection(SnakeFront.FACING_DOWN);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP && sf.getDirection() != SnakeFront.FACING_DOWN) {
					sf.setDirection(SnakeFront.FACING_UP);
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT && sf.getDirection() != SnakeFront.FACING_RIGHT) {
					sf.setDirection(SnakeFront.FACING_LEFT);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT && sf.getDirection() != SnakeFront.FACING_LEFT) {
					sf.setDirection(SnakeFront.FACING_RIGHT);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});
	}

	public void reset() {
		sf.resetSnake();

		if (highscore == Collectible.COLLECTED_COUNTER) {
			File file = new File("snake.config");
			try {
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(writer);
				bw.write(Integer.toString(highscore));
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Collectible.COLLECTED_COUNTER = 0;
		Collectible.randomizeLocation(Window.WINDOW_DIMENSION, Window.WINDOW_DIMENSION, sf);

		gamePanel.setVisible(false);
		displayMenu();
	}

	public void gameLoop() throws InterruptedException {
		while (true) {
			Point prev = new Point(sf.getSnake().get(sf.getSnake().size() - 1));
			sf.moveForward();
			repaint();
			if (sf.checkColCollision()) {
				Collectible.COLLECTED_COUNTER++;
				Collectible.randomizeLocation(getContentPane().getWidth(), getContentPane().getHeight(), sf);
				if (Collectible.COLLECTED_COUNTER > highscore) {
					highscore = Collectible.COLLECTED_COUNTER;
				}

				sf.getSnake().add(prev);
				repaint();
			}

			if (sf.checkSnakeCollision() || sf.checkEdgeCollision()) {
				if (!sf.isInvincible()) {
					reset();
				}
			}
			Thread.sleep(Snake.SPEED);

		}
	}
}
