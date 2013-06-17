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
import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;

public class Shop extends JFrame 
				  implements ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7205764559861185949L;
	private LinkedList<Item> items;
	private JList list;
	private DefaultListModel listModel;
    private static final String buyString = "Kaufen";
    private static final String sellString = "Verkaufen";
    private JButton buyButton;
	private JButton sellButton;
    private JTextField text;
	

	/**
	 * Create the panel.
	 */
	public Shop(Player player, NPC npc) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
		setType(Type.UTILITY);
		setTitle(npc.getName()+"s Shop");
		setSize(450,300);
		setLocationRelativeTo(null);
		//setBounds(100, 100, 450, 300);
		
		items = npc.getItems();
		Item item;
		listModel = new DefaultListModel();
		Iterator iter = items.iterator();
		
		while(iter.hasNext()){
			item = (Item)iter.next();
			listModel.addElement(item.getName()+"       "+item.getDesc()+"   -    "+item.getValue()+" Coins");
		}
		
        //Liste mit Scrolling.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
 
        sellButton = new JButton("Verkaufen");
        sellButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });

        sellButton.setActionCommand(sellString);
        sellButton.setEnabled(false);
 
        buyButton = new JButton("Kaufen");
        buyButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
 
        buyButton.setActionCommand(buyString);
 
        text = new JTextField(10);

 
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
