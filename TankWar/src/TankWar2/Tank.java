package TankWar2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;



public class Tank extends  JFrame
{
	 private int x,y;
	 boolean up=false,down=false,left=false,right=false;
	 public static int THIGHT=50;
	 public static int TWEIGHT=50;
	 public static final int TANKSPEED=10;
	 private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	
	Image imgmytank;
	Image imgbadtank;
	Dir dir=Dir.STOP;
	Dir bulletdir;
	
	private boolean good;
	private boolean tankalive;
	private int tanklife=100;
	Bullet bullet;
	TankClient tc;
	
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	public int id;
	
	public Tank(int x, int y,TankClient tc,boolean good) {
		
		this.setX(x);
		this.setY(y);
		this.tc=tc;
		this.setGood(good);
		this.tankalive=true;
		
	}
	 public Tank(int x2, int y2, boolean good2, Dir dir2, TankClient tc2) {
		this(x2,y2,tc2,good2);
		this.dir=dir2;
	}
	public void draw(Graphics g)
	 {
		 if(!tankalive) return ;
		if(this.isGood())
		{	
			
			imgmytank=tk.getImage("C:\\my\\123.png");
			g.drawImage(imgmytank, getX(), getY(), TWEIGHT, THIGHT, this);
		}
		else
		{
			imgbadtank=tk.getImage("C:\\my\\tankD.gif");
			g.drawImage(imgbadtank, getX(), getY(), TWEIGHT, THIGHT, this);
		}
		// g.drawImage(imgtank, x, y, this);
		 move();
	 }
	public void KeyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:up=true;break;
		case KeyEvent.VK_DOWN:down=true;break;
		case KeyEvent.VK_LEFT:left=true;break;
		case KeyEvent.VK_RIGHT:right=true;break;
		
		}
		direction();
	}
	public void KeyRelaxed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:up=false;break;
		case KeyEvent.VK_DOWN:down=false;break;
		case KeyEvent.VK_LEFT:left=false;break;
		case KeyEvent.VK_RIGHT:right=false;break;
		case  KeyEvent.VK_SPACE : tc.bulletlist.add(fire());break;
		}
		direction();
	}
	public void direction()
	{
		Dir oldDir = this.dir;
	
		if(up&&!down&&!left&&!right)  dir=Dir.UP;
		else if(up&&!down&&left&&!right)  dir=Dir.UPLEFT;
		else if(up&&!down&&!left&&right)  dir=Dir.UPRIGHT;
		else if(!up&&down&&!left&&!right)  dir=Dir.DOWN;
		else if(!up&&down&&left&&!right)  dir=Dir.DOWNLEFT;
		else if(!up&&down&&!left&&right)  dir=Dir.DOWNRIGHT;
		else if(!up&&!down&&left&&!right)  dir=Dir.LEFT;
		else if(!up&&!down&&!left&&right)  dir=Dir.RIGHT;
		else dir=Dir.STOP;
		
		if(dir!=Dir.STOP)
			bulletdir=dir;
		if (dir != oldDir)
		{
			TankMoveMsg msg = new TankMoveMsg(id, x, y, dir);
			tc.sc.send(msg);   //发送移动的消息
		}
	}
	
	public void move()
	{
		int oldx=getX(),oldy=getY();
		switch(dir)
		{
		case  UP: setY(getY() - TANKSPEED);break;
		case  UPLEFT: setX(getX() - TANKSPEED);setY(getY() - TANKSPEED);break;
		case  UPRIGHT: setX(getX() + TANKSPEED);setY(getY() - TANKSPEED);break;
		case  DOWN: setY(getY() + TANKSPEED);break;
		case  DOWNLEFT: setX(getX() - TANKSPEED);setY(getY() + TANKSPEED);break;
		case  DOWNRIGHT: setX(getX() + TANKSPEED);setY(getY() + TANKSPEED);break;
		case  LEFT: setX(getX() - TANKSPEED);break;
		case  RIGHT: setX(getX() + TANKSPEED);break;
		
		
		}
		if(getX()+TWEIGHT>=TankClient.GAME_WIDTH||getY()+THIGHT>=TankClient.GAME_HEIGHT||getX()<0||getY()<20)
		{
			
			setX(oldx);setY(oldy);
		}
		if(!isGood()) {
			Dir[] dirs = Dir.values();
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}			
			step --;
			
			if(r.nextInt(40) > 30) 
				tc.bulletlist.add(fire());
		}
	}
	public Bullet fire()
	{
		
		if(bulletdir==null)
			this.bulletdir=dir;
		if(!tankalive)
			setX(this.getX()-1000);	
		return new Bullet(id,this.getX(),this.getY(),bulletdir,tc,this.isGood());
	
		
	}
	public Rectangle getRect() {
		return new Rectangle(getX(), getY(), TWEIGHT, THIGHT);
	}
	public boolean isLive() {
		
		return this.tankalive;
	}
	public boolean isGood() {
		
		return this.good;
	}
	public int getLife() {
		
		return this.tanklife;
	}
	public void setLife(int i) {
		this.tanklife=i;
		
	}
	public void setLive(boolean b) {
		this.tankalive=b;
		
	}
	public void setGood(boolean good) {
		this.good = good;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	
}
