/**
 * Copyright @ 2016 Quan Nguyen
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sourceforge.vietocr;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class GuiWithImageOps extends GuiWithScan {

    @FXML
    private Button btnPrevPage;
    @FXML
    private Button btnNextPage;
    @FXML
    private Button btnRotateCCW;
    @FXML
    private Button btnRotateCW;
    @FXML
    private Button btnZoomIn;
    @FXML
    private Button btnZoomOut;
    @FXML
    private Button btnActualSize;
    @FXML
    private Button btnFitImage;
    @FXML
    private ChoiceBox cbPageNum;

    private final float ZOOM_FACTOR = 1.25f;

    boolean isFitImageSelected;
    Point curScrollPos;

    private final static Logger logger = Logger.getLogger(GuiWithImageOps.class.getName());

    @FXML
    @Override
    protected void handleAction(ActionEvent event) {
        if (event.getSource() == btnPrevPage) {
            btnPrevPageActionPerformed(event);
        } else if (event.getSource() == btnNextPage) {
            btnNextPageActionPerformed(event);
        } else if (event.getSource() == btnFitImage) {
            btnFitImageActionPerformed(event);
        } else if (event.getSource() == btnActualSize) {
            btnActualSizeActionPerformed(event);
        } else if (event.getSource() == btnRotateCW) {
            btnRotateCWActionPerformed(event);
        } else if (event.getSource() == btnRotateCCW) {
            btnRotateCCWActionPerformed(event);
        } else if (event.getSource() == btnZoomIn) {
            btnZoomInActionPerformed(event);
        } else if (event.getSource() == btnZoomOut) {
            btnZoomOutActionPerformed(event);
        } else {
            super.handleAction(event);
        }
    }

    void btnPrevPageActionPerformed(ActionEvent evt) {
//        canvasImage.deselect();
        imageIndex--;
        if (imageIndex < 0) {
            imageIndex = 0;
        } else {
            this.labelStatus.setText(null);
            progressBar.setVisible(false);
            cbPageNum.getSelectionModel().selectPrevious();
        }
    }

    void btnNextPageActionPerformed(ActionEvent evt) {
//        canvasImage.deselect();
        imageIndex++;
        if (imageIndex > imageTotal - 1) {
            imageIndex = (short) (imageTotal - 1);
        } else {
            this.labelStatus.setText(null);
            progressBar.setVisible(false);
            cbPageNum.getSelectionModel().selectNext();
        }
    }

    /**
     * Fits image to the container while retaining aspect ratio.
     *
     * @param evt
     */
    void btnFitImageActionPerformed(ActionEvent evt) {
        this.btnFitImage.setDisable(true);
        this.btnActualSize.setDisable(false);
        this.btnZoomIn.setDisable(true);
        this.btnZoomOut.setDisable(true);
//        canvasImage.deselect();
//        canvasImage.setSegmentedRegions(null);
//        curScrollPos = this.scrollPaneImage.getViewport().getViewPosition();
//        int w = this.scrollPaneImage.getViewport().getWidth();
//        if (this.scrollPaneImage.getVerticalScrollBar().isVisible()) {
//            w += this.scrollPaneImage.getVerticalScrollBar().getWidth();
//        }
//        int h = this.scrollPaneImage.getViewport().getHeight();
//        if (this.scrollPaneImage.getHorizontalScrollBar().isVisible()) {
//            h += this.scrollPaneImage.getHorizontalScrollBar().getHeight();
//        }
//        Dimension fitSize = fitImagetoContainer(originalW, originalH, w, h);
//        fitImageChange(fitSize.width, fitSize.height);
//        setScale(fitSize.width, fitSize.height);
        isFitImageSelected = true;
    }

    /**
     * Reverts to actual image size.
     *
     * @param evt
     */
    void btnActualSizeActionPerformed(ActionEvent evt) {
        this.btnFitImage.setDisable(false);
        this.btnActualSize.setDisable(true);
        this.btnZoomIn.setDisable(false);
        this.btnZoomOut.setDisable(false);
//        canvasImage.deselect();
//        setSegmentedRegions();
//        fitImageChange(originalW, originalH);
        scaleX = scaleY = 1f;
        isFitImageSelected = false;
    }

    void btnZoomOutActionPerformed(ActionEvent evt) {
//        canvasImage.deselect();
//        canvasImage.setSegmentedRegions(null);
        doChange(false);
        isFitImageSelected = false;
        this.btnActualSize.setDisable(false);
    }

    void btnZoomInActionPerformed(ActionEvent evt) {
//        canvasImage.deselect();
//        canvasImage.setSegmentedRegions(null);
        doChange(true);
        isFitImageSelected = false;
        this.btnActualSize.setDisable(false);
    }

    /**
     * Performs the change on image.
     *
     * @param isZoomIn
     */
    void doChange(final boolean isZoomIn) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                double width = imageView.getImage().getWidth();
                double height = imageView.getImage().getHeight();

                if (isZoomIn) {
//                    image.setScaledSize((int) (width * ZOOM_FACTOR), (int) (height * ZOOM_FACTOR));
                } else {
//                    image.setScaledSize((int) (width / ZOOM_FACTOR), (int) (height / ZOOM_FACTOR));
                }

//                jImageLabel.revalidate();
//                scrollPaneImage.repaint();
                if (isZoomIn) {
                    scaleX /= ZOOM_FACTOR;
                    scaleY /= ZOOM_FACTOR;
                } else {
                    scaleX *= ZOOM_FACTOR;
                    scaleY *= ZOOM_FACTOR;
                }
            }
        });
    }

    /**
     * Rotates left.
     *
     * @param evt
     */
    void btnRotateCCWActionPerformed(ActionEvent evt) {
        rotateImage(270d);
//        clearStack();
    }

    /**
     * Rotates right.
     *
     * @param evt
     */
    void btnRotateCWActionPerformed(ActionEvent evt) {
        rotateImage(90d);
//        clearStack();
    }

    /**
     * Rotates image.
     *
     * @param angle the degree of rotation
     */
    void rotateImage(double angle) {
        try {
            BufferedImage image = imageList.get(imageIndex); //.getRotatedImageIcon(Math.toRadians(angle));
            imageList.set(imageIndex, image); // persist the rotated image
            iioImageList.get(imageIndex).setRenderedImage(image);
            loadImage();
        } catch (OutOfMemoryError oome) {
            logger.log(Level.SEVERE, oome.getMessage(), oome);
//            JOptionPane.showMessageDialog(this, oome.getMessage(), bundle.getString("OutOfMemoryError"), JOptionPane.ERROR_MESSAGE);
        }
    }
}
