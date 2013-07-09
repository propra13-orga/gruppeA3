package com.github.propra13.gruppeA3.Editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Entities.NPC;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Menu.MenuStart;


/**
 * Klasse für den NPC-Dialog. Wird im Editor-Konstruktor
 * konstruiert und bei Bedarf mit Werten gefüllt.
 * @author christian
 *
 */
public class NPCWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	NPC NPCToEdit; //NPC, der bearbeitet werden soll
	NPC workingNPC; //Arbeitskopie des NPCs
	
	Field affectedField; //Feld, auf dem sich der NPC befindet
	RoomTab roomTab; //Raum-Tab, auf dessen Raum sich der NPC befindet
	
	private final static int MINWIDTH = 700;
	private final static int MINHEIGHT = 300;
	
	//Fensterelemente
	JButton bDone, bCancel;
	
	//Listeneinträge
	JRadioButton infoRB, shopRB, questRB;
	
	//Rechter Editor-Teil (abhängig von NPC-Typ)
	JPanel infoNPC_P, shopNPC_P, questNPC_P;
	
	JTextField nameField;
	JTextArea textArea;
	
	public NPCWindow() {
		//JDialog aufbauen
		super(Game.frame);
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(MINWIDTH, MINHEIGHT);
		setModal(true); // blockiert Hauptfenster
		setTitle("NPC-Editor");
		setResizable(false);
		
		
		//NPC-Name
		JLabel nameLabel = new JLabel("Name des NPCs");
		GridBagConstraints nameLConstr = new GridBagConstraints();
		nameLConstr.gridx = 0;
		nameLConstr.gridy = 0;
		nameLConstr.gridheight = nameLConstr.gridwidth = 1;
		nameLConstr.fill = GridBagConstraints.HORIZONTAL;
		nameLConstr.weightx = nameLConstr.weighty = 1;
		nameLConstr.insets = new Insets(1,20,1,4);
		add(nameLabel, nameLConstr);
		
		nameField = new JTextField(15);
		//nameField.setPreferredSize(new Dimension(120, 20));
		GridBagConstraints nameConstr = new GridBagConstraints();
		nameConstr.gridx = 0;
		nameConstr.gridy = 1;
		nameConstr.gridheight = nameConstr.gridwidth = 1;
		nameConstr.fill = GridBagConstraints.HORIZONTAL;
		nameConstr.weightx = nameConstr.weighty = 1;
		nameConstr.insets = new Insets(1,4,20,4);
		add(nameField, nameConstr);
		
		/*
		 * Radio-Button-Liste
		 */
		JLabel typeL = new JLabel("NPC-Typ");
		GridBagConstraints typeLConstraints = new GridBagConstraints();
		typeLConstraints.gridx = 0;
		typeLConstraints.gridy = 2;
		typeLConstraints.gridheight = typeLConstraints.gridwidth = 1;
		typeLConstraints.fill = GridBagConstraints.HORIZONTAL;
		typeLConstraints.weightx = typeLConstraints.weighty = 1;
		typeLConstraints.insets = new Insets(1,20,2,4);
		add(typeL, typeLConstraints);
		
		infoRB = new JRadioButton("Info-NPC");
		infoRB.addActionListener(this);
		GridBagConstraints infoConstraints = new GridBagConstraints();
		infoConstraints.gridx = 0;
		infoConstraints.gridy = 3;
		infoConstraints.gridheight = infoConstraints.gridwidth = 1;
		infoConstraints.fill = GridBagConstraints.HORIZONTAL;
		infoConstraints.weightx = infoConstraints.weighty = 1;
		infoConstraints.insets = new Insets(1,4,2,4);
		add(infoRB, infoConstraints);
		
		shopRB = new JRadioButton("Shop");
		shopRB.addActionListener(this);
		GridBagConstraints shopConstr = new GridBagConstraints();
		shopConstr.gridx = 0;
		shopConstr.gridy = 4;
		shopConstr.gridheight = shopConstr.gridwidth = 1;
		shopConstr.fill = GridBagConstraints.HORIZONTAL;
		shopConstr.weightx = shopConstr.weighty = 1;
		shopConstr.insets = new Insets(1,4,2,4);
		add(shopRB, shopConstr);
		
		questRB = new JRadioButton("Quest");
		questRB.addActionListener(this);
		GridBagConstraints questConstr = new GridBagConstraints();
		questConstr.gridx = 0;
		questConstr.gridy = 5;
		questConstr.gridheight = questConstr.gridwidth = 1;
		questConstr.fill = GridBagConstraints.HORIZONTAL;
		questConstr.weightx = questConstr.weighty = 1;
		questConstr.insets = new Insets(1,4,2,4);
		add(questRB, questConstr);
		
		//Button-Gruppe, damit nur einer selektiert sein kann
		ButtonGroup group = new ButtonGroup();
		group.add(infoRB);
		group.add(shopRB);
		group.add(questRB);
		
		
		//Senkrechte Linie
		GridBagConstraints sepConstr = new GridBagConstraints();
		sepConstr.gridx = 1;
		sepConstr.gridy = 0;
		sepConstr.gridheight = 6;
		sepConstr.gridwidth = 1; //Für Symmetrie der unteren Buttons
		sepConstr.fill = GridBagConstraints.VERTICAL;
		sepConstr.weightx = 0.1;
		sepConstr.weighty = 1;
		add(new JSeparator(SwingConstants.VERTICAL), sepConstr);
		
		/*
		 * Panels
		 */
		//Allgemeine GridBagConstraints für alle Panels
		GridBagConstraints panelConstr = new GridBagConstraints();
		panelConstr.gridx = 2;
		panelConstr.gridy = 0;
		panelConstr.gridheight = 6;
		panelConstr.gridwidth = 1;
		panelConstr.fill = GridBagConstraints.BOTH;
		panelConstr.weightx = 5;
		panelConstr.weighty = 1;
		panelConstr.insets = new Insets(4,4,4,4);
		
		//Info-NPC-Edit-Panel
		infoNPC_P = new JPanel();
		infoNPC_P.setLayout(new BoxLayout(infoNPC_P, BoxLayout.Y_AXIS));
		
		JLabel desc = new JLabel("Dialogtext:");
		desc.setAlignmentY(Component.TOP_ALIGNMENT);
		infoNPC_P.add(desc);
		
		infoNPC_P.add(Box.createRigidArea(new Dimension(6,6)));
		
		textArea = new JTextArea();
		textArea.setAlignmentY(Component.TOP_ALIGNMENT);
		infoNPC_P.add(textArea);
		
		add(infoNPC_P, panelConstr);
		
		/*
		 * Buttons
		 */
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints buttPConstr = new GridBagConstraints();
		buttPConstr.gridx = 0;
		buttPConstr.gridy = 6;
		buttPConstr.gridheight = 1;
		buttPConstr.gridwidth = 3;
		buttPConstr.fill = GridBagConstraints.HORIZONTAL;
		buttPConstr.weightx = buttPConstr.weighty = 1;
		//buttPConstr.insets = new Insets(2,4,2,4);
		add(buttonPanel, buttPConstr);
		
		bDone = new JButton("Fertig");
		bDone.addActionListener(this);
		GridBagConstraints doneConstraints = new GridBagConstraints();
		doneConstraints.gridx = 0;
		doneConstraints.gridy = 0;
		doneConstraints.gridheight = doneConstraints.gridwidth = 1;
		doneConstraints.fill = GridBagConstraints.HORIZONTAL;
		doneConstraints.weightx = doneConstraints.weighty = 1;
		doneConstraints.insets = new Insets(2,4,2,4);
		buttonPanel.add(bDone, doneConstraints);
		
		bCancel = new JButton("Abbrechen");
		bCancel.addActionListener(this);
		GridBagConstraints cancelConstraints = new GridBagConstraints();
		cancelConstraints.gridx = 1;
		cancelConstraints.gridy = 0;
		cancelConstraints.gridheight = cancelConstraints.gridwidth = 1;
		cancelConstraints.fill = GridBagConstraints.HORIZONTAL;
		cancelConstraints.weightx = cancelConstraints.weighty = 1;
		cancelConstraints.insets = new Insets(2,4,2,4);
		buttonPanel.add(bCancel, cancelConstraints);
		
	}
	
	/**
	 * Zeigt das NPC-Editor-Fenster mit dem angegebenen NPC
	 * @param room Raum, in dem sich der zu bearbeitende NPC befindet.
	 * @param field Feld, auf dem sich der NPC befindet.
	 * @param npc NPC, der bearbeitet werden soll.
	 */
	public void showWindow(RoomTab roomTab, Field field, NPC npc) {
		this.roomTab = roomTab;
		
		//workingNPC und NPCToEdit setzen
		NPCToEdit = npc;
		if(NPCToEdit == null)
			System.out.println();
		else
			workingNPC = npc;
		
		//Fenster-Setup
		MenuStart.centerWindow(this);
		setVisible(true);
	}
	
	/*
	 * Button-Aktionen
	 */
	
	/**
	 * Übernimmt die Änderungen und schließt den NPC-Editor.
	 * Wird aufgerufen, falls "Fertig" gedrückt wurde.
	 */
	private void save() {
		
		//Ein bestehender NPC wird gelöscht
		if(workingNPC.getType() == 0 && NPCToEdit != null)
			roomTab.room.entities.remove(NPCToEdit);
		
		//Ein bestehender NPC wird geändert
		else if(workingNPC.getType() != 0 && NPCToEdit != null)
			NPCToEdit.edit(workingNPC);
		
		//Ein neuer NPC wird hinzugefügt
		else if(workingNPC.getType() != 0 && NPCToEdit == null)
			roomTab.room.entities.add(workingNPC);
		
		NPCToEdit = workingNPC = null;
		roomTab.clearHighlights();
		roomTab.repaint();
		setVisible(false);
	}
	
	/**
	 * Schließt den NPC-Editor, verwirft alle Änderungen.
	 * Wird aufgerufen, falls "Abbrechen" gedrückt wurde.
	 */
	private void cancel() {
		NPCToEdit = workingNPC = null;
		roomTab.clearHighlights();
		setVisible(false);
	}

	/**Action-Listener für die Buttons*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bCancel)
			cancel();
		else if(e.getSource() == bDone)
			save();
		
	}


}
