package learn.zhf.test;

import java.util.LinkedList;
import java.util.Queue;
import learn.zhf.test.bean.TestCase;

public class TestManager {
	
	private  static TestManager mInstance;
	private volatile Queue<TestCase> mTestCaseQueue;
	private Object mLock;
	private boolean mStopFlag = false;
	private Runnable mTestRunnable = new Runnable() {
		public void run() {
			while(!mStopFlag){
				synchronized(mLock){
					TestCase testCase = mTestCaseQueue.poll();
					if(testCase != null){
						testCase.run();
					}else{
						try {
							mLock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	};
	
	
	private TestManager(){
		mTestCaseQueue = new LinkedList<TestCase>();
		mLock = new Object();
	}

	public static synchronized TestManager getInstance(){
		if(null == mInstance){
			mInstance = new TestManager();
		}
		return mInstance;
	}
	
	public void addCase(TestCase testCase){
		synchronized(mLock){
			mTestCaseQueue.add(testCase);
			mLock.notify();
		}
	}
	
	public void run(){
		mStopFlag = false;
		new Thread(mTestRunnable).start();
	}
	
	public void stop(){
		mStopFlag = true;
	}
	
	public void clear(){
		mStopFlag = true;
		mTestCaseQueue.clear();
		mInstance = null;
	}
}
