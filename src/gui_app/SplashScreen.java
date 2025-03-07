package gui_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {
	private static final long serialVersionUID = 178765678876234L;
	private JProgressBar progressBar;
	private ImageIcon backgroundImage;

	public SplashScreen() {
		backgroundImage = new ImageIcon("bk_1.jpg");

		JPanel contentPane = new JPanel(new BorderLayout()) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1234654345L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
				}
			}
		};
		contentPane.setOpaque(false);

		JLabel splashLabel = new JLabel("Loading, please wait...", JLabel.CENTER);
		splashLabel.setFont(new Font("Arial", Font.BOLD, 20));
		splashLabel.setForeground(Color.WHITE);
		contentPane.add(splashLabel, BorderLayout.CENTER);

		JLabel newLabel = new JLabel("Crazy Eight Game", JLabel.CENTER);
		newLabel.setFont(new Font("Arial", Font.BOLD, 35));
		newLabel.setForeground(Color.WHITE);
		newLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		contentPane.add(newLabel, BorderLayout.NORTH);

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setStringPainted(true);
		contentPane.add(progressBar, BorderLayout.SOUTH);

		setContentPane(contentPane);
		setSize(1538, 808);
		setLocationRelativeTo(null);

	}

	public void setProgress(int value) {
		progressBar.setValue(value);
	}
}
