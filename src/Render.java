import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.image.*;
import java.awt.image.renderable.RenderContext;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.StrokeBorder;

import java.awt.geom.*;

import java.util.*;
import java.net.URI;
import java.text.SimpleDateFormat;

class Render extends JPanel implements ActionListener {
	private Timer timer;

	Graphics2D g2d;
	static Font font;
	
	static ArrayList<Image> Background = new ArrayList<Image>();
	static Image Stick;
	
	double frame = 0;
	boolean FrameDown = false;
	
	static double Scale = 1;
	
	long PreviousFrameTime = System.currentTimeMillis();
	
	int Timeout = 0;
	
	public Render() {
		initTimer();
	}
	
	private void initTimer() {
		
		timer = new Timer(0, this);
		timer.start();
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	private void doDrawing(Graphics g) {
		try {
			Thread.sleep(15 - (int)(System.currentTimeMillis() - PreviousFrameTime));
		}catch (Exception e) {}
		PreviousFrameTime = System.currentTimeMillis();
		
		g2d = (Graphics2D)g;

		g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

		g2d.drawImage(Background.get((int)frame), 0, 0, Core.WindowWidth, Core.WindowHeight, null);
		
		double TempStickX = Core.StickX;
		double TempStickY = Core.StickY;
		double TempStickAngle = Math.toRadians(Core.StickAngle);
		
		g2d.rotate(TempStickAngle, TempStickX + 3, TempStickY - 70);
		g2d.drawImage(Stick, (int)(TempStickX - 20.0/Math.sqrt(Core.StickZ)), (int)(TempStickY - 100.0/Math.sqrt(Core.StickZ)), (int)(40.0/Math.sqrt(Core.StickZ)), (int)(100.0/Math.sqrt(Core.StickZ)), null);
		g2d.rotate(-1*TempStickAngle, TempStickX + 3, TempStickY - 70);
		
		double PetZscale = 1.0/Math.sqrt(Core.PetZ);
		double TempPetX = Core.PetX;
		double TempPetY = Core.PetY;
		double TempEyesX = Core.EyesX;
		double TempEyesY = Core.EyesY;
		double TempMouthX = Core.MouthX;
		double TempMouthY = Core.MouthY;
		
		g2d.setStroke(new BasicStroke((float)(6*PetZscale/**Scale*/))); //TODO experiment with scale
		
		double Temprotation = Math.toRadians(Core.HeadAngle);
		double TempHeadYOffset =  -Core.HeadYOffset*PetZscale;
		g2d.rotate(Temprotation, TempPetX, TempPetY);
		
		drawOval(g2d, TempPetX, TempPetY+TempHeadYOffset, 150*PetZscale, 150*PetZscale); // Body
		
		if (Core.Petting) {
			if (Core.PettingValue > 100/Math.sqrt(Core.PetZ)) {
				g2d.drawLine((int)(TempPetX-(15-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale), (int)(TempPetX-(45-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(15-TempEyesY)*PetZscale));
				g2d.drawLine((int)(TempPetX+(15+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale), (int)(TempPetX+(45+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(15-TempEyesY)*PetZscale));
			} else {
				g2d.drawLine((int)(TempPetX-(15-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(15-TempEyesY)*PetZscale), (int)(TempPetX-(45-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale));
				g2d.drawLine((int)(TempPetX+(15+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(15-TempEyesY)*PetZscale), (int)(TempPetX+(45+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale));
			}
			
		} else if (Core.Emotion == 4) { // sleep
			///*
			g2d.drawLine((int)(TempPetX-(15-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(15-TempEyesY)*PetZscale), (int)(TempPetX-(45-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale));
			g2d.drawLine((int)(TempPetX+(15+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(15-TempEyesY)*PetZscale), (int)(TempPetX+(45+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale));
			//*/
			/*
			g2d.setClip(0,(int)(TempPetY+TempHeadYOffset-(18-TempEyesY)*PetZscale),Core.WindowWidth,Core.WindowHeight);
			drawOval(g2d, TempPetX-(30-TempEyesX)*PetZscale, TempPetY+TempHeadYOffset+(-24-TempEyesY)*PetZscale, 35*PetZscale, 35*PetZscale); // Right Eye
			drawOval(g2d, TempPetX+(30+TempEyesX)*PetZscale, TempPetY+TempHeadYOffset+(-24-TempEyesY)*PetZscale, 35*PetZscale, 35*PetZscale); // Left Eye
			g2d.setClip(0,0,Core.WindowWidth,Core.WindowHeight);
			//*/
		} else if (Core.BlinkTick < 0) {
			g2d.drawLine((int)(TempPetX-(15-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(15-TempEyesY)*PetZscale), (int)(TempPetX-(45-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale));
			g2d.drawLine((int)(TempPetX+(15+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(15-TempEyesY)*PetZscale), (int)(TempPetX+(45+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale));
		} else if (Core.Emotion == 3) { // joy
			g2d.setClip(0,0,Core.WindowWidth,(int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale));
			drawOval(g2d, TempPetX-(30-TempEyesX)*PetZscale, TempPetY+TempHeadYOffset-(7-TempEyesY)*PetZscale, 35*PetZscale, 35*PetZscale); // Right Eye
			drawOval(g2d, TempPetX+(30+TempEyesX)*PetZscale, TempPetY+TempHeadYOffset-(7-TempEyesY)*PetZscale, 35*PetZscale, 35*PetZscale); // Left Eye
		} else if (Core.Emotion == 2) {
			g2d.setClip(0,0,Core.WindowWidth,(int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale));
			drawOval(g2d, TempPetX-(30-TempEyesX)*PetZscale, TempPetY+TempHeadYOffset-(7-TempEyesY)*PetZscale, 35*PetZscale, 35*PetZscale); // Right Eye
			drawOval(g2d, TempPetX+(30+TempEyesX)*PetZscale, TempPetY+TempHeadYOffset-(7-TempEyesY)*PetZscale, 35*PetZscale, 35*PetZscale); // Left Eye
		} else if (Core.Emotion == 1) {
			g2d.drawLine((int)(TempPetX-(15-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(25-TempEyesY)*PetZscale), (int)(TempPetX-(45-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale)); // Right Eye
			g2d.drawLine((int)(TempPetX+(15+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(25-TempEyesY)*PetZscale), (int)(TempPetX+(45+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale)); // Left Eye
		} else if (Core.Emotion == 0) { // sad
			g2d.drawLine((int)(TempPetX-(15-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(25-TempEyesY)*PetZscale), (int)(TempPetX-(45-TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale)); // Right Eye
			g2d.drawLine((int)(TempPetX+(15+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(25-TempEyesY)*PetZscale), (int)(TempPetX+(45+TempEyesX)*PetZscale), (int)(TempPetY+TempHeadYOffset-(10-TempEyesY)*PetZscale)); // Left Eye
		}
		
		if (Core.Petting) {
			if (Core.PettingValue > 100/Math.sqrt(Core.PetZ)) {
				g2d.setClip(0,(int)(TempPetY+TempHeadYOffset+(20+TempMouthY)*PetZscale),Core.WindowWidth,Core.WindowHeight);
				fillOval(g2d, TempPetX+TempMouthX*PetZscale, TempPetY+TempHeadYOffset+(15+TempMouthY)*PetZscale, 80*PetZscale, 80*PetZscale); // Mouth
				g2d.setClip(0,0,Core.WindowWidth,Core.WindowHeight);
			} else {
				g2d.setClip(0,(int)(TempPetY+(25+TempMouthY)*PetZscale+TempHeadYOffset),Core.WindowWidth,Core.WindowHeight);
				drawOval(g2d, TempPetX+TempMouthX*PetZscale, TempPetY+(25+TempMouthY)*PetZscale+TempHeadYOffset, 40*PetZscale, 30*PetZscale); // Mouth
				g2d.setClip(0,0,Core.WindowWidth,Core.WindowHeight);
			}
		} else if (Core.Emotion == 4) { // sleep
			fillOval(g2d, TempPetX+TempMouthX*PetZscale, TempPetY+(25+TempMouthY)*PetZscale+TempHeadYOffset, 30*PetZscale, 20*PetZscale); // Mouth
			/*g2d.setClip(0, 0 , Core.WindowWidth, (int)(TempPetY+(35+TempMouthY)*PetZscale+TempHeadYOffset));
			fillOval(g2d, TempPetX+TempMouthX*PetZscale, TempPetY+(35+TempMouthY)*PetZscale+TempHeadYOffset, 30*PetZscale, 40*PetZscale); // Mouth
			g2d.setClip(0,0,Core.WindowWidth,Core.WindowHeight);*/
		} else if (Core.Emotion == 3) { // joy
			g2d.setClip(0,(int)(TempPetY+TempHeadYOffset+(20+TempMouthY)*PetZscale),Core.WindowWidth,Core.WindowHeight);
			fillOval(g2d, TempPetX+TempMouthX*PetZscale, TempPetY+TempHeadYOffset+(15+TempMouthY)*PetZscale, 80*PetZscale, 80*PetZscale); // Mouth
			g2d.setClip(0,0,Core.WindowWidth,Core.WindowHeight);
		} else if (Core.Emotion == 2) {
			g2d.setClip(0,(int)(TempPetY+(25+TempMouthY)*PetZscale+TempHeadYOffset),Core.WindowWidth,Core.WindowHeight);
			drawOval(g2d, TempPetX+TempMouthX*PetZscale, TempPetY+(25+TempMouthY)*PetZscale+TempHeadYOffset, 40*PetZscale, 30*PetZscale); // Mouth
			g2d.setClip(0,0,Core.WindowWidth,Core.WindowHeight);
		} else if (Core.Emotion == 1) {
			g2d.setClip(0,(int)(TempPetY+(25+TempMouthY)*PetZscale+TempHeadYOffset),Core.WindowWidth,Core.WindowHeight);
			drawOval(g2d, TempPetX+TempMouthX*PetZscale, TempPetY+(25+TempMouthY)*PetZscale+TempHeadYOffset, 40*PetZscale, 30*PetZscale); // Mouth
			g2d.setClip(0,0,Core.WindowWidth,Core.WindowHeight);
		} else if (Core.Emotion == 0) { // sad
			g2d.setClip(0,0,Core.WindowWidth,(int)(TempPetY+TempHeadYOffset+(40+TempMouthY)*PetZscale));
			drawOval(g2d, TempPetX+TempMouthX*PetZscale, TempPetY+TempHeadYOffset+(40+TempMouthY)*PetZscale, 40*PetZscale, 50*PetZscale); // Mouth
			g2d.setClip(0,0,Core.WindowWidth,Core.WindowHeight);
		}
		g2d.rotate(-1*Temprotation, TempPetX, TempPetY);

		fillOval(g2d, TempPetX+(Core.RHandX*PetZscale), TempPetY+(Core.RHandY*PetZscale), 45*PetZscale, 45*PetZscale); // Right Hand
		fillOval(g2d, TempPetX+(Core.LHandX*PetZscale), TempPetY+(Core.LHandY*PetZscale), 45*PetZscale, 45*PetZscale); // Left Hand

		double TempREarAngle = Math.toRadians(Core.REarAngle);
		double TempLEarAngle = Math.toRadians(Core.LEarAngle);
		
		int[] XRo = {(int)(TempPetX + 125*PetZscale*Math.sin(TempREarAngle)-TempHeadYOffset*Math.sin(Temprotation)), (int)(TempPetX + 82*PetZscale*Math.sin(TempREarAngle) + 20*PetZscale*Math.cos(TempREarAngle)-TempHeadYOffset*Math.sin(Temprotation)), (int)(TempPetX + 82*PetZscale*Math.sin(TempREarAngle) - 20*PetZscale*Math.cos(TempREarAngle)-TempHeadYOffset*Math.sin(Temprotation))};
		int[] YRo = {(int)(TempPetY - 125*PetZscale*Math.cos(TempREarAngle)+TempHeadYOffset*Math.cos(Temprotation)), (int)(TempPetY - 82*PetZscale*Math.cos(TempREarAngle) + 20*PetZscale*Math.sin(TempREarAngle)+TempHeadYOffset*Math.cos(Temprotation)), (int)(TempPetY - 82*PetZscale*Math.cos(TempREarAngle) - 20*PetZscale*Math.sin(TempREarAngle)+TempHeadYOffset*Math.cos(Temprotation))};
		int[] XRi = {(int)(TempPetX + 110*PetZscale*Math.sin(TempREarAngle)-TempHeadYOffset*Math.sin(Temprotation)), (int)(TempPetX + 82*PetZscale*Math.sin(TempREarAngle) + 12*PetZscale*Math.cos(TempREarAngle)-TempHeadYOffset*Math.sin(Temprotation)), (int)(TempPetX + 82*PetZscale*Math.sin(TempREarAngle) - 12*PetZscale*Math.cos(TempREarAngle)-TempHeadYOffset*Math.sin(Temprotation))};
		int[] YRi = {(int)(TempPetY - 110*PetZscale*Math.cos(TempREarAngle)+TempHeadYOffset*Math.cos(Temprotation)), (int)(TempPetY - 82*PetZscale*Math.cos(TempREarAngle) + 12*PetZscale*Math.sin(TempREarAngle)+TempHeadYOffset*Math.cos(Temprotation)), (int)(TempPetY - 82*PetZscale*Math.cos(TempREarAngle) - 12*PetZscale*Math.sin(TempREarAngle)+TempHeadYOffset*Math.cos(Temprotation))};
		int[] XLo = {(int)(TempPetX - 125*PetZscale*Math.sin(TempLEarAngle)-TempHeadYOffset*Math.sin(Temprotation)), (int)(TempPetX - 82*PetZscale*Math.sin(TempLEarAngle) - 20*PetZscale*Math.cos(TempLEarAngle)-TempHeadYOffset*Math.sin(Temprotation)), (int)(TempPetX - 82*PetZscale*Math.sin(TempLEarAngle) + 20*PetZscale*Math.cos(TempLEarAngle)-TempHeadYOffset*Math.sin(Temprotation))};
		int[] YLo = {(int)(TempPetY - 125*PetZscale*Math.cos(TempLEarAngle)+TempHeadYOffset*Math.cos(Temprotation)), (int)(TempPetY - 82*PetZscale*Math.cos(TempLEarAngle) + 20*PetZscale*Math.sin(TempLEarAngle)+TempHeadYOffset*Math.cos(Temprotation)), (int)(TempPetY - 82*PetZscale*Math.cos(TempLEarAngle) - 20*PetZscale*Math.sin(TempLEarAngle)+TempHeadYOffset*Math.cos(Temprotation))};
		int[] XLi = {(int)(TempPetX - 110*PetZscale*Math.sin(TempLEarAngle)-TempHeadYOffset*Math.sin(Temprotation)), (int)(TempPetX - 82*PetZscale*Math.sin(TempLEarAngle) - 12*PetZscale*Math.cos(TempLEarAngle)-TempHeadYOffset*Math.sin(Temprotation)), (int)(TempPetX - 82*PetZscale*Math.sin(TempLEarAngle) + 12*PetZscale*Math.cos(TempLEarAngle)-TempHeadYOffset*Math.sin(Temprotation))};
		int[] YLi = {(int)(TempPetY - 110*PetZscale*Math.cos(TempLEarAngle)+TempHeadYOffset*Math.cos(Temprotation)), (int)(TempPetY - 82*PetZscale*Math.cos(TempLEarAngle) + 12*PetZscale*Math.sin(TempLEarAngle)+TempHeadYOffset*Math.cos(Temprotation)), (int)(TempPetY - 82*PetZscale*Math.cos(TempLEarAngle) - 12*PetZscale*Math.sin(TempLEarAngle)+TempHeadYOffset*Math.cos(Temprotation))};
		g2d.drawPolygon(XRo, YRo, 3);
		g2d.drawPolygon(XLo, YLo, 3);
		g2d.setStroke(new BasicStroke((float)(3*PetZscale*Scale)));
		g2d.drawPolygon(XRi, YRi, 3);
		g2d.drawPolygon(XLi, YLi, 3);
		
		if (FrameDown) {
			if (frame > 0) {
				frame -= 0.2;
			} else {
				FrameDown = false;
			}
		} else {
			if (frame < Background.size()-1) {
				frame += 0.2;
			} else {
				FrameDown = true;
			}
		}
		/*
		g2d.scale(Scale, Scale);
		
		g2d.setColor(Color.WHITE);*/
	}
	
	public void drawOval(Graphics2D g2d, double X, double Y, double Xsize, double Ysize) {
		g2d.drawOval((int)Math.round(X-0.5*(Xsize)), (int)Math.round(Y-0.5*(Ysize)), (int)Math.round(Xsize), (int)Math.round(Ysize));
	}
	
	public void fillOval(Graphics2D g2d, double X, double Y, double Xsize, double Ysize) {
		g2d.fillOval((int)Math.round(X-0.5*(Xsize)), (int)Math.round(Y-0.5*(Ysize)), (int)Math.round(Xsize), (int)Math.round(Ysize));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}