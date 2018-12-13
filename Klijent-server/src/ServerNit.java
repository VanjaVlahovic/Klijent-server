import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerNit extends Thread {
	
	BufferedReader ulazniTokOdKlijenta=null;
	PrintStream izlazniTokKaKlijentu = null;
	Socket soketZaKom=null;
	Korisnik korisnik=new Korisnik();
	ServerNit[] klijenti;
	public ServerNit(Socket soket, ServerNit[] klijent, Korisnik korisnik){
		this.soketZaKom = soket;
		this.klijenti=klijent;
		this.korisnik=korisnik;
	}
	
	public void izracunaj() {
		String linija;
		izlazniTokKaKlijentu.println("Unesite zeljeni izraz: \n[Za prekid komunikacije mozete pritisnuti b] ");
		while(true){
			try {
				linija=ulazniTokOdKlijenta.readLine();
				if (linija.equals("b")) {
					izlazniTokKaKlijentu.println("*** Zatvara se konekcija za Vas ***");
					soketZaKom.close();
					break;
				}
				if (linija.equals("e")) {
					prikaziListu();
					break;
				}
				for (int i = 0; i <=9; i++){
					if (klijenti[i]!=null && klijenti[i]==this){
						String rez="nepoznat, mozda je greska u pisanju izraza.";
						int rezultat=0;
						if (linija.contains("+")){
							int t=linija.indexOf('+');
							String a1 = linija.substring(0, t);
							String b1 = linija.substring(t+1);
							try {
								int a=Integer.parseInt(a1);
								int b=Integer.parseInt(b1);
								rezultat = a+b;
								rez=(""+rezultat);
							} catch (NumberFormatException e) {
								System.err.println("Niste ispravno uneli izraz!");
								break;
							}
						}
						if (linija.contains("/")){
							int t=linija.indexOf('/');
							String a1 = linija.substring(0, t);
							String b1 = linija.substring(t+1);
							try {
								int a=Integer.parseInt(a1);
								int b=Integer.parseInt(b1);
								rezultat = a/b;
								rez=(""+rezultat);
							} catch (NumberFormatException e) {
								System.err.println("Niste ispravno uneli izraz!");
								break;
							}
						}
						if (linija.contains("-")){
							int t=linija.indexOf('-');
							String a1 = linija.substring(0, t);
							String b1 = linija.substring(t+1);
							try {
								int a=Integer.parseInt(a1);
								int b=Integer.parseInt(b1);
								rezultat = a-b;
								rez=(""+rezultat);
							} catch (NumberFormatException e) {
								System.err.println("Niste ispravno uneli izraz!");
								break;
							}
						}
						if (linija.contains("*")){
							int t=linija.indexOf('*');
							String a1 = linija.substring(0, t);
							String b1 = linija.substring(t+1);
							try {
								int a=Integer.parseInt(a1);
								int b=Integer.parseInt(b1);
								rezultat = a*b;
								rez=(""+rezultat);
							} catch (NumberFormatException e) {
								System.err.println("Niste ispravno uneli izraz!");
								break;
							}
						}
						klijenti[i].izlazniTokKaKlijentu.println("Rezultat: "+rez);
						klijenti[i].korisnik.getKalkulacije().add(linija +" ="+rez);
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void izracunaj3() {
		String linija;
		izlazniTokKaKlijentu.println("Posto niste registrovani korisnik imate pravo samo na tri kalkulacije:");
		izlazniTokKaKlijentu.println("Unesite zeljeni izraz: \n[Za prekid komunikacije mozete pritisnuti b] ");
		int k=0;
		while(k<3){
			try {
				
				linija=ulazniTokOdKlijenta.readLine();
				if (linija.equals("b")) {
					izlazniTokKaKlijentu.println("*** Zatvara se konekcija za Vas ***");
					soketZaKom.close();
					break;
				}
				for (int i = 0; i <=9; i++){
					if (klijenti[i]!=null && klijenti[i]==this){
						String rez="nepoznat, mozda je greska u pisanju izraza.";
						int rezultat=0;
						if (linija.contains("+")){
							int t=linija.indexOf('+');
							String a1 = linija.substring(0, t);
							String b1 = linija.substring(t+1);
							try {
								int a=Integer.parseInt(a1);
								int b=Integer.parseInt(b1);
								rezultat = a+b;
								rez=(""+rezultat);
							} catch (NumberFormatException e) {
								System.err.println("Niste ispravno uneli izraz!");
								break;
							}
						}
						if (linija.contains("/")){
							int t=linija.indexOf('/');
							String a1 = linija.substring(0, t);
							String b1 = linija.substring(t+1);
							try {
								int a=Integer.parseInt(a1);
								int b=Integer.parseInt(b1);
								rezultat = a/b;
								rez=(""+rezultat);
							} catch (NumberFormatException e) {
								System.err.println("Niste ispravno uneli izraz!");
								break;
							}
						}
						if (linija.contains("-")){
							int t=linija.indexOf('-');
							String a1 = linija.substring(0, t);
							String b1 = linija.substring(t+1);
							try {
								int a=Integer.parseInt(a1);
								int b=Integer.parseInt(b1);
								rezultat = a-b;
								rez=(""+rezultat);
							} catch (NumberFormatException e) {
								System.err.println("Niste ispravno uneli izraz!");
								break;
							}
						}
						if (linija.contains("*")){
							int t=linija.indexOf('*');
							String a1 = linija.substring(0, t);
							String b1 = linija.substring(t+1);
							try {
								int a=Integer.parseInt(a1);
								int b=Integer.parseInt(b1);
								rezultat = a*b;
								rez=(""+rezultat);
							} catch (NumberFormatException e) {
								System.err.println("Niste ispravno uneli izraz!");
								break;
							}
						}
						klijenti[i].izlazniTokKaKlijentu.println("Rezultat: "+rez);
						k++;
						izlazniTokKaKlijentu.println("Ostalo vam je jos: "+(3-k)+" kalkulacija");
						if (k==3) {
							izlazniTokKaKlijentu.println("Nemate pravo na vise kalkulacija.");
							izadji();
						}
							
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

	public void izadji() {
		izlazniTokKaKlijentu.println("*** Zatvara se konekcija za Vas ***");
		try {
			soketZaKom.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void registracija() {
		String ime;
		String sifra;
		izlazniTokKaKlijentu.println("Unesite jedinstveno korisnicko ime.");
		try {
			ime=ulazniTokOdKlijenta.readLine();
			boolean postoji=false;
			for (int i = 0; i <=9; i++) {
				if(klijenti[i]!=null && klijenti[i].korisnik!=null && klijenti[i].korisnik.getIme()!=null && klijenti[i].korisnik.getIme().equals(ime))
					postoji=true;
			}
			if (!postoji)
				for (int j = 0; j <=9; j++)
					if (klijenti[j]==this) {
						klijenti[j].korisnik.setIme(ime);
					}
			izlazniTokKaKlijentu.println("Unesite sifru. \n[Sifra mora imati minimum 8 karaktera, minimum jedno veliko slovo (A-Z) i minimum jednu cifru (0-9)]");
			sifra=ulazniTokOdKlijenta.readLine();
			for (int j = 0; j <=9; j++)
				if (klijenti[j]==this)
					klijenti[j].korisnik.setSifra(sifra);
			izlazniTokKaKlijentu.println("Uspesno ste se registrovali.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void prijava() {
		String ime;
		String sifra;
		izlazniTokKaKlijentu.println("Unesite korisnicko ime");
		try {
			ime=ulazniTokOdKlijenta.readLine();
			for (int i = 0; i <=9; i++) {
				if(klijenti[i]!=null && klijenti[i].korisnik!=null && klijenti[i].korisnik.getIme()!=null && klijenti[i].korisnik.getIme().equals(ime)) {
					izlazniTokKaKlijentu.println("Unesite sifru.");
					sifra=ulazniTokOdKlijenta.readLine();
					if (klijenti[i].korisnik.getSifra().equals(sifra)) {
						for (int j = 0; j <=9; j++)
							if(klijenti[j]==this) {
								klijenti[j]=klijenti[i];
								klijenti[i]=null;
								
							}
						izlazniTokKaKlijentu.println("Uspesno ste se prijavili.");
						break;
					}else
						izlazniTokKaKlijentu.println("Ne postojite u listi registrovanih korisnika.");
				}
					
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean daLiJeRegistrovan() {
		for (int i = 0; i <=9; i++) {
			if(klijenti[i]==this && klijenti[i]!=null && klijenti[i].korisnik!=null && klijenti[i].korisnik.getIme()!=null)
				return true;
		}
		return false;
	}

	public void prikaziListu() {
		for (int j = 0; j <=9; j++)
			if(klijenti[j]==this)
				izlazniTokKaKlijentu.println(klijenti[j].korisnik.getKalkulacije());
	}
	
	public void run(){
		String opcija;
		try {
			ulazniTokOdKlijenta = new BufferedReader(new  InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijentu = new PrintStream(soketZaKom.getOutputStream());
			
			izlazniTokKaKlijentu.println("Konekcija je uspesno uspostavljena. \nIzaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz\nc - registracija\nd - prijava");
			
			opcija = ulazniTokOdKlijenta.readLine();
			switch (opcija) {
			case "a":
				if(daLiJeRegistrovan())
					izracunaj();
				else
					izracunaj3();
				break;
			case "b":
				izadji();
				break;
			case "c":
				registracija();
				izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz\nd - prijava\n e - prikazi listu kalkulacija");
				switch (ulazniTokOdKlijenta.readLine()) {
				case "a":
					izracunaj();
					break;
				case "b":
					izadji();
					break;
				case "d":
					prijava();
					izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz");
					switch (ulazniTokOdKlijenta.readLine()) {
					case "a":
						izracunaj();
						break;
					case "b":
						izadji();
						break;
					default:
						System.out.println("Nepoznata opcija.");
						break;
					}
					break;
				case "e":
					prikaziListu();
					break;
				default:
					System.out.println("Nepoznata opcija.");
					break;
				}
				break;
			case "d":
				prijava();
				izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz");
				switch (ulazniTokOdKlijenta.readLine()) {
				case "a":
					izracunaj();
					break;
				case "b":
					izadji();
					break;
				default:
					System.out.println("Nepoznata opcija.");
					break;
				}
				break;
			default:
				System.out.println("Nepoznata opcija.");
				break;
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		for (int i = 0; i <=9; i++)
			if(klijenti[i]==this)
				klijenti[i]=null;
	}
}

