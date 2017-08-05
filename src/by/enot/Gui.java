package by.enot;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sintetic on 13.08.2016.
 */
public class Gui extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JTextField userInputTime;
	private JButton confirmButton, cancelButton;
	private JLabel mainLabel;
	private long shutdownDelay;
	private ButtonListener buttonListener;
	private JProgressBar waitingForShutdownBar;

	public Gui(String name) {
		super(name);
		setLayout(null);
		this.setIconImage(
				new ImageIcon("/zzz-sleep-symbol.png").getImage());
		userInputTime = new JTextField(10);
		userInputTime.setDocument(new PlainDocument() {
			private static final int INPUT_CHAR_NUMBER = 3;
			
			public void insertString(int offset, String value, AttributeSet attributes) throws BadLocationException {
				int currLength = this.getContent().length() - 1;
				int max = INPUT_CHAR_NUMBER - currLength;
				if (value.length() <= max) {
					super.insertString(offset, value, attributes);
				} else {
					super.insertString(offset, value.substring(0, max), attributes);
				}
			}
		});
		userInputTime.setBounds(120, 25, 60, 20);
		add(userInputTime);
		confirmButton = new JButton("Запуск");
		confirmButton.setBounds(105, 50, 90, 30);
		add(confirmButton);
		mainLabel = new JLabel("Введите время в минутах");
		mainLabel.setBounds(76, -3, 148, 30);
		add(mainLabel);
		buttonListener = new ButtonListener();
		confirmButton.addActionListener(buttonListener);
		waitingForShutdownBar = new JProgressBar();
		cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(buttonListener);

	}

	class ButtonListener implements ActionListener {
		private Timer timeGuiUpdateTimer = new Timer();
		private Timer shutdownTimer = new Timer();
		private ShutdownTask shutdown;
		private UpdateGuiTimeTask updateGui;

		public void actionPerformed(ActionEvent e) {
			Long parseUserInput = null;
			if (e.getSource() == confirmButton) {
				try {
					parseUserInput = Long.parseLong(userInputTime.getText());
					if (parseUserInput < 0) throw new IllegalArgumentException();
				} catch (Exception e1) {
					userInputTime.setText("");
					userInputTime.requestFocus();
					JOptionPane.showMessageDialog(null, "Введите верное число");
					return;
				}
				shutdownDelay = parseUserInput * 60000;
				remove(confirmButton);
				remove(userInputTime);
				cancelButton.setBounds(105, 50, 90, 30);
				add(cancelButton);
				waitingForShutdownBar.setIndeterminate(true);
				add(waitingForShutdownBar);
				waitingForShutdownBar.setBounds(90, 25, 120, 15);
				shutdown = new ShutdownTask();
				updateGui = new UpdateGuiTimeTask();
				mainLabel.setBounds(15, -3, 270, 30);
				try {
					shutdownTimer.schedule(shutdown, shutdownDelay);
				} catch (Exception e3) {
					JOptionPane.showMessageDialog(null, "опачки");
				}
				try {
					timeGuiUpdateTimer.scheduleAtFixedRate(updateGui, 0, 1000);
				} catch (Exception e8) {
					JOptionPane.showMessageDialog(null, "приехали");
				}
				repaint();
			}
			if (e.getSource() == cancelButton) {
				shutdown.cancel();
				updateGui.cancel();
				shutdownDelay = 0;
				parseUserInput = null;
				remove(cancelButton);
				remove(waitingForShutdownBar);
				userInputTime.setBounds(120, 25, 60, 20);
				mainLabel.setBounds(76, -3, 148, 30);
				add(confirmButton);
				add(userInputTime);
				userInputTime.setText("");
				userInputTime.requestFocus();
				mainLabel.setText("Введите время в минутах");
				repaint();
			}
		}
	}

	class UpdateGuiTimeTask extends TimerTask {
		private long activationTime = System.currentTimeMillis();

		public void run() {
			int timeLeft = (int) ((shutdownDelay + (activationTime - System.currentTimeMillis())) / 1000);
			mainLabel.setText("Компьютер выключится через " + timeLeft / 60 + " мин " + timeLeft % 60 + " сек");
		}
	}
}
