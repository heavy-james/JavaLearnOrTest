package com.irene.easymusic.dtd.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import learn.zhf.log.Log;

public class DTDDocument {

	public static final String TAG = "DTDDocument";
	private String mFilePath;
	private InputStream mInputStream;
	private BufferedInputStream mBufferedInputStream;
	private byte[] mBuffer = new byte[1024];
	private String mCurrentParseString = "";
	private String mNodeContentString = null;
	private String mEncoding = null;
	private File mFile;
	private Stack<String> mTagStack  = null;
	
	private DTDDocument() {

	}

	public static DTDDocument loadFile(String filePath, String encoding) throws FileNotFoundException, IOException {
		DTDDocument instance = new DTDDocument();
		instance.mTagStack = new Stack<String>();
		instance.mFilePath = filePath;
		instance.mFile = new File(filePath);
		instance.mInputStream = new FileInputStream(instance.mFile);
		instance.mBufferedInputStream = new BufferedInputStream(instance.mInputStream);
		instance.mEncoding = encoding;
		return instance;
	}

	
	private String mLastParseString = null;

	public DTDNode getNextNode() throws DTDException {
		DTDNode node = null;
		Log.d(TAG, "=====DTDDocument getNextNode=====");
		if (mNodeContentString != null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (mNodeContentString == null) {
				return null;
			}
			node = getCommentNode(mNodeContentString);
			if (node == null) {
				node = getAttriNode(mNodeContentString);
			}
			if (node == null) {
				node = getEntityNode(mNodeContentString);
			}
			if (node == null) {
				node = getElementNode(mNodeContentString);
			}
			if (node == null) {
				Log.d(TAG, "get error node data-->" + mNodeContentString);
				Log.d(TAG, "mLastParseString-->" + mLastParseString);
				DTDException e = new DTDException(DTDException.ERROR_CODE_FORMAT,
						DTDException.ERROR_MSG_FORMAT + mCurrentParseString);
				throw e;
			} else {

			}
		} else {
			Log.d(TAG, "getNextNode hasNextNode error, need call hasNextNode() before call getNextNode()");
			throw new DTDException(DTDException.ERROR_CODE_OPERATION_ERROR,
					DTDException.ERROR_MSG_OPERATION_ERROR + ", need call hasNextNode() before call getNextNode()");
		}
		return node;
	}

	public boolean hasNextNode() {
		String nodeContent = null;
		while (nodeContent == null) {
			nodeContent = getNodeContent();
			Log.d(TAG, "get node content-->" + nodeContent);
			if (nodeContent == null) {
				String nextString = readNextString();
				if (nextString != null) {
					mCurrentParseString += nextString;
					mTagStack.clear();
					nodeContent = getNodeContent();
				} else {
					return false;
				}
			}
		}
		mNodeContentString = nodeContent;
		return true;
	}
	
	
	Pattern mNodeStartPattern = Pattern.compile("\\s*<![^-]\\s*");
	Pattern mNodeEndPattern = Pattern.compile("\\s*[^-]>\\s*");
	Pattern mCommentStartPattern = Pattern.compile("\\s*<!--\\s*");
	Pattern mCommentEndPattern = Pattern.compile("\\s*-->\\s*");
	
	Pattern mTagPattern = Pattern.compile("\\s*(<!--|<![^-]|[^-]>|-->)\\s*");
	
	private String getNodeContent(){
		if(mCurrentParseString == null || "".equals(mCurrentParseString)){
			return null;
		}
		Log.d(TAG, "getNodeContent mCurrentParseString-->" + mCurrentParseString);
		Matcher matcher = mTagPattern.matcher(mCurrentParseString);
		int startPos = 0;
		int length = 0;
		while(matcher.find()){
			startPos = matcher.start();
			//Log.d(TAG, "find pos-->" + startPos);
			length = matcher.group(0).length();
			mTagStack.push(matcher.group(0));
			Log.d(TAG, "matched tag, stack push-->" + matcher.group(0));
			if(hasPairTag()){
				mTagStack.clear();
				String content =  mCurrentParseString.substring(0,  startPos + length);
				mCurrentParseString = mCurrentParseString.substring(content.length());
				return content;
			}
		}
		return null;
	}
	
	
	private boolean hasPairTag(){
		boolean needMatch = true;
		while(mTagStack.size() > 1 && needMatch){
			String lastTag = mTagStack.pop();
			//Log.d(TAG, "hasPairTag lastTag-->" + lastTag);
			String preTag = mTagStack.peek();
			//Log.d(TAG, "hasPairTag preTag-->" + preTag);
			boolean matched = false;
			if(mNodeStartPattern.matcher(preTag).matches() && mNodeEndPattern.matcher(lastTag).matches()){
				matched = true;
			}
			if(mCommentStartPattern.matcher(preTag).matches() && mCommentEndPattern.matcher(lastTag).matches()){
				matched = true;
			}
			if(matched){
				mTagStack.pop();
				Log.d(TAG, "matched tag, stack size-->" + mTagStack.size());
			}else{
				mTagStack.push(lastTag);
				needMatch = false;
			}
		}
		if(mTagStack.size() == 0){
			Log.d(TAG, "hasPairTag res-->" + true);
			return true;
		}
		Log.d(TAG, "hasPairTag res-->" + false);
		return false;
	}
	

	private String readNextString() {
		if (mBufferedInputStream != null) {
			try {
				int length = mBufferedInputStream.read(mBuffer);
				if (length != -1) {
					return new String(mBuffer, mEncoding);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public void release() throws IOException {
		if (mBufferedInputStream != null) {
			mBufferedInputStream.close();
		}
		if (mInputStream != null) {
			mInputStream.close();
		}
	}


	private DTDNode getAttriNode(String content) throws DTDException {

		DTDNode node = null;
		if (content != null) {
			Matcher matcher = DTDConstants.Patterns.getAttriContentPattern().matcher(content);
			String elementName = null;
			if (matcher.matches()) {
				elementName = matcher.group(1);
				String defStr = matcher.group(3);
				node = new DTDNode(DTDNode.NODE_TYPE_ATTRIBUTE, elementName, mFilePath);
				node.setStringData(defStr);
			}

			/*
			List<String> attriStrs = null;

			if (elementName != null) {
				pattern = Pattern.compile(REGULAR_ATTR_CONTENT);
				matcher = pattern.matcher(content);
				if (matcher.find()) {
					attriStrs = new ArrayList<String>();
					do {
						String attrStr = matcher.group();
						attriStrs.add(attrStr);
					} while (matcher.find());
				} else {
					throw new DTDException(DTDException.ERROR_CODE_DEFINITION_NOT_FOUND,
							DTDException.ERROR_MSG_DEFINITION_NOT_FOUND + ",no attri found in attri list");

				}
			}

			if (attriStrs != null) {
				Pattern attriDefPattern = Pattern.compile(REGULAR_ATTRI_NAME_VALUE_EXISTENCE);
				Pattern entityRefPattern = Pattern.compile(REGULAR_ENTITY_REFERENCE);
				node = new DTDNode(DTDNode.NODE_TYPE_ATTRIBUTE, elementName, mFilePath);

				for (String attrStr : attriStrs) {
					Matcher attriMatcher = attriDefPattern.matcher(attrStr);
					String attrValue = attriMatcher.group(3);
					if (attriMatcher.matches()) {
						AttriInfo attri = null;
						if (Pattern.matches(REGULAR_ENUM_NAMES, attrValue)) {
							// 鍊煎畾涔�
							attri = new AttriInfo(elementName, DTDConstants.DataType.TEXT, attriMatcher.group(1));
							String[] values = attrValue.substring("(".length(), attrValue.length() - ")".length())
									.split("|");
							for (String value : values) {
								// 姝ゅ鍖呭惈浜嗚寮曠敤鐨勫疄浣撳悕绉帮紝鍚庣画杩橀渶瑕佸鐞�
								attri.addAttriValue(value);
							}

						} else if (Pattern.matches(REGULAR_ENTITY_REFERENCE, attrValue)) {
							// 瀹炰綋寮曠敤
							attri = new AttriInfo(elementName, DTDConstants.DataType.ENTITY_REFERENCE, attriMatcher.group(1));
							DTDNode entityRef = new DTDNode(DTDNode.NODE_TYPE_ENTITY_REFERENCE, attrValue, mFilePath);
							node.addChild(entityRef);

						} else {
							// 鍥哄畾绫诲瀷
							attri = new AttriInfo(elementName,DTDConstants.DataType.parseType(attrValue), attrValue);

						}
					} else if (entityRefPattern.matcher(attrStr).matches()) {
						DTDNode entityRef = new DTDNode(DTDNode.NODE_TYPE_ENTITY_REFERENCE, attrValue, mFilePath);
						node.addChild(entityRef);
					}
				}
				
			}
		*/
		}
		return node;
	}

	private DTDNode getElementNode(String content) throws DTDException {

		if (content != null) {
			Matcher matcher = DTDConstants.Patterns.getElementContentPattern().matcher(content);
			DTDNode node = null;
			if (matcher.matches()) {
				
				/**
				 *match attri content group-->0; str-->attributes (%editorial;, divisions?, key*, time*,
				 *  staves?, part-symbol?, instruments?, clef*, staff-details*, transpose*, directive*, measure-style*)
				 *match attri content group-->1; str-->attributes
				 *match attri content group-->2; str-->null
				 *match attri content group-->3; str-->%editorial;, divisions?, key*, time*,  staves?,
				 * part-symbol?, instruments?, clef*, staff-details*, transpose*, directive*, measure-style*
				 *match attri content group-->4; str-->measure-style*
				 *match attri content group-->5; str-->-style
				 *match attri content group-->6; str-->null
				 */
				String elementName = matcher.group(1);
				String subElementNames = matcher.group(3);
				node = new DTDNode(DTDNode.NODE_TYPE_ELEMENT, elementName, mFilePath);
				node.setStringData(subElementNames);
				/*
				matcher = Pattern.compile(REGULAR_NAME).matcher(content);
				String elementName = null;
				int startPos = 0;
				if (matcher.find()) {
					elementName = matcher.group();
					node = new DTDNode(DTDNode.NODE_TYPE_ELEMENT, elementName, mFilePath);
					Log.d(TAG, "find element name-->" + elementName);
					startPos = matcher.start() + elementName.length();
					Log.d(TAG, "sub element def position-->" + startPos);
				}
				if (node != null) {
					Pattern entityRefPattern = Pattern.compile(REGULAR_ENTITY_REFERENCE);
					Pattern subElementRefPattern = Pattern.compile(REGULAR_ELEMENT_REFERENCE_NAME);
					matcher = Pattern.compile(REGULAR_ELEMENT_SUB_ELEMENT_DEF).matcher(content);
					boolean res = matcher.find(startPos);
					List<ElementInfo> subElementInfos = null;
					while (res) {
						Matcher entityMatcher = entityRefPattern.matcher(matcher.group());
						Matcher subElementMatcher = subElementRefPattern.matcher(matcher.group());
						if (entityMatcher.find()) {
							Log.d(TAG, "find entity ref-->" + matcher.group());
							DTDNode entityRefNode = new DTDNode(DTDNode.NODE_TYPE_ENTITY_REFERENCE,
									entityMatcher.group(), mFilePath);
							node.addChild(entityRefNode);
						} else if (subElementMatcher.find()) {
							int existenceFlag = 0;
							if (matcher.group().contains("*")) {
								existenceFlag = DTDNode.EXISTENCE_FLAG_ANY;
							} else if (matcher.group().contains("?")) {
								existenceFlag = DTDNode.EXISTENCE_FLAG_ONCE;
							} else if (matcher.group().contains("+")) {
								existenceFlag = DTDNode.EXISTENCE_FLAG_NOT_NULL;
							} else {
								throw new DTDException(DTDException.ERROR_CODE_DEFINITION_NOT_FOUND,
										"no existence definitionFound");
							}
							ElementInfo elementInfo = new ElementInfo(subElementMatcher.group(), existenceFlag);
							if (subElementInfos == null) {
								subElementInfos = new LinkedList<ElementInfo>();
							} else {
								subElementInfos.add(elementInfo);
							}
							Log.d(TAG, "find sub element ref-->" + matcher.group());
						}
						res = matcher.find();
					}
					if (subElementInfos != null) {
						node.setObjData(subElementInfos);
					}
				}
				*/
			}
			return node;
		}
		return null;
	}

	private DTDNode getCommentNode(String content) {
		DTDNode node = null;
		if (content != null) {
			content = content.trim();
			if(content.startsWith("<!--") && content.endsWith("-->")){
				node = new DTDNode(DTDNode.NODE_TYPE_COMMENT, null, mFilePath);
				node.setStringData(content.substring("<!--".length(), content.length() - "-->".length()));
			}
		}
		return node;
	}



	private DTDNode getEntityNode(String content) throws DTDException {
		if (content != null) {
			DTDNode node = null;
			Matcher matcher = DTDConstants.Patterns.getEntityDefValuesOnlyPattern().matcher(content);
			if (matcher.matches()) {
				// 绾�煎疄浣撳畾涔夌殑澶勭悊
				/**
				 * matched groups-->0;str--> % start-stop-continue "(start | stop | continue)" 
				 * matched groups-->1;str-->start-stop-continue
				 * matched groups-->2;str-->-continue 
				 * matched groups-->3;str-->(start | stop | continue) matched groups-->4;str-->null
				 * matched groups-->5;str-->| continue 
				 * matched groups-->6;str-->null
				 */
				String entityName = matcher.group(1);
				node = new DTDNode(DTDNode.NODE_TYPE_ENTITY, entityName, mFilePath);
				EntityInfo info = new EntityInfo(EntityInfo.TYPE_VALUES_ONLY, null, mFilePath);
				info.setValue(matcher.group(3));
				node.setObjData(info);
				return node;
			}

			matcher = DTDConstants.Patterns.getEntityDefWithNamePattern().matcher(content);
			if (matcher.matches()) {
				// 鍚嶇О-鍊肩被鍨嬪畾涔夌殑瀹炰綋鐨勫鐞�
				/**
				 *matched groups-->0;str--> % font " font-family  
				 *CDATA  #IMPLIED font-style   CDATA  #IMPLIED font-size 
				 *   CDATA  #IMPLIED font-weight  CDATA  #IMPLIED"
				 *matched groups-->1;str-->font
				 *matched groups-->2;str-->null
				 *matched groups-->3;str-->font-family  CDATA  #IMPLIED font-style 
				 *  CDATA  #IMPLIED font-size    CDATA  #IMPLIED font-weight  
				 *  CDATA  #IMPLIEDmatched groups-->4;str-->font-weight  CDATA  #IMPLIED
				 *matched groups-->5;str-->font-weight
				 *matched groups-->6;str-->-weight
				 *matched groups-->7;str-->CDATA 
				 *matched groups-->8;str-->null
				 *matched groups-->9;str-->null
				 *matched groups-->10;str-->null
				 *matched groups-->11;str-->null
				 *matched groups-->12;str-->#IMPLIED
				 */
				
				String entityName = matcher.group(1);
				node = new DTDNode(DTDNode.NODE_TYPE_ENTITY, entityName, mFilePath);
				String defStr = matcher.group(3);
				matcher = DTDConstants.Patterns.getNameValueExistencePattern().matcher(defStr);
				List<EntityInfo> infos = new LinkedList<EntityInfo>();
				while(matcher.find()){
					EntityInfo info = new EntityInfo(EntityInfo.TYPE_VALUES_WITH_NAME, matcher.group(1), mFilePath);
					info.setValue(matcher.group(3));
					info.setExistence(DTDConstants.DataType.parseType(matcher.group(8)));
					infos.add(info);
				}
				if(infos.size() > 0){
					node.setObjData(infos);
				}
				return node;
			}
			matcher = DTDConstants.Patterns.getEntityDocDefPattern().matcher(content);
			if (matcher.matches()) {
				// 鍦ㄥ叾浠栨枃妗ｄ腑瀹氫箟鐨勫疄浣撶殑澶勭悊
				/**
				 * matched groups-->0;str-->% layout PUBLIC
				 * "-//Recordare//ELEMENTS MusicXML 3.0 Layout//EN" "layout.mod"
				 * matched groups-->1;str-->layout matched groups-->2;str-->null
				 * matched groups-->3;str-->PUBLIC matched groups-->4;str-->
				 * "-//Recordare//ELEMENTS MusicXML 3.0 Layout//EN" matched
				 * groups-->5;str-->//EN matched groups-->6;str-->"layout.mod"
				 */
				
				printMatcher(matcher);
				String refDocName = mFile.getParent() + File.separator + matcher.group(6);
				node = new DTDNode(DTDNode.NODE_TYPE_ENTITY, matcher.group(1), refDocName);
				EntityInfo info = new EntityInfo(EntityInfo.TYPE_COMPLEX_CONTENT, null, refDocName);
				node.setObjData(info);
				return node;
			}

			matcher = DTDConstants.Patterns.getEntityInstrumentPattern().matcher(content);
			if (matcher.matches()) {
				// 瀹炰綋鎸囦护鐨勫鐞�
				/**
				 * 
				 * matched groups-->0;str--> % timewise "IGNORE" matched
				 * groups-->1;str-->timewise matched groups-->2;str-->null
				 * matched groups-->3;str-->IGNORE
				 */
				node = new DTDNode(DTDNode.NODE_TYPE_ENTITY, matcher.group(1), mFilePath);
				EntityInfo info = new EntityInfo(EntityInfo.TYPE_INSTRUMENT, matcher.group(1), mFilePath);
				node.setObjData(info);
				return node;
			}
		}
		return null;
	}

	private void printMatcher(Matcher matcher){
		if(matcher == null){
			Log.d(TAG, "printMatcher matcher is null");
			return;
		}
		for(int i = 0; i<= matcher.groupCount(); i++){
			Log.d(TAG, "matcher group-->" + i + "; content-->" + matcher.group(i));
		}
	}
	
}
