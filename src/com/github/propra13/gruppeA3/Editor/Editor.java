package com.github.propra13.gruppeA3.Editor;

import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/* @author CK
 * Hauptklasse des Map-Editors
 */
public class Editor extends JTabbedPane {
  
	private static final long serialVersionUID = 1L;
	
	private String mapName;
	@SuppressWarnings("unused")
	private LinkedList<JPanel> roomTabs = new LinkedList<JPanel>();

	public Editor(String mapName) {
		super(JTabbedPane.TOP);
		System.out.println("Ich bin ein Editor!");
		this.mapName = mapName;
		
		// init Map
		try {
			Map.initialize(this.mapName);
		} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
			e.printStackTrace();
		}
		
		//Menü-Tab
		addTab("Menü", new Menu(this));
		
		//Raum-Tabs
		for(int i=0; i < Map.mapRooms.length; i++) {
			String title = ""+i;
			addTab(title, new RoomTab(Map.getMapRoom(i)));
		}
		
		repaint();
		setVisible(true);
	}
	
	public void exit() {

		MenuStart.editor = false;
		MenuStart.menu = true;
		Game.Menu.remove(this);
		Game.Menu.setVisible(true);
	}
} 