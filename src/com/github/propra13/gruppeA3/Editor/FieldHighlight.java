package com.github.propra13.gruppeA3.Editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.github.propra13.gruppeA3.Map.Position;

/**
 * JComponent, das ein Highlight-Quadrat für Felder definiert.
 * @author Christian
 *
 */
public class FieldHighlight extends JComponent {
	private static final long serialVersionUID = 1L;
	
	//Paint-Position
	private Position pos;
	private Color color;
	
	/**
	 * Feld-Eigenschaft, anhand derer hervorgehoben wird (=> Farbe)
	 * FIELD: Normales Feld, das hervorgehoben wird
	 * LINK: Link wird hervorgehoben
	 * TRIGGER: Trigger wird hervorgehoben */
	public enum Type {FIELD, LINK, TRIGGER, SPAWN, END}
	
	/**
	 * 
	 * @param pos Position des FieldHighlights
	 * @param type Feld-Eigenschaft, anhand derer hervorgehoben wird
	 */
	protected FieldHighlight(Position pos, Type type) {
		this.pos = pos;
		setPreferredSize(new Dimension(33, 33));
		this.setBounds(pos.x,pos.y,33,33);
		
		switch(type) {
		case FIELD:
			color = Color.decode("#019000");
			break;
		case LINK:
			color = Color.decode("#8e0202");
			break;
		case TRIGGER:
			color = Color.decode("#07016f");
			break;
		case SPAWN:
			color = Color.decode("#ffe200");
			break;
		case END:
			color = Color.WHITE;
			break;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(color);
		g.drawRect(pos.x,pos.y, 32, 32);
	}
	

}
