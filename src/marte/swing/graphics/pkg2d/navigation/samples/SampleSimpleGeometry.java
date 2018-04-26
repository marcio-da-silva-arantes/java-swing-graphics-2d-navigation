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
public class SampleSimpleGeometry {
    public static void main(String args[]){
        sFrame frame = new sFrame("sample simple geometry");
        
        sPanelDraw draw = new sPanelDraw(Color.WHITE) {
            @Override
            protected void paintDynamicScene(Graphics2D g2) {
                g2.setColor(Color.BLUE);
                g2.fillRect(-140, -40, 80, 80);
                
                
                g2.setColor(Color.GREEN);
                g2.fillOval(140, -40, 80, 80);
                g2.setColor(Color.BLACK);
                g2.drawOval(140, -40, 80, 80);
                
                g2.setColor(Color.BLACK);
                String comandos[] = {
                    String.format("1) Utilize o scroll para aproximar(+zoom) ou distanciar os objetos(-zoom): zoom = %g x", zoom),
                    String.format("2) Araste do mouse segurando o botao direito para direcionar onde ficara o centro da cena: Cx = %g e Cy = %g", Cx(), Cy()),
                    String.format("3) Click com o botao direito do mouse para centralizar o zoom sobre um ponto desejado")
                };
                int i=0;
                for(String str : comandos){
                    g2.drawString(str, -350, 80 + i*20);
                    i++;
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
