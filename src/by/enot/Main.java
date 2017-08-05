package by.enot;

import javax.swing.*;

/**
 * Created by sintetic on 13.08.2016.
 */
public class Main {
	public static void main(String args[]) {
		Gui mainWindow = new Gui("Таймер выключения");
		mainWindow.setVisible(true);
		mainWindow.pack();
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setSize(300, 120);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setResizable(false);
	}
}
