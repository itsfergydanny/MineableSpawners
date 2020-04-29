# MineableSpawners
This plugin allows players to mine and drop spawners, receive them via command and more!​


## Current features:

    Define if users can mine spawners.​
    Change spawner types using /spawner (type).​
    Give spawners to players using /spawnergive (player) (type) (amount).​
    Disable spawner renaming in anvils.​
    Define if users can change spawner types using spawn eggs.​
    & more!​

​
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


Everything is configurable and explained in the configuration file!

Download link: [https://www.spigotmc.org/resources/mineablespawners.59921/](https://www.spigotmc.org/resources/mineablespawners.59921/)
