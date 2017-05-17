package TankWar2;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;



public class SocketClient {
	TankClient tc;

	private int udpPort;
	
	String IP; // server IP

	DatagramSocket ds = null;
	
	
	public SocketClient(TankClient tc) {
		this.tc = tc;
	}
	
	public void connect(String IP, int tcpPort) 
	{
		this.IP = IP;
		
		try {
				ds = new DatagramSocket(udpPort);  // 本机用udp端口发送消息
			} 
		catch (SocketException e) {
				e.printStackTrace();
			}
		Socket s = null;
		try {
			s = new Socket(IP, tcpPort);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);   //  （1）      第一次发送消息，发送本机的udp端口号
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt(); // 【1】      第一次接收，接受id号
			tc.mytank.id = id; //设置自己的坦克id

		/*	if (id % 2 == 0)
				tc.mytank.setGood(false);
			else
				tc.mytank.setGood(true);
*/
			tc.mytank.setGood(true); 
			System.out.println("Connected to server! and server give me a ID:"
					+ id);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (s != null) {
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	/**
	 *      新增坦克消息
	 * */	TankNewMsg msg = new TankNewMsg(tc.mytank);
		send(msg);

		new Thread(new UDPRecvThread()).start();// udp 接收线程开启
	}
	public void send(Msg msg) {
		msg.send(ds, IP, TankServer.UDP_PORT);
	}
	private class UDPRecvThread implements Runnable {

		byte[] buf = new byte[1024];

		public void run() {

			while (ds != null)  //如果本机的udp发送没关闭
			{
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try 
				{
					ds.receive(dp);  // 接收流接受数据包
					parse(dp);          //解析数据包
					System.out.println("a packet received from server!");
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void parse(DatagramPacket dp) 
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp
					.getLength());   //把数组包装成字节数组输出流
			DataInputStream dis = new DataInputStream(bais);//输出流封装字节数组输出流
			int msgType = 0;  //  消息类型   ：  新加坦克消息/坦克移动消息/子弹发出消息
			try {
				msgType = dis.readInt();       //【2】 接收消息类型
			} catch (IOException e) {
				e.printStackTrace();
			}
			Msg msg = null;
			switch (msgType) {
			case Msg.TANK_NEW_MSG:
				msg = new TankNewMsg(SocketClient.this.tc);
				msg.parse(dis);      //解析
				break;
			case Msg.TANK_MOVE_MSG:
				msg = new TankMoveMsg(SocketClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_NEW_MSG:
				msg = new MissileNewMsg(SocketClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.TANK_DEAD_MSG:
				msg = new TankDeadMsg(SocketClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.MISSILE_DEAD_MSG:
				msg = new MissileDeadMsg(SocketClient.this.tc);
				msg.parse(dis);
				break;
			}

		}

	}
	
	/**
	 * 取得UDP端口(客户端接收数据用)
	 * @return
	 */
	public int getUdpPort() {
		return udpPort;
	}
	
	/**
	 * 设定UDP端口(客户端接收数据用)
	 * @param udpPort
	 */
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	
}
