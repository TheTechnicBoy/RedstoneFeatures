package me.thetechnicboy.redstonefeatures.block;

import me.thetechnicboy.redstonefeatures.container.RedstoneRequesterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class RedstoneRequester extends Block implements EntityBlock {

    public static final IntegerProperty REDSTONE_LEVEL = IntegerProperty.create("redstone_level", 0, 15);

    public RedstoneRequester() {
        super(Properties.of().instrument(NoteBlockInstrument.BELL).strength(1.5F, 6.0F));
        this.registerDefaultState(this.stateDefinition.any().setValue(REDSTONE_LEVEL, 0));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneRequesterEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REDSTONE_LEVEL);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof RedstoneRequesterEntity) {
                RedstoneRequesterEntity redstoneBlockEntity = (RedstoneRequesterEntity) entity;
                NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("gui.redstonefeatures.redstone_requester.title");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
                        return new RedstoneRequesterMenu(id, inv, pos, redstoneBlockEntity.getURL());
                    }
                }, buf -> {
                    buf.writeBlockPos(pos);
                    buf.writeUtf(redstoneBlockEntity.getURL());
                });
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (!level.isClientSide && type == ModBlocks.REDSTONE_REQUESTER_BLOCKENTITY.get()) {
            return (lvl, pos, blockState, blockEntity) -> {
                if (blockEntity instanceof RedstoneRequesterEntity be) be.tick(lvl, pos, blockState);
            };
        }
        return null;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return side != null && side.getAxis().isHorizontal();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            int redstone = level.getBestNeighborSignal(pos);
            if (state.getValue(REDSTONE_LEVEL) != redstone) {
                level.setBlock(pos, state.setValue(REDSTONE_LEVEL, redstone), 2);
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        RedstoneRequesterEntity be = (RedstoneRequesterEntity) level.getBlockEntity(pos);
        return Math.max(be.mLastOutput, 0);
    }
}
