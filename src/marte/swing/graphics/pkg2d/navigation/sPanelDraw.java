/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marte.swing.graphics.pkg2d.navigation;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.RenderingHints.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
/**
 *
 * @author Marcio
 */
public abstract class sPanelDraw extends sPanel {
    
    private final double taxaZoon = 1.125;

    private final double maxZoon = 5e8;
    private final double minZoon = 1e-7;
    
    
    protected double zoom = 1.0;
    private double Cx = 0;
    private double Cy = 0;
    private double dx = 0;
    private double dy = 0;
    protected double Xt;
    protected double Yt;
    private boolean translated = false;

    private int width;
    private int height;

    private boolean anti_aliasing = true;
    private float composite = 1.0f;
    

    public sPanelDraw(Color default_color) {
        super(default_color);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try{
                    MouseClicked(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try{
                    MousePressed(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try{
                    MouseReleased(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                try{
                    MouseMoved(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                try{
                    MouseDragged(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }
        });
        this.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                try{
                    MouseWheelMoved(e);
                }catch(Throwable ex){
                    ex.printStackTrace();
                }
            }
        });

        Init();
    }

    @Override
    public final void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        this.width = preferredSize.width;
        this.height = preferredSize.height;
        Xt = width / 2 - Cx * zoom;
        Yt = height / 2 - Cy * zoom;
        changeSize();
        repaint();
    }

    public void changeSize(){
        
    }
    
    public void goTo(double x, double y, double w, double h){
        setCenter(x+w/2, y+h/2);
        
        zoom = Math.min(width/w, height/h);
        
        repaint();
    }
    
    public void camera(double x, double y){
        this.Cx = x;
        this.Cy = y;
        Xt = width / 2 - Cx * zoom;
        Yt = height / 2 - Cy * zoom;
    }
    
    public int width() {
        return width;
    }
    public int height() {
        return height;
    }
    public double zoom(){
        return zoom;
    }
    public double Cx(){
        return Cx;
    }
    public double Cy(){
        return Cy;
    }
    
    public double Xi(){
        return getXReal(0);
    }
    public double Yi(){
        return getYReal(0);
    }
    public double Xf(){
        return getXReal(width);
    }
    public double Yf(){
        return getYReal(height);
    }

    public void setAntiAliasing(boolean flag){
        anti_aliasing = flag;
    }
    public void setComposite(float composite){
        this.composite = composite;
    }

    protected final void Init() {
    }
    //------------------------------------------metodos privates--------------------------------

    private void MouseClicked(MouseEvent e) throws Throwable{
        boolean next = this.MouseClicked(e, e.getX(), e.getY());
        if(e.getButton() == MouseEvent.BUTTON3) {
            this.Cx = getXReal(e.getX());
            this.Cy = getYReal(e.getY());
            Xt = width / 2 - Cx * zoom;
            Yt = height / 2 - Cy * zoom;
            repaint();
        }else{
            if(next){
                this.MouseClicked(e, getXReal(e.getX()), getYReal(e.getY()));
            }
        }
    }

    private void MouseMoved(MouseEvent e) throws Throwable{
        if (width != getWidth() || height != getHeight()) {
            width = getWidth();
            height = getHeight();
        }
        this.MouseMoved(e, getXReal(e.getX()), getYReal(e.getY()));
    }

    private void MouseWheelMoved(MouseWheelEvent e) throws Throwable{
        if(e.isAltDown() || e.isShiftDown() || e.isControlDown()){
            this.MouseWheelMoved(e, getXReal(e.getX()), getYReal(e.getY()));
        }else{
            if(e.getWheelRotation() < 0){
                if(zoom*taxaZoon < maxZoon){
                    zoom *= taxaZoon;
                    Xt = width / 2 - Cx * zoom;
                    Yt = height / 2 - Cy * zoom;
                    repaint();
                }
            }else if(e.getWheelRotation() > 0){
                if(zoom/taxaZoon > minZoon){
                    zoom /= taxaZoon;
                    Xt = width / 2 - Cx * zoom;
                    Yt = height / 2 - Cy * zoom;
                    repaint();
                }
            }
        }
    }

    private Cursor before = null;
    private void MousePressed(MouseEvent e) throws Throwable{
        if (e.getButton() == MouseEvent.BUTTON3) {
            this.before = this.getCursor();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            this.dx = getXReal(e.getX());
            this.dy = getYReal(e.getY());
            translated = true;
            repaint();
        }
        MousePressed(e, getXReal(e.getX()), getYReal(e.getY()));
    }

    private void MouseReleased(MouseEvent e) throws Throwable{
        if(e.getButton() == MouseEvent.BUTTON3){
            this.setCursor(before);
        }
        translated = false;
        MouseReleased(e, getXReal(e.getX()), getYReal(e.getY()));
    }

    private void MouseDragged(MouseEvent e) throws Throwable{
        if (translated) {
            //this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            this.Cx += dx - getXReal(e.getX());
            this.Cy += dy - getYReal(e.getY());
            this.dx = getXReal(e.getX());
            this.dy = getYReal(e.getY());

            Xt = width / 2 - Cx * zoom;
            Yt = height / 2 - Cy * zoom;
            repaint();
        }else{
            MouseDragged(e, getXReal(e.getX()), getYReal(e.getY()));
        }
    }

    //------------------------------------------metodos publics e protecteds-------------------------------
    /**
     * @return retorna um ponto X com as cordenadas reais, de onde se clicou apos ter calculado o zoon
     */
    public double getXReal(double X) {
        return this.Cx + (X - width / 2) / zoom;
    }

    /**
     * @return retorna um ponto Y com as cordenadas reais, de onde se clicou apos ter calculado o zoon
     */
    public double getYReal(double Y) {
        return this.Cy + (Y - height / 2) / zoom;
    }
    
    public double getXOrg(double X){
        return (X-Cx)*zoom + width/2;
    }
    public double getYOrg(double Y){
        return (Y-Cy)*zoom + height/2;
    }

    public void setCenter(double Cx, double Cy) {
        this.Cx = Cx;
        this.Cy = Cy;
        Xt = width / 2 - Cx * zoom;
        Yt = height / 2 - Cy * zoom;
        repaint();
    }
    public void restart_system() {
        this.Cx = 0;
        this.Cy = 0;
        dx = 0;
        dy = 0;
        zoom = 1;
        Xt = width / 2 - Cx * zoom;
        Yt = height / 2 - Cy * zoom;
        repaint();
    }

    protected final static BasicStroke stroke = new BasicStroke(1);
    protected final static Font fontBase = new Font(Font.MONOSPACED, Font.PLAIN, 11);
    protected final static Font fontRuler = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;


        if(anti_aliasing){
            g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        }
        if(composite<1.0f){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, composite));
        }
        g2.setColor(Color.BLACK);
        g2.setStroke(stroke);
        g2.setFont(fontBase);
        g2.translate(Xt, Yt);
        g2.scale(zoom, zoom);
        paintDynamicScene(g2);

        g2.setColor(Color.BLACK);
        g2.setFont(fontBase);
        g2.setStroke(stroke);
        g2.scale(1.0 / zoom, 1.0 / zoom);
        g2.translate(-Xt, -Yt);
        g2.translate(0, 0);
        paintStaticFirst(g2);
        
        g2.setColor(Color.BLACK);
        g2.setFont(fontBase);
        g2.setStroke(stroke);
        g2.translate(Xt, 0.0);
        g2.scale(zoom, zoom);
        paintDynamicTop(g2);
        
        g2.setColor(Color.BLACK);
        g2.setFont(fontBase);
        g2.setStroke(stroke);
        g2.scale(1.0 / zoom, 1.0 / zoom);
        g2.translate(-Xt, 0.0);
        g2.translate(0.0, +Yt);
        g2.scale(zoom, zoom);
        paintDynamicLeft(g2);
        
        
        g2.setColor(Color.BLACK);
        g2.setFont(fontBase);
        g2.setStroke(stroke);
        g2.scale(1.0 / zoom, 1.0 / zoom);
        g2.translate(0.0, -Yt);
        paintStaticLast(g2);

    }
    
    protected void paintRuler(Graphics2D g2, int pixels_for_unit) {
        g2.setFont(fontRuler);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, width(), 40);
        g2.fillRect(0, 0, 40, height());
        g2.setColor(Color.black);
        g2.drawRect(0, 0, width(), 40);
        g2.drawRect(0, 0, 40, height());
        
        drawScaleNumeric(g2, width(), Cx(), true, pixels_for_unit);
        drawScaleNumeric(g2, height(), Cy(), false, pixels_for_unit);

        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, 40, 40);
        g2.setColor(Color.black);
        g2.drawRect(0, 0, 40, 40);
    }
    
    private void drawNumericX(Graphics2D g2, String strM, String strC, double sx){
        drawNumericX(g2, strM, strC, sx, fontRuler);  
    }
    private void drawNumericX(Graphics2D g2, String strM, String strC, double sx, Font font){
        Rectangle2D rect = fontRuler.getStringBounds(strM, g2.getFontRenderContext());
        if(strC!=null){
            g2.drawString(strM, (int) (sx - rect.getWidth()), 20);
            g2.setFont(fontBase);
            g2.drawString(strC, (int) (sx), 20);
            g2.setFont(fontRuler);
        }else{
            g2.setFont(font);
            g2.drawString(strM, (int) (sx - rect.getWidth()/2), 20);
            g2.setFont(fontRuler);
        }
    }
    private void drawNumericY(Graphics2D g2, String strM, String strC, int sy){
        drawNumericY(g2, strM, strC, sy, fontRuler);  
    }
    private void drawNumericY(Graphics2D g2, String strM, String strC, int sy, Font font){
        Rectangle2D rect = fontRuler.getStringBounds(strM, g2.getFontRenderContext());
        if(strC!=null){
            g2.drawString(strM, 5, sy-4);
            g2.setFont(fontBase);
            g2.drawString(strC, 15, sy+12);
            g2.setFont(fontRuler);
        }else{
            g2.setFont(font);
            g2.drawString(strM, 5, sy-4);
            g2.setFont(fontRuler);
        }
    }
    private void drawScaleNumeric(Graphics2D g2, int dimension, double center, boolean isX, int pixels_for_unit) {
        double delta = zoom();

        int N = (int)(Math.log10(delta/500));
        N += (delta>=500 ? 1 : 0);
        double incr = Math.pow(10, N);//(int) (Math.log10(delta));

        delta = delta / incr;

        long i = (long) ((40 - dimension / 2 + center * zoom() ) / delta);
        while ((long) (dimension / 2 - center * zoom() + i * delta) > 40) {
            i--;
        }
        while ((long) (dimension / 2 - center * zoom() + i * delta) < 40) {
            i++;
        }
  
        while ((int) (dimension / 2 - center * zoom() + i * delta) < dimension) {
            g2.setColor(Color.BLACK);
            if (isX) {
                g2.drawLine((int) (dimension / 2 - center * zoom() + i * delta), 40 - 1,
                        (int) (dimension / 2 - center * zoom() + i * delta), 30 - 1);
                g2.setColor(Color.GRAY);
                g2.drawLine((int) (dimension / 2 - center * zoom() + i * delta) + 1, 40 - 1,
                        (int) (dimension / 2 - center * zoom() + i * delta) + 1, 30 - 1);
                g2.setColor(Color.BLACK);
                double val = (i*100.0)/(incr*pixels_for_unit);
                
                double sx = dimension / 2 - center * zoom() + i * delta;
                String strM = String.format(Locale.ENGLISH, "%3.0f", val/100);
                    
                if(incr > 2e3){
                    long meter = (long)(val/100);
                    long micro = Math.abs((long)(val*10000-meter*1000000));
                    String strC = String.format(Locale.ENGLISH, "%06d", micro);
                    drawNumericX(g2, strM, strC, sx);
                }else if(incr>2e2){
                    long meter = (long)(val/100);
                    long milli = Math.abs((long)(val*10-meter*1000));
                    String strC = String.format(Locale.ENGLISH, "%03d", milli);
                    drawNumericX(g2, strM, strC, sx);  
                }else if(incr>5){
                    long meter = (long)(val/100);
                    long cent = Math.abs((long)(val-meter*100));
                    String strC = String.format(Locale.ENGLISH, "%02d", cent);
                    drawNumericX(g2, strM, strC, sx);  
                }else if(incr>2e-5){
                    drawNumericX(g2, strM, null, sx);                
                }else{
                    drawNumericX(g2, strM, null, sx, fontBase);                
                }
            } else {
                g2.drawLine(30 - 1, (int) (dimension / 2 - center * zoom() + i * delta),
                        40 - 1, (int) (dimension / 2 - center * zoom() + i * delta));
                g2.setColor(Color.GRAY);
                g2.drawLine(30 - 1, (int) (dimension / 2 - center * zoom() + i * delta) + 1,
                        40 - 1, (int) (dimension / 2 - center * zoom() + i * delta) + 1);
                g2.setColor(Color.BLACK);
                
                double val = (i*100.0)/(incr*pixels_for_unit);
                
                int sy = (int) (dimension / 2 - center * zoom() + i * delta);
                String strM = String.format(Locale.ENGLISH, "%3.0f", val/100);
                    
                if(incr > 2e3){
                    long meter = (long)(val/100);
                    long micro = Math.abs((long)(val*10000-meter*1000000));
                    String strC = String.format(Locale.ENGLISH, "%06d", micro);
                    drawNumericY(g2, strM, strC, sy);
                }else if(incr>2e2){
                    long meter = (long)(val/100);
                    long milli = Math.abs((long)(val*10-meter*1000));
                    String strC = String.format(Locale.ENGLISH, "%03d", milli);
                    drawNumericY(g2, strM, strC, sy);  
                }else if(incr>5){
                    long meter = (long)(val/100);
                    long cent = Math.abs((long)(val-meter*100));
                    String strC = String.format(Locale.ENGLISH, "%02d", cent);
                    drawNumericY(g2, strM, strC, sy);  
                }else if(incr>2e-5){
                    drawNumericY(g2, strM, null, sy);                
                }else{
                    drawNumericY(g2, strM, null, sy, fontBase);                
                }
            }
            i++;
        }
    }

    
    //------------------------------------------metodos abstratos-----------------------------
    /**
     * @param e MouseEvent
     * @param X Posição X real, de onde se clicou, considerando o zoon
     * @param Y Posição Y real, de onde se clicou, considerando o zoon
     * @throws java.lang.Throwable
     */
    protected void MouseClicked(MouseEvent e, double X, double Y) throws Throwable{
    }
    protected boolean MouseClicked(MouseEvent e, int X, int Y) throws Throwable{
        return true;
    }
    /**
     * @param e MouseEvent
     * @param X Posição X real, de onde se clicou, considerando o zoon
     * @param Y Posição Y real, de onde se clicou, considerando o zoon
     * @throws java.lang.Throwable
     */
    protected void MouseMoved(MouseEvent e, double X, double Y) throws Throwable{
    }
    

    /**
     * @param e MouseEvent
     * @param X Posição X real, de onde se clicou, considerando o zoon
     * @param Y Posição Y real, de onde se clicou, considerando o zoon
     * @throws java.lang.Throwable
     */
    protected void MouseWheelMoved(MouseWheelEvent e, double X, double Y) throws Throwable{
    }

    protected void MousePressed(MouseEvent e, double X, double Y) throws Throwable{
    }

    protected void MouseReleased(MouseEvent e, double X, double Y) throws Throwable{
    }
    protected void MouseDragged(MouseEvent e, double X, double Y) throws Throwable{
    }



    /**Pinta no Graphics antes da mudança de cordenadas devido a translação e o zoon
     * que será aplicado.
     * @param g2
     */
    protected void paintStaticLast(Graphics2D g2) {
    }

    /**Pinta no Graphics depois da mudança de cordenadas devido a translação e o zoon
     * que será aplicado.(apos)<Br>
     * g2.translate(width/2-Cx*zoon, height/2-Cy*zoon);<br>
     * g2.scale(zoon, zoon);<br>
     * @param g2
     */
    protected void paintDynamicScene(Graphics2D g2) {
    }
    protected void paintStaticFirst(Graphics2D g2) {
    }
    protected void paintDynamicTop(Graphics2D g2) {
    }

    protected void paintDynamicLeft(Graphics2D g2) {
        
    }
}
