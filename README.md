<p>
  <img src="https://img.shields.io/bstats/players/7354">
  <img src="https://img.shields.io/bstats/servers/7354">
</p>

# Unmaintained
I haven't touched this in a while as I've been mostly out of the MC scene. It should continue to work fine on servers that are still on the supported MC versions (1.8 1.9 1.10 1.11 1.12 1.13 1.14 1.15 1.16 1.17 1.18 1.19) but I won't make any guarantees. Thanks for using something I've created, I'm glad it helped out people at a time where alternatives were not as good.

# MineableSpawners
This plugin allows players to mine and drop spawners, receive them via command and more!​


## Current features:

* Define if users can mine spawners.
* Change spawner types using /spawner (type).
* Give spawners to players using /spawnergive (player) (type) (amount).
* Disable spawner renaming in anvils.
* Define if users can change spawner types using spawn eggs.
* & more!


### Commands: Main command "/mineablespawners" or "ms"

   * "/ms give <player> <type> <amount>" => Give a player spawners.
   * "/ms set <mob>" => Set a spawner type by looking at it.
   * "/ms types" => List all available types.
   * "/ms reload" => Reload the configuration.

### Permissions:

   * "mineablespawners.give" => Access to /ms give
   * "mineablespawners.set" => Access to /ms set
   * "mineablespawners.set.(type)" => Access to a specific type of mob in /spawner. Only applicable if require-individual-permission is set to true in the config.
   * "mineablespawners.mine" => Permission to mine a spawner.
   * "mineablespawners.mine.<type>" => Permission to mine a specific type of spawner.
   * "mineablespawners.nosilk" => Bypass the silk touch requirement when mining spawners.
   * "mineablespawners.eggchange" => Ability to change spawners by right clicking them with spawn eggs.
   * "mineablespawners.eggchange.<type>" => Access to specific types of mobs when using spawn eggs to change spawner types.
   * "mineablespawners.types" => List available entity types.
   * "mineablespawners.bypass" => Bypass spawner placing/mining requirements.

### Dependencies:

   * Optional: Vault [https://dev.bukkit.org/projects/vault] and a compatible economy plugin like Essentials for the economy features.

### Metrics:
We use bStats to collect anonymous metrics (only grabs stuff like playercount, java version, server version and such). This helps me get some information about how used my plugin is and keeps me motivated. If you wish to turn it off you can in ./plugins/bStats/config.yml

Everything is configurable and explained in the configuration file!

Download link: [https://www.spigotmc.org/resources/mineablespawners.59921/](https://www.spigotmc.org/resources/mineablespawners.59921/)
