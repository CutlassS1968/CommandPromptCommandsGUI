import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TerminalCommandsGUI extends JFrame implements ActionListener {

    // Panels to hold separate elements
    private JPanel searchPanel;
    private JPanel tablePanel;

    // Search elements
    private JButton searchButton;
    private JTextField searchField;
    private String input;

    // Table elements
    private JTable table;
    private JScrollPane scrollList;
    private ArrayList<Integer> tableColumnWidths;

    private ListModel commandList;

    public TerminalCommandsGUI() {
        searchPanel = new JPanel();
        tablePanel = new JPanel();
        searchButton = new JButton("Search");
        searchField = new JTextField();
        commandList = new ListModel();
        tableColumnWidths = new ArrayList<Integer>();


        // Resizable table with JFrame
        table = new JTable(commandList)
        {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component returnComp = super.prepareRenderer(renderer, row, col);
                Color alternateColor = new Color(242, 242, 242);
                Color whiteColor = Color.WHITE;
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    Color bg = (row % 2 == 0 ? alternateColor: whiteColor);
                    returnComp.setBackground(bg);
                    bg = null;
                }
                return returnComp;
            }
        };
        scrollList = new JScrollPane(table);

        setupSearchPanel();
        setupTablePanel();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setVisible(true);
        this.pack();
        this.setSize(500,625);

    }

    private void setupSearchPanel(){
        searchButton.addActionListener(this);
        searchField.addActionListener(this);
        searchField.setPreferredSize(new Dimension(325,25));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        this.add(searchPanel, BorderLayout.PAGE_START);
    }

    private void setupTablePanel(){
        autoResizeTable();

//        tablePanel.setBorder(BorderFactory.createTitledBorder("Commands"));

        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        scrollList.setPreferredSize(new Dimension((Integer) table.getPreferredSize().width + 18, 525));

        table.getTableHeader().setReorderingAllowed(false);

        tablePanel.add(scrollList);
        this.add(tablePanel, BorderLayout.CENTER);
    }



    private void autoResizeTable() {
        // Auto resize Column width depending on cell content - https://tips4java.wordpress.com/2008/11/10/table-column-adjuster/
        if (commandList.getDisplay() == ScreenDisplay.DefaultDisplay) {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int col = 0; col < table.getColumnCount(); col++) {
                TableColumn tableColumn = table.getColumnModel().getColumn(col);
                int preferredWidth = tableColumn.getMinWidth();
                int maxWidth = tableColumn.getMaxWidth();

                for (int row = 0; row < table.getRowCount(); row++) {
                    TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
                    Component c = table.prepareRenderer(cellRenderer, row, col);
                    int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                    preferredWidth = Math.max(preferredWidth, width);

                    // We've exceeded the maximum width, no need to check other rows

                    if (preferredWidth >= maxWidth) {
                        preferredWidth = maxWidth;
                        break;
                    }
                }
                tableColumn.setPreferredWidth(preferredWidth);

                // If the columns width have already been set, reset the stored values to the new ones
                if (tableColumnWidths.size() >= commandList.getColumnCount()) tableColumnWidths.clear();

                tableColumnWidths.add(preferredWidth);

            }
        } else {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//            ArrayList<Integer> tempWidth = (ArrayList<Integer>) tableColumnWidths.clone();
            for (int col = 0; col < tableColumnWidths.size(); col++) {
                TableColumn tableColumn = table.getColumnModel().getColumn(col);
                int preferredWidth = tableColumnWidths.get(col);
                tableColumn.setPreferredWidth(preferredWidth);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton || e.getSource() == searchField) {

            // Obtain string
            // If input isn't blank, search list. If it is, set display to default
            if (!searchField.getText().isBlank()) {
                input = searchField.getText();

                commandList.search(input);
                autoResizeTable();

                searchField.setText("");
            } else {
                commandList.setDisplay(ScreenDisplay.DefaultDisplay);
                autoResizeTable();
            }
        }

    }

    public static void main(String[] args) {
        TerminalCommandsGUI main = new TerminalCommandsGUI();
    }
}
