package DnfHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.ImageUtil;

public class CompareSkillImg implements Runnable {
	/**
	 * �Ƿ�ر�
	 */
	private boolean isShutdown;
	
	/**
	 * ͼ��
	 */
	private BufferedImage []skillImgs;
	
	private static String[] keyCodes = {"A","S","D","F","G", "Q","W","E","R","T"};
	
	// �̳߳�
	private static ExecutorService executor = Executors.newFixedThreadPool(10);
	
	// ��Ϣ�����߳�
	private static List<CompareSingleSkillImg> threads = new ArrayList<>();
	
	/**
	 * ��ʹ�õļ��ܳ�
	 */
	private Set<String> skillSet;
	
	/**
	 * @param skillImgs ����ͼƬ
	 * @param skillSet ��ʹ�ü��ܳض���
	 */
	public CompareSkillImg(BufferedImage[] skillImgs,Set<String> skillSet) {
		isShutdown = false;
		this.skillImgs = skillImgs;
		this.skillSet = skillSet;
	}
	
	@Override
	public void run() {
		try {
//			BufferedImage bgImg = KeyBoardUtil.priteScreen(298, 532, 156, 63);
//			// ��ʼ�����ܼ�������
//			for(int i = 0;i<skillImgs.length;i++){
//				CompareSingleSkillImg cpSkImg = new CompareSingleSkillImg(skillImgs[i], bgImg, skillSet, keyCodes[i]);
//				executor.submit(cpSkImg); 
//				threads.add(cpSkImg);
//			}
			BufferedImage[] bgImgs = new BufferedImage[10];
			while(!isShutdown){
				// ��ȡ����ͼ�������ж��Ǹ�����CD����
//				bgImg = KeyBoardUtil.priteScreen(298, 532, 156, 63);
				bgImgs = DnfUtil.getSkillImgs(298, 532, 156, 63, 32);
//				CompareSingleSkillImg.bgImg = bgImg;
				for(int i=0;i<keyCodes.length;i++) {
					if(ImageUtil.compareImge(skillImgs[i], bgImgs[i])) {
						System.out.println("������ "+keyCodes[i]);
						skillSet.add(keyCodes[i]);
					}
				}
				Thread.sleep(500);
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the isShutdown
	 */
	public boolean isShutdown() {
		return isShutdown;
	}

	/**
	 * @param isShutdown the isShutdown to set
	 */
	public void setShutdown(boolean isShutdown) {
		this.isShutdown = isShutdown;
		
		if(isShutdown && threads.size() > 0) {
			for(CompareSingleSkillImg thread : threads){
				thread.setShutdown(isShutdown);
			}
		}
		System.out.println("�������ܼ�ؽ��̣�");
	}
}
