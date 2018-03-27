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

		// 通过A*算法搜寻最短到达食物的距离
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

		return null; // 保持原状
	}

	/*
	 * 如果可以吃到蛋 派一个虚拟的蛇 如果吃完蛋以后还可以有路径走到尾巴那 吃蛋去 如果不能吃到蛋 绕尾巴转圈------等到可以吃到蛋的时候就去吃蛋
	 * 如果不能吃到蛋还不能绕尾巴转圈 就去乱走散散心
	 * 
	 * 如此循环
	 */
	ArrayList<Node> long_path = null;
	int index = 0;
	private Node search() {
		nodes = Search(find(MySnake._SNAKE_HEAD), find(MySnake._Food), _short,plant);// 找到蛇头到蛋的最短距离
		
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
		
		//I、模拟出蛇吃完蛋后的棋盘状态
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

	    //II、若此时的snake仍有路径可以到达尾巴就去吃
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

		openList.add(start);// 将开始节点放入开放列表(开始节点的F和G值都视为0);

		Node now = null;

		F: while (true) {
			// i. 在开放列表中查找最大或最小F值的节点,并把查找到的节点作为当前节点;
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
			// ii. 把当前节点从开放列表删除, 加入到封闭列表;
			openList.remove(now);
			closeList.add(now);

			ArrayList<Node> temp = new ArrayList<Node>(4);
			temp.add(get(now, 1, 0));
			temp.add(get(now, -1, 0));
			temp.add(get(now, 0, 1));
			temp.add(get(now, 0, -1));

			for (Node n : temp) {
				// 如果该相邻节点不可通行或者该相邻节点已经在封闭列表中,则什么操作也不执行,继续检验下一个节点;
				if (n.x < 0 || n.y < 0 ||n.x >= plant.size() || n.y >= plant.get(0).length|| plant.get(n.x)[n.y] == MySnake._SNAKE_BODY || (plant.get(n.x)[n.y] == MySnake._SNAKE_TAIL && type != _long)|| closeList.contains(n))
					continue; 

				// 如果该相邻节点不在开放列表中,则将该节点添加到开放列表中,
				// 并将该相邻节点的父节点设为当前节点,同时保存该相邻节点的G和F值;
				if (!openList.contains(n)) {
					n.G = now.G + 10;
					n.H = Math.abs(end.x - n.x) + Math.abs(end.y - n.y);
					n.F = n.H + n.G;
					n.father = now;
					openList.add(n);

					// 当终点节点被加入到开放列表作为待检验节点时, 表示路径被找到,此时应终止循环;
					if (n.equals(end)) {
						break F;
					}
				}

				// 如果该相邻节点在开放列表中,
				// 则判断若经由当前节点到达该相邻节点的G值是否大于或小于原来保存的G值,若大于或小于,则将该相邻节点的父节点设为当前节点,并重新设置该相邻节点的G和F值.
				if (openList.contains(n)) {
					if ((n.G > (now.G + 10) && type == _short)
							|| (n.G < (now.G + 10) && type == _long)) {
						n.father = now;
						n.G = now.G + 10;
						n.F = n.G + n.H;
					}
				}
			}

			// 当开放列表为空,表明已无可以添加的新节点,而已检验的节点中没有终点节点则意味着路径无法被找到,此时也结束循环;
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

	// 返还相应type的NODE
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

	// A*算法的节点
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