package com.github.propra13.gruppeA3.Menu;

import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.NPC;
import com.github.propra13.gruppeA3.Entities.Player;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Klasse für: Quest , öffnet ein Fenster in dem die Quest-Belohnung uebergeben wird
 * @author Jenny Lenz
 *
 */
public class Quest extends JFrame 
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
* 			selectString: String, was auf selectButton zu sehen ist
* 			selectButton: Ein graphischer Knopf zum Auswählen von Items aus dem Shop
* 			text: Ein graphisches Textfeld zum Anzeigen von Informationen
*/
private static final long serialVersionUID = 3397250190749280364L;
private LinkedList<Item> items;
private JList<Item> list;
private DefaultListModel<Item> listModel;
private static final String selectString = "Nehmen";
private JButton selectButton;
private JTextField text;


/**
* Der Konstruktor erzeugt ein JFrame
* @param player Das Spieler Objekt
* @param npc Das Quest-NPC Objekt
*/
public Quest(final Player player, final NPC npc) {
setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
setAlwaysOnTop(true);
setResizable(false);
setType(Type.UTILITY);
setTitle(npc.getName()+"s Quest");
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




selectButton = new JButton("Auswählen");
selectButton.setActionCommand(selectString);
selectButton.addActionListener(new ActionListener() {
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
	text.setText("Vielen Dank für deine Hilfe!");
} else {
	text.setText("Du besitzt nicht genug Münzen!");
}

if(listModel.isEmpty()){
	selectButton.setEnabled(false);
} else selectButton.setEnabled(true);
}
});


text = new JTextField(10);
text.setFont(new Font("Arial Black", Font.BOLD, 11));
text.setEditable(false);
text.setEnabled(false);


JPanel buttonPane = new JPanel();
buttonPane.setLayout(new BoxLayout(buttonPane,
                         BoxLayout.LINE_AXIS));
buttonPane.add(selectButton);
buttonPane.add(text);
buttonPane.add(selectButton);
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