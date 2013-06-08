package com.github.propra13.gruppeA3.Entities;

public class Hitbox {
	public int width;
	public int height;
	
	public Hitbox(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	// Standardhitbox
	public Hitbox() {
		this.width = this.height = 32;
	}
}
