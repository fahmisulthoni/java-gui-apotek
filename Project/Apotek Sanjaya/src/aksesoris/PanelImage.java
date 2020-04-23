/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aksesoris;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author MhdSyarif
 * Thursday, 30 January 2014, 02 : 03 : 50 WIB  
 * Tugas Akhir Matakuliah Java2SE
 * Mhd. Syarif | 49013075
 * TKJMD - STEI - ITB
 */
    public class PanelImage extends JPanel{
     
        private Image image; // membuat variable image
         public PanelImage() {
            image = new ImageIcon(getClass().getResource("/images/fahmi.jpg")).getImage();
            //memanggil sumber daya gambar
        }
         @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
         
            Graphics gd = (Graphics2D) g.create();
         
            gd.drawImage(image, 0,0,getWidth(),getHeight(), this);
            // menggambar image
            gd.dispose();
        }
    }

