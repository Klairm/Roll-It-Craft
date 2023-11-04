package init;

import com.github.klairm.projectm.ProjectM;

import blockentity.DrugMachineEntity;
import blockentity.DryingRackEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
	public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ProjectM.MOD_ID);

	public static final RegistryObject<BlockEntityType<DryingRackEntity>> DRYING_RACK = TILE_ENTITY_TYPES.register(
			"drying_rack",
			() -> BlockEntityType.Builder.of(DryingRackEntity::new, BlockInit.DRYING_RACK.get()).build(null));

	public static final RegistryObject<BlockEntityType<DrugMachineEntity>> DRUG_MACHINE = TILE_ENTITY_TYPES.register(
			"drug_machine",
			() -> BlockEntityType.Builder.of(DrugMachineEntity::new, BlockInit.DRUG_MACHINE.get()).build(null));

}
