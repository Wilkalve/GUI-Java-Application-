package game_network;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class GameServer {
	private static GameServer instance;
    private JFrame frame;
    private JTextField nameField;
    private JComboBox<Integer> playerDropdown;
    private JComboBox<Integer> portDropdown;
    private JLabel statusLabel;
    private List<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerSocket serverSocket;
    private JButton hostButton;
    private  JButton cancelButton;
    private int numPlayer;

    //Default constructor
    public GameServer() {
       
    }
    
 /* Returns the singleton instance of GameServer, ensuring only one instance exists.*/
    public static synchronized GameServer getInstance() {
        if (instance == null) {
            instance = new GameServer();
        }
        return instance;
    }

  /* Initializes and displays the server connection GUI, setting up input fields and buttons. */
    private void initializeGUI() {
    	if (frame != null && frame.isVisible()) {
            frame.toFront(); 
            return; 
        }
    	
        frame = new JFrame("Server Connection");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 3, 10, 10));

        // Name field
        JLabel nameLabel = new JLabel("Host Name:");
        nameField = new JTextField("HostPlayer", 20);
        
        frame.add(nameLabel);
        frame.add(nameField);
        
        // num player
        JLabel numField = new JLabel("Player number:");
        playerDropdown = new JComboBox<>();
        for (int i = 2; i <= 4; i++) { 
        	playerDropdown.addItem(i);
        }
        frame.add(numField);
        frame.add(playerDropdown);

        // Port dropdown
        JLabel portLabel = new JLabel("Port:");
        portDropdown = new JComboBox<>();
        for (int port = 10000; port <= 65535; port++) { 
            portDropdown.addItem(port);
        }
        
        frame.add(portLabel);
        frame.add(portDropdown);

        // Status label
        JLabel statusTextLabel = new JLabel("Status:");
        statusLabel = new JLabel("Waiting for action...");
        frame.add(statusTextLabel);
        frame.add(statusLabel);

        // Buttons
         hostButton = new JButton("Host");
        cancelButton = new JButton("Cancel");
        frame.add(hostButton);
        frame.add(cancelButton);
       

        frame.pack();
        frame.setVisible(true);
    }

    /*Starts the game server by binding to the specified port, 
     accepting client connections, and handling communication.*/
    private void startServer() {
        String name = nameField.getText();
        int port = (int) portDropdown.getSelectedItem();
        statusLabel.setText("Starting server: " + name + " on port " + port);
        
      

        try {
        	serverSocket = new ServerSocket(port, 50, java.net.InetAddress.getByName("0.0.0.0"));

            statusLabel.setText("Server running on port " + port);
            logDebug("Server started successfully. Listening on port " + port);

            new Thread(() -> {
            	
                while (true) {
                	
                    try {
                        Socket clientSocket = serverSocket.accept();
                        logDebug("Client connected: " + clientSocket.getRemoteSocketAddress());
                        ClientHandler handler = new ClientHandler(clientSocket);
                        clientHandlers.add(handler);
                        new Thread(handler).start();
                    } catch (IOException e) {
                        logDebug("Error accepting connection: " + e.getMessage());
                        return;
                    }
                }
            }).start();
            
        } catch (IOException e) {
            statusLabel.setText("Error starting server: " + e.getMessage());
            logDebug("Error starting server: " + e.getMessage());
        }
    }

    private void logDebug(String message) {
    	SwingUtilities.invokeLater(() -> {
    	    if (statusLabel != null) {
    	        statusLabel.setText(message);
    	    } else {
    	        System.err.println("Error: statusLabel is null.");
    	    }
    	});

        System.out.println(message);
    }

    
    /* Sends a message from the sender to all connected clients in the server.*/
    private void broadcastMessage(String senderName, String message) {
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers) {
                handler.sendMessage(senderName + ": " + message); 
            }
            logDebug("Message broadcasted from " + senderName + ": " + message);
        }
    }

    /*Disconnects a specific client from the server by closing their socket
       and removing them from the list of active client handlers.*/
    private void disconnectClient(String senderName) {
        synchronized (clientHandlers) {
            ClientHandler handlerToRemove = null;
            for (ClientHandler handler : clientHandlers) {
                if (handler.getPlayerName().equals(senderName)) {
                    handlerToRemove = handler;
                    break;
                }
            }

            if (handlerToRemove != null) {
                try {
                    handlerToRemove.socket.close(); 
                    logDebug("Client " + senderName + " disconnected.");
                } catch (IOException e) {
                    logDebug("Error closing socket for " + senderName + ": " + e.getMessage());
                }
                clientHandlers.remove(handlerToRemove); 
                logDebug("Total connected players: " + clientHandlers.size());
            }
        }
    }

    /*Handles individual client connections, receiving messages,
  updating player names, and managing disconnections.*/
    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String playerName = "Unknown"; 

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                this.out = new PrintWriter(socket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                logDebug("New client handler created for: " + socket.getRemoteSocketAddress());
            } catch (IOException e) {
                logDebug("Error initializing client handler: " + e.getMessage());
            }
        }

        public String getPlayerName() {
            return playerName;
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    logDebug("Message received from " + playerName + ": " + message);

                    if (message.startsWith("2#")) { 
                        playerName = message.split("#", 2)[1].trim(); 
                        logDebug("Player name set to: " + playerName);
                    } else {
                        handleProtocol(message, playerName); 
                    }
                }
            } catch (IOException e) {
                logDebug("Connection lost with client: " + socket.getRemoteSocketAddress() + " - " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    logDebug("Error closing socket: " + e.getMessage());
                }
                synchronized (clientHandlers) {
                    clientHandlers.remove(this);
                }
                logDebug("Client disconnected. Total connected players: " + clientHandlers.size());
            }
        }
    }
    
    /*Processes incoming messages based on a predefined protocol,
     handling disconnections, player joins, and message broadcasts*/ 
    private void handleProtocol(String message, String senderName) {
        String[] parts = message.split("#", 2);
        
        
        if (parts.length != 2) {
            logDebug("Invalid protocol format: " + message);
            return;
        }

        int protocolId;
        try {
            protocolId = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            logDebug("Error parsing protocol ID: " + parts[0] + " - " + e.getMessage());
            return;
        }

        String data = parts[1];
        switch (protocolId) {
            case 1: 
                logDebug("Disconnection requested by player: " + senderName);
                disconnectClient(senderName);
                break;
            case 2: 
                logDebug("Player joined: " + senderName);
                break;
            case 3: 
                broadcastMessage(senderName, data);
                break;
            default:
                logDebug("Unknown protocol ID: " + protocolId);
        }
    }

    // Launch and initialite the GUI to connect to the server
    public void launchserver() {
    	 initializeGUI();
    	 
    	 
    	 // host button
    	 hostButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 startServer();
             }
         });
    	 
    	 // Cacel button
    	  cancelButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(cancelButton);
                  currentFrame.dispose();
              }
          });

    	 
    	 SwingUtilities.invokeLater(GameServer::new);
    }
    
    
    public void disconnectServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); 
                logDebug("Server stopped.");
            }

            synchronized (clientHandlers) {
                for (ClientHandler handler : clientHandlers) {
                    try {
                        handler.socket.close(); 
                    } catch (IOException e) {
                        logDebug("Error disconnecting client: " + e.getMessage());
                    }
                }
                clientHandlers.clear(); 
                logDebug("All clients disconnected.");
            }

            frame.dispose(); 
        } catch (IOException e) {
            logDebug("Error shutting down server: " + e.getMessage());
        }
    }

   
    public int getSelectedPlayerNumber() {
    	int playerNumber = 0;
    	if (playerDropdown != null) {
    	     playerNumber = (int) playerDropdown.getSelectedItem();
    	} else {
    		 System.err.println("Error: playerDropdown is null");
    	   return 0;
    	}

    	
    	System.out.println("Number of player: " +  playerNumber);
        return (int) playerDropdown.getSelectedItem(); 
    }

    
    public JButton getHostButton() {
    	 return hostButton;

    }
  
    public JButton getCancelButton() {
   	 return cancelButton;

   }
 
}
