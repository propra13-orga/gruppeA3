package com.github.propra13.gruppeA3.Editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Menu.MenuStart;

public class RoomTab extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	
	Room room;

	public RoomTab(Room room) {
		setBackground(Color.GREEN);
		System.out.println("Ich bin der RoomTab des Raums "+room.ID);
		this.room = room;
		addMouseListener(this);
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		MenuStart.paintRoom(g2d, room, this);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Klick: "+e.getX()+":"+e.getY());
		JPopupMenu pop = new JPopupMenu();
		pop.setLocation(e.getLocationOnScreen());
		JMenuItem item = new JMenuItem("Eintrag 0");
		pop.add(item);
		pop.setVisible(true);
	}

	// Dummies (interface-Methoden wo nichts passiert)
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
