/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marte.swing.graphics.pkg2d.navigation.samples;

import java.awt.Color;
import java.awt.Graphics2D;
import marte.swing.graphics.pkg2d.navigation.sPanelDraw;
import javax.swing.JFrame;
import marte.swing.graphics.pkg2d.navigation.sFrame;

/**
 *
 * @author Marcio
 */
public class SampleDynamicRuler {
    public static void main(String args[]){
        sFrame frame = new sFrame("sample dynamic ruler");
        
        sPanelDraw draw = new sPanelDraw(Color.WHITE) {
            @Override
            protected void paintStaticLast(Graphics2D g2) {
                g2.drawRect(0, 0, width()-1, height()-1);
                g2.setColor(Color.GREEN);
                g2.fillRect(500, 400, 300, 50);
                g2.setColor(Color.BLACK);
                g2.drawRect(500, 400, 300, 50);
                g2.drawString("This is a static draw", 525, 425);
            }
            @Override
            protected void paintDynamicScene(Graphics2D g2) {
                
                g2.drawString("Draw a matrix 4 x 3 on scene", -300, -212);
                for(int j=0; j<3; j++){
                    for(int i=0; i<4; i++){
                        g2.setColor(Color.ORANGE);
                        g2.fillRect(-300+j*100, -200+i*100, 80, 80);
                        g2.setColor(Color.BLACK);
                        g2.drawRect(-300+j*100, -200+i*100, 80, 80);
                    }
                }
                
                for(int j=0; j<3; j++){
                    g2.drawOval(-270+j*100, -250, 20, 20);
                    g2.drawString(""+j, -270+j*100+7, -250+12);
                }
                for(int i=0; i<4; i++){
                    g2.drawOval(-370, -170+i*100, 20, 20);
                    g2.drawString(""+i, -370+7, -170+i*100+12);
                }
            }

            @Override
            protected void paintStaticFirst(Graphics2D g2) {
                g2.setColor(Color.lightGray);
                g2.fillRect(0, 0, width()-1, (int)(50*zoom));
                g2.fillRect(0, 0, (int)(50*zoom), height()-1);
                g2.setColor(Color.BLACK);
                g2.drawRect(0, 0, width()-1, (int)(50*zoom));
                g2.drawRect(0, 0, (int)(50*zoom), height()-1);
                g2.setColor(Color.BLUE);
                g2.drawString("This is a static background, can be used to draw a menu to navegation", 75, 15);
            }
            @Override
            protected void paintDynamicTop(Graphics2D g2) {
                for(int j=0; j<3; j++){
                    g2.drawOval(-270+j*100, 25, 20, 20);
                    g2.drawString(""+j, -270+j*100+7, 25+12);
                }
            }

            @Override
            protected void paintDynamicLeft(Graphics2D g2) {
                for(int i=0; i<4; i++){
                    g2.drawOval(15, -170+i*100, 20, 20);
                    g2.drawString(""+i, 15+7, -170+i*100+12);
                }
            }
        };
        
        
        frame.add(draw);
        
        frame.setSize(1000, 700);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        draw.Config(900, 600);
        draw.restart_system();
        draw.repaint();
    }
    
}
