package me.thetechnicboy.redstonefeatures.datagen;

import me.thetechnicboy.redstonefeatures.Redstonefeatures;
import me.thetechnicboy.redstonefeatures.block.ModBlocks;
import me.thetechnicboy.redstonefeatures.block.RedstoneRequester;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Redstonefeatures.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        this.getVariantBuilder(ModBlocks.REDSTONE_REQUESTER.get()).forAllStates( state -> {
            int power = state.getValue(RedstoneRequester.REDSTONE_LEVEL);
            return ConfiguredModel.builder()
                .modelFile(new ModelFile.UncheckedModelFile((modLoc("block/redstone_requester_"+ power))))
                .build();
        });

    }
}
