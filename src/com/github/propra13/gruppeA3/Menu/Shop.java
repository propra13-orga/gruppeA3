package com.github.propra13.gruppeA3.Menu;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.LinkedList;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Editor.ListRenderer;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.NPC;
import com.github.propra13.gruppeA3.Entities.Player;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

/**
 * Diese Klasse erzeugt einen Shop in einem eigenen Fenster
 * Es werden der Spieler und der Shop-NPC übergeben
 * @author Majida Dere
 *
 */
public class Shop extends JDialog
				  implements ListSelectionListener, ActionListener {

	/**
	 * 
	 */
	/**
	 * Attribute:
	 * 			serialVersionUID: Vom Compiler generierte ID, wird nicht benutzt.
	 * 			items: Liste die Items enthält, die der NPC mit sicht führt
	 * 			list: Die Items aus der LinkedList items, werden hier graphisch dargestellt
	 * 			listModel: Das Model für die JList
	 * 			buyString: String, was auf buyButton zu sehen ist
	 * 			sellString: String, was auf sellButton zu sehen ist
	 * 			buyButton: Ein graphischer Knopf zum Kaufen von Items aus dem Shop
	 * 			sellButton: Ein graphischer Button zum Verkaufen von Items an den Shop (noch unbenutzt)
	 * 			text: Ein graphisches Textfeld zum Anzeigen von Informationen
	 */
	private static final long serialVersionUID = 3397250190749280364L;
	private LinkedList<Item> items;
	private JList<JPanel> list;
    private JButton buyButton;
	private JButton cancelButton;
    private JTextField text;
    String[] itemNames;
    Player player;
    NPC npc;
    
	DefaultTableModel tableModel;
	
	//Listeneinträge
	JPanel healthPanel, poisonPanel, manaPanel,
			iceShieldPanel, waterShieldPanel, fireShieldPanel, physicalShieldPanel,
			iceSwordPanel, waterSwordPanel, fireSwordPanel, physicalSwordPanel, dragonEggPanel;
	JLabel healthEntry, poisonEntry, manaEntry, physicalSwordEntry, iceSwordEntry, fireSwordEntry,
				waterSwordEntry, physicalShieldEntry, iceShieldEntry, fireShieldEntry,
				waterShieldEntry, dragonEggEntry;
	

	/**
	 * Der Konstruktor erzeugt ein JFrame
	 * @param player Das Spieler Objekt
	 * @param npc Das Shop-NPC Objekt
	 */
	public Shop(final Player player, final NPC npc) {
		super(Game.frame);
		this.player = player;
		this.npc = npc;
		items = npc.getItems();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setType(Type.UTILITY);
		setTitle(npc.getName()+"s Shop");
		setSize(450,300);
		setLocationRelativeTo(null);
		setModal(true);
		
		/* Item-Liste //TODO: Dynamisch einlesen
		 * Listeneinträge sind JPanels, die JLabels beinhalten
		 */
		//JList-Setup
		list = new JList<JPanel>();
		list.setCellRenderer(new ListRenderer());
		list.addListSelectionListener(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setFixedCellHeight(38);
		
		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize(new Dimension(100, 300)); //Größe der Liste
		
		//Liste auf Dialog legen
		add(listScrollPane);

        cancelButton = new JButton("Schließen");
        cancelButton.addActionListener(this);
 
        buyButton = new JButton("Kaufen");
        buyButton.addActionListener(this);
 
 
        text = new JTextField(10);
        text.setFont(new Font("Arial Black", Font.BOLD, 11));
        text.setEditable(false);
        text.setEnabled(false);
        text.setText("Du besitzt "+player.getMoney()+" Münzen!");
 
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(buyButton);
        buttonPane.add(text);
        buttonPane.add(cancelButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
 
        getContentPane().add(listScrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        
        updateList();
        
        setVisible(true);
	}
	
	/**
	 * Updatet den Inhalt der Item-Liste.
	 */
	private void updateList() {
		JPanel[] panels = new JPanel[items.size()];
		
		Item testItem;
		for(int i=0; i < panels.length; i++) {
			testItem = items.get(i);
			panels[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
			
			switch(testItem.getType()) {
			case 1:
				panels[i].add(new JLabel("Lebenstrank", new ImageIcon(GameWindow.lifePosion), JLabel.LEFT));
				break;
				
			case 2:
				panels[i].add(new JLabel("Gifttrank", new ImageIcon(GameWindow.deadlyPosion), JLabel.LEFT));
				break;
				
			case 3:
				panels[i].add(new JLabel("Manatrank", new ImageIcon(GameWindow.manaPosion), JLabel.LEFT));
				break;
				
			case 4:
				switch(testItem.getElement()) {
				case PHYSICAL:
					panels[i].add(new JLabel("Schwert", new ImageIcon(GameWindow.swords[0]), JLabel.LEFT));
					break;
				case FIRE:
					panels[i].add(new JLabel("Feuerschwert", new ImageIcon(GameWindow.swords[1]), JLabel.LEFT));
					break;
				case WATER:
					panels[i].add(new JLabel("Wasserschwert", new ImageIcon(GameWindow.swords[2]), JLabel.LEFT));
					break;
				case ICE:
					panels[i].add(new JLabel("Eisschwert", new ImageIcon(GameWindow.swords[3]), JLabel.LEFT));
					break;
				}
				break;
				
			case 5:

				switch(testItem.getElement()) {
				case PHYSICAL:
					panels[i].add(new JLabel("Schild", new ImageIcon(GameWindow.shields[0]), JLabel.LEFT));
					break;
				case FIRE:
					panels[i].add(new JLabel("Feuerschild", new ImageIcon(GameWindow.shields[1]), JLabel.LEFT));
					break;
				case WATER:
					panels[i].add(new JLabel("Wasserschild", new ImageIcon(GameWindow.shields[2]), JLabel.LEFT));
					break;
				case ICE:
					panels[i].add(new JLabel("Eisschild", new ImageIcon(GameWindow.shields[3]), JLabel.LEFT));
					break;
				}
				break;
			}
		}
		
		list.setListData(panels);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buyButton) {
			//Ausgewähltes Item bestimmen
			Item tempItem = items.get(list.getSelectedIndex());
			
			/**
			 * Der Wert des zu Kaufenden Objektes wird mit dem Vermögen des Players verglichen
			 * Falls Der Spieler genug Münze besitzt, wird das Objekt aus der Liste entfernt und vom Spieler benutzt.
			 * Wiederverkaufbare Objekte verlieren ihren Wert für den Wiederverkauf
			 */
			if(player.getMoney() >= tempItem.getValue()){
				player.useItem(tempItem);
				items.remove(list.getSelectedIndex());
				player.setMoney(player.getMoney()-tempItem.getValue());
				tempItem.setValue(tempItem.getValue()/2);
				npc.getItems().remove(tempItem);
				text.setText("Vielen Dank für den Einkauf!");
			} else {
				text.setText("Du besitzt nicht genug Münzen!");
			}
			
			updateList();
			
			if(items.isEmpty()){
				buyButton.setEnabled(false);
			} else buyButton.setEnabled(true);
		}
		else if(e.getSource() == cancelButton)
			setVisible(false);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if(list.getSelectedIndex() >= 0) {
			int value = items.get(list.getSelectedIndex()).getValue();
			text.setText("Kosten: "+value+" Münzen");
		}
		
	}

}
