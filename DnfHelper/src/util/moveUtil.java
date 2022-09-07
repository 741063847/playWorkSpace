package util;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import DnfHelper.KeyBoardUtil;

public class moveUtil{
	public static Dimension screenSize = new Dimension(1200, 800);
	public static Rectangle screenRectangle = new Rectangle(screenSize);
	// 第一个房间地图图标
	public static BufferedImage oneRoomImg = null;
	public static int[] oneRoom;
	// 第二个房间地图图标
	public static BufferedImage twoRoomImg = null;
	public static int[] twoRoom;
	// 第三个房间地图图标
	public static BufferedImage threeRoomImg = null;//178,207 金色音符
	public static int[] threeRoom;
	// 第四个房间地图图标
	public static BufferedImage fourRoomImg = null;
	public static int[] fourRoom;
	// 问号1图标
	public static BufferedImage wen1Img = null;
	public static int[] wen1;
	// 问号2图标
	public static BufferedImage wen2Img = null;
	public static int[] wen2;
	
	static {
		oneRoom = getImgAndData("oneRoom");
		twoRoom = getImgAndData("twoRoom");
		threeRoom = getImgAndData("threeRoom");
		fourRoom = getImgAndData("fourRoom");
		wen1 = getImgAndData("wen1");
		wen2 = getImgAndData("wen2");
	}
	
	public static int[] getImgAndData(String path) {
		File file = new File("D:/dnfImg/"+path+".png");
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if("oneRoom".equals(path)) {
			oneRoomImg = bi;
		}else if("twoRoom".equals(path)) {
			twoRoomImg = bi;
		}else if("threeRoom".equals(path)) {
			threeRoomImg = bi;
		}else if("fourRoom".equals(path)) {
			fourRoomImg = bi;
		}else if("wen1".equals(path)) {
			wen1Img = bi;
		}else if("wen2".equals(path)) {
			wen2Img = bi;
		}
		return getImageGRB(bi);
	}
	
	public static int[] getImageGRB(BufferedImage bfImage) {
		int width = bfImage.getWidth();
		int height = bfImage.getHeight();
		int[] result = new int[height * width];
		int n = 0;
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				//使用getRGB(w, h)获取该点的颜色值是ARGB，而在实际应用中使用的是RGB，所以需要将ARGB转化成RGB，即bufImg.getRGB(w, h) & 0xFFFFFF。
				result[n] = bfImage.getRGB(w, h) & 0xFFFFFF;
				n++;
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		int i = 0;
		int[] one = null;
		int[] two = null;
		int[] three = null;
		int[] four = null;
		
		BufferedImage imge = KeyBoardUtil.getRobot().createScreenCapture(screenRectangle);
		one = getImageGRB(imge.getSubimage(760, 85, oneRoomImg.getWidth(), oneRoomImg.getHeight()));
		two = getImageGRB(imge.getSubimage(760, 66, twoRoomImg.getWidth(), twoRoomImg.getHeight()));
		three = getImageGRB(imge.getSubimage(790, 82, threeRoomImg.getWidth(), threeRoomImg.getHeight()));
		four = getImageGRB(imge.getSubimage(778, 67, fourRoomImg.getWidth(), fourRoomImg.getHeight()));
		if(Arrays.equals(oneRoom, one)) {
			i=1;
		}
		if(Arrays.equals(twoRoom, two)) {
			i=2;
		}
		if(Arrays.equals(threeRoom, three)) {
			i=3;
		}
		if(Arrays.equals(fourRoom, four)) {
			i=4;
		}
		System.out.println("i="+i);
//		KeyBoardUtil.getRobot().delay(2000);
//		run();
	}

	public static void run() {
		while(true) {//738,28 //58,91
			KeyBoardUtil.preceKey(KeyEvent.VK_Q);
			int room = 1;
			int[] w1 = null;
			int[] w2 = null;
			int[] one = null;
			int[] two = null;
			int[] three = null;
			int[] four = null;
			BufferedImage imge = KeyBoardUtil.getRobot().createScreenCapture(screenRectangle);
			for (int x = 738; x < 58; x++) {
				for (int y = 28; y < 91; y++) {
					one = getImageGRB(imge.getSubimage(x, y, oneRoomImg.getWidth(), oneRoomImg.getHeight()));
					two = getImageGRB(imge.getSubimage(x, y, twoRoomImg.getWidth(), twoRoomImg.getHeight()));
					three = getImageGRB(imge.getSubimage(x, y, threeRoomImg.getWidth(), threeRoomImg.getHeight()));
					four = getImageGRB(imge.getSubimage(x, y, fourRoomImg.getWidth(), fourRoomImg.getHeight()));
				}
			}
			if(one != null) {
				while(true) {
					if(room == 1) {
						KeyBoardUtil.preceKey(KeyEvent.VK_D);
						KeyBoardUtil.getRobot().delay(500);
						KeyBoardUtil.preceKey(KeyEvent.VK_E);
					}
					if(room == 2) {
						KeyBoardUtil.preceKey(KeyEvent.VK_W);
						KeyBoardUtil.preceKey(KeyEvent.VK_S, 1000);
						KeyBoardUtil.getRobot().delay(500);
						KeyBoardUtil.preceKey(KeyEvent.VK_F);
					}
					if(room == 3) {
						KeyBoardUtil.preceKey(KeyEvent.VK_D);
						KeyBoardUtil.getRobot().delay(500);
						KeyBoardUtil.preceKey(KeyEvent.VK_A);
						KeyBoardUtil.getRobot().delay(500);
						KeyBoardUtil.preceKey(KeyEvent.VK_E);
					}
					for (int x = 738; x < 58; x++) {
						for (int y = 28; y < 91; y++) {
							w1 = getImageGRB(imge.getSubimage(x, y, wen1Img.getWidth(), wen1Img.getHeight()));
							w2 = getImageGRB(imge.getSubimage(x, y, wen2Img.getWidth(), wen2Img.getHeight()));
						}
					}
					if(room == 1 && (w1 != null || w2 != null)) {
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8,2000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,5000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD5,1000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,1500);
						room = 2;
						break;
					}
					if(room == 2 && (w1 != null || w2 != null)) {
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD5,2000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,7000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8,1000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4);
						room = 3;
						break;
					}
					if(room == 3 && (w1 != null || w2 != null)) {
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,5000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8,2000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4,500);
						room = 1;
						break;
					}
					KeyBoardUtil.preceKey(KeyEvent.VK_X);
					KeyBoardUtil.getRobot().delay(200);
					KeyBoardUtil.preceKey(KeyEvent.VK_X);
				}
				w1 = null;  
				w2 = null;
				continue;
			}
			if(two != null) {
				while(true) {
					KeyBoardUtil.preceKey(KeyEvent.VK_D);
					KeyBoardUtil.getRobot().delay(500);
					KeyBoardUtil.preceKey(KeyEvent.VK_E);
					for (int x = 738; x < 58; x++) {
						for (int y = 28; y < 91; y++) {
							w1 = getImageGRB(imge.getSubimage(x, y, wen1Img.getWidth(), wen1Img.getHeight()));
							w2 = getImageGRB(imge.getSubimage(x, y, wen2Img.getWidth(), wen2Img.getHeight()));
						}
					}
					if(w1 != null || w2 != null) {
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8,2000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD5,1000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,3000);
						break;
					}
				}
				w1 = null;  
				w2 = null;
				continue;
			}
			if(three != null) {
				while(true) {
					KeyBoardUtil.preceKey(KeyEvent.VK_D);
					KeyBoardUtil.getRobot().delay(500);
					KeyBoardUtil.preceKey(KeyEvent.VK_W);
					KeyBoardUtil.getRobot().delay(500);
					KeyBoardUtil.preceKey(KeyEvent.VK_A);
					for (int x = 738; x < 58; x++) {
						for (int y = 28; y < 91; y++) {
							w1 = getImageGRB(imge.getSubimage(x, y, wen1Img.getWidth(), wen1Img.getHeight()));
							w2 = getImageGRB(imge.getSubimage(x, y, wen2Img.getWidth(), wen2Img.getHeight()));
						}
					}
					if(w1 != null || w2 != null) {
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,5000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD5,1000);
						KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4,1000);
						break;
					}
				}
				w1 = null;  
				w2 = null;
				continue;
			}
			if(four != null) {
				KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD5,800);
				KeyBoardUtil.preceKey(KeyEvent.VK_R);
				KeyBoardUtil.getRobot().delay(5000);
				continue;
			}
			KeyBoardUtil.getRobot().mouseMove(178, 207);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.getRobot().delay(200);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			KeyBoardUtil.getRobot().delay(200);
			KeyBoardUtil.preceKey(KeyEvent.VK_F10);
			KeyBoardUtil.getRobot().delay(5000);
		}
	}
}
