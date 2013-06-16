/**
 * 
 */
package com.github.propra13.gruppeA3.Entities;

import java.util.LinkedList;

import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;

/**
 * @author Majida Dere
 * Diese Klasse erzeugt einen Non-Playable Character
 * Dieser NPC kan 2 Typen annehmen:
 * 					einen Erzähler, der die Geschichte erzählt
 * 				und einen Verkäufer, bei dem man Items kaufen kann.
 *
 **/
public class NPC extends Moveable {

	// Attribute
	private int type=1;
	private String desc=null;
	private String name=null;
	private String text=null;
	private LinkedList<Item> items=null;
	
	//Konstruktor
	
	public NPC (Room room_bind, int type, String desc, String name, int x, int y){
		super(room_bind);
		this.desc = desc;
		this.name = name;
		this.type = type;
		this.pos = new Position(x,y);
		this.hitbox = new Hitbox();
		setPosition(x+(hitbox.width/2),y+(hitbox.height/2));
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

	/* (non-Javadoc)
	 * @see com.github.propra13.gruppeA3.Entities.Entities#getHitbox()
	 */
	@Override
	public Hitbox getHitbox() {
		// TODO Auto-generated method stub
		return this.hitbox;
	}

	/* (non-Javadoc)
	 * @see com.github.propra13.gruppeA3.Entities.Entities#tick()
	 */
	@Override
	void tick() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.github.propra13.gruppeA3.Entities.Entities#collision(com.github.propra13.gruppeA3.Entities.Entities)
	 */
	@Override
	void collision(Entities entity) {
		// TODO Auto-generated method stub

	}
	
	public void setItems(LinkedList<Item> items){
		this.items = items;
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

}
