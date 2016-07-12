package learn.zhf.test.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.irene.easymusic.dtd.parser.DTDConstants;

import learn.zhf.log.Log;

public class RegularExpressionTest extends TestCase {

	public static final String TAG = "RegularExpressionTest";

	public static final String REGULAR_NAME = "\\w+(-\\w+)*";

	public static final String REGULAR_XML_NAME = "\\s*xml:\\w+\\s*";

	public static final String REGULAR_ENUM_NAMES = "\\(\\s*" + REGULAR_NAME + "(\\s*\\|\\s*" + REGULAR_NAME
			+ "\\s*)*\\s*\\)";

	private static final String REGULAR_ENTITY_REFERENCE = "%\\w+(-\\w+)*;*";

	public static final String REGULAR_ATTRI_NAME = "(" + REGULAR_NAME + "|" + REGULAR_XML_NAME + ")";

	private static final String REGULAR_ATTR_ENUM_VALUE = "("
			+ REGULAR_ENUM_NAMES
			+ "|\\s*"
			+ REGULAR_ENTITY_REFERENCE
			+ "\\s*|\\s*CDATA\\s*|\\s*ID\\s*|\\s*IDREF\\s*|\\s*IDREFS\\s*|\\s*NMTOKEN\\s*|\\s*NMTOKENS\\s*|\\s*NOTATION\\s*)";

	private static final String REGULAR_ATTR_ENUM_EXISTENCE = "(\\s*#REQUIRED\\s*|\\s*#IMPLIED\\s*|\\s*#FIXED\\s*\\w*)?";

	private static final String REGULAR_ATTRI_NAME_VALUE_EXISTENCE = "\\s*" + REGULAR_ATTRI_NAME + "\\s+"
			+ REGULAR_ATTR_ENUM_VALUE + "\\s+" + REGULAR_ATTR_ENUM_EXISTENCE + "\\s*";

	private static final String REGULAR_ATTR_CONTENT = "\\s*<!ATTLIST\\s*("+ REGULAR_NAME + ")\\s*((\\s*" + REGULAR_ATTRI_NAME_VALUE_EXISTENCE + "|"
			+ REGULAR_ENTITY_REFERENCE + "\\s*)+)\\s*>\\s*";

	private static final String REGULAR_ELEMENT_REFERENCE_NAME = "\\s*\\w+(-\\w+)*[\\?\\*\\+]\\s*";
	
	private static final String REGULAR_ELEMENT_SUB_ELEMENT_DEF = REGULAR_ELEMENT_REFERENCE_NAME + ",?\\s*|\\s*"
			+ REGULAR_ENTITY_REFERENCE + "\\s*,?\\s*";

	private static final String REGULAR_ELEMENT_CONTENT = "\\s*<!ELEMENT\\s*(" + REGULAR_NAME + ")\\s*\\(((" + REGULAR_ELEMENT_SUB_ELEMENT_DEF + ")+)\\s*\\)\\s*>\\s*";
	
	
	private static final String REGULAR_DOCTYPE_DECLARETION = "[^/]+(//[^/]+)+";
	
	private static final String REGULAR_FILE_URI = "\\s*.*\\s*|\\s*http.*\\s*";
	
	private static final String REGULAR_ENTITY_DEFINITION_VALUES_ONLY = "\\s*<!ENTITY\\s*%\\s*(" + REGULAR_NAME + ")\\s*\"\\s*(" + REGULAR_ENUM_NAMES +")\\s*\"\\s*>\\s*";
	
	private static final String REGULAR_ENTITY_DEFINITION_WITH_NAME = "\\s*<!ENTITY\\s*%\\s*(" + REGULAR_NAME + ")\\s*\"\\s*((" + REGULAR_ATTRI_NAME_VALUE_EXISTENCE +")+)\\s*\"\\s*>\\s*";
	
	private static final String REGULAR_ENTITY_INSTRUMENT = "\\s*<!ENTITY\\s*%\\s*(" + REGULAR_NAME + ")\\s*\"" + "(\\s*IGNORE|INCLUDE\\s*)" + "\\s*\"\\s*>\\s*";
	
	private static final String REGULAR_ENTITY_DOCUMENT_DEF = "\\s*<!ENTITY\\s*%\\s*(" + REGULAR_NAME + ")\\s*(\\s*PUBLIC|SYSTEM\\s*)\\s*(\\s*\"\\s*" + REGULAR_DOCTYPE_DECLARETION + "\"\\s*)?\\s*\"\\s*(" + REGULAR_FILE_URI + ")\\s*\"\\s*>\\s*";

	private static final String REGULAR_DTD_TAG = "\\s*(<!(ENTITY|ATTLIST|ELEMENT)[^<>]+>|<!--[^<>]+-->)";
	
	
	public void run(){
		run6();
	}
	
	
	public void run6(){
		String attrStr = "<!ATTLIST addbb >";
		String elStr = "<!ELEMENT scaling (millimeters, tenths)>";
		String enStr = "<!ENTITY % yyyy-mm-dd \"(#PCDATA)\">";
	}
	
	
	public void run4(){
		String content5 = "separator (none | horizontal | diagonal | vertical | adjacent) #IMPLIED ";
		
		String content7 = "  font-style   CDATA  #IMPLIED ";
		
		String mathcerStr = content5;
		String patternStr = REGULAR_ATTRI_NAME_VALUE_EXISTENCE;
		
		// mathcerStr = "\"IGNORE\"";
		// patternStr = "\\s*\"" + "\\s*(\\s*IGNORE|INCLUDE\\s*)\\s*" + "\\s*\"";
		//patternStr = ".*";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher =  pattern.matcher(mathcerStr);
		Log.d(TAG, "match content-->" + mathcerStr);
		Log.d(TAG, "match regular-->" + patternStr);
		
		Log.d(TAG, "match result-->" + matcher.matches());
		for(int i = 0; i<= matcher.groupCount(); i++){
			Log.d(TAG, "matched groups-->" + i + ";str-->" + matcher.group(i)) ;
		}
	}
	
	
	
	public void run3(){
		String content = " % time-separator \"separator (none | horizontal | diagonal | vertical | adjacent) #IMPLIED \"";
		
		String content8 = "% time-separator \"separator CDATA #IMPLIED \"";
		
		String content6 = "separator (none | horizontal | diagonal | vertical | adjacent) #IMPLIED ";
		
		String content2 = "<!ENTITY % tenths 'CDATA'>";
		
		String content3 = " % start-stop-continue \"(start | stop | continue)\"";
		
		String content4 = " % timewise \"IGNORE\"";
		
		String content5 = "% layout PUBLIC \"-//Recordare//ELEMENTS MusicXML 3.0 Layout//EN\" \"layout.mod\"";
		
		String content7 = " % font \" font-family  CDATA  #IMPLIED font-style   CDATA  #IMPLIED font-size    CDATA  #IMPLIED font-weight  CDATA  #IMPLIED\"";
		
		String mathcerStr = content7;
		String patternStr = REGULAR_ENTITY_DEFINITION_WITH_NAME;
		
		// mathcerStr = "\"IGNORE\"";
		// patternStr = "\\s*\"" + "\\s*(\\s*IGNORE|INCLUDE\\s*)\\s*" + "\\s*\"";
		//patternStr = ".*";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher =  pattern.matcher(mathcerStr);
		Log.d(TAG, "match content-->" + mathcerStr);
		Log.d(TAG, "match regular-->" + patternStr);
		
		Log.d(TAG, "match result-->" + matcher.matches());
		for(int i = 0; i<= matcher.groupCount(); i++){
			Log.d(TAG, "matched groups-->" + i + ";str-->" + matcher.group(i)) ;
		}
	}
	
	
	public void run2() {
		String content1 = "<!ELEMENT attributes (%editorial;, divisions?, key*, time*, "
				+ " staves?, part-symbol?, instruments?, clef*, staff-details*, transpose*, directive*, measure-style*)>";
		
		String content = "attributes (%editorial;, divisions?, key*, time*, "
				+ " staves?, part-symbol?, instruments?, clef*, staff-details*, transpose*, directive*, measure-style*)";
		Matcher matcher = Pattern.compile(REGULAR_ELEMENT_CONTENT).matcher(content);
		if(matcher.matches()){
			for(int i = 0; i <= matcher.groupCount(); i++){
				Log.d(TAG, "match attri content group-->" + i + "; str-->" + matcher.group(i));
			}
		}
	}

	public void run1() {

		// boolean res = Pattern.matches(REGULAR_ENTITY_REFERENCE,
		// " %print-object;");

		String content = "<!ATTLIST staff-details" + " number         CDATA                #IMPLIED "
				+ " show-frets     (numbers | letters)  #IMPLIED " + "type %start-stop; #REQUIRED" + " id ID #REQUIRED"
				+ " %print-object; " + " %print-spacing; " + " xml:lang NMTOKEN #IMPLIED>";

		if (content != null && content.startsWith("<!ATTLIST") && content.endsWith(">")) {
			content = content.substring("<!ATTLIST".length(), content.length() - ">".length());
			Pattern pattern = Pattern.compile(REGULAR_ATTR_CONTENT);
			Matcher matcher = pattern.matcher(content);
			Log.d(TAG, "attri content match result-->" + matcher.matches());
			for(int i = 0; i<= matcher.groupCount(); i++){
				Log.d(TAG, "mathch attri content group-->" + i + ";value-->" + matcher.group(i));
			}
			String elementName = null;
			if (matcher.find()) {
				elementName = matcher.group();
				content = content.substring(matcher.start() + elementName.length());
			} else {

			}

			List<String> attriStrs = null;

			if (elementName != null) {
				pattern = Pattern.compile(REGULAR_ATTR_CONTENT);
				matcher = pattern.matcher(content);
				if (matcher.find()) {
					attriStrs = new ArrayList<String>();
					do {
						String attrStr = matcher.group();
						Log.d(TAG, "catch attri-->" + attrStr);
						attriStrs.add(attrStr);
					} while (matcher.find());
				} else {

				}
			}

			if (attriStrs != null) {
				Pattern attriDefPattern = Pattern.compile(REGULAR_ATTRI_NAME_VALUE_EXISTENCE);
				Pattern entityRefPattern = Pattern.compile(REGULAR_ENTITY_REFERENCE);
				Log.d(TAG, "match REGULAR_ATTRI_NAME_VALUE_EXISTENCE regular string-->\n"
						+ REGULAR_ATTRI_NAME_VALUE_EXISTENCE);
				for (String attrStr : attriStrs) {
					Matcher attriMatcher = attriDefPattern.matcher(attrStr);

					if (attriMatcher.matches()) {
						//Log.d(TAG, "catch attri def-->" + attrStr);
						for (int i = 0; i < attriMatcher.groupCount() + 1; i++) {
							//Log.d(TAG, "catch attri group-->" + i + "; value-->" + attriMatcher.group(i));
						}
					} else if (entityRefPattern.matcher(attrStr).matches()) {
						//Log.d(TAG, "catch attri Ref-->" + attrStr);
					}
				}
			}

		}
	}

}
