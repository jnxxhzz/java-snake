package snake;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Snake {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Snake();
	}
	public Snake() {
		JFrame frame = new JFrame();
		//基本窗口设置
		frame.setBounds(10,10,1000,720);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Snake");
		
		Snakebackground panel = new Snakebackground();
		
		frame.add(panel);
		
		
		frame.setVisible(true);
	}

}
