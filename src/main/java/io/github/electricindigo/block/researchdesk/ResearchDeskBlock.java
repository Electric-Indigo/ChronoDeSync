package io.github.electricindigo.block.researchdesk;

import io.github.electricindigo.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

public class ResearchDeskBlock extends HorizontalDirectionalBlock implements EntityBlock
{
    public static final EnumProperty<DeskPart> PART = EnumProperty.create("part", DeskPart.class);

    public ResearchDeskBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(PART, DeskPart.PRIMARY));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, PART);
    }

    private static Direction secondaryOffset(Direction facing)
    {
        return facing.getCounterClockWise();
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos secondaryPos = context.getClickedPos().relative(secondaryOffset(facing));

        if (!context.getLevel().getBlockState(secondaryPos).canBeReplaced(context))
        {
            return null;
        }

        return defaultBlockState().setValue(FACING, facing).setValue(PART, DeskPart.PRIMARY);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity by, ItemStack itemStack)
    {
        super.setPlacedBy(level, pos, state, by, itemStack);
        if (!level.isClientSide())
        {
            Direction facing = state.getValue(FACING);
            BlockPos secondaryPos = pos.relative(secondaryOffset(facing));
            level.setBlock(secondaryPos, state.setValue(PART, DeskPart.SECONDARY), Block.UPDATE_ALL);
        }
    }

    private static Direction directionToOtherHalf(BlockState state)
    {
        Direction facing = state.getValue(FACING);
        return state.getValue(PART) == DeskPart.PRIMARY
                ? secondaryOffset(facing)
                : secondaryOffset(facing).getOpposite();
    }

    public static BlockPos getOtherHalfPos(BlockState state, BlockPos pos)
    {
        return pos.relative(directionToOtherHalf(state));
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
    {
        if (directionToNeighbour != directionToOtherHalf(state))
        {
            return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
        }

        return neighbourState.is(this) && neighbourState.getValue(PART) != state.getValue(PART)
                ? state
                : Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if (!level.isClientSide() && player.preventsBlockDrops())
        {
            BlockPos otherPos = getOtherHalfPos(state, pos);
            BlockState otherState = level.getBlockState(otherPos);

            if (otherState.is(this) && otherState.getValue(PART) != state.getValue(PART))
            {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, otherPos, Block.getId(otherState));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return blockState.getValue(PART) == DeskPart.PRIMARY
                ? new ResearchDeskBlockEntity(ModBlockEntities.RESEARCH_DESK.get(), blockPos, blockState)
                : null;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (level.isClientSide())
        {
            return InteractionResult.SUCCESS;
        }
        BlockPos primaryPos = state.getValue(PART) == DeskPart.PRIMARY ? pos : getOtherHalfPos(state, pos);

        if (level.getBlockEntity(primaryPos) instanceof ResearchDeskBlockEntity blockEntity)
        {
            player.openMenu(blockEntity);
        }
        return InteractionResult.CONSUME;
    }
}
