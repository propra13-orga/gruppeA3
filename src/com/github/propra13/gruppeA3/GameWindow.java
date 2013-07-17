package com.github.propra13.gruppeA3;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Klasse für GUI: Spielgrafik
 * @autor Jenny Lenz
 * 
 * Bilder werden hier eingelesen und eingebunden 
 */


public class GameWindow {
	//monster
	//boss monster
	public static BufferedImage[] bossImgs_left = new BufferedImage[4];
	public static BufferedImage[] bossImgs_right = new BufferedImage[4];
	public static BufferedImage[] bossImgs_up = new BufferedImage[4];
	public static BufferedImage[] bossImgs_down = new BufferedImage[4];
	
	//normale Monster
	public static BufferedImage monsterimg_1left;
	public static BufferedImage monsterimg_1right;
	public static BufferedImage monsterimg_1up;
	public static BufferedImage monsterimg_1down;
	
	public static BufferedImage monsterimg_2left;
	public static BufferedImage monsterimg_2right;
	public static BufferedImage monsterimg_2up;
	public static BufferedImage monsterimg_2down;
	
	public static BufferedImage monsterimg_3left;
	public static BufferedImage monsterimg_3right;
	public static BufferedImage monsterimg_3up;
	public static BufferedImage monsterimg_3down;
	
	public static BufferedImage monsterimg_4left;
	public static BufferedImage monsterimg_4right;
	public static BufferedImage monsterimg_4up;
	public static BufferedImage monsterimg_4down;
	
	//Texturen
    public static BufferedImage backgroundimg_1;
    public static BufferedImage backgroundimg_2;
    public static BufferedImage riverimg_r;
    public static BufferedImage riverimg_d;
    public static BufferedImage riverimg_l;
    public static BufferedImage riverimg_u;
    public static BufferedImage backgroundimg_4;
    public static BufferedImage exitimg;
    
    //player
    public static BufferedImage playerimg_left1;
    public static BufferedImage playerimg_left2;
    public static BufferedImage playerimg_right1;
    public static BufferedImage playerimg_right2;
    public static BufferedImage playerimg_up1;
    public static BufferedImage playerimg_up2;
    public static BufferedImage playerimg_down1;
    public static BufferedImage playerimg_down2;
    public static BufferedImage kampfimg_left1;
    public static BufferedImage kampfimg_left2;
    public static BufferedImage kampfimg_right1;
    public static BufferedImage kampfimg_right2;
    public static BufferedImage kampfimg_up1;
    public static BufferedImage kampfimg_up2;
    public static BufferedImage kampfimg_down1;
    public static BufferedImage kampfimg_down2;
    public static BufferedImage kampfimg_shieldL;
    public static BufferedImage kampfimg_shieldR;
    public static BufferedImage kampfimg_shieldU;
    public static BufferedImage kampfimg_shieldD;
    //NPC
    public static BufferedImage coin;
    public static BufferedImage npc1;
    public static BufferedImage npc2;
    public static BufferedImage npc3;
    public static BufferedImage npc4;
    
        //infoleiste
    public static BufferedImage plasma;
    public static BufferedImage mana;
    public static BufferedImage infosword;
    public static BufferedImage infoshield;
    public static BufferedImage heart;
    
    // Item images
    public static BufferedImage lifePosion;
    public static BufferedImage deadlyPosion;
    public static BufferedImage manaPosion;
    public static BufferedImage[] swords = new BufferedImage[4];
    public static BufferedImage[] shields = new BufferedImage[4];
    public static BufferedImage drachenei;
    
    //Hintergrund bild
    public static BufferedImage background;

    protected Toolkit tool;
    protected String GamePath;
    
    public GameWindow(){
		GamePath = System.getProperty("user.dir");
	    tool = Toolkit.getDefaultToolkit();
	    
	    //Boss
	    bossImgs_down[0] = this.getBufferedImage(this.GamePath + "/data/images/Monster/Bphy.png");
	    bossImgs_left[0] = rotate(bossImgs_down[0]);
	    bossImgs_up[0] = rotate(bossImgs_left[0]);
	    bossImgs_right[0] = rotate(bossImgs_up[0]);
	    
	    bossImgs_down[1] = this.getBufferedImage(this.GamePath + "/data/images/Monster/Bfeuer.png");
	    bossImgs_left[1] = rotate(bossImgs_down[1]);
	    bossImgs_up[1] = rotate(bossImgs_left[1]);
	    bossImgs_right[1] = rotate(bossImgs_up[1]);
	    
	    bossImgs_down[2] = this.getBufferedImage(this.GamePath + "/data/images/Monster/Bwasser.png");
	    bossImgs_left[2] = rotate(bossImgs_down[2]);
	    bossImgs_up[2] = rotate(bossImgs_left[2]);
	    bossImgs_right[2] = rotate(bossImgs_up[2]);
	    
	    bossImgs_down[3] = this.getBufferedImage(this.GamePath + "/data/images/Monster/Beis.png");
	    bossImgs_left[3] = rotate(bossImgs_down[3]);
	    bossImgs_up[3] = rotate(bossImgs_left[3]);
	    bossImgs_right[3] = rotate(bossImgs_up[3]);
	    
	    
	    //Monster
	    monsterimg_1left = this.getBufferedImage(this.GamePath + "/data/images/Monster/M1left.png");
	    monsterimg_1right = this.getBufferedImage(this.GamePath + "/data/images/Monster/M1right.png");
	    monsterimg_1up = this.getBufferedImage(this.GamePath + "/data/images/Monster/M1up.png");
	    monsterimg_1down = this.getBufferedImage(this.GamePath + "/data/images/Monster/M1down.png");
	    monsterimg_2left = this.getBufferedImage(this.GamePath + "/data/images/Monster/M2left.png");
	    monsterimg_2right = this.getBufferedImage(this.GamePath + "/data/images/Monster/M2right.png");
	    monsterimg_2up = this.getBufferedImage(this.GamePath + "/data/images/Monster/M2up.png");
	    monsterimg_2down = this.getBufferedImage(this.GamePath + "/data/images/Monster/M2down.png");
	    monsterimg_3left = this.getBufferedImage(this.GamePath + "/data/images/Monster/M3left.png");
	    monsterimg_3right = this.getBufferedImage(this.GamePath + "/data/images/Monster/M3right.png");
	    monsterimg_3up = this.getBufferedImage(this.GamePath + "/data/images/Monster/M3up.png");
	    monsterimg_3down = this.getBufferedImage(this.GamePath + "/data/images/Monster/M3down.png");
	    monsterimg_4left = this.getBufferedImage(this.GamePath + "/data/images/Monster/M4left.png");
	    monsterimg_4right = this.getBufferedImage(this.GamePath + "/data/images/Monster/M4right.png");
	    monsterimg_4up = this.getBufferedImage(this.GamePath + "/data/images/Monster/M4up.png");
	    monsterimg_4down = this.getBufferedImage(this.GamePath + "/data/images/Monster/M4down.png");
	    
	    
	    //Hintergrundis
	    backgroundimg_4 = this.getBufferedImage(this.GamePath + "/data/images/exit.png");
	    backgroundimg_1 = this.getBufferedImage(this.GamePath + "/data/images/UG1.png");
	    backgroundimg_2 = this.getBufferedImage(this.GamePath + "/data/images/UG2.png");
	    riverimg_r = getBufferedImage(this.GamePath + "/data/images/Fluss.png");
	    riverimg_d = rotate(riverimg_r);
	    riverimg_l = rotate(riverimg_d);
	    riverimg_u = rotate(riverimg_l);
	    
	    //Player laeuft
	    playerimg_left1 = this.getBufferedImage(this.GamePath + "/data/images/Spieler laeuft/Pleft1.png");
	    playerimg_left2 = this.getBufferedImage(this.GamePath + "/data/images/Spieler laeuft/Pleft2.png");
	    playerimg_right1 = this.getBufferedImage(this.GamePath + "/data/images/Spieler laeuft/Pright1.png");
	    playerimg_right2 = this.getBufferedImage(this.GamePath + "/data/images/Spieler laeuft/Pright2.png");
	    playerimg_up1 = this.getBufferedImage(this.GamePath + "/data/images/Spieler laeuft/Pup1.png");
	    playerimg_up2 = this.getBufferedImage(this.GamePath + "/data/images/Spieler laeuft/Pup2.png");
	    playerimg_down1 = this.getBufferedImage(this.GamePath + "/data/images/Spieler laeuft/Pdown1.png");
	    playerimg_down1 = this.getBufferedImage(this.GamePath + "/data/images/Spieler laeuft/Pdown2.png");
	    
	    //Player kaempft
	    kampfimg_left1 = this.getBufferedImage(this.GamePath +"/data/images/Spieler kampf/Kleft1.png");
	    kampfimg_left2 = this.getBufferedImage(this.GamePath +"/data/images/Spieler kampf/Kleft2.png");
	    kampfimg_right1 = this.getBufferedImage(this.GamePath +"/data/images/Spieler kampf/Kright1.png");
	    kampfimg_right2 = this.getBufferedImage(this.GamePath +"/data/images/Spieler kampf/Kright2.png");
	    kampfimg_up1 = this.getBufferedImage(this.GamePath +"/data/images/Spieler kampf/Kup1.png");
	    kampfimg_up2 = this.getBufferedImage(this.GamePath +"/data/images/Spieler kampf/Kup2.png");
	    kampfimg_down1 = this.getBufferedImage(this.GamePath +"/data/images/Spieler kampf/Kdown1.png");
	    kampfimg_down2 = this.getBufferedImage(this.GamePath +"/data/images/Spieler kampf/Kdown2.png");
	    kampfimg_shieldL = this.getBufferedImage(this.GamePath + "/data/images/Spieler kampf/Sleft1.png");
	    kampfimg_shieldR = this.getBufferedImage(this.GamePath + "/data/images/Spieler kampf/Sright1.png");
	    kampfimg_shieldU = this.getBufferedImage(this.GamePath + "/data/images/Spieler kampf/Sup1.png");
	    kampfimg_shieldD = this.getBufferedImage(this.GamePath + "/data/images/Spieler kampf/Sdown1.png");
	    
	    //Player zaubert 
	    plasma = this.getBufferedImage(this.GamePath + "/data/images/Plasma.png");
	    
	    //NPC
	    npc1 = this.getBufferedImage(this.GamePath + "/data/images/npc/npc1.png");
	    npc2 = this.getBufferedImage(this.GamePath + "/data/images/npc/npc2.png");
	    npc3 = this.getBufferedImage(this.GamePath + "/data/images/npc/npc3.png");
	    npc4 = this.getBufferedImage(this.GamePath + "/data/images/npc/npc4.png");
	    //Room-Items
	    lifePosion = this.getBufferedImage(this.GamePath + "/data/images/Items room/T1.png");
	    deadlyPosion = this.getBufferedImage(this.GamePath + "/data/images/Items room/T2.png");
	    manaPosion = this.getBufferedImage(this.GamePath + "/data/images/Items room/T3.png");
	    drachenei = this.getBufferedImage(this.GamePath + "/data/images/Items room/ei.png");
	    swords[0] = this.getBufferedImage(this.GamePath + "/data/images/Items room/W1.png");
	    swords[1] = this.getBufferedImage(this.GamePath + "/data/images/Items room/Wfeuer.png");
	    swords[2] = this.getBufferedImage(this.GamePath + "/data/images/Items room/Wwasser.png");
	    swords[3] = this.getBufferedImage(this.GamePath + "/data/images/Items room/Weis.png");
	    shields[0] = this.getBufferedImage(this.GamePath + "/data/images/Items room/S1.png");
	    shields[1]= this.getBufferedImage(this.GamePath + "/data/images/Items room/Sfeuer.png");
	    shields[2] = this.getBufferedImage(this.GamePath + "/data/images/Items room/Swasser.png");
	    shields[3] = this.getBufferedImage(this.GamePath + "/data/images/Items room/Seis.png");
	    //Infoleiste 
	    heart = this.getBufferedImage(this.GamePath + "/data/images/Infoleiste/herz.png");
	    coin = this.getBufferedImage(this.GamePath + "/data/images/Infoleiste/coin.png");
	    mana = this.getBufferedImage(this.GamePath + "/data/images/Infoleiste/mana.png");
	    infosword = this.getBufferedImage(this.GamePath + "/data/images/Infoleiste/schwert.png");
	    infoshield = this.getBufferedImage(this.GamePath + "/data/images/Infoleiste/schild.png");
	    
	    //hintergrundbild
	    background = this.getBufferedImage(this.GamePath + "/data/images/hintergrund.jpg");
	    
    }
	
    protected BufferedImage getBufferedImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (NullPointerException | IOException e) {
			e.printStackTrace();
		}

        return img;
    }
    
    /**
     * Produziert eine um 90° gedrehte Kopie eines BufferedImages.
     * @param img zu drehendes Bild
     * @return gedrehtes Bild
     */
    public static BufferedImage rotate(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newBufferedImage = new BufferedImage(height, width, img.getType());
  
        for(int i=0 ; i < width ; i++)
            for(int j=0 ; j < height ; j++)
                newBufferedImage.setRGB(height-1-j, i, img.getRGB(i,j));
        
        return newBufferedImage;
    }
    
    /**
     * Färbt ein Bild rot.
     * @param img Zu färbendes Bild
     */
    public static BufferedImage turnRed(BufferedImage img) {
    	BufferedImage newImg = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
    	
    	//Iteriert über Bild
        for(int i=0 ; i < img.getWidth(); i++)
            for(int j=0 ; j < img.getHeight(); j++) {
                int oldRGB = img.getRGB(i,j);
                int alpha = oldRGB>>>24;
            	int red = (oldRGB -(alpha<<24)) >>> 16;
            	int green = (oldRGB<<16)>>>24;
            	int blue = (oldRGB<<24)>>>24;
            	
            	int smallest = red;
            	if(green < red)
            		smallest = green;
            	if(blue < red)
            		smallest = blue;
            	
            	int newBlue = smallest;
            	int newGreen = smallest;
            	int newRed = ((red/2 + green/3 + blue/3)<<24)>>>24;
            	
            	//Falls zu dunkel, künstlich aufhellen
            	if(newRed < 10)
            		newRed = newRed + (10 - newRed) * 5;
            		
            	int newRGB = (alpha<<24) + (red<<16) + (newGreen<<8) + (newBlue);
            	newRGB = (alpha<<24) + (newRed<<16);
            	newImg.setRGB(i, j, newRGB);
            }
        
        return newImg;
    }
}
