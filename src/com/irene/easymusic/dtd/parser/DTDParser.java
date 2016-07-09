package com.irene.easymusic.dtd.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import sun.misc.Queue;
import learn.zhf.log.Log;

public class DTDParser {
	
	private  static final String TAG = "DTDParser";
	private Stack<DTDNode> mNodes;
	private List<DTDNode> mEntities;
	private List<DTDNode> mAttributes;
	private List<DTDNode> mELements;
	
	private DTDParser(){
		mNodes = new Stack<DTDNode>();
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
	
	public static DTDParser parse(String sourceFile, String encoding) throws FileNotFoundException, IOException{
		DTDDocument document = DTDDocument.loadFile(sourceFile, encoding);
		return null;
	}
	
	public void toJavaClasses(String targetPath, String packageName){
		
	}
	
	public void toDataBaseCreatFile(String dataBaseName){
		
	}
	
	public void toSchema(){
		
	}
}
