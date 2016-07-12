package com.irene.easymusic.dtd.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import learn.zhf.log.Log;

public class DTDHandler {

	public static final String MODEL_JAVA_CLASS = "java_class";
	public static final String MODEL_SCHEMA = "schema";
	public static final String MODEL_DATA_BASE = "data_base";
	
	private  static final String TAG = "DTDHandler";
	private Stack<DTDNode> mNodes;
	private List<DTDNode> mEntities;
	private List<DTDNode> mAttributes;
	private List<DTDNode> mELements;
	
	private String mTargetModel  = MODEL_JAVA_CLASS;
	private String mDTDFileName;
	
	
	public DTDHandler(String targetModel,String fileName){
		mTargetModel = targetModel;
		mDTDFileName = fileName;
		mNodes = new Stack<DTDNode>();
		mEntities = new LinkedList<DTDNode>();
	}
	
	public interface OnNewDocumentListener{
		public void onNewDocument(String fileName);
	}
	
	public interface OnHandleFinishedListener{
		public void onHandleFinished(String dtdFileName);
	}
	
	public OnNewDocumentListener mDocumentListener;
	
	private OnHandleFinishedListener mFinishListener;
	
	public void setOnNewDocumentListener(OnNewDocumentListener listener){
		mDocumentListener = listener;
	}
	
	public void setOnHandleFinishedListener(OnHandleFinishedListener listener){
		mFinishListener = listener;
	}
	
	private void addNode(DTDNode node){
		if(null != node){
			mNodes.push(node);
		}
	}
	
	private void addComment(DTDNode node){
		if(node == null){
			Log.d(TAG, "try to add comment node failed, node-->" + "null");
			return;
		}else if( node.getNodeType() != DTDNode.NODE_TYPE_COMMENT|| !node.isValid()){
			Log.d(TAG, "try to add comment node failed, node-->" + node.getNodeType() + "; valid-->"+ node.isValid());
			return;
		}
		addNode(node);
	}
	
	private void addEntity(DTDNode node){
		if(node == null){
			Log.d(TAG, "try to add entity node failed, node-->" + "null");
			return;
		}else if( node.getNodeType() != DTDNode.NODE_TYPE_ENTITY || !node.isValid()){
			Log.d(TAG, "try to add entity node failed, node-->" + node.getNodeType() + "; valid-->"+ node.isValid());
			return;
		}
		if(mEntities == null){
			mEntities = new LinkedList<DTDNode>();
		}
		if(mEntities.contains(node.getName())){
			Log.d(TAG, "try to add entity node failed, node all ready existent-->" + node.getName());
		}else{
			mEntities.add(node);
		}
		addNode(node);
	}
	
	private void addAttribute(DTDNode node){
		if(node == null){
			Log.d(TAG, "try to add attribute node failed, node-->" + "null");
			return;
		}else if( node.getNodeType() != DTDNode.NODE_TYPE_ATTRIBUTE || !node.isValid()){
			Log.d(TAG, "try to add attribute node failed, node-->" + node.getNodeType() + "; valid-->"+ node.isValid());
			return;
		}
		if(mAttributes == null){
			mAttributes = new LinkedList<DTDNode>();
		}
		if(mAttributes.contains(node.getName())){
			Log.d(TAG, "try to add attribute node failed, node all ready existent-->" + node.getName());
		}else{
			mAttributes.add(node);
		}
		addNode(node);
	}
	
	private void addElement(DTDNode node){
		if(node == null){
			Log.d(TAG, "try to add element node failed, node-->" + "null");
			return;
		}else if( node.getNodeType() != DTDNode.NODE_TYPE_ELEMENT || !node.isValid()){
			Log.d(TAG, "try to add element node failed, node-->" + node.getNodeType() + "; valid-->"+ node.isValid());
			return;
		}
		if(mEntities == null){
			mEntities = new LinkedList<DTDNode>();
		}
		if(mEntities.contains(node.getName())){
			Log.d(TAG, "try to add element node failed, node all ready existent-->" + node.getName());
		}else{
			mEntities.add(node);
		}
		addNode(node);
	}
	
	private List<DTDNode> getEntities(){
		return mEntities;
	}
	
	private List<DTDNode> getAttributes(){
		return mAttributes;
	}
	
	private List<DTDNode> getElements(){
		return mELements;
	}
	
	public boolean handledAll(){
		return false;
	}
	
	public boolean handling(){
		return false;
	}
	
	public boolean handle(DTDNode node){
		if(node == null){
			return false;
		}
		Log.d(TAG, "node type-->" + node.getNodeType() + "; node name-->" + node.getName());
		Log.d(TAG, "node strData-->" + node.getStringData() + "; node objData-->" + node.getObjData());
		switch(node.getNodeType()){
		case DTDNode.NODE_TYPE_ATTRIBUTE:
			break;
		case DTDNode.NODE_TYPE_COMMENT:
			break;
		case DTDNode.NODE_TYPE_ELEMENT:
			break;
		case DTDNode.NODE_TYPE_ENTITY:
			processEntityNode(node);
			break;
		case DTDNode.NODE_TYPE_ENTITY_REFERENCE:
			break;
		}
		return false;
	}
	
	private void processEntityNode(DTDNode node){
		if(node == null || node.getNodeType() != DTDNode.NODE_TYPE_ENTITY){
			Log.d(TAG, "processEntityNode dtd ndoe is null");
			return;
		}
		EntityInfo info = node.getObjData() == null ? null : (EntityInfo)node.getObjData();
		if(info == null){
			Log.d(TAG, "processEntityNode entity info is null");
			return;
		}
		switch(info.getType()){
		case EntityInfo.TYPE_COMPLEX_CONTENT:
			mNodes.push(node);
			mEntities.add(node);
			if(mDocumentListener != null){
				mDocumentListener.onNewDocument(info.getDocumentName());
			}
			break;
		case EntityInfo.TYPE_INSTRUMENT:
			break;
		case EntityInfo.TYPE_VALUES_ONLY:
			break;
		case EntityInfo.TYPE_VALUES_WITH_NAME:
			break;
		}
	}

}
