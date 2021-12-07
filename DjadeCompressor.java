package system.source;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DjadeCompressor {
// SETTING INSTANCE VARIABLES
private String Dir;
private String ZipDir;
static final int BUFFER=2048;
public long CheckSum=0;
/************************* CONSTRUCTING CLASS *************************/
public DjadeCompressor(){};


/********************* SETTING HELPER METHODS *************************/
public void fIn(String path){
	Dir=path;
}

public void zipDir(String path){
	ZipDir=path;
}

/******************** CONSTRUCTING THE MAIN METHOD ********************/
public static void main(String[] args){
	// Now lets call zip
	DjadeCompressor compress=new DjadeCompressor();
	compress.fIn("cores/core/log/DjadeZips/zip_402781856.zip");
	// compress.zipDir("cores/core/log/DjadeZips/");
	// String trueValue=compress.zip(null);
	// System.out.println(trueValue);
	System.out.println(compress.unZip("cores/core/log/TestZip/"));
	System.out.println(compress.CheckSum);
}



/********************* CONSTRUCTING THE ZIP METHOD *********************/
public String zip(String filename){
	// NOW LETS CONSTRUCT THE ZIP METHOD LOGIC TO LOG ZIPS
	String zipName="zip_"+new Random().nextInt(999999999)+".zip";
	DjadeUtil Util=new DjadeUtil();
	String stringValue=null;
	File[] allFiles={};
	String dir="";
	// LETS START PROCESSING DefaultZipDir
	try{
		// Here we set the zipname
		if(filename!=null && filename.length()>0){
			if(filename.indexOf(".zip")>=0){
				zipName=filename;
			}
			else{
				zipName=filename+".zip";
			}
		}
		// Here we set zip path
		dir=ZipDir+zipName;
		
		// Now lets initiate zip
		BufferedInputStream origin=null;
		FileOutputStream dest=new FileOutputStream(dir);
		CheckedOutputStream checksum=new CheckedOutputStream(dest, new Adler32());
		ZipOutputStream out=new ZipOutputStream(new BufferedOutputStream(checksum));
		// Now lets compress
		out.setMethod(ZipOutputStream.DEFLATED);
		// Now lets set Byte
		byte data[]=new byte[BUFFER];
		// Get a list of files from current directory
		File f=new File(Dir);
		// Now lets list files
		File[] files=f.listFiles();
		allFiles=Util.fileFilter(Util.getFiles(files));
		// Now lets add files
		for(int i=0; i<allFiles.length; i++){
			if(allFiles[i]!=null){
				FileInputStream fi=new FileInputStream(allFiles[i]);
				origin=new BufferedInputStream(fi, BUFFER);
				ZipEntry entry=new ZipEntry(allFiles[i].getName());
				out.putNextEntry(entry);
				int count;
				// Now lets loop to write
				while((count=origin.read(data, 0, BUFFER))!=-1){
					out.write(data, 0, count);
				} // End of loop
				// Now lets close
				origin.close();
			}
		} // End of loop
		// Now lets close out
		out.close();
		// Now lets set checksum
		CheckSum=checksum.getChecksum().getValue();
		// Now lets set the return
		stringValue=dir;
	}
	catch(IOException e){
		e.printStackTrace();
	}
	
	return stringValue;
}



/********************* CONSTRUCTING THE UNZIP METHOD *******************/
public String unZip(String zipPath){
	// NOW LETS SET VARIABLES TO WORK WITH THIS METHOD
	String returnValue="";
	String SavedPath="";
	// LETS START PROCESS
	try{
	// Now lets initiate
	BufferedOutputStream dest=null;
	FileInputStream fis=new FileInputStream(Dir);
	CheckedInputStream checksum=new CheckedInputStream(fis, new Adler32());
	ZipInputStream zis=new ZipInputStream(new BufferedInputStream(checksum));
	ZipEntry entry;
	// Now lets loop
	while((entry=zis.getNextEntry())!=null){
		int count;
		byte data[]=new byte[BUFFER];
		// Write the files to the disk
		SavedPath=zipPath+entry.getName(); // Lets save file
		FileOutputStream fos=new FileOutputStream(SavedPath);
		dest=new BufferedOutputStream(fos, BUFFER);
		// Now lets loop to write file
		while((count=zis.read(data, 0, BUFFER))!=-1){
			dest.write(data, 0, count);
		} // End of loop
		// NOW LETS ADD FILE
		if(returnValue.length()>0){
		returnValue+=","+SavedPath;
		}
		else{
		returnValue+=SavedPath;
		}
		// NOW LETS CLOSE STREAMS
		dest.flush();
		dest.close();
	} // End of loop
	// Lets close zip
	zis.close();
	// Now lets add the check sum
	CheckSum=checksum.getChecksum().getValue();
	}
	catch(IOException e){
		e.printStackTrace();
	}
	
	return returnValue;
}



// END OF CLASS	
}
