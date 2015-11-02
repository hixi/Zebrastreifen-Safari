package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;
import java.util.Date;

/**
 *
 * @author aeugster
 */
public class CreateCrossingGUI extends CreateUpdateGUI {

    public CreateCrossingGUI(MainGUI mainGUI) {
        super(mainGUI, "Erstellen eines neuen Zebrastreifens");
        pack();
    }

    @Override
    protected void onSendClick() {
        try {
            long osmNode = Long.parseLong(osmNodeIdTextField.getText());
            Crossing crossing = mainGUI.getCrossing(osmNode);

            if (crossing == null) {
                crossing = new Crossing(null, osmNode, 1, 1);
                observable.notifyObservers(crossing);
            }

            DataServiceLoader.getCrossingData().addRating(
                    new Rating(null,
                            commentTextArea.getText(),
                            mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                            mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroupe)),
                            mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                            mainGUI.getUser(userComboBox.getSelectedItem().toString()),
                            crossing,
                            imageTextField.getText(),
                            new Date()
                    )
            );

            this.dispose();
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "Der OSM Node muss eine Zahl sein", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
