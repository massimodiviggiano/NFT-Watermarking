import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

public class MarketPlace
{
	private int MARKETPLACE_ID;
	private int codiceWatermark;
	private String publicKeystore;
	private String privateKeystore;
	private int publicKey;
	private int privateKey;
	private ArrayList<User> users;
	private ArrayList<NFT> NFTs;
	private Map<Integer, String> extranctionsKeys;
	private static int user_id=1;
	private static int nft_id=1;
	
	public MarketPlace(String publicKeystore, String privateKeystore) throws Exception
	{
		this.MARKETPLACE_ID=1;
		this.codiceWatermark=4533245;
		this.publicKeystore=publicKeystore;
		this.privateKeystore=privateKeystore;
		
		int[] keys=RSA.createKeys(publicKeystore, privateKeystore);
		
		this.publicKey=keys[0];
		this.privateKey=keys[1];
		
		this.users=new ArrayList<User>();
		this.NFTs=new ArrayList<NFT>();
		this.extranctionsKeys=new HashMap<Integer, String>();
	}
	
	public int getMarketplaceId()
	{
		return this.MARKETPLACE_ID;
	}
	
	
	public void setMarketplaceId(int MARKETPLACE_ID)
	{
		this.MARKETPLACE_ID=MARKETPLACE_ID;
	}
	
	public int getCodiceWatermark()
	{
		return this.codiceWatermark;
	}
	
	public void setCodiceWatermark(int codiceWatermark)
	{
		this.codiceWatermark=codiceWatermark;
	}
	
	public boolean equalsCodiceWatermark(int codiceWatermark)
	{
		if(this.codiceWatermark==codiceWatermark)
			return true;
		else
			return false;
	}
	
	public String getPublicKeystore()
	{
		return this.publicKeystore;
	}
	
	public void setPublicKeystore(String publicKeystore)
	{
		this.publicKeystore=publicKeystore;
	}
	
	public String getPrivateKeystore()
	{
		return this.privateKeystore;
	}
	
	public void setPrivateKeystore(String privateKeystore)
	{
		this.privateKeystore=privateKeystore;
	}
	
	public int getPublicKey()
	{
		return this.publicKey;
	}
	
	public void setPublicKey(int publicKey)
	{
		this.publicKey=publicKey;
	}
	
	public int getPrivateKey()
	{
		return this.privateKey;
	}
	
	public void setPrivateKey(int privateKey)
	{
		this.privateKey=privateKey;
	}
	
	public void addUser(User user)
	{
		users.add(user);
	}
	
	public User addUser(String nomeUtente, String password, String publicKeystore, String privateKeystore) throws Exception
	{
		User user=new User(MarketPlace.user_id, nomeUtente, password, publicKeystore, privateKeystore);
		
		this.users.add(user);
		
		MarketPlace.user_id++;
		
		return user;
	}
	
	public ArrayList<User> getUsers()
	{
		return this.users;
	}
	
	public String chiaviPubblicheUtenti()
	{
		int i=0;
		String s="";
		
		for(;i<this.users.size();++i)
		{
			s+="\n"+this.users.get(i).getNomeUtente()+" ---> "+this.users.get(i).getPublicKey();
		}
		
		return s;
	}
	
	public User getUser(int i)
	{
		if(i<this.users.size())
			return this.users.get(i);
		else
			return null;
	}
	
	public User getUser(String nomeUtente, String password)
	{
		int i=0;
		
		for(;i<this.users.size();++i)
		{
			if(this.users.get(i).getNomeUtente().compareTo(nomeUtente)==0 && this.users.get(i).getPassword().compareTo(password)==0)
			{
				return this.users.get(i);
			}
		}
		
		return null;
	}
	
	public boolean setUser(int i, User user)
	{
		if(i<this.users.size())
		{
			this.users.set(i, user);
			
			return true;
		}
		else
			return false;
	}
	
	public int countUsers()
	{
		return this.users.size();
	}
	
	public boolean userPresente(String nomeUtente, String password)
	{
		for(int i=0;i<users.size();++i)
		{
			if(this.users.get(i).getNomeUtente().compareTo(nomeUtente)==0 && this.users.get(i).getPassword().compareTo(password)==0)
				return true;
		}
		
		return false;
	}
	
	public boolean nomeUtentePresente(String nomeUtente)
	{
		for(int i=0;i<users.size();++i)
		{
			if(users.get(i).getNomeUtente().compareTo(nomeUtente)==0)
				return true;
		}
		
		return false;
	}
	
	//Controllo se la chiave pubblica è ancora valida e non è stata revocata
	public boolean chiavePubblicaValida(int publicKey)
	{
		for(int i=0;i<users.size();++i)
		{
			if(users.get(i).getPublicKey()==publicKey)
				return true;
		}
		
		return false;
	}
	
	//Controlla se chiave pubblica corrisponde all'utente
	public boolean chiavePrivataCheck(User user, int publicKey)
	{
		if(user.getPublicKey()==publicKey)
			return true;
		
		return false;
	}
	
	//Controllo se la coppia chiavi pubblica e privata sono valide ancora o sono state revocate
	public boolean chiaviValide(int publicKey, int privateKey)
	{
		for(int i=0;i<users.size();++i)
		{
			if(users.get(i).getPublicKey()==publicKey && users.get(i).getPrivateKey()==privateKey)
				return true;
		}
		
		return false;
	}
	
	public void addNFT(NFT nft)
	{
		NFTs.add(nft);
	}
	
	public NFT addNFT(LocalDate DATECREA, String URL, String nome, String descrizione, User proprietario) throws Exception
	{
		String URLKbit="NFT/Kbit/K-bit"+URL, URLwatermark="NFT/Watermark/watermark"+URL;
		URL="NFT/Originale/"+URL;
		LocalDate DATEMI=LocalDate.now();
		NFT nft=new NFT(MarketPlace.nft_id, DATECREA, DATEMI, URLwatermark, URL, nome, descrizione, proprietario);
		
		//Controllo se chiave pubblica è valida, altrimenti l'operazione viene annullata
		if(this.chiavePubblicaValida(proprietario.getPublicKey()))
		{
			BufferedImage immagine=ImageIO.read(new File(URL));
			BufferedImage contenuto;

	        int larghezza=immagine.getWidth();
	        int altezza=immagine.getHeight();
	        
	        int primoPixel=immagine.getRGB(0, 0)*(-1);
	        
	        if(this.equalsCodiceWatermark(primoPixel))
	        {
	        	 int indiceExtractionsKeys=-immagine.getRGB(0, 1);
	        	 int publicKey;
	        	 
	        	 String u=this.extranctionsKeys.get(indiceExtractionsKeys);
	        	 
	        	 BufferedImage immagine2=ImageIO.read(new File(u));
	        	 
	        	 publicKey=immagine.getRGB(0, 2)^immagine2.getRGB(0, 2);
	        	 
	        	 if(proprietario.getPublicKey()==publicKey)
	        	 {
	        		 if(this.chiavePrivataCheck(proprietario, proprietario.getPublicKey()))
	        		 {
	        			 System.out.print("\nWatermark gia' presente, l'opera e' gia' di tua proprieta'");
	        		 }
	        		 else
	        		 {
	        			 System.out.print("\nErrore! Chiave pubblica e privata non corrispondono");
	        		 }
	        	 }
	        	 else
	        	 {
	        		 System.out.print("\nWatermark gia' presente! L'opera appartiene gia' ad un altro utente");
	        	 }
	        	 
	        	 return null;
	        }
	        else
	        {
		        contenuto=new BufferedImage(larghezza, altezza, 1);
	            
		        for(int l=0;l<larghezza;l++)
		        {
		            for(int a=0;a<altezza;a++)
		            {
		            	contenuto.setRGB(l, a, immagine.getRGB(l, a)^proprietario.getPublicKey());
		            }
		        }
		        
		        contenuto.setRGB(0, 0, -this.codiceWatermark);
		        contenuto.setRGB(0, 1, -MarketPlace.nft_id);
		                    
	            File file=new File(URLwatermark);
	            ImageIO.write(contenuto, "png", file);
	            
	            //Generazione immagine originale
	            BufferedImage watermark=ImageIO.read(new File(URLwatermark));
	            
	            for(int l=0;l<larghezza;l++)
	            {
	                for(int a=0;a<altezza;a++)
	                {
	                	contenuto.setRGB(l, a, watermark.getRGB(l, a)^proprietario.getPublicKey());
	                }
	            }
	            
	            file=new File("immagineWatermark.png");
	            ImageIO.write(contenuto, "png", file);
		        
				this.addNFT(nft);
				
				proprietario.addNFT(nft);
				
				this.extranctionsKeys.put(MarketPlace.nft_id, URL);
				
				MarketPlace.nft_id++;
				
				return nft;
	        }
		}
		else
		{
			System.out.print("\nErrore! Chiave proprietario non piu' valida");
			
			return null;
		}
	}
	
	//TO DO
	public NFT cambioProprietarioNFT(NFT nft, User nuovoProprietario) throws Exception
	{
		if(nft.getProprietario().getPublicKey()!=nuovoProprietario.getPublicKey())
		{
			//Controllo se chiave pubblica è valida, altrimenti l'operazione viene annullata
			if(this.chiavePubblicaValida(nuovoProprietario.getPublicKey()))
			{
			    BufferedImage immagine=ImageIO.read(new File(nft.getUrlOriginale()));
				BufferedImage contenuto;
	
		        int larghezza=immagine.getWidth();
		        int altezza=immagine.getHeight();
	        	int publicKey;
	        	 
	            publicKey=nuovoProprietario.getPublicKey();
	        	 
	        	contenuto=new BufferedImage(larghezza, altezza, BufferedImage.TYPE_INT_RGB);
		            
		        for(int l=0;l<larghezza;l++)
		        {
		            for(int a=0;a<altezza;a++)
		            {
		            	contenuto.setRGB(l, a, immagine.getRGB(l, a)^publicKey);
		            }
		        }
		        
		        contenuto.setRGB(0, 0, -this.codiceWatermark);
		        contenuto.setRGB(0, 1, -nft.getNftId());
		        
		        File file=new File(nft.getUrl());
	            ImageIO.write(contenuto, "png", file);
		        
		        nft.setProprietario(nuovoProprietario);
		        nft.setChksum(Checksum.calculateLRC(Checksum.toBytes(nuovoProprietario.getPublicKey())));
		        
		        nuovoProprietario.addNFT(nft);
		        
		        //Generazione immagine originale
	            BufferedImage watermark=ImageIO.read(new File(nft.getUrl()));
	            
	            for(int l=0;l<larghezza;l++)
	            {
	                for(int a=0;a<altezza;a++)
	                {
	                	contenuto.setRGB(l, a, watermark.getRGB(l, a)^nuovoProprietario.getPublicKey());
	                }
	            }
	            
	            file=new File("immagineWatermark.png");
	            ImageIO.write(contenuto, "png", file);
				
				return nft;
			}
			else
			{
				System.out.print("\nErrore! Chiave proprietario non piu' valida");
				
				return null;
			}
		}
		else
		{
			System.out.print("\nErrore! Stai cercando di vendere l'NFT a te stesso");
			
			return null;
		}
	}	

	public ArrayList<NFT> getNFTs()
	{
		return this.NFTs;
	}
	
	public NFT getNFT(int i)
	{
		if(i<this.NFTs.size())
			return this.NFTs.get(i);
		else
			return null;
	}
	
	public boolean setNFT(int i, NFT nft)
	{
		if(i<this.NFTs.size())
		{
			this.NFTs.set(i, nft);
			
			return true;
		}
		else
			return false;
	}
	
	public Map<Integer, String> getExtractionsKeys()
	{
		return this.extranctionsKeys;
	}
	
	//Calcolo parte watermark W2
	public int W2(NFT nft)
	{
		//if(NFTs.contains(nft))
		//{
		return nft.getProprietario().getUserId()|(nft.getDateMi().getDayOfMonth()*nft.getDateMi().getMonthValue()*nft.getDateMi().getYear());
		//}
		
		//return 0;
	}
	
	//Controllo la paternità dell'opera da NFT già creato
	public boolean controlloProprietaOpera(User user, NFT nft) throws Exception
	{
		User proprietario=nft.getProprietario();
		
		if(proprietario.getPublicKey()==user.getPublicKey() && proprietario.getPrivateKey()==user.getPrivateKey())
			return true;
		else
			return false;
	}
	
	public int getUserId()
	{
		return MarketPlace.user_id;
	}
	
	public int getNftId()
	{
		return MarketPlace.nft_id;
	}
	
	public String toString()
	{
		String s="";
		
		s+="MARKETPLACE_ID: "+this.MARKETPLACE_ID+"\nPublic key store: "+this.publicKeystore+"\nPrivate key store: "+this.privateKeystore;
		
		s+="\nUSERS: ";
		
		if(this.users.isEmpty())
		{
			s+="empty";
		}
		else
		{
			for(int i=0;i<this.users.size();++i)
			{
				s+="\n"+this.users.get(i).toString();
			}
		}
		
		if(this.NFTs.isEmpty())
		{
			s+="\nNFT: empty";
		}
		else
		{
			s+="\nNFT: ";
			
			for(int i=0;i<this.NFTs.size();++i)
			{
				s+="\n"+this.NFTs.get(i).toString();
			}
		}
		
		return s;
	}
	
	public String toStringUsers()
	{
		String s="Users:";
		
		for(int i=0;i<this.users.size();++i)
		{
			s+="\n"+i+". "+this.users.get(i).getNomeUtente();
		}
		
		return s;
	}
}
