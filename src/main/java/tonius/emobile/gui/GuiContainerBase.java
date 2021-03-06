package tonius.emobile.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiContainerBase extends GuiContainer {
    
    public GuiContainerBase(Container container) {
        super(container);
    }
    
    @Override
    protected abstract void drawGuiContainerBackgroundLayer(float f, int i, int j);
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float gameTicks) {
        super.drawScreen(mouseX, mouseY, gameTicks);
        this.drawTooltips(mouseX, mouseY);
    }
    
    protected void drawTooltips(int mouseX, int mouseY) {
        List<String> lines = new ArrayList<String>();
        this.getTooltipLines(lines, mouseX, mouseY);
        if (lines.size() > 0) {
            this.drawTooltip(lines, mouseX, mouseY, this.fontRendererObj);
        }
    }
    
    protected abstract void getTooltipLines(List lines, int mouseX, int mouseY);
    
    @SuppressWarnings("rawtypes")
    protected void drawTooltip(List list, int x, int y, FontRenderer font) {
        if (list == null || list.isEmpty()) {
            return;
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int k = 0;
        Iterator iterator = list.iterator();
        
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            int l = font.getStringWidth(s);
            
            if (l > k) {
                k = l;
            }
        }
        int i1 = x + 12;
        int j1 = y - 12;
        int k1 = 8;
        
        if (list.size() > 1) {
            k1 += 2 + (list.size() - 1) * 10;
        }
        if (i1 + k > this.width) {
            i1 -= 28 + k;
        }
        if (j1 + k1 + 6 > this.height) {
            j1 = this.height - k1 - 6;
        }
        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        int l1 = -267386864;
        this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
        this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
        this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
        this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
        int i2 = 1347420415;
        int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
        this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
        this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
        this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);
        
        for (int k2 = 0; k2 < list.size(); ++k2) {
            String s1 = (String) list.get(k2);
            font.drawStringWithShadow(s1, i1, j1, -1);
            
            if (k2 == 0) {
                j1 += 2;
            }
            j1 += 10;
        }
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }
    
    protected void drawTankFluid(int x, int y, FluidTankInfo tank) {
        if (tank.capacity <= 0) {
            return;
        }
        FluidStack fluidStack = tank.fluid;
        if (fluidStack == null) {
            return;
        }
        if (fluidStack.amount <= 0) {
            return;
        }
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) {
            return;
        }
        IIcon fluidIcon = fluid.getIcon();
        if (fluidIcon == null) {
            fluidIcon = Blocks.water.getIcon(0, 0);
        }
        
        int height = (int) Math.floor((double) fluidStack.amount / (double) tank.capacity * 60);
        if (height == 0) {
            height++;
        }
        
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        this.bindFluidTexture(fluid);
        
        int drawHeight = 0;
        for (int i = 0; i < height; i += 16) {
            drawHeight = Math.min(height - i, 16);
            this.drawTexturedModelRectFromIcon(x, y + 60 - height + i, fluidIcon, 16, drawHeight);
        }
        
    }
    
    protected void bindFluidTexture(Fluid fluid) {
        if (fluid.getSpriteNumber() == 0) {
            this.mc.renderEngine.bindTexture(new ResourceLocation("textures/atlas/blocks.png"));
        } else {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, fluid.getSpriteNumber());
        }
    }
    
}
