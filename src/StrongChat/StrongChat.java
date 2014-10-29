package StrongChat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StrongChat extends JavaPlugin
{
    public void onEnable()
    {
	PluginManager pm = Bukkit.getPluginManager();
	pm.registerEvents(new Listeners(), this);
    }
}
