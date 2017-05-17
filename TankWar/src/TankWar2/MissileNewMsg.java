package TankWar2;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * 代表子弹产生的消息类
 * @author mashibing
 *
 */
public class MissileNewMsg implements Msg {
	int msgType = Msg.MISSILE_NEW_MSG;

	TankClient tc;

	Bullet m;
	
	/**
	 * 根据子弹信息构建新的消息类
	 * @param m 产生消息的子弹
	 */
	public MissileNewMsg(Bullet m) {
		this.m = m;
	}
	
	/**
	 * 根据消息产生的场所构建新的消息
	 * @param tc
	 */
	public MissileNewMsg(TankClient tc) {
		this.tc = tc;
	}
	
	/**
	 * 发送相关的消息
	 * @param ds 通过该socket发送数据
	 * @param IP 数据的目标IP
	 * @param udpPort 数据的目标端口
	 */
	public void send(DatagramSocket ds, String IP, int udpPort) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			dos.writeInt(m.tankId);
			dos.writeInt(m.id);
			dos.writeInt(m.getX());
			dos.writeInt(m.getY());
			dos.writeInt(m.dir.ordinal());
			dos.writeBoolean(m.isGood());
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buf = baos.toByteArray();
		try {
			DatagramPacket dp = new DatagramPacket(buf, buf.length,
					new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分析接收到的消息数据
	 * @param dis 接收到的消息数据的输入流
	 */
	public void parse(DataInputStream dis) {
		try {
			int tankId = dis.readInt();
			if (tankId == tc.mytank.id) {
				return;
			}
			int id = dis.readInt();
			int x = dis.readInt();
			int y = dis.readInt();
			Dir dir = Dir.values()[dis.readInt()];
			boolean good = dis.readBoolean();

			// System.out.println("id:" + id + "-x:" + x + "-y:" + y + "-dir:" +
			// dir + "-good:" + good);
			Bullet m = new Bullet(tankId, x, y, dir, tc,good);
			m.id = id;
			tc.bulletlist.add(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
