package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import AI.AI_II;
import AI.Snake_AI;

public class MySnake extends Frame {
	
	ImageIcon foodIcon = new ImageIcon("food.png");
	/**
	 * 蛇的尺寸大小
	 */
	public static final int SBody_Size = 25;

	/**
	 * 用于去除边框
	 */
	public static final Dimension _space = new Dimension(6, 28);

	/**
	 * 调整绘制的位置
	 */
	public static final Dimension locale = new Dimension(3, 25);
	
	/**
	 * 控制刷新循环
	 */
	public static boolean stop = false;
	
	/**
	 * 用于对snakes进行遍历
	 */
	Sbody s = null;
	
	/**
	 * 记录下蛋的位置
	 */
	Point Food = null;
	
	/**
	 * 用于产生随机数从而产生蛋
	 */
	Random ran = new Random();
	
	/**
	 * 存储贪吃蛇的身体
	 */
	public static ArrayList<Sbody> snakes = new ArrayList<Sbody>();

	/**
	 * 棋盘状态常量
	 */
	public static final int _SPACE = 0;// 空地

	public static final int _Food = -1;// 蛋

	public static final int _SNAKE_BODY = 1;// 蛇身

	public static final int _SNAKE_HEAD = 2;// 蛇头

	public static final int _SNAKE_TAIL = 3;// 蛇尾

	/**
	 * 记录整个棋盘的状态
	 */
	ArrayList<int[]> plant = new ArrayList<int[]>();

	/**
	 * AI
	 */
	Snake_AI ai = new AI_II();
	
	Color back = Color.black;//背景色
	
	/**
	 * 主方法入口函数
	 */
	public static void main(String[] args) {
		
	}

	/**
	 * 创建窗口
	 * @param line_x 贪吃蛇界面的长
	 * @param line_y 贪吃蛇界面的宽
	 */
	public void lauchFrame(int line_x, int line_y) {
		/**
		 * 进行基础设置
		 */
		this.setSize(line_x * SBody_Size + _space.width, 
				     line_y * SBody_Size + _space.height);
		this.setBounds(10,10, this.getWidth(), this.getHeight());
		this.setBackground(back); //new Color(104, 104, 255);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});/* 实现关闭按钮 */
		this.setTitle("Snake");
		this.setResizable(false);
		this.addKeyListener(new KeyMonitor());
		
		/**
		 * 初始化蛇身和蛋
		 */
		init();
		
		/**
		 * 启动刷新线程
		 */
		new Thread(new PaintThread()).start();
	}
	void init() {
		/**
		 * 初始化蛇身
		 */
		for (int i = 7; i >= 0; i--) 
			snakes.add(new Sbody(0, 0 + i * SBody_Size));

		/**
		 * 初始化棋盘
		 */
		for (int i = 0; i < (this.getWidth() - _space.width) / SBody_Size; i++)
			plant.add(new int[(this.getHeight() - _space.height) / SBody_Size]); 
		
		/**
		 * 将变更写入棋盘
		 */
		frush();
		
		/**
		 * 初始化蛋
		 */
		newFood();
	}

	/**
	 * 刷新棋盘
	 */
	private void frush() {		
		s = snakes.get(snakes.size() - 1);
		
		if (plant.get(s.oldx / SBody_Size)[s.oldy / SBody_Size] == _SNAKE_TAIL){
			plant.get(s.oldx / SBody_Size)[s.oldy / SBody_Size] = 0;
		}
		
		for (int i = 0; i < snakes.size(); i++) {
			s = snakes.get(i);
			plant.get(s.x / SBody_Size)[s.y / SBody_Size] = (i == 0 ? _SNAKE_HEAD
					: i == snakes.size() - 1 ? _SNAKE_TAIL : _SNAKE_BODY);
		}
		s = snakes.get(0);
	}

	/**
	 * 初始化蛋
	 */
	private void newFood(){
		Food = new Point();
		
		if (plant.size() * plant.get(0).length - snakes.size() > plant.size() * plant.get(0).length / 2){
			int temp = ran.nextInt(plant.size() * plant.get(0).length - snakes.size() - 1) + 1;	
			getFood(temp,Food);
		}else {
			Point _Food = new Point();
			boolean first = true;
			int value = 0;
			S:for(int i=1;i<=plant.size() * plant.get(0).length - snakes.size();i++){	
				getFood(i, _Food);
				if (first){
					first = false;
					Food.x = _Food.x;
					Food.y = _Food.y;
				}
				
				Point temp_Food;
				int _temp = 0;
				temp_Food = get(_Food, 1, 0);
				if (check_up(temp_Food))
					_temp += 1;
				temp_Food = get(_Food, -1, 0);
				if (check_up(temp_Food))
					_temp += 1;
				temp_Food = get(_Food, 0,-1);
				if (check_up(temp_Food))
					_temp += 1;
				temp_Food = get(_Food, 0, 1);
				if (check_up(temp_Food))
					_temp += 1;
				
				if(value < _temp){
					value = _temp;
					Food.x = _Food.x;
					Food.y = _Food.y;// Food = _Food; 血的教训啊，为了这个小bug弄了半天 ，艹
					if (value == 4)
						break S;
				}
			}
		}
		plant.get(Food.x / SBody_Size)[Food.y / SBody_Size] = _Food;
	}
	/**
	 * 辅助newFood()
	 * @param index
	 * @param p
	 */
	public void getFood(int index,Point p){
		while(index > 0)
			F:for (int y=0;y<plant.size();y++){ 
				for (int x=0;x<plant.get(0).length;x++){
				 if (plant.get(x)[y] == _SPACE)
					 index -= 1;
				 
				 if (index == 0){
					 p.x = x * SBody_Size;
					 p.y = y * SBody_Size;
					 break F;
				 }	 
			  }
			}
	}
	
	/**
	 * 检查该点是否为有效点和空地
	 * @param e
	 * @return
	 */
	public boolean check_up(Point e){
		return (e.x / SBody_Size >= 0 && e.y / SBody_Size >= 0 && e.x / SBody_Size < plant.size() && e.y / SBody_Size < plant.get(0).length && plant.get(e.x / SBody_Size)[e.y / SBody_Size] == 0);
	}
	
	/**
	 * 辅助newFood()
	 */
	public Point get(Point e, int i, int j) {
		return new Point(e.x + i * 10,e.y + j * 10);
	}
	
	/**
	 * 刷新线程
	 */
	public void paint(Graphics g) {
		if (stop) {
			return;
		}
		
		ForWard frd = ai.play(plant);
		if (frd != null) s.changeFrd(frd);
		
		for (int i = 0; i < snakes.size(); i++) {
			s = snakes.get(i);
			s.draw(g);
			if (!stop)
				s.move();

			if (i != 0) {
				s.oldf = s.f;
				s.f = snakes.get(i - 1).oldf;


				if (snakes.get(0).x == s.x && snakes.get(0).y == s.y) {
					gameOver();
				} 

			}else 
				if (s.x < 0|| s.y < 0|| 
					s.x > this.getWidth() - _space.width - SBody_Size|| 
					s.y > (this.getHeight() - _space.height)- SBody_Size){
					gameOver();
					s.x = s.oldx;
					s.y = s.oldy;
				}

		}
		
		//画出食物
		//g.setColor(new Color(104, 255, 104));
		//g.fillRect(locale.width + Food.x, locale.height + Food.y, SBody_Size,
		//		SBody_Size);
		foodIcon.paintIcon(this,g,locale.width + Food.x, locale.height + Food.y);
		
		frush();
		
		if (snakes.get(0).x == Food.x && snakes.get(0).y == Food.y) {
			this.addSbody();
			newFood();
		}	
	}

	/**
	 * 双缓冲
	 */
	Image i = null;

	public void update(Graphics g) {
		if (i == null) {
			i = this.createImage(this.getWidth(), this.getHeight());
		}
		Graphics G = i.getGraphics();
		Color c = G.getColor();
		G.setColor(back);
		G.fillRect(0, 0, this.getWidth(), this.getHeight());
		G.setColor(c);
		paint(G);
		g.drawImage(i, 0, 0, null);
	}

	private class PaintThread implements Runnable {
		public void run() {
			while (!stop) {			
				repaint();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class KeyMonitor extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			snakes.get(0).KeyPress(e);
		}
	}

	public void gameOver() {
		stop = true;
	}

	public void addSbody() {
		Sbody s = new Sbody(snakes.get(snakes.size() - 1).oldx,
				snakes.get(snakes.size() - 1).oldy);
		s.f = snakes.get(snakes.size() - 1).oldf;
		snakes.add(s);
		frush();
	}
}