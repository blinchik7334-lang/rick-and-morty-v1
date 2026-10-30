package com.example.rickmorty;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RickMortyPortalMod implements ModInitializer {
    public static final String MOD_ID = "rickmortyportal";

    // Должен быть объявлен ДО предметов одежды (порядок статической инициализации).
    private static final List<Item> GUNS = new ArrayList<>();
    private static final List<Item> CLOTHING = new ArrayList<>();

    public static final Item PORTAL_GUN = gun("portal_gun", 1.0f, 0x35E66B);
    public static final Item PRIME_PORTAL_GUN = gun("prime_portal_gun", 1.35f, 0x35E66B);
    public static final Item EVIL_MORTY_PORTAL_GUN = gun("evil_morty_portal_gun", 1.2f, 0xF2D52E);
    public static final Item KID_PORTAL_GUN = gun("kid_portal_gun", 0.75f, 0x35E66B);

    // --- Рик ---
    public static final Item RICK_HAIR = clothing("rick_hair", CharacterArmorMaterial.RICK, ArmorItem.Type.HELMET);
    public static final Item RICK_COAT = clothing("rick_coat", CharacterArmorMaterial.RICK, ArmorItem.Type.CHESTPLATE);
    public static final Item RICK_PANTS = clothing("rick_pants", CharacterArmorMaterial.RICK, ArmorItem.Type.LEGGINGS);
    public static final Item RICK_SHOES = clothing("rick_shoes", CharacterArmorMaterial.RICK, ArmorItem.Type.BOOTS);

    // --- Рик Прайм ---
    public static final Item RICK_PRIME_HAIR = clothing("rick_prime_hair", CharacterArmorMaterial.RICK_PRIME, ArmorItem.Type.HELMET);
    public static final Item RICK_PRIME_COAT = clothing("rick_prime_coat", CharacterArmorMaterial.RICK_PRIME, ArmorItem.Type.CHESTPLATE);
    public static final Item RICK_PRIME_PANTS = clothing("rick_prime_pants", CharacterArmorMaterial.RICK_PRIME, ArmorItem.Type.LEGGINGS);
    public static final Item RICK_PRIME_SHOES = clothing("rick_prime_shoes", CharacterArmorMaterial.RICK_PRIME, ArmorItem.Type.BOOTS);

    // --- Злой Морти ---
    public static final Item EVIL_MORTY_HAIR = clothing("evil_morty_hair", CharacterArmorMaterial.EVIL_MORTY, ArmorItem.Type.HELMET);
    public static final Item EVIL_MORTY_COAT = clothing("evil_morty_coat", CharacterArmorMaterial.EVIL_MORTY, ArmorItem.Type.CHESTPLATE);
    public static final Item EVIL_MORTY_PANTS = clothing("evil_morty_pants", CharacterArmorMaterial.EVIL_MORTY, ArmorItem.Type.LEGGINGS);
    public static final Item EVIL_MORTY_SHOES = clothing("evil_morty_shoes", CharacterArmorMaterial.EVIL_MORTY, ArmorItem.Type.BOOTS);

    // --- Морти ---
    public static final Item MORTY_HAIR = clothing("morty_hair", CharacterArmorMaterial.MORTY, ArmorItem.Type.HELMET);
    public static final Item MORTY_COAT = clothing("morty_coat", CharacterArmorMaterial.MORTY, ArmorItem.Type.CHESTPLATE);
    public static final Item MORTY_PANTS = clothing("morty_pants", CharacterArmorMaterial.MORTY, ArmorItem.Type.LEGGINGS);
    public static final Item MORTY_SHOES = clothing("morty_shoes", CharacterArmorMaterial.MORTY, ArmorItem.Type.BOOTS);

    public static final ItemGroup MAIN_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(MOD_ID, "main"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup." + MOD_ID + ".main"))
                    .icon(() -> new ItemStack(PORTAL_GUN))
                    .entries((context, entries) -> {
                        GUNS.forEach(entries::add);
                        CLOTHING.forEach(entries::add);
                    })
                    .build());

    private static Item gun(String name, float range, int color) {
        Item item = register(name, new PortalGunItem(new FabricItemSettings().maxCount(1), range, color));
        GUNS.add(item);
        return item;
    }

    private static Item clothing(String name, ArmorMaterial material, ArmorItem.Type type) {
        Item item = register(name, new ArmorItem(material, type, new FabricItemSettings()));
        CLOTHING.add(item);
        return item;
    }

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);
    }

    @Override
    public void onInitialize() {
        PortalGunItem.registerServerTick();
    }
}
