package UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Sbody extends JPanel{
	ImageIcon upIcon = new ImageIcon("up.png");
	ImageIcon downIcon = new ImageIcon("down.png");
	ImageIcon leftIcon = new ImageIcon("left.png");
	ImageIcon rightIcon = new ImageIcon("right.png");
	
	ImageIcon body = new ImageIcon("body.png");
	
	
	ForWard f = ForWard.D;

	ForWard oldf = f;

	public int x, y, oldx, oldy;

	public Sbody(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics g) {
		Sbody s = MySnake.snakes.get(0);
		if (s == this){// ÉßÍ·
			switch (s.f) {
				case L:
					leftIcon.paintIcon(this,g,MySnake.locale.width + x,MySnake.locale.height + y);
					break;
				case U:
					upIcon.paintIcon(this,g,MySnake.locale.width + x,MySnake.locale.height + y);
					break;
				case R:
					rightIcon.paintIcon(this,g,MySnake.locale.width + x,MySnake.locale.height + y);
					break;
				case D:
					downIcon.paintIcon(this,g,MySnake.locale.width + x,MySnake.locale.height + y);
					break;
			}
		}else body.paintIcon(this,g,MySnake.locale.width + x,MySnake.locale.height + y);
		//g.setColor(Color.orange);	
		//g.fillRect(MySnake.locale.width + x, MySnake.locale.height + y,
		//		MySnake.SBody_Size, MySnake.SBody_Size);
	}

	public void KeyPress(KeyEvent e) {
		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_LEFT:
			changeFrd(ForWard.L);
			break;
		case KeyEvent.VK_UP:
			changeFrd(ForWard.U);
			break;
		case KeyEvent.VK_RIGHT:
			changeFrd(ForWard.R);
			break;
		case KeyEvent.VK_DOWN:
			changeFrd(ForWard.D);
			break;
		
		}
	}

	public static void changeFrd(ForWard f) {
		Sbody s = MySnake.snakes.get(0);
		switch (f) {
		case L:
			if (s.f != ForWard.R) {
				s.f = ForWard.L;
			}
			break;
		case U:
			if (s.f != ForWard.D)
				s.f = ForWard.U;
			break;
		case R:
			if (s.f != ForWard.L)
				s.f = ForWard.R;
			break;
		case D:
			if (s.f != ForWard.U)
				s.f = ForWard.D;
			break;
		}
	}

	public void move() {
		oldx = x;
		oldy = y;
		switch (f) {
		case L:
			x -= MySnake.SBody_Size;
			break;
		case U:
			y -= MySnake.SBody_Size;
			break;
		case R:
			x += MySnake.SBody_Size;
			break;
		case D:
			y += MySnake.SBody_Size;
			break;
		}
		oldf = this.f;
	}
}
