package com.github.propra13.gruppeA3;

/* @author CK
 * Trigger, der unwiderruflich einen Checkpoint freischaltet
 */
public class Checkpoint extends Trigger {
	private Link toActivate;
	
	public Checkpoint(Field field, Link link) {
		super(field);
		toActivate = link;
	}

	@Override
	public void trigger() {
		//System.out.println("Ich würde jetzt gern "+linkToActivate.ID+" aktivieren.");
		//System.out.println("Der geht von "+linkToActivate.targetRooms[0].ID+" nach "+linkToActivate.targetRooms[1].ID+".");
		toActivate.activate();

	}

	@Override
	public boolean status() {
		return toActivate.isActivated();
	}

}
