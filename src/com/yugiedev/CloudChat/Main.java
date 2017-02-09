package com.yugiedev.CloudChat;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
        extends JavaPlugin
        implements Listener {
    Placeholder player_name = new Placeholder("%player_name%");
    Placeholder player_display_name = new Placeholder("%player_display_name%");
    Placeholder vault_group_prefix = new Placeholder("%vault_group_prefix%");
    Placeholder vault_group_suffix = new Placeholder("%vault_group_suffix%");
    Placeholder vault_player_prefix = new Placeholder("%vault_player_prefix%");
    Placeholder vault_player_suffix = new Placeholder("%vault_player_suffix%");
    Placeholder message = new Placeholder("%message%", "%2$s");
    public static Chat chat = null;
    String format;
    String join;
    String leave;

    public void onEnable() {
        if (!setupChat()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", new Object[]{getDescription().getName()}));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
        getLogger().info("Enabled CloudChat");
    }

    public void onDisable() {
        getLogger().info("Disabled CloudChat");
    }

    public void loadConfig() {
        getConfig().addDefault("format", this.vault_group_prefix.getPlaceholder() + this.player_name.getPlaceholder() + " " + this.vault_group_suffix.getPlaceholder() + "&7» &7" + this.message.getPlaceholder());
        getConfig().addDefault("join", "&7[&a+&7] " + this.vault_group_prefix.getPlaceholder() + this.player_name.getPlaceholder());
        getConfig().addDefault("leave", "&7[&c-&7] " + this.vault_group_prefix.getPlaceholder() + this.player_name.getPlaceholder());
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private boolean setupChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = (Chat) rsp.getProvider();
        return chat != null;
    }

    void getPlaceholder() {
        this.format = ChatColor.translateAlternateColorCodes('&', getConfig().getString("format"));
        this.join = ChatColor.translateAlternateColorCodes('&', getConfig().getString("join"));
        this.leave = ChatColor.translateAlternateColorCodes('&', getConfig().getString("leave"));
    }

    public String replacePlaceholder(Placeholder p) {
        return this.format.replace(p.getPlaceholder(), p.getVariable());
    }

    public String joinplaceholder(Placeholder p) {
        return this.join.replace(p.getPlaceholder(), p.getVariable());
    }

    public String leaveplaceholder(Placeholder p) {
        return this.leave.replace(p.getPlaceholder(), p.getVariable());
    }



    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onChat(AsyncPlayerChatEvent e) {
        this.player_name.setVariable(e.getPlayer().getName());
        this.player_display_name.setVariable(e.getPlayer().getDisplayName());

        this.vault_group_prefix.setVariable(chat.getGroupPrefix("world", chat.getPrimaryGroup(e.getPlayer())));
        this.vault_group_suffix.setVariable(chat.getGroupSuffix("world", chat.getPrimaryGroup(e.getPlayer())));
        getPlaceholder();
        this.format = replacePlaceholder(this.player_name);
        this.format = replacePlaceholder(this.player_display_name);
        this.format = replacePlaceholder(this.vault_group_prefix);
        this.format = replacePlaceholder(this.vault_group_suffix);
        this.format = replacePlaceholder(this.message);
        e.setFormat(this.format);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent e) {
        this.player_name.setVariable(e.getPlayer().getName());
        this.player_display_name.setVariable(e.getPlayer().getDisplayName());

        this.vault_group_prefix.setVariable(chat.getGroupPrefix("world", chat.getPrimaryGroup(e.getPlayer())));
        this.vault_group_suffix.setVariable(chat.getGroupSuffix("world", chat.getPrimaryGroup(e.getPlayer())));
        getPlaceholder();
        this.join = joinplaceholder(this.player_name);
        this.join = joinplaceholder(this.player_display_name);
        this.join = joinplaceholder(this.vault_group_prefix);
        this.join = joinplaceholder(this.vault_group_suffix);
        this.join = joinplaceholder(this.message);
        e.setJoinMessage(this.join);

    }    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)

    private void onPlayerQuit(PlayerQuitEvent e) {
        this.player_name.setVariable(e.getPlayer().getName());
        this.player_display_name.setVariable(e.getPlayer().getDisplayName());

        this.vault_group_prefix.setVariable(chat.getGroupPrefix("world", chat.getPrimaryGroup(e.getPlayer())));
        this.vault_group_suffix.setVariable(chat.getGroupSuffix("world", chat.getPrimaryGroup(e.getPlayer())));
        getPlaceholder();
        this.leave = leaveplaceholder(this.player_name);
        this.leave = leaveplaceholder(this.player_display_name);
        this.leave = leaveplaceholder(this.vault_group_prefix);
        this.leave = leaveplaceholder(this.vault_group_suffix);
        this.leave = leaveplaceholder(this.message);
        e.setQuitMessage(this.leave);
    }
}