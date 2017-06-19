package auth_miniproject;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.*;


public class client {
	private Socket connection;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public void start(){
		try{
			int dec = 0;
			Scanner sc= new Scanner(System.in);
			
			
			do{
			
				System.out.println("1.register");
				System.out.println("2.login");
				System.out.println("3.password update");
				System.out.println("4.exit");
				dec = sc.nextInt();
				
				switch(dec){
				case 1: 
				register();
				break;
				case 2:
					login();
					break;
				case 3:
					update();
					break;
				case 4:
				shutdown();
				break;
				}
				
			}while(dec!=4);
				
			
		sc.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error client(Start)");
		}
		
	}
	
	public void shutdown(){
		openconnection();
		setupstream();
		System.out.println("System shutting down");
		sendmessage("END");
		closeconnection();
		System.out.println("System shut down");
		
	}
	
	public String Stringer(String para,String start,String end){
		int a = para.indexOf(start);
		a+=start.length();
		int b = para.indexOf(end);
		String result = para.substring(a, b);
		return result;
	}
	
	
	public void setupstream(){

		
		
		 try {
		out = new ObjectOutputStream(connection.getOutputStream());
		 out.flush();
		 in = new ObjectInputStream(connection.getInputStream());
		 System.out.println("Stream up");
		 }catch(Exception e){
			 e.printStackTrace();
			 System.out.println("Error in setting up Streams");
		 }
		
	}

	public void openconnection(){
		System.out.println("Attempting connection");
		try {
			connection = new Socket("127.0.0.1", 8903);
			System.out.println("Connected to server");
			connection.setKeepAlive(true);
			connection.setSoTimeout(0);
			
		} catch (Exception e) {
			System.out.println("Unable to connect");
			e.printStackTrace();
		} 
		
	}
	
	
	public void closeconnection(){
		try{
			connection.close();
			
			

			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Failure in closing");
		}
	}
	
public void sendmessage(String x){ //to send message to client
	try{
		out.writeObject(x);
		out.flush();
		
		
	}catch(Exception e){
		e.printStackTrace();
		System.out.println("Send message(client) error");
	}
}

	public void register(){ //to register client with server
		try{
			openconnection();
			setupstream();
			
		Scanner sc = new Scanner(System.in);
		System.out.println("Select a username");
		String ID = sc.nextLine();
		System.out.println("Select a Password");
		String pwd = sc.nextLine();
		System.out.println("Select a random user nonce for security");
		double ru = sc.nextDouble();
		
		
		String temp=ru+pwd;
		
		double Rpwd = temp.hashCode();
		
		String send = "/R/<ID>"+ID+"</ID>"+"<Rpwd>"+Rpwd+"</Rpwd>";
		System.out.print("Sending");
		out.writeObject(send);
		
		
		

		String SC =(String) in.readObject();
		System.out.println("Received "+SC);
		
			String t = ID+pwd;
			
			double t2 = t.hashCode();
			
			double Ai =Double.longBitsToDouble(Double.doubleToRawLongBits(ru)^Double.doubleToRawLongBits(t2));
			
			double ti =Double.longBitsToDouble(~(Double.doubleToRawLongBits(ru)^Double.doubleToRawLongBits(Rpwd)));
			
			
			
			String Zi="";
			for(int i=0;i<ID.length();i++){
				Zi=Zi+(ID.charAt(i)^Double.doubleToLongBits(ti));
				
			}
			
			
			FileInputStream fin = new FileInputStream(ID+"_SC.txt");
			
			int rd;
			
			String writen="";
			
			while((rd=fin.read())!=-1){
				char c =(char) rd;
				writen=writen+c;
		}
			
			writen=writen+"<Ai>"+Ai+"</Ai>"+"<Zi>"+Zi+"</Zi>";
			FileOutputStream fout = new FileOutputStream(ID+"_SC.txt");
			
			for(int i=0;i<writen.length();i++){
				fout.write(writen.charAt(i));
			}
			
			fin.close();
			fout.close();
			System.out.println("Successfully registered");
			in.close();
			
			closeconnection();
	
		
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(" register client(error)");
		}
	}
	
	public void login(){
		openconnection();
		setupstream();
		
		try{
			System.out.println("Enter the Username");
			Scanner sc1 = new Scanner(System.in);
			String ID = sc1.nextLine();
			System.out.println("Enter the Password");
			String pwd = sc1.nextLine();
			FileInputStream fin = new FileInputStream(ID+"_SC.txt");
			String param="";
			int d;
			while((d=fin.read())!=-1){
				param = param+(char) d;
			}
			fin.close();
			double t=(ID+pwd).hashCode();
			double Ai= Double.parseDouble(Stringer(param,"<Ai>","</Ai>"));
			double tmp =(ID+pwd).hashCode();
			double ru =Double.longBitsToDouble(Double.doubleToRawLongBits(Ai)^Double.doubleToRawLongBits(tmp));
			String Zi = Stringer(param,"<Zi>","</Zi>");
			String tNi = Stringer(param,"<Ni>","</Ni>");
			double Ni = Double.parseDouble(tNi);
			String tn = Stringer(param,"<n>","</n>");
			double n = Double.parseDouble(tn);
			 
				 
			 double Rpwd=(ru+pwd).hashCode();
				
				double ti =Double.longBitsToDouble(~(Double.doubleToRawLongBits(ru)^Double.doubleToRawLongBits(Rpwd)));
				
				String rZi="";
				for(int i=0;i<ID.length();i++){
					rZi=rZi+(ID.charAt(i)^Double.doubleToLongBits(ti));
					
				}
				
				
				if(rZi.equals(Zi)!=true){
					System.out.println("local password verification failed.");
					return ;
				}
				String temp = ru+pwd;
				
				temp=ID+Rpwd;
				ti = temp.hashCode();
				double Mi = Double.longBitsToDouble(Double.doubleToRawLongBits(Ni)^Double.doubleToRawLongBits(ti));
				double T =System.currentTimeMillis();
				ti=Double.longBitsToDouble(Double.doubleToRawLongBits(Mi)^Double.doubleToRawLongBits(T));
				temp=""+ti;
				double b = temp.hashCode();
				double g=0,Y=0;
				try{
					fin = new FileInputStream("server_setup.txt");
					String servparam="";
					 d=0;
					while((d=fin.read())!=-1){
						servparam = servparam+(char) d;
					}
					 String tY = Stringer(servparam,"<Y>","</Y>");
					 Y = Double.parseDouble(tY);
					
						 String tg = Stringer(servparam,"<g>","</g>");
						 g = Double.parseDouble(tg);
						 fin.close();
					
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("Couldn't find server's published values");
				}
			int ri =(int) (Math.random()*10);
			ti=Math.pow(g,ri);
			double C1 = ti%n;
			ti =Math.pow(Y, ri);
			double t1 = ti%n;
			byte[] tC2 = ID.getBytes();
			String C2="";
		
				for(int i=0;i<tC2.length;i++){
				tC2[i]=(byte)(tC2[i]^(int)t1);
				C2=C2+tC2[i]+",";
			}
			
			
				
			double Ci = (b*b)%n;
			
			byte[] tdid = ID.getBytes();
			String DID="";
			for(int i=0;i<tdid.length;i++){
				tdid[i]=(byte)(tdid[i]^(int)b);
				DID=DID+tdid[i]+",";
			}
			
			String M1 ="/L/<Ci>"+Ci+"</Ci>"+"<DID>"+DID+"</DID>"+"<C1>"+C1+"</C1>"+"<C2>"+C2+"</C2>"+"<T>"+T+"</T>";
			sendmessage(M1);
			
			
			double dt = System.currentTimeMillis();
			

			
			String para =(String) in.readObject();
			
			temp = Stringer(para,"<Ts>","</Ts>");
			double Ts = Double.parseDouble(temp);
			temp = Stringer(para,"<C3>","</C3>");
			double C3 = Double.parseDouble(temp);
			
			if((dt-Ts)>6000){
				System.out.println("server response timeout(intercept detected)");
				return;
			}
			ti=Double.longBitsToDouble(Double.doubleToRawLongBits(Mi)^Double.doubleToRawLongBits(Ts));
			double cC3 = (Math.pow(Y, ti))%n;
			if(cC3!=C3){
				System.out.println("C3'==C3 verification failed");
				return;
			}
			System.out.println("login successfull");
			ti =Double.longBitsToDouble(Double.doubleToRawLongBits(T)^Double.doubleToRawLongBits(Ts));
			String sess = Double.toString(Mi)+ID+Double.toString(ti);
			double SK = sess.hashCode(); 
			//future communication done with SK(XOR)message//
			
			closeconnection();
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error in login");
		}
		
	}
		
	public void update(){
		try{

			System.out.println("Enter the Username");
			Scanner sc2 = new Scanner(System.in);
			String ID = sc2.nextLine();
			System.out.println("Enter the Password");
			String pwd = sc2.nextLine();
			System.out.println("Enter the new Password");
			String pwdnew = sc2.nextLine();
			
			FileInputStream fin = new FileInputStream(ID+"_SC.txt");
			String param="";
			int d;
			while((d=fin.read())!=-1){
				param = param+(char) d;
			}
			fin.close();
			double t=(ID+pwd).hashCode();
			double Ai= Double.parseDouble(Stringer(param,"<Ai>","</Ai>"));
			double tmp =(ID+pwd).hashCode();
			double ru =Double.longBitsToDouble(Double.doubleToRawLongBits(Ai)^Double.doubleToRawLongBits(tmp));
			 String Zi = Stringer(param,"<Zi>","</Zi>");
			
			
				 String tNi = Stringer(param,"<Ni>","</Ni>");
			 double Ni = Double.parseDouble(tNi);
			 
				 String tn = Stringer(param,"<n>","</n>");
			 double n = Double.parseDouble(tn);
			 
				 
			 double Rpwd=(ru+pwd).hashCode();
				
				double ti =Double.longBitsToDouble(~(Double.doubleToRawLongBits(ru)^Double.doubleToRawLongBits(Rpwd)));
				
				String rZi="";
				for(int i=0;i<ID.length();i++){
					rZi=rZi+(ID.charAt(i)^Double.doubleToLongBits(ti));
					
				}
	
				if(rZi.equals(Zi)==false){
					System.out.println("local password verification failed.");
					return ;
				}
				double Rpwdnew =(ru+pwdnew).hashCode();
				 
				tmp = (ID+Rpwd).hashCode();
				double td =(ID+Rpwdnew).hashCode();
				
				double Ninew= Double.longBitsToDouble(Double.doubleToRawLongBits(Ni)^Double.doubleToRawLongBits(tmp)^Double.doubleToRawLongBits(td));
				
			tmp =(ID+pwd).hashCode();
			td = (ID+pwdnew).hashCode();
				double Ainew= Double.longBitsToDouble(Double.doubleToRawLongBits(Ai)^Double.doubleToRawLongBits(tmp)^Double.doubleToRawLongBits(td));
				ti =Double.longBitsToDouble(~(Double.doubleToRawLongBits(ru)^Double.doubleToRawLongBits(Rpwdnew)));
				String Zinew="";
				for(int i=0;i<ID.length();i++){
					Zinew=Zinew+(ID.charAt(i)^Double.doubleToLongBits(ti));
					
				}
			
				
			String writer ="<Ni>"+Ninew+"</Ni>"+"<n>"+n+"</n>"+"<Ai>"+Ainew+"</Ai>"+"<Zi>"+Zinew+"</Zi>";
			 try{
				FileOutputStream fout = new FileOutputStream(ID+"_SC.txt");
				
				for(d=0;d<writer.length();d++){
					fout.write((int)writer.charAt(d));
				}
				
				fout.close();
				System.out.println("Smart Card successfully updated");
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("error in opening files");
			}
			 
			 System.out.println("Smart Card successfully updated");
			 
				
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("error in password updation");
		}
		
	}
		
	
	
	public static void main(String[] args){
		
			client c = new client();
			c.start();
		
				
			
		
		}
		
	}


