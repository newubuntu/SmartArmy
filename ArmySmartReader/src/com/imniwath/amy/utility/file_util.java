package com.imniwath.amy.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class file_util {
	private static final int MODE_PRIVATE = 0;
	private Context context;
	public file_util(Context con){
		this.context=con;
	}
	public void WriteFeed(String s,String file) {
		FileOutputStream fOut = null;
		ObjectOutputStream osw = null;
		try { 
			fOut =context.openFileOutput(file, MODE_PRIVATE);
			osw = new ObjectOutputStream(fOut);
			osw.writeObject(s);
			osw.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public String ReadFeed(String fName){
		FileInputStream fIn = null;
		ObjectInputStream isr = null;
	     String armyfeeds=null;
		File feedFile = context.getFileStreamPath(fName);
		if (!feedFile.exists())
			return null;
		try {
			fIn = context.openFileInput(fName);
			isr = new ObjectInputStream(fIn);
			armyfeeds = (String) isr.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return armyfeeds;
   }
}
