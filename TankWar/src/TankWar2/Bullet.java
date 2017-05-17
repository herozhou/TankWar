package TankWar2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Bullet 
{

	private int x,y;
	public static int BHIGHT=10;
	public static int BWEIGHT=10;
	public static int BULLETSPEED=20;

	Dir dir;
	private boolean good;
	private boolean bulletalive;
	
	TankClient tc;
	public int tankId;
	private static int ID = 1;

	

	int id;
	public Bullet(int tankId,int x, int y, Dir dir,TankClient tc,boolean good) 
	{
		this.tankId=tankId;
		this.setX(x+(Tank.TWEIGHT)/2);
		this.setY(y+(Tank.THIGHT)/2);
		
		 this.dir = dir;
		this.tc=tc;
		this.setBulletalive(true);
		this.setGood(good);
		id = ID++;
	}
	public void draw(Graphics g)
	{
		if(!isBulletalive()){
			tc.bulletlist.remove(this);
			return ;
		}
		Color c=g.getColor();
		g.setColor(Color.black);
		g.fillOval(getX(), getY(), BHIGHT, BWEIGHT);
		move();
	}
	public void move()
	{
		switch(dir)
		{
		case  UP: setY(getY() - BULLETSPEED);break;
		case  UPLEFT: setX(getX() - BULLETSPEED);setY(getY() - BULLETSPEED);break;
		case  UPRIGHT: setX(getX() + BULLETSPEED);setY(getY() - BULLETSPEED);break;
		case  DOWN: setY(getY() + BULLETSPEED);break;
		case  DOWNLEFT: setX(getX() - BULLETSPEED);setY(getY() + BULLETSPEED);break;
		case  DOWNRIGHT: setX(getX() + BULLETSPEED);setY(getY() + BULLETSPEED);break;
		case  LEFT: setX(getX() - BULLETSPEED);break;
		case  RIGHT: setX(getX() + BULLETSPEED);break;
		case STOP:setX(-10000);
		
		}
		if(getX()>TankClient.GAME_WIDTH||getY()>TankClient.GAME_HEIGHT||getX()<0||getY()<20)
		{
			
			setBulletalive(false);
			
		}
	}
	public Rectangle getRect() {
		return new Rectangle(getX(), getY(), BWEIGHT, BHIGHT);
	}
	public boolean hitTank(Tank t) {
		if(this.isBulletalive() && this.getRect().intersects(t.getRect()) && t.isLive() && this.isGood() != t.isGood()) 
		{
			if(t.isGood())
			{
				t.setLife(t.getLife()-20);
				if(t.getLife() <= 0) t.setLive(false);
			} else {
				t.setLive(false);
			}
			
			this.setBulletalive(false);
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}

	
	public boolean hitTanks(ArrayList<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	public boolean isBulletalive() {
		return bulletalive;
	}
	public void setBulletalive(boolean bulletalive) {
		this.bulletalive = bulletalive;
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
	public boolean isGood() {
		return good;
	}
	public void setGood(boolean good) {
		this.good = good;
	}
}
