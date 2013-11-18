/* 
 * Copyright 2013 Muthukumaran (https://github.com/muthuishere/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.multimachine.views.components;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CheckComboBox extends JComboBox {

    private List<ObjCheckBox> cbs;
    private Map<Object, Boolean> mapObjSelected;
    private List<CheckComboBoxSelectionChangedListener> changedListeners
            = new Vector();

    private Object nullObject = new Object();

    public CheckComboBox(final Set objs) {
        this(objs, false);
    }

    public CheckComboBox(final Set objs, boolean selected) {
        resetObjs(objs, selected);
    }

    public CheckComboBox(final Set objs, final Set selected) {
        mapObjSelected = new LinkedHashMap();
        for (Object obj : objs) {
            if (obj == null) {
                obj = nullObject;
            }
            mapObjSelected.put(obj, selected.contains(obj));
        }

        reset();
    }

    public CheckComboBox(Map<Object, Boolean> mapObjSelected) {
        this.mapObjSelected = mapObjSelected;
        reset();
    }

    public void addSelectionChangedListener(CheckComboBoxSelectionChangedListener l) {
        if (l == null) {
            return;
        }
        changedListeners.add(l);
    }

    public void removeSelectionChangedListener(CheckComboBoxSelectionChangedListener l) {
        changedListeners.remove(l);
    }

    public void resetObjs(final Set objs, boolean selected) {
        mapObjSelected = new LinkedHashMap();
        for (Object obj : objs) {
            mapObjSelected.put(obj, selected);
        }

        reset();
    }

    public Object[] getSelectedItems() {
        Set ret = new TreeSet(); // alphabetically
        for (Map.Entry<Object, Boolean> entry : mapObjSelected.entrySet()) {
            Object obj = entry.getKey();
            Boolean selected = entry.getValue();

            if (selected) {
                ret.add(obj);
            }
        }

        if (ret.isEmpty()) {
            return null;
        }

        return ret.toArray(new Object[ret.size()]);
    }

    public ArrayList<Integer> getSelectedIndexes() {
        ArrayList<Integer> ret = new ArrayList<Integer>(); // alphabetically
        int i = 0;
        for (Map.Entry<Object, Boolean> entry : mapObjSelected.entrySet()) {
            Object obj = entry.getKey();
            Boolean selected = entry.getValue();

            if (selected) {
                ret.add(i);
            }
            i++;
        }

        if (ret.isEmpty()) {
            return null;
        }

        return ret;
    }

    public void addSelectedItems(Collection c) {
        if (c == null) {
            return;
        }

        for (Object obj : c) {
            if (mapObjSelected.containsKey(obj)) {
                mapObjSelected.put(obj, true);
            }
        }

        reset();
        repaint();
    }

    public void addSelectedItems(Object[] objs) {
        if (objs == null) {
            return;
        }

        for (Object obj : objs) {
            if (mapObjSelected.containsKey(obj)) {
                mapObjSelected.put(obj, true);
            }
        }

        reset();
        repaint();
    }

    private void reset() {
        this.removeAllItems();

        initCBs();

        this.addItem(new String());
        for (JCheckBox cb : cbs) {
            this.addItem(cb);
        }

        setRenderer(new CheckBoxRenderer(cbs));
        addActionListener(this);
    }

    private void initCBs() {
        cbs = new Vector<ObjCheckBox>();

        ObjCheckBox cb;
        for (Map.Entry<Object, Boolean> entry : mapObjSelected.entrySet()) {
            Object obj = entry.getKey();
            Boolean selected = entry.getValue();

            cb = new ObjCheckBox(obj);
            cb.setSelected(selected);
            cbs.add(cb);
        }
        /*
         cb = new ObjCheckBox("Select all");
         cb.setSelected(selectedAll);
         cbs.add(cb);

         cb = new ObjCheckBox("Select none");
         cb.setSelected(selectedNone);
         cbs.add(cb);
        
         */
    }

    private void checkBoxSelectionChanged(int index) {
        int n = cbs.size();
        if (index < 0 || index >= n) {
            return;
        }

        //Set selectedObj = getSelected();
        if (index < n) {
            ObjCheckBox cb = cbs.get(index);
            if (cb.getObj() == nullObject) {
                return;
            }

            if (cb.isSelected()) {
                cb.setSelected(false);
                mapObjSelected.put(cb.getObj(), false);

                //cbs.get(n-2).setSelected(false); //Select all
                //cbs.get(n-1).setSelected(getSelectedItems()==null); // select none
            } else {
                cb.setSelected(true);
                mapObjSelected.put(cb.getObj(), true);

                Object[] sobjs = getSelectedItems();
                //cbs.get(n-2).setSelected(sobjs!=null && sobjs.length==n-2); // Select all
                //cbs.get(n-1).setSelected(false); // select none
            }
        }/* else if (index==n-2) {
         for (Object obj : mapObjSelected.keySet()) {
         if (obj!=nullObject)
         mapObjSelected.put(obj, true);
         }

         for (int i=0; i<n-1; i++) {
         if (cbs.get(i)!=nullObject)
         cbs.get(i).setSelected(true);
         }
         cbs.get(n-1).setSelected(false);
         } else { // if (index==n-1)
         for (Object obj : mapObjSelected.keySet()) {
         mapObjSelected.put(obj, false);
         }

         for (int i=0; i<n-1; i++) {
         cbs.get(i).setSelected(false);
         }
         cbs.get(n-1).setSelected(true);
         }*/

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int sel = getSelectedIndex();

        if (sel == 0) {
            getUI().setPopupVisible(this, false);
        } else if (sel > 0) {
            checkBoxSelectionChanged(sel - 1);
            for (CheckComboBoxSelectionChangedListener l : changedListeners) {
                l.selectionChanged(sel - 1);
            }
        }

        this.setSelectedIndex(-1); // clear selection
    }

    @Override
    public void setPopupVisible(boolean flag) {
        //TODO this not work, fix it
        // Not code here prevents the populist from closing
    }

    // checkbox renderer for combobox
    class CheckBoxRenderer implements ListCellRenderer {

        private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
        private javax.swing.JSeparator separator;
        private final List<ObjCheckBox> cbs;
        //private final Set objs;

        public CheckBoxRenderer(final List<ObjCheckBox> cbs) {
            //setOpaque(true);
            this.cbs = cbs;
            //this.objs = objs;
            separator = new javax.swing.JSeparator(javax.swing.JSeparator.HORIZONTAL);
        }

        //@Override
        public Component getListCellRendererComponent(JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            if (index > 0 && index <= cbs.size()) {
                ObjCheckBox cb = cbs.get(index - 1);
                if (cb.getObj() == nullObject) {
                    return separator;
                }

                // cb.setBackground(isSelected ? Color.blue : Color.orange);
                cb.setForeground(isSelected ? Color.blue : Color.black);

                return cb;
            }

            String str;
            Object[] objs = getSelectedItems();
            Vector<String> strs = new Vector();
            if (objs == null) {
                str = "Please select one or more Servers ";
            } else {
                for (Object obj : objs) {
                    strs.add(obj.toString());
                }
                str = strs.toString();
            }
            return defaultRenderer.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
        }
    }

    class ObjCheckBox extends JCheckBox {

        private final Object obj;

        public ObjCheckBox(final Object obj) {
            super(obj.toString());
            this.obj = obj;
        }

        public Object getObj() {
            return obj;
        }
    }

}

interface CheckComboBoxSelectionChangedListener extends java.util.EventListener {

    public void selectionChanged(int idx);
}
