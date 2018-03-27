package AI;

import java.util.ArrayList;

import UI.ForWard;
import UI.MySnake;

/**
 * ������A*�㷨��AI
 */
public class AI_I implements Snake_AI{
	
	ArrayList<int[]> plant = null;

	@Override
	public ForWard play(ArrayList<int[]> plant) {
		this.plant = plant; 
		
		//ͨ��A*�㷨��Ѱ��̵���ʳ��ľ���
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
		
		openList.add(start);//����ʼ�ڵ���뿪���б�(��ʼ�ڵ��F��Gֵ����Ϊ0);
		
		Node now = null;

		F:while(true){
			//i. �ڿ����б��в��Ҿ�����СFֵ�Ľڵ�,���Ѳ��ҵ��Ľڵ���Ϊ��ǰ�ڵ�;
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
			//ii. �ѵ�ǰ�ڵ�ӿ����б�ɾ��, ���뵽����б�;	
			openList.remove(now);
			closeList.add(now);
			
			ArrayList<Node> temp = new ArrayList<Node>(4);
			temp.add(get(now, 1, 0));
			temp.add(get(now, -1, 0));
			temp.add(get(now, 0, 1));
			temp.add(get(now, 0, -1));
		
			for(Node n : temp){
				//��������ڽڵ㲻��ͨ�л��߸����ڽڵ��Ѿ��ڷ���б���,��ʲô����Ҳ��ִ��,����������һ���ڵ�;
				if (n.x < 0 || n.y < 0 ||n.x >= plant.size() || n.y >= plant.get(0).length|| plant.get(n.x)[n.y] == MySnake._SNAKE_BODY || plant.get(n.x)[n.y] == MySnake._SNAKE_TAIL || closeList.contains(n))
					continue; 
				
				//��������ڽڵ㲻�ڿ����б���,�򽫸ýڵ���ӵ������б���, ���������ڽڵ�ĸ��ڵ���Ϊ��ǰ�ڵ�,ͬʱ��������ڽڵ��G��Fֵ;
				if (!openList.contains(n)){
					n.G = now.G + 10;
					n.H = Math.abs(end.x - n.x) + Math.abs(end.y - n.y);
					n.F = n.H + n.G;
					n.father = now;
					openList.add(n);

					//���յ�ڵ㱻���뵽�����б���Ϊ������ڵ�ʱ, ��ʾ·�����ҵ�,��ʱӦ��ֹѭ��;
					if(n.equals(end)){
						break F;
					}
				}
				
				//��������ڽڵ��ڿ����б���, ���ж������ɵ�ǰ�ڵ㵽������ڽڵ��Gֵ�Ƿ�С��ԭ�������Gֵ,��С��,�򽫸����ڽڵ�ĸ��ڵ���Ϊ��ǰ�ڵ�,���������ø����ڽڵ��G��Fֵ.
				if (openList.contains(n)){
					if (n.G > (now.G + 10)){
						n.father = now;
						n.G = now.G + 10;
						n.F = n.G + n.H;
					}		
				}
			}
			
			//�������б�Ϊ��,�������޿�����ӵ��½ڵ�,���Ѽ���Ľڵ���û���յ�ڵ�����ζ��·���޷����ҵ�,��ʱҲ����ѭ��;
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

	
	//������Ӧtype��NODE
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

	// A*�㷨�Ľڵ�
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
