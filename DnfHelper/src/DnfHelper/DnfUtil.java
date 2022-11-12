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
	// 一般图片素材
	private static Map<String,BufferedImage> imgesMap = new HashMap<>();
	// 数字图片素材
	private static Map<String,BufferedImage> numImgesMap = new HashMap<>();
	// 黄龙票位置
	public static int[] hlPiao = { 0, 0 };
	// 取出按钮的位置
	public static int[] quchu = { 0, 0 };
	// 账号金库的位置
	public static int[] zhjinku = { 0, 0 };
	// 任务按钮的位置
	public static int[] renwu = { 0, 0 };
	// 无尽材料的位置
	public static int[] wujin = { 0, 0 };
	// 西海岸传送门的位置
	public static int[] xihaian = { 0, 0 };
	
	private static String[] keyCodes = {"Q", "W", "E", "R", "T", "A", "S", "D", "F", "G"};
	
	// 线程池
	private static ExecutorService executor = Executors.newFixedThreadPool(10);
	
	// 消息处理线程
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
	 * @since getSkillImgs 获取技能图标数组
	 * @param xp 技能域起始横坐标
	 * @param yp 技能域起始纵坐标
	 * @param width 技能域宽度
	 * @param height 技能域高度
	 * @param len 图标边长
	 * @return 技能图标数组
	 * @throws IOException 
	 * */
	public static BufferedImage[] getSkillImgs(int xp, int yp, int width, int height, int len) throws IOException {
		BufferedImage[] imgs = new BufferedImage[10];
		int n = 0;
		BufferedImage img = ImageUtil.priteScreen(xp, yp, width, height);
		// 行
		for (int i = 1; i >= 0; i--) {
			// 列
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
	 * @since getFightPoint 获取刷图点
	 * */
	public static void getFightPoint() {
		// 打开刷图点购买界面
		RobotUtil.mouseMove(645, 595);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		// 购买每周刷图点
		RobotUtil.mouseMove(180, 210);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		// 切换到消耗品物品栏
		RobotUtil.mouseMove(575, 270);
		RobotUtil.mouseLeft();
		RobotUtil.delay(2000);
		// 使用刷图点药
		BufferedImage bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		int [] yao = ImageUtil.locateOnScreen(imgesMap.get("shuatudian"), bgImage);
		RobotUtil.mouseMove(yao[0] + 20, yao[1]);
		RobotUtil.mouseRight();
		// 购买贵族机要普通
		RobotUtil.mouseMove(350, 210);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		// 购买寂静城普通
		RobotUtil.mouseMove(180, 265);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		RobotUtil.mouseLeft();
		RobotUtil.delay(200);
		// 关闭界面
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * @since publicBtnlocateInit 公共按钮位置初始化
	 * */
	public static void publicBtnlocateInit() {
		// 打开个人仓库
		RobotUtil.mouseMove(250, 320);
		RobotUtil.mouseLeft();
		RobotUtil.delay(1000);
		RobotUtil.mouseMove(0, 0);
		// 取出按钮的位置
		BufferedImage bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		quchu = ImageUtil.locateOnScreen(imgesMap.get("quchu"), bgImage);
		// 账号金库位置
		zhjinku = ImageUtil.locateOnScreen(imgesMap.get("zhjinku"), bgImage);
		RobotUtil.mouseMove(zhjinku[0] + 20, zhjinku[1] + 6);
		RobotUtil.mouseLeft();
		RobotUtil.delay(1000);
		// 无尽材料位置
		bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		wujin = ImageUtil.locateOnScreen(imgesMap.get("wujin"), bgImage);
		// 关闭仓库
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * @since roleBtnlocateInit 角色按钮位置初始化
	 * */
	public  static void roleBtnlocateInit() {
		// 打开个人背包
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD1);
		RobotUtil.mouseMove(0, 0);
		// 任务按钮的位置
		BufferedImage bgImage = ImageUtil.priteScreen(0, 0, 800, 600);
		renwu = ImageUtil.locateOnScreen(imgesMap.get("renwu1"), bgImage);
		if(renwu[0] == -1) {
			renwu = ImageUtil.locateOnScreen(imgesMap.get("renwu2"), bgImage);
		}
		// 点击任务按钮
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
	 * @since 加载资源图片
	 * */
	public static void loadImages() {
		String commonImagePath = System.getProperty("user.dir") + "/imgs/common";
		imgesMap = ImageUtil.loadImages(commonImagePath);
		String numberImagePath = System.getProperty("user.dir") + "/imgs/number";
		numImgesMap = ImageUtil.loadImages(numberImagePath);
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
			RobotUtil.mouseMove(100, 100);
			RobotUtil.mouseLeft();
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
	    	RobotUtil.mouseMove(100, 100);
			RobotUtil.mouseLeft();
			RobotUtil.delay(1000);
		    return "";
		}
	}
	
	/**
	 * @desc 关闭仓库锁提示
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
	 * 改方向键
	 * */
	public static void changeKey() {
		// 打开菜单
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
		// 游戏设置
		RobotUtil.mouseMove(254, 448);
		RobotUtil.mouseLeft();
		// 快捷键设置
		RobotUtil.mouseMove(200, 450);
		RobotUtil.mouseLeft();
		// 查看是否已改键
		int n = 0;
		while(true) {
			// 上
			RobotUtil.mouseMove(315,355);//315,355
			RobotUtil.mouseLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD8);
			// 左
			RobotUtil.mouseMove(315,375);//315,375
			RobotUtil.mouseLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD4);
			// 下
			RobotUtil.mouseMove(315,395);//315,395
			RobotUtil.mouseLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD5);
			// 右
			RobotUtil.mouseMove(315,415);//315,415
			RobotUtil.mouseLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD6);
			// 保存
			RobotUtil.mouseMove(570, 515);
			RobotUtil.mouseLeft();
			n++;
			if(n > 2) {
				break;
			}
			// 获取方向键位置颜色，用于判断改键成功
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
		// 退出菜单
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
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
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
	public static void getMaterial(int piaoNum) {
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
			RobotUtil.mouseMove(250, 320);
			RobotUtil.mouseLeft();
			// 打开账户金库
			RobotUtil.delay(500);
			RobotUtil.mouseMove(zhjinku[0] + 20, zhjinku[1] + 6);
			RobotUtil.mouseLeft();
			// 单击取出按钮
			int x = quchu[0] + NumUtil.getRandomNum(imgesMap.get("quchu").getWidth(), 2);
			int y = quchu[1] + NumUtil.getRandomNum(imgesMap.get("quchu").getHeight(), 2);
			RobotUtil.mouseMove(x , y);
			RobotUtil.mouseLeft();
			// 选择无尽材料
			x = wujin[0] + NumUtil.getRandomNum(imgesMap.get("wujin").getWidth(), 2);
			y = wujin[1] + NumUtil.getRandomNum(imgesMap.get("wujin").getHeight(), 2);
			RobotUtil.mouseMove(x , y);
			RobotUtil.mouseLeft();
			// 输入要取得材料数量
			RobotUtil.preceKey(aa[0]+96);
			RobotUtil.preceKey(aa[1]+96);
			RobotUtil.preceKey(aa[2]+96);
			RobotUtil.preceKey(KeyEvent.VK_ENTER);
			// 关闭菜单
			RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
			RobotUtil.delay(1000);
		}
	}
	
	/**
	 * @description 从赛利亚房间到进青龙图
	 * @param piaoNum 票数量
	 * @param buyTicket 是否买票
	 * */
	public static boolean homeMoveToDragon(int piaoNum, boolean buyTicket) throws IOException {
		// 鼠标移动出屏幕，防止遮挡选项
		RobotUtil.mouseMove(0 , 0);
		// 移动至房间右侧传送阵
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD6,3000);
		// 西海岸传送门的位置
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
		// 西海岸渡口到青龙图
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD4,800);
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD8,1600);
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD6,1600);
		// 用方向键选中黄龙
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD8);
		RobotUtil.preceKey(KeyEvent.VK_SPACE);
		RobotUtil.delay(4000);
		// 将地图整体右移
		RobotUtil.preceKey(KeyEvent.VK_N);
		if(buyTicket && piaoNum < 26) {
			RobotUtil.delay(2000);
			// 打开地图，移动贴身小铁柱，防止点击角色遮挡
			RobotUtil.mouseMove(393, 386);
			RobotUtil.mouseLeft();
			RobotUtil.delay(4000);
			RobotUtil.preceKey(KeyEvent.VK_N);
			// 青龙到小铁柱，点击NPC上方多一点，防止玩家角色遮挡
			RobotUtil.mouseMove(465, 127);
			RobotUtil.mouseLeft();
			// 打开商店
			RobotUtil.mouseMove(515, 177);
			RobotUtil.mouseLeft();
			// 移动至黄龙票位置
			RobotUtil.mouseMove(365, 170);
			RobotUtil.mouseShiftLeft();
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD3);
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD3);
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD3);
			// 两次enter确认买票
			RobotUtil.preceKey(KeyEvent.VK_ENTER);
			RobotUtil.preceKey(KeyEvent.VK_SPACE);
			RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
			// 打开地图
			RobotUtil.preceKey(KeyEvent.VK_N);
		}
		// 进青龙图 
		RobotUtil.mouseMove(392, 277);
		RobotUtil.mouseLeft();
		RobotUtil.preceKey(KeyEvent.VK_N);
		RobotUtil.delay(10000);
		return true;
	}
	
	/**
	 * @description 技能键码转释放
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
	 * @since 获取技能图片
	 * @return 按键码和对应CD
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
	 * @since 技能图片黑白化
	 * @throws IOException 
	 * */
	public static BufferedImage changeImgColor(BufferedImage image) {
		Color color1 = new Color(83, 205, 255);
		Color color2 = new Color(188, 50, 50);
		Color color3 = new Color(134, 120, 79);
		//Color color4 = new Color(209, 185, 148); 名称
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
	 * @since 获取技能图片并黑白化，以得到技能冷却时间
	 * @return 按键码和对应CD
	 * @throws IOException 
	 * */
	public static Map<String, Double> getCds() throws IOException {
		Map<String, Double> result = new HashMap<>();
		// 获取黑白化后的技能图片，key为技能的按键码
		Map<String, BufferedImage> jnImage = getJnImg();
		// 冷却时间图片，用于定位时间位置，截取时间图片
		BufferedImage lengqueshijian = imgesMap.get("lengqueshijian");
		BufferedImage imge;
		// 循环处理每一个技能图片，并获得冷却时间
		for (String key : jnImage.keySet()) {
			imge = jnImage.get(key);
			// 截取更精确的时间图片位置
			int[] point = ImageUtil.locateOnScreen(lengqueshijian, imge);
			if(point[0] == -1) {
				result.put(key, Double.valueOf("1"));
				continue;
			}
			imge = imge.getSubimage(point[0] + 56, point[1], imge.getWidth() - point[0] - 56, 14); 
			File outImgFile = new File("D:/dnfImg/aaaa/"+key+".png");
			ImageIO.write(imge, "png", outImgFile);
			// 将时间图片转化成数字
			Map<Integer, String> resultMap = new TreeMap<>();
			for (String num : numImgesMap.keySet()) {
				List<Integer> point1 = ImageUtil.getXps(numImgesMap.get(num), imge);
				if(point1 != null && point1.size() > 0) {
					for (int i = 0; i < point1.size(); i++) {
						resultMap.put(point1.get(i), num);
					}
				}
			}
			// 根据各个数字的横坐标排序获取冷却时间的字符串
			String str = "";
			for (Integer key1 : resultMap.keySet()) {
				str += resultMap.get(key1);
			}
			result.put(key, Double.valueOf(str) * 1000);
		}
		
		return result;
	}
	
	/**
	 * @description 青龙图战斗
	 * */
	public static boolean fightDragon() throws IOException {
		boolean isOver = false;
		// 获取技能详情图片并黑白化
//		Map<String, Double> jns = getCds();
		
		// 进图，鼠标移动至黄龙关卡，左键选中并按空格键确认
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD6,2000);
		RobotUtil.mouseMove(615, 408);
		RobotUtil.mouseLeft();
		RobotUtil.preceKey(KeyEvent.VK_SPACE);
		
		// 初始化技能线程池
//		for (String key : jns.keySet()) {
//			CompareSingleSkillImg cpSkImg = new CompareSingleSkillImg(key, jns.get(key).longValue());
//			threads.add(cpSkImg);
//		}
		
		while(true && !isEnd) {
			// 进图空格跳过选人
			RobotUtil.mouseMove(407, 456);
			RobotUtil.mouseLeft();
			isOver = false;
			while(true && !isEnd) {
				RobotUtil.preceKey(KeyEvent.VK_SPACE);
				if(fightAgain()) {
					RobotUtil.preceKey(KeyEvent.VK_F10);
				}
				// 获取准备，判断开始战斗
				if(isFighting()) {
					break;
				}
			}
			// 提交技能线程至线程池中执行
//			for(int i = 0;i<threads.size();i++){
//				if(threads.get(i).isShutdown) {
//					threads.get(i).setShutdown(false);
//				}
//				executor.submit(threads.get(i));
//			}
			// 循环ASDFG  X 攻击
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
				// 判断本次战斗结束，聚物捡东西
				Color kongXueTiao = RobotUtil.getRobot().getPixelColor(485, 72);
				if(kongXueTiao.getRed() < 5 
						&& kongXueTiao.getGreen() < 5 
						&& kongXueTiao.getBlue() < 5 ) {
					RobotUtil.delay(1000);
					RobotUtil.preceKey(KeyEvent.VK_NUMPAD0);
					boolean isNext = false;
					// 等待对战下一个角色
					while(true && !isEnd) {
						for(int i =0;i<10;i++) {
							RobotUtil.preceKey(KeyEvent.VK_X);
						}
						if(isFighting() || isEnd) {
							break;
						}
						// F11选择其他地下城按钮出现
						Color F11_f = RobotUtil.getRobot().getPixelColor(720, 116);
						Color F11_1 = RobotUtil.getRobot().getPixelColor(728, 116);
						if (isBetween(F11_f.getRed(), 230) && isBetween(F11_f.getGreen(), 200)
								&& isBetween(F11_f.getBlue(), 155) && isBetween(F11_1.getRed(), 230)
								&& isBetween(F11_1.getGreen(), 200) && isBetween(F11_1.getBlue(), 155)) {
							// 结束技能线程
//							for(int i = 0;i<threads.size();i++){
//								threads.get(i).setShutdown(true);
//							}
							// F10 继续战斗，跳转至第一步执行
							if (fightAgain()) {
								RobotUtil.preceKey(KeyEvent.VK_F10);
								RobotUtil.delay(500);
								RobotUtil.preceKey(KeyEvent.VK_F10);
								isNext = true;
								break;
								// 返回城镇
							} else {
								isOver = true;
								break;
							}
						}
					}
					// F10继续战斗或者返回城镇
					if(isOver || isNext || isEnd) {
						break;
					}
				}
			}
			// 本角色战斗结束，F12出图
			if(isOver || isEnd) {
				TxtLog.log("当前角色F12退出");
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
		// F10 继续战斗，跳转至第一步执行
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
	 * @description 判断再次战斗，VS标识存在，且血条不空
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
	 * @description 处理装备，卖白装，分解蓝装
	 * */
	public static void dealEquipment() {
		// 打开副职业面板
		RobotUtil.preceKey(KeyEvent.VK_NUMPAD2);
		BufferedImage bgImage = ImageUtil.priteScreen(277, 241, 248, 25);
		int[] fenjieshi = ImageUtil.locateOnScreen(imgesMap.get("fenjieshi"), bgImage);
		TxtLog.log("fenjieshi x="+fenjieshi[0]+"y="+fenjieshi[1]);
		if(fenjieshi[0] != -1) {
			// 修理分解机
			RobotUtil.mouseMove(480, 335);
			RobotUtil.mouseLeft();
			RobotUtil.delay(500);
			RobotUtil.mouseLeft();
			RobotUtil.delay(500);
			RobotUtil.preceKey(KeyEvent.VK_SPACE);
			// 分解装备
			RobotUtil.mouseMove(410, 335);
			RobotUtil.mouseLeft();
		}else {
			// 关闭副职业面板
			RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
			// 移动至右上角，方便定位分解机
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD4,1000);
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD8,2000);
			RobotUtil.preceKey(KeyEvent.VK_NUMPAD6,2000);
			// 打开装备分解机，分解蓝装
			RobotUtil.mouseMove(146, 326);
			RobotUtil.mouseLeft();
			// 选择分解菜单
			RobotUtil.mouseMove(185, 380);
			RobotUtil.mouseLeft();
		}
		// 单击全部分解按钮并确定
		RobotUtil.preceKey(KeyEvent.VK_A);
		RobotUtil.preceKey(KeyEvent.VK_ENTER);
		// 等待5秒分解完成，关闭分级机
		RobotUtil.delay(4000);
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	
	/**
	 * @description 存东西到金库
	 * @param saveMoney 是否存钱
	 * @param saveMaterial 是否存材料
	 * */
	public static void saveMoney(boolean saveMoney,boolean saveMaterial) {
		if(!saveMoney && !saveMaterial) {
			return;
		}
		// 打开角色选择界面
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
		// 打开金库
		RobotUtil.mouseMove(250, 320);
		RobotUtil.mouseLeft();
		RobotUtil.preceKey(KeyEvent.VK_A);
		RobotUtil.preceKey(KeyEvent.VK_SPACE);
		
		// 打开账户金库
		RobotUtil.mouseMove(zhjinku[0] + 20, zhjinku[1] + 6);
		RobotUtil.mouseLeft();
		// 存材料
		if(saveMaterial) {
			RobotUtil.preceKey(KeyEvent.VK_A);
			RobotUtil.preceKey(KeyEvent.VK_SPACE);
		}
		// 存金币，确定
		if(saveMoney) {
			RobotUtil.mouseMove(153, 428);
			RobotUtil.mouseLeft();
			RobotUtil.mouseLeft();
		}
		// 关闭菜单
		RobotUtil.preceKey(KeyEvent.VK_ESCAPE);
		RobotUtil.delay(1000);
	}
	
	/**
	 * @description 换角色
	 * @param roleNum 第几角色
	 * */
	public static boolean changeRole(int roleNum) {
		// 打开角色选择界面
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
		// 循环将角色栏拉到最上方
		if(roleNum == 0) {
			RobotUtil.mouseMove(790, 342);
			for(int i = 0;i < 8;i++) {
				RobotUtil.mouseLeft();
			}
			++roleNum;
		}
		// 计算下一个角色的点击位置
		int n = (roleNum - 1) % 6;
		// 下一排角色
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
		// 每个角色耗时25分钟，totalTime为所有角色总耗时（毫秒）
		try {
			java.lang.Runtime.getRuntime().exec( "shutdown -s -t " + (roleNum * 25 * 60));
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
}
