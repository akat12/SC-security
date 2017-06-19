package auth_miniproject;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.*;


public class server {
	
	private ServerSocket server;
	private Socket connection;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public void start(){
		try{
			 ServerSocket server = new ServerSocket(8903,600);
			 String X="";
			 while(X!="END"){
			 
			 connection = server.accept();
			 connection.setKeepAlive(true);
			 connection.setSoTimeout(0);
			 
				 out = new ObjectOutputStream(connection.getOutputStream());
				 
				 out.flush();
				
				 
				 
				 
				 
			 
				 
					 in = new ObjectInputStream(connection.getInputStream());
						try{
							X =(String) in.readObject();
							 char a = X.charAt(1);
							
							 switch(a){
							 case 'R': 
							 server_register(X);
							 break;
							 case 'N':
							 break;
							 case 'L':
								 server_login(X);
							 
								 
							
							 
							 }
						
							
							}catch(Exception e){
							e.printStackTrace();
							System.out.println("error while chatting");
						}
						in.close();
					
			 }
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Server start error");
			}finally{
				closeconnection();
				
			}
	}
	public String Stringer(String para,String start,String end){
		int a = para.indexOf(start);
		a+=start.length();
		int b = para.indexOf(end);
		String result = para.substring(a, b);
		return result;
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
			System.out.println("Error in sendmessage server");
		}
		
	}
	public void showmessage(String x){ // to show message on window
		
	}
	
	public void server_register(String x){ // client registration request handler
		
		
		int X=4;
		String ID = Stringer(x,"<ID>","</ID>");
		String Rpwd = Stringer(x,"<Rpwd>","</Rpwd>");
		String temp="";
		byte ar[] = ID.getBytes();
		for(int i=0;i<ar.length;i++){
			ar[i]=(byte) (ar[i]^X);
		temp=temp+ar[i];	
		}
		
	double Mi = temp.hashCode();
	String temp2 = ID+Rpwd;
	double t1 = temp2.hashCode();
	double Ni = Double.longBitsToDouble(Double.doubleToRawLongBits(Mi)^Double.doubleToRawLongBits(t1));
	try{
	File f = new File(ID+"_SC.txt");
	f.createNewFile();
	FileOutputStream out = new FileOutputStream(ID+"_SC.txt");
	String SC = "<Ni>"+Ni+"</Ni>"+"<n>"+21+"</n>";
	for(int i=0;i<SC.length();i++){
		out.write(SC.charAt(i));
	}
	out.close();
	String tst = "SC";
	sendmessage(tst);
	}catch(Exception e){
		e.printStackTrace();
		System.out.println("error in creating file "+ID+" client_SC.txt");
	}
		
	}
	public void server_login(String x){
		
		int n=21;
		double dT = System.currentTimeMillis();
		String tT = Stringer(x,"<T>","</T>");
		double T = Double.parseDouble(tT);
		if((dT-T)>6000){
			System.out.println("client request timeout(intercept detected)");
			return;
		}
		
		String tC2 = Stringer(x,"<C2>","</C2>");
		String ts = Stringer(x,"<C1>","</C1>");
		double C1 = Double.parseDouble(ts);
		String DID = Stringer(x,"<DID>","</DID>");
		
		
		int X=4;
		double td = (Math.pow(C1, X))%n;
		String ID="";
		String f[] = tC2.split(",");
		byte C2[]=new byte[f.length];
		for(int i=0;i<f.length;i++){
			C2[i] = Byte.parseByte(f[i]);
			
		}
		
		for(int i=0;i<C2.length;i++){
			C2[i]=(byte)(C2[i]^(int)td);
		}
		ID = new String(C2);
		String temp="";
		for(int i=0;i<ID.length();i++){
			temp=temp+(ID.charAt(i)^X);
		}
		double Mi = temp.hashCode();
		td=Double.longBitsToDouble(Double.doubleToRawLongBits(Mi)^Double.doubleToRawLongBits(T));
		temp=""+td;
		double b = temp.hashCode();
		String DI[] = DID.split(",");
		byte arr[]=new byte[DI.length];
		for(int i=0;i<DI.length;i++)
			arr[i] = Byte.parseByte(DI[i]);
		for(int i=0;i<arr.length;i++)
			arr[i]=(byte)(arr[i]^(int)b);
		String a = new String(arr);
		temp="";
		byte ar[] = a.getBytes();
		for(int i=0;i<ar.length;i++){
			ar[i]=(byte) (ar[i]^X);
		temp=temp+ar[i];	
		}
		double tMi= temp.hashCode();
		td=Double.longBitsToDouble(Double.doubleToRawLongBits(tMi)^Double.doubleToRawLongBits(T));
		temp=""+td;
		double tb = temp.hashCode();
		if(tb!=b){
			System.out.println("bi==b check failed");
			return;
		}
	
		
		double Ts = System.currentTimeMillis();
		td = Double.longBitsToDouble(Double.doubleToRawLongBits(Mi)^Double.doubleToRawLongBits(Ts));
		double Y=0;
		try{
			FileInputStream fin = new FileInputStream("server_setup.txt");
			String servparam="";
			int d=0;
			while((d=fin.read())!=-1){
				servparam = servparam+(char) d;
			}
			 String tY = Stringer(servparam,"<Y>","</Y>");
			 Y = Double.parseDouble(tY);
			 fin.close();
				
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Couldn't find server's published values");
		}
		
		double C3 = (Math.pow(Y, td))%n;
		td =Double.longBitsToDouble(Double.doubleToRawLongBits(T)^Double.doubleToRawLongBits(Ts));
		String sess = Double.toString(Mi)+ID+Double.toString(td);
		double SK = sess.hashCode();
		String M2 ="<C3>"+C3+"</C3>"+"<Ts>"+Ts+"</Ts>";
		sendmessage(M2);
	
		
		
		
		
		//future communication done with SK(XOR)message//
	
	
	}
	
	
	public static void main(String[] args){
		
			server s = new server();
			s.start();
			
		
		}
	
		
		
	}
	
	

