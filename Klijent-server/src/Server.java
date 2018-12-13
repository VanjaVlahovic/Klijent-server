import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	static ServerNit klijenti[] = new ServerNit[10];
	public static void main(String[] args) {
		int port=2222;
		if (args.length>0)
			port = Integer.parseInt(args[0]);
		Socket klijentSoket = null;
		try{
			ServerSocket serverSoket = new ServerSocket(port);
			while(true){
				klijentSoket = serverSoket.accept();
				Korisnik korisnik=new Korisnik();
				for (int i = 0; i <=9; i++)
					if(klijenti[i]==null){
						klijenti[i]= new ServerNit(klijentSoket, klijenti, korisnik);
						klijenti[i].start();
						break;
					}
			}
		} catch(IOException e){
			System.out.println(e);
		}	
	}
}
