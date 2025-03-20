package dev.zontreck.amp;

import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * AutoMoneyPlugin
 *
 * @author Copyright (c) zontreck. Licensed under the GPLv3.
 */
public class AutoMoneyPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    PaperLib.suggestPaper(this);

    saveDefaultConfig();
  }
}
