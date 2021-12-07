package system.source;

import java.net.URISyntaxException;
import java.util.HashMap;

public class SetHandle {
// SETTING INSTANCE VARIABLES
private String Syntax;
private String Mode="r";
private HashMap<String, HashMap<String, String>> SetData;
private int CheckSet;
private String SetFile="data/setting/config.set";

/******************************* CONSTRUCTING THE CLASS *********************************/
public SetHandle(String syntax, String mode){
	// Setting values
	Syntax=syntax;
	if(mode!=null){
		Mode=mode;
	}
	
	// Calling the setting wizard
	try {
		setting();
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public SetHandle(){
	// Calling the setting wizard
	try {
		setting();
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


/************************* CREATING THE MAIN METHOD TO TEST THE SET ********************/
public static void main(String[] args){
	// Setting variables
	// String syntax="setting->email(2)~val-email:yes;setting->cmd(9)~val-cmd:yes";
	// Instantiating class
	SetHandle setRead=new SetHandle();
	// SetHandle setWrite=new SetHandle(syntax, "x");
	System.out.println(setRead.getSet("expression", "expression"));
	// System.out.println(setRead.syntaxGenerator());
	// System.out.println(setWrite.writeCheck());
}



/*************************** CONSTRUCTING THE SYNATX GENERATOR METHOD ******************/
public String syntaxGenerator(){
	// LETS START GENERATING SYNTAX FOR THE SETTING XML INSTRUCTION
	String syntax="";
	XmlReader read;
	
	// HERE WE START PROCESSING
	try {
		read=new XmlReader(SetFile); // Read set file
		syntax=read.syntaxBuilder("setting");
	} catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	// Here we return
	return syntax;
}



/********** CONSTRUCTING THE SET WIZARD * @throws URISyntaxException **********/
private void setting() throws URISyntaxException{
	// NOW LETS START PROCESSING SETTING WIZARD
	HashMap<String, HashMap<String, String>> setFetch=new HashMap<String, HashMap<String, String>>();
	String data="";
	int trueValue=0;
	XmlWriter write;
	XmlReader read;
	
	// NOW LETS START PROCESSING
	if(Mode!=null){
		// NOW LETS CHECK MODE
		if(Mode.equals("r")){
			// Getting syntax data from file
			// data=util.readByScanner(syntaxPath);
			data=syntaxGenerator();
			read=new XmlReader(data, SetFile, "rawMode"); // Read set file
			setFetch=read.emberFetch(); // Lets get the results
			// Setting returnable value
			SetData=setFetch;
		}
		else if(Mode.equals("x")){
			// HERE MODE IS TO UPDATE SETTING FILE
			write=new XmlWriter(Syntax, null);
			write.mode("a");
			write.fIn(SetFile);
			// Setting the editor true value
			trueValue=write.xmlEditor();
			// Setting returnable value
			CheckSet=trueValue;
		}
	} // End of mode check
}


/************************** CONSTRUCTING THE GET SET METHOD ***************************/
public String getSet(String name, String xname){
	// NOW LETS PROCESS THE SET DATA AND RETURN GIVEN VALUE
	String fetch="";
	// NOW LETS CHECK XNAME
	if(xname!=null){
		// Here the name extra is setted
		fetch=SetData.get(name).get(xname);
	}
	else{
		// Here the name extra is not setted
		fetch=SetData.get(name).get(name);
	}
	
	// NOW LETS RETURN THE SPECIFIED SET
	return fetch;
}

public int writeCheck(){
	// NOW LETS RETURN VALUES
	return CheckSet;
}

// END OF CLASS	
}
