package DnfHelper;

public class CompareSingleSkillImg implements Runnable {
	/**
	 * 是否关闭
	 */
	boolean isShutdown;
	
	/**
	 * 间隔时间
	 */
	private long delayTime;
	
	/**
	 * 键编码
	 */
	private String keyCode;
	
	/**
	 * @param keyCode 技能键码
	 */
	public CompareSingleSkillImg(String keyCode, long delayTime) {
		isShutdown = false;
		this.keyCode = keyCode;
		this.delayTime = delayTime;
		System.out.println("技能" + keyCode + "间隔时间" + delayTime + "ms,进程构造完成！");
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
