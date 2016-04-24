package leevro.pucpr.br.leevro19.utils;

import android.content.Context;

import leevro.pucpr.br.leevro19.ComplexPreferences;
import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.entity.Book;

public class PrefUtils {


    public static void setCurrentPublicProfile(AppUser currentPublicProfile, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "public_profile_prefs", 0);
        complexPreferences.putObject("current_public_profile_value", currentPublicProfile);
        complexPreferences.commit();
    }

    /**
     * RETORNA PERFIL DE USUÁRIO A SER VISTO
     * @param ctx
     * @return
     */
    public static AppUser getCurrentPublicProfile(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "public_profile_prefs", 0);
        AppUser currentPublicProfile = complexPreferences.getObject("current_public_profile_value", AppUser.class);
        return currentPublicProfile;
    }

    public static void setCurrentBook(Book currentBook, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "book_prefs", 0);
        complexPreferences.putObject("current_book_value", currentBook);
        complexPreferences.commit();
    }

    /**
     * RETORNA O LIVRO ATUAL A SER VISTO
     * @param ctx
     * @return
     */
    public static Book getCurrentBook(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "book_prefs", 0);
        Book currentBook = complexPreferences.getObject("current_book_value", Book.class);
        return currentBook;
    }

    public static void clearCurrentBook(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "book_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

    public static void setCurrentUser(AppUser currentUser, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    /**
     * RETORNA O USUÁRIO LOGADO NO APLICATIVO
     * @param ctx
     * @return
     */
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