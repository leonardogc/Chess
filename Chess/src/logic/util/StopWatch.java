package logic.util;

public class StopWatch {
	private long start;
	public double time;
	
	public StopWatch() {
		this.time = 0;
	}
	
	public void start() {
		this.start = System.nanoTime();
	}
	
	public void stop() {
		this.time+=((double)(System.nanoTime()-this.start))/1000000000;
	}
	
	public void restart() {
		this.time = 0;
	}
}
