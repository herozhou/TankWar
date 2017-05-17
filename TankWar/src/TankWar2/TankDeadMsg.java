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
 * 代表坦克被击毙的消息类
 * @author mashibing
 *
 */
public class TankDeadMsg implements Msg {
	int msgType = Msg.TANK_DEAD_MSG;

	TankClient tc;

	int id;
	/**
	 * 根据坦克的id构建消息
	 * @param id 坦克id
	 */
	public TankDeadMsg(int id) {
		this.id = id;
	}
	
	/**
	 * 根据消息产生的场所构建新的消息
	 * @param tc
	 */
	public TankDeadMsg(TankClient tc) {
		this.tc = tc;
	}
	
	/**
	 * 分析接收到的消息数据
	 * @param dis 接收到的消息数据的输入流
	 */
	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if (tc.mytank.id == id) {
				return;
			}

			// System.out.println("id:" + id + "-x:" + x + "-y:" + y + "-dir:" +
			// dir + "-good:" + good);
			for (int i = 0; i < tc.tanklist.size(); i++) {
				Tank t = tc.tanklist.get(i);
				if (t.id == id) {
					t.setLive(false);
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

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
			dos.writeInt(id);
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

}
