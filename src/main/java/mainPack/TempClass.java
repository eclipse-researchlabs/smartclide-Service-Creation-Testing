package mainPack;

public class TempClass {
	private String text;
	private int counter;
	private boolean flag;
	
	public TempClass(String sss, int iii, boolean bbb) {
		this.setText(sss);
		this.setCounter(iii);
		this.setFlag(bbb);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
