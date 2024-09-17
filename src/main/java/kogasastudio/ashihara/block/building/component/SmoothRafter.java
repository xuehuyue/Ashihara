package kogasastudio.ashihara.block.building.component;

import kogasastudio.ashihara.block.tileentities.MultiBuiltBlockEntity;
import kogasastudio.ashihara.helper.ShapeHelper;
import kogasastudio.ashihara.registry.BuildingComponents;
import kogasastudio.ashihara.utils.BuildingComponentModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

import static kogasastudio.ashihara.helper.PositionHelper.XTP;

public class SmoothRafter extends AdditionalComponent
{
    private final BuildingComponentModelResourceLocation MODEL;

    protected VoxelShape SHAPE;

    public SmoothRafter
    (
        String idIn,
        BuildingComponents.Type typeIn,
        BuildingComponentModelResourceLocation model,
        VoxelShape shape,
        List<ItemStack> dropsIn
    )
    {
        super(idIn, typeIn, dropsIn);
        this.MODEL = model;
        this.SHAPE = shape;
    }

    public SmoothRafter
    (
        String idIn,
        BuildingComponents.Type typeIn,
        BuildingComponentModelResourceLocation model,
        List<ItemStack> dropsIn
    )
    {
        this(idIn, typeIn, model, null, dropsIn);
        initShape();
    }

    private void initShape()
    {
        this.SHAPE = Shapes.box(0.25, 0, 0, 0.75, 0.5, 1);
    }

    @Override
    public ComponentStateDefinition definite(MultiBuiltBlockEntity beIn, UseOnContext context)
    {
        Direction direction = context.getHorizontalDirection();
        direction = beIn.fromAbsolute(direction);
        Vec3 inBlockPos = beIn.transformVec3(beIn.inBlockVec(context.getClickLocation()));

        float r = direction.getAxis().equals(Direction.Axis.X) ? 90 : 0;
        double y = inBlockPos.y();

        int floor = (int) Math.clamp(Math.floor(y * 4), 0, 3);

        y = XTP((float) (floor * 4));

        Occupation occupation = Occupation.CENTER_ALL.get(floor);

        VoxelShape shape = SHAPE;
        shape = ShapeHelper.rotateShape(shape, -r);
        shape = ShapeHelper.offsetShape(shape, 0, y, 0);

        return new ComponentStateDefinition
        (
            BuildingComponents.get(this.id),
            new Vec3(0, y, 0),
            0, r, 0,
            shape,
            MODEL,
            List.of(occupation)
        );
    }
}
