import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

/**
 * Created by matpe on 02/06/2016.
 */
public class NewTestBits {

    public static int BIT0 = new Double(Math.pow(2, 0)).intValue();
    public static int BIT1 = new Double(Math.pow(2, 1)).intValue();
    public static int BIT2 = new Double(Math.pow(2, 2)).intValue();
    public static int BIT3 = new Double(Math.pow(2, 3)).intValue();
    public static int BIT4 = new Double(Math.pow(2, 4)).intValue();
    public static int BIT5 = new Double(Math.pow(2, 5)).intValue();
    public static int BIT6 = new Double(Math.pow(2, 6)).intValue();
    public static int BIT7 = new Double(Math.pow(2, 7)).intValue();

    public static void main(String[] args ){
        byte[] dataByte = forgeDNSRequest("server.mete0r.fr");
        try {
            InetAddress server = InetAddress.getByName("8.8.8.8");
            DatagramPacket dataSent = new DatagramPacket(dataByte, dataByte.length, server, 53);
            DatagramSocket socket = new DatagramSocket();
            socket.send(dataSent);

            DatagramPacket dataReceived = new DatagramPacket(new byte[dataByte.length], dataByte.length);
            socket.receive(dataReceived);

            byte[] received = dataReceived.getData();
            //test print
            System.out.println("Data received : " + new String(received));
            System.out.println("From : " + dataReceived.getAddress() + ":" + dataReceived.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static byte[] forgeDNSRequest(String domainName){
        byte[] header = new byte[12];
        int myId = 123456;
        int msg = 0;
        int QR = 0;
        int opcode = 0;
        int tc = 0;
        int rd = 1;
        int ra = 0;
        int z = 0;
        int rcode = 0;
        int qdcount = 1;
        int ancount = 0;
        int nscount = 0;
        int arcount = 0;

        header[0] = (byte) (myId >> 8);
        header[1]  = (byte) (myId & 0xff);

        msg = (QR << 7);
        msg |= (opcode << 3);
        msg |= (tc << 1);
        msg |= (rd);
        header[2] = (byte)msg;
        int fourth = 0;
        fourth |= (ra << 7);
        fourth |= (z << 4);
        fourth |= (rcode);
        header[3] = (byte)fourth;
        header[4] = (byte)(qdcount >> 8);
        header[5] = (byte)(qdcount & 0xff);
        header[6] = (byte)(ancount >> 8);
        header[7] = (byte)(ancount & 0xff);
        header[8] = (byte)(nscount >> 8);
        header[9] = (byte)(nscount & 0xff);
        header[10] = (byte)(arcount >> 8);
        header[11] = (byte)(arcount & 0xff);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        String dom = domainName;
        String[] doms = dom.split("\\.");
        try {
            out.write(header);
            for (String name: doms){
                out.write(name.length());
                out.write(name.getBytes());
            }
            out.write(0b0);
            out.write(0b0); //type byte a
            out.write(0b1); //type byte b
            out.write(0b0); //class byte a
            out.write(0b1); //class byte b
            out.flush();

        }catch (IOException e){
            e.printStackTrace();
        }


        byte[] dataByte = out.toByteArray();
        return dataByte;



//        System.out.println(msg);
//        System.out.println((msg & BIT7) >> 7);
//        int count = 7;
//
//        msg |= (count << 3);
//        System.out.println(msg);
//
//        System.out.println((msg & 0b111000 ) >> 3);
//        return header;
    }
}
