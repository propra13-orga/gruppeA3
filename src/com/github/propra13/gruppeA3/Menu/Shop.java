package com.github.propra13.gruppeA3.Menu;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.NPC;
import com.github.propra13.gruppeA3.Entities.Player;

public class Shop extends JPanel 
				  implements ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7205764559861185949L;
	private LinkedList<Item> items;
	private JList list;
	private DefaultListModel listModel;
	
	private static final String buyString = "Kaufen";

	/**
	 * Create the panel.
	 */
	public Shop(Player player, NPC npc) {
		
		items = npc.getItems();
		Item item;
		listModel = new DefaultListModel();
		Iterator iter = items.iterator();
		
		while(iter.hasNext()){
			item = (Item)iter.next();
			listModel.addElement(item.getName()+" "+item.getDesc()+" - "+item.getValue()+" Coins");
		}
		
        //Liste mit Scrolling.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setViewportView(list);
        
		JButton btnNewButton = new JButton(buyString);
		add(listScrollPane);
		add(btnNewButton);
		this.setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
