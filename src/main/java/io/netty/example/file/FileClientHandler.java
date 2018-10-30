package io.netty.example.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class FileClientHandler extends SimpleChannelInboundHandler<String> {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		System.out.println("Client:Connection is established.");
		ctx.writeAndFlush("C:\\Users\\yourando\\Desktop\\1.txt");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println(msg);
	}

}
