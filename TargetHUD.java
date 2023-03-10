package cellar.mods.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import cellar.event.hud.ScreenPosition;
import cellar.mods.ModDraggable;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;

public class ModTargetHud extends ModDraggable{

	private List<Long> clicks = new ArrayList<Long>();
	private boolean wasPressed;
	private long lastPressed;
	
	EntityLivingBase savedTarget;
	
	@Override
	public int getHeight() {
		return font.FONT_HEIGHT + 3 * 4;
	}

	@Override
	public int getWidth() {
		return 60;
	}

	@Override
	public void save(ScreenPosition pos) {
		this.pos = pos;
	}

	@Override
	public ScreenPosition load() {
		return pos;
	}
	
	@Override
	public void render(ScreenPosition pos) {
		renderTargetHUD();
	}
	
	@Override
	public void renderDummy(ScreenPosition pos) {
		font.drawStringWithShadow(mc.thePlayer.getName(), pos.getAbsoluteX()+1, pos.getAbsoluteY()+2, -1);
		font.drawStringWithShadow("§f" + mc.thePlayer.getHealth() + "§r\u2764", pos.getAbsoluteX()+1, pos.getAbsoluteY()+2 +font.FONT_HEIGHT, new Color(233,55,65).getRGB());

	}
	
	private void renderTargetHUD() {
		
		if(!(mc.pointedEntity instanceof EntityItemFrame)) {
		EntityLivingBase target = (EntityLivingBase)mc.pointedEntity;
			if(target!=null) {
				final boolean pressed = Mouse.isButtonDown(0);
				if(pressed != this.wasPressed) {
					this.lastPressed = System.currentTimeMillis();
					this.wasPressed = pressed;
					if(pressed) {
						this.clicks.add(this.lastPressed);
					}
				}
				
				Gui.drawRoundedRect(pos.getAbsoluteX() + 1, pos.getAbsoluteY() + 10, pos.getAbsoluteX()+ getWidth() + 63, pos.getAbsoluteY()+ getHeight() + 29, 5, new Color(0, 0, 0, 170).getRGB());
				
				font.drawStringWithShadow(String.format("§fDistance: §r%.2f", target.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)), pos.getAbsoluteX() + 5, pos.getAbsoluteY() + 38, new Color(233,55,65).getRGB());
				
				font.drawStringWithShadow(target.getName(), pos.getAbsoluteX()+28, pos.getAbsoluteY() + 16, -1);
				Gui.drawPlayerHead(pos.getAbsoluteX() + 10, pos.getAbsoluteY() + 20, 16, target);
				renderHealthBar(target);
			}
		}
	}
	private void renderHealthBar(EntityLivingBase target) {
		Gui.drawRect(pos.getAbsoluteX() + 27, pos.getAbsoluteY()+27, pos.getAbsoluteX()+ 120,pos.getAbsoluteY()+35, new Color(25,23,13).getRGB());
		Gui.drawRect(pos.getAbsoluteX() + 27, pos.getAbsoluteY()+27, pos.getAbsoluteX()+ 27+(int)(93*(target.getHealth()/target.getMaxHealth())),pos.getAbsoluteY()+35, new Color(233,55,65).getRGB());
	}
}


//Go to Gui.java and Scroll to the bottom and paste this code:

public static void drawPlayerHead(int x, int y, int width, EntityLivingBase player) {
    	NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getUniqueID());
    	if (playerInfo != null){
            Minecraft.getMinecraft().getTextureManager().bindTexture(playerInfo.getLocationSkin());
            GL11.glColor4f(1F, 1F, 1F, 1F);

            Gui.drawScaledCustomSizeModalRect((int) x - 5, (int) y - 5, 8F, 8F, 8, 8, 20, 20, 64F, 64F);
        }
        
    }

public static void drawRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
    GL11.glPushAttrib(0);
    GL11.glScaled(0.5D, 0.5D, 0.5D);
    x *= 2.0D;
    y *= 2.0D;
    x1 *= 2.0D;
    y1 *= 2.0D;
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glEnable(GL11.GL_LINE_SMOOTH);
    GlStateManager.color(0F, 0F, 0F, 0.4F);
    GL11.glEnable(GL11.GL_LINE_SMOOTH);
    GL11.glBegin(GL11.GL_POLYGON);
    int i;
    for (i = 0; i <= 90; i += 3)
        GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
    for (i = 90; i <= 180; i += 3)
        GL11.glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D); 
    for (i = 0; i <= 90; i += 3)
        GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius); 
    for (i = 90; i <= 180; i += 3)
        GL11.glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius); 
	GL11.glEnd();
	GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
	GL11.glDisable(GL11.GL_LINE_SMOOTH);
	GL11.glDisable(GL11.GL_BLEND);	
	GL11.glDisable(GL11.GL_LINE_SMOOTH);
    GL11.glScaled(2.0D, 2.0D, 2.0D);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glPopAttrib();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
}
