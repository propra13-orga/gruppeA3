package com.github.propra13.gruppeA3;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Klasse für GUI: Spielgrafik
 * @autor Jenny Lenz
 */


public class GameWindow {
	//monster
	public static Image bossimg_1left;
	public static Image bossimg_1right;
	public static Image bossimg_1up;
	public static Image bossimg_1down;
	
	public static Image monsterimg_1left;
	public static Image monsterimg_1right;
	public static Image monsterimg_1up;
	public static Image monsterimg_1down;
	
	public static Image monsterimg_2left;
	public static Image monsterimg_2right;
	public static Image monsterimg_2up;
	public static Image monsterimg_2down;
	
	public static Image monsterimg_3left;
	public static Image monsterimg_3right;
	public static Image monsterimg_3up;
	public static Image monsterimg_3down;
	
	public static Image monsterimg_4left;
	public static Image monsterimg_4right;
	public static Image monsterimg_4up;
	public static Image monsterimg_4down;
	
	//Texturen
    public static Image backgroundimg_1;
    public static Image backgroundimg_2;
    public static Image backgroundimg_3;
    public static Image backgroundimg_4;
    public static Image exitimg;
    
    //player
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
    //NPC
    public static Image coin;
    public static Image npc1;
    public static Image npc2;
    public static Image npc3;
    public static Image npc4;
    
        //infoleiste
    public static Image plasma;
    public static Image mana;
    public static Image infosword;
    public static Image infoshield;
    public static Image heart;
    
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
    
    //Boss
    bossimg_1left = this.getImage(this.GamePath + "/data/images/Monster/B1left.png");
    bossimg_1right = this.getImage(this.GamePath + "/data/images/Monster/B1right.png");
    bossimg_1up = this.getImage(this.GamePath + "/data/images/Monster/B1up.png");
    bossimg_1down = this.getImage(this.GamePath + "/data/images/Monster/B1down.png");
    
    //Monster
    monsterimg_1left = this.getImage(this.GamePath + "/data/images/Monster/M1left.png");
    monsterimg_1right = this.getImage(this.GamePath + "/data/images/Monster/M1right.png");
    monsterimg_1up = this.getImage(this.GamePath + "/data/images/Monster/M1up.png");
    monsterimg_1down = this.getImage(this.GamePath + "/data/images/Monster/M1down.png");
    monsterimg_2left = this.getImage(this.GamePath + "/data/images/Monster/M2left.png");
    monsterimg_2right = this.getImage(this.GamePath + "/data/images/Monster/M2right.png");
    monsterimg_2up = this.getImage(this.GamePath + "/data/images/Monster/M2up.png");
    monsterimg_2down = this.getImage(this.GamePath + "/data/images/Monster/M2down.png");
    monsterimg_3left = this.getImage(this.GamePath + "/data/images/Monster/M3left.png");
    monsterimg_3right = this.getImage(this.GamePath + "/data/images/Monster/M3right.png");
    monsterimg_3up = this.getImage(this.GamePath + "/data/images/Monster/M3up.png");
    monsterimg_3down = this.getImage(this.GamePath + "/data/images/Monster/M3down.png");
    monsterimg_4left = this.getImage(this.GamePath + "/data/images/Monster/M4left.png");
    monsterimg_4right = this.getImage(this.GamePath + "/data/images/Monster/M4right.png");
    monsterimg_4up = this.getImage(this.GamePath + "/data/images/Monster/M4up.png");
    monsterimg_4down = this.getImage(this.GamePath + "/data/images/Monster/M4down.png");
    
    
    //Hintergrundis
    backgroundimg_4 = this.getImage(this.GamePath + "/data/images/Exit.png");
    backgroundimg_1 = this.getImage(this.GamePath + "/data/images/UG1.png");
    backgroundimg_2 = this.getImage(this.GamePath + "/data/images/UG2.png");
    backgroundimg_3 = this.getImage(this.GamePath + "/data/images/Fluss.png");
    //Player laeuft
    playerimg_left1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pleft1.png");
    playerimg_left2 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pleft2.png");
    playerimg_right1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pright1.png");
    playerimg_right2 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pright2.png");
    playerimg_up1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pup1.png");
    playerimg_up2 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pup2.png");
    playerimg_down1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pdown1.png");
    playerimg_down1 = this.getImage(this.GamePath + "/data/images/Spieler laeuft/Pdown2.png");
    
    //Player kaempft
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
    npc1 = this.getImage(this.GamePath + "/data/images/npc/npc1.png");
    npc2 = this.getImage(this.GamePath + "/data/images/npc/npc2.png");
    npc3 = this.getImage(this.GamePath + "/data/images/npc/npc3.png");
    npc4 = this.getImage(this.GamePath + "/data/images/npc/npc4.png");
    //Room-Items
    lifePosion = this.getImage(this.GamePath + "/data/images/items room/T1.png");
    deadlyPosion = this.getImage(this.GamePath + "/data/images/items room/T2.png");
    manaPosion = this.getImage(this.GamePath + "/data/images/items room/T3.png");
    sword = this.getImage(this.GamePath + "/data/images/items room/W1.png");
    shield = this.getImage(this.GamePath + "/data/images/items room/S1.png");
    //Infoleiste 
    heart = this.getImage(this.GamePath + "/data/images/Infoleiste/herz.png");
    coin = this.getImage(this.GamePath + "/data/images/Infoleiste/coin.png");
    mana = this.getImage(this.GamePath + "/data/images/Infoleiste/mana.png");
    infosword = this.getImage(this.GamePath + "/data/images/Infoleiste/schwert.png");
    infoshield = this.getImage(this.GamePath + "/data/images/Infoleiste/schild.png");
    
    
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
