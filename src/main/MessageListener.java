package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter
{

	static String personne = "Titan le sang";//nom actuelle de la cible
	static String idPersonne = "183719402343104512";//id de titan 




	public static void main(String[] args)
			throws LoginException, RateLimitedException, InterruptedException
	{
		if(args.length==0 || args.length>2) {
			System.out.println("Le bot à besoin de paramètres : \n"
					+ "Si 1 paramètre : String token. token est le token du bot et la personne à Sniper de base par le bot est Acrkenver (id:183719402343104512)\n"
					+ "Si 2 paramètre : String token, String idPersonne. token est le token du bot et idPersonne est l'id de la personne à Sniper\n"
					+ "Exemple : java -jar SniperBot.jar Njc***gwNDk0O4AyMTA4***3-XkAg***e0Zdq***kz-57-fBzkdb***ODgY 201064975848964096");

			return;
		} else if(args.length==2) {
			idPersonne = args[1];
		}

		//System.out.println("token : "+args[0]);
		//System.out.println("idPersonne : "+args[1]);

		if(!new File("ListeServeur/").exists()) {
			if(new File("ListeServeur").mkdirs()) {
				System.out.println("Création dossier ListeServeur");
			} else {
				System.out.println("**Dossier ListeServeur manquant!**");
			}
		}
		JDA jda = new JDABuilder(AccountType.BOT).setToken(args[0]).buildBlocking();
		
		
		System.out.println("Bot En Ligne");
		jda.addEventListener(new MessageListener());
	}


	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{

		if (event.isFromType(ChannelType.TEXT))
		{
			User author = event.getAuthor(); //Charge le User de l'event

			if (!author.isBot()) {







				MessageChannel channel = event.getChannel(); // Charge le salon de l'event
				Message message = event.getMessage(); // Charge le message de l'event
				String msg = message.getContentDisplay(); // Charge le texte du message
				Guild guild = event.getGuild(); // Charge le serveur de l'event
				TextChannel textChannel = event.getTextChannel(); // Charge le salon texte de l'event

				personne = guild.getMemberById(idPersonne).getEffectiveName(); //Charge le bon nom de personne
				
				
				
				System.out.printf("[%s][%s] %s: %s\n",guild.getName(), textChannel.getName(), author.getName(), msg); // Affiche l'event dans le terminal

				/*if (msg.equals("ùping"))
				{
					channel.sendMessage("pong!").queue();
				}*/

				//Affiche le score
				if (msg.equals("ùscore"))
				{
					try(BufferedReader reader =
							new BufferedReader(new FileReader(new File("ListeServeur/"+guild.getId()+"."+idPersonne)));
							){
						String line = reader.readLine();
						String[] tab = line.split(";");
						String res = "```" + "Liste des Snipe de "+personne+" sur "+tab[0]+" :\n";

						while((line = reader.readLine()) != null){
							tab = line.split(":");
							//System.out.println(guild.getMemberById(tab[0]).getUser().getName()+" à "+tab[1]);

							res += guild.getMemberById(tab[0]).getUser().getName()+" a "+tab[1]+"\n";

						}
						res += "```";
						channel.sendMessage(res).queue();
					} catch(IOException ioe){
						System.out.println("Erreur lors de la lecture");
					}

					
				}
				
				//Affiche les infos en rapport avec le dernier Snipe
				else if (msg.equals("ùlast")) {


					String res = "";

					//channel.sendMessage(lastSnipe.toString()).queue();


					try(BufferedReader reader =
							new BufferedReader(new FileReader(new File("ListeServeur/"+guild.getId()+"."+idPersonne)));
							){
						String line = "";
						line = reader.readLine();
						String[] tab = line.split(";");
						Date time = new Date(Long.parseLong(tab[2]));
						long temps = (3600 - (new Date().getTime()/1000 - time.getTime()/1000));
						//System.out.println(line);

						res = "```" + "Dernier Snipe : " + time +"\n";
						if(temps>0) {
							res += "Temps avant prochain Snipe : "+ temps/60 + " minutes et " + temps%60 + " secondes\n";
						} else {
							res += "Vous pouvez Sniper "+personne+"!\n";
						}

						res += "Dernier Snipe par : " + guild.getMemberById(tab[1]).getEffectiveName();
						res += "```";
						channel.sendMessage(res).queue();
					} catch(IOException ioe){
						if(new File("ListeServeur/"+guild.getId()).isFile()) {
							System.out.println("Erreur lors de la lecture");
						}
					}




				}


				//Affiche la page d'aide
				else if (msg.equals("ùhelp"))
				{
					channel.sendMessage("```"
							+ "Voici toute les commandes.\n"
							+ "  @"+personne+" vu - Permet de Snipe "+personne+"!\n"
							+ "\n"
							+ "  ùlast  - Affiche toutes les infos du dernier Snipe!\n"
							+ "  ùscore - Affiche le score des utilisateur sur le serveur!\n"
							+ "\n"
							+ "  ùhelp  - affiche toutes les commandes\n"
							+ "```").queue();
				}


				// New detection of Sniper more flex
				else if(msg.startsWith("@"+personne) && msg.contains(" ") && msg.contains("vu")){
					
					String decoupe = msg.split(" ")[1];
					//verif si il y a 2 espaces
					if(decoupe.equals("")) {
						decoupe = msg.split(" ")[2];
					}
					if(decoupe.contains("vu") && decoupe.length() < 5) {
						//channel.sendMessage(guild.getMemberById(idPersonne).getOnlineStatus().toString()).queue();
						if(guild.getMemberById(idPersonne).getOnlineStatus().toString().equals("ONLINE") && trouvable(guild)) {
							//channel.sendMessage("Bien vue").queue();
							
							ajouterPoint(guild,author);


							message.addReaction("\uD83D\uDC4D").queue();
							//SetTimer
						} else {
							message.addReaction("\uD83D\uDC4E").queue();
						}
					}

				}
				//Old detection of snipe
				/*else if (msg.equals("@"+personne+" vu"))
				{
					//channel.sendMessage(guild.getMemberById(idPersonne).getOnlineStatus().toString()).queue();
					if(guild.getMemberById(idPersonne).getOnlineStatus().toString().equals("ONLINE") && trouvable()) {
						//channel.sendMessage("Bien vue").queue();

						ajouterPoint(guild,author);
						lastSnipe = new Date();
						lastSniper = author.getName();
						message.addReaction("\uD83D\uDC4D").queue();
						//SetTimer
					} else {
						message.addReaction("\uD83D\uDC4E").queue();
					}
				}*/


				
			}
		}


	}

	/**
	 * Permet de savoir si la personne est trouvable
	 * @return true si trouvable false sinon
	 */
	private boolean trouvable(Guild guild) {
		
		if(!new File("ListeServeur/"+guild.getId()+"."+idPersonne).isFile()) {
			return true;
		}
		try(BufferedReader reader =
				new BufferedReader(new FileReader(new File("ListeServeur/"+guild.getId()+"."+idPersonne)));
				){
			String line = reader.readLine();

			String[] tab = line.split(";");
			//System.out.println(tab[2]);
			if(new Date().getTime() - Long.parseLong(tab[2]) > 3600000) {
				return true;
			}

		} catch(IOException ioe){
			if(new File("ListeServeur/"+guild.getId()).isFile()) {
				System.out.println("Erreur lors de la lecture");
			}
		}
		return false;
	}

	/**
	 * Ajoute un point au User en parametre si il peut optenir le point
	 * @param guild serveur sur lequel ajouter le point
	 * @param author User qui optiendra le point
	 */
	private void ajouterPoint(Guild guild, User author) {
		
		String res ="";
		boolean dansLeFichier = false;
		try(BufferedReader reader =
				new BufferedReader(new FileReader(new File("ListeServeur/"+guild.getId()+"."+idPersonne)));
				){
			String line = "";
			reader.readLine();
			while((line = reader.readLine()) != null){
				String[] tab = line.split(":");
				System.out.println(guild.getMemberById(tab[0]).getUser().getName()+":"+tab[1]);

				if(tab[0].equals(author.getId())) {
					res += guild.getMemberById(tab[0]).getUser().getId()+":"+(Integer.parseInt(tab[1]) + 1)+"\n";
					dansLeFichier = true;
				} else {
					res += guild.getMemberById(tab[0]).getUser().getId()+":"+tab[1]+"\n";
				}
			}

		} catch(IOException ioe){
			
			if(new File("ListeServeur/"+guild.getId()+"."+idPersonne).isFile()) {
				System.out.println("Erreur lors de la lecture");
			} else {
				System.out.println("Creation du fichier : "+guild.getId()+", serveur name : "+guild.getName());
			}
		}
		if(!dansLeFichier) {
			res += author.getId()+":1\n";
		}
		try(BufferedWriter writer = 
				new BufferedWriter(new FileWriter(
						new File("ListeServeur/"+guild.getId()+"."+idPersonne)));
				){
			writer.append(ligneNormalisation(guild.getName(), author.getId(), new Date().getTime()+"")+"\n");
			writer.append(res);
			writer.flush(); // Si tu libère le buffer (ici ce n'est pas utile)
		} catch(IOException ioe){
			System.out.println("Erreur écriture");
		}


		
	}

	/**
	 * Pas super utile mais c'est propre, permet de normaliser l'écriture dans un fichier de la premier ligne
	 * @param serverName Nom du serveur
	 * @param lastFinderName Dernier User qui à trouve la personne
	 * @param lastFinderDate Date du dernier Snipe
	 * @return String normalisé des parametres
	 */
	private String ligneNormalisation(String serverName, String lastFinderName, String lastFinderDate) {
		return serverName+";"+lastFinderName+";"+lastFinderDate;
	}



}
