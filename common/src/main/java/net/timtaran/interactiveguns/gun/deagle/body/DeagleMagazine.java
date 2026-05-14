package net.timtaran.interactiveguns.gun.deagle.body;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.xmx.velthoric.core.body.VxBody;
import net.xmx.velthoric.core.body.VxBodyType;
import net.xmx.velthoric.core.body.factory.VxRigidBodyFactory;
import net.xmx.velthoric.core.network.synchronization.VxDataSerializers;
import net.xmx.velthoric.core.network.synchronization.accessor.VxServerAccessor;
import net.xmx.velthoric.core.physics.VxPhysicsLayers;
import net.xmx.velthoric.core.physics.world.VxPhysicsWorld;

import java.util.UUID;

public class DeagleMagazine extends VxBody {
    public static final VxServerAccessor<Integer> CARTRIDGE_AMOUNT = VxServerAccessor.create(DeagleMagazine.class, VxDataSerializers.INTEGER);
    private static final float EMPTY_MASS = 0.2f;
    private static final float CARTRIDGE_MASS = 0.02f;

    public DeagleMagazine(VxBodyType type, VxPhysicsWorld physicsWorld, UUID id) {
        super(type, physicsWorld, id);
    }

    @Environment(EnvType.CLIENT)
    public DeagleMagazine(VxBodyType type, UUID id) {
        super(type, id);
    }


    public static int createJoltBody(VxBody body, VxRigidBodyFactory factory) {
        Vec3 fullSize = new Vec3(0.3f, 0.3f, 0.3f);

        try (ShapeSettings shapeSettings = new BoxShapeSettings(new Vec3(fullSize.getX() / 2, fullSize.getY() / 2, fullSize.getZ() / 2)); BodyCreationSettings bcs = new BodyCreationSettings()) {
            bcs.setMotionType(EMotionType.Dynamic);
            bcs.setObjectLayer(VxPhysicsLayers.MOVING);

            MassProperties massProperties = bcs.getMassPropertiesOverride();
            massProperties.scaleToMass(EMPTY_MASS + CARTRIDGE_MASS * body.get(CARTRIDGE_AMOUNT));
            bcs.setMassPropertiesOverride(massProperties);

            return factory.create(shapeSettings, bcs);
        }
    }

}
