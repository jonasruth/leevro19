package leevro.pucpr.br.leevro19.utils;

import android.content.Context;

import leevro.pucpr.br.leevro19.app.ComplexPreferences;
import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.entity.Book;

public class PrefUtils {

    public static void setCurrentISBN(String isbn, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "isbn_prefs", 0);
        complexPreferences.putObject("current_isbn_value", isbn);
        complexPreferences.commit();
    }

    public static String getCurrentISBN(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "isbn_prefs", 0);
        String isbn = complexPreferences.getObject("current_isbn_value", String.class);
        return isbn;
    }

    public static void clearCurrentISBN(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "isbn_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

    public static void setCurrentBook(Book currentBook, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "book_prefs", 0);
        complexPreferences.putObject("current_book_value", currentBook);
        complexPreferences.commit();
    }

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

    public static void setTargerUser(AppUser currentPublicProfile, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "public_profile_prefs", 0);
        complexPreferences.putObject("current_public_profile_value", currentPublicProfile);
        complexPreferences.commit();
    }

    public static AppUser getTargetUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "public_profile_prefs", 0);
        AppUser currentPublicProfile = complexPreferences.getObject("current_public_profile_value", AppUser.class);
        return currentPublicProfile;
    }

    public static void clearTargetUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "public_profile_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

    public static void setLoggedUser(AppUser currentUser, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.putObject("current_user_value", currentUser);
        complexPreferences.commit();
    }

    public static AppUser getLoggedUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        AppUser currentUser = complexPreferences.getObject("current_user_value", AppUser.class);
        return currentUser;
    }

    public static void clearLoggedUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_prefs", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }


}