package DnfHelper;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;

import util.ImageUtil;
import util.NumUtil;
import util.StringUtil;

public class DnfUtil {
	
	public static boolean isEnd = false;
	private static Map<String,String> numberMap = new HashMap<>();
	// һ��ͼƬ�ز�
	private static Map<String,BufferedImage> imgesMap = new HashMap<>();
	// ����ͼƬ�ز�
	private static Map<String,BufferedImage> numImgesMap = new HashMap<>();
	// ����Ʊλ��
	public static int[] hlPiao = { 0, 0 };
	// ȡ����ť��λ��
	public static int[] quchu = { 0, 0 };
	// �˺Ž���λ��
	public static int[] zhjinku = { 0, 0 };
	// ����ť��λ��
	public static int[] renwu = { 0, 0 };
	// �޾����ϵ�λ��
	public static int[] wujin = { 0, 0 };
	// �����������ŵ�λ��
	public static int[] xihaian = { 0, 0 };
	
	private static String[] keyCodes = {"Q", "W", "E", "R", "T", "A", "S", "D", "F", "G"};
	
	// �̳߳�
	private static ExecutorService executor = Executors.newFixedThreadPool(10);
	
	// ��Ϣ�����߳�
	private static List<CompareSingleSkillImg> threads = new ArrayList<>();
	
	static {
		numberMap.put("0111110100000110000010111110","0");
		numberMap.put("11111110100000","1");
		numberMap.put("0110001100100110001010100011","2");
		numberMap.put("0110110100100110000010100010","3");
		numberMap.put("0000100111111101101000001100","4");
		numberMap.put("1001110101000110100011111010","5");
		numberMap.put("0100110100100110010010111110","6");
		numberMap.put("1100000101100010001111000000","7");
		numberMap.put("0110110100100110010010110110","8");
		numberMap.put("0111110100100110010010110010","9");
		resetLocation();
		loadImages();
	}
	
	public static void main(String[] args) { 
		try {
			RobotUtil.delay(2000);
			getCds();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String, Integer> getSsNum() {
		// 213,300   28*28   33*33
		Map<String, Integer> result = new HashMap<>();
		BufferedImage bgImage = ImageUtil.priteScreen(213, 300, 380, 155);
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 5; j++) {
				BufferedImage image = bgImage.getSubimage(i * 32 + 4, j * 32 + 4, 20, 20);
				String key = img2Str(image);
				if(result.containsKey(key)) {
					result.put(key, result.get(key) + 1);
				}else {
					result.put(key, 1);
				}
			}
		}
		return result;
	}
	
	private static String img2Str(BufferedImage img) {
		StringBuffer str = new StringBuffer();
		for(int x=0;x<img.getWidth();x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				str.append(img.getRGB(x, y));
			}
		}
		return StringUtil.MD5(str.toString());
	}
	
	public static void getNameAndRole() throws IOException {
		//  4  505-529         130
		int x = 0;
		for (int i = 0; i < 6; i++) {
			x = 5 + 130 * i;
			RobotUtil.delay(1000);
			BufferedImage img = ImageUtil.priteScreen(x, 505, 130, 24);
			File outImgFile = new File("D:/dnfImg/role" + i + ".png");
			ImageIO.write(img, "png", outImgFile);
		}
	}
	
	/**
	 * @since getSkillImgs ��ȡ����ͼ������
	 * @param xp ��������ʼ������
	 * @param yp ��������ʼ������
	 * @param width ��������
	 * @param height ������߶�
	 * @param len ͼ��߳�
	 * @return ����ͼ������
	 * @throws IOException 
	 * */
	public static BufferedImage[] getSkillImgs(int xp, int yp, int width, int height, int len) throws IOException {
		BufferedImage[] imgs = new BufferedImage[10];
		int n = 0;
		BufferedImage img = ImageUtil.priteScreen(xp, yp, width, height);
		// ��
		for (int i = 1; i >= 0; i--) {
			// ��
			for (int j = 0; j < 5; j++) {
				imgs[n++] = img.getSubimage(27 + j * (len - 1), 2 + i * (len - 1), 3, 6);
				// imgs[n++] = img.getSubimage(j * (len - 1), i * (len - 1), len, len);
				File outImgFile = new File("D:/dnfImg/" + i + "-" + j + ".png");
				ImageIO.write(imgs[n - 1], "png", outImgFile);
			}
		}
		return imgs;
	}
	
	/**
	 * @since getFightPoint ��ȡˢͼ��
	 * */
	public static void getFightPoint() {
		// ��ˢͼ�㹺�����
		RobotUtil.mouseMove(645, 595);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		// ����ÿ��ˢͼ��
		RobotUtil.mouseMove(180, 210);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		// �л�������Ʒ��Ʒ��
		RobotUtil.mouseMove(575, 270);
		RobotUtil.mouseLeft();
		RobotUtil.delay(2000);
		// ʹ��ˢͼ��ҩ
		BufferedImage bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		int [] yao = ImageUtil.locateOnScreen(imgesMap.get("shuatudian"), bgImage);
		RobotUtil.mouseMove(yao[0] + 20, yao[1]);
		RobotUtil.mouseRight();
		// ��������Ҫ��ͨ
		RobotUtil.mouseMove(350, 210);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		// ����ž�����ͨ
		RobotUtil.mouseMove(180, 265);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		// �رս���
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * @since publicBtnlocateInit ������ťλ�ó�ʼ��
	 * */
	public static void publicBtnlocateInit() {
		// �򿪸��˲ֿ�
		RobotUtil.mouseMove(250, 320);
		RobotUtil.mouseLeft();
		RobotUtil.delay(1000);
		RobotUtil.mouseMove(0, 0);
		// ȡ����ť��λ��
		BufferedImage bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		quchu = ImageUtil.locateOnScreen(imgesMap.get("quchu"), bgImage);
		// �˺Ž��λ��
		zhjinku = ImageUtil.locateOnScreen(imgesMap.get("zhjinku"), bgImage);
		RobotUtil.mouseMove(zhjinku[0] + 20, zhjinku[1] + 6);
		RobotUtil.mouseLeft();
		RobotUtil.delay(1000);
		// �޾�����λ��
		bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		wujin = ImageUtil.locateOnScreen(imgesMap.get("wujin"), bgImage);
		// �رղֿ�
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * @since roleBtnlocateInit ��ɫ��ťλ�ó�ʼ��
	 * */
	public  static void roleBtnlocateInit() {
		// �򿪸��˱���
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD1);
		RobotUtil.mouseMove(0, 0);
		// ����ť��λ��
		BufferedImage bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		renwu = ImageUtil.locateOnScreen(imgesMap.get("renwu1"), bgImage);
		if(renwu[0] == -1) {
			renwu = ImageUtil.locateOnScreen(imgesMap.get("renwu2"), bgImage);
		}
		// �������ť
		int x = renwu[0] + NumUtil.getRandomNum(imgesMap.get("renwu1").getWidth(), 2);
		int y = renwu[1] + NumUtil.getRandomNum(imgesMap.get("renwu1").getHeight(), 2);
		TxtLog.log("dianjirenwu x="+renwu[0]+"y="+renwu[1]);
		RobotUtil.mouseMove(x , y);
		RobotUtil.mouseLeft();
		RobotUtil.delay(1000);
		bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		hlPiao = ImageUtil.locateOnScreen(imgesMap.get("hlPiao1"), bgImage);
		if(hlPiao[0] == -1) {
			hlPiao = ImageUtil.locateOnScreen(imgesMap.get("hlPiao2"), bgImage);
		}
	}
	
	/**
	 * @since ������ԴͼƬ
	 * */
	public static void loadImages() {
		String commonImagePath = System.getProperty("user.dir") + "/imgs/common";
		imgesMap = ImageUtil.loadImages(commonImagePath);
		String numberImagePath = System.getProperty("user.dir") + "/imgs/number";
		numImgesMap = ImageUtil.loadImages(numberImagePath);
	}
	
	/**
	 * ��Ϸ���ڸ�λ
	 * */
	public static String resetLocation() {
		HWND hwnd = User32.INSTANCE.FindWindow(null, "���³�����ʿ");//����ʦ-������Ϸ
		if(hwnd == null) {
			hwnd = User32.INSTANCE.FindWindow(null, "Dungeon & Fighter");
		}
		if(hwnd == null) {
			hwnd = User32.INSTANCE.FindWindow(null, "Dungeon&Fighter");
		}
		if(hwnd == null) {
			RobotUtil.mouseMove(100, 100);
			RobotUtil.mouseLeft();
			hwnd = User32.INSTANCE.GetDesktopWindow();
		}
		if (hwnd == null) {
			TxtLog.log("���³�����ʿδ������");
			return "���³�����ʿδ������";
		}else{
			WinDef.RECT winRect = new  WinDef.RECT();
		    User32.INSTANCE.GetWindowRect(hwnd, winRect);
	 	    int winWidth = winRect.right-winRect.left;
		    int winHeight = winRect.bottom-winRect.top;
		    User32.INSTANCE.MoveWindow(hwnd, 0, 0, winWidth, winHeight, true);
		    // ѡ����Ϸ���ڣ�����������ǰ��
	    	RobotUtil.mouseMove(100, 100);
			RobotUtil.mouseLeft();
			RobotUtil.delay(1000);
		    return "";
		}
	}
	
	/**
	 * @desc �رղֿ�����ʾ
	 * */
	public static void colseLockTip() {
		RobotUtil.mouseMove(250, 320);
		RobotUtil.mouseLeft();
		RobotUtil.delay(1500);
		RobotUtil.mouseMove(435, 395);
		RobotUtil.mouseLeft();
		RobotUtil.mouseMove(475, 360);
		RobotUtil.mouseLeft();
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * �ķ����
	 * */
	public static void changeKey() {
		// �򿪲˵�
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
		// ��Ϸ����
		RobotUtil.mouseMove(254, 448);
		RobotUtil.mouseLeft();
		// ��ݼ�����
		RobotUtil.mouseMove(200, 450);
		RobotUtil.mouseLeft();
		// �鿴�Ƿ��Ѹļ�
		int n = 0;
		while(true) {
			// ��
			RobotUtil.mouseMove(315,355);//315,355
			RobotUtil.mouseLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD8);
			// ��
			RobotUtil.mouseMove(315,375);//315,375
			RobotUtil.mouseLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD4);
			// ��
			RobotUtil.mouseMove(315,395);//315,395
			RobotUtil.mouseLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD5);
			// ��
			RobotUtil.mouseMove(315,415);//315,415
			RobotUtil.mouseLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD6);
			// ����
			RobotUtil.mouseMove(570, 515);
			RobotUtil.mouseLeft();
			n++;
			if(n > 2) {
				break;
			}
			// ��ȡ�����λ����ɫ�������жϸļ��ɹ�
			if(!colorCompare(636, 197, 11, 13, 17)) {//150, 89, 199
				continue;
			}
			if(!colorCompare(609, 224, 67, 75, 90)) {
				continue;
			}
			if(!colorCompare(636, 197, 11, 13, 17)) {
				continue;
			}
			if(!colorCompare(663, 197, 11, 13, 17)) {
				continue;
			}
			break;
		}
		// �˳��˵�
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	public static boolean colorCompare(int xPixel, int yPixel, int red, int green, int blue) {
		Color color = RobotUtil.getRobot().getPixelColor(xPixel, xPixel);
		if(isBetween(color.getRed(), red) 
				&& isBetween(color.getGreen(), green)
				&& isBetween(color.getBlue(), blue)) {
			return true;
		}
		return false;
	}
	
	public static int[] getImageGRB(BufferedImage bfImage) {
		int width = bfImage.getWidth();
		int height = bfImage.getHeight();
		int[] result = new int[height * width];
		int n = 0;
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				//ʹ��getRGB(w, h)��ȡ�õ����ɫֵ��ARGB������ʵ��Ӧ����ʹ�õ���RGB��������Ҫ��ARGBת����RGB����bufImg.getRGB(w, h) & 0xFFFFFF��
				result[n] = bfImage.getRGB(w, h) & 0xFFFFFF;
				n++;
			}
		}
		return result;
	}
	
	
	public static int getPiaos() {
		// ��ȡ���˻���Ʊλ��
		roleBtnlocateInit();
		int num = getPiaos(hlPiao[0] + 23, hlPiao[1] - 8);//����Ʊ��6��ݼ�ʱ����183, 571
		// �رո��˱���
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
		return num;
	}
	
	/**
	 * getPiao ��ȡ��ɫ��ǰ��Ʊ��
	 * */
	public static int getPiaos(int x, int y) {
		// ��ѯ��ǰ��ɫ��Ʊ����,���������Ͻǵ㿪ʼ
		int startX = x;
		int startY = y;
		int n = 0;
		int result = 0;
		while(startX > (183 - 24)) {
			String str = "";
			for(int i=0;i < 4;i++) {
				for(int j=0;j<7;j++) {
					int red = RobotUtil.getRobot().
							getPixelColor(startX-i, startY+j).getRed();
					int blue = RobotUtil.getRobot().
							getPixelColor(startX-i, startY+j).getBlue();
					int green = RobotUtil.getRobot().
							getPixelColor(startX-i, startY+j).getGreen();
					if(red >= 255 && blue >= 255 && green >= 255) {
						str += "1";
					}else {
						str += "0";
					}
				}
			}
			String num;
			// ����Ϊ1
			if("11111110100000".equals(str.substring(0, 14))) {
				num = "1";
			}else {
				num = numberMap.get(str);
			}
			if(num != null) {
				result += Integer.valueOf(num)*Math.pow(10, n++);
				if("1".equals(num)) {
					startX -= 4;
				}else {
					startX -= 6;
				}
			}else {
				break;//startX -= 1;
			}
		}
		if(result == 0){
			return 1;
		}else {
			return result;
		}
	}

	/**
	 * @description ȡ����
	 * @param piaoNum ��ǰ��ɫƱ��
	 * */
	public static void getMaterial(int piaoNum) {
		// ��Ʊ���㣬ȡ�����޾�������
		if(piaoNum < 26) {
			// �����޾�����������
			int [] aa = {0,0,0};
			// Ʊ���㣬����26��Ʊ
			int yonNum = (100 - piaoNum) * 5;
			aa[2] = yonNum%10;
			aa[1] = (yonNum/10)%10;
			aa[0] = (yonNum/100)%10;
			// �򿪽��
			RobotUtil.mouseMove(250, 320);
			RobotUtil.mouseLeft();
			// ���˻����
			RobotUtil.delay(500);
			RobotUtil.mouseMove(zhjinku[0] + 20, zhjinku[1] + 6);
			RobotUtil.mouseLeft();
			// ����ȡ����ť
			int x = quchu[0] + NumUtil.getRandomNum(imgesMap.get("quchu").getWidth(), 2);
			int y = quchu[1] + NumUtil.getRandomNum(imgesMap.get("quchu").getHeight(), 2);
			RobotUtil.mouseMove(x , y);
			RobotUtil.mouseLeft();
			// ѡ���޾�����
			x = wujin[0] + NumUtil.getRandomNum(imgesMap.get("wujin").getWidth(), 2);
			y = wujin[1] + NumUtil.getRandomNum(imgesMap.get("wujin").getHeight(), 2);
			RobotUtil.mouseMove(x , y);
			RobotUtil.mouseLeft();
			// ����Ҫȡ�ò�������
			RobotUtil.preceKey(aa[0]+96);
			RobotUtil.preceKey(aa[1]+96);
			RobotUtil.preceKey(aa[2]+96);
			RobotUtil.preceKey(KeyEvent.VK_ENTER);
			// �رղ˵�
			RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
			RobotUtil.delay(1000);
		}
	}
	
	/**
	 * @description �������Ƿ��䵽������ͼ
	 * @param piaoNum Ʊ����
	 * @param buyTicket �Ƿ���Ʊ
	 * */
	public static boolean homeMoveToDragon(int piaoNum, boolean buyTicket) throws IOException {
		// ����ƶ�����Ļ����ֹ�ڵ�ѡ��
		RobotUtil.mouseMove(0 , 0);
		// �ƶ��������Ҳഫ����
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD6,3000);
		// �����������ŵ�λ��
		BufferedImage bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		xihaian = ImageUtil.locateOnScreen(imgesMap.get("xihaian"), bgImage);
		int n = 1;
		while(xihaian[0] == -1 || n > 6) {
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD5);
			bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
			xihaian = ImageUtil.locateOnScreen(imgesMap.get("xihaian"), bgImage);
			n++;
		}
		if(n > 6) {
			return false;
		}
		int x = xihaian[0] + NumUtil.getRandomNum(imgesMap.get("xihaian").getWidth(), 2);
		int y = xihaian[1] + NumUtil.getRandomNum(imgesMap.get("xihaian").getHeight(), 2);
		TxtLog.log("dianjixihaian x="+xihaian[0]+"y="+xihaian[1]);
		RobotUtil.mouseMove(x , y);
		RobotUtil.mouseLeft();
		RobotUtil.delay(2000);
		// �������ɿڵ�����ͼ
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD4,800);
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD8,1600);
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD6,1600);
		// �÷����ѡ�л���
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD8);
		RobotUtil.preceKey(KeyEvent.VK_SPACE);
		RobotUtil.delay(4000);
		// ����ͼ��������
		RobotUtil.preceKey(KeyEvent.VK_N);
		if(buyTicket && piaoNum < 26) {
			RobotUtil.delay(2000);
			// �򿪵�ͼ���ƶ�����С��������ֹ�����ɫ�ڵ�
			RobotUtil.mouseMove(393, 386);
			RobotUtil.mouseLeft();
			RobotUtil.delay(4000);
			RobotUtil.preceKey(KeyEvent.VK_N);
			// ������С���������NPC�Ϸ���һ�㣬��ֹ��ҽ�ɫ�ڵ�
			RobotUtil.mouseMove(465, 127);
			RobotUtil.mouseLeft();
			// ���̵�
			RobotUtil.mouseMove(515, 177);
			RobotUtil.mouseLeft();
			// �ƶ�������Ʊλ��
			RobotUtil.mouseMove(365, 170);
			RobotUtil.mouseShiftLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD3);
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD3);
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD3);
			// ����enterȷ����Ʊ
			RobotUtil.preceKey(KeyEvent.VK_ENTER);
			RobotUtil.preceKey(KeyEvent.VK_SPACE);
			RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
			// �򿪵�ͼ
			RobotUtil.preceKey(KeyEvent.VK_N);
		}
		// ������ͼ 
		RobotUtil.mouseMove(392, 277);
		RobotUtil.mouseLeft();
		RobotUtil.preceKey(KeyEvent.VK_N);
		RobotUtil.delay(10000);
		return true;
	}
	
	/**
	 * @description ���ܼ���ת�ͷ�
	 * */
	static void releaseSkill(String key) {
		switch(key){
			case "A" : 
				RobotUtil.preceKey(KeyEvent.VK_A);
				break;
			case "Q" : 
				RobotUtil.preceKey(KeyEvent.VK_Q);
				break;
			case "S" : 
				RobotUtil.preceKey(KeyEvent.VK_S);
				break;
			case "W" : 
				RobotUtil.preceKey(KeyEvent.VK_W);
				break;
			case "D" : 
				RobotUtil.preceKey(KeyEvent.VK_D);
				break;
			case "E" : 
				RobotUtil.preceKey(KeyEvent.VK_E);
				break;
			case "F" : 
				RobotUtil.preceKey(KeyEvent.VK_F);
				break;
			case "R" : 
				RobotUtil.preceKey(KeyEvent.VK_R);
				break;
			case "G" : 
				RobotUtil.preceKey(KeyEvent.VK_G);
				break;
			case "T" : 
				RobotUtil.preceKey(KeyEvent.VK_T);
				break;
			default :
				RobotUtil.preceKey(KeyEvent.VK_X);
		}
	}
	
	/**
	 * @since ��ȡ����ͼƬ
	 * @return ������Ͷ�ӦCD
	 * */
	private static Map<String, BufferedImage> getJnImg() throws IOException {
		Map<String, BufferedImage> result = new HashMap<>();
		int keyNum = 0;
		int x = 0;
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < 5; i++) {
				x = 313 + 31 * i;
				RobotUtil.mouseMove(x, 547 + j * 31);
				RobotUtil.delay(1000);
				BufferedImage img = ImageUtil.priteScreen(x - 125, 547 - 300 + j * 31, 250, 300);
				result.put(keyCodes[keyNum++], changeImgColor(img));
//				File outImgFile = new File("D:/dnfImg/in/" + j + "-"+ i + ".png");
//				ImageIO.write(img, "png", outImgFile);
			}
		}
		return result;
	}
	
	/**
	 * @since ����ͼƬ�ڰ׻�
	 * @throws IOException 
	 * */
	public static BufferedImage changeImgColor(BufferedImage image) {
		Color color1 = new Color(83, 205, 255);
		Color color2 = new Color(188, 50, 50);
		Color color3 = new Color(134, 120, 79);
		//Color color4 = new Color(209, 185, 148); ����
		for(int i=0;i<image.getWidth();i++) {
			for (int j=0;j<image.getHeight();j++) {
				if(image.getRGB(i, j) == color1.getRGB() 
						|| image.getRGB(i, j) == color2.getRGB()
						|| image.getRGB(i, j) == color3.getRGB()) {
					image.setRGB(i, j, Color.BLACK.getRGB());
				}else {
					image.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}
		return image;
	}
	
	/**
	 * @since ��ȡ����ͼƬ���ڰ׻����Եõ�������ȴʱ��
	 * @return ������Ͷ�ӦCD
	 * @throws IOException 
	 * */
	public static Map<String, Double> getCds() throws IOException {
		Map<String, Double> result = new HashMap<>();
		// ��ȡ�ڰ׻���ļ���ͼƬ��keyΪ���ܵİ�����
		Map<String, BufferedImage> jnImage = getJnImg();
		// ��ȴʱ��ͼƬ�����ڶ�λʱ��λ�ã���ȡʱ��ͼƬ
		BufferedImage lengqueshijian = imgesMap.get("lengqueshijian");
		BufferedImage imge;
		// ѭ������ÿһ������ͼƬ���������ȴʱ��
		for (String key : jnImage.keySet()) {
			imge = jnImage.get(key);
			// ��ȡ����ȷ��ʱ��ͼƬλ��
			int[] point = ImageUtil.locateOnScreen(lengqueshijian, imge);
			if(point[0] == -1) {
				result.put(key, Double.valueOf("1"));
				continue;
			}
			imge = imge.getSubimage(point[0] + 56, point[1], imge.getWidth() - point[0] - 56, 14); 
			File outImgFile = new File("D:/dnfImg/aaaa/"+key+".png");
			ImageIO.write(imge, "png", outImgFile);
			// ��ʱ��ͼƬת��������
			Map<Integer, String> resultMap = new TreeMap<>();
			for (String num : numImgesMap.keySet()) {
				List<Integer> point1 = ImageUtil.getXps(numImgesMap.get(num), imge);
				if(point1 != null && point1.size() > 0) {
					for (int i = 0; i < point1.size(); i++) {
						resultMap.put(point1.get(i), num);
					}
				}
			}
			// ���ݸ������ֵĺ����������ȡ��ȴʱ����ַ���
			String str = "";
			for (Integer key1 : resultMap.keySet()) {
				str += resultMap.get(key1);
			}
			result.put(key, Double.valueOf(str) * 1000);
		}
		
		return result;
	}
	
	/**
	 * @description ����ͼս��
	 * */
	public static boolean fightDragon() throws IOException {
		boolean isOver = false;
		// ��ȡ��������ͼƬ���ڰ׻�
//		Map<String, Double> jns = getCds();
		
		// ��ͼ������ƶ��������ؿ������ѡ�в����ո��ȷ��
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD6,2000);
		RobotUtil.mouseMove(615, 408);
		RobotUtil.mouseLeft();
		RobotUtil.preceKey(KeyEvent.VK_SPACE);
		
		// ��ʼ�������̳߳�
//		for (String key : jns.keySet()) {
//			CompareSingleSkillImg cpSkImg = new CompareSingleSkillImg(key, jns.get(key).longValue());
//			threads.add(cpSkImg);
//		}
		
		while(true && !isEnd) {
			// ��ͼ�ո�����ѡ��
			RobotUtil.mouseMove(407, 456);
			RobotUtil.mouseLeft();
			isOver = false;
			while(true && !isEnd) {
				RobotUtil.preceKey(KeyEvent.VK_SPACE);
				if(fightAgain()) {
					RobotUtil.preceKey(KeyEvent.VK_F10);
				}
				// ��ȡ׼�����жϿ�ʼս��
				if(isFighting()) {
					break;
				}
			}
			// �ύ�����߳����̳߳���ִ��
//			for(int i = 0;i<threads.size();i++){
//				if(threads.get(i).isShutdown) {
//					threads.get(i).setShutdown(false);
//				}
//				executor.submit(threads.get(i));
//			}
			// ѭ��ASDFG  X ����
			while(true && !isEnd) {
				RobotUtil.preceKey(KeyEvent.VK_A);
				RobotUtil.preceKey(KeyEvent.VK_S);
				RobotUtil.preceKey(KeyEvent.VK_Q);
				RobotUtil.preceKey(KeyEvent.VK_W);
				RobotUtil.preceKey(KeyEvent.VK_D);
				RobotUtil.preceKey(KeyEvent.VK_F);
				RobotUtil.preceKey(KeyEvent.VK_E);
				RobotUtil.preceKey(KeyEvent.VK_R);
				RobotUtil.preceKey(KeyEvent.VK_X);
				// �жϱ���ս���������������
				Color kongXueTiao = RobotUtil.getRobot().getPixelColor(485, 72);
				if(kongXueTiao.getRed() < 5 
						&& kongXueTiao.getGreen() < 5 
						&& kongXueTiao.getBlue() < 5 ) {
					RobotUtil.delay(1000);
					RobotUtil.preceKey(KeyEvent.VK_NUMPAD0);
					boolean isNext = false;
					// �ȴ���ս��һ����ɫ
					while(true && !isEnd) {
						for(int i =0;i<10;i++) {
							RobotUtil.preceKey(KeyEvent.VK_X);
						}
						if(isFighting() || isEnd) {
							break;
						}
						// F11ѡ���������³ǰ�ť����
						Color F11_f = RobotUtil.getRobot().getPixelColor(720, 116);
						Color F11_1 = RobotUtil.getRobot().getPixelColor(728, 116);
						if (isBetween(F11_f.getRed(), 230) && isBetween(F11_f.getGreen(), 200)
								&& isBetween(F11_f.getBlue(), 155) && isBetween(F11_1.getRed(), 230)
								&& isBetween(F11_1.getGreen(), 200) && isBetween(F11_1.getBlue(), 155)) {
							// ���������߳�
//							for(int i = 0;i<threads.size();i++){
//								threads.get(i).setShutdown(true);
//							}
							// F10 ����ս������ת����һ��ִ��
							if (fightAgain()) {
								RobotUtil.preceKey(KeyEvent.VK_F10);
								RobotUtil.delay(500);
								RobotUtil.preceKey(KeyEvent.VK_F10);
								isNext = true;
								break;
								// ���س���
							} else {
								isOver = true;
								break;
							}
						}
					}
					// F10����ս�����߷��س���
					if(isOver || isNext || isEnd) {
						break;
					}
				}
			}
			// ����ɫս��������F12��ͼ
			if(isOver || isEnd) {
				TxtLog.log("��ǰ��ɫF12�˳�");
				RobotUtil.preceKey(KeyEvent.VK_F12);
				int [] sunan = { 0, 0 };
				BufferedImage bgImage;
				int n = 1;
				while(true) {
					bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
					sunan = ImageUtil.locateOnScreen(imgesMap.get("sunan"), bgImage);
					if(sunan[0] > 0 || n > 6){
						break;
					}
					n++;
					RobotUtil.delay(1000);
				}
				break;
			}
		}
//		compareSkillImg.setShutdown(true);
		return true;
	}
	
	public static boolean fightAgain() {
		// F10 ����ս������ת����һ��ִ��
		Color F10_1 = RobotUtil.getRobot().getPixelColor(696, 86);
		Color F10_f = RobotUtil.getRobot().getPixelColor(690, 86);
		if (isBetween(F10_f.getRed(), 230) && isBetween(F10_f.getGreen(), 200)
				&& isBetween(F10_f.getBlue(), 155) && isBetween(F10_1.getRed(), 230)
				&& isBetween(F10_1.getGreen(), 200) && isBetween(F10_1.getBlue(), 155)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @description �ж��ٴ�ս����VS��ʶ���ڣ���Ѫ������
	 * */
	public static boolean isFighting() {
		Color manXueTiao = RobotUtil.getRobot().getPixelColor(490, 70);
		Color vs = RobotUtil.getRobot().getPixelColor(417,89);//(379, 82)RDB(12,12,12);
		Color vs1 = RobotUtil.getRobot().getPixelColor(415,40);
		if(vs.getRed() < 5 && vs.getGreen() <5 && vs.getBlue() <5
				&& (vs1.getRed() >250 && vs1.getGreen() > 250 && vs1.getBlue() < 180)
				&& !(manXueTiao.getRed() < 5 && manXueTiao.getGreen() <5 && manXueTiao.getBlue() <5)) {
			return true;
		}
		return false;
	}
	
	public static boolean isBetween(int num,int standard) {
		if(num > (standard-5) && num < (standard + 5)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @description ����װ��������װ���ֽ���װ
	 * */
	public static void dealEquipment() {
		// �򿪸�ְҵ���
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD2);
		BufferedImage bgImage = ImageUtil.priteScreen(277, 241, 248, 25);
		int[] fenjieshi = ImageUtil.locateOnScreen(imgesMap.get("fenjieshi"), bgImage);
		TxtLog.log("fenjieshi x="+fenjieshi[0]+"y="+fenjieshi[1]);
		if(fenjieshi[0] != -1) {
			// ����ֽ��
			RobotUtil.mouseMove(480, 335);
			RobotUtil.mouseLeft();
			RobotUtil.delay(500);
			RobotUtil.mouseLeft();
			RobotUtil.delay(500);
			RobotUtil.preceKey(KeyEvent.VK_SPACE);
			// �ֽ�װ��
			RobotUtil.mouseMove(410, 335);
			RobotUtil.mouseLeft();
		}else {
			// �رո�ְҵ���
			RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
			// �ƶ������Ͻǣ����㶨λ�ֽ��
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD4,1000);
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD8,2000);
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD6,2000);
			// ��װ���ֽ�����ֽ���װ
			RobotUtil.mouseMove(146, 326);
			RobotUtil.mouseLeft();
			// ѡ��ֽ�˵�
			RobotUtil.mouseMove(185, 380);
			RobotUtil.mouseLeft();
		}
		// ����ȫ���ֽⰴť��ȷ��
		RobotUtil.preceKey(KeyEvent.VK_A);
		RobotUtil.preceKey(KeyEvent.VK_ENTER);
		// �ȴ�5��ֽ���ɣ��رշּ���
		RobotUtil.delay(4000);
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	
	/**
	 * @description �涫�������
	 * @param saveMoney �Ƿ��Ǯ
	 * @param saveMaterial �Ƿ�����
	 * */
	public static void saveMoney(boolean saveMoney,boolean saveMaterial) {
		if(!saveMoney && !saveMaterial) {
			return;
		}
		// �򿪽�ɫѡ�����
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
		RobotUtil.mouseMove(380, 470);
		RobotUtil.mouseLeft();
		BufferedImage bgImage;
		while(true) {
			bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
			if(ImageUtil.isImgExist(imgesMap.get("xuanzejuese"), bgImage)){
				break;
			}
			RobotUtil.delay(1000);
		}
		RobotUtil.preceKey(KeyEvent.VK_SPACE);
		RobotUtil.delay(5000);
		// �򿪽��
		RobotUtil.mouseMove(250, 320);
		RobotUtil.mouseLeft();
		RobotUtil.preceKey(KeyEvent.VK_A);
		RobotUtil.preceKey(KeyEvent.VK_SPACE);
		
		// ���˻����
		RobotUtil.mouseMove(zhjinku[0] + 20, zhjinku[1] + 6);
		RobotUtil.mouseLeft();
		// �����
		if(saveMaterial) {
			RobotUtil.preceKey(KeyEvent.VK_A);
			RobotUtil.preceKey(KeyEvent.VK_SPACE);
		}
		// ���ң�ȷ��
		if(saveMoney) {
			RobotUtil.mouseMove(153, 428);
			RobotUtil.mouseLeft();
			RobotUtil.mouseLeft();
		}
		// �رղ˵�
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
		RobotUtil.delay(1000);
	}
	
	/**
	 * @description ����ɫ
	 * @param roleNum �ڼ���ɫ
	 * */
	public static boolean changeRole(int roleNum) {
		// �򿪽�ɫѡ�����
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
		RobotUtil.mouseMove(380, 470);
		RobotUtil.mouseLeft();
		BufferedImage bgImage;
		int m = 1;
		while(true) {
			bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
			if(ImageUtil.isImgExist(imgesMap.get("xuanzejuese"), bgImage) || m > 6){
				break;
			}
			m++;
			RobotUtil.delay(1000);
		}
		// ѭ������ɫ���������Ϸ�
		if(roleNum == 0) {
			RobotUtil.mouseMove(790, 342);
			for(int i = 0;i < 8;i++) {
				RobotUtil.mouseLeft();
			}
			++roleNum;
		}
		// ������һ����ɫ�ĵ��λ��
		int n = (roleNum - 1) % 6;
		// ��һ�Ž�ɫ
		if (n == 0 && roleNum > 1) {
			RobotUtil.mouseMove(790, 536);
			RobotUtil.mouseLeft();
		} 
		int x = 130 * n + 65;
		int y = 510;
		RobotUtil.mouseMove(x, y);
		RobotUtil.mouseLeft();
		RobotUtil.preceKey(KeyEvent.VK_SPACE);
		m = 1;
		while(true) {
			bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
			if(ImageUtil.isImgExist(imgesMap.get("shangcheng"), bgImage) || m > 6){
				break;
			}
			RobotUtil.delay(1000);
			m++;
		}
		return true;
	}
	
	public static void overTime(int roleNum){
		// ÿ����ɫ��ʱ25���ӣ�totalTimeΪ���н�ɫ�ܺ�ʱ�����룩
		try {
			java.lang.Runtime.getRuntime().exec( "shutdown -s -t " + (roleNum * 25 * 60));
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
}
