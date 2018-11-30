package software.sitb.utils.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 田尘殇Sean Create At 2018/10/15 11:03
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyClient extends ChannelInboundHandlerAdapter {


    private String host;

    private int port;

    private Bootstrap bootstrap;

    private Object request;

    private Object response;

    private Exception cause;

    private NettyClient(String host, int port, ChannelHandler[] channelHandlers) {
        this.host = host;
        this.port = port;
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel channel) throws Exception {
                channel.pipeline().addLast(channelHandlers);
                channel.pipeline().addLast();
            }
        }).option(ChannelOption.SO_KEEPALIVE, true);
    }

    @SuppressWarnings("unchecked")
    public <O> O send(Object request) throws Exception {
        this.request = request;
        this.response = null;
        this.cause = null;
        bootstrap.connect(host, port)
                .channel()
                .closeFuture()
                .await();
        if (null != cause)
            throw cause;

        return (O) this.response;
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
        response = msg;
        LOGGER.info("read complete. close channel.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("NettyClient Exception, close channel.", cause);
        this.cause = new Exception(cause);
        ctx.close();
    }

    public static class Builder {

        private String host;

        private int port;

        private List<ChannelHandler> handlers = new ArrayList<>(3);

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder addHandler(ChannelHandler channelHandler) {
            this.handlers.add(channelHandler);
            return this;
        }

        public NettyClient build() {
            return new NettyClient(host, port, handlers.toArray(new ChannelHandler[0]));
        }
    }
}
