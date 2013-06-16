package com.github.propra13.gruppeA3.Editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Menu.MenuStart;

public class RoomTab extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Room room;

	public RoomTab(Room room) {
		setBackground(Color.GREEN);
		this.room = room;
		setVisible(true);
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		MenuStart.paintRoom(g2d, room, this);
		
	}

}
