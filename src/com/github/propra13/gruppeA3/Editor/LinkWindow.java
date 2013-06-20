package com.github.propra13.gruppeA3.Editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Link;
import com.github.propra13.gruppeA3.Map.Room;

public class LinkWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private final static int MINHEIGHT = 300;
	private final static int MINWIDTH = 500;
	
	public LinkWindow(Field field) {
		//TODO: Links nur am Rand
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("Link-Editor");
		setResizable(false);
		
		// Ausrichten: Zentrieren über Hauptfenster
		Point framePos = Game.frame.getLocationOnScreen();
		Point thisPos = new Point();
		thisPos.x = framePos.x - (Game.MINWIDTH/2) + MINWIDTH/2;
		thisPos.y = framePos.y - (Game.MINHEIGHT/2) + MINHEIGHT/2;
		setLocation(thisPos);
		
		// Link erzeugen, falls nötig
		if (field.link == null) {
					
			// Erste freie Link-ID suchen
			int freeID = 256; //256 ist mit einem Byte (Room-Datei) nicht erreichbar
			int[] IDs = new int[Editor.editor.mapLinks.size()];
			Iterator<Link> iter = Editor.editor.mapLinks.iterator();
			Link testLink;
			for (int i=0; iter.hasNext(); i++) {
				testLink = iter.next();
				IDs[i] = testLink.ID;
			}
			java.util.Arrays.sort(IDs);
			
			if (IDs[IDs.length - 1] == IDs.length - 1)
				//höchste ID == Anzahl IDs => keine ID zwischen drin
				freeID = IDs.length;
			else
				for(int i=0; i < IDs.length; i++) {
					if (IDs[i] > i) {
						freeID = i;
						break;
					}
				}
			
			System.out.println("freeID: "+freeID);
				
			// Konstruktorargumente vorbereiten
			Room[] targetRooms = new Room[2];
			targetRooms[0] = field.getRoom();
			targetRooms[1] = null;
			Field[] targetFields = new Field[2];
			targetFields[0] = field;
			targetFields[1] = null;
			field.link = new Link(6, targetRooms, targetFields, true, true);
		}
		
		
		//Fensterelemente
		JButton bDone = new JButton("Fertig");
		GridBagConstraints doneConstraints = new GridBagConstraints();
		doneConstraints.gridx = 0;
		doneConstraints.gridy = 5;
		doneConstraints.gridheight = doneConstraints.gridwidth = 1;
		doneConstraints.fill = GridBagConstraints.HORIZONTAL;
		doneConstraints.weightx = doneConstraints.weighty = 1;
		doneConstraints.insets = new Insets(2,4,2,4);
		add(bDone, doneConstraints);
		
		JButton bCancel = new JButton("Abbrechen");
		GridBagConstraints cancelConstraints = new GridBagConstraints();
		cancelConstraints.gridx = 1;
		cancelConstraints.gridy = 5;
		cancelConstraints.gridheight = cancelConstraints.gridwidth = 1;
		cancelConstraints.fill = GridBagConstraints.HORIZONTAL;
		cancelConstraints.weightx = cancelConstraints.weighty = 1;
		cancelConstraints.insets = new Insets(2,4,2,4);
		add(bCancel, cancelConstraints);
		
		JLabel titleLink1 = new JLabel("Link-Teil A");
		GridBagConstraints title1Constraints = new GridBagConstraints();
		title1Constraints.gridx = 0;
		title1Constraints.gridy = 0;
		title1Constraints.gridheight = title1Constraints.gridwidth = 1;
		title1Constraints.fill = GridBagConstraints.HORIZONTAL;
		title1Constraints.weightx = title1Constraints.weighty = 1;
		title1Constraints.insets = new Insets(2,4,2,4);
		titleLink1.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLink1, title1Constraints);
		
		JLabel titleLink2 = new JLabel("Link-Teil B");
		GridBagConstraints title2Constraints = new GridBagConstraints();
		title2Constraints.gridx = 1;
		title2Constraints.gridy = 0;
		title2Constraints.gridheight = title2Constraints.gridwidth = 1;
		title2Constraints.fill = GridBagConstraints.HORIZONTAL;
		title2Constraints.weightx = title2Constraints.weighty = 1;
		title2Constraints.insets = new Insets(2,4,2,4);
		titleLink2.setHorizontalAlignment(SwingConstants.CENTER);
		add(titleLink2, title2Constraints);
		
		JLabel roomID1 = new JLabel("Raum: "+field.getRoom().ID);
		GridBagConstraints room1Constraints = new GridBagConstraints();
		room1Constraints.gridx = 0;
		room1Constraints.gridy = 1;
		room1Constraints.gridheight = room1Constraints.gridwidth = 1;
		room1Constraints.fill = GridBagConstraints.HORIZONTAL;
		room1Constraints.weightx = room1Constraints.weighty = 1;
		room1Constraints.insets = new Insets(2,4,2,4);
		add(roomID1, room1Constraints);
		
		JLabel fieldPos1 = new JLabel("Feld: "+field.pos);
		GridBagConstraints field1Constraints = new GridBagConstraints();
		field1Constraints.gridx = 0;
		field1Constraints.gridy = 2;
		field1Constraints.gridheight = field1Constraints.gridwidth = 1;
		field1Constraints.fill = GridBagConstraints.HORIZONTAL;
		field1Constraints.weightx = field1Constraints.weighty = 1;
		field1Constraints.insets = new Insets(2,4,2,4);
		add(fieldPos1, field1Constraints);
		
		//Raum
		//Feld
		//Trigger
		
		setVisible(true);
	}
	
	//paint()

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
