package agent;

import world.World;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*if(args[0].equals("m"))
		else*/
		
		World world = new World();
		Agent agent	= new Agent();
		
		while(true){
			
			try {
				Thread.sleep(20); //50 "fps"
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			
			agent.update();
		}
	}

}
