package com.irene.easymusic.dtd.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.irene.easymusic.dtd.parser.DTDHandler.OnHandleFinishedListener;
import com.irene.easymusic.dtd.parser.DTDHandler.OnNewDocumentListener;

import learn.zhf.log.Log;


public class DTDParser {
	
	private final String TAG ="DTDParser";
	
	private String mEntranceFile;
	
	private String mEncoding;
	
	private String mTargetModel;
	
	private boolean mStopFlag = false;
	
	OnNewDocumentListener mOnNewDocumentListener;
	
	OnHandleFinishedListener mOnHandlerFinishedListener;

	private Map<String, DTDDocument> mDocuments;
	
	public DTDParser(String sourceFile, String encoding,String targetModel){
		mEntranceFile = sourceFile;
		mEncoding = encoding;
		mTargetModel = targetModel;
		mDocuments = new HashMap<String, DTDDocument>();
		mOnHandlerFinishedListener = new MyHandlerFinishedListener();
		mOnNewDocumentListener = new MyOnNewDocumentListener();
	}
	
	public void parse() throws FileNotFoundException, IOException{
		Log.d(TAG, "DTDParser parse");
		mOnNewDocumentListener.onNewDocument(mEntranceFile);
	}
	
	public void stop(){
		mStopFlag = true;
	}
	
	private class MyHandlerFinishedListener implements DTDHandler.OnHandleFinishedListener{

		@Override
		public void onHandleFinished(String dtdFileName) {
			Log.d(TAG, "handler file-->" + dtdFileName + " is finished!");
			if(mDocuments.containsKey(dtdFileName)){
				DTDDocument doc = mDocuments.get(dtdFileName);
				try {
					Log.d(TAG, "release dtd document-->" + dtdFileName);
					doc.release();
					mDocuments.remove(dtdFileName);
				} catch (IOException e) {
					Log.d(TAG, "release dtd document-->" + dtdFileName + "io exception!");
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class StreamThread extends Thread{
		
		private DTDDocument mSourceDocument;
		private DTDHandler mSourceHandler;
		
		public StreamThread(DTDDocument document,DTDHandler handler){
			mSourceDocument = document;
			mSourceHandler = handler;
		}
		@Override
		public void run() {
			Log.d(TAG, "=====StreamThread run=====");
			if(mSourceDocument == null || mSourceHandler == null) {
				Log.d(TAG, "startStream initial condition error, return");
				return ;
			}
			while(!mStopFlag && mSourceDocument.hasNextNode()){
				try {
					mSourceHandler.handle(mSourceDocument.getNextNode());
				} catch (DTDException e) {
					//mStopFlag = true;
					e.printStackTrace();
				}
			}
		}
	}
	
	private class MyOnNewDocumentListener implements DTDHandler.OnNewDocumentListener{

		@Override
		public void onNewDocument(String fileName) {
			try {
				if(fileName == null || fileName.equals("")){
					Log.d(TAG, "MyOnNewDocumentListener onNewDocument fileName is empty");
					return;
				}
				DTDDocument document = DTDDocument.loadFile(fileName, mEncoding);
				mDocuments.put(fileName, document);
				DTDHandler handler = new DTDHandler(mTargetModel, fileName);
				handler.setOnHandleFinishedListener(mOnHandlerFinishedListener);
				handler.setOnNewDocumentListener(mOnNewDocumentListener);
				new StreamThread(document, handler).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
