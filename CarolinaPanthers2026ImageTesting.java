import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CarolinaPanthers2026GUITesting extends JFrame {
    private JPanel mainPanel, buttonPanel, itemPanel, detailPanel;
    private JButton button1, button2;
    private JList<String> itemList;
    private DefaultListModel<String> listModel;
    private JLabel imageLabel;

    private String[] players = {"Maxwell Harper", "Landon Rivers", "Troy Jefferson"};
    private String[] coaches = {"Frank Reich", "Josh McCown", "Dan Morgan"};

    private Map<String, String> playerImages = new HashMap<>();
    private Map<String, String> coachImages = new HashMap<>();

    public CarolinaPanthers2026GUITesting() {
        setTitle("NFL Carolina Panthers 2026 Season Info");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Replace these with your actual online image URLs
        playerImages.put("Maxwell Harper", "https://via.placeholder.com/400x400.png?text=Maxwell+Harper");
        playerImages.put("Landon Rivers",  "https://via.placeholder.com/400x400.png?text=Landon+Rivers");
        playerImages.put("Troy Jefferson", "https://legacymedia.sportsplatform.io/image/upload/x_0,y_0,w_1758,h_1173,c_crop/v1678477349/tprn6lw4piddlzhzjvfd.jpg");
        
        coachImages.put("Frank Reich", "https://via.placeholder.com/400x400.png?text=Frank+Reich");
        coachImages.put("Josh McCown", "https://via.placeholder.com/400x400.png?text=Josh+McCown");
        coachImages.put("Dan Morgan",  "https://via.placeholder.com/400x400.png?text=Dan+Morgan");

        mainPanel = new JPanel(new CardLayout());
        buttonPanel = new JPanel(new GridBagLayout()); 
        itemPanel = new JPanel(new BorderLayout());
        detailPanel = new JPanel(new BorderLayout());

        button1 = new JButton("Players");
        button2 = new JButton("Coaches/Staff");
        buttonPanel.add(button1);
        buttonPanel.add(button2);

        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.addListSelectionListener(new ItemSelectionListener());

        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setPreferredSize(new Dimension(180, 0));
        itemPanel.add(scrollPane, BorderLayout.WEST);
        
        imageLabel = new JLabel("Select a name to view photo", SwingConstants.CENTER);
        detailPanel.add(imageLabel, BorderLayout.CENTER);
        itemPanel.add(detailPanel, BorderLayout.CENTER);
        
        JButton backButton = new JButton("Back to Main Menu");
        itemPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(buttonPanel, "Buttons");
        mainPanel.add(itemPanel, "Items");

        add(mainPanel, BorderLayout.CENTER);

        button1.addActionListener(new ButtonClickListener("Players"));
        button2.addActionListener(new ButtonClickListener("Coaches/Staff"));
        backButton.addActionListener(e -> showButtons());
    }

    private void showItemDetail(String selectedItem) {
        String urlString = playersListContains(selectedItem) ? 
                          playerImages.get(selectedItem) : coachImages.get(selectedItem);
        
        if (urlString != null) {
            // Loading text while we fetch the image
            imageLabel.setIcon(null);
            imageLabel.setText("Loading image from web...");

            // Fetching from web in a separate thread to prevent GUI from freezing
            new Thread(() -> {
                try {
                    URL url = new URL(urlString);
                    // Use ImageIO to read the image from the URL
                    BufferedImage img = ImageIO.read(url);
                    
                    if (img != null) {
                        // Resizing logic
                        int maxWidth = 450, maxHeight = 400;
                        double scale = Math.min((double)maxWidth / img.getWidth(), 
                                                (double)maxHeight / img.getHeight());
                        
                        int w = (int)(img.getWidth() * scale);
                        int h = (int)(img.getHeight() * scale);
                        
                        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                        
                        // Update the GUI on the Event Dispatch Thread
                        SwingUtilities.invokeLater(() -> {
                            imageLabel.setIcon(new ImageIcon(scaled));
                            imageLabel.setText(null);
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> imageLabel.setText("Failed to decode image."));
                    }
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> imageLabel.setText("Error: Could not load from URL."));
                }
            }).start();
        }
    }

    private class ButtonClickListener implements ActionListener {
        private String category;
        public ButtonClickListener(String category) { this.category = category; }
        @Override
        public void actionPerformed(ActionEvent e) {
            listModel.clear();
            imageLabel.setIcon(null);
            imageLabel.setText("Select a name");
            String[] items = category.equals("Players") ? players : coaches;
            for (String item : items) listModel.addElement(item);
            showItems();
        }
    }

    private class ItemSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                String selected = itemList.getSelectedValue();
                if (selected != null) showItemDetail(selected);
            }
        }
    }

    private boolean playersListContains(String item) {
        for (String p : players) if (p.equals(item)) return true;
        return false;
    }

    private void showItems() {
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Items");
    }

    private void showButtons() {
        itemList.clearSelection();
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Buttons");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarolinaPanthers2026GUITesting().setVisible(true));
    }
}
