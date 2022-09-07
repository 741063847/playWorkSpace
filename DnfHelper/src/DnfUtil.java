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
	// 黄龙票位置
	public static int[] hlPiao = { 0, 0 };
	// 取出按钮的位置
	public static int[] quchu = { 0, 0 };
	// 任务按钮的位置
	public static int[] renwu = { 0, 0 };
	// 无尽材料的位置
	public static int[] wujin = { 0, 0 };
	// 西海岸传送门的位置
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
	 * @since publicBtnlocateInit 公共按钮位置初始化
	 * */
	public static void publicBtnlocateInit() {
		// 打开个人仓库
		KeyBoardUtil.mouseMove(250, 320);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(1000);
		KeyBoardUtil.mouseMove(0, 0);
		// 取出按钮的位置
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
		// 无尽材料位置
		bgImage = KeyBoardUtil.priteScreen(0, 0, 800, 600);
		wujin = KeyBoardUtil.locateOnScreen(imgesMap.get("wujin.png"), bgImage);
		TxtLog.log("wujin x="+wujin[0]+"y="+wujin[1]);
		// 关闭仓库
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * @since roleBtnlocateInit 角色按钮位置初始化
	 * */
	private static void roleBtnlocateInit() {
		// 打开个人背包
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD1);
		KeyBoardUtil.mouseMove(0, 0);
		// 任务按钮的位置
		BufferedImage bgImage = KeyBoardUtil.priteScreen(0, 0, 800, 600);
		renwu = KeyBoardUtil.locateOnScreen(imgesMap.get("renwu1.png"), bgImage);
		if(renwu[0] == -1) {
			renwu = KeyBoardUtil.locateOnScreen(imgesMap.get("renwu2.png"), bgImage);
		}
		TxtLog.log("renwu x="+renwu[0]+"y="+renwu[1]);
		// 点击任务按钮
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
	 * @since 加载资源图片
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
	 * 游戏窗口复位
	 * */
	public static String resetLocation() {
		HWND hwnd = User32.INSTANCE.FindWindow(null, "地下城与勇士");//阴阳师-网易游戏
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
			TxtLog.log("地下城与勇士未启动！");
			return "地下城与勇士未启动！";
		}else{
			WinDef.RECT winRect = new  WinDef.RECT();
		    User32.INSTANCE.GetWindowRect(hwnd, winRect);
	 	    int winWidth = winRect.right-winRect.left;
		    int winHeight = winRect.bottom-winRect.top;
		    User32.INSTANCE.MoveWindow(hwnd, 0, 0, winWidth, winHeight, true);
		    // 选中游戏窗口，将其置于最前面
	    	KeyBoardUtil.mouseMove(100, 100);
			KeyBoardUtil.mouseLeft();
		    return "";
		}
	}
	
	/**
	 * @desc 关闭仓库锁提示
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
	 * 改方向键
	 * */
	public static void changeKey() {
		// 打开菜单
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		// 游戏设置
		KeyBoardUtil.mouseMove(254, 448);
		KeyBoardUtil.mouseLeft();
		// 快捷键设置
		KeyBoardUtil.mouseMove(200, 450);
		KeyBoardUtil.mouseLeft();
		// 上
		KeyBoardUtil.mouseMove(315,355);//315,355
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8);
		// 左
		KeyBoardUtil.mouseMove(315,375);//315,375
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4);
		// 下
		KeyBoardUtil.mouseMove(315,395);//315,395
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD5);
		// 右
		KeyBoardUtil.mouseMove(315,415);//315,415
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6);
		// 保存
		KeyBoardUtil.mouseMove(570, 515);
		KeyBoardUtil.mouseLeft();
		// 退出菜单
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
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
	
	
	public static int getPiaos() {
		// 获取个人黄龙票位置
		roleBtnlocateInit();
		int num = getPiaos(hlPiao[0] + 23, hlPiao[1] - 8);//黄龙票在6快捷键时坐标183, 571
		// 关闭个人背包
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		return num;
	}
	
	/**
	 * getPiao 获取角色当前门票数
	 * */
	public static int getPiaos(int x, int y) {
		// 查询当前角色门票数量,从数字右上角点开始
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
			// 数字为1
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
	 * @description 取材料
	 * @param piaoNum 当前角色票数
	 * */
	static void getMaterial(int piaoNum) {
		// 若票不足，取材料无尽的永恒
		if(piaoNum < 26) {
			// 计算无尽的永恒数量
			int [] aa = {0,0,0};
			// 票不足，购买26张票
			int yonNum = (100 - piaoNum) * 5;
			aa[2] = yonNum%10;
			aa[1] = (yonNum/10)%10;
			aa[0] = (yonNum/100)%10;
			// 打开金库
			KeyBoardUtil.mouseMove(250, 320);
			KeyBoardUtil.mouseLeft();
			// 打开账户金库
			KeyBoardUtil.getRobot().delay(500);
			KeyBoardUtil.preceKey(KeyEvent.VK_TAB);
			// 单击取出按钮
			int x = quchu[0] + KeyBoardUtil.getRandomNum(imgesMap.get("quchu.png").getWidth(), 2);
			int y = quchu[1] + KeyBoardUtil.getRandomNum(imgesMap.get("quchu.png").getHeight(), 2);
			KeyBoardUtil.mouseMove(x , y);
			KeyBoardUtil.mouseLeft();
			// 选择无尽材料
			x = wujin[0] + KeyBoardUtil.getRandomNum(imgesMap.get("wujin.png").getWidth(), 2);
			y = wujin[1] + KeyBoardUtil.getRandomNum(imgesMap.get("wujin.png").getHeight(), 2);
			KeyBoardUtil.mouseMove(x , y);
			KeyBoardUtil.mouseLeft();
//			// 将无尽拖至个人背包中
//			KeyBoardUtil.mouseMove(25, 66);
//			KeyBoardUtil.getRobot().mousePress(KeyEvent.BUTTON1_MASK);
//			KeyBoardUtil.getRobot().delay(200);
//			KeyBoardUtil.mouseMove(730, 335);
//			KeyBoardUtil.getRobot().delay(200);
//			KeyBoardUtil.getRobot().mouseRelease(KeyEvent.BUTTON1_MASK);
//			KeyBoardUtil.getRobot().delay(200);
			// 输入要取得材料数量
			KeyBoardUtil.preceKey(aa[0]+96);
			KeyBoardUtil.preceKey(aa[1]+96);
			KeyBoardUtil.preceKey(aa[2]+96);
			KeyBoardUtil.preceKey(KeyEvent.VK_ENTER);
			// 关闭菜单
			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			KeyBoardUtil.getRobot().delay(1000);
		}
	}
	
	/**
	 * @description 从赛利亚房间到进青龙图
	 * @param piaoNum 票数量
	 * @param buyTicket 是否买票
	 * */
	static void homeMoveToDragon(int piaoNum, boolean buyTicket) throws IOException {
		// 移动至房间右侧传送阵
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,3000);
		// 西海岸传送门的位置
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
		// 西海岸渡口到青龙图
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4,800);
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8,1600);
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,1600);
		// 用方向键选中黄龙
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8);
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		KeyBoardUtil.getRobot().delay(4000);
		// 将地图整体右移
		KeyBoardUtil.preceKey(KeyEvent.VK_N);
		if(buyTicket && piaoNum < 26) {
			KeyBoardUtil.getRobot().delay(2000);
			// 打开地图，移动贴身小铁柱，防止点击角色遮挡
			KeyBoardUtil.mouseMove(393, 386);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.getRobot().delay(4000);
			KeyBoardUtil.preceKey(KeyEvent.VK_N);
			// 青龙到小铁柱，点击NPC上方多一点，防止玩家角色遮挡
			KeyBoardUtil.mouseMove(465, 127);
			KeyBoardUtil.mouseLeft();
			// 打开商店
			KeyBoardUtil.mouseMove(515, 177);
			KeyBoardUtil.mouseLeft();
			// 移动至黄龙票位置
			KeyBoardUtil.mouseMove(365, 170);
			KeyBoardUtil.mouseShiftLeft();
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD3);
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD3);
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD3);
			// 两次enter确认买票
			KeyBoardUtil.preceKey(KeyEvent.VK_ENTER);
			KeyBoardUtil.preceKey(KeyEvent.VK_ENTER);
			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			// 打开地图
			KeyBoardUtil.preceKey(KeyEvent.VK_N);
		}
		// 进青龙图 
		KeyBoardUtil.mouseMove(392, 277);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(10000);
	}
	
	/**
	 * @description 青龙图战斗
	 * */
	static void fightDragon() throws IOException {
		boolean isOver = false;
		// 进图，鼠标移动至黄龙关卡，左键选中并按空格键确认
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,2000);
		KeyBoardUtil.mouseMove(615, 408);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		// 
		while(true && !isEnd) {
			// 进图空格跳过选人
			double startTime = System.currentTimeMillis();       
			double endTime = startTime;
			KeyBoardUtil.mouseMove(407, 456);
			KeyBoardUtil.mouseLeft();
			isOver = false;
			while(true && !isEnd) {
				KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
				// 获取准备，判断开始战斗
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
			// 无票空等30秒退出
			if(isOver || isEnd) {
				// 用F11继续战斗，无PL时推出
//				KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
				break;
			}//300，534    331，534   300，565
			// 循环ASDFG  X 攻击
			while(true && !isEnd) {
				KeyBoardUtil.preceKey(KeyEvent.VK_A);
				KeyBoardUtil.preceKey(KeyEvent.VK_S);
				KeyBoardUtil.preceKey(KeyEvent.VK_D);
				KeyBoardUtil.preceKey(KeyEvent.VK_F);
				KeyBoardUtil.preceKey(KeyEvent.VK_G);
				KeyBoardUtil.preceKey(KeyEvent.VK_X);
				// 判断本次战斗结束，聚物捡东西
				Color kongXueTiao = KeyBoardUtil.getRobot().getPixelColor(485, 72);
				if(kongXueTiao.getRed() < 5 
						&& kongXueTiao.getGreen() < 5 
						&& kongXueTiao.getBlue() < 5 ) {
					KeyBoardUtil.getRobot().delay(1000);
					KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD0);
					boolean isNext = false;
					// 等待对战下一个角色
					while(true && !isEnd) {
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						KeyBoardUtil.preceKey(KeyEvent.VK_X,100);
						if(isFighting() || isEnd) {
							break;
						}
						// F10 继续战斗，跳转至第一步执行
						Color jixu = KeyBoardUtil.getRobot().getPixelColor(656, 56);
						if(isBetween(jixu.getRed(),29) && isBetween(jixu.getGreen(),12) && isBetween(jixu.getBlue(),15)) {
							KeyBoardUtil.preceKey(KeyEvent.VK_F10);//VK_F10改VK_F11
							isNext = true;
							break;
						}
					}
					// F10继续战斗
					if(isNext || isEnd) {
						break;
					}
				}
			}
			// 本角色战斗结束，F12出图
			if(isOver || isEnd) {
				TxtLog.log("当前角色F12退出");
				break;
			}
		}
	}
	
	/**
	 * @description 判断再次战斗，VS标识存在，且血条不空
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
	 * @description 处理装备，卖白装，分解蓝装
	 * */
	static void dealEquipment() {
		KeyBoardUtil.getRobot().delay(4000);
		// 打开副职业面板
		KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD2);
		BufferedImage bgImage = KeyBoardUtil.priteScreen(277, 241, 248, 25);
		int[] fenjieshi = KeyBoardUtil.locateOnScreen(imgesMap.get("fenjieshi.png"), bgImage);
		TxtLog.log("fenjieshi x="+fenjieshi[0]+"y="+fenjieshi[1]);
		if(fenjieshi[0] != -1) {
			// 修理分解机
			KeyBoardUtil.mouseMove(480, 335);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.getRobot().delay(500);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.getRobot().delay(500);
			KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
			// 分解装备
			KeyBoardUtil.mouseMove(410, 335);
			KeyBoardUtil.mouseLeft();
		}else {
			// 关闭副职业面板
			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			// 移动至右上角，方便定位分解机
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD4,1000);
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD8,2000);
			KeyBoardUtil.preceKey(KeyEvent.VK_NUMPAD6,2000);
			// 打开分解机商店，出售白装
//			KeyBoardUtil.mouseMove(146, 326);
//			KeyBoardUtil.mouseLeft();
//			KeyBoardUtil.getRobot().delay(500);
//			KeyBoardUtil.mouseMove(180, 355);
//			KeyBoardUtil.mouseLeft();
//			KeyBoardUtil.getRobot().delay(500);
//			// 一键出售
//			KeyBoardUtil.preceKey(KeyEvent.VK_A);
//			KeyBoardUtil.getRobot().delay(500);
//			KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
//			Point point=MouseInfo.getPointerInfo().getLocation();
//			KeyBoardUtil.mouseMove(point.x - 55, point.y);
//			KeyBoardUtil.getRobot().delay(500);
//			KeyBoardUtil.mouseLeft();
//			// 关闭商店
//			KeyBoardUtil.getRobot().delay(500);
//			KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
			// 打开装备分解机，分解蓝装
			KeyBoardUtil.mouseMove(146, 326);
			KeyBoardUtil.mouseLeft();
			// 选择分解菜单
			KeyBoardUtil.mouseMove(185, 380);
			KeyBoardUtil.mouseLeft();
		}
		// 单击全部分解按钮并确定
		KeyBoardUtil.preceKey(KeyEvent.VK_A);
		KeyBoardUtil.preceKey(KeyEvent.VK_ENTER);
		// 等待5秒分解完成，关闭分级机
		KeyBoardUtil.getRobot().delay(4000);
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	
	/**
	 * @description 存东西到金库
	 * @param saveMoney 是否存钱
	 * @param saveMaterial 是否存材料
	 * */
	static void saveMoney(boolean saveMoney,boolean saveMaterial) {
		if(!saveMoney && !saveMaterial) {
			return;
		}
		// 打开角色选择界面
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		KeyBoardUtil.mouseMove(380, 470);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(3000);
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		KeyBoardUtil.getRobot().delay(5000);
		// 打开金库
		KeyBoardUtil.mouseMove(250, 320);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_A);
		KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		
		// 打开账户金库
		KeyBoardUtil.preceKey(KeyEvent.VK_TAB);
		// 存材料
		if(saveMaterial) {
			KeyBoardUtil.preceKey(KeyEvent.VK_A);
			KeyBoardUtil.preceKey(KeyEvent.VK_SPACE);
		}
		// 存金币，确定
		if(saveMoney) {
			KeyBoardUtil.mouseMove(153, 428);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.mouseLeft();
		}
		// 关闭菜单
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		KeyBoardUtil.getRobot().delay(1000);
	}
	
	/**
	 * @description 换角色
	 * @param roleNum 第几角色
	 * */
	static void changeRole(int roleNum) {
		// 打开角色选择界面
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		KeyBoardUtil.mouseMove(380, 470);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(6000);
		// 循环将角色栏拉到最上方
		if(roleNum == 0) {
			KeyBoardUtil.mouseMove(790, 342);
			for(int i = 0;i < 8;i++) {
				KeyBoardUtil.mouseLeft();
			}
			++roleNum;
		}
		// 计算下一个角色的点击位置
		int n = (roleNum - 1) % 6;
		// 下一排角色
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
