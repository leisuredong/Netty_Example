/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.echo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.example.echo.pojo.Grade;

/**
 * Handler implementation for the echo client. It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

	private final ByteBuf firstMessage;

	/**
	 * Creates a client-side handler.
	 */
	public EchoClientHandler() {
		// firstMessage = Unpooled.buffer(EchoClient.SIZE);
		// for (int i = 0; i < firstMessage.capacity(); i++) {
		// firstMessage.writeByte((byte) i);
		// }
		firstMessage = Unpooled.copiedBuffer("666".getBytes());
	}

	/** Read the object from Base64 string. */
	private static Object fromString(String s) throws IOException,
			ClassNotFoundException {
		byte[] data = Base64Coder.decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
				data));
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	/** Write the object to a Base64 string. */
	private static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return new String(Base64Coder.encode(baos.toByteArray()));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		Grade grade = new Grade();
		String message = "";
		try {
			message = toString(grade);
		} catch (Exception e) {
			// TODO: handle exception
		}
		ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf buf = (ByteBuf) msg;
		byte[] data = new byte[buf.readableBytes()];
		buf.readBytes(data);
		try {
			System.out.println("msg from server:" + new String(data, "utf-8"));
			String request = new String(data, "utf-8");
			Grade grade = new Grade();

			grade = (Grade) fromString(request);
			System.out.println("chinese:" + grade.getChinese() + "math:"
					+ grade.getMath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ctx.write(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
