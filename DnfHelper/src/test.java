import java.io.File;

import DnfHelper.KeyBoardUtil;

public class test {

	public static void main(String[] args) {
//		KeyBoardUtil.getRobot().delay(3000);
//		KeyBoardUtil.getRobot().mouseMove(240, 380);
//		KeyBoardUtil.mouseLeft();
//		KeyBoardUtil.getRobot().delay(1000);
//		KeyBoardUtil.getRobot().mouseMove(435, 396);
//		KeyBoardUtil.mouseLeft();
//		KeyBoardUtil.getRobot().mouseMove(470, 360);
//		KeyBoardUtil.mouseLeft();
		System.out.println(System.getProperty("user.dir"));
		File file = new File(System.getProperty("user.dir"));
		if(!file.exists()) {
			file.mkdirs();
		}
		File list[] = file.listFiles();
		for(File ff:list) {
			System.out.println(ff.getName());
		}
	}
	
	class MyRunnable implements Runnable {
		
		MyRunnable(){
		}
		
	    @Override
	    public void run() {
	    	int i=0;
	    	while(true) {
	    		i++;
	    		if(i>1000) {
	    			i=0;
	    		}
	    	}
	    }
	}

}
