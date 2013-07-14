package com.github.propra13.gruppeA3.Entities;

import java.util.LinkedList;
import com.github.propra13.gruppeA3.Map.Position;

/**
 * @author Majida Dere
 * Diese Klasse erzeugt einen Non-Playable Character
 * Dieser NPC kan 2 Typen annehmen:
 * 					einen Erzähler, der die Geschichte erzählt
 * 				und einen Verkäufer, bei dem man Items kaufen kann.
 *
 **/
public class NPC extends Entities {

	/**
	 * Attribute:
	 * 			type: Typ des NPCs (Erzähler, Shop, ...)
	 * 		    desc: Beschreibung des NPCs (Beispiel: "the babarian")
	 * 			name: Name des NPCs (Nancy, Harold, ...)
	 * 			text: Das, was die NPCs aufsagen.
	 * 			items: Falls der NPCs einen Shop darstellt, besitzt es eine Liste mit Items
	 */		
	private int type=1;
	private String desc=null;
	private String name=null;
	private String text=null;
	private LinkedList<Item> items=null;
	private Position pos=null;
	private Hitbox hitbox=null;
	
	final public static int INFO_NPC = 1;
	final public static int SHOP_NPC = 2;
	
	/**
	 * Der Konstruktor erzeugt einen NPC mit folgenden Paramtern
	 * @param room_bind Der Raum, in dem sich der NPC befindet
	 * @param type Typ des NPCs (1: Info, 2: Shop)
	 * @param desc Beschreibung des NPCs
	 * @param name Name des NPCs
	 * @param x Position X Achse
	 * @param y Position Y Achse
	 */	
	public NPC (int type, String desc, String name, int x, int y){
		this.setDesc(desc);
		this.setName(name);
		this.type = type;
		this.hitbox = Hitbox.standard;
		pos = new Position(x, y);
		//Bei Type 1 NPCs bleibt er leer und wird nicht benutzt.
		this.items = new LinkedList<Item>();
	}

	/* (non-Javadoc)
	 * @see com.github.propra13.gruppeA3.Entities.Entities#getPosition()
	 */
	@Override
	public Position getPosition() {
		// TODO Auto-generated method stub
		return this.pos;
	}

	public void setText(String text){
		this.text = text;
	}
	
	public LinkedList<Item> getItems(){
		return this.items;
	}
	
	public String getText(){
		return this.text;
	}
	
	public int getType(){
		return this.type;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public Hitbox getHitbox() {
		// TODO Auto-generated method stub
		return this.hitbox;
	}

	@Override
	void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void collision(Entities entity) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Map-Editor-Methoden
	 */
	
	/**
	 * Ändert die NPC-Attribute.
	 * @param npc NPC, dessen Attribute übernommen werden sollen
	 */
	public void edit(NPC npc) {
		type = npc.getType();
		desc = npc.getDesc();
		name = npc.getName(); 
		pos = npc.getPosition();
		items = npc.getItems();
		text = npc.getText();
	}
	
	/**
	 * Gibt eine Kopie dieses NPCs zurück.
	 */
	public NPC clone() {
		return new NPC(type,
				desc,
				name,
				pos.getDrawPosition(hitbox).x,
				pos.getDrawPosition(hitbox).y);	
	}
	
	/**
	 * Ändert den Typ des NPCs.
	 * @param type Neuer NPC-Typ
	 */
	public void setType(int type) {
		this.type = type;
	}
}