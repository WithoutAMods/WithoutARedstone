package withoutaname.mods.withoutaredstone.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import withoutaname.mods.withoutaredstone.WithoutARedstone;
import withoutaname.mods.withoutaredstone.network.Networking;
import withoutaname.mods.withoutaredstone.network.WirelessLinkModifyPacket;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class WirelessLinkModifyScreen extends Screen {

	protected final ResourceLocation GUI_TEXTURE = new ResourceLocation(WithoutARedstone.MODID, "textures/gui/wireless_link_modify.png");

	private final int xSize = 228;
	private final int ySize = 68;
	private int guiLeft;
	private int guiTop;

	private final Pattern numeric = Pattern.compile("-?\\d+");

	private int frequency;
	private boolean receiver;

	private Button receiverButton;
	private Button senderButton;
	private TextFieldWidget frequencyTextField;

	public WirelessLinkModifyScreen(int frequency, boolean receiver) {
		super(new TranslationTextComponent("screen.withoutaredstone.wireless_link_modify"));
		this.frequency = frequency;
		this.receiver = receiver;
	}

	public static void open(int frequency, boolean receiver) {
		Minecraft.getInstance().setScreen(new WirelessLinkModifyScreen(frequency, receiver));
	}

	@Override
	protected void init() {
		super.init();
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		int i = guiLeft + 12;
		int j = guiTop + 12;

		receiverButton = addButton(new Button(i, j, 100, 20,
				new StringTextComponent("Receiver"), button -> setReceiver(true)));
		senderButton = addButton(new Button(i + 104, j, 100, 20,
				new StringTextComponent("Sender"), button -> setReceiver(false)));
		setReceiver(receiver);

		frequencyTextField = addButton(new TextFieldWidget(minecraft.font, i, j + 24, 204, 20, StringTextComponent.EMPTY));
		frequencyTextField.setResponder(s -> {
			if ("".equals(s) || "-".equals(s)) {
				frequency = 0;
			} else if (numeric.matcher(s).matches()) {
				frequency = Integer.parseInt(s);
			} else {
				frequencyTextField.setValue(String.valueOf(frequency));
			}
		});
		frequencyTextField.setValue(String.valueOf(frequency));
	}

	private void setReceiver(boolean receiver) {
		this.receiver = receiver;
		receiverButton.active = !receiver;
		senderButton.active = receiver;
	}

	@Override
	public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		this.drawGuiBackgroundLayer(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	protected void drawGuiBackgroundLayer(MatrixStack matrixStack) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		assert this.minecraft != null;
		this.minecraft.getTextureManager().bind(GUI_TEXTURE);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
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
