package dev.zontreck.amp;

import io.papermc.lib.PaperLib;
import net.milkbowl.vault.economy.Economy;

import java.util.Timer;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * AutoMoneyPlugin
 *
 * @author Copyright (c) zontreck. Licensed under the GPLv3.
 */
public class AutoMoneyPlugin extends JavaPlugin {
  Timer task = new Timer();
  Economy economy = null;

  @Override
  public void onEnable() {
    PaperLib.suggestPaper(this);

    saveDefaultConfig();
    reloadConfig();

    // Apply configuration
    Configuration.g_dPaymentAmount = getConfig().getDouble("amountToGive", 0.25);
    Configuration.g_iPaymentInterval = getConfig().getInt("duration", 60);

    // Setup the economy
    if (!setupEconomy()) {
      getLogger().severe("No economy plugin found. Disabling plugin.");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }

    // Schedule the task
    reschedule();
  }

	
	/**
	 * Sets up the vault economy.
	 *
	 * @return True if the economy was set up, false otherwise.
	 */
	private boolean setupEconomy() {
		RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		
		return economy != null;
	}

  public void reschedule() {
    task.scheduleAtFixedRate(new java.util.TimerTask() {
        @Override
        public void run() {
          if(Configuration.PaymentEnabled()) {
            // Pay all online players
            getServer().getOnlinePlayers().forEach(player -> {
              //player.sendMessage("You have been paid " + Configuration.g_dPaymentAmount + " for being online.");
              //player.giveExp((int) Configuration.g_dPaymentAmount);
              
            });
          }
        }
    }, 0L, Configuration.g_iPaymentInterval * 1000L);
  }

  @Override
  public void onDisable() {
    // Stop the timer task. Save configuration
    saveConfig();
    task.cancel();
  }
}
