package game_network;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private JLabel statusLabel;
	private static GameClient instance;
    private static boolean isSetupDialogShown = false; 

    public GameClient(String serverAddress, int port, String playerName) throws IOException {
        try {
            this.socket = new Socket(serverAddress, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            logDebug("Connected to server at " + serverAddress + " on port " + port);

            out.println("2#" + playerName);
            logDebug("Player name sent to server: " + playerName);
        } catch (IOException e) {
            logDebug("Error connecting to server: " + e.getMessage());
            throw new IOException("Unable to connect to server. Error: " + e.getMessage());
        }
    }
    
    // Default constructor
    public GameClient() {
    	
    }
    
    public static synchronized GameClient getInstance() {
		if (instance == null) {
			instance = new GameClient();
		}
		return instance;
	}

    private void initializeGUI(String playerName) {
    	if (frame != null && frame.isVisible()) {
            frame.toFront(); 
            return; 
        }
    	
        frame = new JFrame("Chat Client - Player: " + playerName);
        messageArea = new JTextArea(16, 25);
        messageArea.setEditable(false);
        inputField = new JTextField(35);
        statusLabel = new JLabel("Connected as: " + playerName);

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.add(inputField, BorderLayout.SOUTH);
        frame.add(statusLabel, BorderLayout.NORTH);
        frame.pack();

        // Position frame bottom-right
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - frame.getWidth();
        int y = screenSize.height - frame.getHeight();
        frame.setLocation(x, y);

        inputField.addActionListener(e -> {
            String message = inputField.getText();
            if (!message.trim().isEmpty()) {
                out.println("3#" + message);
                inputField.setText("");
                logDebug("Message sent to server: " + message);
            } else {
                logDebug("Empty message ignored.");
            }
        });

        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    messageArea.append(message + "\n");
                    logDebug("Message received from server: " + message);
                }
            } catch (IOException e) {
                logDebug("Connection to server lost: " + e.getMessage());
                messageArea.append("Connection to server lost.\n");
            }
        }).start();

        frame.setVisible(true);  
    }



    public void launchClient() {
        if (isSetupDialogShown) {
            logDebug("Client setup dialog has already been shown.");
            return; 
        }

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField playerNameField = new JTextField("DefaultPlayer", 20);
        JTextField serverAddressField = new JTextField("localhost", 20);
        JTextField portField = new JTextField("10000", 20);
        JLabel statusLabel = new JLabel("Enter details and click Connect.");

        panel.add(new JLabel("Player Name:"));
        panel.add(playerNameField);
        panel.add(new JLabel("IP Address:"));
        panel.add(serverAddressField);
        panel.add(new JLabel("Port:"));
        panel.add(portField);
        panel.add(new JLabel("Status:"));
        panel.add(statusLabel);

        int option = JOptionPane.showConfirmDialog(null, panel, "Client Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            isSetupDialogShown = true; 

            String playerName = playerNameField.getText();
            String serverAddress = serverAddressField.getText();
            int port;
            try {
                port = Integer.parseInt(portField.getText());
                if (port < 10000 || port > 65535) {
                    throw new NumberFormatException("Port out of valid range.");
                }

                try {
                    GameClient client = new GameClient(serverAddress, port, playerName.isEmpty() ? "DefaultPlayer" : playerName);
                    client.initializeGUI(playerName.isEmpty() ? "DefaultPlayer" : playerName);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Failed to connect: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    logDebug("Failed to connect to server: " + e.getMessage());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid port. Please use a value between 10000 and 65535.", "Error", JOptionPane.ERROR_MESSAGE);
                logDebug("Invalid port entered: " + portField.getText());
            }
        } else {
            logDebug("Client setup canceled by user.");
        }
    }

    
    private void disconnectFromServer() {
        try {
            if (out != null) {
                out.println("1#DISCONNECT"); 
                logDebug("Disconnect request sent to server.");
            }

            if (socket != null && !socket.isClosed()) {
                socket.close(); 
                logDebug("Disconnected from server.");
            }

            frame.dispose(); 
        } catch (IOException e) {
            logDebug("Error disconnecting: " + e.getMessage());
        }
    }

    private static void logDebug(String message) {
        System.out.println(message);
    }
}
