package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.SystemUtils;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.item.context.BlockActionContext;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.IWorldReader;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.level.block.state.BlockStateList;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockStateBoolean;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapeCollision;
import net.minecraft.world.phys.shapes.VoxelShapes;

public class BlockVine extends Block {

    public static final BlockStateBoolean UP = BlockSprawling.UP;
    public static final BlockStateBoolean NORTH = BlockSprawling.NORTH;
    public static final BlockStateBoolean EAST = BlockSprawling.EAST;
    public static final BlockStateBoolean SOUTH = BlockSprawling.SOUTH;
    public static final BlockStateBoolean WEST = BlockSprawling.WEST;
    public static final Map<EnumDirection, BlockStateBoolean> PROPERTY_BY_DIRECTION = (Map) BlockSprawling.PROPERTY_BY_DIRECTION.entrySet().stream().filter((entry) -> {
        return entry.getKey() != EnumDirection.DOWN;
    }).collect(SystemUtils.a());
    protected static final float AABB_OFFSET = 1.0F;
    private static final VoxelShape UP_AABB = Block.a(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.a(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.a(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape NORTH_AABB = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape SOUTH_AABB = Block.a(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<IBlockData, VoxelShape> shapesCache;

    public BlockVine(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.k((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) this.stateDefinition.getBlockData()).set(BlockVine.UP, false)).set(BlockVine.NORTH, false)).set(BlockVine.EAST, false)).set(BlockVine.SOUTH, false)).set(BlockVine.WEST, false));
        this.shapesCache = ImmutableMap.copyOf((Map) this.stateDefinition.a().stream().collect(Collectors.toMap(Function.identity(), BlockVine::h)));
    }

    private static VoxelShape h(IBlockData iblockdata) {
        VoxelShape voxelshape = VoxelShapes.a();

        if ((Boolean) iblockdata.get(BlockVine.UP)) {
            voxelshape = BlockVine.UP_AABB;
        }

        if ((Boolean) iblockdata.get(BlockVine.NORTH)) {
            voxelshape = VoxelShapes.a(voxelshape, BlockVine.NORTH_AABB);
        }

        if ((Boolean) iblockdata.get(BlockVine.SOUTH)) {
            voxelshape = VoxelShapes.a(voxelshape, BlockVine.SOUTH_AABB);
        }

        if ((Boolean) iblockdata.get(BlockVine.EAST)) {
            voxelshape = VoxelShapes.a(voxelshape, BlockVine.EAST_AABB);
        }

        if ((Boolean) iblockdata.get(BlockVine.WEST)) {
            voxelshape = VoxelShapes.a(voxelshape, BlockVine.WEST_AABB);
        }

        return voxelshape.isEmpty() ? VoxelShapes.b() : voxelshape;
    }

    @Override
    public VoxelShape a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return (VoxelShape) this.shapesCache.get(iblockdata);
    }

    @Override
    public boolean c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return true;
    }

    @Override
    public boolean canPlace(IBlockData iblockdata, IWorldReader iworldreader, BlockPosition blockposition) {
        return this.n(this.h(iblockdata, iworldreader, blockposition));
    }

    private boolean n(IBlockData iblockdata) {
        return this.o(iblockdata) > 0;
    }

    private int o(IBlockData iblockdata) {
        int i = 0;
        Iterator iterator = BlockVine.PROPERTY_BY_DIRECTION.values().iterator();

        while (iterator.hasNext()) {
            BlockStateBoolean blockstateboolean = (BlockStateBoolean) iterator.next();

            if ((Boolean) iblockdata.get(blockstateboolean)) {
                ++i;
            }
        }

        return i;
    }

    private boolean b(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        if (enumdirection == EnumDirection.DOWN) {
            return false;
        } else {
            BlockPosition blockposition1 = blockposition.shift(enumdirection);

            if (a(iblockaccess, blockposition1, enumdirection)) {
                return true;
            } else if (enumdirection.n() == EnumDirection.EnumAxis.Y) {
                return false;
            } else {
                BlockStateBoolean blockstateboolean = (BlockStateBoolean) BlockVine.PROPERTY_BY_DIRECTION.get(enumdirection);
                IBlockData iblockdata = iblockaccess.getType(blockposition.up());

                return iblockdata.a((Block) this) && (Boolean) iblockdata.get(blockstateboolean);
            }
        }
    }

    public static boolean a(IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        IBlockData iblockdata = iblockaccess.getType(blockposition);

        return Block.a(iblockdata.getCollisionShape(iblockaccess, blockposition), enumdirection.opposite());
    }

    private IBlockData h(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        BlockPosition blockposition1 = blockposition.up();

        if ((Boolean) iblockdata.get(BlockVine.UP)) {
            iblockdata = (IBlockData) iblockdata.set(BlockVine.UP, a(iblockaccess, blockposition1, EnumDirection.DOWN));
        }

        IBlockData iblockdata1 = null;
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockStateBoolean blockstateboolean = getDirection(enumdirection);

            if ((Boolean) iblockdata.get(blockstateboolean)) {
                boolean flag = this.b(iblockaccess, blockposition, enumdirection);

                if (!flag) {
                    if (iblockdata1 == null) {
                        iblockdata1 = iblockaccess.getType(blockposition1);
                    }

                    flag = iblockdata1.a((Block) this) && (Boolean) iblockdata1.get(blockstateboolean);
                }

                iblockdata = (IBlockData) iblockdata.set(blockstateboolean, flag);
            }
        }

        return iblockdata;
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (enumdirection == EnumDirection.DOWN) {
            return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        } else {
            IBlockData iblockdata2 = this.h(iblockdata, generatoraccess, blockposition);

            return !this.n(iblockdata2) ? Blocks.AIR.getBlockData() : iblockdata2;
        }
    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (random.nextInt(4) == 0) {
            EnumDirection enumdirection = EnumDirection.a(random);
            BlockPosition blockposition1 = blockposition.up();
            BlockPosition blockposition2;
            IBlockData iblockdata1;
            EnumDirection enumdirection1;

            if (enumdirection.n().d() && !(Boolean) iblockdata.get(getDirection(enumdirection))) {
                if (this.a((IBlockAccess) worldserver, blockposition)) {
                    blockposition2 = blockposition.shift(enumdirection);
                    iblockdata1 = worldserver.getType(blockposition2);
                    if (iblockdata1.isAir()) {
                        enumdirection1 = enumdirection.g();
                        EnumDirection enumdirection2 = enumdirection.h();
                        boolean flag = (Boolean) iblockdata.get(getDirection(enumdirection1));
                        boolean flag1 = (Boolean) iblockdata.get(getDirection(enumdirection2));
                        BlockPosition blockposition3 = blockposition2.shift(enumdirection1);
                        BlockPosition blockposition4 = blockposition2.shift(enumdirection2);

                        if (flag && a((IBlockAccess) worldserver, blockposition3, enumdirection1)) {
                            worldserver.setTypeAndData(blockposition2, (IBlockData) this.getBlockData().set(getDirection(enumdirection1), true), 2);
                        } else if (flag1 && a((IBlockAccess) worldserver, blockposition4, enumdirection2)) {
                            worldserver.setTypeAndData(blockposition2, (IBlockData) this.getBlockData().set(getDirection(enumdirection2), true), 2);
                        } else {
                            EnumDirection enumdirection3 = enumdirection.opposite();

                            if (flag && worldserver.isEmpty(blockposition3) && a((IBlockAccess) worldserver, blockposition.shift(enumdirection1), enumdirection3)) {
                                worldserver.setTypeAndData(blockposition3, (IBlockData) this.getBlockData().set(getDirection(enumdirection3), true), 2);
                            } else if (flag1 && worldserver.isEmpty(blockposition4) && a((IBlockAccess) worldserver, blockposition.shift(enumdirection2), enumdirection3)) {
                                worldserver.setTypeAndData(blockposition4, (IBlockData) this.getBlockData().set(getDirection(enumdirection3), true), 2);
                            } else if ((double) random.nextFloat() < 0.05D && a((IBlockAccess) worldserver, blockposition2.up(), EnumDirection.UP)) {
                                worldserver.setTypeAndData(blockposition2, (IBlockData) this.getBlockData().set(BlockVine.UP, true), 2);
                            }
                        }
                    } else if (a((IBlockAccess) worldserver, blockposition2, enumdirection)) {
                        worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(getDirection(enumdirection), true), 2);
                    }

                }
            } else {
                if (enumdirection == EnumDirection.UP && blockposition.getY() < worldserver.getMaxBuildHeight() - 1) {
                    if (this.b((IBlockAccess) worldserver, blockposition, enumdirection)) {
                        worldserver.setTypeAndData(blockposition, (IBlockData) iblockdata.set(BlockVine.UP, true), 2);
                        return;
                    }

                    if (worldserver.isEmpty(blockposition1)) {
                        if (!this.a((IBlockAccess) worldserver, blockposition)) {
                            return;
                        }

                        IBlockData iblockdata2 = iblockdata;
                        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                        while (iterator.hasNext()) {
                            enumdirection1 = (EnumDirection) iterator.next();
                            if (random.nextBoolean() || !a((IBlockAccess) worldserver, blockposition1.shift(enumdirection1), enumdirection1)) {
                                iblockdata2 = (IBlockData) iblockdata2.set(getDirection(enumdirection1), false);
                            }
                        }

                        if (this.canSpread(iblockdata2)) {
                            worldserver.setTypeAndData(blockposition1, iblockdata2, 2);
                        }

                        return;
                    }
                }

                if (blockposition.getY() > worldserver.getMinBuildHeight()) {
                    blockposition2 = blockposition.down();
                    iblockdata1 = worldserver.getType(blockposition2);
                    if (iblockdata1.isAir() || iblockdata1.a((Block) this)) {
                        IBlockData iblockdata3 = iblockdata1.isAir() ? this.getBlockData() : iblockdata1;
                        IBlockData iblockdata4 = this.a(iblockdata, iblockdata3, random);

                        if (iblockdata3 != iblockdata4 && this.canSpread(iblockdata4)) {
                            worldserver.setTypeAndData(blockposition2, iblockdata4, 2);
                        }
                    }
                }

            }
        }
    }

    private IBlockData a(IBlockData iblockdata, IBlockData iblockdata1, Random random) {
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();

            if (random.nextBoolean()) {
                BlockStateBoolean blockstateboolean = getDirection(enumdirection);

                if ((Boolean) iblockdata.get(blockstateboolean)) {
                    iblockdata1 = (IBlockData) iblockdata1.set(blockstateboolean, true);
                }
            }
        }

        return iblockdata1;
    }

    private boolean canSpread(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockVine.NORTH) || (Boolean) iblockdata.get(BlockVine.EAST) || (Boolean) iblockdata.get(BlockVine.SOUTH) || (Boolean) iblockdata.get(BlockVine.WEST);
    }

    private boolean a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        boolean flag = true;
        Iterable<BlockPosition> iterable = BlockPosition.b(blockposition.getX() - 4, blockposition.getY() - 1, blockposition.getZ() - 4, blockposition.getX() + 4, blockposition.getY() + 1, blockposition.getZ() + 4);
        int i = 5;
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            BlockPosition blockposition1 = (BlockPosition) iterator.next();

            if (iblockaccess.getType(blockposition1).a((Block) this)) {
                --i;
                if (i <= 0) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean a(IBlockData iblockdata, BlockActionContext blockactioncontext) {
        IBlockData iblockdata1 = blockactioncontext.getWorld().getType(blockactioncontext.getClickPosition());

        return iblockdata1.a((Block) this) ? this.o(iblockdata1) < BlockVine.PROPERTY_BY_DIRECTION.size() : super.a(iblockdata, blockactioncontext);
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        IBlockData iblockdata = blockactioncontext.getWorld().getType(blockactioncontext.getClickPosition());
        boolean flag = iblockdata.a((Block) this);
        IBlockData iblockdata1 = flag ? iblockdata : this.getBlockData();
        EnumDirection[] aenumdirection = blockactioncontext.f();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];

            if (enumdirection != EnumDirection.DOWN) {
                BlockStateBoolean blockstateboolean = getDirection(enumdirection);
                boolean flag1 = flag && (Boolean) iblockdata.get(blockstateboolean);

                if (!flag1 && this.b((IBlockAccess) blockactioncontext.getWorld(), blockactioncontext.getClickPosition(), enumdirection)) {
                    return (IBlockData) iblockdata1.set(blockstateboolean, true);
                }
            }
        }

        return flag ? iblockdata1 : null;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockVine.UP, BlockVine.NORTH, BlockVine.EAST, BlockVine.SOUTH, BlockVine.WEST);
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        switch (enumblockrotation) {
            case CLOCKWISE_180:
                return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockVine.NORTH, (Boolean) iblockdata.get(BlockVine.SOUTH))).set(BlockVine.EAST, (Boolean) iblockdata.get(BlockVine.WEST))).set(BlockVine.SOUTH, (Boolean) iblockdata.get(BlockVine.NORTH))).set(BlockVine.WEST, (Boolean) iblockdata.get(BlockVine.EAST));
            case COUNTERCLOCKWISE_90:
                return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockVine.NORTH, (Boolean) iblockdata.get(BlockVine.EAST))).set(BlockVine.EAST, (Boolean) iblockdata.get(BlockVine.SOUTH))).set(BlockVine.SOUTH, (Boolean) iblockdata.get(BlockVine.WEST))).set(BlockVine.WEST, (Boolean) iblockdata.get(BlockVine.NORTH));
            case CLOCKWISE_90:
                return (IBlockData) ((IBlockData) ((IBlockData) ((IBlockData) iblockdata.set(BlockVine.NORTH, (Boolean) iblockdata.get(BlockVine.WEST))).set(BlockVine.EAST, (Boolean) iblockdata.get(BlockVine.NORTH))).set(BlockVine.SOUTH, (Boolean) iblockdata.get(BlockVine.EAST))).set(BlockVine.WEST, (Boolean) iblockdata.get(BlockVine.SOUTH));
            default:
                return iblockdata;
        }
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        switch (enumblockmirror) {
            case LEFT_RIGHT:
                return (IBlockData) ((IBlockData) iblockdata.set(BlockVine.NORTH, (Boolean) iblockdata.get(BlockVine.SOUTH))).set(BlockVine.SOUTH, (Boolean) iblockdata.get(BlockVine.NORTH));
            case FRONT_BACK:
                return (IBlockData) ((IBlockData) iblockdata.set(BlockVine.EAST, (Boolean) iblockdata.get(BlockVine.WEST))).set(BlockVine.WEST, (Boolean) iblockdata.get(BlockVine.EAST));
            default:
                return super.a(iblockdata, enumblockmirror);
        }
    }

    public static BlockStateBoolean getDirection(EnumDirection enumdirection) {
        return (BlockStateBoolean) BlockVine.PROPERTY_BY_DIRECTION.get(enumdirection);
    }
}
