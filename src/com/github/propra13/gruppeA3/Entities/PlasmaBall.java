package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;

public class PlasmaBall extends Projectile {
	
	private final static int damage = 5;
	public final static int manaCost = 10;
	public PlasmaBall(Room room_bind, Position pos, Moveable.Direction direct1, Moveable.Direction direct2) {
		super(room_bind, direct1, direct2);
		
		hitbox = new Hitbox(6, 6);
		this.pos = new Position(0,0);
		room_bind.entities.add(this);
		setSpeed(4.0);
		
		// Position bestimmen; soll am Hitboxrand losfliegen
		switch(direct) {
		case DOWN:
			this.pos.setPosition(pos.x, pos.y + hitbox.height / 2);
			break;
		case LEFT:
			this.pos.setPosition(pos.x - hitbox.width / 2, pos.y);
			break;
		case RIGHT:
			this.pos.setPosition(pos.x + hitbox.width / 2, pos.y);
			break;
		case UP:
			this.pos.setPosition(pos.x, pos.y - hitbox.height / 2);
			break;
		case NONE:
		default:
			this.pos.setPosition(pos.x, pos.y);
			break;
		}
		
		System.out.println("Ich bin ein Plasmaball! Ich sitze auf "+this.pos.x+":"+this.pos.y+" und fliege nach "+direct);
	}
	
	@Override
	public int getDamage() {
		return damage;
	}
	
}
