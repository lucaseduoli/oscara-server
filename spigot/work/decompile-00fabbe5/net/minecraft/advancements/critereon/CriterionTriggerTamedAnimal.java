package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.level.storage.loot.LootTableInfo;

public class CriterionTriggerTamedAnimal extends CriterionTriggerAbstract<CriterionTriggerTamedAnimal.a> {

    static final MinecraftKey ID = new MinecraftKey("tame_animal");

    public CriterionTriggerTamedAnimal() {}

    @Override
    public MinecraftKey a() {
        return CriterionTriggerTamedAnimal.ID;
    }

    @Override
    public CriterionTriggerTamedAnimal.a b(JsonObject jsonobject, CriterionConditionEntity.b criterionconditionentity_b, LootDeserializationContext lootdeserializationcontext) {
        CriterionConditionEntity.b criterionconditionentity_b1 = CriterionConditionEntity.b.a(jsonobject, "entity", lootdeserializationcontext);

        return new CriterionTriggerTamedAnimal.a(criterionconditionentity_b, criterionconditionentity_b1);
    }

    public void a(EntityPlayer entityplayer, EntityAnimal entityanimal) {
        LootTableInfo loottableinfo = CriterionConditionEntity.b(entityplayer, entityanimal);

        this.a(entityplayer, (criteriontriggertamedanimal_a) -> {
            return criteriontriggertamedanimal_a.a(loottableinfo);
        });
    }

    public static class a extends CriterionInstanceAbstract {

        private final CriterionConditionEntity.b entity;

        public a(CriterionConditionEntity.b criterionconditionentity_b, CriterionConditionEntity.b criterionconditionentity_b1) {
            super(CriterionTriggerTamedAnimal.ID, criterionconditionentity_b);
            this.entity = criterionconditionentity_b1;
        }

        public static CriterionTriggerTamedAnimal.a c() {
            return new CriterionTriggerTamedAnimal.a(CriterionConditionEntity.b.ANY, CriterionConditionEntity.b.ANY);
        }

        public static CriterionTriggerTamedAnimal.a a(CriterionConditionEntity criterionconditionentity) {
            return new CriterionTriggerTamedAnimal.a(CriterionConditionEntity.b.ANY, CriterionConditionEntity.b.a(criterionconditionentity));
        }

        public boolean a(LootTableInfo loottableinfo) {
            return this.entity.a(loottableinfo);
        }

        @Override
        public JsonObject a(LootSerializationContext lootserializationcontext) {
            JsonObject jsonobject = super.a(lootserializationcontext);

            jsonobject.add("entity", this.entity.a(lootserializationcontext));
            return jsonobject;
        }
    }
}
