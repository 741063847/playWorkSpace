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
	 * @since mouseMove �ƶ��������x��y����
	 * @param x ����ƶ�x����
	 * @param y ����ƶ�y����
	 */
	public static void mouseMove(int x, int y) {
		robot.mouseMove(x, y);
		robot.delay(getRandomNum(500, 200));
    }

	/**
	 * mouseLeft �������
	 */
	public static void mouseLeft() {
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * mouseShiftLeft ����shift���������
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
	 * mouseLeft ˫�����
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
	 * mouseRight �����Ҽ�
	 */
	public static void mouseRight() {
		robot.mousePress(KeyEvent.BUTTON3_MASK);
		robot.delay(getRandomNum(150, 50));
		robot.mouseRelease(KeyEvent.BUTTON3_MASK);
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * preceKey ����key
	 * 
	 * @param key ��ֵ
	 */
	public static void preceKey(int key) {
		robot.keyPress(key);
		robot.delay(getRandomNum(150, 50));
		robot.keyRelease(key);
		robot.delay(getRandomNum(500, 200));
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
		robot.delay(getRandomNum(500, 200));
	}

	/**
	 * getRandomNum ����min~max֮ǰ���������
	 * 
	 * @param min ���������
	 * @param max ���������
	 */
	public static int getRandomNum(int max, int min) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	/**
	 * @since compareImge �Ƚ�����BufferedImageͼƬ�Ƿ���ȫһ��
	 * @param image1 �Ƚ�ͼƬ1
	 * @param image2 �Ƚ�ͼƬ2
	 * @return ���رȽϵĲ������
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
	 * @since priteScreen ���ݸ�������ʼ��ͽ�����߽���
	 * @param x ��ʼ�������
	 * @param y ��ʼ��������
	 * @param width �������
	 * @param height �����߶�
	 * @return ���ؽ�����BufferedImageͼƬ
	 */
	public static BufferedImage priteScreen(int x, int y, int width, int height) {
		if (width <= 0) {
			width = Toolkit.getDefaultToolkit().getScreenSize().width;
		}
		if (height <= 0) {
			height = Toolkit.getDefaultToolkit().getScreenSize().height;
		}
		Rectangle rectangle = new Rectangle(x, y, width, height);
		return robot.createScreenCapture(rectangle);// �ӵ�ǰ��Ļ�ж�ȡ������ͼ�񣬸�ͼ�񲻰��������
	}
	
	/**
	 * @since locateOnScreen ��ȡĿ��ͼƬ�ڱ���ͼƬ�еĵ�һ��λ��
	 * @param targetImage Ŀ��ͼƬ
	 * @param bgImage ����ͼƬ
	 * @return Ŀ��ͼƬ�ڱ���ͼƬ�еĵ�һ��λ�ã������򷵻�[-1,-1]
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
