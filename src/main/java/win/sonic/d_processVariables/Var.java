package win.sonic.d_processVariables;

import java.io.Serializable;

public class Var implements Serializable {
	/** 固定序列化id */
	private static final long serialVersionUID = -6861432963497907648L;
	// 需要序列化才能被支持

	private int day;
	private String name;
	private String reason;

	/**
	 * 获得 day
	 * 
	 * @return day int对象
	 */
	public int getDay() {
		return day;
	}

	/**
	 * 设置 day
	 * 
	 * @param day
	 *            int对象
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * 获得 name
	 * 
	 * @return name String对象
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置 name
	 * 
	 * @param name
	 *            String对象
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获得 reason
	 * 
	 * @return reason String对象
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * 设置 reason
	 * 
	 * @param reason
	 *            String对象
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Var [day=" + day + ", name=" + name + ", reason=" + reason + "]";
	}

}
