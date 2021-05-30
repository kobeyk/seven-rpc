import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

/**
 * <p>Netty·ByteBuf对象使用，其封装了NIO的ByteBuffer对象，使其API更好操作</p>
 *
 * @author appleyk
 * @version V.0.1.1
 * @blob https://blog.csdn.net/appleyk
 * @github https://github.com/kobeyk
 * @date created on 14:03 2021/5/19
 */
public class NettyByteBufTest {
    @Test
    public void byteBuf(){

        /*在内存开辟一个byte[10]大小的空间*/
        ByteBuf buf = Unpooled.buffer(10);

        /*
         * 在Netty的ByteBuf中，不需要使用flip函数对读写进行翻转
         * 因为其底层维护了一个readerIndex（读的下标）和writerIndex（写的下标）
         *        0    - readerIndex ： 已经读取的区域
         * readerIndex - writerIndex ： 剩余可读的区域
         * writerIndex - capacity    ： 剩余可写的区域
         */
        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }
        System.out.println("容量："+buf.capacity());
        /*开始读*/
        for (int i = 0; i < 10 ; i++) {
            buf.readByte();
        }

        /*
         *   读完了，readerIndex=10了，导致不能再读了
         *   public boolean isReadable() {
         *         return this.writerIndex > this.readerIndex;
         *   }
         *   此时writerIndex == readerIndex
         */
        /*重置下读指针，就可以继续读了*/
        buf.resetReaderIndex();
        int res = buf.readByte();
        System.out.println(res);
    }

    @Test
    public void writeInt(){
        ByteBuf buf = Unpooled.directBuffer(10);
        buf.writeInt(10);
        int i = buf.readInt();
        System.out.println(i);
    }

    @Test
    public void markedReaderIndex(){
        int capacity = 3;
        ByteBuf buf = Unpooled.buffer(capacity);
        for (int i = 0; i < capacity ; i++) {
            buf.writeInt(i+1);
        }
        buf.readInt();
        /*标记下下一次读取的位置*/
        buf.markReaderIndex();
        int i = buf.readInt();
        System.out.println(i);
    }

    /**
     * ByteBuf是按类型顺序写入、然后顺序读取的，前往不要乱读
     */
    @Test
    public void writeAndRead() throws Exception{
        int capacity = 2;
        ByteBuf buf = Unpooled.buffer(capacity);
        /*内容的长度5写进去，这样读取的时候可以按长度去读，防止tcp数据包的拆分和粘在一起的问题*/
        buf.writeInt(5);
        buf.writeBytes("hello".getBytes());
        buf.writeInt(3);
        buf.writeBytes("中".getBytes("utf-8"));

        /*buf中总过可读字节数， 5占4个字节，hello占5个字节，中占3个字节，可读字节总过 = 5+4+3 = 12*/
        int readableBytes = buf.readableBytes();
        System.out.println("buf中可读取的字节数："+readableBytes);

        /*读取"hello"的长度*/
        int len = buf.readInt();
        System.out.printf("要读取的内容长度：%d\n",len);
        byte[] content = new byte[len];
        buf.readBytes(content);
        System.out.printf("要读取的内容：%s",new String(content));

        /*读取"中"的长度*/
        len = buf.readInt();
        System.out.printf("要读取的内容长度：%d\n",len);
        content = new byte[len];
        buf.readBytes(content);
        System.out.printf("要读取的内容：%s",new String(content));

    }
}
