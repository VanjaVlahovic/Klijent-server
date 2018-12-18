
public class Korisnik {
	private String ime;
	private String sifra;
	private String kalkulacije;
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getSifra() {
		return sifra;
	}
	public void setSifra(String sifra) {
		char x;
		boolean velikoSlovo=false;
		boolean cifra = false;
		if (sifra.length()<8)
			throw new RuntimeException("Sifra ne sme imati manje od 8 karaktera");
		for (int i = 0; i < sifra.length(); i++) {
			x = sifra.charAt(i);
			if (Character.isUpperCase(x))
				velikoSlovo=true;
			if (Character.isDigit(x))
				cifra=true;
		}
		if (!velikoSlovo)
			throw new RuntimeException("Sifra mora imati bar jedno veliko slovo");
		if (!cifra)
			throw new RuntimeException("Sifra mora imati bar jednu cifru");
		if (velikoSlovo && cifra)
			this.sifra = sifra;
	}
	
	
	public String getKalkulacije() {
		return kalkulacije;
	}
	public void setKalkulacije(String kalkulacije) {
		this.kalkulacije = kalkulacije;
	}
	@Override
	public String toString() {
		return ime + ";" + sifra + "[" + kalkulacije + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Korisnik))
			throw new RuntimeException("Morate uneti objekat klase Korisnik");
		Korisnik k = (Korisnik)(obj);
		if (sifra==k.getSifra()) return true;
		else return false;
	}
	
}
