package learn.zhf.test.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import learn.zhf.log.Log;


public class RegularExpressionTest extends TestCase{
	
	public static final String TAG = "RegularExpressionTest";
	
	public static final String REGULAR_NAME = "(\\w+(-\\w+)*)";
	
	private static final String REGULAR_ENUM_ATTR_TYPE = "(\\(\\s*" + REGULAR_NAME  + "\\s*(\\|\\s*" + REGULAR_NAME + "\\s*)*\\s*\\)"
			+"|\\s*CDATA\\s*|\\s*ID\\s*|\\s*IDREF\\s*|\\s*IDREFS\\s*|\\s*NMTOKEN\\s*|\\s*NMTOKENS\\s*|\\s*NOTATION\\s*|\\s*xml:\\w+\\s*)";
	
	private static final String REGULAR_ENUM_ATTR_VALUE = "(\\(\\s*" + REGULAR_NAME  + "\\s*(\\|\\s*" + REGULAR_NAME + "\\s*)*\\s*\\)|\\s*#REQUIRED\\s*|\\s*#IMPLIED\\s*|\\s*#FIXED\\s*\\w*)";
	
	private static final String REGULAR_ATTRI_NAME_TYPE_VALUE = "(\\s*" + REGULAR_NAME + "\\s+" + REGULAR_ENUM_ATTR_TYPE + "\\s+" + REGULAR_ENUM_ATTR_VALUE + "\\s*)";
	
	private static final String REGULAR_ENTITY_REFERENCE =  "%\\w+(-\\w+)*;";
	
	private static final String REGULAR_ATTR_CONTENT = "(\\s*" + REGULAR_ATTRI_NAME_TYPE_VALUE + "|" + REGULAR_ENTITY_REFERENCE + "\\s*)";
	
	@Override
	public void run() {
		String testString = "<!ATTLIST staff-details" +
				" number         CDATA                #IMPLIED " +
				" show-frets     (numbers | letters)  #IMPLIED " +
				" %print-object; " +
				" %print-spacing; " +
				" xml:lang NMTOKEN #IMPLIED>";
			
		Pattern pattern = Pattern.compile(REGULAR_ATTR_CONTENT);
		
		Matcher matcher =	pattern.matcher(testString);
		boolean find = matcher.find();
		while(find){
			Log.d(TAG, "find attri of element-->" + matcher.group());
			int start = matcher.start();
			//Log.d(TAG, "find attri of element startPos-->" + start);
			int end = matcher.end();
			//Log.d(TAG, "find attri of element endPos-->" + end);
			//testString = testString.substring(start + matcher.group().length());
			find = matcher.find();
		}
		Log.d(TAG, "matcher result-->" + matcher.matches());
	}
	
}
