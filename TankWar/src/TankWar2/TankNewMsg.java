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
 * 代表坦克诞生的消息类
 * @author mashibing
 *
 */
public class TankNewMsg implements Msg {
	int msgType = Msg.TANK_NEW_MSG;

	Tank tank;

	TankClient tc;
	
	/**
	 * 根据tank的信息构建消息
	 * @param tank
	 */
	public TankNewMsg(Tank tank) {
		this.tank = tank;
	}
	
	/**
	 * 根据消息产生的场所构建新的消息
	 * @param tc
	 */
	public TankNewMsg(TankClient tc)
	{
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
			dos.writeInt(msgType);  // （2）
			dos.writeInt(tank.id);// （3）
			dos.writeInt(tank.getX());//（4）
			dos.writeInt(tank.getY());//（5）
			dos.writeInt(tank.dir.ordinal());//（6）
			dos.writeBoolean(tank.isGood());//（7）
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
			int id = dis.readInt();     //【3】 接收id 号
			if (tc.mytank.id == id) {
				return;
			}

			int x = dis.readInt();//【4】
			int y = dis.readInt();//【5】
			Dir dir = Dir.values()[dis.readInt()];//【6】
			boolean good = dis.readBoolean();//【7】
			 System.out.println("id:" + id + "-x:" + x + "-y:" + y + "-dir:" +
			 dir + "-good:" + good);
			 
			boolean exist = false;
			for (int i = 0; i < tc.tanklist.size(); i++) {
				Tank t = tc.tanklist.get(i);
				if (t.id == id) {
					exist = true;
					break;
				}
			}

			if (!exist)  //如果坦克列表里不存在  就新添加新坦克到 坦克列表里
			{
				TankNewMsg tnMsg = new TankNewMsg(tc.mytank);
				tc.sc.send(tnMsg);

				Tank t = new Tank(x, y, good, dir, tc);
				t.id = id;
				tc.tanklist.add(t);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
