package init;

import com.github.klairm.projectm.ProjectM;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tile.DryingRackTile;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ProjectM.MOD_ID);

	public static final RegistryObject<BlockEntityType<DryingRackTile>> DRYING_RACK = TILE_ENTITY_TYPES.register(
			"drying_rack",
			() -> BlockEntityType.Builder.of(DryingRackTile::new, BlockInit.DRYING_RACK.get()).build(null));

}
