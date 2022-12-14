import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;

import DnfHelper.DnfUtil;
import DnfHelper.RobotUtil;
import DnfHelper.TxtLog;

public class DnfHelper extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	// 快捷键1,2,3个
	public static final int GLOBAL_HOT_KEY_0 = 0;
	public static final int GLOBAL_HOT_KEY_7 = 7;
	public static final int GLOBAL_HOT_KEY_8 = 8;
	public static final int GLOBAL_HOT_KEY_9 = 9;
	private JPanel contentPane;
	// 自动更换角色
	private boolean changeRole = true;
	// 获取材料
	private boolean getMaterial = true;
	// 移动至黄龙
	private boolean move = true;
	// 买票
	private boolean buyTicket = true;
	// 切换方向键
	private boolean changeKey = false;
	// 无PL跳过
	private boolean noPlGoOver = true;
	// 战斗
	private boolean fight = true;
	// 处理装备
	private boolean dealEqualition = true;
	// 存材料
	private boolean saveMaterial= true;
	// 存材料
	private boolean saveMoney = false;
	// 完成关机
	private boolean shutdown= false;
	// 循环战斗
	private boolean xuhuan = false;
	// 开始角色
	private int startNum = 1;
	// 结束角色
	private int endNum = 25;
	// 当前角色票数量
	int piaoNum = 0;
	// 刷图开始
	private boolean startFighing = false;
	// 暂停
	private boolean isStop = false;
	// 刷图开始
	double totalStartTime = System.currentTimeMillis();
	
	JTextArea txtpnTips;
	
	Thread fighting;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DnfHelper frame = new DnfHelper();
					frame.setVisible(true);
				} catch (Exception e) {
					// nothing
				}
			}
		});
	}
	
	// 购买刷图点数
	class buyPoints implements Runnable {
	    @Override
	    public void run() {
	    	DnfUtil.resetLocation();
	    	for (int i = startNum; i <= endNum; ) {
				DnfUtil.getFightPoint();
				// 换角色
				if(changeRole) {
					if(xuhuan && i == endNum) {
						i = 0;
					}else {
						++i;
					}
					DnfUtil.changeRole(i);
				}
			}
	    	txtpnTipsPrint("刷图点购买结束！");
	    }
	}
	
	public void txtpnTipsPrint(String str) {
		txtpnTips.append(str + "\r\n");
		txtpnTips.setCaretPosition(txtpnTips.getText().length());
		// 打印日志文件
		TxtLog.log(str);
	}
	
	/**
	 * Create the frame.
	 */
	public DnfHelper() {
		setTitle("\u5C0F\u5DE5\u5177");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(820, 50, 329, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtpnTips = new JTextArea();
		txtpnTips.setBounds(6, 6, 300, 220);
		txtpnTips.setEditable(false);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 6, 300, 220);
		scrollPane.setViewportView(txtpnTips);
		contentPane.add(scrollPane);
		
		class MyFight implements Runnable {
		    @Override
		    public void run() {
				// 改方向键
				if(changeKey && move) {
					DnfUtil.changeKey();
				}
				// 关闭仓库锁
				DnfUtil.colseLockTip();
				// 获取公共按钮位置
				DnfUtil.publicBtnlocateInit();
				try {
					totalStartTime = System.currentTimeMillis();
					if(shutdown) {
						DnfUtil.overTime(endNum - startNum + 1);
					}
					boolean isContinue = true;
					for (int i = startNum; i <= endNum; ) {
						double startTime = System.currentTimeMillis();
						// 如果该角色无PL，直接下一个角色
						if(noPlGoOver) {
							Color pila = RobotUtil.getRobot().getPixelColor(657, 584);
							if(pila.getRed() < 10 && pila.getGreen() < 10 
									&& pila.getBlue() < 10) {
								txtpnTipsPrint("第" + i + "个角色无PL");
								// 换角色
								++i;
								DnfUtil.changeRole(i);
								continue;
							}
						}
						// 关闭仓库锁
						DnfUtil.colseLockTip();
						// 买票26张
						piaoNum = DnfUtil.getPiaos();
						txtpnTipsPrint("第" + i + "个角色开始,当前门票" + piaoNum);
						// 取材料
						if(buyTicket && getMaterial) {
							DnfUtil.getMaterial(piaoNum);
						}
						// 从赛利亚房间到进青龙图
						if(move && isContinue) {
							isContinue = DnfUtil.homeMoveToDragon(piaoNum, buyTicket);
						}
						// 青龙图战斗
						if(fight && isContinue) {
							isContinue = DnfUtil.fightDragon();
						}
						// 处理装备
						if(dealEqualition && isContinue) {
							DnfUtil.dealEquipment();
						}
						// 存钱
						DnfUtil.saveMoney(saveMoney,saveMaterial);
						double endTime = System.currentTimeMillis();
						txtpnTipsPrint("第"+i+"个角色耗时："+(endTime - startTime)/1000);
						// 换角色
						if(changeRole) {
							if(xuhuan && i == endNum) {
								i = 0;
							}else {
								++i;
							}
							isContinue = DnfUtil.changeRole(i);
							if(!isContinue) {
								--i;
							}
						}
					}
					double totalEndTime = System.currentTimeMillis();
					txtpnTipsPrint("总耗时："+(totalEndTime - totalStartTime) / 1000 / 60 +"分钟");
					if(shutdown) {
						java.lang.Runtime.getRuntime().exec( "shutdown -s");
					}
				} catch (IOException e) {
					// nothing
				}
		    }
		}
		
		JCheckBox checkBox1 = new JCheckBox("改方向键");
		checkBox1.setBounds(3, 230, 90, 25);
		checkBox1.setSelected(changeKey);
		checkBox1.addActionListener(actionPerformed -> 
			changeKey = checkBox1.isSelected()
		);
		contentPane.add(checkBox1);
		
		JCheckBox checkBox2 = new JCheckBox("更换角色");
		checkBox2.setBounds(95, 230, 90, 25);
		checkBox2.setSelected(changeRole);
		checkBox2.addActionListener(actionPerformed -> 
			changeRole = checkBox2.isSelected()
		);
		contentPane.add(checkBox2);
		
		JCheckBox chckbxpl = new JCheckBox("无PL跳过");
		chckbxpl.setBounds(211, 230, 90, 25);
		chckbxpl.setSelected(noPlGoOver);
		chckbxpl.addActionListener(actionPerformed -> 
			noPlGoOver = chckbxpl.isSelected()
		);
		contentPane.add(chckbxpl);
		
		JCheckBox checkBox3 = new JCheckBox("取材料");
		checkBox3.setBounds(3, 255, 90, 25);
		checkBox3.setSelected(getMaterial);
		checkBox3.addActionListener(actionPerformed -> 
			getMaterial = checkBox3.isSelected()
		);
		contentPane.add(checkBox3);
		
		JCheckBox checkBox5 = new JCheckBox("买票");
		checkBox5.setBounds(95, 255, 90, 25);
		checkBox5.setSelected(buyTicket);
		checkBox5.addActionListener(actionPerformed -> 
			buyTicket = checkBox5.isSelected()
		);
		contentPane.add(checkBox5);
		
		JCheckBox checkBox4 = new JCheckBox("移动");
		checkBox4.setBounds(211, 255, 90, 25);
		checkBox4.setSelected(move);
		checkBox4.addActionListener(actionPerformed -> 
			move = checkBox4.isSelected()
		);
		contentPane.add(checkBox4);
		
		JCheckBox checkBox6 = new JCheckBox("战斗");
		checkBox6.setBounds(3, 280, 90, 25);
		checkBox6.setSelected(fight);
		checkBox6.addActionListener(actionPerformed -> 
			fight = checkBox6.isSelected()
		);
		contentPane.add(checkBox6);
		
		JCheckBox checkBox7 = new JCheckBox("处理材料");
		checkBox7.setBounds(95, 280, 90, 25);
		checkBox7.setSelected(dealEqualition);
		checkBox7.addActionListener(actionPerformed -> 
			dealEqualition = checkBox7.isSelected()
		);
		contentPane.add(checkBox7);
		
		JCheckBox checkBox8 = new JCheckBox("存钱");
		checkBox8.setBounds(211, 280, 90, 25);
		checkBox8.setSelected(saveMoney);
		checkBox8.addActionListener(actionPerformed -> 
			saveMoney = checkBox8.isSelected()
		);
		contentPane.add(checkBox8);
		
		JCheckBox checkBox9 = new JCheckBox("存材料");
		checkBox9.setBounds(3, 305, 90, 25);
		checkBox9.setSelected(saveMaterial);
		checkBox9.addActionListener(actionPerformed -> 
			saveMaterial = checkBox9.isSelected()
		);
		contentPane.add(checkBox9);
		
		JCheckBox checkBox10 = new JCheckBox("关机");
		checkBox10.setBounds(95, 305, 90, 25);
		checkBox10.setSelected(shutdown);
		checkBox10.addActionListener(actionPerformed -> 
			shutdown = checkBox10.isSelected()
		);
		contentPane.add(checkBox10);
		
		JCheckBox checkBox10_1 = new JCheckBox("循环");
		checkBox10_1.setBounds(211, 305, 90, 25);
		checkBox10_1.setSelected(xuhuan);
		checkBox10_1.addActionListener(actionPerformed -> 
			xuhuan = checkBox10_1.isSelected()
		);
		contentPane.add(checkBox10_1);
		
		// 开始角色
		JSpinner spinner2 = new JSpinner();
		spinner2.setBounds(6, 330, 50, 25);
		spinner2.setValue(startNum);
		spinner2.addChangeListener(a -> startNum = (int) spinner2.getValue());
		contentPane.add(spinner2);
		
		JLabel label = new JLabel("-");
		label.setBounds(70, 331, 28, 22);
		contentPane.add(label);
		
		// 结束角色
		JSpinner spinner1 = new JSpinner();
		spinner1.setBounds(92, 330, 50, 25);
		spinner1.setValue(25);
		spinner1.addChangeListener(a -> endNum = (int) spinner1.getValue());
		contentPane.add(spinner1);
		
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_0, 0, KeyEvent.VK_0);
		
		JButton button1 = new JButton("开始");
		button1.setBounds(152, 331, 144, 24);
		button1.addActionListener(a -> {
			JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_7, 0, KeyEvent.VK_7);
			JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_8, 0, KeyEvent.VK_8);
			JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_9, 0, KeyEvent.VK_9);
			if(!startFighing) {
				fighting = new Thread(new MyFight());
				fighting.start();
			}
		});
		contentPane.add(button1);
		
		// 添加热键监听器
		// 第二步：添加热键监听器
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			@SuppressWarnings("deprecation")
			public void onHotKey(int markCode) {
				switch (markCode) {
				case GLOBAL_HOT_KEY_7:// 继续
					if(isStop) {
						fighting.resume();
					};
					break;
				case GLOBAL_HOT_KEY_8:// 暂停
					if(!isStop) {
						fighting.suspend();
					};
					break;
				case GLOBAL_HOT_KEY_9:// 停止
					DnfUtil.isEnd = true;
					try {
						java.lang.Runtime.getRuntime().exec( "shutdown -a");
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case GLOBAL_HOT_KEY_0:
					break;
				}
			}
		});
		
		JButton btnNewButton = new JButton("窗口复位");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 未打开游戏
				String resetLocation = DnfUtil.resetLocation();
				if(resetLocation != "") {
					txtpnTipsPrint(resetLocation);
				}
			}
		});
		btnNewButton.setBounds(6, 365, 90, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_3 = new JButton("刷图点");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread buy = new Thread(new buyPoints());
				buy.start();
			}
		});
		btnNewButton_3.setBounds(106, 365, 90, 23);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_2 = new JButton("窗口句柄");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				txtpnTipsPrint("窗口句柄：" + DnfUtil.getHwndName());
			}
		});
		btnNewButton_2.setBounds(206, 365, 90, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_4 = new JButton("活动");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String,Integer> result = DnfUtil.getSsNum();
				for (String key : result.keySet()) {
					txtpnTipsPrint(key + "===" + result.get(key));
				}
			}
		});
		btnNewButton_4.setBounds(6, 390, 90, 23);
		contentPane.add(btnNewButton_4);
		
		JLabel lblNewLabel = new JLabel("热键：7继续 8暂停 9停止");
		lblNewLabel.setBounds(106, 390, 160, 28);
		contentPane.add(lblNewLabel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
}
