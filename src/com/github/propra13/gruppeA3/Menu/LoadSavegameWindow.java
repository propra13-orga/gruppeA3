package com.github.propra13.gruppeA3.Menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Editor.ListRenderer;

public class LoadSavegameWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final static int MINWIDTH = 350;
	private final static int MINHEIGHT = 300;
	
	//Fensterelemente
	JButton bDone, bCancel;
	JList<JPanel> list;
	LinkedList<File> saveFiles = new LinkedList<File>();
	
	public LoadSavegameWindow() {
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("Öffne Karte ...");
		setResizable(false);
		MenuStart.centerWindow(this);
		
		/* Savegame-Liste
		 * Listeneinträge sind JPanels, die JLabels beinhalten
		 */
		//JList-Setup
		list = new JList<JPanel>();
		list.setCellRenderer(new ListRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setFixedCellHeight(38);
		
		//Liste einlesen
		updateList();
		
		JScrollPane mapListPane = new JScrollPane(list);
		mapListPane.setPreferredSize(new Dimension(100, 300)); //Größe der Liste
		
		//Liste auf Dialog legen
		GridBagConstraints listConstraints = new GridBagConstraints();
		listConstraints.gridx = 0;
		listConstraints.gridy = 0;
		listConstraints.gridheight = 1;
		listConstraints.gridwidth = 2;
		listConstraints.fill = GridBagConstraints.BOTH;
		listConstraints.weightx = 1;
		listConstraints.weighty = 6;
		listConstraints.insets = new Insets(2,4,2,4);
		add(mapListPane, listConstraints);
		
		/*
		 * Buttons
		 */
		bDone = new JButton("Spielen");
		bDone.addActionListener(this);
		GridBagConstraints doneConstraints = new GridBagConstraints();
		doneConstraints.gridx = 0;
		doneConstraints.gridy = 2;
		doneConstraints.gridheight = doneConstraints.gridwidth = 1;
		doneConstraints.fill = GridBagConstraints.HORIZONTAL;
		doneConstraints.weightx = doneConstraints.weighty = 1;
		doneConstraints.insets = new Insets(1,4,2,4);
		add(bDone, doneConstraints);
		
		bCancel = new JButton("Abbrechen");
		bCancel.addActionListener(this);
		GridBagConstraints cancelConstraints = new GridBagConstraints();
		cancelConstraints.gridx = 1;
		cancelConstraints.gridy = 2;
		cancelConstraints.gridheight = cancelConstraints.gridwidth = 1;
		cancelConstraints.fill = GridBagConstraints.HORIZONTAL;
		cancelConstraints.weightx = cancelConstraints.weighty = 1;
		cancelConstraints.insets = new Insets(1,4,2,4);
		add(bCancel, cancelConstraints);
		

		list.setSelectedIndex(0);
		setVisible(true);
		
	}
	
	private void updateList() {
		//Alle Dateien in maps/
		File f = new File(MenuStart.saveDir);
		if (! f.exists())
			try {
				throw new FileNotFoundException(MenuStart.saveDir);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
		//Sammle save-dateien
		File[] savesDirEntries = f.listFiles();
		saveFiles.clear();
		
		for(int i=0; i < savesDirEntries.length; i++) {
			if(savesDirEntries[i].isFile()) {
				File file = savesDirEntries[i];
				String fileName = file.getName();
				String[] fileNameParts = fileName.split("\\.(?=[^\\.]+$)");
				
				//Auf Raum- und Headerdateien prüfen
				if(fileNameParts.length > 1) {
					if(fileNameParts[1].equals(MenuStart.saveEnding))
						saveFiles.add(file);
				}
			}
		}
		
		//In Panels zusammenbasteln
		JPanel[] panels = new JPanel[saveFiles.size()];
		for(int i=0; i < panels.length; i++) {
			panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel label = new JLabel(saveFiles.get(i).getName());
			panels[i].add(label);
		}
		
		list.setListData(panels);
	}
	
	/*
	 * Button-Aktionen
	 */

	/**Action-Listener für die Buttons*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bCancel)
			setVisible(false);
		else if(e.getSource() == bDone) {
			Game.Menu.loadGame(saveFiles.get(list.getSelectedIndex()));
			setVisible(false);
		}
		
	}


}
