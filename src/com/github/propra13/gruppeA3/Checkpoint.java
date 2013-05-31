package com.github.propra13.gruppeA3;

/* @author CK
 * Trigger, der unwiderruflich einen Checkpoint freischaltet
 */
public class Checkpoint extends Trigger {
	public Link toActivate;
	
	public Checkpoint(Field field, Link link) {
		super(field);
		System.out.println("Ich bin ein Checkpointtrigger und wurde soeben erzeugt.");
		this.toActivate = link;

		System.out.println("Ich bin dazu da, Link"+link.ID+" von "+link.targetRooms[0].ID+":"+link.targetFields[0].pos.x+":"+link.targetFields[0].pos.y+" nach "+link.targetRooms[1]+":"+link.targetFields[1].pos.x+":"+link.targetFields[1].pos.y+" zu aktivieren.");
	}

	@Override
	public void trigger() {
		System.out.println("Ich würde jetzt gern "+toActivate.ID+" aktivieren.");
		//System.out.println("Der geht von "+linkToActivate.targetRooms[0].ID+" nach "+linkToActivate.targetRooms[1].ID+".");
		toActivate.activate();

	}

	@Override
	public boolean status() {
		return toActivate.isActivated();
	}

}
