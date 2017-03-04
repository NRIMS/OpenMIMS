/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * QSACorrectionFrame.java
 *
 * Created on Dec 13, 2011, 10:50:52 AM
 */
package com.nrims.managers;

import com.nrims.MimsJFileChooser;
import com.nrims.UI;
import java.awt.Cursor;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author zkaufman
 */
public class QSAcorrectionManager extends javax.swing.JFrame {

    UI ui;
    float dwellTime;

    public static final String DWELL_TIME_LABEL_STRING = "Using dwelltime (ms): ";

    /**
     * Creates new form QSACorrectionFrame
     */
    public QSAcorrectionManager(UI ui) {
        this.ui = ui;
        setLocation(ui.getLocation());
        initComponents();
        initComponentsCustom();
    }

    /*
     * Customize components.
     */
    private void initComponentsCustom() {

        // Set the betas label.
        jLabel1.setText("Betas: (" + ui.getOpener().getNMasses() + " values, comma separated)");
        try {
            dwellTime = Float.parseFloat(ui.getOpener().getDwellTime()) / 1000;
        } catch (Exception e) {
            ij.IJ.error("Error", "Mal-formed FC Objective (\"" + ui.getOpener().getDwellTime() + "\" not a valid number).");
            return;
        }

        // Set the betas label.
        dwellTimeLabel.setText(DWELL_TIME_LABEL_STRING + Float.toString(dwellTime));

        // Fill in a default file name
        fileTextField.setText(ui.getImageDir() + "/" + ui.getImageFilePrefix() + UI.NRRD_EXTENSION);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      jLabel1 = new javax.swing.JLabel();
      betasTextField = new javax.swing.JTextField();
      jLabel2 = new javax.swing.JLabel();
      FCObjectiveTextField = new javax.swing.JTextField();
      dwellTimeLabel = new javax.swing.JLabel();
      jLabel4 = new javax.swing.JLabel();
      fileTextField = new javax.swing.JTextField();
      browseButton = new javax.swing.JButton();
      OKButton = new javax.swing.JButton();
      cancelButton = new javax.swing.JButton();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

      jLabel1.setText("Betas:");

      jLabel2.setText("FC Objective (pA):");

      dwellTimeLabel.setText("Using dwelltime (ms):");

      jLabel4.setText("Save corrected file as:");

      fileTextField.setEditable(false);

      browseButton.setText("Browse...");
      browseButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            browseButtonActionPerformed(evt);
         }
      });

      OKButton.setText("OK");
      OKButton.setMaximumSize(new java.awt.Dimension(55, 28));
      OKButton.setMinimumSize(new java.awt.Dimension(55, 28));
      OKButton.setPreferredSize(new java.awt.Dimension(55, 28));
      OKButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            OKButtonActionPerformed(evt);
         }
      });

      cancelButton.setText("Cancel");
      cancelButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            cancelButtonActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jLabel1)
               .addComponent(dwellTimeLabel)
               .addComponent(jLabel4)
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addComponent(fileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(browseButton))
               .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                  .addComponent(cancelButton)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(OKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(betasTextField, javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(FCObjectiveTextField, javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap())
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(betasTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(FCObjectiveTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(dwellTimeLabel)
            .addGap(18, 18, 18)
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(fileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(browseButton))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(OKButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(cancelButton))
            .addContainerGap())
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKButtonActionPerformed

        float[] betas;
        float FCObj;
        File file;

        // Get the Betas.
        String[] betasplit = betasTextField.getText().split(",");
        if (betasplit.length != ui.getOpener().getNMasses()) {
            ij.IJ.error("Error", "Incorrect number of betas defined.");
            return;
        }
        betas = new float[betasplit.length];
        try {
            for (int i = 0; i < betas.length; i++) {
                betas[i] = Float.parseFloat(betasplit[i]);
            }
        } catch (Exception e) {
            ij.IJ.error("Error", "Mal-formed Betas (\"" + betasTextField.getText() + "\" not a valid number).");
            return;
        }

        // Get the FC Objective.
        try {
            FCObj = Float.parseFloat(FCObjectiveTextField.getText());
        } catch (Exception e) {
            ij.IJ.error("Error", "Mal-formed FC Objective (\"" + FCObjectiveTextField.getText() + "\" not a valid number).");
            return;
        }

        // Make sure we can write the file
        file = new File(fileTextField.getText());
        if (!file.getParentFile().canWrite()) {
            ij.IJ.error("Error", "Can not write to directory: " + file.getParentFile().getAbsolutePath());
            return;
        }

        // Perform QSA correction.
        int start_slice = ui.getOpenMassImages()[0].getCurrentSlice();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            com.nrims.data.massCorrection masscor = new com.nrims.data.massCorrection(ui);
            masscor.performQSACorr(ui.getOpenMassImages(), betas, dwellTime, FCObj);
            ui.setIsQSACorrected(true);
            ui.setIsDTCorrected(true);
            ui.setBetas(betas);
            ui.setFCObjective(FCObj);
            ui.saveSession(file.getAbsolutePath(), true);
            ui.QSACorrectionMenuItem.setEnabled(false);
            ui.DTCorrectionMenuItem.setSelected(true);
            ui.DTCorrectionMenuItem.setEnabled(false);

        } catch (Exception e) {
            ij.IJ.error("Unexpected error while performing QSA correction.");
        } finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        ui.updateStatus("QSA correction complete.");
        ui.getOpenMassImages()[0].setSlice(start_slice);
        ui.updateAllImages();

        // Close the frame
        this.setVisible(false);
        ij.IJ.showMessage("Done");
    }//GEN-LAST:event_OKButtonActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        MimsJFileChooser fc = new MimsJFileChooser(ui);
        if (ui.getImageFilePrefix() != null) {
            fc.setSelectedFile(new java.io.File(ui.getImageFilePrefix() + UI.NRRD_EXTENSION));
        }
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileTextField.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_browseButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        ui.QSACorrectionMenuItem.setSelected(false);
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JTextField FCObjectiveTextField;
   private javax.swing.JButton OKButton;
   private javax.swing.JTextField betasTextField;
   private javax.swing.JButton browseButton;
   private javax.swing.JButton cancelButton;
   private javax.swing.JLabel dwellTimeLabel;
   private javax.swing.JTextField fileTextField;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel4;
   // End of variables declaration//GEN-END:variables

}
