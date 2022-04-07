package ch.timonhueppi.etg.jugilightcontroller.model;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading of variants
 *
 * @author Timon Hüppi @tihuep
 * @version 1.0
 * @since 2022/01/15
 */
public class VariantModel {

    /**
     * Sharedprefs keys of the variants
     */
    private static final String COLOR_VARIANTS = "color_variants";
    private static final String WHITE_VARIANTS = "white_variants";

    /**
     * Local lists of saved variants
     */
    public static List<ColorVariant> colorVariants = new ArrayList<>();
    public static List<WhiteVariant> whiteVariants = new ArrayList<>();

    /**
     * Lads all variants from sharedprefs to local lists
     *
     * @param sharedPreferences Sharedprefs reference from context (activity)
     */
    public static void loadVariants(SharedPreferences sharedPreferences) {
        // Loads json strings from sharedprefs
        String colorVariantsStr = sharedPreferences.getString(COLOR_VARIANTS, "[]");
        String whiteVariantsStr = sharedPreferences.getString(WHITE_VARIANTS, "[]");

        //Creates list types for converting json to lists
        Type colorListType = new TypeToken<ArrayList<ColorVariant>>(){}.getType();
        Type whiteListType = new TypeToken<ArrayList<WhiteVariant>>(){}.getType();

        // Creates lists out of json string storing all variants
        colorVariants = new Gson().fromJson(colorVariantsStr, colorListType);
        whiteVariants = new Gson().fromJson(whiteVariantsStr, whiteListType);
    }

    /**
     * Saves all locally saved variants to sharedprefs
     *
     * @param sharedPreferences Sharedprefs reference from context (activity)
     */
    public static void saveVariants(SharedPreferences sharedPreferences) {
        // Produces json strings out of local variant lists
        String colorVariantsStr = new Gson().toJson(colorVariants);
        String whiteVariantsStr = new Gson().toJson(whiteVariants);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clears current sharedprefs variant storage
        editor.remove(COLOR_VARIANTS);
        editor.remove(WHITE_VARIANTS);

        // Saves json strings containing the variants to sharedprefs
        editor.putString(COLOR_VARIANTS, colorVariantsStr);
        editor.putString(WHITE_VARIANTS, whiteVariantsStr);

        editor.apply();
    }
}
