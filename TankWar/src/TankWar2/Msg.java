package TankWar2;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Msg {
	/**
	 * 坦克产生的消息
	 */
	public static final int TANK_NEW_MSG = 1;
	
	/**
	 * 坦克移动的消息
	 */
	public static final int TANK_MOVE_MSG = 2;
	
	/**
	 * 子弹产生的消息
	 */
	public static final int MISSILE_NEW_MSG = 3;
	
	/**
	 * 坦克死掉的消息
	 */
	public static final int TANK_DEAD_MSG = 4;
	
	/**
	 * 子弹死掉的消息
	 */
	public static final int MISSILE_DEAD_MSG = 5;
	
	/**
	 * 发送数据
	 * @param ds
	 * @param IP
	 * @param udpPort
	 */
	public void send(DatagramSocket ds, String IP, int udpPort);
	
	/**
	 * 接收并处理数据
	 * @param dis
	 */
	public void parse(DataInputStream dis);
}