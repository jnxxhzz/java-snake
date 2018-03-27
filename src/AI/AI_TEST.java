package AI;

import java.util.ArrayList;
import java.util.Random;
import UI.ForWard;

/**
 *随机产生方向的测试AI 
 */
public class AI_TEST implements Snake_AI{
	
	
	@Override
	public ForWard play(ArrayList<int[]> plant) {
		Random ran = new Random();
		int r = ran.nextInt(3);
		return (r == 0 ? ForWard.D : r == 1 ? ForWard.L : r == 2 ? ForWard.R : ForWard.U);
	}


}
