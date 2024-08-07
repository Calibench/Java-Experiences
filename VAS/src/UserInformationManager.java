import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class UserInformationManager {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new UserInformationFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class UserInformationFrame extends JFrame {
    private JTextField usernameField, passwordField, emailField;
    private JSpinner levelSlider;
    private JComboBox<String> rankComboBox;
    private JCheckBox dailyTaskCheckbox;
    private JButton saveButton, loadButton, updateButton, changeAccountButton;
    private JComboBox<String> accountList;
    private JFileChooser fileChooser;
    private File selectedFile;
    private Map<String, UserInformation> userInformationMap;
    private Timer dailyTaskResetTimer;
    private JButton addButton;
    private static final String CONFIG_FILE = "config.properties";

    public UserInformationFrame() {
        setTitle("User Information Manager");
        setSize(600, 400);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(inputPanel, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username:");
        inputPanel.add(usernameLabel);
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        inputPanel.add(passwordLabel);
        passwordField = new JTextField();
        inputPanel.add(passwordField);

        JLabel levelLabel = new JLabel("Level:");
        inputPanel.add(levelLabel);
        SpinnerNumberModel levelSpinnerModel = new SpinnerNumberModel(0, 0, 1000, 1);
        levelSlider = new JSpinner(levelSpinnerModel);
        inputPanel.add(levelSlider);

        JLabel emailLabel = new JLabel("Email:");
        inputPanel.add(emailLabel);
        emailField = new JTextField();
        inputPanel.add(emailField);

        JLabel rankLabel = new JLabel("Rank:");
        inputPanel.add(rankLabel);
        rankComboBox = new JComboBox<>(new String[]{
                "Unrated", "Iron 1", "Iron 2", "Iron 3", "Bronze 1", "Bronze 2", "Bronze 3",
                "Silver 1", "Silver 2", "Silver 3", "Gold 1", "Gold 2", "Gold 3",
                "Platinum 1", "Platinum 2", "Platinum 3", "Diamond 1", "Diamond 2", "Diamond 3",
                "Ascendant 1", "Ascendant 2", "Ascendant 3",
                "Immortal 1", "Immortal 2", "Immortal 3", "Radiant"
        });
        inputPanel.add(rankComboBox);

        JLabel dailyTaskLabel = new JLabel("Daily Task Completed:");
        inputPanel.add(dailyTaskLabel);
        dailyTaskCheckbox = new JCheckBox();
        inputPanel.add(dailyTaskCheckbox);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveData());
        buttonPanel.add(saveButton);

        loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadData());
        buttonPanel.add(loadButton);

        updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateData());
        buttonPanel.add(updateButton);

        /*
        changeAccountButton = new JButton("Change Account");
        changeAccountButton.addActionListener(e -> changeAccount());
        buttonPanel.add(changeAccountButton);
        */
        
        addButton = new JButton("Add Account");
        addButton.addActionListener(e -> addAccount());
        buttonPanel.add(addButton);

        accountList = new JComboBox<>();
        accountList.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                displaySelectedAccount();
            }
        });
        add(accountList, BorderLayout.NORTH);

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        userInformationMap = new LinkedHashMap<>();
        
        dailyTaskResetTimer = new Timer(86400000, e -> dailyTaskCheckbox.setSelected(false));
        dailyTaskResetTimer.setRepeats(true);
        dailyTaskResetTimer.start();
        
        loadLastFile();
    }

    private void saveData() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".csv")) {
                selectedFile = new File(selectedFile.getPath() + ".csv");
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(selectedFile))) {
                for (UserInformation userInfo : userInformationMap.values()) {
                    writer.println(userInfo);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            saveLastFile();
        }
    }
    
    private void saveLastFile() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            Properties config = new Properties();
            config.setProperty("lastFile", selectedFile.getAbsolutePath());
            config.store(fos, null);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving last file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            userInformationMap.clear();
            accountList.removeAllItems();
            try (Scanner scanner = new Scanner(selectedFile)) {
                while (scanner.hasNextLine()) {
                    UserInformation userInfo = UserInformation.fromString(scanner.nextLine());
                    userInformationMap.put(userInfo.getUsername(), userInfo);
                    accountList.addItem(userInfo.getUsername());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            saveLastFile();
        }
    }
    
    private void loadLastFile() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                Properties config = new Properties();
                config.load(fis);

                String lastFilePath = config.getProperty("lastFile");
                if (lastFilePath != null) {
                    File lastFile = new File(lastFilePath);
                    if (lastFile.exists()) {
                        selectedFile = lastFile;
                        loadData();
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading last file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateData() {
        String username = usernameField.getText();
        UserInformation userInfo = new UserInformation(
            username,
            passwordField.getText(),
            (int) levelSlider.getValue(),
            emailField.getText(),
            (String) rankComboBox.getSelectedItem(),
            dailyTaskCheckbox.isSelected()
        );
        userInformationMap.put(username, userInfo);
        if (accountList.getSelectedIndex() == -1) {
            accountList.addItem(username);
        }
    }

    /*
    private void changeAccount() {
        String selectedUsername = (String) accountList.getSelectedItem();
        if (selectedUsername != null) {
            UserInformation userInfo = userInformationMap.get(selectedUsername);
            usernameField.setText(userInfo.getUsername());
            passwordField.setText(userInfo.getPassword());
            levelSlider.setValue(userInfo.getLevel());
            emailField.setText(userInfo.getEmail());
            rankComboBox.setSelectedItem(userInfo.getRank());
            dailyTaskCheckbox.setSelected(userInfo.isDailyTaskCompleted());
        } else {
            JOptionPane.showMessageDialog(this, "No account selected", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    */
    
    private void addAccount() {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userInformationMap.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserInformation userInfo = new UserInformation(
            username,
            passwordField.getText(),
            (int) levelSlider.getValue(),
            emailField.getText(),
            (String) rankComboBox.getSelectedItem(),
            dailyTaskCheckbox.isSelected()
        );
        userInformationMap.put(username, userInfo);
        accountList.addItem(username);
    }
    
    private void displaySelectedAccount() {
        String selectedUsername = (String) accountList.getSelectedItem();
        if (selectedUsername != null) {
            UserInformation userInfo = userInformationMap.get(selectedUsername);
            usernameField.setText(userInfo.getUsername());
            passwordField.setText(userInfo.getPassword());
            levelSlider.setValue(userInfo.getLevel());
            emailField.setText(userInfo.getEmail());
            rankComboBox.setSelectedItem(userInfo.getRank());
            dailyTaskCheckbox.setSelected(userInfo.isDailyTaskCompleted());
        } else {
            JOptionPane.showMessageDialog(this, "No account selected", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

class UserInformation {
    private String username;
    private String password;
    private int level;
    private String email;
    private String rank;
    private boolean dailyTaskCompleted;

    public UserInformation(String username, String password, int level, String email, String rank, boolean dailyTaskCompleted) {
        this.username = username;
        this.password = password;
        this.level = level;
        this.email = email;
        this.rank = rank;
        this.dailyTaskCompleted = dailyTaskCompleted;
    }

    public static UserInformation fromString(String userInfoString) {
        String[] userInfoArray = userInfoString.split(",");
        return new UserInformation(
                userInfoArray[0].trim(),
                userInfoArray[1].trim(),
                Integer.parseInt(userInfoArray[2].trim()),
                userInfoArray[3].trim(),
                userInfoArray[4].trim(),
                Boolean.parseBoolean(userInfoArray[5].trim())
        );
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getLevel() {
        return level;
    }

    public String getEmail() {
        return email;
    }

    public String getRank() {
        return rank;
    }

    public boolean isDailyTaskCompleted() {
        return dailyTaskCompleted;
    }

    @Override
    public String toString() {
        return username + "," + password + "," + level + "," + email + "," + rank + "," + dailyTaskCompleted;
    }
}

