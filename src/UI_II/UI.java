package UI_II;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import UI.MySnake;
import snake.Snake;

public class UI extends JPanel implements MouseListener{
	JButton snake_game = new JButton("Snake_Game");
	JButton snake_ai = new JButton("Snake_AI");
	JFrame frame = new JFrame();
	JPanel center = new JPanel();
	public UI() {

		//基本窗口设置
		frame.setBounds(10,10,100,200);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Snake");
		center.setLayout(new GridLayout(2,1));
		JLabel shang = new JLabel("↑正常游戏");
		JLabel xia = new JLabel("↓AI自动");
		center.add(shang);
		center.add(xia);
		
		snake_game.addMouseListener(this);
		snake_ai.addMouseListener(this);
	
		frame.add(snake_game,BorderLayout.NORTH);
		frame.add(snake_ai,BorderLayout.SOUTH);
		frame.add(center, BorderLayout.CENTER);
		
		frame.setVisible(true);
	}
	
	public void paint(Graphics x) {
		super.paint(x);
		x.setColor(Color.white);
		x.setFont(new Font("arial",Font.BOLD,10));
		x.drawString("Game over", 30, 10);
		//x.drawString("Press space to Start again", 300, 400);
	
	}
	
	public static void main(String args[]) {
		new UI();
	}

	@Override
	public void mouseClicked(MouseEvent xx) {
		// TODO Auto-generated method stub
		JButton button = (JButton)xx.getSource();
		if (button == snake_game) {
			new Snake();
		}
		else {
			new MySnake().lauchFrame(15,15);
			
		}
		frame.setVisible(false);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
