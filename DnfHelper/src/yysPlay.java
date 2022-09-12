import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;

import DnfHelper.KeyBoardUtil;

public class yysPlay extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	
	private boolean hunStart = false;
	private boolean clickStart = false;
	JTextArea txtpnTips;
	
	private Thread clickFun = new Thread(new ClickFun());
	
	private static Map<String,BufferedImage> imgesMap = new HashMap<>();
	
	/**
	 * @since 加载资源图片
	 * */
	private static void loadImages() {
		String path = System.getProperty("user.dir") + "/yysImgs";
		File file = new File(path);
		if(!file.exists()) {
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
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					yysPlay frame = new yysPlay();
					frame.setVisible(true);
					loadImages();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public yysPlay() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 295, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnNewButton = new JButton("御魂开始");
		btnNewButton.setBounds(10, 29, 93, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hunStart = true;
				Thread fighting = new Thread(new YuHunFight());
				fighting.start();
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("御魂结束");
		btnNewButton_1.setBounds(10, 62, 93, 23);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hunStart = false;
			}
		});
		contentPane.add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBounds(185, 30, 66, 21);
		textField.setText("1500");
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u70B9\u51FB\u95F4\u9694             ms");
		lblNewLabel.setBounds(131, 34, 189, 15);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton_2 = new JButton("开始");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickStart = true;
				clickFun.start();
			}
		});
		btnNewButton_2.setBounds(126, 62, 93, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("结束");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickStart = false;
			}
		});
		btnNewButton_3.setBounds(126, 95, 93, 23);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("窗口重置");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetLocation();
			}
		});
		btnNewButton_4.setBounds(10, 95, 93, 23);
		contentPane.add(btnNewButton_4);
		
		txtpnTips = new JTextArea();
		txtpnTips.setBounds(10, 128, 260, 123);
		txtpnTips.setEditable(false);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 128, 260, 123);
		scrollPane.setViewportView(txtpnTips);
		contentPane.add(scrollPane);
	}
	
	/**
	 * 日志输出
	 * */
	private void txtpnTipsPrint(String str) {
		txtpnTips.append(str + "\r\n");
//		System.out.println(str);
		txtpnTips.setCaretPosition(txtpnTips.getText().length());
	}
	
	/**
	 * 换御魂
	 * */
	private void changeYuHun() {
		BufferedImage bgImage = KeyBoardUtil.priteScreen(130, 190, 300, 430);
//		int[] hunshi = KeyBoardUtil.locateOnScreen(imgesMap.get("hunshi.png"), bgImage);
		int[] huntu = KeyBoardUtil.locateOnScreen(imgesMap.get("huntu.png"), bgImage);
		KeyBoardUtil.mouseMove(740, 655);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(500);
		KeyBoardUtil.mouseMove(330, 155);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(500);
		if(huntu[0] != -1) {
			KeyBoardUtil.mouseMove(865, 215);
			KeyBoardUtil.mouseLeft();
		}else {
			KeyBoardUtil.mouseMove(865, 350);
			KeyBoardUtil.mouseLeft();
		}
		KeyBoardUtil.getRobot().delay(500);
		KeyBoardUtil.mouseMove(670, 450);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * 清理御魂
	 * */
	private void cleanYuHun() {
		KeyBoardUtil.mouseMove(740, 655);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(500);
		KeyBoardUtil.mouseMove(500, 435);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(500);
		KeyBoardUtil.mouseMove(1050, 600);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(500);
		KeyBoardUtil.mouseMove(800, 560);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(500);
		KeyBoardUtil.mouseMove(850, 625);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(500);
		KeyBoardUtil.mouseMove(660, 450);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(1000);
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		KeyBoardUtil.mouseMove(1050, 255);
		KeyBoardUtil.mouseLeft();
		KeyBoardUtil.getRobot().delay(500);
		for(int i = 0;i<2;i++) {
			KeyBoardUtil.mouseMove(120, 280);
			KeyBoardUtil.getRobot().mousePress(KeyEvent.BUTTON1_MASK);
			KeyBoardUtil.getRobot().delay(2000);
			KeyBoardUtil.getRobot().mouseRelease(KeyEvent.BUTTON1_MASK);
			KeyBoardUtil.getRobot().delay(500);
			KeyBoardUtil.mouseMove(770, 650);
			KeyBoardUtil.mouseLeft();
			KeyBoardUtil.getRobot().delay(3000);
			KeyBoardUtil.mouseLeft();
		}
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
		KeyBoardUtil.getRobot().delay(500);
		KeyBoardUtil.preceKey(KeyEvent.VK_ESCAPE);
	}

	/**
	 * 窗口重置
	 * */
	public void resetLocation() {
		HWND hwnd = User32.INSTANCE.FindWindow(null, "阴阳师 - MuMu模拟器");//
		if (hwnd == null) {
			txtpnTipsPrint("阴阳师未启动！");
		}else{
			WinDef.RECT winRect = new  WinDef.RECT();
		    User32.INSTANCE.GetWindowRect(hwnd, winRect);
	 	    int winWidth = winRect.right-winRect.left;
		    int winHeight = winRect.bottom-winRect.top;
		    User32.INSTANCE.MoveWindow(hwnd, 0, 0, winWidth, winHeight, true);
		}
	}
	
	/**
	 * 魂十魂土，会清理御魂
	 * */
	class YuHunFight implements Runnable {
	    @Override
	    public void run() {
	    	resetLocation();
	    	changeYuHun();
			int num = 0;
			int waitNum = 0;
			boolean waitTime = false;
	    	while(hunStart) {
				++num;
				waitNum = 0;
				while(true) {
					BufferedImage bgImage = KeyBoardUtil.priteScreen(800, 500, 300, 200);
					int[] point1 = KeyBoardUtil.locateOnScreen(imgesMap.get("huntiaozhan.png"), bgImage);
					int[] point2 = KeyBoardUtil.locateOnScreen(imgesMap.get("hunzudui.png"), bgImage);
					if(point1[0] != -1 || point2[0] != -1) {
						KeyBoardUtil.mouseMove(1010, 670);
						KeyBoardUtil.mouseLeft();
						break;
					}
					if(waitNum > 10) {
						break;
					}
					++waitNum;
					KeyBoardUtil.getRobot().delay(1000);
				}
				waitTime = false;
				waitNum = 0;
				while(true) {
					BufferedImage bgImage = KeyBoardUtil.priteScreen(90, 400, 600, 250);
					int[] point1 = KeyBoardUtil.locateOnScreen(imgesMap.get("tongguanshijian.png"), bgImage);
					if(!waitTime && point1[0] != -1) {
						KeyBoardUtil.mouseMove(1010, 500);
						KeyBoardUtil.mouseLeft();
						waitTime = true;
					}
					int[] point2 = KeyBoardUtil.locateOnScreen(imgesMap.get("baoxiang.png"), bgImage);
					if(point2[0] != -1) {
						KeyBoardUtil.mouseMove(1010, 500);
						KeyBoardUtil.mouseLeft();
						break;
					}
					if(waitNum > 30) {
						break;
					}
					++waitNum;
					KeyBoardUtil.getRobot().delay(1000);
				}
				txtpnTipsPrint("已刷御魂"+num+"次");
				if(num % 5 == 0) {
					KeyBoardUtil.getRobot().delay(2000);
					cleanYuHun();
				}
			}
	    	//208 630 通关时间
	    	// 641 591 宝箱
	    }
	}
	
	/**
	 * 单击功能
	 * */
	class ClickFun implements Runnable {
	    @Override
	    public void run() {
	    	KeyBoardUtil.getRobot().delay(2000);
	    	int num = 0;
			while(clickStart) {
				BufferedImage bgImage = KeyBoardUtil.priteScreen(90, 400, 600, 250);
				KeyBoardUtil.mouseLeft();
				int[] point2 = KeyBoardUtil.locateOnScreen(imgesMap.get("baoxiang.png"), bgImage);
				if(point2[0] != -1) {
					num++;
					txtpnTipsPrint("已刷御魂"+num+"次");
					if(num >= 300) {
						KeyBoardUtil.getRobot().delay(2000);
						cleanYuHun();
						num = 0;
						KeyBoardUtil.mouseMove(1010, 670);
					}
				}
				KeyBoardUtil.getRobot().delay(Integer.parseInt(textField.getText()));
			}
	    }
	}
}
