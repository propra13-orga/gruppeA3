package com.github.propra13.gruppeA3.Map;


/** 
 * @author CK
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
	
	/*
	 * Map-Editor-Methoden
	 */
	
	public Link getToActivate() {
		return toActivate;
	}
	
	public void setToActivate(Link link) {
		if(link != null)
			this.toActivate = link;
		else
			try {
				throw new NullPointerException("Feld hat keinen Link.");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public void edit(Trigger trigger) {
		Checkpoint checkpoint = (Checkpoint)trigger;
		toActivate = checkpoint.getToActivate();
		field = checkpoint.getField();
		
	}
	
	@Override
	public Trigger clone() {
		return new Checkpoint(field, toActivate);
	}

}
