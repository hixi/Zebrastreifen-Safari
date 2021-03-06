package ch.hsr.ifs.zebrastreifensafari.view.screen.modify;

import ch.hsr.ifs.zebrastreifensafari.controller.modify.ModifyController;
import ch.hsr.ifs.zebrastreifensafari.exception.InvalidTimeException;
import ch.hsr.ifs.zebrastreifensafari.service.Properties;
import ch.hsr.ifs.zebrastreifensafari.view.adapter.DocumentAdapter;
import ch.hsr.ifs.zebrastreifensafari.view.screen.MainGUI;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jdesktop.swingx.JXDatePicker;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public abstract class ModifyGUI extends JDialog {

    private final ModifyController controller;
    protected JButton sendButton;
    protected JTextField osmNodeIdTextField;
    protected JTextField imageTextField;
    protected JTextField creationTime;
    protected JTextArea commentTextArea;
    protected JRadioButton spatialClarityGoodRadioButton;
    protected JRadioButton spatialClarityOkRadioButton;
    protected JRadioButton spatialClarityBadRadioButton;
    protected JRadioButton trafficFewRadioButton;
    protected JRadioButton trafficLittleRadioButton;
    protected JRadioButton trafficALotRadioButton;
    protected JRadioButton illuminationGoodRadioButton;
    protected JRadioButton illuminationOkRadioButton;
    protected JRadioButton illuminationBadRadioButton;
    protected JRadioButton illuminationNoneRadioButton;
    protected JComboBox<String> userComboBox;
    protected JLabel osmNodeIdLabel;
    protected JLabel userLabel;
    protected JLabel spatialClarityLabel;
    protected JLabel illuminationLabel;
    protected JLabel trafficLabel;
    protected JLabel commentLabel;
    protected JLabel imageLabel;
    protected JLabel imageField;
    protected JLabel creationDateLabel;
    protected JLabel creationTimeLabel;
    protected JScrollPane commentScrollPane;
    protected ButtonGroup spatialClarityButtonGroup;
    protected ButtonGroup illuminationButtonGroup;
    protected ButtonGroup trafficButtonGroup;
    protected JXDatePicker creationDate;
    private JPanel mainPanel;
    private JButton cancelButton;

    protected ModifyGUI(ModifyController controller, MainGUI parent, String title) {
        super(parent, title, true);
        this.controller = controller;
        $$$setupUI$$$();
        setContentPane(mainPanel);
        getRootPane().setDefaultButton(sendButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 600);
        setLocationRelativeTo(parent);
        initListeners();
    }

    private void initListeners() {
        sendButton.addActionListener(e -> {
            if (checkValues()) {
                onSendClick();
            }
        });
        cancelButton.addActionListener(e -> dispose());
        imageTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                setImage(imageTextField.getText());
            }
        });
        commentTextArea.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (commentTextArea.getDocument().getLength() > 500) {
                    SwingUtilities.invokeLater(() -> {
                        commentTextArea.setText(commentTextArea.getText().substring(0, 500));
                        errorMessage(Properties.get("commentLengthError"));
                    });
                }
            }
        });
        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    protected abstract void onSendClick();

    protected int getSelectedButton(ButtonGroup bg) {
        Enumeration<AbstractButton> buttons = bg.getElements();

        for (int i = 1; buttons.hasMoreElements(); i++) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return i;
            }
        }

        return 0;
    }

    protected boolean checkValues() {
        if (spatialClarityButtonGroup.getSelection() == null || illuminationButtonGroup.getSelection() == null || trafficButtonGroup.getSelection() == null || osmNodeIdTextField.getText() == null) {
            errorMessage(Properties.get("missingInputError"));
            return false;
        }

        return true;
    }

    protected void setImage(String mapillaryKey) {
        try {
            imageField.setIcon(new ImageIcon(ImageIO.read(new URL(Properties.get("mapillaryLink") + mapillaryKey + Properties.get("mapillaryImageSize")))));
            imageField.setText(null);
        } catch (IOException ioex) {
            imageField.setIcon(null);
            imageField.setText(Properties.get("noImageFound"));
        }
    }

    protected Date getCreationTime() throws InvalidTimeException {
        return controller.getCreationTime(creationDate.getDate(), creationTime.getText());
    }

    protected void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, Properties.get("error"), JOptionPane.ERROR_MESSAGE);
    }

    //<editor-fold desc="GUI Builder">
    private void createUIComboBox() {
        userComboBox = new JComboBox<>(controller.getUsernames());
    }

    private void createUIRadioButtons() {
        spatialClarityButtonGroup = new ButtonGroup();
        illuminationButtonGroup = new ButtonGroup();
        trafficButtonGroup = new ButtonGroup();
        spatialClarityGoodRadioButton = new JRadioButton();
        spatialClarityOkRadioButton = new JRadioButton();
        spatialClarityBadRadioButton = new JRadioButton();
        illuminationGoodRadioButton = new JRadioButton();
        illuminationOkRadioButton = new JRadioButton();
        illuminationBadRadioButton = new JRadioButton();
        illuminationNoneRadioButton = new JRadioButton();
        trafficFewRadioButton = new JRadioButton();
        trafficLittleRadioButton = new JRadioButton();
        trafficALotRadioButton = new JRadioButton();
        spatialClarityButtonGroup.add(spatialClarityGoodRadioButton);
        spatialClarityButtonGroup.add(spatialClarityOkRadioButton);
        spatialClarityButtonGroup.add(spatialClarityBadRadioButton);
        illuminationButtonGroup.add(illuminationGoodRadioButton);
        illuminationButtonGroup.add(illuminationOkRadioButton);
        illuminationButtonGroup.add(illuminationBadRadioButton);
        illuminationButtonGroup.add(illuminationNoneRadioButton);
        trafficButtonGroup.add(trafficFewRadioButton);
        trafficButtonGroup.add(trafficLittleRadioButton);
        trafficButtonGroup.add(trafficALotRadioButton);
    }

    private void createUIComponents() {
        createUIComboBox();
        createUIRadioButtons();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(mainPanel.getFont().getName(), mainPanel.getFont().getStyle(), mainPanel.getFont().getSize())));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        mainPanel.add(panel1, BorderLayout.SOUTH);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0), null));
        sendButton = new JButton();
        this.$$$loadButtonText$$$(sendButton, ResourceBundle.getBundle("Bundle").getString("create"));
        panel1.add(sendButton, BorderLayout.WEST);
        cancelButton = new JButton();
        this.$$$loadButtonText$$$(cancelButton, ResourceBundle.getBundle("Bundle").getString("cancel"));
        panel1.add(cancelButton, BorderLayout.EAST);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(11, 6, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, BorderLayout.CENTER);
        osmNodeIdLabel = new JLabel();
        this.$$$loadLabelText$$$(osmNodeIdLabel, ResourceBundle.getBundle("Bundle").getString("osmNodeId"));
        panel2.add(osmNodeIdLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userLabel = new JLabel();
        this.$$$loadLabelText$$$(userLabel, ResourceBundle.getBundle("Bundle").getString("user"));
        panel2.add(userLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spatialClarityLabel = new JLabel();
        this.$$$loadLabelText$$$(spatialClarityLabel, ResourceBundle.getBundle("Bundle").getString("spacialClarity"));
        panel2.add(spatialClarityLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        illuminationLabel = new JLabel();
        this.$$$loadLabelText$$$(illuminationLabel, ResourceBundle.getBundle("Bundle").getString("illumination"));
        panel2.add(illuminationLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trafficLabel = new JLabel();
        this.$$$loadLabelText$$$(trafficLabel, ResourceBundle.getBundle("Bundle").getString("traffic"));
        panel2.add(trafficLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        osmNodeIdTextField = new JTextField();
        panel2.add(osmNodeIdTextField, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        panel2.add(userComboBox, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commentLabel = new JLabel();
        this.$$$loadLabelText$$$(commentLabel, ResourceBundle.getBundle("Bundle").getString("comment"));
        panel2.add(commentLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageLabel = new JLabel();
        this.$$$loadLabelText$$$(imageLabel, ResourceBundle.getBundle("Bundle").getString("imageId"));
        panel2.add(imageLabel, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageTextField = new JTextField();
        panel2.add(imageTextField, new GridConstraints(8, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        this.$$$loadButtonText$$$(spatialClarityGoodRadioButton, ResourceBundle.getBundle("Bundle").getString("good"));
        panel2.add(spatialClarityGoodRadioButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trafficFewRadioButton.setSelected(false);
        this.$$$loadButtonText$$$(trafficFewRadioButton, ResourceBundle.getBundle("Bundle").getString("few"));
        panel2.add(trafficFewRadioButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(6, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        this.$$$loadButtonText$$$(illuminationGoodRadioButton, ResourceBundle.getBundle("Bundle").getString("good"));
        panel2.add(illuminationGoodRadioButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commentScrollPane = new JScrollPane();
        panel2.add(commentScrollPane, new GridConstraints(5, 1, 3, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        commentTextArea = new JTextArea();
        commentTextArea.setLineWrap(true);
        commentTextArea.setMargin(new Insets(2, 2, 2, 2));
        commentTextArea.setWrapStyleWord(true);
        commentScrollPane.setViewportView(commentTextArea);
        imageField = new JLabel();
        this.$$$loadLabelText$$$(imageField, ResourceBundle.getBundle("Bundle").getString("noImageFound"));
        panel2.add(imageField, new GridConstraints(9, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        creationDateLabel = new JLabel();
        creationDateLabel.setRequestFocusEnabled(true);
        this.$$$loadLabelText$$$(creationDateLabel, ResourceBundle.getBundle("Bundle").getString("creationDate"));
        panel2.add(creationDateLabel, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        creationDate = new JXDatePicker();
        panel2.add(creationDate, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        creationTimeLabel = new JLabel();
        this.$$$loadLabelText$$$(creationTimeLabel, ResourceBundle.getBundle("Bundle").getString("time"));
        panel2.add(creationTimeLabel, new GridConstraints(10, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(10, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        this.$$$loadButtonText$$$(illuminationOkRadioButton, ResourceBundle.getBundle("Bundle").getString("medium"));
        panel2.add(illuminationOkRadioButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.$$$loadButtonText$$$(spatialClarityOkRadioButton, ResourceBundle.getBundle("Bundle").getString("medium"));
        panel2.add(spatialClarityOkRadioButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.$$$loadButtonText$$$(trafficLittleRadioButton, ResourceBundle.getBundle("Bundle").getString("medium"));
        panel2.add(trafficLittleRadioButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.$$$loadButtonText$$$(trafficALotRadioButton, ResourceBundle.getBundle("Bundle").getString("aLot"));
        panel2.add(trafficALotRadioButton, new GridConstraints(4, 3, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.$$$loadButtonText$$$(illuminationBadRadioButton, ResourceBundle.getBundle("Bundle").getString("bad"));
        panel2.add(illuminationBadRadioButton, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.$$$loadButtonText$$$(spatialClarityBadRadioButton, ResourceBundle.getBundle("Bundle").getString("bad"));
        panel2.add(spatialClarityBadRadioButton, new GridConstraints(2, 3, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        this.$$$loadButtonText$$$(illuminationNoneRadioButton, ResourceBundle.getBundle("Bundle").getString("none"));
        panel2.add(illuminationNoneRadioButton, new GridConstraints(3, 4, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        creationTime = new JTextField();
        panel2.add(creationTime, new GridConstraints(10, 4, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) {
                    break;
                }
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) {
                    break;
                }
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    //</editor-fold>
}
