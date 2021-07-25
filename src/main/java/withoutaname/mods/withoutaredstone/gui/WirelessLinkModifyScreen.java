package withoutaname.mods.withoutaredstone.gui;

import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import withoutaname.mods.withoutaredstone.WithoutARedstone;
import withoutaname.mods.withoutaredstone.network.Networking;
import withoutaname.mods.withoutaredstone.network.WirelessLinkModifyPacket;

public class WirelessLinkModifyScreen extends Screen {
	
	protected final ResourceLocation GUI_TEXTURE = new ResourceLocation(WithoutARedstone.MODID, "textures/gui/wireless_link_modify.png");
	
	private final int xSize = 228;
	private final int ySize = 68;
	private final Pattern numeric = Pattern.compile("-?\\d+");
	
	private int guiLeft;
	private int guiTop;
	private long frequency;
	private boolean receiver;
	
	private Button receiverButton;
	private Button senderButton;
	private EditBox frequencyTextField;
	
	public WirelessLinkModifyScreen(long frequency, boolean receiver) {
		super(new TranslatableComponent("screen.withoutaredstone.wireless_link_modify"));
		this.frequency = frequency;
		this.receiver = receiver;
	}
	
	public static void open(long frequency, boolean receiver) {
		Minecraft.getInstance().setScreen(new WirelessLinkModifyScreen(frequency, receiver));
	}
	
	@Override
	protected void init() {
		super.init();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		
		int i = guiLeft + 12;
		int j = guiTop + 12;
		
		receiverButton = addRenderableWidget(new Button(i, j, 100, 20,
				new TextComponent("Receiver"), button -> setReceiver(true)));
		senderButton = addRenderableWidget(new Button(i + 104, j, 100, 20,
				new TextComponent("Sender"), button -> setReceiver(false)));
		setReceiver(receiver);
		
		frequencyTextField = addRenderableWidget(new EditBox(minecraft.font, i, j + 24, 204, 20, TextComponent.EMPTY));
		frequencyTextField.setResponder(s -> {
			if ("".equals(s) || "-".equals(s)) {
				frequency = 0;
			} else if (numeric.matcher(s).matches()) {
				try {
					frequency = Long.parseLong(s);
				} catch (NumberFormatException e) {
					resetFrequencyTextField();
				}
			} else {
				resetFrequencyTextField();
			}
		});
		resetFrequencyTextField();
	}
	
	private void resetFrequencyTextField() {
		frequencyTextField.setValue(String.valueOf(frequency));
	}
	
	private void setReceiver(boolean receiver) {
		this.receiver = receiver;
		receiverButton.active = !receiver;
		senderButton.active = receiver;
	}
	
	@Override
	public void render(@Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		this.drawGuiBackgroundLayer(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
	}
	
	protected void drawGuiBackgroundLayer(PoseStack poseStack) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI_TEXTURE);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(poseStack, i, j, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	public void removed() {
		Networking.sendToServer(new WirelessLinkModifyPacket(frequency, receiver));
	}
	
}
