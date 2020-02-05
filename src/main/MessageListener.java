package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public static void main(String[] args)
			throws LoginException, RateLimitedException, InterruptedException
	{
		JDA jda = new JDABuilder(AccountType.BOT).setToken("TOKEN").buildBlocking();
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



				String patternString = "";

				Pattern pattern = Pattern.compile(patternString);

				Matcher matcher = pattern.matcher(msg);
				boolean matches = matcher.matches();
				if (matches){
					System.out.printf("[%s][%s] %s: %s\n",guild.getName(), textChannel.getName(), author.getName(), msg);
				}
				if (msg.equals("ùping"))
				{
					channel.sendMessage("pong!").queue();
				}
				else if (msg.equals("ùhelp"))
				{
					channel.sendMessage("```"
							+ "Voici toute les commandes.\n"
							+ "  ùping - pong!\n"
							+ "  ùhelp - affiche toutes les commandes\n"
							+ "  ùtest - test\n"
							+ "```").queue();
				}



				else if (msg.equals("ùjoin")){
					
				}
				
				/*else if (msg.substring(0, 5).equals(pront+"pront")){
					if (msg.length() == 8) {
						pront = msg.substring(7, 8);
					} else {
						channel.sendMessage("Que 1 char pour le pront salo").queue();
					}
				}*/


				else if (matches){
					channel.sendMessage("C'est pas une commande!").queue();
				}				

			}
		}
		else
		{

			System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
					event.getMessage().getContentDisplay());
		}

	}
}
