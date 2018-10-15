package software.sitb.utils.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 田尘殇Sean Create At 2018/10/15 11:03
 */
@Slf4j
public abstract class NettyClient<I, O> extends ChannelInboundHandlerAdapter implements NettyNetwork {

    private I request;

    private O response;

    private Exception cause;

    public O send(I request) throws Exception {
        this.request = request;
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    addHandler(channel.pipeline());
                    channel.pipeline().addLast(NettyClient.this);
                }
            }).option(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.connect(getHost(), getPort()).channel().closeFuture().await();

            if (null != cause) {
                throw cause;
            }

            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("send data => [{}]", request);
        ctx.writeAndFlush(request);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info("receive data -> [{}]", msg);
        response = (O) msg;
        LOGGER.info("read complete. close channel.");
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("NettyClient Exception, close channel.", cause);
        this.cause = new Exception(cause);
        ctx.close();
    }
}
