package com.github.propra13.gruppeA3.Editor;

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
import com.github.propra13.gruppeA3.Entities.Moveable;
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
	JRadioButton infoRB, shopRB, delRB;

	JTextField nameField;
	
	//Rechter Editor-Teil (abhängig von NPC-Typ)
	JPanel infoNPC_P, shopNPC_P;
	
	//Info-NPC-Kram
	JTextArea textArea;
	
	//Shop-NPC-Kram
	JList<JPanel> shopList;
	JLabel healthPEntry, manaPEntry, poisonPEntry, 
			phySwordEntry, fireSwordEntry, waterSwordEntry, iceSwordEntry,
			phyShieldEntry, fireShieldEntry, waterShieldEntry, iceShieldEntry;
	JButton plus, minus;
	JPanel[] shopData = new JPanel[11];
	JTextArea shopTextArea;
	GridBagConstraints panelConstr;
	
	//Anzahl der Items im Shop
	int healthCtr=0;
	int manaCtr=0;
	int poisonCtr=0;
	
	int phySwordCtr=0;
	int fireSwordCtr=0;
	int waterSwordCtr=0;
	int iceSwordCtr=0;
	
	int phyShieldCtr=0;
	int fireShieldCtr=0;
	int waterShieldCtr=0;
	int iceShieldCtr=0;
	
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
		
		/*
		 * Linkes Panel
		 */
		//Panel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());
		GridBagConstraints lPanelConstr = new GridBagConstraints();
		lPanelConstr.gridx = lPanelConstr.gridy = 0;
		lPanelConstr.gridheight = lPanelConstr.gridwidth = 1;
		lPanelConstr.fill = GridBagConstraints.BOTH;
		lPanelConstr.weightx = lPanelConstr.weighty = 1;
		lPanelConstr.insets = new Insets(2,2,2,2);
		add(leftPanel, lPanelConstr);
		
		//NPC-Name
		JLabel nameLabel = new JLabel("Name des NPCs");
		GridBagConstraints nameLConstr = new GridBagConstraints();
		nameLConstr.gridx = 0;
		nameLConstr.gridy = 0;
		nameLConstr.gridheight = nameLConstr.gridwidth = 1;
		nameLConstr.fill = GridBagConstraints.HORIZONTAL;
		nameLConstr.weightx = nameLConstr.weighty = 1;
		nameLConstr.insets = new Insets(1,20,1,4);
		leftPanel.add(nameLabel, nameLConstr);
		
		nameField = new JTextField(15);
		nameField.setMinimumSize(nameField.getPreferredSize());
		GridBagConstraints nameConstr = new GridBagConstraints();
		nameConstr.gridx = 0;
		nameConstr.gridy = 1;
		nameConstr.gridheight = nameConstr.gridwidth = 1;
		nameConstr.fill = GridBagConstraints.HORIZONTAL;
		nameConstr.weightx = nameConstr.weighty = 1;
		nameConstr.insets = new Insets(1,4,20,4);
		leftPanel.add(nameField, nameConstr);
		
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
		leftPanel.add(typeL, typeLConstraints);
		
		infoRB = new JRadioButton("Info-NPC");
		infoRB.addActionListener(this);
		GridBagConstraints infoConstraints = new GridBagConstraints();
		infoConstraints.gridx = 0;
		infoConstraints.gridy = 3;
		infoConstraints.gridheight = infoConstraints.gridwidth = 1;
		infoConstraints.fill = GridBagConstraints.HORIZONTAL;
		infoConstraints.weightx = infoConstraints.weighty = 1;
		infoConstraints.insets = new Insets(1,4,2,4);
		leftPanel.add(infoRB, infoConstraints);
		
		shopRB = new JRadioButton("Shop");
		shopRB.addActionListener(this);
		GridBagConstraints shopConstr = new GridBagConstraints();
		shopConstr.gridx = 0;
		shopConstr.gridy = 4;
		shopConstr.gridheight = shopConstr.gridwidth = 1;
		shopConstr.fill = GridBagConstraints.HORIZONTAL;
		shopConstr.weightx = shopConstr.weighty = 1;
		shopConstr.insets = new Insets(1,4,2,4);
		leftPanel.add(shopRB, shopConstr);
		
		delRB = new JRadioButton("NPC löschen");
		delRB.addActionListener(this);
		GridBagConstraints delConstr = new GridBagConstraints();
		delConstr.gridx = 0;
		delConstr.gridy = 5;
		delConstr.gridheight = delConstr.gridwidth = 1;
		delConstr.fill = GridBagConstraints.HORIZONTAL;
		delConstr.weightx = delConstr.weighty = 1;
		delConstr.insets = new Insets(1,4,2,4);
		leftPanel.add(delRB, delConstr);
		
		//Button-Gruppe, damit nur einer selektiert sein kann
		ButtonGroup group = new ButtonGroup();
		group.add(infoRB);
		group.add(shopRB);
		group.add(delRB);
		
		
		//Senkrechte Linie
		GridBagConstraints sepConstr = new GridBagConstraints();
		sepConstr.gridx = 1;
		sepConstr.gridy = 0;
		sepConstr.gridheight = 1;
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
		panelConstr.gridheight = 1;
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
		
		JLabel desc = new JLabel("Dialogtext");
		desc.setAlignmentY(Component.TOP_ALIGNMENT);
		infoNPC_P.add(desc);
		
		infoNPC_P.add(Box.createRigidArea(new Dimension(6,6)));
		
		textArea = new JTextArea(9, 40);
		textArea.setMinimumSize(textArea.getPreferredSize());
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
		
		//Phy-Schwert-Panel
		shopData[3] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		phySwordEntry = new JLabel("", new ImageIcon(GameWindow.swords[0]), JLabel.LEFT);
		shopData[3].add(phySwordEntry);
		
		//Feuer-Schwert-Panel
		shopData[4] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fireSwordEntry = new JLabel("", new ImageIcon(GameWindow.swords[1]), JLabel.LEFT);
		shopData[4].add(fireSwordEntry);
		
		//Wasser-Schwert-Panel
		shopData[5] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		waterSwordEntry = new JLabel("", new ImageIcon(GameWindow.swords[2]), JLabel.LEFT);
		shopData[5].add(waterSwordEntry);
		
		//Eis-Schwert-Panel
		shopData[6] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		iceSwordEntry = new JLabel("", new ImageIcon(GameWindow.swords[3]), JLabel.LEFT);
		shopData[6].add(iceSwordEntry);
		
		//Phy-Schild-Panel
		shopData[7] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		phyShieldEntry = new JLabel("", new ImageIcon(GameWindow.shields[0]), JLabel.LEFT);
		shopData[7].add(phyShieldEntry);
		
		//Feuer-Schild-Panel
		shopData[8] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fireShieldEntry = new JLabel("", new ImageIcon(GameWindow.shields[1]), JLabel.LEFT);
		shopData[8].add(fireShieldEntry);
		
		//Wasser-Schild-Panel
		shopData[9] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		waterShieldEntry = new JLabel("", new ImageIcon(GameWindow.shields[2]), JLabel.LEFT);
		shopData[9].add(waterShieldEntry);
		
		//Eis-Schild-Panel
		shopData[10] = new JPanel(new FlowLayout(FlowLayout.LEFT));
		iceShieldEntry = new JLabel("", new ImageIcon(GameWindow.shields[3]), JLabel.LEFT);
		shopData[10].add(iceShieldEntry);
		
		//List-Setup
		shopList = new JList<JPanel>();
		shopList.setCellRenderer(new ListRenderer());
		shopList.addListSelectionListener(this);
		shopList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		shopList.setLayoutOrientation(JList.VERTICAL);
		shopList.setFixedCellHeight(38);
		shopList.setListData(shopData);
		shopList.setMinimumSize(shopList.getPreferredSize());
		
		//List auf Scrollpane legen
		JScrollPane shopListPane = new JScrollPane(shopList);
		shopListPane.setPreferredSize(new Dimension(130, 200)); //Größe der Liste
		shopListPane.setMinimumSize(shopListPane.getPreferredSize());
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
        shopTextArea = new JTextArea(7,20);
        shopTextArea.setMinimumSize(shopTextArea.getPreferredSize());
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
		buttPConstr.gridy = 1;
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
		affectedField = field;
		
		//Shop-Zähler zurücksetzen
		healthCtr = poisonCtr = manaCtr =
				phyShieldCtr = fireShieldCtr = waterShieldCtr = iceShieldCtr =
				phySwordCtr = fireSwordCtr = waterSwordCtr = iceSwordCtr = 0;
		
		//workingNPC und NPCToEdit setzen
		NPCToEdit = npc;
		if(NPCToEdit == null)
			workingNPC = new NPC(1, "", "", affectedField.pos);
		else
			workingNPC = npc;
		
		//Fenster befüllen
		nameField.setText(workingNPC.getName());
		switch(workingNPC.getType()) {
		//Info-NPC
		case 1:
			textArea.setText(workingNPC.getText());
			
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
					switch(testItem.getElement()) {
					case FIRE:
						fireSwordCtr++;
						break;
					case ICE:
						iceSwordCtr++;
						break;
					case PHYSICAL:
						phySwordCtr++;
						break;
					case WATER:
						waterSwordCtr++;
						break;
					}
					break;
				//Schild
				case 5:
					switch(testItem.getElement()) {
					case FIRE:
						fireShieldCtr++;
						break;
					case ICE:
						iceShieldCtr++;
						break;
					case PHYSICAL:
						phyShieldCtr++;
						break;
					case WATER:
						waterShieldCtr++;
						break;
					}
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
		if(delRB.isSelected() && NPCToEdit != null)
			roomTab.room.entities.remove(NPCToEdit);
		
		//NPC speichern (Werte aus Feldern abgrasen)
		else {
			//Info-NPC
			if(infoRB.isSelected()) {
				workingNPC.setText(textArea.getText());
				workingNPC.setType(1);
				workingNPC.setName(nameField.getText());
			}
			else if(shopRB.isSelected()) {
				workingNPC.setText(shopTextArea.getText());
				workingNPC.setType(2);
				workingNPC.setName(nameField.getText());
				
				//Shop zusammensammeln
				for(int i = healthCtr; i > 0; i--) {
					workingNPC.getItems().add(new Item(10, 1, 
							affectedField.pos, "Bringt dich wieder zu Kräften",
							"Lebenstrank", 4, Moveable.Element.PHYSICAL));
				}
				for(int i = manaCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(30, 3,
							affectedField.pos, "Erweckt deine Zauberkräfte",
							"Mana-Trank", 4, Moveable.Element.PHYSICAL));
				for(int i = poisonCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(10, 2,
							affectedField.pos, "Vergiftet dich",
							"Gift-Trank", 4, Moveable.Element.PHYSICAL));
				
				//Schwerter
				for(int i = phySwordCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(2, 4,
							affectedField.pos, "Verstärkt deinen Angriff",
							"Normales Schwert", 10, Moveable.Element.PHYSICAL));
				for(int i = fireSwordCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(2, 4,
							affectedField.pos, "Verstärkt deinen Angriff",
							"Feuerschwert", 10, Moveable.Element.FIRE));
				for(int i = waterSwordCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(2, 4,
							affectedField.pos, "Verstärkt deinen Angriff",
							"Wasserschwert", 10, Moveable.Element.WATER));
				for(int i = iceSwordCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(2, 4,
							affectedField.pos, "Verstärkt deinen Angriff",
							"Eisschwert", 10, Moveable.Element.ICE));
				
				//Schilde
				for(int i = phyShieldCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(2, 5,
							affectedField.pos, "Erhöht deine Verteidigung",
							"Normaler Schild", 10, Moveable.Element.PHYSICAL));
				for(int i = fireShieldCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(2, 5,
							affectedField.pos, "Erhöht deine Verteidigung",
							"Feuerschild", 10, Moveable.Element.FIRE));
				for(int i = waterShieldCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(2, 5,
							affectedField.pos, "Erhöht deine Verteidigung",
							"Wasserschild", 10, Moveable.Element.WATER));
				for(int i = iceShieldCtr; i > 0; i--)
					workingNPC.getItems().add(new Item(2, 5,
							affectedField.pos, "Erhöht deine Verteidigung",
							"Eisschild", 10, Moveable.Element.ICE));
			}
			//Ein bestehender NPC wird geändert
			if(! delRB.isSelected() && NPCToEdit != null)
				NPCToEdit.edit(workingNPC);
			
			//Ein neuer NPC wird hinzugefügt
			else if(! delRB.isSelected() && NPCToEdit == null)
				roomTab.room.entities.add(workingNPC);
		}	
		
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

		healthPEntry.setText(healthCtr + " HP-Tränke");
		manaPEntry.setText(manaCtr + " Mana-Tränke");
		poisonPEntry.setText(poisonCtr + " Gifttränke");
		phySwordEntry.setText(phySwordCtr + " normale Schwerter");
		fireSwordEntry.setText(fireSwordCtr + " Feuerschwerter");
		waterSwordEntry.setText(waterSwordCtr + " Wasserschwerter");
		iceSwordEntry.setText(iceSwordCtr + " Eisschwerter");
		phyShieldEntry.setText(phyShieldCtr + " normale Schilde");
		fireShieldEntry.setText(fireShieldCtr + " Feuerschilde");
		waterShieldEntry.setText(waterShieldCtr + " Wasserschilde");
		iceShieldEntry.setText(iceShieldCtr + " Eisschilde");
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
			phySwordCtr++;
			break;
		case 4:
			fireSwordCtr++;
			break;
		case 5:
			waterSwordCtr++;
			break;
		case 6:
			iceSwordCtr++;
			break;
		case 7:
			phyShieldCtr++;
			break;
		case 8:
			fireShieldCtr++;
			break;
		case 9:
			waterShieldCtr++;
			break;
		case 10:
			iceShieldCtr++;
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
			if(phySwordCtr > 0)
				phySwordCtr--;
			break;
		case 4:
			if(fireSwordCtr > 0)
				fireSwordCtr--;
			break;
		case 5:
			if(waterSwordCtr > 0)
				waterSwordCtr--;
			break;
		case 6:
			if(iceSwordCtr > 0)
				iceSwordCtr--;
			break;
		case 7:
			if(phyShieldCtr > 0)
				phyShieldCtr--;
			break;
		case 8:
			if(fireShieldCtr > 0)
				fireShieldCtr--;
			break;
		case 9:
			if(waterShieldCtr > 0)
				waterShieldCtr--;
			break;
		case 10:
			if(iceShieldCtr > 0)
				iceShieldCtr--;
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
		else if(e.getSource() == delRB) {
			remove(shopNPC_P);
			remove(infoNPC_P);
			validate();
			repaint();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
	}


}
