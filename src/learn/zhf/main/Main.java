package learn.zhf.main;

import learn.zhf.test.TestManager;
import learn.zhf.test.bean.DTDParseTestCase;
import learn.zhf.test.bean.RegularExpressionTest;

public class Main {
	public static void main(String[] args){
		TestManager manager = TestManager.getInstance();
		manager.addCase(new DTDParseTestCase());
		//manager.addCase(new RegularExpressionTest());
		manager.run();
	}
}
