package nova.core.wrapper.mc18.network.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import nova.core.wrapper.mc18.launcher.NovaMinecraft;
import nova.core.wrapper.mc18.network.discriminator.PacketAbstract;

/**
 * @author tgame14
 * @since 31/05/14
 */
@ChannelHandler.Sharable
public class MCPacketHandler extends SimpleChannelInboundHandler<PacketAbstract> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PacketAbstract packet) throws Exception {
		INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();

		switch (FMLCommonHandler.instance().getEffectiveSide()) {
			case CLIENT:
				FMLClientHandler.instance().getClient().addScheduledTask(() -> packet.handleClientSide(NovaMinecraft.proxy.getClientPlayer()));
				break;
			case SERVER:
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> packet.handleServerSide(((NetHandlerPlayServer) netHandler).playerEntity));
				break;
			default:
				break;
		}

	}

}
