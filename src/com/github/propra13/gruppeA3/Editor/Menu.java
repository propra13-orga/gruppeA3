package com.github.propra13.gruppeA3.Editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	Editor editor;
	
	public Menu(Editor editor) {
		
		this.editor = editor;
		
		setLayout(new GridBagLayout());
		
		//Buttons
		//Insets: Abstand zwischen Elementen, new Insets(oben, links, unten, rechts)
		JButton bOpen = new JButton("Öffnen");
		GridBagConstraints openConstraints = new GridBagConstraints();
		openConstraints.gridx = openConstraints.gridy = 0;
		openConstraints.gridheight = openConstraints.gridwidth = 1;
		openConstraints.fill = GridBagConstraints.HORIZONTAL;
		openConstraints.weightx = openConstraints.weighty = 1;
		openConstraints.insets = new Insets(2,4,2,4);
		add(bOpen, openConstraints);
		
		JButton bSave = new JButton("Speichern");
		GridBagConstraints saveConstraints = new GridBagConstraints();
		saveConstraints.gridx = 0;
		saveConstraints.gridy = 1;
		saveConstraints.gridheight = saveConstraints.gridwidth = 1;
		saveConstraints.fill = GridBagConstraints.HORIZONTAL;
		saveConstraints.weightx = saveConstraints.weighty = 1;
		saveConstraints.insets = new Insets(2,4,2,4);
		add(bSave, saveConstraints);
		
		JButton bSaveAs = new JButton("Speichern unter ...");
		GridBagConstraints saveAsConstraints = new GridBagConstraints();
		saveAsConstraints.gridx = 0;
		saveAsConstraints.gridy = 2;
		saveAsConstraints.gridheight = saveAsConstraints.gridwidth = 1;
		saveAsConstraints.fill = GridBagConstraints.HORIZONTAL;
		saveAsConstraints.weightx = saveAsConstraints.weighty = 1;
		saveAsConstraints.insets = new Insets(2,4,2,4);
		add(bSaveAs, saveAsConstraints);
		
		JButton bExit = new JButton("Beenden");
		GridBagConstraints exitConstraints = new GridBagConstraints();
		exitConstraints.gridx = 0;
		exitConstraints.gridy = 3;
		exitConstraints.gridheight = exitConstraints.gridwidth = 1;
		exitConstraints.fill = GridBagConstraints.HORIZONTAL;
		exitConstraints.weightx = exitConstraints.weighty = 1;
		exitConstraints.insets = new Insets(2,4,2,4);
		add(bExit, exitConstraints);
		
		JButton bAdd = new JButton("Raum hinzufügen");
		GridBagConstraints addConstraints = new GridBagConstraints();
		addConstraints.gridx = 0;
		addConstraints.gridy = 5;
		addConstraints.gridheight = addConstraints.gridwidth = 1;
		addConstraints.fill = GridBagConstraints.HORIZONTAL;
		addConstraints.weightx = addConstraints.weighty = 1;
		addConstraints.insets = new Insets(2,4,2,4);
		add(bAdd, addConstraints);
		
		JButton bMove = new JButton("Raum verschieben");
		GridBagConstraints moveConstraints = new GridBagConstraints();
		moveConstraints.gridx = 0;
		moveConstraints.gridy = 6;
		moveConstraints.gridheight = moveConstraints.gridwidth = 1;
		moveConstraints.fill = GridBagConstraints.HORIZONTAL;
		moveConstraints.weightx = moveConstraints.weighty = 1;
		moveConstraints.insets = new Insets(2,4,2,4);
		add(bMove, moveConstraints);
		
		JButton bDel = new JButton("Raum entfernen");
		GridBagConstraints delConstraints = new GridBagConstraints();
		delConstraints.gridx = 0;
		delConstraints.gridy = 7;
		delConstraints.gridheight = moveConstraints.gridwidth = 1;
		delConstraints.fill = GridBagConstraints.HORIZONTAL;
		delConstraints.weightx = moveConstraints.weighty = 1;
		delConstraints.insets = new Insets(2,4,2,4);
		add(bDel, delConstraints);

		// Button-Actions
		bOpen.setActionCommand("bOpen");
		bOpen.addActionListener(this);
		bSave.setActionCommand("bSave");
		bSave.addActionListener(this);
		bSaveAs.setActionCommand("bSaveAs");
		bSaveAs.addActionListener(this);
		bExit.setActionCommand("bExit");
		bExit.addActionListener(this);
		bAdd.setActionCommand("bAdd");
		bAdd.addActionListener(this);
		bMove.setActionCommand("bMove");
		bMove.addActionListener(this);
		bDel.setActionCommand("bDel");
		bDel.addActionListener(this);
		
		
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
		Dimension size = new Dimension(0, 300);
		dummyUPanel.setMinimumSize(size);
		dummyUPanel.setPreferredSize(size);
		add(dummyUPanel, dummyUConstraints);
		
		
		setVisible(true);
	}
	
	// Button-Aktionen
	
	private void bNewMap() {
		System.out.println("\"Neu\" gedrückt");
		
	}

	private void bOpen() {
		System.out.println("\"Öffnen\" gedrückt");
		
	}
	
	private void bSave() {
		System.out.println("\"Speichern\" gedrückt");
		
	}
	
	private void bSaveAs() {
		System.out.println("\"Speichern unter ...\" gedrückt");
		
	}
	
	private void bExit() {
		System.out.println("\"Beenden\" gedrückt");
		editor.exit();
	}
	
	private void bAdd() {
		System.out.println("\"Raum hinzufügen\" gedrückt");
	}
	
	private void bMove() {
		System.out.println("\"Raum verschieben\" gedrückt");
	}
	
	private void bDel() {
		System.out.println("\"Raum löschen\" gedrückt");
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();
		if("bNewMap".equals(cmd))
			bNewMap();
		else if("bOpen".equals(cmd))
			bOpen();
		else if("bSave".equals(cmd))
			bSave();
		else if("bSaveAs".equals(cmd))
			bSaveAs();
		else if("bExit".equals(cmd))
			bExit();
		else if("bAdd".equals(cmd))
			bAdd();
		else if("bMove".equals(cmd))
			bMove();
		else if("bDel".equals(cmd))
			bDel();
	}

}
