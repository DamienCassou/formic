/* class JCheckBox
 *
 * Copyright (C) 2001  R M Pitman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package charvax.swing;

import charva.awt.*;
import charva.awt.event.ItemEvent;
import charva.awt.event.KeyEvent;


/**
 * An implementation of a checkbox - an object that is always in one of two
 * states (SELECTED or DESELECTED) and which displays its state to the user.
 */
public class JCheckBox
        extends JToggleButton {

    /**
     * The default constructor creates a deselected checkbox with an
     * empty label.
     */
    public JCheckBox() {
        super("", false);
    }

    /**
     * Use this constructor when you want to initialize the label.
     */
    public JCheckBox(String text_) {
        super(text_, false);
    }

    /**
     * Use this constructor when you want to set both the label and the value.
     */
    public JCheckBox(String label_, boolean value_) {
        super(label_, value_);
    }

    /**
     * Return the size of the text field. Overrides the method in the
     * Component superclass.
     */
    public Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public int getWidth() {
        Insets insets = super.getInsets();
        return super.getText().length() + 4 + insets.left + insets.right;
    }

    public int getHeight() {
        Insets insets = super.getInsets();
        return 1 + insets.top + insets.bottom;
    }

    /**
     * Called by the LayoutManager.
     */
    public Dimension minimumSize() {
        return this.getSize();
    }

    /**
     * Called by this JCheckBox's parent container.
     */
    public void draw() {

        // Draw the border if it exists
        super.draw();

        String valstring;

        /* Get the absolute origin of this component.
         */
        Point origin = getLocationOnScreen();
        Insets insets = super.getInsets();

        Toolkit term = Toolkit.getDefaultToolkit();

        term.setCursor(origin.addOffset(insets.left, insets.top));
        if (super.isSelected())
            valstring = "[*] ";
        else
            valstring = "[ ] ";

        int colorpair = getCursesColor();
        int attribute = super._enabled ? Toolkit.A_BOLD : 0;
        term.addString(valstring + super.getLabelString(), attribute, colorpair);
    }

    public void processKeyEvent(KeyEvent ke_) {
        /* First call all KeyListener objects that may have been registered
         * for this component.
         */
        super.processKeyEvent(ke_);

        /* Check if any of the KeyListeners consumed the KeyEvent.
         */
        if (ke_.isConsumed())
            return;

        Toolkit term = Toolkit.getDefaultToolkit();
        int key = ke_.getKeyCode();
        switch (key) {
            case '\t':
                getParent().nextFocus();
                return;

            case KeyEvent.VK_BACK_TAB:
                getParent().previousFocus();
                return;

            case KeyEvent.VK_ENTER:
                if (!super.isEnabled()) {
                    term.beep();
                    return;
                }
                super.setSelected(!super.isSelected());

                // post an ItemEvent
                EventQueue queue = term.getSystemEventQueue();
                int state = (super.isSelected()) ?
                        ItemEvent.SELECTED :
                        ItemEvent.DESELECTED;
                queue.postEvent(new ItemEvent(this, this, state));
                break;
        }

        draw();
        requestFocus();
        super.requestSync();
    }

    public void requestFocus() {
        /* Generate the FOCUS_GAINED event.
         */
        super.requestFocus();

        /* Get the absolute origin of this component.
         */
        Point origin = getLocationOnScreen();
        Insets insets = super.getInsets();
        Toolkit.getDefaultToolkit().setCursor(origin.addOffset(1 + insets.left, 0 + insets.top));
    }

    public String toString() {
        return "JCheckBox location=" + getLocation() +
                " label=\"" + getLabel() +
                "\" actionCommand=\"" + getActionCommand() +
                "\" selected=" + isSelected();
    }

    public void debug(int level_) {
        for (int i = 0; i < level_; i++)
            System.err.print("    ");
        System.err.println("JCheckBox origin=" + _origin +
                " size=" + getSize() + " label=" + super.getText());
    }

    //====================================================================
    // INSTANCE VARIABLES

}
