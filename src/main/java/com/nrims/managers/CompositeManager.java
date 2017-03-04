/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * CompositeManager.java
 *
 * Created on Jan 28, 2010, 12:17:00 PM
 */
package com.nrims.managers;

import com.nrims.*;
import java.util.ArrayList;

/**
 *
 * @author cpoczatek
 */
public class CompositeManager extends javax.swing.JFrame {

    static javax.swing.JFrame instance;

    private UI ui;
    private javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();

    // DJ: 08/04/2014
    // arraylist to hold all opened windows' long titles. 
    private ArrayList<String> wholetitles = new ArrayList<String>();

    /**
     * Creates new form CompositeManager
     */
    public CompositeManager(UI ui) {
        initComponents();
        jList1.setModel(listModel);
        jList1.setDragEnabled(true); // DJ: 08/01/2014
        this.ui = ui;
        this.setTitle("Composite Manager");

        instance = this;

    }

    @Override
    public void setVisible(boolean viz) {
        if (!viz) {
            super.setVisible(viz);
        }

        listModel.removeAllElements();

        addImages(ui.getOpenMassImages());
        addImages(ui.getOpenRatioImages());
        addImages(ui.getOpenSumImages());

        super.setVisible(viz);
    }

    // DJ: 08/07/2014
    public void update(MimsPlus[] mp) {
        listModel.removeAllElements();
        redTextField.setText("");
        greenTextField.setText("");
        blueTextField.setText("");
        grayTextField.setText("");
        addImages(mp);

    }

    public void addImages(MimsPlus[] imgs) {

        for (int i = 0; i < imgs.length; i++) {
            // DJ: 08/01/2014
            this.wholetitles.add(imgs[i].getTitle());

            if (imgs[i].getShortTitle().contains("[")) {
                listModel.addElement(imgs[i].getShortTitle().substring(0, imgs[i].getShortTitle().indexOf("[")));
            } else if (imgs[i].getTitle().contains(":")) {
                listModel.addElement(imgs[i].getTitle().substring(0, imgs[i].getTitle().indexOf(":")));
            } else {
                listModel.addElement(imgs[i].getTitle());
            }
        }
    }

    public static CompositeManager getInstance() {
        return (CompositeManager) instance;
    }

    public void closeWindow() {
        super.dispose();
    }

    public void showWindow() {
        super.frameInit();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        redButton = new javax.swing.JButton();
        greenButton = new javax.swing.JButton();
        blueButton = new javax.swing.JButton();
        grayButton = new javax.swing.JButton();
        displayButton = new javax.swing.JButton();
        redTextField = new javax.swing.JTextField();
        greenTextField = new javax.swing.JTextField();
        blueTextField = new javax.swing.JTextField();
        grayTextField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        redButton.setText("Red");
        redButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButtonActionPerformed(evt);
            }
        });

        greenButton.setText("Green");
        greenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                greenButtonActionPerformed(evt);
            }
        });

        blueButton.setText("Blue");
        blueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButtonActionPerformed(evt);
            }
        });

        grayButton.setText("Gray");
        grayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grayButtonActionPerformed(evt);
            }
        });

        displayButton.setText("Display");
        displayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(displayButton)
                .add(30, 30, 30)
                .add(cancelButton)
                .add(52, 52, 52))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(14, 14, 14)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(greenButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(redButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(blueButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(grayButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(greenTextField)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, blueTextField)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, grayTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(0, 0, Short.MAX_VALUE))
                            .add(redTextField))
                        .add(24, 24, 24))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(clearButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(79, 79, 79))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                .add(grayTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(grayButton))
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(redButton)
                                    .add(redTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(greenButton)
                                    .add(greenTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(blueButton)
                                    .add(blueTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(33, 33, 33)))
                        .add(clearButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(displayButton)
                            .add(cancelButton))
                        .add(23, 23, 23))
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(92, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.clearButtonActionPerformed(evt);
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void displayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayButtonActionPerformed

        String[] imgNames = {"", "", "", ""};

        int redINDX = -1;
        boolean is_red_set = false;
        int grnINDX = -1;
        boolean is_grn_set = false;
        int bluINDX = -1;
        boolean is_blu_set = false;
        int gryINDX = -1;
        boolean is_gry_set = false;

        for (int i = 0; i < listModel.getSize(); i++) {
            if (((String) (listModel.getElementAt(i))).equals(redTextField.getText()) && is_red_set == false) {
                redINDX = i;
                is_red_set = true;
            }
            if (((String) (listModel.getElementAt(i))).equals(greenTextField.getText()) && is_grn_set == false) {
                grnINDX = i;
                is_grn_set = true;
            }
            if (((String) (listModel.getElementAt(i))).equals(blueTextField.getText()) && is_blu_set == false) {
                bluINDX = i;
                is_blu_set = true;
            }
            if (((String) (listModel.getElementAt(i))).equals(grayTextField.getText()) && is_gry_set == false) {
                gryINDX = i;
                is_gry_set = true;
            }
        }

        if (redINDX != -1) {
            imgNames[0] = this.wholetitles.get(redINDX);
        }
        if (grnINDX != -1) {
            imgNames[1] = this.wholetitles.get(grnINDX);
        }
        if (bluINDX != -1) {
            imgNames[2] = this.wholetitles.get(bluINDX);
        }
        if (gryINDX != -1) {
            imgNames[3] = this.wholetitles.get(gryINDX);
        }

        Object[] imgs = new Object[4];

        for (int i = 0; i < 4; i++) {
            MimsPlus nimg = ui.getImageByName(imgNames[i]);
            if (nimg == null) {
                imgs[i] = null;
                continue;
            }

            if (nimg.getMimsType() == MimsPlus.RATIO_IMAGE) {
                imgs[i] = nimg.getRatioProps();
            }
            if (nimg.getMimsType() == MimsPlus.SUM_IMAGE) {
                imgs[i] = nimg.getSumProps();
            }
            if (nimg.getMimsType() == MimsPlus.MASS_IMAGE) {
                imgs[i] = new MassProps(nimg.getMassIndex(), nimg.getMassValue());
            }
        }

        CompositeProps props = new CompositeProps(imgs);
        MimsPlus img = new MimsPlus(ui, props);
        img.showWindow();
        // set to true -- so the user can add multiple composite images without opening
        // the composite manager all over again.
        // hit OK to add as much composite images as wanted; when done, hit cancel/done Adding
        this.setVisible(true);


    }//GEN-LAST:event_displayButtonActionPerformed

    private void redButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButtonActionPerformed
        if (redTextField.getText().equals("")) {
            redTextField.setText((String) jList1.getSelectedValue());
        } else {
            redTextField.setText("");
        }
    }//GEN-LAST:event_redButtonActionPerformed

    private void greenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_greenButtonActionPerformed
        if (greenTextField.getText().equals("")) {
            greenTextField.setText((String) jList1.getSelectedValue());
        } else {
            greenTextField.setText("");
        }
    }//GEN-LAST:event_greenButtonActionPerformed

    private void blueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blueButtonActionPerformed
        if (blueTextField.getText().equals("")) {
            blueTextField.setText((String) jList1.getSelectedValue());
        } else {
            blueTextField.setText("");
        }
    }//GEN-LAST:event_blueButtonActionPerformed

    private void grayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grayButtonActionPerformed
        if (grayTextField.getText().equals("")) {
            grayTextField.setText((String) jList1.getSelectedValue());
        } else {
            grayTextField.setText("");
        }
    }//GEN-LAST:event_grayButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed

        redTextField.setText("");
        greenTextField.setText("");
        blueTextField.setText("");
        grayTextField.setText("");

        jList1.clearSelection();
    }//GEN-LAST:event_clearButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton blueButton;
    private javax.swing.JTextField blueTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton displayButton;
    private javax.swing.JButton grayButton;
    private javax.swing.JTextField grayTextField;
    private javax.swing.JButton greenButton;
    private javax.swing.JTextField greenTextField;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton redButton;
    private javax.swing.JTextField redTextField;
    // End of variables declaration//GEN-END:variables

}
