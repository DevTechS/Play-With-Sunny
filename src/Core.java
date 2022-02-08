import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.font.FontRenderContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.geom.*;

import java.util.*;
import java.util.stream.Stream;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

public class Core extends JFrame{
	
	static boolean MousePressed = false;
	static boolean RightMousePressed = false;
	static int MouseX = 0;
	static int MouseY = 0;

	static int LastMouseX = 0;
	static int LastMouseY = 0;
	
	static int MouseDX = 0;
	static int MouseDY = 0;
	
	static int WindowHeight = 900;
	static int WindowWidth = 1200;
	static int WindowX = 0;
	static int WindowY = 0;
	
	static long PreviousFrameTime = System.currentTimeMillis();
	
	static ArrayList<String> Keys = new ArrayList<String>();

	static double StickX = WindowWidth + 50;
	static double StickY = WindowHeight - 50;
	static double StickZ = 1;
	static double StickAngle = 45;
	static double StickXGoal = WindowWidth - 50;
	static double StickYGoal = WindowHeight - 50;
	static double StickZGoal = 1;
	static double StickAngleGoal = 45;
	static double StickDX = 0;
	static double StickDY = 0;
	static double StickDZ = 0;
	static double StickDAngle = 0;
	
	static int StickState = 0;

	static boolean Petting = false;
	static double PettingValue = 0;
	
	static int Emotion = 2; // 0-3; 
	
	static double PetX = WindowWidth/2;
	static double PetY = WindowHeight/2;
	static double PetZ = 1;
	static double PetXGoal = WindowWidth/2;
	static double PetYGoal = WindowHeight/2;
	static double PetZGoal = 1;
	
	static double XTarget = WindowWidth/2;
	static double YTarget = WindowHeight/2;
	static double ZTarget = 1;
	
	static double WalkingSpeed = 1;

	static double LHandX = -75;
	static double LHandY = 50;
	static double LHandXGoal = -75;
	static double LHandYGoal = 50;
	static double RHandX = 75;
	static double RHandY = 50;
	static double RHandXGoal = 75;
	static double RHandYGoal = 50;

	static double EyesX = 0;
	static double EyesY = 0;
	static double EyesXGoal = 0;
	static double EyesYGoal = 0;

	static double MouthX = 0;
	static double MouthY = 0;
	static double MouthXGoal = 0;
	static double MouthYGoal = 0;

	static double REarAngle = 35;
	static double REarAngleGoal = 35;
	static double LEarAngle = 35;
	static double LEarAngleGoal = 35;

	static double HeadYOffset = 10;
	static double HeadYOffsetGoal = 0;
	static double HeadAngle = 0;
	static double HeadAngleGoal = 0;

	public static long Tick;
	
	public static int BlinkTick = 240;
	
	public static int Stage = 3;

	public static int SleepStage = 0;
	
	public static boolean Goodbye = false;
	
	public static double MouseAngle = 0;
	
	public Core() {
		initUI();
	}
	
	public static void main(String[] args) {
		Start();
	}
	
	private void initUI() {
		
		final Render Render = new Render();
		add(Render);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Timer timer = Render.getTimer();
				timer.stop();
			}
		});
		setTitle("Play With Sunny");
		setSize(WindowWidth, WindowHeight);
		setBackground(Color.WHITE);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setExtendedState(0);
		setUndecorated(false);
		setFocusTraversalKeysEnabled(false);
		setAlwaysOnTop(false);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		//setBounds(-7, -13, Toolkit.getDefaultToolkit().getScreenSize().width+15, Toolkit.getDefaultToolkit().getScreenSize().height+62);
		addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				MousePressed = false;
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				WindowHeight = getHeight();
				WindowWidth = getWidth();
				Render.Scale = WindowHeight/1000.0;
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				WindowX = (int)getLocation().getX();
				WindowY = (int)getLocation().getY();
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				
			}
		});
		addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent me) {
				if (me.getButton() == 1) {
					MousePressed = true;
				} else if (me.getButton() == 3) {
					RightMousePressed = true;
				}
			}
	        public void mouseReleased(MouseEvent me) {
	        	if (me.getButton() == 1) {
					MousePressed = false;
				} else if (me.getButton() == 3) {
					RightMousePressed = false;
				}
	        }
	        public void mouseEntered(MouseEvent me) {
	        	MousePressed = false;
	        }
	        public void mouseExited(MouseEvent me) {
	        	MousePressed = false;
	        }
	        public void mouseClicked(MouseEvent me) {
	        	
	        }
	    });
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent me) {
				MouseX = me.getX();
				MouseY = me.getY();
			}
			public void mouseMoved(MouseEvent me) {
				MouseX = me.getX();
				MouseY = me.getY();
			}
	    });
		
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				
			}
			public void keyReleased(KeyEvent e) {
				Keys.remove(String.valueOf(e.getKeyChar()));
			}
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				if (e.getKeyCode() == 27) {
					Thread ByeBye = new Thread() {
						public void run() {
							Goodbye = true;
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							System.exit(0);
						}
					};
					ByeBye.start();
				}
				if (!Keys.contains(String.valueOf(e.getKeyChar()))) {
					Keys.add(String.valueOf(e.getKeyChar()));
				}
			}
		});
	}
	public static void Start() {
		Thread FileLoader = new Thread() {
			public void run() {
				try {
					//Render.font = Font.createFont(Font.TRUETYPE_FONT, new File("arial.ttf"));
					Render.Stick = ImageIO.read(new File("Stick.png"));
				} catch (Exception e) {}
			}
		};
		Thread BackgroundLoader = new Thread() {
			public void run() {
				try {
					int i = 1;
					while (true) {
						String num = String.valueOf(i);
						while (num.length() < 5) {
							num = "0" + num;
						}
						Render.Background.add(ImageIO.read(new File("Background/Background" + num + ".png")));
						i += 4;
					}
				}catch (Exception e) {}
			}
		};
		Thread WindowThread = new Thread() {
			public void run() {
				Core ex = new Core();
				ex.setVisible(true);
			}
		};
		BackgroundLoader.start();
		FileLoader.start();
		while (FileLoader.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				
			}
		}
		WindowThread.start();
		while (WindowThread.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				
			}
		}
		Render.Scale = WindowHeight/1000.0;
		if (Stage == 0) {
			PetX = -50;
			PetZ = 10;
			PetXGoal = -50;
			PetZGoal = 10;
			ZTarget = 10;
			Emotion = 0;
		}
		try {
			while (MousePressed) {
				Thread.sleep(10);
			}
			while (!MousePressed) {
				Thread.sleep(10);
			}
		} catch (Exception e) {
			
		}
		while (true) {
			Tick++;
			if (BlinkTick < -3-2*Math.random()) {
				BlinkTick = (int)(100 + 140*Math.random());
			} else {
				BlinkTick--;
			}
			try {
				Thread.sleep(15 - (int)(System.currentTimeMillis() - PreviousFrameTime));
			}catch (Exception e) {}
			PreviousFrameTime = System.currentTimeMillis();
			
			if (Stage == 0) {
				WalkingSpeed = 1;
				Walking();
				if (XTarget - PetX < 100) {
					Emotion = 1;
				}
				if (PetX > WindowWidth/2-4) {
					Stage = 1;
					Tick = 0;
					Idle();
				}
			} else if (Stage == 1) {
				Emotion = 3;
				Wave();
				if (Tick > 240) {
					ZTarget = 1;
					Stage = 2;
					Idle();
				}
			} else if (Stage == 2) {
				WalkingSpeed = 1;
				Walking();
				if (Tick > 400) {
					Wave();
				}
				if (Tick > 550) {
					StickXGoal = Toolkit.getDefaultToolkit().getScreenSize().width - 50;
					PetZGoal = 1;
					Stage = 3;
					Emotion = 2;
					Idle();
				}
			} else if (Stage == 3) {
				if (StickState == 0 && MousePressed && Math.sqrt((PetX-MouseX)*(PetX-MouseX)+(PetY-MouseY)*(PetY-MouseY)) < 75/Math.sqrt(PetZ)) {
					Pet();
				} else {
					if (PettingValue > 0 && PetZ > 0.9) {
						WalkingSpeed = 0.2;
						XTarget = WindowWidth/2;
						YTarget = WindowHeight/2;
						ZTarget = 0.2;
					}
					Petting = false;
					PettingValue = 0;
					if (StickState == 1) {
						Emotion = 3;
						FollowStick();
					} else if (StickState == 2 || StickState == 3) {
						Emotion = 2;
						XTarget = StickX;
						if (StickY > WindowHeight/2 + 100) {
							YTarget = StickY;
						} else {
							YTarget = WindowHeight/2;
						}
						ZTarget = StickZ;
						if (StickAngle%360 == 90) {//TODO Fix Jank
							XTarget -= 70;
						} else if (StickAngle%360 == 270) {
							XTarget += 70;
						}
						WalkingSpeed = 1;
						Walking();
						if (StickY > WindowHeight/2 && Math.abs(XTarget - PetX) < 100/Math.sqrt(ZTarget) && Math.abs(YTarget - PetY) < 100/Math.sqrt(ZTarget) && Math.abs(ZTarget - PetZ) < 0.1*ZTarget) {
							StickState = 4;
							Tick = 0;
							StickXGoal = PetXGoal + RHandXGoal/Math.sqrt(Core.PetZ);
							StickYGoal = PetYGoal + RHandYGoal/Math.sqrt(Core.PetZ);
							StickAngleGoal = 30;
							Wave();
						}
					} else if (StickState == 4) {
						Emotion = 3;
						StickState = 4;
						StickXGoal = PetXGoal + RHandXGoal/Math.sqrt(Core.PetZ) + 30;
						StickYGoal = PetYGoal + RHandYGoal/Math.sqrt(Core.PetZ) + 10;
						StickAngleGoal = 30;
						Wave();
						if (Tick > 120) {
							Idle();
							StickState = 5;
						}
					} else if (StickState == 5) {
						Emotion = 2;
						StickXGoal = PetXGoal + RHandXGoal/Math.sqrt(Core.PetZ) + 30;
						StickYGoal = PetYGoal + RHandYGoal/Math.sqrt(Core.PetZ) + 10;
						StickZGoal = PetZGoal;
						StickAngleGoal = 30;
						XTarget = Core.WindowWidth - 120;
						YTarget = Core.WindowHeight - 120;
						ZTarget = 1;
						WalkingSpeed = 1;
						Walking();
						if (Math.abs(XTarget - PetX) < 10 && Math.abs(ZTarget - PetZ) < 0.3) {
							StickState = 0;
							StickXGoal = WindowWidth - 50;
							StickYGoal = WindowHeight - 50;
							StickAngleGoal = 45;
							Idle();
							XTarget = 150 + (WindowWidth - 300)*Math.random();
							YTarget = 150 + (WindowHeight - 300)*Math.random();
							ZTarget = 0.5 + (1.5)*Math.random();
						}
					} else {
						if (Goodbye) {
							Emotion = 3;
							WalkingSpeed = 0.7;
							XTarget = WindowWidth/2;
							YTarget = WindowHeight/2;
							ZTarget = 0.2;
							if (Math.abs(XTarget - PetX) < 10 && Math.abs(ZTarget - PetZ) < 0.3) {
								Wave();
							} else {
								Walking();
							}
						} else if (Math.abs(XTarget - PetX) < 3 && Math.abs(YTarget - PetY) < 3 && Math.abs(ZTarget - PetZ) < 0.1) {
							
							Idle();
							if (Math.random() > 0.999 && !Petting) {
								WalkingSpeed = 0.2;
								XTarget = 150 + (WindowWidth - 300)*Math.random();
								YTarget = 150 + (WindowHeight - 300)*Math.random();
								ZTarget = 0.5 + (1.5)*Math.random();
							}
							//Sleeping();
						} else {
							Walking();
						}
					}
				}
			}
			
			Stick();

			MouseDX = MouseX - LastMouseX;
			MouseDY = MouseY - LastMouseY;
			LastMouseX = MouseX;
			LastMouseY = MouseY;
			
			PetX += (PetXGoal - PetX)/5;
			PetY += (PetYGoal - PetY)/5;
			PetZ += (PetZGoal - PetZ)/5;
			HeadYOffset += (HeadYOffsetGoal - HeadYOffset)/5;
			HeadAngle += (HeadAngleGoal - HeadAngle)/5;
			LHandY += (LHandYGoal - LHandY)/5;
			RHandY += (RHandYGoal - RHandY)/5;
			LHandX += (LHandXGoal - LHandX)/5;
			RHandX += (RHandXGoal - RHandX)/5;
			REarAngle += (REarAngleGoal - REarAngle)/5;
			LEarAngle += (LEarAngleGoal - LEarAngle)/5;
			EyesX += (EyesXGoal - EyesX)/5;
			EyesY += (EyesYGoal - EyesY)/5;
			MouthX += (MouthXGoal - MouthX)/5;
			MouthY += (MouthYGoal - MouthY)/5;
			StickX += (StickXGoal - StickX)/2;
			StickY += (StickYGoal - StickY)/2;
			StickZ += (StickZGoal - StickZ)/2;
			StickAngle += (StickAngleGoal - StickAngle)/2;
		}
	}
	public static void Idle() {
		LHandYGoal = 50 + 10*Math.sin(Tick/30.0);
		RHandYGoal = 50 + 10*Math.cos(Tick/30.0);
		LHandXGoal = -75;
		RHandXGoal = 75;
		HeadYOffsetGoal = 5*Math.cos(Tick/10.0);
		REarAngleGoal = 35 + 3*Math.cos((Tick-5)/10.0);
		LEarAngleGoal = 35 + 3*Math.cos((Tick-5)/10.0);
		HeadAngleGoal = 0;
		EyesXGoal = 0;
		MouthXGoal = 0;
	}
	public static void Sleeping() {
		Emotion = 4;
		
		if (SleepStage == 0) {
			
		} else {
			LHandYGoal = 50;
			RHandYGoal = 70;
			LHandXGoal = -60;
			RHandXGoal = -15;
			HeadYOffsetGoal = 5*Math.cos(Tick/50.0);
			REarAngleGoal = -30 + 10*Math.cos((Tick-25)/50.0) - 3*Math.cos(Tick/50.0);
			LEarAngleGoal = 100 + 5*Math.cos((Tick-15)/50.0) - 3*Math.cos(Tick/50.0);
			HeadAngleGoal = -45 + 3*Math.cos(Tick/50.0);
			EyesXGoal = 0;
			EyesYGoal = 0;
			MouthXGoal = 0;
			MouthYGoal = 3*Math.cos((Tick+20)/25.0);
		}
	}
	public static void Pet() {
		Petting = true;
		PettingValue += Math.sqrt(MouseDX*MouseDX + MouseDY*MouseDY);
		PettingValue *= 0.97;
		
		LHandYGoal = 50 + 7*Math.cos(Tick/10.0);
		RHandYGoal = 50 + 7*Math.sin(Tick/10.0);
		LHandXGoal = -75;
		RHandXGoal = 75;
		HeadYOffsetGoal = 5*Math.abs(Math.cos(Tick/7.0));
		REarAngleGoal = 35 + 3*Math.cos((Tick-5)/7.0);
		LEarAngleGoal = 35 + 3*Math.cos((Tick-5)/7.0);
		EyesXGoal = 0;
		MouthXGoal = 0;
		
		MouseAngle = Math.toDegrees(Math.atan2(MouseY - PetY, MouseX - PetX) + Math.PI/2); //TODO make local
		
		if (MouseAngle > -45 && MouseAngle < 45) {
			REarAngleGoal = MouseAngle/2 + 45 + 3*Math.cos((Tick-5)/7.0);
			LEarAngleGoal = MouseAngle/-2 + 45 + 3*Math.cos((Tick-5)/7.0);
			HeadAngleGoal = MouseAngle/2;
		} else if (MouseAngle > 45 && MouseAngle < 135) {
			REarAngleGoal = -90 + (90 + MouseAngle)/3 + 45 + 3*Math.cos((Tick-5)/7.0);
			LEarAngleGoal = 90 + (90 + MouseAngle)/-2 + 45 + 3*Math.cos((Tick-5)/7.0);
			HeadAngleGoal = -90 + (90 + MouseAngle)/2;
		} else if (MouseAngle < -45) {
			REarAngleGoal = (90 + MouseAngle)/2 + 45 + 3*Math.cos((Tick-5)/7.0);
			LEarAngleGoal = (90 + MouseAngle)/-3 + 45 + 3*Math.cos((Tick-5)/7.0);
			HeadAngleGoal = (90 + MouseAngle)/2;
		} else if (MouseAngle > 225) {
			REarAngleGoal = -180 + (90 + MouseAngle)/2 + 45 + 3*Math.cos((Tick-5)/7.0);
			LEarAngleGoal = 90 + (90 + MouseAngle)/-3 + 45 + 3*Math.cos((Tick-5)/7.0);
			HeadAngleGoal = -180 + (90 + MouseAngle)/2;
		} else {
			HeadAngleGoal = MouseAngle/2 - 90;
			REarAngleGoal = MouseAngle/2 - 65 + 3*Math.cos((Tick-5)/7.0);
			LEarAngleGoal = MouseAngle/-2 + 115 + 3*Math.cos((Tick-5)/7.0);
		}
	}
	public static void Walking() {
		LHandYGoal = 50 + 10*Math.abs(Math.sin(Tick/10.0));
		RHandYGoal = 50 + 10*Math.abs(Math.sin(Tick/10.0));
		HeadYOffsetGoal = 15*Math.abs(Math.cos(Tick/7.0));
		REarAngleGoal = 35 + 10*Math.abs(Math.cos(Tick/7.0));
		LEarAngleGoal = 35 + 10*Math.abs(Math.cos(Tick/7.0));
		HeadAngleGoal = 0;
		EyesXGoal = 0;
		MouthXGoal = 0;
		
		double AngleToTarget = Math.atan2(YTarget - PetY, XTarget - PetX);
		
		if (Math.abs((XTarget-PetX)/20) > 10/Math.sqrt(Math.sqrt(PetZ))) {
			if ((XTarget-PetX)/20 > 0) {
				EyesXGoal = 10;
				MouthXGoal = 10;
				REarAngleGoal -= 30;
				LEarAngleGoal += 10;
			} else {
				EyesXGoal = -10;
				MouthXGoal = -10;
				REarAngleGoal += 10;
				LEarAngleGoal -= 30;
			}
			PetXGoal += Math.cos(AngleToTarget)*WalkingSpeed*10/Math.sqrt(PetZ);
		} else {
			EyesXGoal = (XTarget-PetX)/20;
			MouthXGoal = (XTarget-PetX)/20;
			REarAngleGoal -= 3*(XTarget-PetX)/20;
			LEarAngleGoal += 3*(XTarget-PetX)/20;
			PetXGoal += Math.cos(AngleToTarget)*WalkingSpeed*Math.abs(XTarget-PetX)/(10*Math.sqrt(PetZ));
		}
		
		if (Math.abs((YTarget-PetY)/20) > 10/Math.sqrt(PetZ)) {
			if ((YTarget-PetY)/20 > 0) {
				EyesYGoal = 10;
				MouthYGoal = 10;
			} else {
				EyesYGoal = -10;
				MouthYGoal = -10;
			}
			PetYGoal += Math.sin(AngleToTarget)*WalkingSpeed*10/Math.sqrt(PetZ);
		} else {
			EyesYGoal = (YTarget-PetY)/20;
			MouthYGoal = (YTarget-PetY)/20;
			PetYGoal += Math.sin(AngleToTarget)*WalkingSpeed*Math.abs(YTarget-PetY)/(10*Math.sqrt(PetZ));
		}
		
		PetZGoal += WalkingSpeed*(ZTarget-PetZ)/(20*Math.sqrt(PetZ));
	}
	public static void Wave() {
		HeadAngleGoal = -15;
		RHandXGoal = 80 + 30*Math.sin(Tick/3.0);
		RHandYGoal = -60 + 20*Math.sin(Tick/3.0);
		HeadYOffsetGoal = 15*Math.abs(Math.cos(Tick/14.0));
		LHandYGoal = 50 + 10*Math.abs(Math.sin(Tick/10.0));
		REarAngleGoal = 5 + 10*Math.abs(Math.cos(Tick/6.0));
		LEarAngleGoal = 50 + 10*Math.abs(Math.cos(Tick/6.0));
	}
	public static void FollowStick() {
		LHandYGoal = 50 + 10*Math.abs(Math.sin(Tick/10.0));
		RHandYGoal = 50 + 10*Math.abs(Math.sin(Tick/10.0));
		HeadYOffsetGoal = 15*Math.abs(Math.cos(Tick/7.0));
		REarAngleGoal = 35 + 10*Math.abs(Math.cos(Tick/7.0));
		LEarAngleGoal = 35 + 10*Math.abs(Math.cos(Tick/7.0));
		
		double StickAngle = Math.atan2(StickY - PetY, StickX - PetX);
		
		if (StickAngle > -Math.PI/2 && StickAngle < Math.PI/2) {
			HeadAngleGoal = Math.toDegrees(StickAngle)/2;
		} else if (StickAngle < -Math.PI/2) {
			HeadAngleGoal = Math.toDegrees(StickAngle)/2 + 90;
		} else {
			HeadAngleGoal = Math.toDegrees(StickAngle)/2 - 90;
		}
		
		EyesYGoal = 10*Math.sin(StickAngle);
		MouthYGoal = 10*Math.sin(StickAngle);
		EyesXGoal = 10*Math.cos(StickAngle);
		MouthXGoal = 10*Math.cos(StickAngle);
	}
	public static void Stick() {
		
		if (StickState == 0) {
			if (Math.sqrt((MouseX - StickX)*(MouseX - StickX) + (MouseY - StickY)*(MouseY - StickY)) < 100 && MousePressed) {
				StickState = 1;
			}
		} else if (StickState == 1) {
			
			StickXGoal = Core.MouseX - 3;
			StickYGoal = Core.MouseY + 70;
			//StickAngleGoal = 45*Math.sin(Tick/10.0);
			
			StickDAngle += MouseDX/5*Math.cos(Math.toRadians(StickAngleGoal));
			StickDAngle += MouseDY/5*Math.sin(Math.toRadians(StickAngleGoal));
			
			StickDAngle -= 10*Math.sin(Math.toRadians(StickAngleGoal)); // gravity
			
			StickAngleGoal += StickDAngle/10;
			
			StickDAngle *= 0.98;
			if (!MousePressed) {
				if (MouseDY > 50) {
					MouseDY = 50;
				}if (MouseDY < -50) {
					MouseDY = -50;
				}
				StickDX = MouseDX/3;
				StickDY = MouseDY/3;
				StickDZ = Math.abs(MouseDY/30);
				StickDAngle /= 5;
				StickState = 2;
			}
		} else if (StickState == 2) {
			StickXGoal += StickDX;
			StickXGoal += Math.sqrt(StickDZ)/100*((WindowWidth/2)-StickXGoal);
			StickYGoal += StickDY;
			if (StickY < WindowHeight/2) {
				StickZGoal += StickDZ;
			}
			StickAngleGoal += StickDAngle;
			
			if (StickXGoal < -150*StickZGoal) {
				StickXGoal = -150*StickZGoal;
				StickDX = 0;
			} else if (StickXGoal > WindowWidth + 150*StickZGoal) {
				StickXGoal = WindowWidth + 150*StickZGoal;
				StickDX = 0;
			}
			
			StickDY += 1;
			StickDX *= 0.98;
			StickDY *= 0.98;
			StickDAngle *= 0.98;
			
			if (StickYGoal > WindowHeight/2+70+80/Math.sqrt(StickZ)) {
				if (StickDY > 3) { // Bounce
					StickDAngle = 10*Math.cos(StickAngleGoal);
					StickDY = -0.5*StickDY;
				} else if (StickDY < 0) {
					//StickYGoal = WindowHeight/2+70+80/Math.sqrt(StickZ);
					StickDAngle = 0;
					while (StickAngleGoal%360 < 0) {
						StickAngleGoal += 360;
						StickAngle += 360;
					}
					if (StickAngleGoal%360 > 0 && StickAngleGoal%360 <= 180) {
						StickAngleGoal = (int)(StickAngleGoal/360.0)*360.0 + 90;
					} else if (StickAngleGoal%360 > 180 && StickAngleGoal%360 < 360) {
						StickAngleGoal = (int)(StickAngleGoal/360.0)*360.0 + 270;
					} else {
						StickAngleGoal = (int)(StickAngleGoal/360.0)*360.0 - 90;
					}
					StickState = 3;
				}
			}
		}
		
	}
}