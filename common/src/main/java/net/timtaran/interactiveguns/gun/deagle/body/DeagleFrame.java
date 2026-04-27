package net.timtaran.interactiveguns.gun.deagle.body;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import com.github.stephengold.joltjni.readonly.RVec3Arg;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;
import net.timtaran.interactivemc.body.player.PlayerBodyPart;
import net.timtaran.interactivemc.body.type.IGrabbable;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.VxBodyType;
import net.xmx.velthoric.core.body.factory.VxRigidBodyFactory;
import net.xmx.velthoric.core.physics.VxPhysicsLayers;
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DeagleFrame extends VxBody implements IGrabbable {
    private static final RVec3 GRAB_POINT = new RVec3(0f, 0.046875f, -0.078125f); // todo replace with RVec3Arg

    public DeagleFrame(VxBodyType type, VxPhysicsWorld physicsWorld, UUID id) {
        super(type, physicsWorld, id);
    }

    @Environment(EnvType.CLIENT)
    public DeagleFrame(VxBodyType type, UUID id) {
        super(type, id);
    }


    public static int createJoltBody(VxBody body, VxRigidBodyFactory factory) {
        Vec3 fullSize = new Vec3(0.0375f, 0.1625f, 0.275f);

        try (ShapeSettings shapeSettings = new BoxShapeSettings(new Vec3(fullSize.getX() / 2, fullSize.getY() / 2, fullSize.getZ() / 2)); BodyCreationSettings bcs = new BodyCreationSettings()) {
            bcs.setMotionType(EMotionType.Dynamic);
            bcs.setObjectLayer(VxPhysicsLayers.MOVING);
            return factory.create(shapeSettings, bcs);
        }
    }

    @Override
    public @Nullable RVec3 getGrabPoint(RVec3 intersectionPoint) {
        return GRAB_POINT; // todo: allow only 1 grab
    }

    @Override
    public @Nullable RVec3 getRemoteGrabPoint(RVec3 intersectionPoint) {
        return GRAB_POINT;
    }
}
