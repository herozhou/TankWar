package TankWar2;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;







public class TankClient extends Frame
{
	
	 private static Toolkit tk = Toolkit.getDefaultToolkit();
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 800;
	Image imgbackground=tk.getImage("C:\\my\\tank2.jpg");;
	Tank mytank;
	Image offScreenImage = null;
	
	
	ArrayList<Bullet> bulletlist=new ArrayList<Bullet>();
	ArrayList<Tank> tanklist=new ArrayList<Tank>();
	ArrayList<Explode> explodes=new ArrayList<Explode>();
	
	ConnDialog dialog = new ConnDialog();
	SocketClient sc=new SocketClient(this);
	public void paint(Graphics g)
	{
		
		g.drawImage(imgbackground, 0, 0, GAME_WIDTH, GAME_HEIGHT, this);
		for(int i=0;i<bulletlist.size();i++)
		{
			Bullet bullets=bulletlist.get(i);
			
			bullets.hitTanks(tanklist);
			
			
			if (bullets.hitTank(mytank)) {
				TankDeadMsg msg = new TankDeadMsg(mytank.id);
				sc.send(msg);
				MissileDeadMsg mdmMsg = new MissileDeadMsg(bullets.tankId, bullets.id);
				sc.send(mdmMsg);
			}
			bullets.draw(g);
		}
		for(int i=0;i<tanklist.size();i++)
		{
			
			tanklist.get(i).draw(g);
		}
		for(int i=0;i<explodes.size();i++)
		{
			explodes.get(i).draw(g);
		}
		mytank.draw(g);
		
	}
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.WHITE);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	public void lunchJFrame()
	{
		mytank=new Tank(100,200,this,true);
		
		for(int i=0;i<10;i++)
		{
			tanklist.add(new Tank(50+i*50,50,this,false));
		}
		this.setVisible(true);
		this.setTitle("");
		this.setSize(800, 800);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener(new Keycontrol());
		
		new Thread(new ThreadPaint()).start();;
	}
	
	private class Keycontrol extends KeyAdapter 
	{
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_CONTROL)			{
				dialog.setVisible(true);
			} else {
				mytank.KeyPressed(e);
			}
			
		}
		public void keyReleased(KeyEvent e)
		{
			mytank.KeyRelaxed(e);
		}
	}
	private  class ThreadPaint implements Runnable
	{
		public void run()
		{
			while(true)
			{
				repaint();
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		TankClient tankclient=new TankClient();
		tankclient.lunchJFrame();
		
	}
	class ConnDialog extends Dialog {
		Button b = new Button("È·¶¨");

		TextField tfIP = new TextField("192.168.1.104", 12);

		TextField tfPort = new TextField("" + TankServer.TCP_PORT, 4);

		TextField tfMyUDPPort = new TextField("2223", 4);

		public ConnDialog() {
			super(TankClient.this, true);

			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("My UDP Port:"));
			this.add(tfMyUDPPort);
			this.add(b);
			this.setLocation(300, 300);
			this.pack();
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					setVisible(false);
				}
			});
			b.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//String IP = tfIP.getText().trim();
					String IP="192.168.1.104";
					int port = Integer.parseInt(tfPort.getText().trim());
					int myUDPPort = Integer.parseInt(tfMyUDPPort.getText()
							.trim());
					sc.setUdpPort(myUDPPort);
					sc.connect(IP, port);
					setVisible(false);
				}

			});
		}
	}


}
