/**
 * Copyright @ 2016 Quan Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.vietocr;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import net.sourceforge.vietpad.inputmethod.InputMethods;
import net.sourceforge.vietpad.inputmethod.VietKeyListener;

public class MenuSettingsController implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem miOptions;
    @FXML
    private MenuItem miDownloadLangData;
    @FXML
    private Menu menuPSM;
    @FXML
    private Menu menuIM;

    private final String strPSM = "PageSegMode";
    private final String strInputMethod = "inputMethod";
    protected String selectedPSM = "3"; // 3 - Fully automatic page segmentation, but no OSD (default)
    private String selectedInputMethod;
    static final Preferences prefs = Preferences.userRoot().node("/net/sourceforge/vietocr3");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedPSM = prefs.get(strPSM, "3");
//        Label labelPSMValue = (Label) menuBar.getScene().lookup("#labelPSMValue");
//        labelPSMValue.setText(enumOf(selectedPSM));

        // build PageSegMode submenu
        ToggleGroup groupPSM = new ToggleGroup();
        for (PageSegMode mode : PageSegMode.values()) {
            RadioMenuItem radioItem = new RadioMenuItem(mode.getDesc());
            radioItem.setUserData(mode.getVal());
            radioItem.setSelected(mode.getVal().equals(selectedPSM));
            radioItem.setToggleGroup(groupPSM);
            menuPSM.getItems().add(radioItem);
        }

        groupPSM.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
                if (newToggle != null) {
                    selectedPSM = newToggle.getUserData().toString();
                    Label labelPSMValue = (Label) menuBar.getScene().lookup("#labelPSMValue");
                    labelPSMValue.setText(enumOf(selectedPSM));
                }
            }
        });

        selectedInputMethod = prefs.get(strInputMethod, "Telex");

        // build Input Method submenu
        ToggleGroup groupIM = new ToggleGroup();
        for (InputMethods im : InputMethods.values()) {
            String inputMethod = im.name();
            RadioMenuItem radioItem = new RadioMenuItem(inputMethod);
            radioItem.setSelected(inputMethod.equals(selectedInputMethod));
            radioItem.setToggleGroup(groupIM);
            menuIM.getItems().add(radioItem);
        }

        groupIM.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {
                if (newToggle != null) {
                    selectedInputMethod = newToggle.getUserData().toString();
                    VietKeyListener.setInputMethod(InputMethods.valueOf(selectedInputMethod));
                }
            }
        });
    }

    void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
//        TextArea textarea = (TextArea) menuBar.getScene().lookup("#textarea");
//        new VietKeyListener(textarea);
//        VietKeyListener.setInputMethod(InputMethods.valueOf(selectedInputMethod));
//        VietKeyListener.setSmartMark(true);
//        VietKeyListener.consumeRepeatKey(true);
////        boolean vie = curLangCode.startsWith("vie");
////        VietKeyListener.setVietModeEnabled(vie);
    }

    @FXML
    private void handleAction(ActionEvent event) {
        if (event.getSource() == miDownloadLangData) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DownloadDialog.fxml"));
                Parent root = fxmlLoader.load();
                DownloadDialogController controller = fxmlLoader.getController();
                controller.setLookupISO639(GuiWithOCR.getInstance().lookupISO639);
                controller.setLookupISO_3_1_Codes(GuiWithOCR.getInstance().lookupISO_3_1_Codes);
                controller.setInstalledLanguages(GuiWithOCR.getInstance().installedLanguages);
                controller.setTessdataDir(new File(GuiWithOCR.getInstance().datapath, GuiWithOCR.TESSDATA));
                Stage downloadDialog = new Stage();
                downloadDialog.setOnShowing((WindowEvent e) -> {
                    controller.loadListView();
                });
                downloadDialog.setResizable(false);
                downloadDialog.initStyle(StageStyle.UTILITY);
                downloadDialog.setAlwaysOnTop(true);
//            downloadDialog.setX(prefs.getDouble(strChangeCaseX, changeCaseDialog.getX()));
//            downloadDialog.setY(prefs.getDouble(strChangeCaseY, changeCaseDialog.getY()));
                Scene scene1 = new Scene(root);
                downloadDialog.setScene(scene1);
                downloadDialog.setTitle("Download Language Pack");
                downloadDialog.toFront();
                downloadDialog.show();
            } catch (Exception e) {

            }
        } else if (event.getSource() == miOptions) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OptionsDialog.fxml"));
                Parent root = fxmlLoader.load();
                OptionsDialogController controller = fxmlLoader.getController();
                Stage optionsDialog = new Stage();
                optionsDialog.setResizable(false);
                optionsDialog.initStyle(StageStyle.UTILITY);
                optionsDialog.setAlwaysOnTop(true);
//            optionsDialog.setX(prefs.getDouble(strChangeCaseX, changeCaseDialog.getX()));
//            optionsDialog.setY(prefs.getDouble(strChangeCaseY, changeCaseDialog.getY()));
                Scene scene1 = new Scene(root);
                optionsDialog.setScene(scene1);
                optionsDialog.setTitle("Options");
                optionsDialog.toFront();
                optionsDialog.show();
            } catch (Exception e) {

            }
        }
    }

    /**
     * Gets accessible name of PageSegMode enum.
     *
     * @param val
     * @return
     */
    final String enumOf(String val) {
        return PageSegMode.enumOf(val).name().replace("PSM_", "").replace("_", " ");
    }

    void savePrefs() {
        prefs.put(strPSM, selectedPSM);
    }
}
