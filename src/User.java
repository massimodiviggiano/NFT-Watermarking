import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class User
{
	private int USER_ID;
	private String nomeUtente;
	private String password;
	private int publicKey;
	private int privateKey;
	private String publicKeystore;
	private String privateKeystore;
	private ArrayList<NFT> NFTs;
	
	public User(int USER_ID, String nomeUtente, String password, String publicKeystore, String privateKeystore) throws Exception
	{
		this.USER_ID=USER_ID;
		this.nomeUtente=nomeUtente;
		this.password=password;
		this.publicKeystore=publicKeystore;
		this.privateKeystore=privateKeystore;
		
		int[] keys=RSA.createKeys(publicKeystore, privateKeystore);
		
		this.publicKey=keys[0];
		this.privateKey=keys[1];
		
		NFTs=new ArrayList<NFT>();
	}
	
	public int getUserId()
	{
		return this.USER_ID;
	}
	
	public void setUserID(int USER_ID)
	{
		this.USER_ID=USER_ID;
	}
	
	public String getNomeUtente()
	{
		return this.nomeUtente;
	}
	
	public void setNomeUtente(String nomeUtente)
	{
		this.nomeUtente=nomeUtente;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(String password)
	{
		this.password=password;
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
	
	//Revoca chiavi e riassegno chiavi
	public void revocaChiavi() throws Exception
	{
		int[] keys=RSA.createKeys(publicKeystore, privateKeystore);
		
		this.publicKey=keys[0];
		this.privateKey=keys[1];
	}
	
	public void addNFT(NFT nft)
	{
		NFTs.add(nft);
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
	
	public boolean hasNFT()
	{
		if(this.NFTs.isEmpty())
			return false;
		else
			return true;
	}
	
	public int countNFT()
	{
		return this.NFTs.size();
	}
	
	//Calcolo parte W1 watermark
	public int W1(NFT nft)
	{
		/*if(NFTs.contains(nft))
		{*/
		return this.USER_ID|(nft.getDateCrea().getDayOfMonth()*nft.getDateCrea().getMonthValue()*nft.getDateCrea().getYear());
		//}
		
		//return 0;
	}
	
	//Codifica W1 e invio messaggio al MarketPlace
	public void W1codifica(String publicKeystoreMP, NFT nft) throws Exception
	{
		FileWriter fileout=new FileWriter("Messaggi/W1");
		
		String str=Integer.toString(this.W1(nft));

        for(int i=0;i<str.length();i++)
            fileout.write(str.charAt(i));
        
        fileout.close();
        
        RSA.codifica(publicKeystoreMP, "Messaggi/W1", "Messaggi/W1codificato");
	}
	
	public String toString()
	{
		String s="";
		
		s+="USER_ID: "+this.USER_ID+"\nNome utente: "+this.nomeUtente+"\nPassword: "+this.password+"\nPublic key: "+this.publicKey+"\nPrivate key: "+this.privateKey+"\nPublic key store: "+this.publicKeystore+"\nPrivate key store: "+this.privateKeystore;
		
		if(this.NFTs.isEmpty())
		{
			s+="\nNFT: empty";
		}
		else
		{
			s+="\nNFT:";
			
			for(int i=0;i<this.NFTs.size();++i)
			{
				s+="\n\n"+this.NFTs.get(i).toString();
			}
		}
		
		return s;
	}
	
	public String toStringNFT()
	{
		String s="";
				
		if(this.NFTs.isEmpty())
		{
			s+="NFT: empty";
		}
		else
		{
			s+="NFT:";
			
			for(int i=0;i<this.NFTs.size();++i)
			{
				s+="\n\n"+this.NFTs.get(i).toString();
			}
		}
		
		return s;
	}
	
	public String toStringElencoNFT()
	{
		String s="";

		s+="NFT:";
		
		for(int i=0;i<this.NFTs.size();++i)
		{
			s+="\n"+i+". "+this.NFTs.get(i).getNome();
		}
		
		return s;
	}
}
