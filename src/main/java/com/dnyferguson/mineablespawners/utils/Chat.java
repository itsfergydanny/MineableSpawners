package com.dnyferguson.mineablespawners.utils;

import org.bukkit.ChatColor;

public class Chat {
  public static String format(String msg) {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }

  public static String uppercaseStartingLetters(String msg) {
    String[] words = msg.split("_");
    String[] wordsFormatted = new String[words.length];
    int count = 0;
    for (String word : words) {
      String formattedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
      wordsFormatted[count] = formattedWord;
      count++;
    }

    return String.join(" ", wordsFormatted);
  }
}
