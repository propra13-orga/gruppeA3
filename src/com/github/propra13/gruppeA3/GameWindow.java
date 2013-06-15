package com.github.propra13.gruppeA3;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Klasse für GUI: Spielgrafik
 * @autor Jenny Lenz
 */


public class GameWindow {
	
	public static Image monsterimg;
    public static Image backgroundimg_1;
    public static Image backgroundimg_2;
    public static Image backgroundimg_3;
    public static Image backgroundimg_4;
    public static Image playerimg_left1;
    public static Image playerimg_left2;
    public static Image playerimg_right1;
    public static Image playerimg_right2;
    public static Image playerimg_up1;
    public static Image playerimg_up2;
    public static Image playerimg_down1;
    public static Image playerimg_down2;
    public static Image kampfimg_left1;
    public static Image kampfimg_left2;
    public static Image kampfimg_right1;
    public static Image kampfimg_right2;
    public static Image kampfimg_up1;
    public static Image kampfimg_up2;
    public static Image kampfimg_down1;
    public static Image kampfimg_down2;
    public static Image kampfimg_shieldL;
    public static Image kampfimg_shieldR;
    public static Image kampfimg_shieldU;
    public static Image kampfimg_shieldD;
    public static Image exitimg;
    public static Image heart;
    public static Image coin;
    
    public static Image plasma;
    
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
    //Monster
    monsterimg = this.getImage(this.GamePath + "/data/images/Monster/M1.png");
    //Hintergrund
    backgroundimg_4= this.getImage(this.GamePath + "/data/images/Exit.png");
    backgroundimg_1= this.getImage(this.GamePath + "/data/images/UG1.png");
    backgroundimg_2= this.getImage(this.GamePath + "/data/images/UG2.png");
    backgroundimg_3 = this.getImage(this.GamePath + "/data/images/Fluss.png");
    //Player läuft
    playerimg_left1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pleft1.png");
    playerimg_left2 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pleft2.png");
    playerimg_right1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pright1.png");
    playerimg_right2 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pright2.png");
    playerimg_up1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pup1.png");
    playerimg_up2 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pup2.png");
    playerimg_down1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pdown1.png");
    playerimg_down1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pdown2.png");
    
    //Player kämpft
    kampfimg_left1 = this.getImage(this.GamePath +"/data/images/Spieler kampf/Kleft1");
    kampfimg_left2 = this.getImage(this.GamePath +"/data/images/Spieler kampf/Kleft2");
    kampfimg_right1 = this.getImage(this.GamePath +"/data/images/Spieler kampf/Kright1");
    kampfimg_right2 = this.getImage(this.GamePath +"/data/images/Spieler kampf/Kright2");
    kampfimg_up1 = this.getImage(this.GamePath +"/data/images/Spieler kampf/Kup1");
    kampfimg_up2 = this.getImage(this.GamePath +"/data/images/Spieler kampf/Kup2");
    kampfimg_down1 = this.getImage(this.GamePath +"/data/images/Spieler kampf/Kdown1");
    kampfimg_down2 = this.getImage(this.GamePath +"/data/images/Spieler kampf/Kdown2");
    kampfimg_shieldL = this.getImage(this.GamePath + "/data/images/Spieler kampf/Sleft1");
    kampfimg_shieldR = this.getImage(this.GamePath + "/data/images/Spieler kampf/Sright1");
    kampfimg_shieldU = this.getImage(this.GamePath + "/data/images/Spieler kampf/Sup1");
    kampfimg_shieldD = this.getImage(this.GamePath + "/data/images/Spieler kampf/Sdown1");
    
    //Player zaubert 
    plasma = this.getImage(this.GamePath + "/data/images/Plasma.png");
    
    //NPC
    //Room-Items
    sword = this.getImage(this.GamePath + "/data/images/items room/W1.png");
    shield = this.getImage(this.GamePath + "/data/images/items room/S1.png");
    //Infoleiste 
    heart = this.getImage(this.GamePath + "/data/images/infoleiste/herz.png");
    coin = this.getImage(this.GamePath + "/data/images/infoleiste/coin.png");
    
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