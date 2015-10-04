package ru.redenergy.gui.component;

import java.util.stream.IntStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import ru.redenergy.gui.render.Renderer;
import ru.redenergy.gui.render.TextRenderer;

/**
 * Simple button component <br>
 * Supported width: <b> 0 - 400 </b> (due to texture length it can't be larger) <br>
 * Supported height: <b> 5 - INFINITY </b> <br>
 * 
 * Use {@link #setClickListener(ButtonClickListener)} to define action on button pressed
 */
public class Button extends GuiComponent {

    protected static final int DISABLED_STATE = 0;
    protected static final int IDLE_STATE = 1;
    protected static final int HOVER_STATE = 2;
    
    protected ResourceLocation buttonTexture = new ResourceLocation("textures/gui/widgets.png");
    
    protected String text;
    protected Rectangle shape;

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    protected ButtonClickListener onClick;
    
    public Button(int xPos, int yPos, int width, int height, String title) {
        this(new Rectangle(xPos, yPos, width, height), title);
    }
    
    public Button(Rectangle rect, String title){
        this.shape = rect;
        this.text = title;
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            prepareRender();
            if (!isEnabled()) {
                drawButton(DISABLED_STATE);
            } else if (isButtonUnderMouse(mouseX, mouseY)) {
                drawButton(HOVER_STATE);
            } else {
                drawButton(IDLE_STATE);
            }
            TextRenderer.renderCenteredString(getRect().getX() + getRect().getWidth() / 2, getRect().getY() + getRect().getHeight() / 2 - 4, getText());
        }
    }
    
    private void prepareRender(){
        Minecraft.getMinecraft().getTextureManager().bindTexture(getButtonTexture());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void drawButton(int state) {
        //top border
        Renderer.drawTexturedModalRect(getRect().getX(), getRect().getY(), 0, 46 + (state * 20), getRect().getWidth() / 2, 2);
        Renderer.drawTexturedModalRect(getRect().getX() + getRect().getWidth() / 2, getRect().getY(), 200 - getRect().getWidth() / 2, 46 + (state * 20), getRect().getWidth() / 2, 2);
        //middle
        IntStream.range(0, getRect().getHeight() - 5).forEach(idx -> Renderer.drawTexturedModalRect(getRect().getX(), getRect().getY() + 2 + (1 * idx), 0, 46 + (state * 20) + 2 + idx % 15, getRect().getWidth() / 2, 1));
        IntStream.range(0, getRect().getHeight() - 5).forEach(idx -> Renderer.drawTexturedModalRect(getRect().getX() + getRect().getWidth() / 2, getRect().getY() + 2 + (1 * idx), 200 - getRect().getWidth() / 2, 46 + (state * 20) + 2 + idx % 15, getRect().getWidth() / 2, 1));
        //bottom border
        Renderer.drawTexturedModalRect(getRect().getX(), getRect().getY() + getRect().getHeight() - 3, 0, 46 + (state * 20) + 17, getRect().getWidth() / 2, 3);
        Renderer.drawTexturedModalRect(getRect().getX() + getRect().getWidth() / 2, getRect().getY() + getRect().getHeight() - 3, 200 - getRect().getWidth() / 2, 46 + (state * 20) + 17, getRect().getWidth() / 2, 3);
    }

    @Override
    public void onMouseClicked(int posX, int posY, int mouseButtonIndex) {
        if (isButtonUnderMouse(posX, posY) && isEnabled()) {
            if (getClickListener() != null){
                getClickListener().onClick(this);
            }
            playClickSound();
        }
    }

    public boolean isButtonUnderMouse(int mouseX, int mouseY) {
        return mouseX >= getRect().getX() && mouseX <= getRect().getX() + getRect().getWidth() && mouseY >= getRect().getY() && mouseY <= getRect().getY() + getRect().getHeight();
    }

    /**
     * Provided listener will be executed by pressing the button
     * @param onClicked listener
     * @return self 
     */
    public Button setClickListener(ButtonClickListener onClicked) {
        this.onClick = onClicked;
        return this;
    }

    public ButtonClickListener getClickListener() {
        return onClick;
    }

    /**
     * @return <code> true </code> if button would be rendered
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * @return <code> true</code> if button can be clicked
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    public Button setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    public Button setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public ResourceLocation getButtonTexture() {
        return buttonTexture;
    }

    public Button setCustomTexture(ResourceLocation res) {
        this.buttonTexture = res;
        return this;
    }
    
    public String getText(){
        return text;
    }

    private void playClickSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }
    
    
    public Rectangle getRect(){
        return shape;
    }

    @FunctionalInterface
    public static interface ButtonClickListener {
        void onClick(Button button);
    }
}
