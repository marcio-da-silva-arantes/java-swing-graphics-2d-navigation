/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marte.swing.graphics.pkg2d.navigation;

import java.awt.Dimension;

/**
 *
 * @author marcio
 */
public interface iDimensions {
    public void Config(int w, int h);
    public void Original(int w, int h);
    public int oWidth();
    public int oHeight();
    public Dimension oDimension();
}
