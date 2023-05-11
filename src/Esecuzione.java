import java.security.*;
import java.security.spec.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.crypto.*;
import javax.imageio.ImageIO;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;

public class Esecuzione
{	
	public static void main(String args[]) throws Exception
	{
		Scanner in=new Scanner(System.in);
		//Istanziamo classe MarketPlace e User
		MarketPlace marketplace=new MarketPlace("MarketPlace/Pubblica/publicKeystore", "MarketPlace/Pubblica/privateKeystore");
		User user, user1, user2, user3;
		NFT nft1, nft2;
		String nomeUtente, password;
		int scelta;
		
		user1=marketplace.addUser("max", "val", "User/Pubbliche/maxpub", "User/Private/maxpri");
		user2=marketplace.addUser("val", "max", "User/Pubbliche/valpub", "User/Private/valpri");
		user3=marketplace.addUser("fel", "ix", "User/Pubbliche/felpub", "User/Private/felpri");
		
		//Menù
		System.out.print("0. Esci\n1. Login\n2. Registrazione\n\nScelta: ");
		scelta=in.nextInt();
		
		while(scelta<0 || scelta>2)
		{
			System.out.print("\nErrore! Scelta non ammessa, riprova.\n\n0. Esci\n1. Login\n2. Registrazione\n\nScelta: ");
			scelta=in.nextInt();
		}
		
		while(scelta!=0)
		{
			if(scelta==1)
			{
				System.out.print("\nNome utente: ");
				nomeUtente=in.next();
				
				System.out.print("Password: ");
				password=in.next();
				
				while(!marketplace.userPresente(nomeUtente, password))
				{
					System.out.println("\nErrore! Nome utente e password non presenti, riprova.");
					
					System.out.print("\nNome utente: ");
					nomeUtente=in.next();
					
					System.out.print("Password: ");
					password=in.next();
				}
				
				user=marketplace.getUser(nomeUtente, password);
				
				System.out.print("\nLogin avvenuto con successo!\n");
				
				//Menù utente loggato
				System.out.print("\nBENVENUTO!\n\n0. Logout\n1. Visualizza informazioni account\n2. Visualizza i tuoi NFT\n3. Assegna nuove chiavi\n4. Aggiungi NFT\n5. Visualizza chiavi pubbliche\n6. Vendi NFT\n\nScelta: ");
				scelta=in.nextInt();
				
				while(scelta<0 || scelta>6)
				{
					System.out.print("\nErrore! Scelta non ammessa, riprova.\n\n0. Logout\n1. Visualizza informazioni account\n2. Visualizza i tuoi NFT\n3. Assegna nuove chiavi\n4. Aggiungi NFT\n5. Visualizza chiavi pubbliche\n6. Vendi NFT\n\nScelta: ");
					scelta=in.nextInt();
				}
				
				while(scelta!=0)
				{
					if(scelta==1)
					{
						System.out.print("\n"+user.toString());
					}
					else if(scelta==2)
					{
						System.out.print("\n"+user.toStringNFT());
					}
					else if(scelta==3)
					{
						user.revocaChiavi();
						
						System.out.print("\nRevoca e cambio chiavi avvenuto con successo!");
					}
					else if(scelta==4)
					{
						LocalDate dc;
						String URL, nome, descrizione;
						NFT nft;
						
						dc=LocalDate.of(2010, 4, 5);
						
						System.out.print("\nURL immagine: ");
						URL=in.next();
						
						System.out.print("\nNome: ");
						nome=in.next();
						
						System.out.print("\nDescrizione: ");
						descrizione=in.next();
						
						nft=marketplace.addNFT(dc, URL, nome, descrizione, user);
						
						if(nft!=null)
						{
							System.out.print("\nNFT aggiunto con successo!");
						}
						else
						{
							System.out.print("\n\nErrore! Non e' possibile aggiungere questo NFT");
						}
					}
					else if(scelta==5)
					{
						System.out.print("\nNome utente ---> chiave pubblica:");
						System.out.print(marketplace.chiaviPubblicheUtenti());
					}
					else if(scelta==6)
					{
						//Controllo se ha almeno un NFT
						if(user.hasNFT())
						{
							int iNFT, venditore;
							NFT newNFT;
							
							System.out.print("\n"+user.toStringElencoNFT());
							
							System.out.print("\n\nDimmi l'NFT che vuoi vendere: ");
							iNFT=in.nextInt();
							
							while(iNFT<0 || iNFT>=user.countNFT())
							{
								System.out.print("\nErrore! NFT non valido\n\nDimmi l'NFT che vuoi vendere: ");
								iNFT=in.nextInt();
							}
							
							System.out.print("\n"+marketplace.toStringUsers());
							
							System.out.print("\n\nDimmi a chi vuoi vendere l'NFT: ");
							venditore=in.nextInt();
							
							while(venditore<0 || venditore>=marketplace.countUsers())
							{
								System.out.print("\nErrore! Utente non valido\n\nDimmi a chi vuoi vendere l'NFT: ");
								venditore=in.nextInt();
							}
							
							User soldTo=marketplace.getUser(venditore);
							NFT nftSold=user.getNFT(iNFT);
							
							newNFT=marketplace.cambioProprietarioNFT(nftSold, soldTo);
							
							if(newNFT!=null)
							{
								user.getNFTs().remove(iNFT);
								System.out.print("\nNFT venduto con successo!");
							}
							else
								System.out.print("\nErrore! Non e' stato possibile vendere l'NFT");
						}
						else
						{
							System.out.print("\nErrore! Non hai nessun NFT in tuo possesso");
						}
					}
					
					System.out.print("\n\n0. Logout\n1. Visualizza informazioni account\n2. Visualizza i tuoi NFT\n3. Assegna nuove chiavi\n4. Aggiungi NFT\n5. Visualizza chiavi pubbliche\n6. Vendi NFT\n\nScelta: ");
					scelta=in.nextInt();
					
					while(scelta<0 || scelta>6)
					{
						System.out.print("\nErrore! Scelta non ammessa, riprova.\n\n0. Logout\n1. Visualizza informazioni account\n2. Visualizza i tuoi NFT\n3. Assegna nuove chiavi\n4. Aggiungi NFT\n5. Visualizza chiavi pubbliche\n6. Vendi NFT\n\nScelta: ");
						scelta=in.nextInt();
					}
				}
				
				System.out.print("\nLogout avvenuto con successo!\n");
			}
			else
			{
				System.out.print("\nNome utente: ");
				nomeUtente=in.next();
				
				System.out.print("Password: ");
				password=in.next();
				
				while(marketplace.nomeUtentePresente(nomeUtente))
				{
					System.out.println("\nErrore! Nome utente gia' esistente, riprova.");
					
					System.out.print("\nNome utente: ");
					nomeUtente=in.next();
					
					System.out.print("Password: ");
					password=in.next();
				}
				
				user=marketplace.addUser(nomeUtente, password, "User/Pubbliche/"+nomeUtente+"pub", "User/Private/"+nomeUtente+"pri");
				
				System.out.print("\nRegistrazione avvenuta con successo!\n");
			}
			
			System.out.print("\n0. Esci\n1. Login\n2. Registrazione\n\nScelta: ");
			scelta=in.nextInt();
			
			while(scelta<0 || scelta>2)
			{
				System.out.print("\nErrore! Scelta non ammessa, riprova.\n\n0. Esci\n1. Login\n2. Registrazione\n\nScelta: ");
				scelta=in.nextInt();
			}
		}
		
		System.out.println("\nCiao!");
	}
}