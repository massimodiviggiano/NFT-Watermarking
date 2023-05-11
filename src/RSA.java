import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA
{
	public RSA()
	{}
	
	//Creazione chiavi nel percorso passato dall'utente
	public static void createStoreKeys(String publicKeystore, String privateKeystore) throws Exception
	{
        //Generazione coppia di chiavi
        
        //Inizializza un generatore di coppie di chiavi usando RSA
        KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
        
        //Le chiavi sono molto lunghe: 1024 bit sono 128 byte. La forza di RSA è nell'impossibilità pratica di fattorizzare numeri così grandi
        kpg.initialize(1024);
        
        //Genera la coppia
        KeyPair kp=kpg.generateKeyPair();
       
        //Salvataggio chiave pubblica
        
        //Ottieni la versione codificata in X.509 della chiave pubblica (senza cifrare)
        byte[] publicBytes=kp.getPublic().getEncoded();
        
        //Salva nel keystore selezionato dall'utente
        FileOutputStream fos=new FileOutputStream(publicKeystore);
        
        fos.write(publicBytes);
        
        fos.close();
       
        //Salvataggio chiave privata
        
        //ottieni la versione codificata in PKCS#8
        byte[] privateBytes=kp.getPrivate().getEncoded();
       
        fos=new FileOutputStream(privateKeystore);
        
        fos.write(privateBytes);
        
        fos.close();
	}
	
	//Creazione chiavi nel percorso passato dall'utente con ritorno delle chiavi intere tramite HashCode
	public static int[] createKeys(String publicKeystore, String privateKeystore) throws Exception
	{
		int[] keys=new int[2];
		
        //Generazione coppia di chiavi
        
        //Inizializza un generatore di coppie di chiavi usando RSA
        KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
        
        //Le chiavi sono molto lunghe: 1024 bit sono 128 byte. La forza di RSA è nell'impossibilità pratica di fattorizzare numeri così grandi
        kpg.initialize(1024);
        
        //Genera la coppia
        KeyPair kp=kpg.generateKeyPair();
       
        //Salvataggio chiave pubblica
        
        //Ottieni la versione codificata in X.509 della chiave pubblica (senza cifrare)
        byte[] publicBytes=kp.getPublic().getEncoded();
        
        //Salva nel keystore selezionato dall'utente
        FileOutputStream fos=new FileOutputStream(publicKeystore);
        
        fos.write(publicBytes);
        
        fos.close();
       
        //Salvataggio chiave privata
        
        //ottieni la versione codificata in PKCS#8
        byte[] privateBytes=kp.getPrivate().getEncoded();
       
        fos=new FileOutputStream(privateKeystore);
        
        fos.write(privateBytes);
        
        fos.close();
        
        keys[0]=kp.getPublic().hashCode();
        keys[1]=kp.getPrivate().hashCode();
        
        return keys;
	}
	
	/*public static int createPublicKey(String publicKeystore) throws Exception
	{
        //Generazione coppia di chiavi
        
        //Inizializza un generatore di coppie di chiavi usando RSA
        KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
        
        //Le chiavi sono molto lunghe: 1024 bit sono 128 byte. La forza di RSA è nell'impossibilità pratica di fattorizzare numeri così grandi
        kpg.initialize(1024);
        
        //Genera la coppia
        KeyPair kp=kpg.generateKeyPair();
       
        //Salvataggio chiave pubblica
        
        //Ottieni la versione codificata in X.509 della chiave pubblica (senza cifrare)
        byte[] publicBytes=kp.getPublic().getEncoded();
        
        //Salva nel public keystore selezionato dall'utente
        FileOutputStream fos=new FileOutputStream(publicKeystore);
        
        fos.write(publicBytes);
        
        fos.close();
        
        return kp.getPublic().hashCode();
	}
	
	public static int createPrivateKey(String privateKeystore) throws Exception
	{
        //Generazione coppia di chiavi
        
        //Inizializza un generatore di coppie di chiavi usando RSA
        KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
        
        //Le chiavi sono molto lunghe: 1024 bit sono 128 byte. La forza di RSA è nell'impossibilità pratica di fattorizzare numeri così grandi
        kpg.initialize(1024);
        
        //Genera la coppia
        KeyPair kp=kpg.generateKeyPair();
        
        //Salva nel keystore selezionato dall'utente
        FileOutputStream fos=new FileOutputStream(privateKeystore);
       
        //Salvataggio chiave privata
        
        //ottieni la versione codificata in PKCS#8
        byte[] privateBytes=kp.getPrivate().getEncoded();
        
        fos.write(privateBytes);
        
        fos.close();
        
        return kp.getPrivate().hashCode();
	}*/
	
	//Codifica messaggio
	public static void codifica(String publicKeystore, String pathMessaggio, String pathDestinazione) throws Exception
	{
        //Lettura chiave pubblica codificata in X509
        FileInputStream fis=new FileInputStream(publicKeystore);
        
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        
        int i=0;
        
        while((i=fis.read())!=-1) 
        {
        	baos.write(i);
        }
        
        fis.close();
        
        byte[] publicKeyBytes=baos.toByteArray();
        
        baos.close();
       
        //Converto chiave pubblica da X509 a chiave utilizzabile
       
        //Inizializza convertitore da X.509 a chiave pubblica
        X509EncodedKeySpec ks=new X509EncodedKeySpec(publicKeyBytes);
        
        //Inizializza un KeyFactory per ricreare la chiave usando RSA
        KeyFactory kf=KeyFactory.getInstance("RSA");
        
        //Crea una chiave pubblica usando generatePublic di KeyFactory in base alla chiave decodificata da ks
        PublicKey publicKey=kf.generatePublic(ks);
       
        //Lettura file sorgente
        fis=new FileInputStream(pathMessaggio);
        
        baos.reset();
        
        byte[] plainFile;
        
        i=0;
        
        while((i=fis.read())!=-1)
        {
        	baos.write(i);
        }
        
        fis.close();
        
        plainFile=baos.toByteArray();
       
        //Codifica file sorgente
        
        //Inizializzo un cifrario che usa come algoritmo RSA, come modalità ECB e come padding PKCS1
        Cipher c=Cipher.getInstance("RSA/ECB/PKCS1Padding");
        
        //Lo inizializzo dicendo modalità di codifica e chiave pubblica da usare
        c.init(Cipher.ENCRYPT_MODE, publicKey);
        
        //Codifico e metto il risultato in encodeFile
        byte[] encodeFile=c.doFinal(plainFile);
       
        //Salvataggio file codificato
        FileOutputStream fos=new FileOutputStream(pathDestinazione);
        
        fos.write(encodeFile);
        
        fos.close();
	}
	
	//Decodifica messaggio
	public static void decodifica(String privateKeystore, String sorgente, String pathDestinazione) throws Exception 
	{
        //Lettura chiave privata codificata in PKCS#8
        FileInputStream fis=new FileInputStream(privateKeystore);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        
        int i=0;
        
        while((i=fis.read())!=-1)
        {
        	baos.write(i);
        }
        
        fis.close();
        
        byte[] privateKeyBytes=baos.toByteArray();
        
        baos.close();
       
        //Converto chiave privata PKCS8 in chiave normale
       
        PKCS8EncodedKeySpec ks=new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kf=KeyFactory.getInstance("RSA");
        PrivateKey privateKey=kf.generatePrivate(ks);
       
        //Lettura file codificato
        fis=new FileInputStream(sorgente);
        
        baos.reset();
        
        byte[] codFile;
        
        i=0;
        
        while((i=fis.read())!=-1)
        {
        	baos.write(i);
        }
        
        fis.close();
        
        codFile=baos.toByteArray();
       
        Cipher c=Cipher.getInstance("RSA/ECB/PKCS1Padding");
        
        c.init(Cipher.DECRYPT_MODE, privateKey);
        
        byte[] plainFile=c.doFinal(codFile);
       
        //Salvataggio file
        FileOutputStream fos=new FileOutputStream(pathDestinazione);
        
        fos.write(plainFile);
        
        fos.close();
	}
}
