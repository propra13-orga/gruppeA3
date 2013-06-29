/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import javax.swing.JFrame;

import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.Menu.MenuStart.NetworkStatus;

/**
 * @author Majida Dere
 *
 */
public class Client extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8970545556976842124L;
	private Protocol protocol=null;
	private MenuStart gui;

	/**
	 * 
	 */
	public Client(MenuStart gui, NetworkStatus netstat) {
		this.gui = gui;
		System.out.println("Client " + netstat.toString() + " started");
		// TODO Auto-generated constructor stub
	}

}
