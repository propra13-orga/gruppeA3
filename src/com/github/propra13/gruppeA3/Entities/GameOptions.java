package com.github.propra13.gruppeA3.Entities;

import java.util.Map;

public class GameOptions {

    protected String GamePath;
    protected String ImagePath;
    protected Map<Integer, String> WallImagePaths;

    protected String StartMap;
    protected Map<String, Player> player;

    public GameOptions() {
        this.GamePath = System.getProperty("user.dir");
        this.ImagePath = GamePath + "/" + "data/images";

        this.readWallImagePaths();
    }

    protected void readWallImagePaths() {
        for(int i=0; i<5; i++)
            WallImagePaths.put(i, ImagePath+"/wall_"+i+"_32.png");

    }

    public Map<String, Player> getAllPlayers() {
        return this.player;
    }

    public Player getPlayer(String name) {
        return player.get(name);
    }

    public void addPlayer(String name, Player player) {
        this.player.put(name, player);
    }

    public void removePlayer(String name) {
        this.player.remove(name);
    }

    public String getStartMap() {
        return StartMap;
    }

    public void setStartMap(String startMap) {
        StartMap = startMap;
    }

    public String getGamePath() {
        return GamePath;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getWallImagePath(Integer id) {
        return WallImagePaths.get(id);
    }
}
