package dev.zontreck.amp;

//import io.papermc.lib.PaperLib;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

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
    //PaperLib.suggestPaper(this);

    saveDefaultConfig();
    reloadConfig();

    // Apply configuration
    Configuration.g_dPaymentAmount = getConfig().getDouble("amountToGive", 0.25);
    Configuration.g_iPaymentInterval = getConfig().getInt("duration", 60);
    Configuration.g_sBankAccount = getConfig().getString("bank", "");
    Configuration.g_bVerbose = getConfig().getBoolean("verbose", false);

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
            if(Configuration.g_bVerbose) {
              getLogger().info("Paying all online players " + Configuration.g_dPaymentAmount);
            }
            // Pay all online players
            getServer().getOnlinePlayers().forEach(player -> {
              //player.sendMessage("You have been paid " + Configuration.g_dPaymentAmount + " for being online.");
              //player.giveExp((int) Configuration.g_dPaymentAmount);
              // Check if a bank account is used.
              if(Configuration.g_sBankAccount.isEmpty()) {
                economy.depositPlayer(player, Configuration.g_dPaymentAmount);
              } else {
                EconomyResponse ecoReply = economy.bankWithdraw(Configuration.g_sBankAccount, Configuration.g_dPaymentAmount);
                if(ecoReply.type == ResponseType.SUCCESS) {
                  economy.depositPlayer(player, Configuration.g_dPaymentAmount);
                } else {
                  getLogger().severe("Failed to withdraw from bank account: " + ecoReply.errorMessage);
                }
              }
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
