package ch.timonhueppi.etg.jugilightcontroller;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VariantModel {

    public static List<ColorVariant> colorVariants = new ArrayList<>();

    public static void loadColorVariants(SharedPreferences sharedPreferences){
        String colorVariantsStr = sharedPreferences.getString("color_variants", "[]");

        Type listType = new TypeToken<ArrayList<ColorVariant>>(){}.getType();
        colorVariants = new Gson().fromJson(colorVariantsStr, listType);
    }

    public static void saveColorVariants(SharedPreferences sharedPreferences){
        String colorVariantsStr = new Gson().toJson(colorVariants);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("color_variants");
        editor.putString("color_variants", colorVariantsStr);

        editor.apply();
    }
}
