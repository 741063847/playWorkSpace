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
	private boolean changeKey = false;
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
	private boolean shutdown= false;
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
	
	// ����ˢͼ����
	class buyPoints implements Runnable {
	    @Override
	    public void run() {
	    	DnfUtil.resetLocation();
	    	for (int i = startNum; i <= endNum; ) {
				DnfUtil.getFightPoint();
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
	    	txtpnTipsPrint("ˢͼ�㹺�������");
	    }
	}
	
	public void txtpnTipsPrint(String str) {
		txtpnTips.append(str + "\r\n");
		txtpnTips.setCaretPosition(txtpnTips.getText().length());
		// ��ӡ��־�ļ�
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
						DnfUtil.overTime(endNum - startNum + 1);
					}
					boolean isContinue = true;
					for (int i = startNum; i <= endNum; ) {
						double startTime = System.currentTimeMillis();
						// ����ý�ɫ��PL��ֱ����һ����ɫ
						if(noPlGoOver) {
							Color pila = RobotUtil.getRobot().getPixelColor(657, 584);
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
						// ��Ʊ26��
						piaoNum = DnfUtil.getPiaos();
						txtpnTipsPrint("��" + i + "����ɫ��ʼ,��ǰ��Ʊ" + piaoNum);
						// ȡ����
						if(buyTicket && getMaterial) {
							DnfUtil.getMaterial(piaoNum);
						}
						// �������Ƿ��䵽������ͼ
						if(move && isContinue) {
							isContinue = DnfUtil.homeMoveToDragon(piaoNum, buyTicket);
						}
						// ����ͼս��
						if(fight && isContinue) {
							isContinue = DnfUtil.fightDragon();
						}
						// ����װ��
						if(dealEqualition && isContinue) {
							DnfUtil.dealEquipment();
						}
						// ��Ǯ
						DnfUtil.saveMoney(saveMoney,saveMaterial);
						double endTime = System.currentTimeMillis();
						txtpnTipsPrint("��"+i+"����ɫ��ʱ��"+(endTime - startTime)/1000);
						// ����ɫ
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
		checkBox1.setBounds(3, 230, 90, 25);
		checkBox1.setSelected(changeKey);
		checkBox1.addActionListener(actionPerformed -> 
			changeKey = checkBox1.isSelected()
		);
		contentPane.add(checkBox1);
		
		JCheckBox checkBox2 = new JCheckBox("������ɫ");
		checkBox2.setBounds(95, 230, 90, 25);
		checkBox2.setSelected(changeRole);
		checkBox2.addActionListener(actionPerformed -> 
			changeRole = checkBox2.isSelected()
		);
		contentPane.add(checkBox2);
		
		JCheckBox chckbxpl = new JCheckBox("��PL����");
		chckbxpl.setBounds(211, 230, 90, 25);
		chckbxpl.setSelected(noPlGoOver);
		chckbxpl.addActionListener(actionPerformed -> 
			noPlGoOver = chckbxpl.isSelected()
		);
		contentPane.add(chckbxpl);
		
		JCheckBox checkBox3 = new JCheckBox("ȡ����");
		checkBox3.setBounds(3, 255, 90, 25);
		checkBox3.setSelected(getMaterial);
		checkBox3.addActionListener(actionPerformed -> 
			getMaterial = checkBox3.isSelected()
		);
		contentPane.add(checkBox3);
		
		JCheckBox checkBox5 = new JCheckBox("��Ʊ");
		checkBox5.setBounds(95, 255, 90, 25);
		checkBox5.setSelected(buyTicket);
		checkBox5.addActionListener(actionPerformed -> 
			buyTicket = checkBox5.isSelected()
		);
		contentPane.add(checkBox5);
		
		JCheckBox checkBox4 = new JCheckBox("�ƶ�");
		checkBox4.setBounds(211, 255, 90, 25);
		checkBox4.setSelected(move);
		checkBox4.addActionListener(actionPerformed -> 
			move = checkBox4.isSelected()
		);
		contentPane.add(checkBox4);
		
		JCheckBox checkBox6 = new JCheckBox("ս��");
		checkBox6.setBounds(3, 280, 90, 25);
		checkBox6.setSelected(fight);
		checkBox6.addActionListener(actionPerformed -> 
			fight = checkBox6.isSelected()
		);
		contentPane.add(checkBox6);
		
		JCheckBox checkBox7 = new JCheckBox("�������");
		checkBox7.setBounds(95, 280, 90, 25);
		checkBox7.setSelected(dealEqualition);
		checkBox7.addActionListener(actionPerformed -> 
			dealEqualition = checkBox7.isSelected()
		);
		contentPane.add(checkBox7);
		
		JCheckBox checkBox8 = new JCheckBox("��Ǯ");
		checkBox8.setBounds(211, 280, 90, 25);
		checkBox8.setSelected(saveMoney);
		checkBox8.addActionListener(actionPerformed -> 
			saveMoney = checkBox8.isSelected()
		);
		contentPane.add(checkBox8);
		
		JCheckBox checkBox9 = new JCheckBox("�����");
		checkBox9.setBounds(3, 305, 90, 25);
		checkBox9.setSelected(saveMaterial);
		checkBox9.addActionListener(actionPerformed -> 
			saveMaterial = checkBox9.isSelected()
		);
		contentPane.add(checkBox9);
		
		JCheckBox checkBox10 = new JCheckBox("�ػ�");
		checkBox10.setBounds(95, 305, 90, 25);
		checkBox10.setSelected(shutdown);
		checkBox10.addActionListener(actionPerformed -> 
			shutdown = checkBox10.isSelected()
		);
		contentPane.add(checkBox10);
		
		JCheckBox checkBox10_1 = new JCheckBox("ѭ��");
		checkBox10_1.setBounds(211, 305, 90, 25);
		checkBox10_1.setSelected(xuhuan);
		checkBox10_1.addActionListener(actionPerformed -> 
			xuhuan = checkBox10_1.isSelected()
		);
		contentPane.add(checkBox10_1);
		
		// ��ʼ��ɫ
		JSpinner spinner2 = new JSpinner();
		spinner2.setBounds(6, 330, 50, 25);
		spinner2.setValue(startNum);
		spinner2.addChangeListener(a -> startNum = (int) spinner2.getValue());
		contentPane.add(spinner2);
		
		JLabel label = new JLabel("-");
		label.setBounds(70, 331, 28, 22);
		contentPane.add(label);
		
		// ������ɫ
		JSpinner spinner1 = new JSpinner();
		spinner1.setBounds(92, 330, 50, 25);
		spinner1.setValue(25);
		spinner1.addChangeListener(a -> endNum = (int) spinner1.getValue());
		contentPane.add(spinner1);
		
		JIntellitype.getInstance().registerHotKey(GLOBAL_HOT_KEY_0, 0, KeyEvent.VK_0);
		
		JButton button1 = new JButton("��ʼ");
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
		
		// ����ȼ�������
		// �ڶ���������ȼ�������
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			@SuppressWarnings("deprecation")
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
		
		JButton btnNewButton = new JButton("���ڸ�λ");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// δ����Ϸ
				String resetLocation = DnfUtil.resetLocation();
				if(resetLocation != "") {
					txtpnTipsPrint(resetLocation);
				}
			}
		});
		btnNewButton.setBounds(6, 365, 90, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_3 = new JButton("ˢͼ��");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread buy = new Thread(new buyPoints());
				buy.start();
			}
		});
		btnNewButton_3.setBounds(106, 365, 90, 23);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_2 = new JButton("���ھ��");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				txtpnTipsPrint("���ھ����" + DnfUtil.getHwndName());
			}
		});
		btnNewButton_2.setBounds(206, 365, 90, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_4 = new JButton("�");
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
		
		JLabel lblNewLabel = new JLabel("�ȼ���7���� 8��ͣ 9ֹͣ");
		lblNewLabel.setBounds(106, 390, 160, 28);
		contentPane.add(lblNewLabel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}
}
