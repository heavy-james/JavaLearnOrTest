package learn.zhf.test.bean;

import java.io.FileNotFoundException;
import java.io.IOException;

import learn.zhf.log.Log;

import com.irene.easymusic.dtd.parser.DTDParser;

public class DTDParseTestCase extends TestCase{
	private static final String TAG = "DTDParseTestCase";

	@Override
	public void run() {
		DTDParser parser = new DTDParser("D:\\material\\partwise.dtd", "utf-8", "java_class");
		try {
			parser.parse();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "FileNotFoundException-->" + e);
			e.printStackTrace();
		} catch (IOException e) {
			Log.d(TAG, "IOException-->" + e);
			e.printStackTrace();
		}
	}

}
