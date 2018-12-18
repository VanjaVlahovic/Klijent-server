import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class ServerNit extends Thread {
	
	BufferedReader ulazniTokOdKlijenta=null;
	PrintStream izlazniTokKaKlijentu = null;
	Socket soketZaKom=null;
	LinkedList<String > imena = new LinkedList<>();
	LinkedList<Korisnik> listaRegistrovanihKorisnika= new LinkedList<>();
	ServerNit[] klijenti;
	String fajl;
	
	public ServerNit(Socket soket, ServerNit[] klijent, String fajl){
		this.soketZaKom = soket;
		this.klijenti=klijent;
		this.fajl=fajl;
	}
	
	public void ucitajIzfajla(){
		try {
			BufferedReader in = new BufferedReader(new FileReader(fajl));
			boolean kraj = false;
			while(!kraj){
				String pom = in.readLine();
				if(pom==null) kraj=true;
				else{
					Korisnik k = new Korisnik();
					String ime = pom.substring(0, pom.indexOf(';'));
					String sifra = pom.substring(pom.indexOf(';')+1, pom.indexOf('['));
					String kalkulacije = pom.substring(pom.indexOf('[')+1, pom.indexOf(']'));
					k.setIme(ime);
					k.setSifra(sifra);
					k.setKalkulacije(kalkulacije);
					if (!listaRegistrovanihKorisnika.contains(k)) listaRegistrovanihKorisnika.add(k);
				}
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Nema fajla ili nesto drugo");
		}
	}
	public void upisiUfajl(Korisnik k){
		try {
			
			PrintWriter out =new PrintWriter(new BufferedWriter(new FileWriter(fajl)));
			String s=k.toString();
			BufferedReader in = new BufferedReader(new FileReader(fajl));
			boolean kraj = false;
			while(!kraj){
				String pom = in.readLine();
				if(pom==null){
					out.println(s);
					kraj=true;
				}else{
					out.println(pom);
				}
			}
			in.close();
			out.close();
			}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void prikaziListu(String sifra) {
		for(int i=0; i<listaRegistrovanihKorisnika.size(); i++)
			if(listaRegistrovanihKorisnika.get(i).getSifra().equals(sifra))
					izlazniTokKaKlijentu.println(listaRegistrovanihKorisnika.get(i).getKalkulacije());
	}
	
	public void izracunaj( String sifra) {
		String linija;
		izlazniTokKaKlijentu.println("Unesite zeljeni izraz: \n[Za prekid komunikacije mozete pritisnuti b\n za prikaz liste e] ");
		while(true){
			try {
				linija=ulazniTokOdKlijenta.readLine();
				if (linija.equals("e")) {
					prikaziListu(sifra);
					izlazniTokKaKlijentu.println("Unesite zeljeni izraz: \n[Za prekid komunikacije mozete pritisnuti b\n za prikaz liste e] ");
					linija=ulazniTokOdKlijenta.readLine();
				}
				if (linija.equals("b")) {
					izlazniTokKaKlijentu.println("*** Zatvara se konekcija za Vas ***");
					soketZaKom.close();
					break;
				}
				
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
						izlazniTokKaKlijentu.println("Rezultat: "+rez);
					for (int i = 0; i < listaRegistrovanihKorisnika.size(); i++) {
						if (listaRegistrovanihKorisnika.get(i).getSifra().equals(sifra)){
							listaRegistrovanihKorisnika.get(i).setKalkulacije(listaRegistrovanihKorisnika.get(i).getKalkulacije()+" "+linija+"="+rezultat);
							upisiUfajl(listaRegistrovanihKorisnika.get(i));
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
						izlazniTokKaKlijentu.println("Rezultat: "+rez);
						k++;
						izlazniTokKaKlijentu.println("Ostalo vam je jos: "+(3-k)+" kalkulacija");
						if (k==3) {
							izlazniTokKaKlijentu.println("Nemate pravo na vise kalkulacija.");
							izadji();
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
			int postoji=0;
			ime=ulazniTokOdKlijenta.readLine();
			Korisnik k = new Korisnik(); 
			for(int i=0; i<listaRegistrovanihKorisnika.size(); i++){
				if (listaRegistrovanihKorisnika.get(i).equals(ime)){
					postoji++;
				}else{
					izlazniTokKaKlijentu.println("Korisnicko ime vec postoji.");
					return;
				}
			}
			if (postoji==0){
				k.setIme(ime);
			}
			izlazniTokKaKlijentu.println("Unesite sifru. \n[Sifra mora imati minimum 8 karaktera, minimum jedno veliko slovo (A-Z) i minimum jednu cifru (0-9)]");
			sifra=ulazniTokOdKlijenta.readLine();
			k.setSifra(sifra);
			izlazniTokKaKlijentu.println("Uspesno ste se registrovali.");
			listaRegistrovanihKorisnika.add(k);
			upisiUfajl(k);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String prijava() {
		String ime;
		String sifra;
		ucitajIzfajla();
		izlazniTokKaKlijentu.println("Unesite korisnicko ime");
		try {
			ime=ulazniTokOdKlijenta.readLine();
			int postoji=0;
			for(int i=0; i<listaRegistrovanihKorisnika.size(); i++){
				if (listaRegistrovanihKorisnika.get(i).getIme().equals(ime)){
					postoji++;
				}
			}
			if(postoji>0) {
				izlazniTokKaKlijentu.println("Unesite sifru.");
				sifra=ulazniTokOdKlijenta.readLine();
				int brojKorisnika=listaRegistrovanihKorisnika.size();
				for(int i=0; i<brojKorisnika; i++) {
					if(listaRegistrovanihKorisnika.get(i).getIme().equals(ime) &&
							listaRegistrovanihKorisnika.get(i).getSifra().equals(sifra)) {	
								izlazniTokKaKlijentu.println("Uspesno ste se prijavili.");
								return sifra;
							}
					}
			}else {
				izlazniTokKaKlijentu.println("Ne postojite u listi registrovanih korisnika.");
				return "bezuspesno";
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "bezuspesno";
		
		
	}	
	
	public void run(){
		String opcija;
		try {
			ulazniTokOdKlijenta = new BufferedReader(new  InputStreamReader(soketZaKom.getInputStream()));
			izlazniTokKaKlijentu = new PrintStream(soketZaKom.getOutputStream());
			
			izlazniTokKaKlijentu.println("Konekcija je uspesno uspostavljena. \nIzaberite jednu od ponudjenih opcija: \na - kalkulator - 3 kalkulacije (Ako zelite vise kalkulacija, prijavite se) \nb - izlaz\nc - registracija\nd - prijava");
			
			opcija = ulazniTokOdKlijenta.readLine();
			switch (opcija) {
			case "a":
				izracunaj3();
				break;
			case "b":
				izadji();
				break;
			case "c":
				registracija();
				izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator - 3 kalkulacije (Ako zelite vise kalkulacija, prijavite se) \nb - izlaz\nd - prijava\ne - prikazi listu kalkulacija");
				switch (ulazniTokOdKlijenta.readLine()) {
				case "a":
					izracunaj3();
					break;
				case "b":
					izadji();
					break;
				case "d":
					String s = prijava();
					if (!s.equals("bezuspesno")) {
					izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz\ne - prikazi listu kalkulacija");
					switch (ulazniTokOdKlijenta.readLine()) {
					case "a":
						izracunaj(s);
						break;
					case "b":
						izadji();
						break;
					case "e":
						prikaziListu(s);
						break;
					default:
						System.out.println("Nepoznata opcija.");
						break;
					}
					}else {
						izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz");
						switch (ulazniTokOdKlijenta.readLine()) {
						case "a":
							izracunaj3();
							break;
						case "b":
							izadji();
							break;
						default:
							System.out.println("Nepoznata opcija.");
							break;
						}
					}
					break;
				default:
					System.out.println("Nepoznata opcija.");
					break;
				}
				break;
			case "d":
				String s = prijava();
				izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz\nc - rikaz kalkulacija");
				switch (ulazniTokOdKlijenta.readLine()) {
				case "a":
					izracunaj(s);
					break;
				case "b":
					izadji();
					break;
				case "c":
					prikaziListu(s);
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

