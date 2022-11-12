package DnfHelper;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import util.NumUtil;

public class RobotUtil {

	private static Robot robot;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
		}
	}

	public static Robot getRobot() {
		return robot;
	}
	
	/**
	 * @since mouseMove �ƶ��������x��y����
	 * @param x ����ƶ�x����
	 * @param y ����ƶ�y����
	 */
	public static void mouseMove(int x, int y) {
		robot.mouseMove(x, y);
		robot.delay(NumUtil.getRandomNum(500, 200));
    }

	/**
	 * mouseLeft �������
	 */
	public static void mouseLeft() {
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(NumUtil.getRandomNum(500, 200));
	}

	/**
	 * mouseShiftLeft ����shift���������
	 */
	public static void mouseShiftLeft() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.delay(NumUtil.getRandomNum(500, 200));
	}

	/**
	 * mouseLeft ˫�����
	 */
	public static void mouseLeftD() {
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(NumUtil.getRandomNum(500, 200));
	}

	/**
	 * mouseRight �����Ҽ�
	 */
	public static void mouseRight() {
		robot.mousePress(KeyEvent.BUTTON3_MASK);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON3_MASK);
		robot.delay(NumUtil.getRandomNum(500, 200));
	}

	/**
	 * preceKey ����key
	 * 
	 * @param key ��ֵ
	 */
	public static void preceKey(int key) {
		robot.keyPress(key);
		robot.delay(NumUtil.getRandomNum(150, 50));
		robot.keyRelease(key);
		robot.delay(NumUtil.getRandomNum(500, 200));
	}

	/**
	 * preceKey ��timeʱ����key��
	 * 
	 * @param key ��ֵ
	 */
	public static void preceKey(int key, int time) {
		robot.keyPress(key);
		robot.delay(time);
		robot.keyRelease(key);
		robot.delay(NumUtil.getRandomNum(500, 200));
	}
	
	public static BufferedImage createScreenCapture(Rectangle screenRect) {
		return robot.createScreenCapture(screenRect);
	}
	
	public static void delay(int ms){
		robot.delay(ms);
	}
	
	/**
	 * @description ���ܼ���ת�ͷ�
	 * */
	static void releaseSkill(String key) {
		switch(key){
			case "A" : 
				preceKey(KeyEvent.VK_A);
				break;
			case "Q" : 
				preceKey(KeyEvent.VK_Q);
				break;
			case "S" : 
				preceKey(KeyEvent.VK_S);
				break;
			case "W" : 
				preceKey(KeyEvent.VK_W);
				break;
			case "D" : 
				preceKey(KeyEvent.VK_D);
				break;
			case "E" : 
				preceKey(KeyEvent.VK_E);
				break;
			case "F" : 
				preceKey(KeyEvent.VK_F);
				break;
			case "R" : 
				preceKey(KeyEvent.VK_R);
				break;
			case "G" : 
				preceKey(KeyEvent.VK_G);
				break;
			case "T" : 
				preceKey(KeyEvent.VK_T);
				break;
			default :
				preceKey(KeyEvent.VK_X);
		}
	}
}
