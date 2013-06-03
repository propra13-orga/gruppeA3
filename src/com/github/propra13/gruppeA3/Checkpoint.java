package com.github.propra13.gruppeA3;

/* @author CK
 * Trigger, der unwiderruflich einen Checkpoint freischaltet
 */
public class Checkpoint extends Trigger {
	private Link toActivate;
	
	public Checkpoint(Field field, Link link) {
		super(field);
		this.toActivate = link;
	}

	
	@Override
	public void trigger() {
		toActivate.activate();
	}

	
	@Override
	public boolean status() {
		return toActivate.isActivated();
	}

}
