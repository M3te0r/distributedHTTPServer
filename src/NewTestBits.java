/**
 * Created by matpe on 02/06/2016.
 */
public class NewTestBits {

    public static void main(String[] args ){

        int BIT0 = new Double(Math.pow(2, 0)).intValue();
        int BIT1 = new Double(Math.pow(2, 1)).intValue();
        int BIT2 = new Double(Math.pow(2, 2)).intValue();
        int BIT3 = new Double(Math.pow(2, 3)).intValue();
        int BIT4 = new Double(Math.pow(2, 4)).intValue();
        int BIT5 = new Double(Math.pow(2, 5)).intValue();
        int BIT6 = new Double(Math.pow(2, 6)).intValue();
        int BIT7 = new Double(Math.pow(2, 7)).intValue();

        byte[] header = new byte[12];


        int myId = 123456;


        header[0] = (byte) (myId >> 8);
        header[1]  = (byte) (myId & 0xff);

        int a = ((header[0] & 0xff) + (header[1]));
        System.out.println(a);
        int msg = 0;
        int QR = 1;

        msg = (QR << 7);
        System.out.println(msg);
        System.out.println((msg & BIT7) >> 7);

        int count = 7;

        msg |= (count << 3);
        System.out.println(msg);

        System.out.println((msg & 0b111000 ) >> 3);

    }
}
