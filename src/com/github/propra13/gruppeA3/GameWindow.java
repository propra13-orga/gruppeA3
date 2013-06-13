package com.github.propra13.gruppeA3;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Klasse für GUI: Spielgrafik
 * @autor Jenny Lenz
 */


public class GameWindow {
	
    public static Image monsterimg;
    public static Image backgroundimg;
    public static Image wallimg_1_32;
    public static Image wallimg_2_32;
    public static Image wallimg_3_32;
    public static Image playerimg;
    public static Image exitimg;
    public static Image heart;
    public static Image coin;
    
    // Item images
    public static Image lifePosion;
    public static Image deadlyPosion;
    public static Image manaPosion;
    public static Image sword;
    public static Image shield;

    protected Toolkit tool;
    protected String GamePath;
    
	public GameWindow(){
	GamePath = System.getProperty("user.dir");
    tool = Toolkit.getDefaultToolkit();
    monsterimg = this.getImage(this.GamePath + "/data/images/Test_Monster.png");
    backgroundimg = this.getImage(this.GamePath + "/data/images/Test_Wand.png");
    wallimg_1_32 = this.getImage(this.GamePath + "/data/images/wall_1_32.png");
    wallimg_2_32 = this.getImage(this.GamePath + "/data/images/wall_2_32.png");
    wallimg_3_32 = this.getImage(this.GamePath + "/data/images/wall_3_32.png");
    playerimg = this.getImage(this.GamePath + "/data/images/Pdown1.png");
    exitimg = this.getImage(this.GamePath + "/data/images/exit.png");
    sword = this.getImage(this.GamePath + "/data/images/sword.png");
    shield = this.getImage(this.GamePath + "/data/images/shield.png");
    heart = this.getImage(this.GamePath + "/data/images/herz.png");
    coin = this.getImage(this.GamePath + "/data/images/coin.png");
	}
	
    protected Image getImage(String path) {
        Image img = null;
        try {
            img = tool.getImage(path);
        } catch (NullPointerException npe) {
        	npe.printStackTrace();
        }

        return img;
    }
}