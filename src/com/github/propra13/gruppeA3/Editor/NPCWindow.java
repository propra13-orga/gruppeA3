package com.github.propra13.gruppeA3.Editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.NPC;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Menu.MenuStart;


/**
 * Klasse für den NPC-Dialog. Wird im Editor-Konstruktor
 * konstruiert und bei Bedarf mit Werten gefüllt.
 * @author christian
 *
 */
public class NPCWindow extends JDialog implements ActionListener, ListSelectionListener {
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
	JRadioButton infoRB, shopRB;

	JTextField nameField;
	
	//Rechter Editor-Teil (abhängig von NPC-Typ)
	JPanel infoNPC_P, shopNPC_P;
	
	//Info-NPC-Kram
	JTextArea textArea;
	
	//Shop-NPC-Kram
	JList<JPanel> shopList;
	JLabel healthPEntry, manaPEntry, poisonPEntry, swordEntry, shieldEntry;
	JButton plus, minus;
	JPanel[] shopData = new JPanel[5];
	JTextArea shopTextArea;
	GridBagConstraints panelConstr;
	
	//Anzahl der Items im Shop
	int healthCtr=0;
	int manaCtr=0;
	int poisonCtr=0;
	int swordCtr=0;
	int shieldCtr=0;
	
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
		
		//Button-Gruppe, damit nur einer selektiert sein kann
		ButtonGroup group = new ButtonGroup();
		group.add(infoRB);
		group.add(shopRB);
		
		
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
		panelConstr = new GridBagConstraints();
		panelConstr.gridx = 2;
		panelConstr.gridy = 0;
		panelConstr.gridheight = 6;
		panelConstr.gridwidth = 1;
		panelConstr.fill = GridBagConstraints.BOTH;
		panelConstr.weightx = 7;
		panelConstr.weighty = 1;
		panelConstr.insets = new Insets(4,4,4,4);
		
		/*
		 * Info-NPC-Edit-Panel
		 */
		infoNPC_P = new JPanel();
		infoNPC_P.setLayout(new BoxLayout(infoNPC_P, BoxLayout.Y_AXIS));
		
		JLabel desc = new JLabel("Dialogtext:");
		desc.setAlignmentY(Component.TOP_ALIGNMENT);
		infoNPC_P.add(desc);
		
		infoNPC_P.add(Box.createRigidArea(new Dimension(6,6)));
		
		textArea = new JTextArea(30, 50);
		textArea.setAlignmentY(Component.TOP_ALIGNMENT);
		infoNPC_P.add(textArea);
		
		
		/*
		 * Shop-NPC-Edit-Panel
		 */
		shopNPC_P = new JPanel();
		shopNPC_P.setLayout(new GridBagLayout());
		
		//Liste
		//Health-Pot-Panel
		shopData[0] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		healthPEntry = new JLabel("HP-Tränke: "+healthCtr, new ImageIcon(GameWindow.lifePosion), JLabel.LEFT);
		shopData[0].add(healthPEntry);
		
		//Mana-Pot-Panel
		shopData[1] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		manaPEntry = new JLabel("Mana-Tränke: "+manaCtr, new ImageIcon(GameWindow.manaPosion), JLabel.LEFT);
		shopData[1].add(manaPEntry);
		
		//Gift-Pot-Panel
		shopData[2] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		poisonPEntry = new JLabel("Gifttränke: "+poisonCtr, new ImageIcon(GameWindow.deadlyPosion), JLabel.LEFT);
		shopData[2].add(poisonPEntry);
		
		//Schwert-Panel
		shopData[3] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		swordEntry = new JLabel("Schwerter: "+swordCtr, new ImageIcon(GameWindow.sword), JLabel.LEFT);
		shopData[3].add(swordEntry);
		
		//Schild-Panel
		shopData[4] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		shieldEntry = new JLabel("Schilde: "+shieldCtr, new ImageIcon(GameWindow.shield), JLabel.LEFT);
		shopData[4].add(shieldEntry);
		
		//List-Setup
		shopList = new JList<JPanel>();
		shopList.setCellRenderer(new ListRenderer());
		shopList.addListSelectionListener(this);
		shopList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shopList.setLayoutOrientation(JList.VERTICAL);
		shopList.setFixedCellHeight(38);
		shopList.setListData(shopData);
		
		//List auf Scrollpane legen
		JScrollPane shopListPane = new JScrollPane(shopList);
		shopListPane.setPreferredSize(new Dimension(100, 300)); //Größe der Liste
		shopNPC_P.add(shopListPane);
		
		//Scrollpane hinzufügen
		GridBagConstraints listConstr = new GridBagConstraints();
		listConstr.gridx = 0;
		listConstr.gridy = 0;
		listConstr.gridheight = 2;
		listConstr.gridwidth = 2;
		listConstr.fill = GridBagConstraints.BOTH;
		listConstr.weightx = 1;
		listConstr.weighty = 5;
		listConstr.insets = new Insets(2,4,2,4);
		shopNPC_P.add(shopListPane, listConstr);
		
		//Buttons
		plus = new JButton("+");
		plus.addActionListener(this);
		GridBagConstraints plusConstr = new GridBagConstraints();
		plusConstr.gridx = 0;
		plusConstr.gridy = 2;
		plusConstr.gridheight = 1;
		plusConstr.gridwidth = 1;
		plusConstr.fill = GridBagConstraints.HORIZONTAL;
		plusConstr.weightx = plusConstr.weighty = 1;
		plusConstr.insets = new Insets(2,4,2,4);
		shopNPC_P.add(plus, plusConstr);
		
		minus = new JButton("-");
		minus.addActionListener(this);
		GridBagConstraints minusConstr = new GridBagConstraints();
		minusConstr.gridx = 1;
		minusConstr.gridy = 2;
		minusConstr.gridheight = 1;
		minusConstr.gridwidth = 1;
		minusConstr.fill = GridBagConstraints.HORIZONTAL;
		minusConstr.weightx = minusConstr.weighty = 1;
		minusConstr.insets = new Insets(2,4,2,4);
		shopNPC_P.add(minus, minusConstr);
		
		//TextArea
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridBagLayout());
		GridBagConstraints textPanelConstr = new GridBagConstraints();
		textPanelConstr.gridx = 2;
		textPanelConstr.gridy = 0;
		textPanelConstr.gridheight = 3;
		textPanelConstr.gridwidth = 1;
		textPanelConstr.fill = GridBagConstraints.BOTH;
		textPanelConstr.weightx = 4;
		textPanelConstr.weighty = 1;
		textPanelConstr.insets = new Insets(2,4,2,4);
		
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		
		JLabel title = new JLabel("Dialogtext:");
		title.setAlignmentY(Component.TOP_ALIGNMENT);
		textPanel.add(title);
		
		textPanel.add(Box.createRigidArea(new Dimension(6,6)));

        JScrollPane shopTextScroller = new JScrollPane();
        shopTextArea = new JTextArea(30,30);
        shopTextScroller.setViewportView(shopTextArea);
		textPanel.add(shopTextScroller);
		
		shopNPC_P.add(textPanel, textPanelConstr);
		
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
			workingNPC = new NPC(1, "", "", field.pos.x, field.pos.y);
		else
			workingNPC = npc;
		
		//Fenster befüllen
		nameField.setText(workingNPC.getName());
		switch(workingNPC.getType()) {
		//Info-NPC
		case 1:
			infoRB.setSelected(true);
			remove(shopNPC_P);
			add(infoNPC_P);
			break;
		//Shop-NPC
		case 2:
			//Shop-Liste einlesen
			Item testItem;
			for(Iterator<Item> iter = workingNPC.getItems().iterator(); iter.hasNext();) {
				testItem = iter.next();
				switch(testItem.getType()) {
				//HP-Pot
				case 1:
					healthCtr++;
					break;
				//Giftpot
				case 2:
					poisonCtr++;
					break;
				//Manapot
				case 3:
					manaCtr++;
					break;
				//Schwert
				case 4:
					swordCtr++;
					break;
				//Schild
				case 5:
					shieldCtr++;
					break;
				}
			}
			update();
			
			shopTextArea.setText(workingNPC.getText());
			
			shopRB.setSelected(true);
			remove(infoNPC_P);
			add(shopNPC_P);
			break;
		}
		
		update();
		
		//Window-Setup
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
	
	/**
	 * Updatet die Felder in der Shop-Liste.
	 */
	private void update() {

		healthPEntry.setText("HP-Tränke: "+healthCtr);
		manaPEntry.setText("Mana-Tränke: "+manaCtr);
		poisonPEntry.setText("Gifttränke: "+poisonCtr);
		swordEntry.setText("Schwerter: "+swordCtr);
		shieldEntry.setText("Schilde: "+shieldCtr);
		shopList.setListData(shopData);
	}
	
	/**
	 * Fügt im Shop-Editor zum ausgewählten Item-Typ eins hinzu.
	 * Wird aufgerufen, falls "+" gedrückt wurde.
	 */
	private void addItem() {
		int index = shopList.getSelectedIndex();
		switch(index) {
		case 0:
			healthCtr++;
			break;
		case 1:
			manaCtr++;
			break;
		case 2:
			poisonCtr++;
			break;
		case 3:
			swordCtr++;
			break;
		case 4:
			shieldCtr++;
			break;
		default:
			break;
		}
		update();
		shopList.setSelectedIndex(index);
	}
	
	/**
	 * Entfernt im Shop-Editor vom ausgewählten Item-Typ eines.
	 * Wird aufgerufen, falls "-" gedrückt wurde.
	 */
	private void delItem() {
		int index = shopList.getSelectedIndex();
		switch(index) {
		case 0:
			if(healthCtr > 0)
				healthCtr--;
			break;
		case 1:
			if(manaCtr > 0)
				manaCtr--;
			break;
		case 2:
			if(poisonCtr > 0)
				poisonCtr--;
			break;
		case 3:
			if(swordCtr > 0)
				swordCtr--;
			break;
		case 4:
			if(shieldCtr > 0)
				shieldCtr--;
			break;
		default:
			break;
		}
		update();
		shopList.setSelectedIndex(index);
	}

	/**Action-Listener für die Buttons*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bCancel)
			cancel();
		else if(e.getSource() == bDone)
			save();
		else if(e.getSource() == plus)
			addItem();
		else if(e.getSource() == minus)
			delItem();

		else if(e.getSource() == infoRB) {
			remove(shopNPC_P);
			add(infoNPC_P, panelConstr);
			validate();
			repaint();
		}
		else if(e.getSource() == shopRB) {
			add(shopNPC_P, panelConstr);
			remove(infoNPC_P);
			validate();
			repaint();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
	}


}
