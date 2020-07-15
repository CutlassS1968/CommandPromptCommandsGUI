import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewCommandDialog extends JDialog implements ActionListener {

    private JPanel panel;
    private JPanel namePanel;
    private JPanel descriptionPanel;

    private JLabel nameLabel;
    private JLabel descriptionLabel;

    private JTextField nameField;
    private JTextField descriptionField;

    private JButton button;

    private Command command;

    private int closeStatus;
    public static final int OK = 0;
    public static final int CANCEL = 1;

    public NewCommandDialog(JFrame parent, Command cmd) {
        super(parent, true);
        command = cmd;
        panel = new JPanel();
        namePanel = new JPanel();
        descriptionPanel = new JPanel();
        nameLabel = new JLabel("Command:");
        descriptionLabel = new JLabel("Description:");
        nameField = new JTextField();
        descriptionField = new JTextField();
        button = new JButton("Ok");
        button.addActionListener(this);
        nameField.setPreferredSize(new Dimension(100,20));
        descriptionField.setPreferredSize(new Dimension(100,20));

        namePanel.add(nameLabel, JPanel.LEFT_ALIGNMENT);
        namePanel.add(nameField, JPanel.CENTER_ALIGNMENT);

        closeStatus = CANCEL;

        descriptionPanel.add(descriptionLabel, JPanel.LEFT_ALIGNMENT);
        descriptionPanel.add(descriptionField, JPanel.CENTER_ALIGNMENT);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalStrut(20));
        panel.add(namePanel);
        panel.add(descriptionPanel);
        panel.add(button, JPanel.BOTTOM_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));

        this.setSize(250, 200);
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setTitle("New Command");
        this.setVisible(true);
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            if (!nameField.getText().isBlank() && !descriptionField.getText().isBlank()) {
                command.setName(nameField.getText());
                command.setDescription(descriptionField.getText());
                closeStatus = OK;
            }
        }
        dispose();
    }

    public int getCloseStatus() {
        return closeStatus;
    }
}