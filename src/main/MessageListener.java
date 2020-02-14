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

	static String personne = "Titan le sang";
	static String idPersonne = "183719402343104512";


	Date lastSnipe = new Date((new Date().getTime()-3600*1000));//heure lancement bot - 1 heure
	String lastSniper = "Pas encore Snipe depuis le lancement du bot";

	public static void main(String[] args)
			throws LoginException, RateLimitedException, InterruptedException
	{
		if(args.length==0 || args.length>2) {
			System.out.println("Le bot à besoin de paramètres : \n"
					+ "Si 1 paramètre : String token. token est le token du bot et la personne à Sniper de base par le bot est Acrkenver (id:183719402343104512)\n"
					+ "Si 2 paramètre : String token, String idPersonne. token est le token du bot et idPersonne est l'id de la personne à Sniper\n"
					+ "Exemple : java SniperBot Njc***gwNDk0O4AyMTA4***3-XkAg***e0Zdq***kz-57-fBzkdb***ODgY 201064975848964096");
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
		personne = jda.getUserById(idPersonne).getName();
		//System.out.println(personne);
		System.out.println("Bot En Ligne");
		jda.addEventListener(new MessageListener());
	}


	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{

		if (event.isFromType(ChannelType.TEXT))
		{
			User author = event.getAuthor();
			boolean bot = author.isBot();

			if (!bot) {







				MessageChannel channel = event.getChannel();
				Message message = event.getMessage();
				String msg = message.getContentDisplay();
				Guild guild = event.getGuild(); 
				TextChannel textChannel = event.getTextChannel();



				//String patternString = "";

				//Pattern pattern = Pattern.compile(patternString);

				//Matcher matcher = pattern.matcher(msg);
				//boolean matches = matcher.matches();

				System.out.printf("[%s][%s] %s: %s\n",guild.getName(), textChannel.getName(), author.getName(), msg);

				/*if (msg.equals("ùping"))
				{
					channel.sendMessage("pong!").queue();
				}*/
				if (msg.equals("@"+personne+" vu"))
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
				}

				if (msg.equals("ùscore"))
				{
					try(BufferedReader reader =
							new BufferedReader(new FileReader(new File("ListeServeur/"+guild.getId())));
							){
						String line = "";
						String res = "```" + "Liste des Snipe de "+personne+" :\n";
						reader.readLine();
						while((line = reader.readLine()) != null){
							String[] tab = line.split(":");
							//System.out.println(guild.getMemberById(tab[0]).getUser().getName()+" à "+tab[1]);
							res += guild.getMemberById(tab[0]).getUser().getName()+" a "+tab[1]+"\n";
						}
						res += "```";
						channel.sendMessage(res).queue();
					} catch(IOException ioe){
						System.out.println("Erreur lors de la lecture");
					}
				}else if (msg.equals("ùlast")) {
					long temps = (3600 - (new Date().getTime()/1000 - lastSnipe.getTime()/1000));


					//channel.sendMessage(lastSnipe.toString()).queue();


					String res = "```" + "Dernier Snipe : " +lastSnipe.toString() +"\n";

					if(temps>0) {
						res += "Temps avant prochain Snipe : "+ temps/60 + " minutes et " + temps%60 + " secondes\n";
					} else {
						res += "Vous pouvez Sniper "+personne+"!\n";
					}

					res += "Dernier Snipe par : " + lastSniper;

					res += "```";
					channel.sendMessage(res).queue();
				}



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


				/*else if (msg.substring(0, 5).equals(pront+"pront")){
					if (msg.length() == 8) {
						pront = msg.substring(7, 8);
					} else {
						channel.sendMessage("Que 1 char pour le pront salo").queue();
					}
				}*/


			}
		}


	}


	private boolean trouvable() {
		return (new Date().getTime()/1000 - lastSnipe.getTime()/1000)>3600;
	}


	private void ajouterPoint(Guild guild, User author) {
		String res ="";
		boolean dansLeFichier = false;
		try(BufferedReader reader =
				new BufferedReader(new FileReader(new File("ListeServeur/"+guild.getId())));
				){
			String line = "";
			reader.readLine();
			while((line = reader.readLine()) != null){
				String[] tab = line.split(":");
				//System.out.println(guild.getMemberById(tab[0]).getUser().getName()+":"+tab[1]);

				if(tab[0].equals(author.getId())) {
					res += guild.getMemberById(tab[0]).getUser().getId()+":"+(Integer.parseInt(tab[1]) + 1)+"\n";
					dansLeFichier = true;
				} else {
					res += guild.getMemberById(tab[0]).getUser().getId()+":"+tab[1]+"\n";
				}
			}

		} catch(IOException ioe){
			if(new File("ListeServeur/"+guild.getId()).isFile()) {
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
						new File("ListeServeur/"+guild.getId())));
				){
			writer.append(guild.getName()+"\n");
			writer.append(res);
			writer.flush(); // Si tu libère le buffer (ici ce n'est pas utile)
		} catch(IOException ioe){
			System.out.println("Erreur écriture");
		}


		/*BufferedReader reader = new BufferedReader(new FileReader(new File(guild.getName())));
		try(BufferedWriter writer = 
				new BufferedWriter(new FileWriter(
						new File(guild.getName())));
				){

			//Ajoute un point
			int points = 0;

				String line = "";
				while((line = reader.readLine()) != null){
					String[] tab = line.split(":");
					if(tab[0].equals(author.getId())) {
						//System.out.println(guild.getMemberById(tab[0]).getUser().getName()+" à "+tab[1]);
						points = Integer.parseInt(tab[1]); //ajoute le point
						//System.out.println("test");
						writer.append(author.getId()+":"+(points+1));//ecrit l'author du tireur
						System.out.println(author.getId()+":"+(points+1));
					} else {
						writer.append(tab[0]+":"+tab[1]);//recrit la ligne
						System.out.println(tab[0]+":"+tab[1]);
					}
					System.out.println(author.getId()+":"+(points+1));
					writer.newLine(); // Aller à la ligne suivante
				}





			//écrit dans le fichier
			writer.flush(); // Si tu libère le buffer (ici ce n'est pas utile)
		} catch(IOException ioe){
			System.out.println("Erreur écriture");
		}*/
	}



}
