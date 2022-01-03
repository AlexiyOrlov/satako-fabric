package dev.buildtool.satako;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class EntityRenderer<E extends LivingEntity, M extends EntityModel<E>> extends LivingEntityRenderer<E, M> {
    private final Identifier texture;
    private final boolean renderName;

    public EntityRenderer(EntityRendererFactory.Context ctx, M model, String mod, String texture, boolean renderName, float shadowRadius) {
        super(ctx, model, shadowRadius);
        this.texture = new Identifier(mod, "textures/entity/" + texture + ".png");
        this.renderName = renderName;
    }

    @Override
    public Identifier getTexture(E entity) {
        return texture;
    }

    @Override
    protected boolean hasLabel(E livingEntity) {
        return renderName && super.hasLabel(livingEntity);
    }
}
