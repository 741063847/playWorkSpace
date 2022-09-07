import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;

import DnfHelper.KeyBoardUtil;
import DnfHelper.TxtLog;

public class DnfUtil {
	
	public static boolean isEnd = false;
	private static Map<String,String> numberMap = new HashMap<>();
	
	private static Map<String,BufferedImage> imgesMap = new HashMap<>();
	// ����Ʊλ��
	public static int[] hlPiao = { 0, 0 };
	// ȡ����ť��λ��
	public static int[] quchu = { 0, 0 };
	// ����ť��λ��
	public static int[] renwu = { 0, 0 };
	// �޾����ϵ�λ��
	public static int[] wujin = { 0, 0 };
	// �����������ŵ�λ��
	public static int[] xihaian = { 0, 0 };
	
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
		loadImages();
	}
	
	public static void main(String[] args) { 
		KeyBoardUtil.getRobot().delay(2000);
		try {
			dealEquipment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @since publicBtnlocateInit ������ťλ�ó�ʼ��
	 * */
	public static void publicBtnlocateInit() {
		// �򿪸��˲ֿ�
		KeyBoardUtil.mouseMove(250, 320);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(1000);
		KeyBoardUtil.mouseMove(0, 0);
		// ȡ����ť��λ��
		BufferedImage bgImage = KeyBoardUtil.priteScreen(0, 0, 800, 600);
		if(imgesMap.isEmpty()) {
			TxtLog.log("empty");
		}else {
			TxtLog.log("no empty");
		}
		quchu = KeyBoardUtil.locateOnScreen(imgesMap.get("quchu.png"), bgImage);
		TxtLog.log("quchu x="+quchu[0]+"y="+quchu[1]);
		KeyBoardUtil.preceKey(KeyEvent.VK_TAB);
		KeyBoardUtil.getRobot().delay(2000);
		// �޾�����λ��
		bgImage = KeyBoardUtil.priteScreen(0, 0, 800, 600);
		wujin = KeyBoardUtil.locateOnScreen(imgesMap.get("wujin.png"), bgImage);
		TxtLog.log("wujin x="+wujin[0]+"y="+wujin[1]);
		// �رղֿ�
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * @since roleBtnlocateInit ��ɫ��ťλ�ó�ʼ��
	 * */
	private static void roleBtnlocateInit() {
		// �򿪸��˱���
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD1);
		KeyBoardUtil.mouseMove(0, 0);
		// ����ť��λ��
		BufferedImage bgImage = KeyBoardUtil.priteScreen(0, 0, 800, 600);
		renwu = KeyBoardUtil.locateOnScreen(imgesMap.get("renwu1.png"), bgImage);
		if(renwu[0] == -1) {
			renwu = KeyBoardUtil.locateOnScreen(imgesMap.get("renwu2.png"), bgImage);
		}
		TxtLog.log("renwu x="+renwu[0]+"y="+renwu[1]);
		// �������ť
		int x = renwu[0] + KeyBoardUtil.getRandomNum(imgesMap.get("renwu1.png").getWidth(), 2);
		int y = renwu[1] + KeyBoardUtil.getRandomNum(imgesMap.get("renwu1.png").getHeight(), 2);
		TxtLog.log("dianjirenwu x="+renwu[0]+"y="+renwu[1]);
		KeyBoardUtil.mouseMove(x , y);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(1000);
		bgImage = KeyBoardUtil.priteScreen(0, 0, 800, 600);
		hlPiao = KeyBoardUtil.locateOnScreen(imgesMap.get("hlPiao1.png"), bgImage);
		if(hlPiao[0] == -1) {
			hlPiao = KeyBoardUtil.locateOnScreen(imgesMap.get("hlPiao2.png"), bgImage);
		}
		TxtLog.log("hlPiao x="+hlPiao[0]+"y="+hlPiao[1]);
	}
	
	/**
	 * @since ������ԴͼƬ
	 * */
	private static void loadImages() {
		TxtLog.log(DnfUtil.class.getResource("").toString());
		String path = System.getProperty("user.dir") + "/imgs";
		TxtLog.log("path="+path);
		File file = new File(path);
		if(!file.exists()) {
			TxtLog.log("1111111111111122222222222222222222222222");
			return;
		}
		File list[] = file.listFiles();
		try {
			for (File img : list) {
				imgesMap.put(img.getName(), ImageIO.read(img));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			KeyBoardUtil.mouseMove(100, 100);
			KeyBoardUtil.mouseLeft();
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
	    	KeyBoardUtil.mouseMove(100, 100);
			KeyBoardUtil.mouseLeft();
		    return "";
		}
	}
	
	/**
	 * @desc �رղֿ�����ʾ
	 * */
	static void colseLockTip() {
		KeyBoardUtil.mouseMove(250, 320);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(1500);
		KeyBoardUtil.mouseMove(435, 395);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.mouseMove(475, 360);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * �ķ����
	 * */
	public static void changeKey() {
		// �򿪲˵�
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		// ��Ϸ����
		KeyBoardUtil.mouseMove(254, 448);
		KeyBoardUtil.mouseLeft();
		// ��ݼ�����
		KeyBoardUtil.mouseMove(200, 450);
		KeyBoardUtil.mouseLeft();
		// ��
		KeyBoardUtil.mouseMove(315,355);//315,355
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8);
		// ��
		KeyBoardUtil.mouseMove(315,375);//315,375
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4);
		// ��
		KeyBoardUtil.mouseMove(315,395);//315,395
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD5);
		// ��
		KeyBoardUtil.mouseMove(315,415);//315,415
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6);
		// ����
		KeyBoardUtil.mouseMove(570, 515);
		KeyBoardUtil.mouseLeft();
		// �˳��˵�
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
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
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
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
					int red = KeyBoardUtil.getRobot().
							getPixelColor(startX-i, startY+j).getRed();
					int blue = KeyBoardUtil.getRobot().
							getPixelColor(startX-i, startY+j).getBlue();
					int green = KeyBoardUtil.getRobot().
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
	static void getMaterial(int piaoNum) {
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
			KeyBoardUtil.mouseMove(250, 320);
			KeyBoardUtil.mouseLeft();
			// ���˻����
			KeyBoardUtil.getRobot().delay(500);
			KeyBoardUtil.preceKey(KeyEvent.VK_TAB);
			// ����ȡ����ť
			int x = quchu[0] + KeyBoardUtil.getRandomNum(imgesMap.get("quchu.png").getWidth(), 2);
			int y = quchu[1] + KeyBoardUtil.getRandomNum(imgesMap.get("quchu.png").getHeight(), 2);
			KeyBoardUtil.mouseMove(x , y);
			KeyBoardUtil.mouseLeft();
			// ѡ���޾�����
			x = wujin[0] + KeyBoardUtil.getRandomNum(imgesMap.get("wujin.png").getWidth(), 2);
			y = wujin[1] + KeyBoardUtil.getRandomNum(imgesMap.get("wujin.png").getHeight(), 2);
			KeyBoardUtil.mouseMove(x , y);
			KeyBoardUtil.mouseLeft();
//			// ���޾��������˱�����
//			KeyBoardUtil.mouseMove(25, 66);
//			KeyBoardUtil.getRobot().mousePress(KeyEvent.BUTTON1_MASK);
//			KeyBoardUtil.getRobot().delay(200);
//			KeyBoardUtil.mouseMove(730, 335);
//			KeyBoardUtil.getRobot().delay(200);
//			KeyBoardUtil.getRobot().mouseRelease(KeyEvent.BUTTON1_MASK);
//			KeyBoardUtil.getRobot().delay(200);
			// ����Ҫȡ�ò�������
			KeyBoardUtil.preceKey(aa[0]+96);
			KeyBoardUtil.preceKey(aa[1]+96);
			KeyBoardUtil.preceKey(aa[2]+96);
			KeyBoardUtil.preceKey(KeyEvent.VK_ENTER);
			// �رղ˵�
			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			KeyBoardUtil.getRobot().delay(1000);
		}
	}
	
	/**
	 * @description �������Ƿ��䵽������ͼ
	 * @param piaoNum Ʊ����
	 * @param buyTicket �Ƿ���Ʊ
	 * */
	static void homeMoveToDragon(int piaoNum, boolean buyTicket) throws IOException {
		// �ƶ��������Ҳഫ����
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,3000);
		// �����������ŵ�λ��
		BufferedImage bgImage = KeyBoardUtil.priteScreen(0, 0, 800, 600);
		xihaian = KeyBoardUtil.locateOnScreen(imgesMap.get("xihaian.png"), bgImage);
		int n = 1;
		while(xihaian[0] == -1 || n > 6) {
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD5);
			bgImage = KeyBoardUtil.priteScreen(0, 0, 800, 600);
			xihaian = KeyBoardUtil.locateOnScreen(imgesMap.get("xihaian.png"), bgImage);
			n++;
		}
		TxtLog.log("xihaian x="+xihaian[0]+"y="+xihaian[1]);
		int x = xihaian[0] + KeyBoardUtil.getRandomNum(imgesMap.get("xihaian.png").getWidth(), 2);
		int y = xihaian[1] + KeyBoardUtil.getRandomNum(imgesMap.get("xihaian.png").getHeight(), 2);
		TxtLog.log("dianjixihaian x="+xihaian[0]+"y="+xihaian[1]);
		KeyBoardUtil.mouseMove(x , y);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(2000);
		// �������ɿڵ�����ͼ
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4,800);
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8,1600);
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,1600);
		// �÷����ѡ�л���
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8);
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		KeyBoardUtil.getRobot().delay(4000);
		// ����ͼ��������
		KeyBoardUtil.preceKey(KeyEvent.VK_N);
		if(buyTicket && piaoNum < 26) {
			KeyBoardUtil.getRobot().delay(2000);
			// �򿪵�ͼ���ƶ�����С��������ֹ�����ɫ�ڵ�
			KeyBoardUtil.mouseMove(393, 386);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.getRobot().delay(4000);
			KeyBoardUtil.preceKey(KeyEvent.VK_N);
			// ������С���������NPC�Ϸ���һ�㣬��ֹ��ҽ�ɫ�ڵ�
			KeyBoardUtil.mouseMove(465, 127);
			KeyBoardUtil.mouseLeft();
			// ���̵�
			KeyBoardUtil.mouseMove(515, 177);
			KeyBoardUtil.mouseLeft();
			// �ƶ�������Ʊλ��
			KeyBoardUtil.mouseMove(365, 170);
			KeyBoardUtil.mouseShiftLeft();
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD3);
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD3);
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD3);
			// ����enterȷ����Ʊ
			KeyBoardUtil.preceKey(KeyEvent.VK_ENTER);
			KeyBoardUtil.preceKey(KeyEvent.VK_ENTER);
			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			// �򿪵�ͼ
			KeyBoardUtil.preceKey(KeyEvent.VK_N);
		}
		// ������ͼ 
		KeyBoardUtil.mouseMove(392, 277);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(10000);
	}
	
	/**
	 * @description ����ͼս��
	 * */
	static void fightDragon() throws IOException {
		boolean isOver = false;
		// ��ͼ������ƶ��������ؿ������ѡ�в����ո��ȷ��
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,2000);
		KeyBoardUtil.mouseMove(615, 408);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		// 
		while(true && !isEnd) {
			// ��ͼ�ո�����ѡ��
			double startTime = System.currentTimeMillis();       
			double endTime = startTime;
			KeyBoardUtil.mouseMove(407, 456);
			KeyBoardUtil.mouseLeft();
			isOver = false;
			while(true && !isEnd) {
				KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
				// ��ȡ׼�����жϿ�ʼս��
				if(isFighting()) {
					break;
				}
				endTime = System.currentTimeMillis();
				if((endTime - startTime) > 15000) {
					isOver = true;
					KeyBoardUtil.preceKey(KeyEvent.VK_F12);
					break;
				}
			}
			// ��Ʊ�յ�30���˳�
			if(isOver || isEnd) {
				// ��F11����ս������PLʱ�Ƴ�
//				KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
				break;
			}//300��534    331��534   300��565
			// ѭ��ASDFG  X ����
			while(true && !isEnd) {
				KeyBoardUtil.preceKey(KeyEvent.VK_A);
				KeyBoardUtil.preceKey(KeyEvent.VK_S);
				KeyBoardUtil.preceKey(KeyEvent.VK_D);
				KeyBoardUtil.preceKey(KeyEvent.VK_F);
				KeyBoardUtil.preceKey(KeyEvent.VK_G);
				KeyBoardUtil.preceKey(KeyEvent.VK_X);
				// �жϱ���ս���������������
				Color kongXueTiao = KeyBoardUtil.getRobot().getPixelColor(485, 72);
				if(kongXueTiao.getRed() < 5 
						&& kongXueTiao.getGreen() < 5 
						&& kongXueTiao.getBlue() < 5 ) {
					KeyBoardUtil.getRobot().delay(1000);
					KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD0);
					boolean isNext = false;
					// �ȴ���ս��һ����ɫ
					while(true && !isEnd) {
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						if(isFighting() || isEnd) {
							break;
						}
						// F10 ����ս������ת����һ��ִ��
						Color jixu = KeyBoardUtil.getRobot().getPixelColor(656, 56);
						if(isBetween(jixu.getRed(),29) && isBetween(jixu.getGreen(),12) && isBetween(jixu.getBlue(),15)) {
							KeyBoardUtil.preceKey(KeyEvent.VK_F10);//VK_F10��VK_F11
							isNext = true;
							break;
						}
					}
					// F10����ս��
					if(isNext || isEnd) {
						break;
					}
				}
			}
			// ����ɫս��������F12��ͼ
			if(isOver || isEnd) {
				TxtLog.log("��ǰ��ɫF12�˳�");
				break;
			}
		}
	}
	
	/**
	 * @description �ж��ٴ�ս����VS��ʶ���ڣ���Ѫ������
	 * */
	private static boolean isFighting() {
		Color manXueTiao = KeyBoardUtil.getRobot().getPixelColor(490, 70);
		Color vs = KeyBoardUtil.getRobot().getPixelColor(417,89);//(379, 82)RDB(12,12,12);
		Color vs1 = KeyBoardUtil.getRobot().getPixelColor(415,40);
		if(vs.getRed() < 5 && vs.getGreen() <5 && vs.getBlue() <5
				&& (vs1.getRed() >250 && vs1.getGreen() > 250 && vs1.getBlue() < 180)
				&& !(manXueTiao.getRed() < 5 && manXueTiao.getGreen() <5 && manXueTiao.getBlue() <5)) {
			return true;
		}
		return false;
	}
	
	private static boolean isBetween(int num,int standard) {
		if(num > (standard-5) && num < (standard + 5)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @description ����װ��������װ���ֽ���װ
	 * */
	static void dealEquipment() {
		KeyBoardUtil.getRobot().delay(4000);
		// �򿪸�ְҵ���
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD2);
		BufferedImage bgImage = KeyBoardUtil.priteScreen(277, 241, 248, 25);
		int[] fenjieshi = KeyBoardUtil.locateOnScreen(imgesMap.get("fenjieshi.png"), bgImage);
		TxtLog.log("fenjieshi x="+fenjieshi[0]+"y="+fenjieshi[1]);
		if(fenjieshi[0] != -1) {
			// ����ֽ��
			KeyBoardUtil.mouseMove(480, 335);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.getRobot().delay(500);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.getRobot().delay(500);
			KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
			// �ֽ�װ��
			KeyBoardUtil.mouseMove(410, 335);
			KeyBoardUtil.mouseLeft();
		}else {
			// �رո�ְҵ���
			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			// �ƶ������Ͻǣ����㶨λ�ֽ��
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4,1000);
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8,2000);
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,2000);
			// �򿪷ֽ���̵꣬���۰�װ
//			KeyBoardUtil.mouseMove(146, 326);
//			KeyBoardUtil.mouseLeft();
//			KeyBoardUtil.getRobot().delay(500);
//			KeyBoardUtil.mouseMove(180, 355);
//			KeyBoardUtil.mouseLeft();
//			KeyBoardUtil.getRobot().delay(500);
//			// һ������
//			KeyBoardUtil.preceKey(KeyEvent.VK_A);
//			KeyBoardUtil.getRobot().delay(500);
//			KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
//			Point point=MouseInfo.getPointerInfo().getLocation();
//			KeyBoardUtil.mouseMove(point.x - 55, point.y);
//			KeyBoardUtil.getRobot().delay(500);
//			KeyBoardUtil.mouseLeft();
//			// �ر��̵�
//			KeyBoardUtil.getRobot().delay(500);
//			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			// ��װ���ֽ�����ֽ���װ
			KeyBoardUtil.mouseMove(146, 326);
			KeyBoardUtil.mouseLeft();
			// ѡ��ֽ�˵�
			KeyBoardUtil.mouseMove(185, 380);
			KeyBoardUtil.mouseLeft();
		}
		// ����ȫ���ֽⰴť��ȷ��
		KeyBoardUtil.preceKey(KeyEvent.VK_A);
		KeyBoardUtil.preceKey(KeyEvent.VK_ENTER);
		// �ȴ�5��ֽ���ɣ��رշּ���
		KeyBoardUtil.getRobot().delay(4000);
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	
	/**
	 * @description �涫�������
	 * @param saveMoney �Ƿ��Ǯ
	 * @param saveMaterial �Ƿ�����
	 * */
	static void saveMoney(boolean saveMoney,boolean saveMaterial) {
		if(!saveMoney && !saveMaterial) {
			return;
		}
		// �򿪽�ɫѡ�����
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		KeyBoardUtil.mouseMove(380, 470);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(3000);
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		KeyBoardUtil.getRobot().delay(5000);
		// �򿪽��
		KeyBoardUtil.mouseMove(250, 320);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_A);
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		
		// ���˻����
		KeyBoardUtil.preceKey(KeyEvent.VK_TAB);
		// �����
		if(saveMaterial) {
			KeyBoardUtil.preceKey(KeyEvent.VK_A);
			KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		}
		// ���ң�ȷ��
		if(saveMoney) {
			KeyBoardUtil.mouseMove(153, 428);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.mouseLeft();
		}
		// �رղ˵�
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		KeyBoardUtil.getRobot().delay(1000);
	}
	
	/**
	 * @description ����ɫ
	 * @param roleNum �ڼ���ɫ
	 * */
	static void changeRole(int roleNum) {
		// �򿪽�ɫѡ�����
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		KeyBoardUtil.mouseMove(380, 470);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(6000);
		// ѭ������ɫ���������Ϸ�
		if(roleNum == 0) {
			KeyBoardUtil.mouseMove(790, 342);
			for(int i = 0;i < 8;i++) {
				KeyBoardUtil.mouseLeft();
			}
			++roleNum;
		}
		// ������һ����ɫ�ĵ��λ��
		int n = (roleNum - 1) % 6;
		// ��һ�Ž�ɫ
		if (n == 0 && roleNum > 1) {
			KeyBoardUtil.mouseMove(790, 536);
			KeyBoardUtil.mouseLeft();
		} 
		int x = 130 * n + 65;
		int y = 510;
		KeyBoardUtil.mouseMove(x, y);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		KeyBoardUtil.getRobot().delay(15000);
	}
}
