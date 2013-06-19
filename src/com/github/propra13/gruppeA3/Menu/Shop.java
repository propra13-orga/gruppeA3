package com.github.propra13.gruppeA3.Menu;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.NPC;
import com.github.propra13.gruppeA3.Entities.Player;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

/**
 * Diese Klasse erzeugt einen Shop in einem Eigenen Fenster
 * Es werden der Spieler und der Shop-NPC übergeben
 * @author Majida Dere
 *
 */
public class Shop extends JFrame 
				  implements ListSelectionListener{

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
	private JList<Item> list;
	private DefaultListModel<Item> listModel;
    private static final String buyString = "Kaufen";
    private static final String sellString = "Verkaufen";
    private JButton buyButton;
	private JButton sellButton;
    private JTextField text;
	

	/**
	 * Der Konstruktor erzeugt ein JFrame
	 * @param player Das Spieler Objekt
	 * @param npc Das Shop-NPC Objekt
	 */
	public Shop(final Player player, final NPC npc) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setType(Type.UTILITY);
		setTitle(npc.getName()+"s Shop");
		setSize(450,300);
		setLocationRelativeTo(null);
		
		items = npc.getItems();
		Item item;
		listModel = new DefaultListModel<Item>();
		Iterator<Item> iter = items.iterator();
		
		while(iter.hasNext()){
			item = (Item)iter.next();
			listModel.addElement(item);		
		}
		
        //Liste mit Scrolling.
        list = new JList<Item>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(list);

        sellButton = new JButton("Verkaufen");
        sellButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });

        sellButton.setActionCommand(sellString);
        sellButton.setEnabled(false);
 
        buyButton = new JButton("Kaufen");
        buyButton.setActionCommand(buyString);
        buyButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Item tempItem = (Item)list.getSelectedValue();
        		
        		/**
        		 * Der Wert des zu Kaufenden Objektes wird mit dem Vermögen des Players verglichen
        		 * Falls Der Spieler genug Münze besitzt, wird das Objekt aus der Liste entfernt und vom Spieler benutzt.
        		 * Wiederverkaufbare Objekte verlieren ihren Wert für den Wiederverkauf
        		 */
        		if(player.getMoney() >= tempItem.getValue()){
        			player.useItem(tempItem);
        			listModel.removeElement(tempItem);
        			player.setMoney(player.getMoney()-tempItem.getValue());
        			tempItem.setValue(tempItem.getValue()/2);
        			npc.getItems().remove(tempItem);
        			text.setText("Vielen Dank für den Einkauf!");
        		} else {
					text.setText("Du besitzt nicht genug Münzen!");
        		}
        		
        		if(listModel.isEmpty()){
        			buyButton.setEnabled(false);
        		} else buyButton.setEnabled(true);
        	}
        });
 
 
        text = new JTextField(10);
        text.setFont(new Font("Arial Black", Font.BOLD, 11));
        text.setEditable(false);
        text.setEnabled(false);
        text.setText("Der Spieler besitzt "+player.getMoney()+" Coins!");
 
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(buyButton);
        buttonPane.add(text);
        buttonPane.add(sellButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
 
        getContentPane().add(listScrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
