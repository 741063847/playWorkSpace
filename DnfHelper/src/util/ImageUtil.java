package util;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import DnfHelper.RobotUtil;

public class ImageUtil {

    private ImageUtil() {
    }

    /**
	 * @since 加载资源图片
	 * @param path 图片资源路径
	 * */
	public static Map<String,BufferedImage> loadImages(String path) {
		if(path == null || "".equals(path)) {
			path = "D:/dnfImg";
		}
		File file = new File(path);
		if(!file.exists()) {
			System.out.println("not found file");
			return null;
		}
		File list[] = file.listFiles();
		Map<String,BufferedImage> imges = new HashMap<>();
		try {
			for (File img : list) {
				imges.put(img.getName().replace(".png", ""), ImageIO.read(img));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imges;
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
		return RobotUtil.createScreenCapture(rectangle);// 从当前屏幕中读取的像素图像，该图像不包括鼠标光标
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
	
	/**
	 * @since getXps 获取目标图片在背景图片中的所有横坐标
	 * @param targetImage 目标图片
	 * @param bgImage 背景图片
	 * */
	public static List<Integer> getXps(BufferedImage targetImage, BufferedImage bgImage) {
		List<Integer> result = new ArrayList<>();
		int width = bgImage.getWidth() - targetImage.getWidth();
		int height = bgImage.getHeight() - targetImage.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				BufferedImage image = bgImage.getSubimage(x, y, targetImage.getWidth(), targetImage.getHeight());
				if (compareImge(targetImage, image)) {
					result.add(x);
				}
			}
		}
		return result;
	}
	
	/**
	 * @since isImgExist 目标图片是否在背景图片中存在
	 * @param targetImage 目标图片
	 * @param bgImage 背景图片
	 * @return 
	 * */
	public static boolean isImgExist(BufferedImage targetImage, BufferedImage bgImage) {
		int[] point = locateOnScreen(targetImage, bgImage);
		if(point[0] > -1) {
			return true;
		}
		return false;
	}
}
