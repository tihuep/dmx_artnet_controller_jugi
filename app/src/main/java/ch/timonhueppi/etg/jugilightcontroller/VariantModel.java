package ch.timonhueppi.etg.jugilightcontroller;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VariantModel {

    private static final String COLOR_VARIANTS = "color_variants";
    private static final String WHITE_VARIANTS = "white_variants";

    public static List<ColorVariant> colorVariants = new ArrayList<>();
    public static List<WhiteVariant> whiteVariants = new ArrayList<>();

    public static void loadVariants(SharedPreferences sharedPreferences){
        String colorVariantsStr = sharedPreferences.getString(COLOR_VARIANTS, "[]");
        String whiteVariantsStr = sharedPreferences.getString(WHITE_VARIANTS, "[]");

        Type colorListType = new TypeToken<ArrayList<ColorVariant>>(){}.getType();
        Type whiteListType = new TypeToken<ArrayList<WhiteVariant>>(){}.getType();
        colorVariants = new Gson().fromJson(colorVariantsStr, colorListType);
        colorVariants = new Gson().fromJson(whiteVariantsStr, whiteListType);
    }

    public static void saveVariants(SharedPreferences sharedPreferences){
        String colorVariantsStr = new Gson().toJson(colorVariants);
        String whiteVariantsStr = new Gson().toJson(whiteVariants);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(COLOR_VARIANTS);
        editor.remove(WHITE_VARIANTS);
        editor.putString(COLOR_VARIANTS, colorVariantsStr);
        editor.putString(WHITE_VARIANTS, whiteVariantsStr);

        editor.apply();
    }
}
