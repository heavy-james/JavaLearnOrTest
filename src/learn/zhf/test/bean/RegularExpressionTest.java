package learn.zhf.test.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import learn.zhf.log.Log;

public class RegularExpressionTest extends TestCase {

	public static final String TAG = "RegularExpressionTest";

	public static final String REGULAR_NAME = "\\w+(-\\w+)*";

	public static final String REGULAR_XML_NAME = "\\s*xml:\\w+\\s*";

	public static final String REGULAR_ENUM_NAMES = "\\(\\s*" + REGULAR_NAME + "\\s*(\\|\\s*" + REGULAR_NAME
			+ "\\s*)*\\s*\\)";

	private static final String REGULAR_ENTITY_REFERENCE = "\\s*%\\w+(-\\w+)*;\\s*";

	public static final String REGULAR_ATTRI_NAME = "(" + REGULAR_NAME + "|" + REGULAR_XML_NAME + ")";

	private static final String REGULAR_ATTR_ENUM_VALUE = "("
			+ REGULAR_ENUM_NAMES
			+ "|"
			+ REGULAR_ENTITY_REFERENCE
			+ "|\\s*CDATA\\s*|\\s*ID\\s*|\\s*IDREF\\s*|\\s*IDREFS\\s*|\\s*NMTOKEN\\s*|\\s*NMTOKENS\\s*|\\s*NOTATION\\s*)";

	private static final String REGULAR_ATTR_ENUM_EXISTENCE = "(\\s*#REQUIRED\\s*|\\s*#IMPLIED\\s*|\\s*#FIXED\\s*\\w*)";

	private static final String REGULAR_ATTRI_NAME_VALUE_EXISTENCE = "\\s*" + REGULAR_ATTRI_NAME + "\\s+"
			+ REGULAR_ATTR_ENUM_VALUE + "\\s+" + REGULAR_ATTR_ENUM_EXISTENCE + "\\s*";

	private static final String REGULAR_ATTR_CONTENT = "(\\s*" + REGULAR_ATTRI_NAME_VALUE_EXISTENCE + "|"
			+ REGULAR_ENTITY_REFERENCE + "\\s*)";

	private static final String REGULAR_ELEMENT_REFERENCE_NAME = "\\s+\\w+(-\\w+)[\\?\\*\\+]\\s*,";

	private static final String REGULAR_ELEMENT_CONTENT = "\\s*" + REGULAR_NAME + "\\s*(" + REGULAR_ELEMENT_REFERENCE_NAME + "|"
			+ REGULAR_ENTITY_REFERENCE + ",)+\\s*";

	@Override
	public void run() {
		String testString = "<!ELEMENT attributes (%editorial;, divisions?, key*, time*, "
				+ " staves?, part-symbol?, instruments?, clef*, staff-details*, transpose*, directive*, measure-style*)>";
		Matcher matcher = Pattern.compile(REGULAR_NAME).matcher(testString);
		if(matcher.find()){
			String elementName = matcher.group();
			Log.d(TAG, "find element name-->" + elementName);
			testString = testString.substring(matcher.start() + elementName.length());
		}
		matcher = Pattern.compile(REGULAR_ELEMENT_CONTENT).matcher(testString);
	}

	public void run1() {

		// boolean res = Pattern.matches(REGULAR_ENTITY_REFERENCE,
		// " %print-object;");

		String content = "<!ATTLIST staff-details" + " number         CDATA                #IMPLIED "
				+ " show-frets     (numbers | letters)  #IMPLIED " + "type %start-stop; #REQUIRED" + " id ID #REQUIRED"
				+ " %print-object; " + " %print-spacing; " + " xml:lang NMTOKEN #IMPLIED>";

		if (content != null && content.startsWith("<!ATTLIST") && content.endsWith(">")) {
			content = content.substring("<!ATTLIST".length(), content.length() - ">".length());
			Pattern pattern = Pattern.compile(REGULAR_NAME);
			Matcher matcher = pattern.matcher(content);
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
						Log.d(TAG, "catch attri def-->" + attrStr);
						for (int i = 0; i < attriMatcher.groupCount() + 1; i++) {
							Log.d(TAG, "catch attri group-->" + i + "; value-->" + attriMatcher.group(i));
						}
					} else if (entityRefPattern.matcher(attrStr).matches()) {
						Log.d(TAG, "catch attri Ref-->" + attrStr);
					}
				}
			}

		}
	}

}
