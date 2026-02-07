import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CarolinaPanthers2026GUITesting extends JFrame {
    private JPanel mainPanel, buttonPanel, itemPanel, detailPanel;
    private JButton button1, button2;
    private JList<String> itemList;
    private DefaultListModel<String> listModel;
    private JLabel imageLabel;

    private String[] players = {
        "Maxwell Harper", "Landon Rivers", "Troy Jefferson"
    };

    private String[] coaches = {
        "Frank Reich", "Josh McCown", "Dan Morgan"
    };

    // Map player names to their image paths
    private java.util.Map<String, String> playerImages = new java.util.HashMap<>();
    
    // Map coach names to their image paths
    private java.util.Map<String, String> coachImages = new java.util.HashMap<>();

    public CarolinaPanthers2026GUITesting() {
        setTitle("NFL Carolina Panthers 2026 Season Info");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize player images (add your image paths here)
        playerImages.put("Maxwell Harper", "resources/player1.jpg");
        playerImages.put("Landon Rivers", "resources/player2.jpg");
        playerImages.put("Troy Jefferson", "resources/troy.png");
        
        // Initialize coach images (add your image paths here)
        coachImages.put("Frank Reich", "resources/osk.jpg");
        coachImages.put("Josh McCown", "resources/oip.png");
        coachImages.put("Dan Morgan", "resources/morgan.jpg");

        mainPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel();
        itemPanel = new JPanel(new BorderLayout());
        detailPanel = new JPanel(new BorderLayout());

        button1 = new JButton("Players");
        button2 = new JButton("Coaches/Staff");

        buttonPanel.add(button1);
        buttonPanel.add(button2);

        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setVisibleRowCount(10);

        itemList.addListSelectionListener(new ItemSelectionListener());

        JScrollPane scrollPane = new JScrollPane(itemList);
        itemPanel.add(scrollPane, BorderLayout.WEST);
        
        // Detail panel for images and info
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 300));
        detailPanel.add(imageLabel, BorderLayout.CENTER);
        itemPanel.add(detailPanel, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Go Back");
        itemPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(buttonPanel, "Buttons");
        mainPanel.add(itemPanel, "Items");

        add(mainPanel, BorderLayout.CENTER);

        button1.addActionListener(new ButtonClickListener("Players"));
        button2.addActionListener(new ButtonClickListener("Coaches/Staff"));
        backButton.addActionListener(e -> showButtons());
    }

    private class ButtonClickListener implements ActionListener {
        private String itemCategory;

        public ButtonClickListener(String itemCategory) {
            this.itemCategory = itemCategory;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateItemList();
            showItems();
        }

        private void updateItemList() {
            listModel.clear();
            String[] items = null;
            if (itemCategory.equals("Players")) {
                items = players;
            } else if (itemCategory.equals("Coaches/Staff")) {
                items = coaches;
            }

            if (items != null) {
                for (String item : items) {
                    listModel.addElement(item);
                }
            }
        }
    }

    private class ItemSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                String selectedItem = itemList.getSelectedValue();
                if (selectedItem != null) {
                    showItemDetail(selectedItem);
                }
            }
        }
    }

    private void showItemDetail(String selectedItem) {
        String message = "Details about " + selectedItem;
        String title = playersListContains(selectedItem) ? "Players Details" : "Coaches/Staff Details";

        // Load and display image if it's a player or coach
        String imagePath = null;
        if (playersListContains(selectedItem) && playerImages.containsKey(selectedItem)) {
            imagePath = playerImages.get(selectedItem);
        } else if (coachesListContains(selectedItem) && coachImages.containsKey(selectedItem)) {
            imagePath = coachImages.get(selectedItem);
        }
        
        if (imagePath != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                if (imageIcon.getIconWidth() > 0) {
                    // Scale image to fit label, maintaining aspect ratio
                    int maxWidth = 280;
                    int maxHeight = 350;
                    int origWidth = imageIcon.getIconWidth();
                    int origHeight = imageIcon.getIconHeight();
                    
                    double scale = Math.min((double)maxWidth / origWidth, (double)maxHeight / origHeight);
                    int newWidth = (int)(origWidth * scale);
                    int newHeight = (int)(origHeight * scale);
                    
                    Image scaledImage = imageIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                    imageLabel.setText(null);
                } else {
                    imageLabel.setText("Image not found:\n" + imagePath);
                    imageLabel.setIcon(null);
                }
            } catch (Exception e) {
                imageLabel.setText("No image available");
                imageLabel.setIcon(null);
            }
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("No image for: " + selectedItem);
        }
    }

    private boolean playersListContains(String selectedItem) {
        for (String player : players) {
            if (player.equals(selectedItem)) {
                return true;
            }
        }
        return false;
    }

    private boolean coachesListContains(String selectedItem) {
        for (String coach : coaches) {
            if (coach.equals(selectedItem)) {
                return true;
            }
        }
        return false;
    }

    private void showItems() {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, "Items");
    }

    private void showButtons() {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, "Buttons");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarolinaPanthers2026GUITesting viewer = new CarolinaPanthers2026GUITesting();
            viewer.setVisible(true);
        });
    }
}
