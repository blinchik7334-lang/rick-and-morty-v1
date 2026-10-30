package com.example.rickmorty;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

/**
 * Материалы одежды персонажей. Защита как у кожаной брони.
 * Текстуры лежат в assets/minecraft/textures/models/armor/<name>_layer_1|2.png,
 * потому что ванильный ArmorFeatureRenderer 1.20.1 не понимает "modid:name".
 */
public enum CharacterArmorMaterial implements ArmorMaterial {
    RICK("rickmorty_rick"),
    RICK_PRIME("rickmorty_rick_prime"),
    EVIL_MORTY("rickmorty_evil_morty"),
    MORTY("rickmorty_morty");

    private static final int DURABILITY_MULTIPLIER = 15;
    private final String name;

    CharacterArmorMaterial(String name) { this.name = name; }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 13;
            case LEGGINGS -> 15;
            case CHESTPLATE -> 16;
            case HELMET -> 11;
        } * DURABILITY_MULTIPLIER;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS, HELMET -> 1;
            case LEGGINGS -> 2;
            case CHESTPLATE -> 3;
        };
    }

    @Override public int getEnchantability() { return 15; }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.ofItems(Items.LEATHER); }
    @Override public String getName() { return name; }
    @Override public float getToughness() { return 0.0f; }
    @Override public float getKnockbackResistance() { return 0.0f; }
}
