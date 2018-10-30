package io.netty.example.echo.pojo;

import java.io.Serializable;

public class Grade implements Serializable{

	private static final long serialVersionUID = 1L;
	int chinese = 80;
	int math = 90;
	public int getChinese() {
		return chinese;
	}
	public void setChinese(int chinese) {
		this.chinese = chinese;
	}
	public int getMath() {
		return math;
	}
	public void setMath(int math) {
		this.math = math;
	}
}
