package AI;

import java.util.ArrayList;
import java.util.Arrays;

import AI.AI_I.Node;
import UI.ForWard;
import UI.MySnake;
import UI.Sbody;

public class AI_II implements Snake_AI {
	ArrayList<Node> nodes = null;
	static ArrayList<int[]> plant = null;

	public static Long _short = 0x00000001L;
	public static Long _long = 0x00000002L;

	@Override
	public ForWard play(ArrayList<int[]> plant) {
		this.plant = plant;

		// ͨ��A*�㷨��Ѱ��̵���ʳ��ľ���
		Node n = search();
		Node start = find(MySnake._SNAKE_HEAD);
		
		if (n == null)
			return null;
		if (n.x == start.x + 1)
			return ForWard.R;

		if (n.x == start.x - 1)
			return ForWard.L;

		if (n.y == start.y + 1)
			return ForWard.D;

		if (n.y == start.y - 1)
			return ForWard.U;

		return null; // ����ԭ״
	}

	/*
	 * ������ԳԵ��� ��һ��������� ������군�Ժ󻹿�����·���ߵ�β���� �Ե�ȥ ������ܳԵ��� ��β��תȦ------�ȵ����ԳԵ�����ʱ���ȥ�Ե�
	 * ������ܳԵ�����������β��תȦ ��ȥ����ɢɢ��
	 * 
	 * ���ѭ��
	 */
	ArrayList<Node> long_path = null;
	int index = 0;
	private Node search() {
		nodes = Search(find(MySnake._SNAKE_HEAD), find(MySnake._Food), _short,plant);// �ҵ���ͷ��������̾���
		
		if (nodes != null && canEat(nodes)){
			index = 0;
			long_path = null;
			return nodes.get(nodes.size() - 1);				
		}else{
			nodes = Search(find(MySnake._SNAKE_HEAD), find(MySnake._SNAKE_TAIL),
						_long, plant);
			
			if (nodes != null){
				return nodes.get(nodes.size() - 1);
			}else{
				return walk_about();
			}
		}
		
	}
	private Node walk_about() {
			
		Node start = find(MySnake._SNAKE_HEAD);
		Node temp = new Node();
		
		temp = get(start, 1, 0);
		if (check_up(temp) && plant.get(temp.x)[temp.y] == 0)
			return temp;
			
		temp = get(start, -1, 0);
		if (check_up(temp) && plant.get(temp.x)[temp.y] == 0)
			return temp;
		temp = get(start, 0, 1);
		if (check_up(temp) && plant.get(temp.x)[temp.y] == 0)
			return temp;
		temp = get(start, 0, -1);
		if (check_up(temp) && plant.get(temp.x)[temp.y] == 0)
			return temp;
		
		return null;
	}

	private boolean canEat(ArrayList<Node> nodes) {
		Sbody s = null;
		
		//I��ģ����߳��군�������״̬
		ArrayList<int[]> copyOFplant = new ArrayList<int[]>();
		for(int i=0;i<plant.size();i++){
			copyOFplant.add(new int[plant.get(0).length]);
			
			for(int j=0;j<copyOFplant.get(i).length;j++)
				copyOFplant.get(i)[j] = plant.get(i)[j];
		}
		
		
		for (int i = 0; i <= MySnake.snakes.size(); i++) {
			if (i < nodes.size()) {
				copyOFplant.get(nodes.get(i).x)[nodes.get(i).y] = (i == 0 ? MySnake._SNAKE_HEAD
						: i == MySnake.snakes.size() ? MySnake._SNAKE_TAIL
								: MySnake._SNAKE_BODY);
			} else {
				s = MySnake.snakes.get(i - nodes.size());
				copyOFplant.get(s.x / MySnake.SBody_Size)[s.y/ MySnake.SBody_Size] = (i == 0 ? MySnake._SNAKE_HEAD
						: i == MySnake.snakes.size() ? MySnake._SNAKE_TAIL
								: MySnake._SNAKE_BODY);
			}
		}
		
		for (int i=0;i < nodes.size() - 1;i++){
			if (i >= MySnake.snakes.size())
				break;
			
			s = MySnake.snakes.get(MySnake.snakes.size() - i - 1);
			copyOFplant.get(s.x / MySnake.SBody_Size)[s.y / MySnake.SBody_Size] = 0;
		}

	    //II������ʱ��snake����·�����Ե���β�;�ȥ��
		if (Search(find(MySnake._SNAKE_HEAD,copyOFplant), find(MySnake._SNAKE_TAIL,copyOFplant),_long, copyOFplant) != null)
			return true;
		return false;
	}
	
	public boolean check_up(Node n){
		return (n.x >= 0 && n.y >= 0 && n.x < plant.size() && n.y < plant.get(0).length);
	}

	public ArrayList<Node> Search(Node start, Node end, long type,
			ArrayList<int[]> plant) {
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closeList = new ArrayList<Node>();

		openList.add(start);// ����ʼ�ڵ���뿪���б�(��ʼ�ڵ��F��Gֵ����Ϊ0);

		Node now = null;

		F: while (true) {
			// i. �ڿ����б��в���������СFֵ�Ľڵ�,���Ѳ��ҵ��Ľڵ���Ϊ��ǰ�ڵ�;
			if (openList.size() != 0) {
				Node temp = null;

				for (Node n : openList) {
					if (temp == null)
						temp = n;
					else {
						if ((n.F < temp.F && type == _short)
								|| (n.F > temp.F && type == _long))
							temp = n;
					}
				}

				now = temp;
			}
			// ii. �ѵ�ǰ�ڵ�ӿ����б�ɾ��, ���뵽����б�;
			openList.remove(now);
			closeList.add(now);

			ArrayList<Node> temp = new ArrayList<Node>(4);
			temp.add(get(now, 1, 0));
			temp.add(get(now, -1, 0));
			temp.add(get(now, 0, 1));
			temp.add(get(now, 0, -1));

			for (Node n : temp) {
				// ��������ڽڵ㲻��ͨ�л��߸����ڽڵ��Ѿ��ڷ���б���,��ʲô����Ҳ��ִ��,����������һ���ڵ�;
				if (n.x < 0 || n.y < 0 ||n.x >= plant.size() || n.y >= plant.get(0).length|| plant.get(n.x)[n.y] == MySnake._SNAKE_BODY || (plant.get(n.x)[n.y] == MySnake._SNAKE_TAIL && type != _long)|| closeList.contains(n))
					continue; 

				// ��������ڽڵ㲻�ڿ����б���,�򽫸ýڵ���ӵ������б���,
				// ���������ڽڵ�ĸ��ڵ���Ϊ��ǰ�ڵ�,ͬʱ��������ڽڵ��G��Fֵ;
				if (!openList.contains(n)) {
					n.G = now.G + 10;
					n.H = Math.abs(end.x - n.x) + Math.abs(end.y - n.y);
					n.F = n.H + n.G;
					n.father = now;
					openList.add(n);

					// ���յ�ڵ㱻���뵽�����б���Ϊ������ڵ�ʱ, ��ʾ·�����ҵ�,��ʱӦ��ֹѭ��;
					if (n.equals(end)) {
						break F;
					}
				}

				// ��������ڽڵ��ڿ����б���,
				// ���ж������ɵ�ǰ�ڵ㵽������ڽڵ��Gֵ�Ƿ���ڻ�С��ԭ�������Gֵ,�����ڻ�С��,�򽫸����ڽڵ�ĸ��ڵ���Ϊ��ǰ�ڵ�,���������ø����ڽڵ��G��Fֵ.
				if (openList.contains(n)) {
					if ((n.G > (now.G + 10) && type == _short)
							|| (n.G < (now.G + 10) && type == _long)) {
						n.father = now;
						n.G = now.G + 10;
						n.F = n.G + n.H;
					}
				}
			}

			// �������б�Ϊ��,�������޿�����ӵ��½ڵ�,���Ѽ���Ľڵ���û���յ�ڵ�����ζ��·���޷����ҵ�,��ʱҲ����ѭ��;
			if (openList.size() == 0) {
				break;
			}
		}

		ArrayList<Node> nodes = new ArrayList<Node>();

		if (openList.size() == 0)
			return null;

		if (openList.get(openList.size() - 1).equals(end)) {
			end = openList.get(openList.size() - 1);
		}

		nodes.add(end);
		while (end.father.father != null) {
			end = end.father;
			nodes.add(end);
		}

		return nodes;
	}

	// ������Ӧtype��NODE
	public Node find(int type) {
		return find(type,plant);
	}
	
	public Node find(int type,ArrayList<int[]> plant) {
		Node node = new Node();

		for (int x = 0; x < plant.size(); x++) {
			for (int y = 0; y < plant.get(0).length; y++) {
				if (type == plant.get(x)[y]) {
					node.x = x;
					node.y = y;
					return node;
				}
			}
		}
		return null;
	}

	public Node get(Node n, int i, int j) {
		Node node = new Node();
		node.x = n.x + i;
		node.y = n.y + j;

		return node;
	}

	// A*�㷨�Ľڵ�
	class Node {
		int F = 0;
		int G = 0;
		int H = 0;
		Node father;
		int x, y;

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Node) {
				Node n = (Node) obj;
				return (n.x == this.x && n.y == this.y);
			}
			return false;
		}

		public String toString() {
			return this.x + "   " + this.y;
		}

	}
}