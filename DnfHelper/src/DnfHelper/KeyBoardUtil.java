package DnfHelper;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class KeyBoardUtil {

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
	 * @since mouseMove 移动鼠标至（x，y）处
	 * @param x 鼠标移动x坐标
	 * @param y 鼠标移动y坐标
	 */
	public static void mouseMove(int x, int y) {
		robot.mouseMove(x, y);
		robot.delay(getRandomNum(500, 200));
    }

	/**
	 * mouseLeft 单击左键
	 */
	public static void mouseLeft() {
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * mouseShiftLeft 长按shift键单击左键
	 */
	public static void mouseShiftLeft() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.delay(getRandomNum(150, 50));
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * mouseLeft 双击左键
	 */
	public static void mouseLeftD() {
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * mouseRight 单击右键
	 */
	public static void mouseRight() {
		robot.mousePress(KeyEvent.BUTTON3_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON3_MASK);
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * preceKey 按键key
	 * 
	 * @param key 键值
	 */
	public static void preceKey(int key) {
		robot.keyPress(key);
		robot.delay(getRandomNum(150, 50));
		robot.keyRelease(key);
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * preceKey 按time时长的key键
	 * 
	 * @param key 键值
	 */
	public static void preceKey(int key, int time) {
		robot.keyPress(key);
		robot.delay(time);
		robot.keyRelease(key);
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * getRandomNum 生成min~max之前的随机整数
	 * 
	 * @param min 随机数下限
	 * @param max 随机数上限
	 */
	public static int getRandomNum(int max, int min) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	/**
	 * @since compareImge 比较两个BufferedImage图片是否完全一样
	 * @param image1 比较图片1
	 * @param image2 比较图片2
	 * @return 返回比较的布尔结果
	 * */
	public static boolean compareImge(BufferedImage image1, BufferedImage image2) {
		for (int h = 0; h < image1.getHeight(); h++) {
			for (int w = 0; w < image1.getWidth(); w++) {
				if (image1.getRGB(w, h) != image2.getRGB(w, h)) {
					return false;
				}
			}
		}
		 return true;
	}
	
	/**
	 * @since priteScreen 根据给定的起始点和截屏宽高截屏
	 * @param x 起始点横坐标
	 * @param y 起始点纵坐标
	 * @param width 截屏宽度
	 * @param height 截屏高度
	 * @return 返回截屏的BufferedImage图片
	 */
	public static BufferedImage priteScreen(int x, int y, int width, int height) {
		if (width <= 0) {
			width = Toolkit.getDefaultToolkit().getScreenSize().width;
		}
		if (height <= 0) {
			height = Toolkit.getDefaultToolkit().getScreenSize().height;
		}
		Rectangle rectangle = new Rectangle(x, y, width, height);
		return robot.createScreenCapture(rectangle);// 从当前屏幕中读取的像素图像，该图像不包括鼠标光标
	}
	
	/**
	 * @since locateOnScreen 获取目标图片在背景图片中的第一个位置
	 * @param targetImage 目标图片
	 * @param bgImage 背景图片
	 * @return 目标图片在背景图片中的第一个位置，若无则返回[-1,-1]
	 * */
	public static int[] locateOnScreen(BufferedImage targetImage, BufferedImage bgImage) {
		int width = bgImage.getWidth() - targetImage.getWidth();
		int height = bgImage.getHeight() - targetImage.getHeight();
		TxtLog.log("width="+width+"height="+height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				BufferedImage image = bgImage.getSubimage(x, y, targetImage.getWidth(), targetImage.getHeight());
				if (compareImge(targetImage, image)) {
					return new int[] { x, y };
				}
			}
		}
		return new int[] { -1, -1 };
	}
}
