import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Klijent implements Runnable{

	static Socket soketZaKomunikaciju=null;
	static PrintStream izlazniTokKaServeru=null;
	static BufferedReader ulazniTokOdServera = null;
	static BufferedReader ulazKonzola = null;
	static boolean kraj=false;
	
	public static void main(String[] args) {
		
		try {
			int port=2222;
			if(args.length>0)
			port=Integer.parseInt(args[0]);
			soketZaKomunikaciju = new Socket("localhost", port);
			ulazKonzola = new BufferedReader(new InputStreamReader(System.in));
			izlazniTokKaServeru = new PrintStream(soketZaKomunikaciju.getOutputStream());
			ulazniTokOdServera = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			new Thread(new Klijent()).start();
			while(!kraj)
				izlazniTokKaServeru.println(ulazKonzola.readLine());
			soketZaKomunikaciju.close();
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("IOExcetpion: "+e);
		}
	}
	
	public void run() {
		String linijaOdServera;
		try{
			while((linijaOdServera=ulazniTokOdServera.readLine())!=null){
				System.out.println(linijaOdServera);
				if (linijaOdServera.indexOf("*** Dovidjenja")==0){
					kraj=true;
					return;
				}
			}
		}catch (IOException e){
			System.err.println("IOException: "+e);
		}
	}
}
