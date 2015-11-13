package com.mgz.AFPEditor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MainMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;
	public static final String ACTIONCOMMAND_FILE_NEW = "ac_file_new";
	public static final String ACTIONCOMMAND_FILE_OPEN = "ac_file_open";
	public static final String ACTIONCOMMAND_FILE_SAVE = "ac_file_save";
	public static final String ACTIONCOMMAND_FILE_SAVE_AS = "ac_file_save_as";
	public static final String ACTIONCOMMAND_FILE_EXIT = "ac_file_exit";
	public static final String ACTIONCOMMAND_EDIT_CUT = "ac_edit_cut";
	public static final String ACTIONCOMMAND_EDIT_COPY = "ac_edit_copy";
	public static final String ACTIONCOMMAND_EDIT_PASTE = "ac_edit_paste";
	public static final String ACTIONCOMMAND_WINDOW_ACTIVATE = "ac_window_activate";
	JMenu fileMenu;
	JMenu editMenu;
	JMenu searchMenu;
	JMenu windowMenu;
	JMenu helpMenu;
	
	
	public MainMenu(ActionListener actionListener){

		
		JMenu fileMenu = new JMenu("File"); fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem menuItem = new JMenuItem("New ...", KeyEvent.VK_N);
		menuItem.setActionCommand(ACTIONCOMMAND_FILE_NEW);
		menuItem.addActionListener(actionListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_N, ActionEvent.ALT_MASK | ActionEvent.SHIFT_MASK));
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Open File ...", KeyEvent.VK_O);
		menuItem.setActionCommand(ACTIONCOMMAND_FILE_OPEN);
		menuItem.addActionListener(actionListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_O, ActionEvent.ALT_MASK | ActionEvent.SHIFT_MASK));
		fileMenu.add(menuItem);

		fileMenu.addSeparator();

		menuItem = new JMenuItem("Save", KeyEvent.VK_S);
		menuItem.setActionCommand(ACTIONCOMMAND_FILE_SAVE);
		menuItem.addActionListener(actionListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(menuItem);

		menuItem = new JMenuItem("Save As ...", KeyEvent.VK_A);
		menuItem.setActionCommand(ACTIONCOMMAND_FILE_SAVE_AS);
		menuItem.addActionListener(actionListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_A, ActionEvent.ALT_MASK | ActionEvent.SHIFT_MASK));
		fileMenu.add(menuItem);

		fileMenu.addSeparator();

		menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		menuItem.setActionCommand(ACTIONCOMMAND_FILE_EXIT);
		menuItem.addActionListener(actionListener);
		fileMenu.add(menuItem);
		
		editMenu = new JMenu("Edit"); editMenu.setMnemonic(KeyEvent.VK_E);

		menuItem = new JMenuItem("Cut", KeyEvent.VK_T);
		menuItem.setActionCommand(ACTIONCOMMAND_EDIT_CUT);
		menuItem.addActionListener(actionListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		editMenu.add(menuItem);
		
		menuItem = new JMenuItem("Copy", KeyEvent.VK_C);
		menuItem.setActionCommand(ACTIONCOMMAND_EDIT_COPY);
		menuItem.addActionListener(actionListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		editMenu.add(menuItem);

		menuItem = new JMenuItem("Paste", KeyEvent.VK_P);
		menuItem.setActionCommand(ACTIONCOMMAND_EDIT_PASTE);
		menuItem.addActionListener(actionListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		editMenu.add(menuItem);

		searchMenu = new JMenu("Search"); searchMenu.setMnemonic(KeyEvent.VK_S);

		windowMenu = new JMenu("Window"); windowMenu.setMnemonic(KeyEvent.VK_W);
		windowMenu.putClientProperty(ButtonGroup.class, new ButtonGroup());
		
		helpMenu = new JMenu("Help"); helpMenu.setMnemonic(KeyEvent.VK_H);
		
		this.add(fileMenu);
		this.add(editMenu);
		this.add(searchMenu);
		this.add(windowMenu);
		this.add(helpMenu);
	}


	public JMenu getFileMenu() {
		return fileMenu;
	}


	public void setFileMenu(JMenu fileMenu) {
		this.fileMenu = fileMenu;
	}


	public JMenu getEditMenu() {
		return editMenu;
	}


	public void setEditMenu(JMenu editMenu) {
		this.editMenu = editMenu;
	}


	public JMenu getSearchMenu() {
		return searchMenu;
	}


	public void setSearchMenu(JMenu searchMenu) {
		this.searchMenu = searchMenu;
	}


	public JMenu getWindowMenu() {
		return windowMenu;
	}


	public void setWindowMenu(JMenu windowMenu) {
		this.windowMenu = windowMenu;
	}


	public JMenu getHelpMenu() {
		return helpMenu;
	}


	public void setHelpMenu(JMenu helpMenu) {
		this.helpMenu = helpMenu;
	}


}
