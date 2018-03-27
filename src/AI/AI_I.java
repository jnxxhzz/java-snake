package AI;

import java.util.ArrayList;

import UI.ForWard;
import UI.MySnake;

/**
 * 单纯的A*算法的AI
 */
public class AI_I implements Snake_AI{
	
	ArrayList<int[]> plant = null;

	@Override
	public ForWard play(ArrayList<int[]> plant) {
		this.plant = plant; 
		
		//通过A*算法搜寻最短到达食物的距离
		Node n = search();
		Node start = find(MySnake._SNAKE_HEAD);
		
		if(n.x == start.x + 1)
			return ForWard.R;
		
		if(n.x == start.x - 1)
			return ForWard.L;
		
		if(n.y == start.y + 1)
			return ForWard.D;
		
		if(n.y == start.y - 1)
			return ForWard.U;
			
		return null;
	}
	
	
	private Node search() {
		ArrayList<Node> openList = new ArrayList<Node>();
		ArrayList<Node> closeList = new ArrayList<Node>();
		
		Node start = find(MySnake._SNAKE_HEAD);
		Node end = find(MySnake._Food);
		
		openList.add(start);//将开始节点放入开放列表(开始节点的F和G值都视为0);
		
		Node now = null;

		F:while(true){
			//i. 在开放列表中查找具有最小F值的节点,并把查找到的节点作为当前节点;
			if (openList.size() != 0){
				Node temp = null;
				
				for(Node n: openList){
					if (temp == null)
						temp = n;
					else
						if (n.F < temp.F)
							temp = n;
				}
				
				now = temp;
			}			
			//ii. 把当前节点从开放列表删除, 加入到封闭列表;	
			openList.remove(now);
			closeList.add(now);
			
			ArrayList<Node> temp = new ArrayList<Node>(4);
			temp.add(get(now, 1, 0));
			temp.add(get(now, -1, 0));
			temp.add(get(now, 0, 1));
			temp.add(get(now, 0, -1));
		
			for(Node n : temp){
				//如果该相邻节点不可通行或者该相邻节点已经在封闭列表中,则什么操作也不执行,继续检验下一个节点;
				if (n.x < 0 || n.y < 0 ||n.x >= plant.size() || n.y >= plant.get(0).length|| plant.get(n.x)[n.y] == MySnake._SNAKE_BODY || plant.get(n.x)[n.y] == MySnake._SNAKE_TAIL || closeList.contains(n))
					continue; 
				
				//如果该相邻节点不在开放列表中,则将该节点添加到开放列表中, 并将该相邻节点的父节点设为当前节点,同时保存该相邻节点的G和F值;
				if (!openList.contains(n)){
					n.G = now.G + 10;
					n.H = Math.abs(end.x - n.x) + Math.abs(end.y - n.y);
					n.F = n.H + n.G;
					n.father = now;
					openList.add(n);

					//当终点节点被加入到开放列表作为待检验节点时, 表示路径被找到,此时应终止循环;
					if(n.equals(end)){
						break F;
					}
				}
				
				//如果该相邻节点在开放列表中, 则判断若经由当前节点到达该相邻节点的G值是否小于原来保存的G值,若小于,则将该相邻节点的父节点设为当前节点,并重新设置该相邻节点的G和F值.
				if (openList.contains(n)){
					if (n.G > (now.G + 10)){
						n.father = now;
						n.G = now.G + 10;
						n.F = n.G + n.H;
					}		
				}
			}
			
			//当开放列表为空,表明已无可以添加的新节点,而已检验的节点中没有终点节点则意味着路径无法被找到,此时也结束循环;
			if (openList.size() == 0){
				break;
			}
		}
		
		//TODO
		if (openList.get(openList.size() - 1).equals(end)){
			end = openList.get(openList.size() - 1);
		}
		
		while(end.father.father != null)
			end = end.father;
		
		
		return end;
	}

	
	//返还相应type的NODE
	public Node find(int type){
		Node node = new Node();
		
		for (int x=0;x<plant.size();x++){
			for (int y=0;y<plant.get(0).length;y++){
				if (type == plant.get(x)[y]){
					node.x = x;
					node.y = y;
					return node;
				}
			}
		}
		return null;
	}
	
	public Node get(Node n,int i,int j){
		Node node = new Node();
		node.x = n.x + i;
		node.y = n.y + j;
		
		return node;
	}

	// A*算法的节点
	class Node{
		int F = 0;
		int G = 0;
 		int H = 0;
		Node father;
		int x,y;
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Node){
				Node n = (Node) obj;
				return (n.x == this.x && n.y == this.y);
			}
			return false;
		}
		
		public String toString(){
			return this.x + "   " + this.y; 
		}
		
	}
}
