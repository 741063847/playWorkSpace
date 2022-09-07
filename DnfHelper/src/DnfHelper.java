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
	
	// ��ݼ�1,2,3��
	public static final int GLOBAL_HOT_KEY_0 = 0;
	public static final int GLOBAL_HOT_KEY_7 = 7;
	public static final int GLOBAL_HOT_KEY_8 = 8;
	public static final int GLOBAL_HOT_KEY_9 = 9;
	private JPanel contentPane;
	// �Զ�������ɫ
	private boolean changeRole = true;
	// ��ȡ����
	private boolean getMaterial = true;
	// �ƶ�������
	private boolean move = true;
	// ��Ʊ
	private boolean buyTicket = true;
	// �л������
	private boolean changeKey = true;
	// ��PL����
	private boolean noPlGoOver = true;
	// ս��
	private boolean fight = true;
	// ����װ��
	private boolean dealEqualition = true;
	// �����
	private boolean saveMaterial= true;
	// �����
	private boolean saveMoney = false;
	// ��ɹػ�
	private boolean shutdown= true;
	// ѭ��ս��
	private boolean xuhuan = false;
	// ��ʼ��ɫ
	private int startNum = 1;
	// ������ɫ
	private int endNum = 25;
	// ��ǰ��ɫƱ����
	int piaoNum = 0;
	// ˢͼ��ʼ
	private boolean startFighing = false;
	// ��ͣ
	private boolean isStop = false;
	// ˢͼ��ʼ
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
		    	// ����λ�ó�ʼ��
				String resetLocation = DnfUtil.resetLocation();
				if(resetLocation != "") {
					txtpnTipsPrint(resetLocation);
					return;
				}
				// �ķ����
				if(changeKey && move) {
					DnfUtil.changeKey();
				}
				// �رղֿ���
				DnfUtil.colseLockTip();
				// ��ȡ������ťλ��
				DnfUtil.publicBtnlocateInit();
				try {
					totalStartTime = System.currentTimeMillis();
					if(shutdown) {
						overTime(endNum - startNum + 1);
					}
					for (int i = startNum; i <= endNum; ) {
						double startTime = System.currentTimeMillis();
						// ����ý�ɫ��PL��ֱ����һ����ɫ
						if(noPlGoOver) {
							Color pila = KeyBoardUtil.getRobot().getPixelColor(657, 584);
							if(pila.getRed() < 10 && pila.getGreen() < 10 
									&& pila.getBlue() < 10) {
								txtpnTipsPrint("��" + i + "����ɫ��PL");
								// ����ɫ
								++i;
								DnfUtil.changeRole(i);
								continue;
							}
						}
						// �رղֿ���
						DnfUtil.colseLockTip();
						TxtLog.log("��" + i + "����ɫ��ʼ");
						// ��Ʊ26��
						piaoNum = DnfUtil.getPiaos();
						txtpnTipsPrint("��" + i + "����ɫ��ʼ,��ǰ��Ʊ" + piaoNum);
						TxtLog.log("��" + i + "����ɫ��ʼ,��ǰ��Ʊ" + piaoNum);
						// ȡ����
						if(buyTicket && getMaterial) {
							DnfUtil.getMaterial(piaoNum);
						}
						// �������Ƿ��䵽������ͼ
						if(move) {
							DnfUtil.homeMoveToDragon(piaoNum, buyTicket);
						}
						// ����ͼս��
						if(fight) {
							DnfUtil.fightDragon();
						}
						if(DnfUtil.isEnd) {
							break;
						}
						// ����װ��
						if(dealEqualition) {
							DnfUtil.dealEquipment();
						}
						// ��Ǯ
						DnfUtil.saveMoney(saveMoney,saveMaterial);
						double endTime = System.currentTimeMillis();
						txtpnTipsPrint("��"+i+"����ɫ��ʱ��"+(endTime - startTime)/1000);
						TxtLog.log("��"+i+"����ɫ��ʱ��"+(endTime - startTime)/1000);
						// ����ɫ
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
					TxtLog.log("�ܺ�ʱ��"+(totalEndTime - totalStartTime) / 1000 / 60 +"����");
					txtpnTipsPrint("�ܺ�ʱ��"+(totalEndTime - totalStartTime) / 1000 / 60 +"����");
					if(shutdown) {
						java.lang.Runtime.getRuntime().exec( "shutdown -s");
					}
				} catch (IOException e) {
					// nothing
				}
		    }
		}
		
		JCheckBox checkBox1 = new JCheckBox("�ķ����");
		checkBox1.setBounds(34, 5, 91, 23);
		checkBox1.setSelected(changeKey);
		checkBox1.addActionListener(actionPerformed -> 
			changeKey = checkBox1.isSelected()
		);
		contentPane.add(checkBox1);
		
		JCheckBox checkBox2 = new JCheckBox("������ɫ");
		checkBox2.setBounds(34, 25, 91, 23);
		checkBox2.setSelected(changeRole);
		checkBox2.addActionListener(actionPerformed -> 
			changeRole = checkBox2.isSelected()
		);
		contentPane.add(checkBox2);
		
		JCheckBox chckbxpl = new JCheckBox("��PL����");
		chckbxpl.setBounds(34, 45, 91, 23);
		chckbxpl.setSelected(noPlGoOver);
		chckbxpl.addActionListener(actionPerformed -> 
			noPlGoOver = chckbxpl.isSelected()
		);
		contentPane.add(chckbxpl);
		
		JCheckBox checkBox3 = new JCheckBox("ȡ����");
		checkBox3.setBounds(34, 65, 104, 23);
		checkBox3.setSelected(getMaterial);
		checkBox3.addActionListener(actionPerformed -> 
			getMaterial = checkBox3.isSelected()
		);
		contentPane.add(checkBox3);
		
		JCheckBox checkBox4 = new JCheckBox("�ƶ�");
		checkBox4.setBounds(34, 85, 104, 23);
		checkBox4.setSelected(move);
		checkBox4.addActionListener(actionPerformed -> 
			move = checkBox4.isSelected()
		);
		contentPane.add(checkBox4);
		
		JCheckBox checkBox5 = new JCheckBox("��Ʊ");
		checkBox5.setBounds(34, 105, 104, 23);
		checkBox5.setSelected(buyTicket);
		checkBox5.addActionListener(actionPerformed -> 
			buyTicket = checkBox5.isSelected()
		);
		contentPane.add(checkBox5);
		
		JCheckBox checkBox6 = new JCheckBox("ս��");
		checkBox6.setBounds(34, 125, 104, 23);
		checkBox6.setSelected(fight);
		checkBox6.addActionListener(actionPerformed -> 
			fight = checkBox6.isSelected()
		);
		contentPane.add(checkBox6);
		
		JCheckBox checkBox7 = new JCheckBox("�������");
		checkBox7.setBounds(34, 145, 104, 23);
		checkBox7.setSelected(dealEqualition);
		checkBox7.addActionListener(actionPerformed -> 
			dealEqualition = checkBox7.isSelected()
		);
		contentPane.add(checkBox7);
		
		JCheckBox checkBox8 = new JCheckBox("��Ǯ");
		checkBox8.setBounds(34, 165, 104, 23);
		checkBox8.setSelected(saveMoney);
		checkBox8.addActionListener(actionPerformed -> 
			saveMoney = checkBox8.isSelected()
		);
		contentPane.add(checkBox8);
		
		JCheckBox checkBox9 = new JCheckBox("�����");
		checkBox9.setBounds(34, 185, 104, 23);
		checkBox9.setSelected(saveMaterial);
		checkBox9.addActionListener(actionPerformed -> 
			saveMaterial = checkBox9.isSelected()
		);
		contentPane.add(checkBox9);
		
		JCheckBox checkBox10 = new JCheckBox("�ػ�");
		checkBox10.setBounds(88, 249, 59, 23);
		checkBox10.setSelected(shutdown);
		checkBox10.addActionListener(actionPerformed -> 
			shutdown = checkBox10.isSelected()
		);
		contentPane.add(checkBox10);
		
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_0, 0, KeyEvent.VK_0);
		
		JButton button1 = new JButton("��ʼ");
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
		
		// ����ȼ�������
		// �ڶ���������ȼ�������
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			public void onHotKey(int markCode) {
				switch (markCode) {
				case GLOBAL_HOT_KEY_7:// ����
					if(isStop) {
						fighting.resume();
					};
					break;
				case GLOBAL_HOT_KEY_8:// ��ͣ
					if(!isStop) {
						fighting.suspend();
					};
					break;
				case GLOBAL_HOT_KEY_9:// ֹͣ
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
				// δ����Ϸ
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
		
		JButton btnNewButton_2 = new JButton("���ھ��");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				txtpnTipsPrint("���ھ����" + DnfUtil.getHwndName());
			}
		});
		btnNewButton_2.setBounds(270, 249, 93, 23);
		contentPane.add(btnNewButton_2);
		
		JCheckBox checkBox10_1 = new JCheckBox("ѭ��");
		checkBox10_1.setBounds(88, 274, 59, 23);
		checkBox10_1.setSelected(xuhuan);
		checkBox10_1.addActionListener(actionPerformed -> 
			xuhuan = checkBox10_1.isSelected()
		);
		contentPane.add(checkBox10_1);
	}
	
	public static void overTime(int roleNum){
		// ÿ����ɫ��ʱ25���ӣ�totalTimeΪ���н�ɫ�ܺ�ʱ�����룩
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
