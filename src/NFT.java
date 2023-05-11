import java.time.LocalDate;
import java.util.Date;

public class NFT
{
	private int NFT_ID;
	private LocalDate DATECREA; //Data di creazione dell'opera
	private LocalDate DATEMI; //Data di minting in blockchain
	private int CHKSUM; //Checksum
	private String URL;
	private String URLoriginale;
	private String nome;
	private String descrizione;
	private User proprietario;
	
	public NFT(int NFT_ID, LocalDate DATECREA, LocalDate DATEMI, String URL, String URLoriginale, String nome, String descrizione, User proprietario)
	{
		this.NFT_ID=NFT_ID;
		this.DATECREA=DATECREA;
		this.DATEMI=DATEMI;
		this.CHKSUM=Checksum.calculateLRC(Checksum.toBytes(proprietario.getPublicKey()));
		this.URL=URL;
		this.URLoriginale=URLoriginale;
		this.nome=nome;
		this.descrizione=descrizione;
		this.proprietario=proprietario;
	}
	
	public int getNftId()
	{
		return this.NFT_ID;
	}
	
	public void setNftId(int NFT_ID)
	{
		this.NFT_ID=NFT_ID;
	}
	
	public LocalDate getDateCrea()
	{
		return this.DATECREA;
	}
	
	public void setDateCrea(LocalDate DATECREA)
	{
		this.DATECREA=DATECREA;
	}
	
	public LocalDate getDateMi()
	{
		return this.DATEMI;
	}
	
	public void setDateMi(LocalDate DATEMI)
	{
		this.DATEMI=DATEMI;
	}
	
	public int getChksum()
	{
		return this.CHKSUM;
	}
	
	public void setChksum(int CHKSUM)
	{
		this.CHKSUM=CHKSUM;
	}
	
	public String getUrl()
	{
		return this.URL;
	}
	
	public void setUrl(String URL)
	{
		this.URL=URL;
	}
	
	public String getUrlOriginale()
	{
		return this.URLoriginale;
	}
	
	public void setUrlOriginale(String URLoriginale)
	{
		this.URLoriginale=URLoriginale;
	}
	
	public String getNome()
	{
		return this.nome;
	}
	
	public void setNome(String nome)
	{
		this.nome=nome;
	}
	
	public String getDescrizione()
	{
		return this.descrizione;
	}
	
	public void setDescrizione(String descrizione)
	{
		this.descrizione=descrizione;
	}
	
	public User getProprietario()
	{
		return this.proprietario;
	}
	
	public void setProprietario(User proprietario)
	{
		this.proprietario=proprietario;
	}
	
	public int getPublicKey()
	{
		return this.proprietario.getPublicKey();
	}
	
	public String toString()
	{
		String s="";
		
		s+="NFT_ID: "+this.NFT_ID+"\nDATECREA: "+this.DATECREA+"\nDATEMI: "+this.DATEMI+"\nCHKSUM: "+this.CHKSUM+"\nURL: "+this.URL+"\nURL originale: "+this.URLoriginale+"\nNome: "+this.nome+"\nDescrizione: "+this.descrizione+"\nProprietario: "+this.proprietario.getNomeUtente()+"\nChiave pubblica proprietario: "+this.getPublicKey();
		
		return s;
	}
}
