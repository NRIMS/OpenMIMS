package com.nrims;

import com.nrims.segmentation.ClassManager;
import com.nrims.segmentation.EngineSetupDialog;
import com.nrims.segmentation.SegRoi;
import com.nrims.segmentation.SegmentationProperties;
import com.nrims.segmentation.SegmentationEngine;
import ij.gui.Roi;
import java.awt.Checkbox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author gormanns
 */
public class SegmentationSetupForm extends javax.swing.JDialog {

//    private ClassManager classes;
//    private String activeClass;
//    private String previousClass;
    private List<ROIgroup> groups;
    private String activeGroupName;
    private String previousGroupName;
    private boolean ignoreEvents;   // flag to ignore combo box events; necessary during certain GUI operations
    private MimsRoiManager2 roiManager;
    private SegmentationProperties props;
    private SegmentationForm form;
    private Checkbox[] massBoxes;
    private Checkbox[] ratioBoxes;
    private Checkbox[] localBoxes;
    private String[] massImageLabels;
    private String[] ratioImageLabels;
    private EngineSetupDialog engineSetup;
    private List<ROIgroup> trainingGroups;

    /**
     * Creates new form SVMSegmentationSetupForm
     * 
     * @param ui the <code>UI</code> instance
     * @param form the <code>SegmentationForm</code> instance
     * @param roiManager  the <code>MimsRoiManager2</code> instance
     * @param trainingGroups a list of training groups
     * @param massImageFeatures an array of mass images
     * @param localFeatures an array of local features
     * @param ratioImageFeatures a boolean array of ration image features
     * @param colorImageIndex a color image index
     * @param props the <code>SegmentationProperties</code> instance
     */
    //public SegmentationSetupForm(UI ui, SegmentationForm form, MimsRoiManager2 roiManager, ClassManager classes, boolean[] massImageFeatures,
    //        boolean[] ratioImageFeatures, int[] localFeatures, int colorImageIndex, SegmentationProperties props) {
    public SegmentationSetupForm(UI ui, SegmentationForm form, MimsRoiManager2 roiManager, List trainingGroups, boolean[] massImageFeatures,
            boolean[] ratioImageFeatures, int[] localFeatures, int colorImageIndex, SegmentationProperties props) {
        super(ui, false);
        initComponents();
        defaultsButton.setVisible(false);
        jSpinner1.setModel(new SpinnerNumberModel(1, 1, 10, 1));
        String[] massNames = ui.getOpener().getMassNames();

        // Mass Checkboxes
        massBoxes = new Checkbox[]{masscheckbox1, masscheckbox2, masscheckbox3, masscheckbox4,
            masscheckbox5, masscheckbox6, masscheckbox7, masscheckbox8,
            masscheckbox9, masscheckbox10, masscheckbox11, masscheckbox12,
            masscheckbox13, masscheckbox14, masscheckbox15, masscheckbox16};
        massImageLabels = new String[massNames.length];
        for (int i = 0; i < massNames.length; i++) {
            massImageLabels[i] = "Mass " + massNames[i];
            massBoxes[i].setLabel(massImageLabels[i]);
            massBoxes[i].setEnabled(true);
        }

        // Ratio Checkboxes
        ratiocheckbox1.setLabel("Ratio " + massNames[1] + " / " + massNames[0]);
        ratiocheckbox2.setLabel("Ratio " + massNames[3] + " / " + massNames[2]);
        ratioBoxes = new Checkbox[]{ratiocheckbox1, ratiocheckbox2, ratiocheckbox3};
        ratioImageLabels = new String[]{ratiocheckbox1.getLabel(), ratiocheckbox2.getLabel(), ratiocheckbox3.getLabel()};

        localBoxes = new Checkbox[]{checkbox7, checkbox8};
        MyCheckboxListener listener = new MyCheckboxListener();
        for (int i = 0; i < massNames.length; i++) {
            massBoxes[i].addItemListener(listener);
            massBoxes[i].setState(massImageFeatures[i]);
        }
        for (int i = 0; i < this.ratioBoxes.length; i++) {
            ratioBoxes[i].addItemListener(listener);
            ratioBoxes[i].setState(ratioImageFeatures[i]);
        }
        fillColorBox(colorImageIndex);
        localBoxes[0].addItemListener(listener);
        localBoxes[1].addItemListener(listener);
        localBoxes[0].setState(localFeatures[0] == 1);
        localBoxes[1].setState(localFeatures[1] == 1);
        jSpinner1.setValue(localFeatures[2]);
        // ##########

        this.form = form;
        this.roiManager = roiManager;
        //clearRoiManager();
        this.props = props;
        //this.activeClass = previousClass = "";
        this.activeGroupName = previousGroupName = "";

       // this.classes = (ClassManager) classes.clone(); // deep copy of class manager
       // this.groups = (ClassManager) classes.clone(); // deep copy of class manager

        ignoreEvents = true;
       // fillClassBox(); // automatically selects first item if there are any
        fillGroupsBox(); // automatically selects first item if there are any

        
        this.trainingGroups = trainingGroups;
        if (classesCombo.getItemCount() > 0) {
            //setActiveClass((String) classesCombo.getSelectedItem());
            setActiveGroupName((String) classesCombo.getSelectedItem());
        }
        ignoreEvents = false;

        for (String engine : SegmentationForm.implementedEngines.keySet()) {
            enginesCombo.addItem(engine);
        }
        enginesCombo.setSelectedItem(props.getValueOf(SegmentationEngine.ENGINE));
    }

    //private boolean fillClassBox() {
    public boolean fillGroupsBox() {
        classesCombo.removeAllItems();
        
        List<ROIgroup> groups = roiManager.getGroups();
        for (ROIgroup group : groups) {
            if (group.getGroupType().compareTo(roiManager.GROUPTYPE_SEGMENTATION_TRAINING) == 0) {
                classesCombo.addItem(group.getGroupName());
            }
        }
//        for (String className : classes.getClasses()) {
//            classesCombo.addItem(className);
//        }
        if (classesCombo.getItemCount() > 0 && !roiManager.isVisible()) {
            roiManager.setVisible(true);
        }

        return classesCombo.getItemCount() > 0;
    }

    private boolean fillColorBox(int selectIndex) {
        if (fillColorBox(null)) {
            colorCombo.setSelectedIndex(selectIndex);
            return true;
        } else {
            return false;
        }
    }

    private boolean fillColorBox(Object selectItem) {
        colorCombo.removeAllItems();
        for (int i = 0; i < massBoxes.length; i++) {
            if (massBoxes[i].getState()) {
                colorCombo.addItem(massImageLabels[i]);
            }
        }
        for (int i = 0; i < ratioBoxes.length; i++) {
            if (ratioBoxes[i].getState()) {
                colorCombo.addItem(ratioImageLabels[i]);
            }
        }
        colorCombo.setSelectedItem(selectItem); // try to select the specified item
        return colorCombo.getItemCount() > 0;
    }

    private void clearRoiManager() {
        // remove ROIs from the RoiManager, if required
        if (roiManager.getROIs().size() > 0) {
            roiManager.selectAll();
            roiManager.delete(false, false);
            roiManager.getImage().setRoi((Roi) null);
        }
    }

    //private void setActiveClass(String className) {
    private void setActiveGroupName(String groupName) {

        // set the current class and add ROIs to the roiManager
        //activeClass = className;
        activeGroupName = groupName;

//        for (SegRoi segRoi : classes.getRois(activeClass)) {
//            roiManager.getImage().setRoi(segRoi.getRoi());
//            roiManager.add();
//        }

//   Because ROIs are now kept in the roiManager groups themselves,
// and not in the old classes here, this code may largely unnecessary.

//        ROIgroup active = roiManager.getGroup(groupName);
//        for (SegRoi segRoi : groups.getRois(activeGroupName)) {
//            roiManager.getImage().setRoi(segRoi.getRoi());
//            roiManager.add();
//        }
    }
    
        
    public void updateGroupName(List<ROIgroup> groups, String oldGroupName, String newGroupName) {                                             
        if (newGroupName.equals("") || newGroupName==null ) {
            return;
        }
        
        fillGroupsBox();
        
        // find the group and rename it
//        for (ROIgroup group : groups) {
//            String groupName = group.getGroupName();
//            if (groupName.compareTo(newGroupName) == 0) {
//                //group.setGroupName(newGroupName);
//                int numItems = classesCombo.getItemCount();
//                String name;
//                for (int i=0; i<numItems; i++) {
//                    name = (String)classesCombo.getItemAt(i);
//                    if (name.compareTo(oldGroupName) == 0) {
//                        classesCombo.removeItem(name);
//                    }
//                }
//                classesCombo.addItem(newGroupName);
//            }
//        }
    }   

    private void closeForm() {
        engineSetup.dispose();
      //  clearRoiManager();
        ((java.awt.Frame) this.getOwner()).setState(java.awt.Frame.NORMAL);
        this.dispose();
    }

    private boolean isSynchronized() {
        if (activeGroupName.equals("")) {
            return false;    // by definition: "out of sync" if there is no active class
        }
        
        // Not sure any of this is any longer necessary
//        for (SegRoi segRoi : groups.getRois(activeGroupName)) {
//            if (roiManager.getROIs().contains(segRoi.getRoi())) {
//                continue;    // match class rois with roiManager
//            }
//            return false;
//        }
//        // "in sync", if matching in one direction succeeded AND the number of elements are the same
//        return groups.getRois(activeGroupName).length == roiManager.getROIs().size();
        return true;
    }

    private int classChangeAttempt() {
        int option = JOptionPane.NO_OPTION; // default option is to discard anything unless we're "out of sync"
        if (roiManager.getROIs().size() > 0 && !isSynchronized()) { // there are ROIs in the manager that have not been saved, yet
            String[] options = {"Keep", "Discard", "Cancel"};
            option = JOptionPane.showOptionDialog(this, "Not all ROIs have been saved!", null,
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);
        }
        return option;
    }

    private class MyCheckboxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            Checkbox box = (Checkbox) e.getSource();
            if (box.equals(checkbox7) || box.equals(checkbox8)) {     // this is a neigborhood feature checkbox
                jSpinner1.setEnabled(checkbox7.getState() || checkbox8.getState());
            } else // this is an image feature checkbox
            {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    // make sure, at least one image is selected
                    boolean selected = false;
                    for (int i = 0; i < massBoxes.length; i++) {
                        if (massBoxes[i].getState()) {
                            selected = true;
                        }
                    }
                    for (int i = 0; i < ratioBoxes.length; i++) {
                        if (ratioBoxes[i].getState()) {
                            selected = true;
                        }
                    }
                    if (!selected) {
                        box.setState(true);                   // force "checked" state of checkbox
                    } else {
                        fillColorBox(colorCombo.getSelectedItem());    // update color combobox
                    }
                } else if (e.getStateChange() == ItemEvent.SELECTED) {
                    fillColorBox(colorCombo.getSelectedItem());
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        masscheckbox1 = new java.awt.Checkbox();
        masscheckbox2 = new java.awt.Checkbox();
        masscheckbox3 = new java.awt.Checkbox();
        masscheckbox4 = new java.awt.Checkbox();
        masscheckbox5 = new java.awt.Checkbox();
        masscheckbox6 = new java.awt.Checkbox();
        checkbox7 = new java.awt.Checkbox();
        checkbox8 = new java.awt.Checkbox();
        jSpinner1 = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        ratiocheckbox2 = new java.awt.Checkbox();
        ratiocheckbox1 = new java.awt.Checkbox();
        masscheckbox7 = new java.awt.Checkbox();
        ratiocheckbox3 = new java.awt.Checkbox();
        jSeparator3 = new javax.swing.JSeparator();
        masscheckbox8 = new java.awt.Checkbox();
        masscheckbox9 = new java.awt.Checkbox();
        masscheckbox10 = new java.awt.Checkbox();
        masscheckbox11 = new java.awt.Checkbox();
        masscheckbox12 = new java.awt.Checkbox();
        masscheckbox13 = new java.awt.Checkbox();
        masscheckbox14 = new java.awt.Checkbox();
        masscheckbox15 = new java.awt.Checkbox();
        masscheckbox16 = new java.awt.Checkbox();
        jPanel3 = new javax.swing.JPanel();
        classesCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        enginesCombo = new javax.swing.JComboBox();
        setupButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        defaultsButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        colorCombo = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Model Configuration");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Features", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.setPreferredSize(new java.awt.Dimension(144, 212));

        masscheckbox1.setEnabled(false);
        masscheckbox1.setLabel("Mass");

        masscheckbox2.setEnabled(false);
        masscheckbox2.setLabel("Mass");

        masscheckbox3.setEnabled(false);
        masscheckbox3.setLabel("Mass");

        masscheckbox4.setEnabled(false);
        masscheckbox4.setLabel("Mass");

        masscheckbox5.setEnabled(false);
        masscheckbox5.setLabel("Mass");

        masscheckbox6.setEnabled(false);
        masscheckbox6.setLabel("Mass");

        checkbox7.setLabel("Neighborhood");

        checkbox8.setLabel("Gradient");

        jSpinner1.setValue(1);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabel2.setText("Images");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Size");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        jLabel4.setText("Neigborhood");

        ratiocheckbox2.setLabel("Ratio");

        ratiocheckbox1.setLabel("Ratio");

        masscheckbox7.setEnabled(false);
        masscheckbox7.setLabel("Mass");

        ratiocheckbox3.setLabel("All Open Ratio");

        jSeparator3.setBackground(new java.awt.Color(99, 130, 191));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        masscheckbox8.setEnabled(false);
        masscheckbox8.setLabel("Mass");

        masscheckbox9.setEnabled(false);
        masscheckbox9.setLabel("Mass");

        masscheckbox10.setEnabled(false);
        masscheckbox10.setLabel("Mass");

        masscheckbox11.setEnabled(false);
        masscheckbox11.setLabel("Mass");

        masscheckbox12.setEnabled(false);
        masscheckbox12.setLabel("Mass");

        masscheckbox13.setEnabled(false);
        masscheckbox13.setLabel("Mass");

        masscheckbox14.setEnabled(false);
        masscheckbox14.setLabel("Mass");

        masscheckbox15.setEnabled(false);
        masscheckbox15.setLabel("Mass");

        masscheckbox16.setEnabled(false);
        masscheckbox16.setLabel("Mass");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(masscheckbox5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(masscheckbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(masscheckbox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(masscheckbox6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(masscheckbox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(masscheckbox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(masscheckbox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(masscheckbox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(masscheckbox13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(masscheckbox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(masscheckbox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(masscheckbox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(masscheckbox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(masscheckbox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(masscheckbox15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(masscheckbox16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ratiocheckbox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ratiocheckbox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ratiocheckbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(166, 166, 166)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3))
                            .addComponent(checkbox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addComponent(checkbox8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(215, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(masscheckbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(masscheckbox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(masscheckbox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(masscheckbox8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(masscheckbox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(masscheckbox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(masscheckbox16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(checkbox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ratiocheckbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(checkbox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ratiocheckbox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ratiocheckbox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ROI Training Groups", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel3.setPreferredSize(new java.awt.Dimension(401, 90));

        classesCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                classesComboItemStateChanged(evt);
            }
        });

        jLabel1.setText("current group");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(classesCombo, 0, 272, Short.MAX_VALUE)
                .addGap(13, 13, 13))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Algorithm Setup", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel1.setPreferredSize(new java.awt.Dimension(293, 90));
        jPanel1.setRequestFocusEnabled(false);

        enginesCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                enginesComboItemStateChanged(evt);
            }
        });

        setupButton.setText("Setup");
        setupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setupButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enginesCombo, 0, 269, Short.MAX_VALUE)
                    .addComponent(setupButton, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(enginesCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setupButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        defaultsButton.setText("Defaults");
        defaultsButton.setEnabled(false);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Color Source Image", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel4.setPreferredSize(new java.awt.Dimension(76, 90));

        colorCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(colorCombo, 0, 210, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(colorCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(defaultsButton)
                        .addGap(91, 91, 91)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 952, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton)
                        .addComponent(okButton))
                    .addComponent(defaultsButton))
                .addGap(0, 21, Short.MAX_VALUE))
        );

        jPanel3.getAccessibleContext().setAccessibleDescription("");
        jPanel1.getAccessibleContext().setAccessibleName("Segmentation algorithm");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void classesComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_classesComboItemStateChanged
        if (ignoreEvents) {
            return; // action is only required, if the combo box was changed "from outside" (i.e. by the user)
        }
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String groupName = (String) classesCombo.getSelectedItem();
            if (groupName.equals(activeGroupName)) {
                return;   // do nothing if the selected class is already active
            }
            int option = classChangeAttempt();
            if (option != JOptionPane.CANCEL_OPTION) {
                if (option == JOptionPane.NO_OPTION) {
                    //clearRoiManager();
                }
                setActiveGroupName(groupName);
            } else {
                classesCombo.setSelectedItem(previousGroupName);
            }
        } else if (evt.getStateChange() == java.awt.event.ItemEvent.DESELECTED) {
            previousGroupName = activeGroupName;
        }
}//GEN-LAST:event_classesComboItemStateChanged

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (props.getValueOf(SegmentationEngine.MODEL) != null) {    // check, whether there was a model trained already
            int value = JOptionPane.showConfirmDialog(this, "The current model is already trained!\n"
                    + "Proceeding will discard the current model\nand any corresponding prediction.", "Confirm Model Discard",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (value == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        form.resetForm(); // this also resets the properties object

        props = form.getProperties();
        props.setValueOf(SegmentationEngine.ENGINE, enginesCombo.getSelectedItem());
        engineSetup.getProperties(props);

        boolean[] massImageFeatures = new boolean[massBoxes.length];
        boolean[] ratioImageFeatures = new boolean[ratioBoxes.length];
        int[] localFeatures = new int[localBoxes.length + 1]; //+1 for neighborhood spinner

        for (int i = 0; i < this.massBoxes.length; i++) {
            massImageFeatures[i] = massBoxes[i].getState();
        }
        for (int i = 0; i < this.ratioBoxes.length; i++) {
            ratioImageFeatures[i] = ratioBoxes[i].getState();
        }
        if (localBoxes[0].getState()) {
            localFeatures[0] = 1; // neighborhood
        }
        if (localBoxes[1].getState()) {
            localFeatures[1] = 1; // gradient
        }
        localFeatures[2] = (Integer) jSpinner1.getValue();  // neighborhood size

        //form.setTrainClasses(classes);
        form.setTrainingGroups(trainingGroups);
        form.setMassImageFeatures(massImageFeatures);
        form.setRatioImageFeatures(ratioImageFeatures);
        form.setLocalFeatures(localFeatures);
        form.setColorImageIndex(colorCombo.getSelectedIndex());
        form.setProperties(props);
        form.updateControls();
        //form.setClassNames(classes.getClasses());
        String[] trainingGroupsStringArray = new String[trainingGroups.size()];
        int i=0;
        for (ROIgroup group : trainingGroups) {
            trainingGroupsStringArray[i++] = group.getGroupName();
        }
        
        form.setTrainingGroupNames(trainingGroupsStringArray);

        closeForm();
}//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        closeForm();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void setupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setupButtonActionPerformed
        engineSetup.setVisible(true);
    }//GEN-LAST:event_setupButtonActionPerformed

    private void enginesComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_enginesComboItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String newEngineName = (String) enginesCombo.getSelectedItem();
            String engineSetupName = SegmentationForm.implementedEngines.get(newEngineName)[1];
            Class engineSetupClass;
            try {
                engineSetupClass = Class.forName(engineSetupName);
                Class[] params = {javax.swing.JDialog.class};
                java.lang.reflect.Constructor con;
                con = engineSetupClass.getConstructor(params);
                engineSetup = (EngineSetupDialog) con.newInstance(this);
                engineSetup.setProperties(props);
            } catch (Exception ex) {
                System.out.println("Error: engine setup form could not be instantiated!");
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_enginesComboItemStateChanged

    private void colorComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_colorComboActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton cancelButton;
    private java.awt.Checkbox checkbox7;
    private java.awt.Checkbox checkbox8;
    private javax.swing.JComboBox classesCombo;
    private javax.swing.JComboBox colorCombo;
    private javax.swing.JButton defaultsButton;
    private javax.swing.JComboBox enginesCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSpinner jSpinner1;
    private java.awt.Checkbox masscheckbox1;
    private java.awt.Checkbox masscheckbox10;
    private java.awt.Checkbox masscheckbox11;
    private java.awt.Checkbox masscheckbox12;
    private java.awt.Checkbox masscheckbox13;
    private java.awt.Checkbox masscheckbox14;
    private java.awt.Checkbox masscheckbox15;
    private java.awt.Checkbox masscheckbox16;
    private java.awt.Checkbox masscheckbox2;
    private java.awt.Checkbox masscheckbox3;
    private java.awt.Checkbox masscheckbox4;
    private java.awt.Checkbox masscheckbox5;
    private java.awt.Checkbox masscheckbox6;
    private java.awt.Checkbox masscheckbox7;
    private java.awt.Checkbox masscheckbox8;
    private java.awt.Checkbox masscheckbox9;
    private javax.swing.JButton okButton;
    private java.awt.Checkbox ratiocheckbox1;
    private java.awt.Checkbox ratiocheckbox2;
    private java.awt.Checkbox ratiocheckbox3;
    private javax.swing.JButton setupButton;
    // End of variables declaration//GEN-END:variables
}
