package init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab extends CreativeModeTab {

	public ModCreativeModeTab(int index, String name) {
		super(index, name);
		
	}
    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemInit.JOINT.get());
    }

    
}
