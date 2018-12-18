import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

	static ServerNit klijenti[] = new ServerNit[10];
	public static void main(String[] args) {
		int port=2222;
		if (args.length>0)
			port = Integer.parseInt(args[0]);
		Socket klijentSoket = null;
		try {
			ServerSocket serverSoket = new ServerSocket(port);
			
			while(true){
				klijentSoket = serverSoket.accept();
				for (int i = 0; i <=9; i++)
					if(klijenti[i]==null){
						klijenti[i]= new ServerNit(klijentSoket, klijenti, "korisnici.txt");
						klijenti[i].start();
						break;
					}
			}
		} catch(Exception e){
			System.out.println(e);
		}	
		
	}
}
