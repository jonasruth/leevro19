package leevro.pucpr.br.leevro19.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import leevro.pucpr.br.leevro19.entity.AppUser;
import leevro.pucpr.br.leevro19.entity.Book;

public class AppUtils {

    // DIRETORIOS DE IMAGENS
    public static final String APP_PATH_VIRTUAL_BOOK_COVER_PATH = "http://96.126.115.143/leevrows/vbook_img/";
    public static final String APP_PATH_USER_BOOK_PIC_PATH = "http://96.126.115.143/leevrows/fbook_img/";
    public static final String APP_PATH_USER_PIC_PATH = "http://96.126.115.143/leevrows/user_img/";

    // WEBSERVICES
    public static final String APP_URL_WS_GET_MY_BOOKS = "http://96.126.115.143/leevrows/retornaMeusLivros.php";

    public static final String APP_URL_WS_BOOKS_FOR_CHOICE = "http://96.126.115.143/leevrows/retornaListaLivrosParaEscolha.php";
    public static final String APP_URL_WS_COMMIT_CHOICE = "http://96.126.115.143/leevrows/decisao.php";
    public static final String APP_URL_WS_CLEAN_CHOICES = "http://96.126.115.143/leevrows/zerarEscolhas.php";

    public static final String APP_URL_WS_KEEP_USER = "http://96.126.115.143/leevrows/adicionaUsuario.php";
    public static final String APP_URL_WS_GET_USER = "http://96.126.115.143/leevrows/retornaUmUsuario.php";
    public static final String APP_URL_WS_UPD_USER_LAST_LOCATION = "http://96.126.115.143/leevrows/atualizarPosicaoUsuario.php";

    public static final String APP_URL_WS_CHECK_NOTIFICATIONS = "http://96.126.115.143/leevrows/notificacoes.php";

    public static final String APP_URL_WS_GET_MY_TRANSACTIONS = "http://96.126.115.143/leevrows/retornaMinhasTransacoes.php";


    /**
     * ESCONDER TECLADO IMEDIATAMENTE
     * @param activity
     */
    public static void esconderTecladoVirtual(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isISBNValid(String isbn){
        boolean isValid = false;




        return isValid;
    }

}