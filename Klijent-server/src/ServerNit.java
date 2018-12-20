import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;

public class ServerNit extends Thread {
	
	BufferedReader ulazniTokOdKlijenta=null;
	PrintStream izlazniTokKaKlijentu = null;
	Socket soketZaKom=null;
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
			String pom = in.readLine();
			while(!kraj){
				pom=in.readLine();
				if(pom==null) kraj=true;
				else{
					Korisnik k = new Korisnik();
					String ime = pom.substring(0, pom.indexOf(';'));
					String sifra = pom.substring(pom.indexOf(';')+1, pom.indexOf('['));
					String kalkulacije = pom.substring(pom.indexOf('[')+1, pom.indexOf(']'));
					k.setIme(ime);
					k.setSifra(sifra);
					k.setKalkulacije(kalkulacije);
					int postoji=0;
					for (int i = 0; i < listaRegistrovanihKorisnika.size(); i++)
						if (listaRegistrovanihKorisnika.get(i).getSifra().equals(sifra))
							postoji++;
					if(kalkulacije.equals("null") && postoji==0) listaRegistrovanihKorisnika.add(k);
					else{
						if (!kalkulacije.equals("null"))
						for (int i = 0; i < listaRegistrovanihKorisnika.size(); i++) {
							if (listaRegistrovanihKorisnika.get(i).getSifra().equals(k.getSifra())){
								if(listaRegistrovanihKorisnika.get(i).getKalkulacije().equals("null"))
									listaRegistrovanihKorisnika.get(i).setKalkulacije(kalkulacije);
								else
									if(!listaRegistrovanihKorisnika.get(i).getKalkulacije().contains(kalkulacije)){
										listaRegistrovanihKorisnika.get(i).setKalkulacije(listaRegistrovanihKorisnika.get(i).getKalkulacije()+" "+kalkulacije);
									}
							}
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Nema fajla");
		}
	}
	public void upisiUfajl(Korisnik k, String novaK){
		try {
			 FileWriter fw = new FileWriter(fajl,true);
			 Korisnik pom = k;
			 pom.setKalkulacije(novaK);
			 String s=pom.toString();
			
			 fw.write("\n"+s);
			 fw.close();
		
			
			}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void upisiUfajlNovogKorisnika(Korisnik k){
		try {
			 FileWriter fw = new FileWriter(fajl,true);
			 String s=k.toString();
			
			 fw.write("\n"+s);
			 fw.close();
		
			
			}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void prikaziListu(String sifra) {
		ucitajIzfajla();
		for(int i=0; i<listaRegistrovanihKorisnika.size(); i++)
			if(listaRegistrovanihKorisnika.get(i).getSifra().equals(sifra))
					izlazniTokKaKlijentu.println(listaRegistrovanihKorisnika.get(i).getKalkulacije());
	}
	public String kalkulacije (String linija){
		
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
				return rez;
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
				return rez;
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
				return rez;
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
				return rez;
			}
		}
		return rez;
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
				String rez = kalkulacije(linija);
				while(rez.equals("nepoznat, mozda je greska u pisanju izraza.")){
					izlazniTokKaKlijentu.println("Unesite zeljeni izraz:");
					linija=ulazniTokOdKlijenta.readLine();
					if (linija.equals("b")){
						izadji();
						return;
					}
					rez=kalkulacije(linija);
				}
				izlazniTokKaKlijentu.println("Rezultat: "+rez);
				for (int i = 0; i < listaRegistrovanihKorisnika.size(); i++) {
					if (listaRegistrovanihKorisnika.get(i).getSifra().equals(sifra)){
						listaRegistrovanihKorisnika.get(i).setKalkulacije(listaRegistrovanihKorisnika.get(i).getKalkulacije()+" "+linija+"="+rez);
						izlazniTokKaKlijentu.println("ubacili smo kalkulaciju");
						upisiUfajl(listaRegistrovanihKorisnika.get(i), linija+"="+rez);
					}
					
				}
			} catch (IOException e1) {
				System.err.println("Nije unet odgovarajuci izraz");
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
					izadji();
					return;
				}
				String rez = kalkulacije(linija);
				while(rez.equals("nepoznat, mozda je greska u pisanju izraza.")){
					izlazniTokKaKlijentu.println("Pogresno unet izraz!!! Pokusajte ponovo...");
					izlazniTokKaKlijentu.println("Unesite zeljeni izraz:");
					linija=ulazniTokOdKlijenta.readLine();
					if (linija.equals("b")) {
						izadji();
						return;
					}
					rez=kalkulacije(linija);
				}
				izlazniTokKaKlijentu.println("Rezultat: "+rez);
				k++;
				izlazniTokKaKlijentu.println("Ostalo vam je jos: "+(3-k)+" kalkulacija");
				if (k==3) {
				izlazniTokKaKlijentu.println("Nemate pravo na vise kalkulacija.");
				izadji();
						}
			} catch (IOException e1) {
				System.err.println("ulazni tok nije obradjen");
			}
		}
		
	}
	public void izadji() {
		izlazniTokKaKlijentu.println("***Zatvara se konekcija za Vas ***");
		try {
			soketZaKom.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void registracija() {
		ucitajIzfajla();
		String ime;
		String sifra;
		izlazniTokKaKlijentu.println("Unesite jedinstveno korisnicko ime.");
		try {
			int postoji=0;
			boolean jedinstveno = false;
			Korisnik k = new Korisnik(); 
			while(jedinstveno==false){
				ime=ulazniTokOdKlijenta.readLine(); 
				for(int i=0; i<listaRegistrovanihKorisnika.size(); i++){
					if (listaRegistrovanihKorisnika.get(i).getIme().equals(ime)){
						postoji++;
					}
				}
				if (postoji==0){
					k.setIme(ime);
					jedinstveno=true;
				}else{
					izlazniTokKaKlijentu.println("Korisnicko ime vec postoji. Unesite neko drugo");
					postoji=0;
				}
			}
			izlazniTokKaKlijentu.println("Unesite sifru. \n[Sifra mora imati minimum 8 karaktera, minimum jedno veliko slovo (A-Z) i minimum jednu cifru (0-9)]");
			sifra=ulazniTokOdKlijenta.readLine();
			
			while(k.setSifra(sifra)==false){
				izlazniTokKaKlijentu.println("niste ispunili zahteve sifre. Molimo Vas unesite neku drugu sifru");
				sifra=ulazniTokOdKlijenta.readLine();
			}
			
			izlazniTokKaKlijentu.println("Uspesno ste se registrovali.");
			listaRegistrovanihKorisnika.add(k);
			upisiUfajlNovogKorisnika(k);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String prijava() {
		String ime;
		String sifra;
		ucitajIzfajla();
		boolean nazad = false;
		boolean uspesnoIme=false;
		boolean uspesnaSifra=false;
		izlazniTokKaKlijentu.println("Unesite korisnicko ime");
		try {
			ime=ulazniTokOdKlijenta.readLine();
			while(uspesnoIme==false && nazad==false){
			int postoji=0;
			for(int i=0; i<listaRegistrovanihKorisnika.size(); i++){
				if (listaRegistrovanihKorisnika.get(i).getIme().equals(ime)){
					postoji++;
				}
			}
			if(postoji==1) {
				uspesnoIme=true;
				izlazniTokKaKlijentu.println("Unesite sifru.");
				sifra=ulazniTokOdKlijenta.readLine();
				while(uspesnaSifra==false){
					int brojKorisnika=listaRegistrovanihKorisnika.size();
					for(int i=0; i<brojKorisnika; i++) {
						if(listaRegistrovanihKorisnika.get(i).getIme().equals(ime) &&
								listaRegistrovanihKorisnika.get(i).getSifra().equals(sifra)) {	
									izlazniTokKaKlijentu.println("Uspesno ste se prijavili.");
									uspesnaSifra=true;
									return sifra;
						}
					}
					izlazniTokKaKlijentu.println("Uneli ste pogresnu sifru. Pokusajte ponovo...Ili ako zelite da izadjete iz prijave pritisnite b");
					sifra = ulazniTokOdKlijenta.readLine();
					if (sifra.equals("b"))
						return "bezuspesno";
				}
			}else {
				izlazniTokKaKlijentu.println("Ne postojite u listi registrovanih korisnika.Pokusajte ponovo. Ako zelite da napustite proces prijavljivanja pritisnite b");
				ime=ulazniTokOdKlijenta.readLine();
				if (ime.equals("b")){
					return "bezuspesno";
				}
			}
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
				izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator - 3 kalkulacije (Ako zelite vise kalkulacija, prijavite se) \nb - izlaz\nd - prijava");
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
					izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz\ne - prikaz kalkulacija");
					switch (ulazniTokOdKlijenta.readLine()) {
					case "a":
						izracunaj(s);
						break;
					case "b":
						izadji();
						break;
					case "e":
						prikaziListu(s);
						izracunaj(s);
						break;
					default:
						izlazniTokKaKlijentu.println("Nepoznata opcija");
						izadji();
						break;
					}
					}else {
						izlazniTokKaKlijentu.println("Niste se uspesno prijavili");
						izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator (3 kalkulacije) \nb - izlaz");
						switch (ulazniTokOdKlijenta.readLine()) {
						case "a":
							izracunaj3();
							break;
						case "b":
							izadji();
							break;
						default:
							izlazniTokKaKlijentu.println("Nepoznata opcija");
							izadji();
							break;
						}
					}
					break;
				default:
					izlazniTokKaKlijentu.println("Nepoznata opcija");
					izadji();
					break;
				}
				break;
			case "d":
				String s = prijava();
				
				if(s.equals("bezuspesno")){
					izlazniTokKaKlijentu.println("Neuspesno prijavljivanje");
					izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator (3 kalkulacije) \nb - izlaz");
					switch (ulazniTokOdKlijenta.readLine()) {
					case "a":
						izracunaj3();
						break;
					case "b":
						izadji();
						break;
					default:
						izlazniTokKaKlijentu.println("Nepoznata opcija");
						izadji();
						break;
					}
				}else{
					izlazniTokKaKlijentu.println("Izaberite jednu od ponudjenih opcija: \na - kalkulator \nb - izlaz\ne - prikaz kalkulacija");
					switch (ulazniTokOdKlijenta.readLine()) {
					case "a":
						izracunaj(s);
						break;
					case "b":
						izadji();
						break;
					case "e":
						prikaziListu(s);
						izracunaj(s);
						break;
					default:
						izlazniTokKaKlijentu.println("Nepoznata opcija");
						izadji();
						break;
				}
				}
				break;
			default:
				izlazniTokKaKlijentu.println("Nepoznata opcija");
				izadji();
				break;
			}
			
		} catch (IOException ex) {
			System.err.println("Nagli prekid komunikacije, bez izlaska!");
		}
		for (int i = 0; i <=9; i++)
			if(klijenti[i]==this)
				klijenti[i]=null;
	}
}

