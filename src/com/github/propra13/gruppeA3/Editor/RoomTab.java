package com.github.propra13.gruppeA3.Editor;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.NPC;
import com.github.propra13.gruppeA3.Map.Checkpoint;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * Klasse für das Raum-Editor-JPanel eines Raums.
 * @author christian
 */
public class RoomTab extends JPanel implements MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;
	
	private JPopupMenu dropdown;
	
	private Field affectedField;
	public Room room;
	private int lastFieldType = 2; //speichert letzte Feldkonvertierung für Linksklick
	
	// Dropdown-Menüitems
	private JMenu type;
	private JMenuItem type1;
	private JMenuItem type2;
	
	private JMenuItem addSubMenu;
	private JMenuItem addLink;
	private JMenuItem addRiver; //TODO
	private JMenuItem changeNPC;
	
	private JMenu addTrigger;
	private JMenuItem addCheckpoint;
	private JMenuItem addSpawn;
	
	//Ändere-Kram
	private JMenuItem changeLink;
	private JMenuItem changeTrigger;
	private JMenuItem changeMonster;
	private JMenuItem changeItem;
	private JMenuItem delSpawn;
	
	private LinkedList<JMenuItem> removeCandidates = new LinkedList<JMenuItem>();

	/**
	 * @param room Raum, der in diesem JPanel dargestellt wird.
	 */
	public RoomTab(Room room) {
		setLayout(null);
		this.room = room;
		
		// Kontextmenü mit Standardfeldern versehen
		dropdown = new JPopupMenu();
		
		//PopupListener, um Highlights abzuräumen
		dropdown.addPopupMenuListener(new PopupMenuListener() {

			/**Räumt Feld-Highlights ab.*/
		    @Override
		    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		        clearHighlights();
		    }

		    /**Nicht implementiert.*/
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {}
			/**Nicht implementiert.*/
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {}
		});
		
		type = new JMenu("Ändere Feldtyp ...");
		dropdown.add(type);
		
		type1 = new JMenuItem("Ändere Typ: Boden");
		type1.addActionListener(this);
		type.add(type1);
		
		type2 = new JMenuItem("Ändere Typ: Wand");
		type2.addActionListener(this);
		type.add(type2);
		
		addSubMenu = new JMenu("Füge hinzu ...");
		dropdown.add(addSubMenu);
		
		addRiver = new JMenuItem("Fluss");
		addRiver.addActionListener(this);
		addSubMenu.add(addRiver);
		
		// Nicht-Standardfelder initialisieren
		changeLink = new JMenuItem("Ändere Link");
		changeLink.addActionListener(this);
		changeTrigger = new JMenuItem("Ändere Trigger");
		changeTrigger.addActionListener(this);
		addLink = new JMenuItem("Link");
		addLink.addActionListener(this);
		addSpawn = new JMenuItem("Spawn");
		addSpawn.addActionListener(this);
		
		changeMonster = new JMenuItem("Monster ...");
		changeMonster.addActionListener(this);
		changeNPC = new JMenuItem("NPC ...");
		changeNPC.addActionListener(this);
		changeItem = new JMenuItem("Item ...");
		changeItem.addActionListener(this);
		delSpawn = new JMenuItem("Spawn löschen");
		delSpawn.addActionListener(this);
		
		addTrigger = new JMenu("Trigger für ...");
		addCheckpoint = new JMenuItem("Checkpoint");
		addTrigger.add(addCheckpoint);
		addCheckpoint.addActionListener(this);
		
		//Spawns markieren
		if(room.ID == 0) {
			for(Iterator<Field> iter = Map.spawns.iterator(); iter.hasNext();) {
				highlightField(iter.next(), FieldHighlight.Type.SPAWN);
			}
		}
		
		addMouseListener(this);
		setVisible(true);
	}
	
	// Feld-Edit-Methoden
	/**
	 * Ändert den Typ eines Felds.
	 * @param type Neuer Typ für das Feld.
	 */
	public void changeFieldType(int type) {
		//Simple Felder; Boden und Wand
		if(type == 1 || type == 2) {
			affectedField.type = type;
			lastFieldType = type;
			affectedField.link = null;
		}
		clearHighlights();
		validate();
		repaint();
	}
	
	/**
	 * Hebt ein Feld visuell hervor.
	 * @param field Hervorzuhebendes Feld
	 */
	public void highlightField(Field field, FieldHighlight.Type type) {
		FieldHighlight highlight = new FieldHighlight(field.pos.toPosition(), type);
		add(highlight);
		validate();
		repaint();
	}
	
	/**
	 * Hebt alle Highlights auf (entfernt sie vom JPanel).
	 */
	public void clearHighlights() {
		for(int i=0; i < getComponents().length; i++)
			if(getComponents()[i] instanceof FieldHighlight)
				remove(getComponents()[i]);
		
		//Spawns wieder markieren
		if(room.ID == 0) {
			for(Iterator<Field> iter = Map.spawns.iterator(); iter.hasNext();) {
				highlightField(iter.next(), FieldHighlight.Type.SPAWN);
			}
		}
		validate();
		repaint();
	}
	
	/**
	 * Ruft den Link-Editor mit einem neuen Link auf.
	 */
	public void addLink() {
		clearHighlights();
		Editor.editor.linkEditor.showWindow(affectedField);
	}
	
	/**
	 * Ruft den Link-Editor mit dem Link des angeklickten Felds auf.
	 */
	public void changeLink() {
		clearHighlights();
		Editor.editor.linkEditor.showWindow(affectedField.link);
	}
	/**
	 * Ruft den Trigger-Editor mit einem neuen Checkpoint auf.
	 */
	public void addCheckpoint() {
		clearHighlights();
		Editor.editor.triggerEditor.showWindow(affectedField, TriggerWindow.CHECKPOINT);
	}
	
	/**
	 * Ruft den Trigger-Editor mit dem Trigger des angeklickten Felds auf.
	 */
	public void changeTrigger() {
		clearHighlights();
		Editor.editor.triggerEditor.showWindow(affectedField.trigger);
	}
	
	/**
	 * Ruft den Monster-Editor mit dem Monster des angeklickten Felds auf.
	 */
	public void changeMonster() {
		clearHighlights();
		
		Monster monsterToShow = null;
		
		//Sucht das Monster auf affectedField
		Entities testEntity;
		for(Iterator<Entities> iter = room.entities.iterator(); iter.hasNext();) {
			testEntity = iter.next();
			if (testEntity instanceof Monster)
				if(room.getField(testEntity.getPosition()) == affectedField)
					monsterToShow = (Monster)testEntity;
		}
			
		highlightField(affectedField, FieldHighlight.Type.FIELD);
		Editor.editor.monsterEditor.showWindow(this, affectedField, monsterToShow);
	}
	
	/**
	 * Ruft den Item-Editor mit dem Item des angeklickten Felds auf.
	 */
	public void changeItem() {
		clearHighlights();
		
		Item itemToShow = null;
		//Sucht das Item auf affectedField
		Entities testEntity;
		for(Iterator<Entities> iter = room.entities.iterator(); iter.hasNext();) {
			testEntity = iter.next();
			if (testEntity instanceof Item)
				if(room.getField(testEntity.getPosition()) == affectedField)
					itemToShow = (Item)testEntity;
		}
			
		highlightField(affectedField, FieldHighlight.Type.FIELD);
		Editor.editor.itemEditor.showWindow(this, affectedField, itemToShow);
	}
	
	/**
	 * Ruft den NPC-Editor mit dem NPC des angeklickten Felds auf.
	 */
	public void changeNPC() {
		clearHighlights();
		
		NPC NPCToShow = null;
		
		//Sucht den NPC auf affectedField
		Entities testEntity;
		for(Iterator<Entities> iter = room.entities.iterator(); iter.hasNext();) {
			testEntity = iter.next();
			if (testEntity instanceof NPC)
				if(room.getField(testEntity.getPosition()) == affectedField)
					NPCToShow = (NPC)testEntity;
		}
			
		highlightField(affectedField, FieldHighlight.Type.FIELD);
		Editor.editor.NPCEditor.showWindow(this, affectedField, NPCToShow);
	}
	
	/**
	 * Fügt das angeklickte Feld als Spawn zur Spawn-Liste hinzu.
	 */
	private void addSpawn() {
		if(room.ID == 0)
			Map.spawns.add(affectedField);
		clearHighlights();
	}
	
	/**
	 * Löscht das angeklickte Feld aus der Spawn-Liste.
	 */
	private void deleteSpawn() {
		Field spawn;
		for(Iterator<Field> iter = Map.spawns.iterator(); iter.hasNext();) {
			spawn = iter.next();
			if(spawn == affectedField)
				iter.remove();
		}
		clearHighlights();
	}
	
	/** 
	 * Zeigt das Kontextmenü an der Cursor-Position
	 * @param e MouseEvent, das die Methode ausgelöst hat
	 */
	@SuppressWarnings("deprecation")
	private void dropdown(MouseEvent e) {
		
		// Räume Dropdown-Menü auf (removeCandidates)
		JMenuItem testItem;
		for( Iterator<JMenuItem> iter = removeCandidates.iterator(); iter.hasNext();) {
			testItem = iter.next();
			if(testItem != null) {
				System.out.println("Räume weg: "+testItem.getLabel());
				addSubMenu.remove(testItem);
				dropdown.remove(testItem);
				iter.remove();
			}
		}
		
		// Link-Optionen
		if(affectedField.link != null) {
			dropdown.add(changeLink);
			removeCandidates.add(changeLink);
		}
		
		// Falls Feld am Raumrand, Link zulassen
		else if( (affectedField.pos.x == Map.ROOMWIDTH -1 || affectedField.pos.x == 0) ||
				 (affectedField.pos.y == Map.ROOMHEIGHT -1 || affectedField.pos.y == 0) ) {
			addSubMenu.add(addLink);
			removeCandidates.add(addLink);
		}
		
		//Trigger-Optionen
		if(affectedField.trigger != null) {
			dropdown.add(changeTrigger);
			removeCandidates.add(addTrigger);
		}
		else {
			addSubMenu.add(addTrigger);
			removeCandidates.add(addTrigger);
		}
		
		//Entity-Edit-Optionen
		Entities testEntity = null;
		boolean entityOnField = false;
		
		//Durchsuche Entities, ob eine auf affectedField liegt
		for(Iterator<Entities> iter = room.entities.iterator(); iter.hasNext();) {
			testEntity = iter.next();
			
			//Falls eine Entity auf affectedField liegt, Schleife abbrechen
			if(room.getField(testEntity.getPosition()) == affectedField) {
				entityOnField = true;
				break;
			}
		}
		
		//Falls eine Entity auf affectedField liegt, Menü entsprechend aufsetzen
		if(entityOnField) {
			if(testEntity instanceof Monster) {
				dropdown.add(changeMonster);
				removeCandidates.add(changeMonster);
			}
			else if(testEntity instanceof Item) {
				dropdown.add(changeItem);
				removeCandidates.add(changeItem);
			}
			else if(testEntity instanceof NPC) {
				dropdown.add(changeNPC);
				removeCandidates.add(changeNPC);
			}
		}
		
		//Falls nicht, alle Entity-Möglichkeiten einblenden.
		else {
			addSubMenu.add(changeMonster);
			removeCandidates.add(changeMonster);
			
			addSubMenu.add(changeItem);
			removeCandidates.add(changeItem);
			
			addSubMenu.add(changeNPC);
			removeCandidates.add(changeNPC);
		}
		
		//Spawn-Optionen
		if(room.ID == 0) {
			Field spawn;
			boolean spawnOnField = false;
			for(Iterator<Field> iter = Map.spawns.iterator(); iter.hasNext();) {
				spawn = iter.next();
				if(spawn == affectedField) {
					spawnOnField = true;
					break;
				}
			}
			
			if(spawnOnField) {
				dropdown.add(delSpawn);
				removeCandidates.add(delSpawn);
			}
			else {
				addSubMenu.add(addSpawn);
				removeCandidates.add(addSpawn);
			}
		}
		

		// Menü anzeigen
		dropdown.show(this, e.getX(), e.getY());
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		MenuStart.paintRoom(g2d, room, this);
		
		//Male Highlights
		Component[] components = getComponents();
		FieldHighlight hl;
		for(int i=0; i < components.length; i++) {
			if(components[i] instanceof FieldHighlight){
				hl = (FieldHighlight)components[i];
				hl.paintComponent(g);
			}	
		}
	}
	
	/**
	 * Implementierte ActionListener-Methode
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.type1)
            changeFieldType(1);
		else if(e.getSource() == this.type2) 
			changeFieldType(2);
		else if(e.getSource() == this.addLink)
			addLink();
		else if(e.getSource() == this.changeLink)
			changeLink();
		else if(e.getSource() == this.changeTrigger)
			changeTrigger();
		else if(e.getSource() == this.addCheckpoint)
			addCheckpoint();
		else if(e.getSource() == this.changeMonster)
			changeMonster();
		else if(e.getSource() == this.changeNPC)
			changeNPC();
		else if(e.getSource() == this.changeItem)
			changeItem();
		else if(e.getSource() == this.addSpawn)
			addSpawn();
		else if(e.getSource() == this.delSpawn)
			deleteSpawn();
	}

	/**
	 * Implementierte MouseListener-Methode
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		affectedField = room.getField(new Position(e.getX(), e.getY()));
		
		// Auswahlklick abfangen; switch über Auswahlklick-Typ
		if(Editor.editor.chooseClick != Editor.ChooseClickType.NONE) {
			switch(Editor.editor.chooseClick) {
			case LINK:
				clearHighlights();
				highlightField(affectedField, FieldHighlight.Type.LINK);
				Editor.editor.linkEditor.chooseClick(affectedField);
				break;
				
			case CHECKPOINTFIELD:
				clearHighlights();
				Editor.editor.triggerEditor.chooseClickTrigger(affectedField);
				highlightField(affectedField, FieldHighlight.Type.TRIGGER);
				
				//Highlightet Zielfeld, falls vorhanden und in diesem Raum
				if(affectedField.trigger instanceof Checkpoint) {
					Checkpoint cp = (Checkpoint)affectedField.trigger;
					//Falls einer der beiden Zielräume des Links dieser Raum ist
					if(cp.getToActivate().targetRooms[0] == room)
						highlightField(cp.getToActivate().targetFields[0], FieldHighlight.Type.LINK);
					else if(cp.getToActivate().targetRooms[1] == room)
						highlightField(cp.getToActivate().targetFields[1], FieldHighlight.Type.LINK);
					
				}
				break;
				
			case CHECKPOINTLINK:
				//Falls gar kein Link auf dem Feld ist, melde das
				if(affectedField.link == null) {
					Editor.editor.warning.showWindow(WarningWindow.Type.HASNOLINK);
					break;
				}
				
				clearHighlights();
				Editor.editor.triggerEditor.chooseClickTarget(affectedField);
				highlightField(affectedField, FieldHighlight.Type.LINK);
				
				//Highlightet Triggerfeld, falls in diesem Raum
				if(Editor.editor.triggerEditor.workingTrigger.getField().getRoom() == room)
					highlightField(Editor.editor.triggerEditor.workingTrigger.getField(), FieldHighlight.Type.TRIGGER);
				break;
				
			default:
				break;
			}
		}
		
		//Rechtsklick
		else if(SwingUtilities.isRightMouseButton(e)) {
			clearHighlights();
			highlightField(affectedField, FieldHighlight.Type.FIELD);
			dropdown(e);
		}
		
		//Linksklick
		else if(SwingUtilities.isLeftMouseButton(e)) {
			clearHighlights();
			clearHighlights();
			changeFieldType(lastFieldType);
		}
	}

	
	
	/**Nicht implementiert.*/
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	/**Nicht implementiert.*/
	@Override
	public void mouseExited(MouseEvent arg0) {}

	/**Nicht implementiert.*/
	@Override
	public void mousePressed(MouseEvent arg0) {}

	/**Nicht implementiert.*/
	@Override
	public void mouseReleased(MouseEvent arg0) {}

	

}
