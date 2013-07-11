package com.github.propra13.gruppeA3.Editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.github.propra13.gruppeA3.Game;

/**
 * Menü-Tab für den Map-Editor
 * @author Christian
 *
 */
public class Menu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	Editor editor;
	
	JButton bNewMap, bOpen, bSave, bSaveAs, bExit, bAdd, bMove, bDel, bSpawns;
	
	public Menu(Editor editor) {
		
		this.editor = editor;
		
		setLayout(new GridBagLayout());
		
		Dimension stdButtonSize = new Dimension(200,30);
		
		//Buttons
		//Insets: Abstand zwischen Elementen, new Insets(oben, links, unten, rechts)
		bNewMap = new JButton("Neue Karte");
		bNewMap.setPreferredSize(stdButtonSize);
		GridBagConstraints newMapConstraints = new GridBagConstraints();
		newMapConstraints.gridx = newMapConstraints.gridy = 0;
		newMapConstraints.gridheight = newMapConstraints.gridwidth = 1;
		newMapConstraints.fill = GridBagConstraints.HORIZONTAL;
		newMapConstraints.weightx = newMapConstraints.weighty = 1;
		newMapConstraints.insets = new Insets(10,4,2,4);
		add(bNewMap, newMapConstraints);
		
		bOpen = new JButton("Öffnen");
		bOpen.setPreferredSize(stdButtonSize);
		GridBagConstraints openConstraints = new GridBagConstraints();
		openConstraints.gridx = 0;
		openConstraints.gridy = 1;
		openConstraints.gridheight = openConstraints.gridwidth = 1;
		openConstraints.fill = GridBagConstraints.HORIZONTAL;
		openConstraints.weightx = openConstraints.weighty = 1;
		openConstraints.insets = new Insets(2,4,2,4);
		add(bOpen, openConstraints);
		
		bSave = new JButton("Speichern");
		bSave.setPreferredSize(stdButtonSize);
		GridBagConstraints saveConstraints = new GridBagConstraints();
		saveConstraints.gridx = 0;
		saveConstraints.gridy = 2;
		saveConstraints.gridheight = saveConstraints.gridwidth = 1;
		saveConstraints.fill = GridBagConstraints.HORIZONTAL;
		saveConstraints.weightx = saveConstraints.weighty = 1;
		saveConstraints.insets = new Insets(2,4,2,4);
		add(bSave, saveConstraints);
		
		bSaveAs = new JButton("Speichern als ...");
		bSaveAs.setPreferredSize(stdButtonSize);
		GridBagConstraints saveAsConstraints = new GridBagConstraints();
		saveAsConstraints.gridx = 0;
		saveAsConstraints.gridy = 3;
		saveAsConstraints.gridheight = saveAsConstraints.gridwidth = 1;
		saveAsConstraints.fill = GridBagConstraints.HORIZONTAL;
		saveAsConstraints.weightx = saveAsConstraints.weighty = 1;
		saveAsConstraints.insets = new Insets(2,4,2,4);
		add(bSaveAs, saveAsConstraints);
		
		bExit = new JButton("Beenden");
		bExit.setPreferredSize(stdButtonSize);
		GridBagConstraints exitConstraints = new GridBagConstraints();
		exitConstraints.gridx = 0;
		exitConstraints.gridy = 4;
		exitConstraints.gridheight = exitConstraints.gridwidth = 1;
		exitConstraints.fill = GridBagConstraints.HORIZONTAL;
		exitConstraints.weightx = exitConstraints.weighty = 1;
		exitConstraints.insets = new Insets(2,4,16,4);
		add(bExit, exitConstraints);
		
		bAdd = new JButton("Raum hinzufügen");
		bAdd.setPreferredSize(stdButtonSize);
		GridBagConstraints addConstraints = new GridBagConstraints();
		addConstraints.gridx = 0;
		addConstraints.gridy = 5;
		addConstraints.gridheight = addConstraints.gridwidth = 1;
		addConstraints.fill = GridBagConstraints.HORIZONTAL;
		addConstraints.weightx = addConstraints.weighty = 1;
		addConstraints.insets = new Insets(2,4,2,4);
		add(bAdd, addConstraints);
		
		bMove = new JButton("Raum verschieben");
		bMove.setPreferredSize(stdButtonSize);
		GridBagConstraints moveConstraints = new GridBagConstraints();
		moveConstraints.gridx = 0;
		moveConstraints.gridy = 6;
		moveConstraints.gridheight = moveConstraints.gridwidth = 1;
		moveConstraints.fill = GridBagConstraints.HORIZONTAL;
		moveConstraints.weightx = moveConstraints.weighty = 1;
		moveConstraints.insets = new Insets(2,4,2,4);
		add(bMove, moveConstraints);
		
		bDel = new JButton("Raum entfernen");
		bDel.setPreferredSize(stdButtonSize);
		GridBagConstraints delConstraints = new GridBagConstraints();
		delConstraints.gridx = 0;
		delConstraints.gridy = 7;
		delConstraints.gridheight = moveConstraints.gridwidth = 1;
		delConstraints.fill = GridBagConstraints.HORIZONTAL;
		delConstraints.weightx = moveConstraints.weighty = 1;
		delConstraints.insets = new Insets(2,4,16,4);
		add(bDel, delConstraints);
		
		bSpawns = new JButton("Spawns ändern");
		bSpawns.setPreferredSize(stdButtonSize);
		GridBagConstraints spawnConstraints = new GridBagConstraints();
		spawnConstraints.gridx = 0;
		spawnConstraints.gridy = 8;
		spawnConstraints.gridheight = spawnConstraints.gridwidth = 1;
		spawnConstraints.fill = GridBagConstraints.HORIZONTAL;
		spawnConstraints.weightx = spawnConstraints.weighty = 1;
		spawnConstraints.insets = new Insets(2,4,2,4);
		add(bSpawns, spawnConstraints);

		// Button-Actions
		bOpen.addActionListener(this);
		bSave.addActionListener(this);
		bSaveAs.addActionListener(this);
		bExit.addActionListener(this);
		bAdd.addActionListener(this);
		bMove.addActionListener(this);
		bDel.addActionListener(this);
		bSpawns.addActionListener(this);
		
		
		//Unsichtbares Panel rechts
		JPanel dummyRPanel = new JPanel();
		GridBagConstraints dummyRConstraints = new GridBagConstraints();
		dummyRConstraints.gridx = 1;
		dummyRConstraints.gridy = 0;
		dummyRConstraints.gridheight = 8;
		dummyRConstraints.weightx = 7;
		add(dummyRPanel, dummyRConstraints);
		
		//Unsichtbares Panel unter Menüknöpfen
		JPanel dummyUPanel = new JPanel();
		GridBagConstraints dummyUConstraints = new GridBagConstraints();
		dummyUConstraints.gridx = 0;
		dummyRConstraints.gridy = 8;
		dummyRConstraints.gridheight = 1;
		dummyRConstraints.weighty = 10;
		dummyRConstraints.fill = GridBagConstraints.VERTICAL;
		Dimension size = new Dimension(0, 200);
		dummyUPanel.setMinimumSize(size);
		dummyUPanel.setPreferredSize(size);
		add(dummyUPanel, dummyUConstraints);
		
		
		setVisible(true);
	}
	
	// Button-Aktionen
	
	/**Wird aufgerufen, wenn "Neu" gedrückt wurde.*/
	private void bNewMap() {
		System.out.println("\"Neu\" gedrückt");
		
	}

	/**Wird aufgerufen, wenn "Öffnen" gedrückt wurde.*/
	private void bOpen() {
		System.out.println("\"Öffnen\" gedrückt");
		
	}
	
	/**Wird aufgerufen, wenn "Speichern" gedrückt wurde.*/
	private void bSave() {
		Editor.editor.warning.showWindow(WarningWindow.Type.OVERWRITE);
	}
	
	/**Wird aufgerufen, wenn "Speichern unter" gedrückt wurde.*/
	private void bSaveAs() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("./data/maps"));
		fc.setDialogTitle("Karte wählen");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		add(fc);
		
		// Falls Map ausgewählt wurde, wirf Editor an
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			Editor.editor.saveAsName = fc.getSelectedFile().getName();
			Editor.editor.warning.showWindow(WarningWindow.Type.OVERWRITE);
		}
		
	}
	
	/**Wird aufgerufen, wenn "Beenden" gedrückt wurde.*/
	private void bExit() {
		System.out.println("\"Beenden\" gedrückt");
		editor.exit();
	}
	
	/**Wird aufgerufen, wenn "Raum hinzufügen" gedrückt wurde.*/
	private void bAdd() {
		System.out.println("\"Raum hinzufügen\" gedrückt");
	}
	
	/**Wird aufgerufen, wenn "Raum verschieben" gedrückt wurde.*/
	private void bMove() {
		System.out.println("\"Raum verschieben\" gedrückt");
	}
	
	/**Wird aufgerufen, wenn "Raum löschen" gedrückt wurde.*/
	private void bDel() {
		System.out.println("\"Raum löschen\" gedrückt");
	}
	
	/**Wird aufgerufen, wenn "Spawns ändern" gedrückt wurde.*/
	private void bSpawns() {
		System.out.println("\"Spawns ändern\" gedrückt");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bNewMap)
			bNewMap();
		else if(e.getSource() == bOpen)
			bOpen();
		else if(e.getSource() == bSave)
			bSave();
		else if(e.getSource() == bSaveAs)
			bSaveAs();
		else if(e.getSource() == bExit)
			bExit();
		else if(e.getSource() == bAdd)
			bAdd();
		else if(e.getSource() == bMove)
			bMove();
		else if(e.getSource() == bDel)
			bDel();
		else if(e.getSource() == bSpawns)
			bSpawns();
	}

}
