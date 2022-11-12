import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import DnfHelper.DnfUtil;

public class test {

	public static void main(String[] args) {
		try {
			System.out.println("=="+(Double.valueOf("9").longValue()));
			System.out.println("=="+Long.parseLong("9"));
			System.out.println("=="+(Long.parseLong("9")==Double.valueOf("9").longValue()));
//			File file = new File("D:/dnfImg/in");
//			File list[] = file.listFiles();
//			for (File imgFile : list) {
//				BufferedImage img = DnfUtil.changeImgColor(ImageIO.read(imgFile));
//				File outImgFile = new File("D:/dnfImg/out/"+imgFile.getName());
//				ImageIO.write(img, "png", outImgFile);
//			}
			
//			File imgFile = new File("D:/dnfImg/out/lengqueshijian.png");
//			BufferedImage image = ImageIO.read(imgFile);
//			File imgFile1 = new File("D:/dnfImg/out/0-0.png");
//			BufferedImage image1 = ImageIO.read(imgFile1);
//			int[] tt = ImageUtil.locateOnScreen(image, image1);
//			System.out.println(tt[0]+"=="+tt[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
