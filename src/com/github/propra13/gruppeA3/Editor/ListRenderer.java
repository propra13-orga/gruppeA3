package com.github.propra13.gruppeA3.Editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 * ListCellRenderer für JPanels, auf denen JLabels mit Icons und Text sind.
 * @author christian
 *
 */
public class ListRenderer implements ListCellRenderer<JPanel> {

	@Override
	public Component getListCellRendererComponent(JList<? extends JPanel> list,
			JPanel panel, int cellIndex, boolean isSelected, boolean cellHasFocus) {
		
		Component component = (Component) panel;
	    component.setForeground (Color.white);
	    component.setBackground (isSelected ? UIManager.getColor("List.selectionBackground") : Color.white);
	    return component;
	}

}
