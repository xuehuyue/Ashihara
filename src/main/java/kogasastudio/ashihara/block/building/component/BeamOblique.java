package kogasastudio.ashihara.block.building.component;

import kogasastudio.ashihara.block.tileentities.MultiBuiltBlockEntity;
import kogasastudio.ashihara.helper.ShapeHelper;
import kogasastudio.ashihara.registry.BuildingComponents;
import kogasastudio.ashihara.utils.BuildingComponentModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.function.Supplier;

import static kogasastudio.ashihara.helper.PositionHelper.XTP;
import static kogasastudio.ashihara.helper.PositionHelper.coordsInRangeFixedY;

public class BeamOblique extends AdditionalComponent
{
    private final BuildingComponentModelResourceLocation MODEL;

    private VoxelShape SHAPE;

    public BeamOblique
    (
        String idIn,
        BuildingComponents.Type typeIn,
        BuildingComponentModelResourceLocation modelIn,
        List<ItemStack> dropsIn
    )
    {
        super(idIn, typeIn, dropsIn);
        this.MODEL = modelIn;
        initShape();
    }

    protected void initShape()
    {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-0.1875, 0, -0.1875, 0.5, 0.5, 0.5), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.5, 0, 0.5, 1.1875, 0.5, 1.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.140625, 0, 0.140625, 0.859375, 0.5, 0.859375), BooleanOp.OR);
        this.SHAPE = shape;
    }

    @Override
    public ComponentStateDefinition definite(MultiBuiltBlockEntity beIn, UseOnContext context)
    {
        Direction direction = context.getHorizontalDirection();
        direction = beIn.fromAbsolute(direction);

        float r = direction.getAxis() == Direction.Axis.Z ? 0 : 90;

        Vec3 inBlockPos = beIn.transformVec3(beIn.inBlockVec(context.getClickLocation()));
        double y = inBlockPos.y();

        y = coordsInRangeFixedY(context.getClickedFace(), y, 0, XTP(8)) ? 0 : XTP(8);

        int floor = y == 0 ? 0 : 2;
        Occupation occupation = Occupation.CENTER_ALL.get(floor);

        VoxelShape shape = SHAPE;
        shape = ShapeHelper.rotateShape(shape, r);
        shape = ShapeHelper.offsetShape(shape, 0, y, 0);

        return new ComponentStateDefinition
        (
            BuildingComponents.get(this.id),
            new Vec3(0, y, 0),
            0, r + 45, 0,
            shape,
            MODEL,
            List.of(occupation)
        );
    }
}
