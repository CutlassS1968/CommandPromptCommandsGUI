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

    private String name;
    private String description;

    public NewCommandDialog() {
        panel = new JPanel();
        namePanel = new JPanel();
        descriptionPanel = new JPanel();
        nameLabel = new JLabel("Command:");
        descriptionLabel = new JLabel("Description:");
        nameField = new JTextField();
        descriptionField = new JTextField();
        button = new JButton("Ok");
        button.addActionListener(this);

        namePanel.add(nameLabel);
        namePanel.add(nameField);

        descriptionPanel.add(descriptionLabel);
        descriptionPanel.add(descriptionField);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(namePanel);
        panel.add(descriptionPanel);

        this.add(panel);

        setTitle("New Command");
        this.setVisible(true);
        this.setPreferredSize(new Dimension(300, 200));

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            if (!nameField.getText().isBlank() && !descriptionField.getText().isBlank()) {
                name = nameField.getText();
                description = descriptionField.getText();
            }
        }

        dispose();
    }
}
