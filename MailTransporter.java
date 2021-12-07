package system.soft.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import system.soft.control.AntResource;
import system.source.DjadeUtil;

public class MailTransporter {
	// HERE WE SET PUBLIC VARIABLES
	private SetBase Setting;
	private FileManager Manage;
	AntResource resource;
	private String Messaging;
	private String Title;
	private boolean Conditions=true;
	public int SENTMESSAGES=0;
	public boolean LOGGED = false;
	private static String TEMPLATESOURCE;
	// LETS CONSTRUCT MAIN CLASS
	public MailTransporter(SetBase setting){
		Setting=setting;
		Manage=new FileManager(setting);
		resource=new AntResource();
		try {
			TEMPLATESOURCE=resource.get(setting.get("template"));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	
	
	/********************** SETTING THE GETTERS METHODS ************************/
	public void subject(String subject){
		// Setting default subject
		if(subject.length()<2){
			subject = "Message from Mail Ant";
		}
		// Here we assign
		Title=subject;
	}
	
	
	public void message(String message){
		// Setting default message
		if(message.length()<2){
			message = "Mail Ant v1.0 - Mail Worker for sending bulk message and extracting email address from the web.";
		}
		// Here we assign
		Messaging=message;
	}
	
	
	public void condition(boolean c){
		Conditions=c;
	}
	
	
	/********************** CONSTRUCTING THE SENDER METHOD **************************/
	public void send(String[] mails, MailTransporter transport){
		// HERE WE START CONSTRUCTING THE SNDER PROCESS
		int NUM_THREADS=Integer.parseInt(Setting.get("maxThread"));
		int limit=0; // initiating rate with zero
		int rate=0;
		String[] sendMails={};
		int istype=0;
		int start=0;
		// HERE PROCESSING BEGINS
		if(mails!=null && mails.length>0){
			// Here we loop mails to store send mails
			rate=mails.length; // setting rate
			if((rate%2)==1){
				limit=((rate-1)/NUM_THREADS)+1;
			}
			else{
				limit=rate/NUM_THREADS;
			}
			// Now lets check amount of mails
			if(mails.length<100){
				limit=mails.length;
				istype=1;
			}
			// Here we set the mails length
			sendMails=new String[limit];
			// HERE WE START THREAD POOL
			ExecutorService es = Executors.newFixedThreadPool(NUM_THREADS);
			List<Future<Integer>> futures = new ArrayList<>(NUM_THREADS);
			
			// Submit task to every thread:
			for (int i = 0; i < NUM_THREADS; i++) {
				// Here we loop to get mails
				if(start<mails.length){
					for(int k=start, j=0; j<mails.length; k++, j++){
						if(j<=(limit-1)){
							// Here we assign new values to email array
							if(k<mails.length && j<sendMails.length){
								sendMails[j]=mails[k];
							}
						}
						else{
							start+=limit;
							break;
						}
					}
					// Here we thread task
					futures.add(i, es.submit((Callable<Integer>) new Transporter(sendMails, transport)));
					try {
						SENTMESSAGES+=futures.get(i).get().intValue();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // change send mails
				}
				else{
					istype=1;
					break;
				}
				
				// Checking thread type to prevent multiple run
				if(istype==1){
					// Here we break
					break;
				}
			} // end of loop
			
			// Shutdown thread pool
			es.shutdown();
		}
	}
	
	
	/**** CONSTRUCTING THE TRANSPORT METHOD * @throws UnsupportedEncodingException ******/
	private Integer transport(String[] mails, String title, String messaging) throws IOException, URISyntaxException{
		// HERE WE START PROCESSING THE TRANSPORT
		String sentLog = new SetBase().get("sentLog");
		String company=Setting.get("company");
		String translatedContent="";
		Integer sent=0;
		int edited=0;
		// HERE WE START PROCESSING
		// Sender's email ID needs to be mentioned
		String from = Setting.get("from");

		// Get system properties
		Properties properties = props();
		// Get the default Session object.
		Session session = session(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from, company));

			// Set To: header field of the header.
			message.addRecipients(Message.RecipientType.BCC, mailAddress(mails));

					
			// Set Subject: header field
			message.setSubject(Title);

			// Send the actual HTML message, as big as you like
			translatedContent=msgTranslate(Title, Messaging);
			message.setContent(translatedContent, "text/html");
			// message.setText("This is actual message");
			// Send message
			Transport.send(message);
			
			// Setting the message return
			// Here we log the mails that has been sent and use its as
			// A reference to know were to start next and avoid repeated send
			if(Conditions){
				try {
					edited=Manage.editor(sentLog, mails);
					if(edited>0){
						LOGGED=true;
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Here we set return variables
			sent=mails.length;
			
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	    
		// Here we return integer
		return sent;
	}
	
	
	
	/******************* CONSTRUCTING THE MESSAGE TRANSLATOR ********************/
	private String msgTranslate(String subject, String messaging) throws IOException, URISyntaxException{
		String data="";
		DjadeUtil util=new DjadeUtil();
		String html="";
		// NOW LETS START PROCESSING
		if(messaging!=null && subject!=null){
			// Now lets read
			try {
				html += "<html><head></head>"+
						"<body><h2>"+subject+"</h2>"+
						"<p>"+messaging+"</p>"+
						"</body></html>";
				// NOW LETS CHECK TEMPLATE
				if(new File(TEMPLATESOURCE).exists()){
					if(TEMPLATESOURCE.indexOf("htm")>=0){
						// Here template file exists
						data=util.readBuffer(TEMPLATESOURCE, "ant");
						// Now lets check
						if(data.length()>0){
							// Here we start matching to replace
							data=data.replace("<sb:title:sb>", subject);
							data=data.replace("<sb:message:sb>", messaging);
						}
					}
					else{
						data += html;
					}
				}
				else{
					// Here template file does not exist
					data += html;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Here we return string
		return data;
	}
	
	
	/******************* CONSTRUCTING RECEPIENT PARSER METHOD *******************/
	private InternetAddress[] mailAddress(String[] mails){
		// HERE WE START PROCESSING THE MAIL ADDRESSES
		InternetAddress[] address={};
		// NOW LETS START
		
		if(mails!=null){
			if(mails.length>0){
				address=new InternetAddress [mails.length];
				for(int i=0; i<mails.length; i++){
					try {
						address[i]=new InternetAddress(mails[i]);
					} catch (AddressException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		// Here we return address
		return address;
	}
	
	
	/********************** CONSTRUCTING THE PROPERTY METHOD ********************/
	private Properties props(){
		// HERE WE START SETTING MESSAGE PROPERTIES
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		Properties properties = System.getProperties();
		String host = "localhost";
		// HERE WE START SETTING
		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.host", Setting.get("host"));
		properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		// properties.put("mail.smtp.starttls.enable","false");
		properties.put("mail.smtp.ssl.trust", "*");
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		properties.put("mail.smtp.socketFactory.port", Setting.get("port"));
		properties.setProperty("mail.smtp.port", Setting.get("port"));
		properties.setProperty("mail.smtp.socketFactory.port", Setting.get("port"));
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.debug", "true");
		properties.put("mail.store.protocol", Setting.get("sp"));
		properties.put("mail.transport.protocol", Setting.get("tp"));
		
		// Here we return property
		return properties;
	}
	
	
	/********************** CONSTRUCTING THE SESSION METHOD ***********************/
	private Session session(Properties props){
		// HERE WE START SETTING THE SESSION
		// Get the default Session object.
		   Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
				      protected PasswordAuthentication getPasswordAuthentication() {
				          return new PasswordAuthentication(Setting.get("username"), Setting.get("password"));
				      }
			});
		   
		   // Here we return session
		   return session;
	}
	
	
	/******************** HERE WE CONSTRUCT TRANSPORT CLASS ***********************/
	public static final class Transporter implements Callable<Integer>{
		// HERE WE CONSTRUCT CLASS
		private String Message;
		private String Title;
		private String[] Mails;
		MailTransporter Transport;
		
		public Transporter(String[] mails, MailTransporter transport){
			Mails=mails;
			Title=transport.Title;
			Message=transport.Messaging;
			Transport=transport;
		}
		
		/*********** HERE WE CALL THE CALLABLE ***********/
		@Override
        public Integer call() throws Exception {
			try {
	            return Transport.transport(Mails, Title, Message);
	        } catch (Throwable t) {
	            t.printStackTrace();
	            throw new RuntimeException(t);
	        }
        }
		
		// END OF INNER CLASS
	}
	
	// END OF OUTER CLASS
}
