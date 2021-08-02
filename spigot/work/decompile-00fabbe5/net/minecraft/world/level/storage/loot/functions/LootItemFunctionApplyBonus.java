package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import net.minecraft.core.IRegistry;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import net.minecraft.world.level.storage.loot.LootTableInfo;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameter;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameters;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootItemFunctionApplyBonus extends LootItemFunctionConditional {

    static final Map<MinecraftKey, LootItemFunctionApplyBonus.c> FORMULAS = Maps.newHashMap();
    final Enchantment enchantment;
    final LootItemFunctionApplyBonus.b formula;

    LootItemFunctionApplyBonus(LootItemCondition[] alootitemcondition, Enchantment enchantment, LootItemFunctionApplyBonus.b lootitemfunctionapplybonus_b) {
        super(alootitemcondition);
        this.enchantment = enchantment;
        this.formula = lootitemfunctionapplybonus_b;
    }

    @Override
    public LootItemFunctionType a() {
        return LootItemFunctions.APPLY_BONUS;
    }

    @Override
    public Set<LootContextParameter<?>> b() {
        return ImmutableSet.of(LootContextParameters.TOOL);
    }

    @Override
    public ItemStack a(ItemStack itemstack, LootTableInfo loottableinfo) {
        ItemStack itemstack1 = (ItemStack) loottableinfo.getContextParameter(LootContextParameters.TOOL);

        if (itemstack1 != null) {
            int i = EnchantmentManager.getEnchantmentLevel(this.enchantment, itemstack1);
            int j = this.formula.a(loottableinfo.a(), itemstack.getCount(), i);

            itemstack.setCount(j);
        }

        return itemstack;
    }

    public static LootItemFunctionConditional.a<?> a(Enchantment enchantment, float f, int i) {
        return a((alootitemcondition) -> {
            return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, new LootItemFunctionApplyBonus.a(i, f));
        });
    }

    public static LootItemFunctionConditional.a<?> a(Enchantment enchantment) {
        return a((alootitemcondition) -> {
            return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, new LootItemFunctionApplyBonus.d());
        });
    }

    public static LootItemFunctionConditional.a<?> b(Enchantment enchantment) {
        return a((alootitemcondition) -> {
            return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, new LootItemFunctionApplyBonus.f(1));
        });
    }

    public static LootItemFunctionConditional.a<?> a(Enchantment enchantment, int i) {
        return a((alootitemcondition) -> {
            return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, new LootItemFunctionApplyBonus.f(i));
        });
    }

    static {
        LootItemFunctionApplyBonus.FORMULAS.put(LootItemFunctionApplyBonus.a.TYPE, LootItemFunctionApplyBonus.a::a);
        LootItemFunctionApplyBonus.FORMULAS.put(LootItemFunctionApplyBonus.d.TYPE, LootItemFunctionApplyBonus.d::a);
        LootItemFunctionApplyBonus.FORMULAS.put(LootItemFunctionApplyBonus.f.TYPE, LootItemFunctionApplyBonus.f::a);
    }

    private interface b {

        int a(Random random, int i, int j);

        void a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext);

        MinecraftKey a();
    }

    private static final class f implements LootItemFunctionApplyBonus.b {

        public static final MinecraftKey TYPE = new MinecraftKey("uniform_bonus_count");
        private final int bonusMultiplier;

        public f(int i) {
            this.bonusMultiplier = i;
        }

        @Override
        public int a(Random random, int i, int j) {
            return i + random.nextInt(this.bonusMultiplier * j + 1);
        }

        @Override
        public void a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("bonusMultiplier", this.bonusMultiplier);
        }

        public static LootItemFunctionApplyBonus.b a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            int i = ChatDeserializer.n(jsonobject, "bonusMultiplier");

            return new LootItemFunctionApplyBonus.f(i);
        }

        @Override
        public MinecraftKey a() {
            return LootItemFunctionApplyBonus.f.TYPE;
        }
    }

    private static final class d implements LootItemFunctionApplyBonus.b {

        public static final MinecraftKey TYPE = new MinecraftKey("ore_drops");

        d() {}

        @Override
        public int a(Random random, int i, int j) {
            if (j > 0) {
                int k = random.nextInt(j + 2) - 1;

                if (k < 0) {
                    k = 0;
                }

                return i * (k + 1);
            } else {
                return i;
            }
        }

        @Override
        public void a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {}

        public static LootItemFunctionApplyBonus.b a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            return new LootItemFunctionApplyBonus.d();
        }

        @Override
        public MinecraftKey a() {
            return LootItemFunctionApplyBonus.d.TYPE;
        }
    }

    private static final class a implements LootItemFunctionApplyBonus.b {

        public static final MinecraftKey TYPE = new MinecraftKey("binomial_with_bonus_count");
        private final int extraRounds;
        private final float probability;

        public a(int i, float f) {
            this.extraRounds = i;
            this.probability = f;
        }

        @Override
        public int a(Random random, int i, int j) {
            for (int k = 0; k < j + this.extraRounds; ++k) {
                if (random.nextFloat() < this.probability) {
                    ++i;
                }
            }

            return i;
        }

        @Override
        public void a(JsonObject jsonobject, JsonSerializationContext jsonserializationcontext) {
            jsonobject.addProperty("extra", this.extraRounds);
            jsonobject.addProperty("probability", this.probability);
        }

        public static LootItemFunctionApplyBonus.b a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            int i = ChatDeserializer.n(jsonobject, "extra");
            float f = ChatDeserializer.l(jsonobject, "probability");

            return new LootItemFunctionApplyBonus.a(i, f);
        }

        @Override
        public MinecraftKey a() {
            return LootItemFunctionApplyBonus.a.TYPE;
        }
    }

    private interface c {

        LootItemFunctionApplyBonus.b deserialize(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext);
    }

    public static class e extends LootItemFunctionConditional.c<LootItemFunctionApplyBonus> {

        public e() {}

        public void a(JsonObject jsonobject, LootItemFunctionApplyBonus lootitemfunctionapplybonus, JsonSerializationContext jsonserializationcontext) {
            super.a(jsonobject, (LootItemFunctionConditional) lootitemfunctionapplybonus, jsonserializationcontext);
            jsonobject.addProperty("enchantment", IRegistry.ENCHANTMENT.getKey(lootitemfunctionapplybonus.enchantment).toString());
            jsonobject.addProperty("formula", lootitemfunctionapplybonus.formula.a().toString());
            JsonObject jsonobject1 = new JsonObject();

            lootitemfunctionapplybonus.formula.a(jsonobject1, jsonserializationcontext);
            if (jsonobject1.size() > 0) {
                jsonobject.add("parameters", jsonobject1);
            }

        }

        @Override
        public LootItemFunctionApplyBonus b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext, LootItemCondition[] alootitemcondition) {
            MinecraftKey minecraftkey = new MinecraftKey(ChatDeserializer.h(jsonobject, "enchantment"));
            Enchantment enchantment = (Enchantment) IRegistry.ENCHANTMENT.getOptional(minecraftkey).orElseThrow(() -> {
                return new JsonParseException("Invalid enchantment id: " + minecraftkey);
            });
            MinecraftKey minecraftkey1 = new MinecraftKey(ChatDeserializer.h(jsonobject, "formula"));
            LootItemFunctionApplyBonus.c lootitemfunctionapplybonus_c = (LootItemFunctionApplyBonus.c) LootItemFunctionApplyBonus.FORMULAS.get(minecraftkey1);

            if (lootitemfunctionapplybonus_c == null) {
                throw new JsonParseException("Invalid formula id: " + minecraftkey1);
            } else {
                LootItemFunctionApplyBonus.b lootitemfunctionapplybonus_b;

                if (jsonobject.has("parameters")) {
                    lootitemfunctionapplybonus_b = lootitemfunctionapplybonus_c.deserialize(ChatDeserializer.t(jsonobject, "parameters"), jsondeserializationcontext);
                } else {
                    lootitemfunctionapplybonus_b = lootitemfunctionapplybonus_c.deserialize(new JsonObject(), jsondeserializationcontext);
                }

                return new LootItemFunctionApplyBonus(alootitemcondition, enchantment, lootitemfunctionapplybonus_b);
            }
        }
    }
}
