package StrongChat;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_7_R4.ChatClickable;
import net.minecraft.server.v1_7_R4.ChatHoverable;
import net.minecraft.server.v1_7_R4.ChatMessage;
import net.minecraft.server.v1_7_R4.ChatModifier;
import net.minecraft.server.v1_7_R4.EnumChatFormat;
import net.minecraft.server.v1_7_R4.EnumClickAction;
import net.minecraft.server.v1_7_R4.EnumHoverAction;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import nz.co.lolnet.james137137.FactionChat.FactionChatAPI;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.massivecraft.factions.entity.UPlayer;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
public class Listeners implements Listener
{
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e)
    {
	PermissionManager pex = PermissionsEx.getPermissionManager();
	PermissionUser p = pex.getUser(e.getPlayer());
	String initialMessage="";
	UPlayer fp = UPlayer.get(e.getPlayer());
	if(!FactionChatAPI.getChatMode(p.getPlayer()).equalsIgnoreCase("public"))
	{
	    e.setCancelled(true);
	    return;
	}
	String hoverText=ChatColor.DARK_GREEN+getRole(fp)+" in "+ChatColor.DARK_GREEN+fp.getFactionName()+"\n"+ChatColor.AQUA+"Real Name: "+p.getName();
	if(e.getPlayer().getDisplayName()!=null)
	{
	    initialMessage = colorize(p.getPrefix())+" "+colorize(p.getPlayer().getDisplayName());
	}
	else
	{
	    initialMessage = colorize(p.getPrefix())+" "+ChatColor.WHITE+ChatColor.stripColor(p.getName());
	}
	if(ChatColor.stripColor(e.getMessage())==" "){e.setCancelled(true);}
	IChatBaseComponent comp = constructComp(initialMessage+": "+ChatColor.DARK_AQUA+e.getMessage(),hoverText, "/msg "+p.getName());	
	sendToAllPlayers(comp);
	e.setCancelled(true);
    }
    public String getRole(UPlayer fp)
    {
	String facRole = fp.getRole().name().toLowerCase();
	facRole = facRole.substring(0, 1).toUpperCase() + facRole.substring(1);
	return facRole;
    }
    public IChatBaseComponent constructComp(String message, String hoverText,String clickText)
    {
	IChatBaseComponent comp = new ChatMessage(message);
	ChatModifier modifier = new ChatModifier()
	.setColor(EnumChatFormat.DARK_AQUA)
	.a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage(hoverText)))
	.setChatClickable((new ChatClickable(EnumClickAction.SUGGEST_COMMAND, clickText)));
	comp.setChatModifier(modifier);
	return comp;
    }
    @SuppressWarnings("deprecation")
    public void sendToAllPlayers(IChatBaseComponent ic)
    {
	PacketPlayOutChat packet = new PacketPlayOutChat(ic, true);
	for(Player p : Bukkit.getOnlinePlayers())
	{
	    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}
    }
    public static String colorize(String msg)
    {
	String coloredMsg = "";
	for (int i = 0; i < msg.length(); i++)
	{
	    if (msg.charAt(i) == '&')
		coloredMsg += '§';
	    else
		coloredMsg += msg.charAt(i);
	}
	return coloredMsg;
    }

    public static String deFormat(String msg)
    {
	String[] replace =
	{ "&k", "&l", "&m", "&n", "&o", "&r" };
	for (String s : replace)
	{
	    msg = msg.replace(s, "");
	}
	return msg;
    }
}
