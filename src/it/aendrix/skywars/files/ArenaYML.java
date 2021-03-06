package it.aendrix.skywars.files;

import it.aendrix.skywars.arena.Border;
import it.aendrix.skywars.items.enums.State;
import it.aendrix.skywars.main.utils.utils;
import it.aendrix.skywars.skywars.SkyWarsArena;
import it.aendrix.skywars.skywars.SkyWarsTeamArena;
import it.aendrix.skywars.skywars.SkyWarsType;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaYML implements FileYML {
    private static final File file = new File(directory + File.separator + "arena.yml");

    private static final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public ArenaYML() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getObject(String name) {
        if (cfg.getString("arenas." + name) == null)
            return null;
        if (cfg.getString("arenas." + name + ".arena").equalsIgnoreCase("SKYWARS")) {
            Border border;
            Location[] spawns = new Location[cfg.getInt("arenas." + name + ".maxPlayers")];
            utils.mergeArray(spawns, utils.getLocationList(cfg, "arenas." + name + ".spawn"));
            Location corner1 = utils.getLocationString(cfg, "arenas." + name + ".corner1");
            Location corner2 = utils.getLocationString(cfg, "arenas." + name + ".corner2");
            try {
                assert corner1 != null;
                assert corner2 != null;

                border = new Border(corner1, corner2);
            } catch (NullPointerException e) {
                border = null;
            }
            SkyWarsArena arena = new SkyWarsArena(cfg.getString("arenas." + name + ".name"), cfg.getInt("arenas." + name + ".maxPlayers"), cfg.getInt("arenas." + name + ".minPlayers"), cfg.getInt("arenas." + name + ".timeToStart"), (SkyWarsType)(new SkyWarsTypeYML()).getObject(cfg.getString("arenas." + name + ".type")), utils.getLocationString(cfg, "arenas." + name + ".lobby"), utils.getLocationString(cfg, "arenas." + name + ".spec"), spawns, border);
            HashMap<Location, Integer> chests = new HashMap<>();
            for (int i = 1; i < 6; i++) {
                if (cfg.getString("arenas." + name + ".chests." + i) != null) {
                    List<String> listchest = cfg.getStringList("arenas." + name + ".chests." + i);
                    for (String s : listchest)
                        chests.put(utils.getLocationString(s), i);
                }
            }
            arena.setChests(chests);
            arena.setCorner1(corner1);
            arena.setCorner2(corner2);
            if (arena.getType() != null && arena.getMinPlayers() < arena.getMaxPlayers() && arena
                    .getSpawnLocations() != null && arena.getLobbyLocation() != null && arena
                    .getBorder() != null) {
                arena.setState(State.WAITING);
            } else {
                arena.setState(State.STOPPED);
            }
        }
        return null;
    }

    public String[] listObjects() {
        if (cfg.getString("arenas") == null)
            return new String[0];
        ArrayList<String> a = new ArrayList<>(cfg.getConfigurationSection("arenas").getKeys(false));
        return a.toArray(new String[a.size()]);
    }

    public static void save(SkyWarsArena arena) {
        cfg.set("arenas." + arena.getName() + ".arena", "SKYWARS");
        cfg.set("arenas." + arena.getName() + ".name", arena.getName());
        cfg.set("arenas." + arena.getName() + ".maxPlayers", arena.getMaxPlayers());
        cfg.set("arenas." + arena.getName() + ".minPlayers", arena.getMinPlayers());
        cfg.set("arenas." + arena.getName() + ".timeToStart", arena.getTimeToStart());
        if (arena.getType() != null)
            cfg.set("arenas." + arena.getName() + ".type", arena.getType().getName());
        cfg.set("arenas." + arena.getName() + ".lobby", utils.locationString(arena.getLobbyLocation()));
        cfg.set("arenas." + arena.getName() + ".spec", utils.locationString(arena.getSpecLocation()));
        cfg.set("arenas." + arena.getName() + ".corner1", utils.locationString(arena.getCorner1()));
        cfg.set("arenas." + arena.getName() + ".corner2", utils.locationString(arena.getCorner2()));
        if (arena.getSpawnLocations() != null)
            for (int i = 0; i < (arena.getSpawnLocations()).length; i++) {
                if (arena.getSpawnLocations()[i] != null)
                    cfg.set("arenas." + arena.getName() + ".spawn." + i, utils.locationString(arena.getSpawnLocations()[i]));
            }
        if (arena.getChests() != null)
            for (int i = 1; i < 6; i++) {
                ArrayList<String> chest = new ArrayList<>();
                if (arena.getChests().containsValue(i)) {
                    for (Location loc : arena.getChests().keySet()) {
                        if (arena.getChests().get(loc) == i)
                            chest.add(utils.locationString(loc));
                    }
                    cfg.set("arenas." + arena.getName() + ".chests." + i, chest);
                }
            }
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(SkyWarsTeamArena arena) {
        cfg.set("arenas." + arena.getName() + ".arena", "SKYWARS");
        cfg.set("arenas." + arena.getName() + ".name", arena.getName());
        cfg.set("arenas." + arena.getName() + ".maxPlayers", arena.getMaxPlayers());
        cfg.set("arenas." + arena.getName() + ".minPlayers", arena.getMinPlayers());
        cfg.set("arenas." + arena.getName() + ".timeToStart", arena.getTimeToStart());
        if (arena.getType() != null)
            cfg.set("arenas." + arena.getName() + ".type", arena.getType().getName());
        cfg.set("arenas." + arena.getName() + ".lobby", utils.locationString(arena.getLobbyLocation()));
        cfg.set("arenas." + arena.getName() + ".spec", utils.locationString(arena.getSpecLocation()));
        cfg.set("arenas." + arena.getName() + ".corner1", utils.locationString(arena.getCorner1()));
        cfg.set("arenas." + arena.getName() + ".corner2", utils.locationString(arena.getCorner2()));
        if (arena.getSpawnLocations() != null)
            for (int i = 0; i < (arena.getSpawnLocations()).length; i++) {
                if (arena.getSpawnLocations()[i] != null)
                    cfg.set("arenas." + arena.getName() + ".spawn." + i, utils.locationString(arena.getSpawnLocations()[i]));
            }
        if (arena.getChests() != null)
            for (int i = 1; i < 6; i++) {
                ArrayList<String> chest = new ArrayList<>();
                if (arena.getChests().containsValue(i)) {
                    for (Location loc : arena.getChests().keySet()) {
                        if (arena.getChests().get(loc) == i)
                            chest.add(utils.locationString(loc));
                    }
                    cfg.set("arenas." + arena.getName() + ".chests." + i, chest);
                }
            }
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void remove(String name) {
        cfg.set("arenas." + name, null);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
