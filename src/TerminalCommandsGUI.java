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

    // Menu items
    private JMenuBar menus;
    private JMenu fileMenu;
    private JMenu actionMenu;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem addItem;

    // Search elements
    private JButton searchButton;
    private JTextField searchField;
    private String input;

    // Table elements
    private JTable table;
    private JScrollPane scrollList;
    private ArrayList<Integer> tableColumnWidths;

    // List of commands
    private ListModel commandList;

    public TerminalCommandsGUI() {
        searchPanel = new JPanel();
        tablePanel = new JPanel();

        searchButton = new JButton("Search");
        searchField = new JTextField();
        commandList = new ListModel();
        tableColumnWidths = new ArrayList<Integer>();

        menus = new JMenuBar();
        fileMenu = new JMenu("File");
        actionMenu = new JMenu("Action");
        openItem = new JMenuItem("Open File");
        saveItem = new JMenuItem("Save File");
        addItem = new JMenuItem("Add Command");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        actionMenu.add(addItem);

        menus.add(fileMenu);
        menus.add(actionMenu);

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        addItem.addActionListener(this);

        setJMenuBar(menus);

        // Allows alternating colors on JTable
        table = new JTable(commandList) {
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

        // Set application look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException e) {
            e.printStackTrace();
        }

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();

        centerFrame();
    }

    private void setupSearchPanel(){
        searchButton.addActionListener(this);
        searchField.addActionListener(this);
        searchField.setPreferredSize(new Dimension(330,25));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        this.add(searchPanel, BorderLayout.PAGE_START);
    }

    private void setupTablePanel(){
        tableColumnWidths.add(67);
        tableColumnWidths.add(325);

        resizeTable();
        table.setShowHorizontalLines(false);
//        table.setShowVerticalLines(false);
        scrollList.setPreferredSize(new Dimension((Integer) table.getPreferredSize().width + 18, 525));
        scrollList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);

        // Add a slight buffer to edges
        tablePanel.setPreferredSize(new Dimension(450, 550));
        tablePanel.add(scrollList);
        this.add(tablePanel, BorderLayout.CENTER);
    }

    private void centerFrame() {
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        setLocation(dx, dy);
    }



    private void resizeTable() {
//        // Auto resize Column width depending on cell content - https://tips4java.wordpress.com/2008/11/10/table-column-adjuster/
//        if (commandList.getDisplay() == ScreenDisplay.DefaultDisplay) {
//            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//            for (int col = 0; col < table.getColumnCount(); col++) {
//                TableColumn tableColumn = table.getColumnModel().getColumn(col);
//                int preferredWidth = tableColumn.getMinWidth();
//                int maxWidth = tableColumn.getMaxWidth();
//
//                for (int row = 0; row < table.getRowCount(); row++) {
//                    TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
//                    Component c = table.prepareRenderer(cellRenderer, row, col);
//                    int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
//                    preferredWidth = Math.max(preferredWidth, width);
//
//                    // We've exceeded the maximum width, no need to check other rows
//
//                    if (preferredWidth >= maxWidth) {
//                        preferredWidth = maxWidth;
//                        break;
//                    }
//                }
//                tableColumn.setPreferredWidth(preferredWidth);
//
//                // If the columns width have already been set, reset the stored values to the new ones
//                if (tableColumnWidths.size() >= commandList.getColumnCount()) tableColumnWidths.clear();
//
//                tableColumnWidths.add(preferredWidth);
//
//            }
//        } else {
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            for (int col = 0; col < tableColumnWidths.size(); col++) {
                TableColumn tableColumn = table.getColumnModel().getColumn(col);
                int preferredWidth = tableColumnWidths.get(col);
                tableColumn.setPreferredWidth(preferredWidth);
            }
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton || e.getSource() == searchField) {

            // Obtain string
            // If input isn't blank, search list. If it is, set display to default
            if (!searchField.getText().isBlank()) {
                input = searchField.getText();

                commandList.search(input);
                resizeTable();

                searchField.setText("");
            } else {
                commandList.setDisplay(ScreenDisplay.DefaultDisplay);
                resizeTable();
            }
        }

        if (e.getSource() == openItem) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                commandList.loadFile(filename);
            }
            resizeTable();
        }
        if (e.getSource() == saveItem) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showSaveDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                commandList.saveFile(filename);
            }
        }
        if (e.getSource() == addItem) {
            Command cmd = new Command();
            NewCommandDialog dia = new NewCommandDialog(this, cmd);
            if (dia.getCloseStatus() == NewCommandDialog.OK) {
                commandList.add(cmd);
            }
            resizeTable();
        }

    }

    public static void main(String[] args) {
        TerminalCommandsGUI main = new TerminalCommandsGUI();
    }
}