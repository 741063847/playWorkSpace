import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

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

import DnfHelper.KeyBoardUtil;
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
	private boolean changeKey = true;
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
	private boolean shutdown= true;
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
	
	private void txtpnTipsPrint(String str) {
		txtpnTips.append(str + "\r\n");
		txtpnTips.setCaretPosition(txtpnTips.getText().length());
	}
	
	/**
	 * Create the frame.
	 */
	public DnfHelper() {
		setTitle("\u5C0F\u5DE5\u5177");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 467, 343);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtpnTips = new JTextArea();
		txtpnTips.setBounds(144, 7, 295, 205);
		txtpnTips.setEditable(false);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(144, 7, 295, 205);
		scrollPane.setViewportView(txtpnTips);
		contentPane.add(scrollPane);
		
		class MyFight implements Runnable {
		    @Override
		    public void run() {
		    	// 窗口位置初始化
				String resetLocation = DnfUtil.resetLocation();
				if(resetLocation != "") {
					txtpnTipsPrint(resetLocation);
					return;
				}
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
						overTime(endNum - startNum + 1);
					}
					for (int i = startNum; i <= endNum; ) {
						double startTime = System.currentTimeMillis();
						// 如果该角色无PL，直接下一个角色
						if(noPlGoOver) {
							Color pila = KeyBoardUtil.getRobot().getPixelColor(657, 584);
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
						TxtLog.log("第" + i + "个角色开始");
						// 买票26张
						piaoNum = DnfUtil.getPiaos();
						txtpnTipsPrint("第" + i + "个角色开始,当前门票" + piaoNum);
						TxtLog.log("第" + i + "个角色开始,当前门票" + piaoNum);
						// 取材料
						if(buyTicket && getMaterial) {
							DnfUtil.getMaterial(piaoNum);
						}
						// 从赛利亚房间到进青龙图
						if(move) {
							DnfUtil.homeMoveToDragon(piaoNum, buyTicket);
						}
						// 青龙图战斗
						if(fight) {
							DnfUtil.fightDragon();
						}
						if(DnfUtil.isEnd) {
							break;
						}
						// 处理装备
						if(dealEqualition) {
							DnfUtil.dealEquipment();
						}
						// 存钱
						DnfUtil.saveMoney(saveMoney,saveMaterial);
						double endTime = System.currentTimeMillis();
						txtpnTipsPrint("第"+i+"个角色耗时："+(endTime - startTime)/1000);
						TxtLog.log("第"+i+"个角色耗时："+(endTime - startTime)/1000);
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
					double totalEndTime = System.currentTimeMillis();
					TxtLog.log("总耗时："+(totalEndTime - totalStartTime) / 1000 / 60 +"分钟");
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
		checkBox1.setBounds(34, 5, 91, 23);
		checkBox1.setSelected(changeKey);
		checkBox1.addActionListener(actionPerformed -> 
			changeKey = checkBox1.isSelected()
		);
		contentPane.add(checkBox1);
		
		JCheckBox checkBox2 = new JCheckBox("更换角色");
		checkBox2.setBounds(34, 25, 91, 23);
		checkBox2.setSelected(changeRole);
		checkBox2.addActionListener(actionPerformed -> 
			changeRole = checkBox2.isSelected()
		);
		contentPane.add(checkBox2);
		
		JCheckBox chckbxpl = new JCheckBox("无PL跳过");
		chckbxpl.setBounds(34, 45, 91, 23);
		chckbxpl.setSelected(noPlGoOver);
		chckbxpl.addActionListener(actionPerformed -> 
			noPlGoOver = chckbxpl.isSelected()
		);
		contentPane.add(chckbxpl);
		
		JCheckBox checkBox3 = new JCheckBox("取材料");
		checkBox3.setBounds(34, 65, 104, 23);
		checkBox3.setSelected(getMaterial);
		checkBox3.addActionListener(actionPerformed -> 
			getMaterial = checkBox3.isSelected()
		);
		contentPane.add(checkBox3);
		
		JCheckBox checkBox4 = new JCheckBox("移动");
		checkBox4.setBounds(34, 85, 104, 23);
		checkBox4.setSelected(move);
		checkBox4.addActionListener(actionPerformed -> 
			move = checkBox4.isSelected()
		);
		contentPane.add(checkBox4);
		
		JCheckBox checkBox5 = new JCheckBox("买票");
		checkBox5.setBounds(34, 105, 104, 23);
		checkBox5.setSelected(buyTicket);
		checkBox5.addActionListener(actionPerformed -> 
			buyTicket = checkBox5.isSelected()
		);
		contentPane.add(checkBox5);
		
		JCheckBox checkBox6 = new JCheckBox("战斗");
		checkBox6.setBounds(34, 125, 104, 23);
		checkBox6.setSelected(fight);
		checkBox6.addActionListener(actionPerformed -> 
			fight = checkBox6.isSelected()
		);
		contentPane.add(checkBox6);
		
		JCheckBox checkBox7 = new JCheckBox("处理材料");
		checkBox7.setBounds(34, 145, 104, 23);
		checkBox7.setSelected(dealEqualition);
		checkBox7.addActionListener(actionPerformed -> 
			dealEqualition = checkBox7.isSelected()
		);
		contentPane.add(checkBox7);
		
		JCheckBox checkBox8 = new JCheckBox("存钱");
		checkBox8.setBounds(34, 165, 104, 23);
		checkBox8.setSelected(saveMoney);
		checkBox8.addActionListener(actionPerformed -> 
			saveMoney = checkBox8.isSelected()
		);
		contentPane.add(checkBox8);
		
		JCheckBox checkBox9 = new JCheckBox("存材料");
		checkBox9.setBounds(34, 185, 104, 23);
		checkBox9.setSelected(saveMaterial);
		checkBox9.addActionListener(actionPerformed -> 
			saveMaterial = checkBox9.isSelected()
		);
		contentPane.add(checkBox9);
		
		JCheckBox checkBox10 = new JCheckBox("关机");
		checkBox10.setBounds(88, 249, 59, 23);
		checkBox10.setSelected(shutdown);
		checkBox10.addActionListener(actionPerformed -> 
			shutdown = checkBox10.isSelected()
		);
		contentPane.add(checkBox10);
		
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_0, 0, KeyEvent.VK_0);
		
		JButton button1 = new JButton("开始");
		button1.setBounds(17, 249, 65, 23);
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
		
		JSpinner spinner2 = new JSpinner();
		spinner2.setBounds(18, 215, 50, 25);
		spinner2.setValue(startNum);
		spinner2.addChangeListener(a -> startNum = (int) spinner2.getValue());
		contentPane.add(spinner2);
		
		JSpinner spinner1 = new JSpinner();
		spinner1.setBounds(88, 215, 50, 25);
		spinner1.setValue(25);
		spinner1.addChangeListener(a -> endNum = (int) spinner1.getValue());
		contentPane.add(spinner1);
		
		JLabel label = new JLabel("-");
		label.setBounds(72, 214, 28, 22);
		contentPane.add(label);
		
		JButton btnNewButton = new JButton("\u7A97\u53E3\u590D\u4F4D");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 未打开游戏
				String resetLocation = DnfUtil.resetLocation();
				if(resetLocation != "") {
					txtpnTipsPrint(resetLocation);
				}
			}
		});
		btnNewButton.setBounds(150, 249, 91, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("\u70ED\u952E\uFF1A7\u7EE7\u7EED 8\u6682\u505C 9\u505C\u6B62");
		lblNewLabel.setBounds(241, 223, 160, 28);
		contentPane.add(lblNewLabel);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(260, 272, 50, -11);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("窗口句柄");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				txtpnTipsPrint("窗口句柄：" + DnfUtil.getHwndName());
			}
		});
		btnNewButton_2.setBounds(270, 249, 93, 23);
		contentPane.add(btnNewButton_2);
		
		JCheckBox checkBox10_1 = new JCheckBox("循环");
		checkBox10_1.setBounds(88, 274, 59, 23);
		checkBox10_1.setSelected(xuhuan);
		checkBox10_1.addActionListener(actionPerformed -> 
			xuhuan = checkBox10_1.isSelected()
		);
		contentPane.add(checkBox10_1);
	}
	
	public static void overTime(int roleNum){
		// 每个角色耗时25分钟，totalTime为所有角色总耗时（毫秒）
		try {
			java.lang.Runtime.getRuntime().exec( "shutdown -s -t " + (roleNum * 25 * 60));
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
}
