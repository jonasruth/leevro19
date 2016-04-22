package leevro.pucpr.br.leevro19;

import android.content.Context;

import leevro.pucpr.br.leevro19.entity.AppUser;

public class PrefUtils {

    public static void setCurrentUser(AppUser currentUser, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static AppUser getCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        AppUser currentUser = complexPreferences.getObject("current_user_value", AppUser.class);
        return currentUser;
    }

    public static void clearCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }


}