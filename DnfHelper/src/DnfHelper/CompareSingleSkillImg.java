package DnfHelper;

public class CompareSingleSkillImg implements Runnable {
	/**
	 * �Ƿ�ر�
	 */
	boolean isShutdown;
	
	/**
	 * ���ʱ��
	 */
	private long delayTime;
	
	/**
	 * ������
	 */
	private String keyCode;
	
	/**
	 * @param keyCode ���ܼ���
	 */
	public CompareSingleSkillImg(String keyCode, long delayTime) {
		isShutdown = false;
		this.keyCode = keyCode;
		this.delayTime = delayTime;
		System.out.println("����" + keyCode + "���ʱ��" + delayTime + "ms,���̹�����ɣ�");
	}
	
	@Override
	public void run() {
		try {
			while (!isShutdown) {
				RobotUtil.releaseSkill(keyCode);
				Thread.sleep(delayTime);
			}
		} catch (InterruptedException e) {
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
	}
}
