package com.oierbravo.melter.content.melter;

import com.oierbravo.melter.Melter;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public enum HeatSources implements StringRepresentable {
    NONE(0,"", "None"),
    // TORCH(1,"minecraft:torch", "Torch"),
    // SOUL_TORCH(1,"minecraft:soul_torch", "Soul Torch"),
    // WALL_TORCH(1,"minecraft:wall_torch", "Torch"),
    PORCELAIN_HEATER(8,"the_ages_of_times:porcelain_heater_on", "Porcelain Heater"),;
    // CAMPFIRE(2,"minecraft:campfire", "Campfire"),
    // SOUL_CAMPFIRE(2,"minecraft:soul_campfire", "Soul Campfire"),
    // LAVA(3,"minecraft:lava", "Lava"),
    // BLAZE_BURNER_INACTIVE(0,"create:blaze_burner:smouldering", "Blaze burner Inactive"),
    // BLAZE_BURNER_FADING(1,"create:blaze_burner:fading","Blaze burner Fading"),
    // BLAZE_BURNER_ACTIVE(5,"create:blaze_burner:kindled","Blaze burner Active"),
    // BLAZE_BURNER_SUPERHEATED(6,"create:blaze_burner:seething","Blaze burner SUPERHEATED!");
    // CREATE HeatLevel: NONE, SMOULDERING, FADING, KINDLED, SEETHING,;
    private int multiplier;
    private String resourceName;
    private String displayName;
    HeatSources (int multiplier, String resourceName, String displayName){
        this.multiplier = multiplier;
        this.resourceName = resourceName;
        this.displayName = displayName;
    }

    public static HeatSources fromLevel(Level level, BlockPos below) {
        BlockState belowBlockState = level.getBlockState(below);
        return HeatSources.get(belowBlockState);
    }

    public int getMultiplier() {
        return multiplier;
    }

    public String getResourceName() {
        return resourceName;
    }

    public static HeatSources get(int minimumHeat){
        return Arrays.stream(HeatSources.values())
                .filter(e -> e.multiplier > minimumHeat)
                .findFirst()
                .orElse(HeatSources.NONE);
    }

    public static HeatSources get(String resourceName){
        return Arrays.stream(HeatSources.values())
                .filter(e -> e.resourceName.equals(resourceName))
                .findFirst()
                .orElse(HeatSources.NONE);
    }
    public static HeatSources get(BlockState blockState){
        ResourceLocation resourceLocation = blockState.getBlock().getRegistryName();
        String nameString = resourceLocation.toString();
        if(Melter.withCreate && (blockState.hasProperty(BlazeBurnerBlock.HEAT_LEVEL))){
            BlazeBurnerBlock.HeatLevel heatLevel = blockState.getValue(BlazeBurnerBlock.HEAT_LEVEL);
            nameString += ":" +heatLevel.getSerializedName();
        }
        return get(nameString);
    }

    //public static HeatSources get(int multiplier){
    //    return Arrays.stream(HeatSources.values())
    //            .filter(e -> e.multiplier >= multiplier )
    //           .findFirst()
    //            .orElse(HeatSources.NONE);
    //}
    public static boolean isHeatSource(BlockState blockState){
        if(Melter.withCreate && blockState.hasProperty(BlazeBurnerBlock.HEAT_LEVEL)){
            return true;
        }
        ResourceLocation name = blockState.getBlock().getRegistryName();
        HeatSources source = HeatSources.get(name.toString());
        if(source != HeatSources.NONE){
            return true;
        }
        return false;
    }
    public static ItemStack getItemStackFromMultiplier(int multiplier){
        HeatSources heatSource = get(multiplier);
        if(heatSource == HeatSources.NONE)
            return ItemStack.EMPTY;

        String resourceName = heatSource.getResourceName();
        if(resourceName.matches("create:blaze_burner(.*)")){
            resourceName = "create:blaze_burner";

        }
        ResourceLocation resourceLocation = ResourceLocation.tryParse(resourceName);
        return new ItemStack(ForgeRegistries.ITEMS.getValue(resourceLocation));
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getDisplayName() {
        return displayName;
    }
}
