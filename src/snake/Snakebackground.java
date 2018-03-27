package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Snakebackground extends JPanel implements KeyListener,ActionListener {
	ImageIcon upIcon = new ImageIcon("up.png");
	ImageIcon downIcon = new ImageIcon("down.png");
	ImageIcon leftIcon = new ImageIcon("left.png");
	ImageIcon rightIcon = new ImageIcon("right.png");
	ImageIcon[] headIcon = new ImageIcon[5];
	ImageIcon title = new ImageIcon("title.png");
	ImageIcon food = new ImageIcon("food.png");
	ImageIcon body = new ImageIcon("body.png");
	int dirright = 0;
	int dirleft = 1;
	int dirup = 2;
	int dirdown = 3;
	Timer timer = new Timer(100,this);
	
	//蛇的属性
	int[] snake_x = new int[750];
	int[] snake_y = new int[750];
	int len = 3;
	int direction = 1;
	int dirx[] = {25,-25, 0,  0};
	int diry[] = {0 ,  0,-25, 25};
	//0 1 2 3
	//R L U D
	
	//食物属性
	Random rand = new Random();
	int foodx = (rand.nextInt(36))*25 + 25;
	int foody = (rand.nextInt(24))*25 + 75;
	
	
	boolean gamestart = false;
	boolean gameover = false;
	
	public Snakebackground() {
		this.setFocusable(true); //获得焦点
		this.addKeyListener(this);
		//初始化snake
		setup();
		timer.start();
		
	}
	
	public void paint(Graphics x) {
		super.paint(x);
		this.setBackground(Color.white);
		
		title.paintIcon(this, x, 25, 11);
		//背景色
		x.fillRect(25, 75, 900, 600);

		//蛇头
		headIcon[direction].paintIcon(this, x, snake_x[0], snake_y[0]);
		//蛇身
		for (int i=1;i<len;i++) body.paintIcon(this,x,snake_x[i],snake_y[i]);
	
		if (gameover == true) {
			x.setColor(Color.white);
			x.setFont(new Font("arial",Font.BOLD,30));
			x.drawString("Game over", 300, 300);
			x.drawString("Press space to Start again", 300, 400);
		}
		
		if (gamestart == false) {
			x.setColor(Color.white);
			x.setFont(new Font("arial",Font.BOLD,30));
			x.drawString("Press space to Start/Pause", 300, 300);
		}
		
		//食物
		food.paintIcon(this, x, foodx, foody);
		
		x.setColor(Color.white);
		x.setFont(new Font("arial",Font.PLAIN,20));
		x.drawString("Score: " + (len-3), 750, 30);
	}
	
	public void setup() {
		headIcon[0] = rightIcon;
		headIcon[1] = leftIcon;
		headIcon[2] = upIcon;
		headIcon[3] = downIcon;
		gamestart = false;
		gameover = false;
		
		len = 3;
		direction = dirright;
		snake_x[0] = 100;
		snake_y[0] = 100;
		snake_x[1] = 75;
		snake_y[1] = 100;
		snake_x[2] = 50;
		snake_y[2] = 100;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		timer.start(); //重启timer
		//移动身体
		if (gamestart && !gameover) {
			for (int i = len; i > 0 ;i--) {
				snake_x[i] = snake_x[i-1];
				snake_y[i] = snake_y[i-1];
			}
			snake_x[0]+=dirx[direction];
			snake_y[0]+=diry[direction];
			if (snake_x[0] > 900) snake_x[0] = 25;
			if (snake_x[0] < 25)  snake_x[0] = 900;
			if (snake_y[0] > 650) snake_y[0] = 75;
			if (snake_y[0] < 75)  snake_y[0] = 650;
			
		}
		
		//吃到食物
		if (snake_x[0] == foodx && snake_y[0] == foody) {
			len++;
			foodx = (rand.nextInt(36))*25 + 25;
			foody = (rand.nextInt(24))*25 + 75;
		}
		
		//撞到障碍物
		for (int i=1;i<len;i++)
			if (snake_x[i] == snake_x[0] && snake_y[i] == snake_y[0]) {
				gameover = true;
			}

		repaint();
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keycode = e.getKeyCode();
		
		if (keycode == KeyEvent.VK_SPACE) {
			if (gameover) {
				setup();
				
			}else {
				gamestart = !gamestart;
			}
		}
		if (direction!=dirdown && (keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_W)) {
			direction = dirup;
		}else if (direction!=dirup && (keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S)) {
			direction = dirdown;
		}else if (direction!=dirleft && (keycode == KeyEvent.VK_RIGHT || keycode == KeyEvent.VK_D)) {
			direction = dirright;
		}else if (direction!=dirright && (keycode == KeyEvent.VK_LEFT ||  keycode == KeyEvent.VK_A)) {
			direction = dirleft;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
