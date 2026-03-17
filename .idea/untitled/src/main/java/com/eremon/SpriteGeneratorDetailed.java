package com.eremon;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SpriteGeneratorDetailed {
    public static void main(String[] args) {
        int width = 126;
        int height = 186;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        // pixel-art style : disable smoothing
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // background transparent
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0,0,width,height);
        g.setComposite(AlphaComposite.SrcOver);

        // couleurs de base
        Color skin = new Color(181, 7, 96);
        Color robe = new Color(60, 50, 45);
        Color accent = new Color(26, 2, 2);   // tâche ou sang
        Color outline = new Color(15, 15, 15);

        int cx = width/2;

        // silhouette et vêtements
        g.setColor(robe);
        g.fillRect(cx-20, height-90, 40, 70);           // corps
        g.setColor(skin);
        g.fillOval(cx-14, height-130, 28, 28);          // tête
        // bras
        g.setColor(skin);
        g.fillRect(cx-30, height-90, 8, 50);
        g.fillRect(cx+22, height-90, 8, 50);
        // jambes
        g.setColor(robe.darker());
        g.fillRect(cx-18, height-20, 12, 20);
        g.fillRect(cx+6, height-20, 12, 20);

        g.setColor(accent);
        g.fillRect(cx-5, height-110, 4, 10);             // marque sur visage
        for(int i=0;i<500;i++){
            int x = (int)(Math.random()*width);
            int y = (int)(Math.random()*height);
            int a = 50 + (int)(Math.random()*50);
            g.setColor(new Color(20,20,20,a));
            g.fillRect(x,y,1,1);
        }

        g.setColor(outline);
        g.drawRect(0,0,width-1,height-1);

        g.dispose();

        try {
            File out = new File("untitled/src/main/resources/image/main-character.png");
            ImageIO.write(img, "png", out);
            System.out.println("✅ Sprite détaillé enregistré : " + out.getAbsolutePath());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
