package leevro.pucpr.br.leevro19.entity;

import java.math.BigInteger;

/**
 * Created by Jonas on 25/04/2016.
 */
public class Trade {

    private Integer id;

    private Integer firstUserId;
    private Integer secondUserId;

    private AppUser firstUser;
    private AppUser secondUser;

    // Livro do secondUser que o firstUser quer
    private Integer firstUserRequestedBookId;
    private Book firstUserRequestedBook;

    // Livro do firstUser que o secondUser quer
    private Integer secondUserRequestedBookId;
    private Book secondUserRequestedBook;

    // quando o firstUser aceita COMMITAR o empréstimo
    private Boolean firstUserAccept;

    // quando o secondUser aceita COMMITAR o empréstimo
    private Boolean secondUserAccept;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(Integer firstUserId) {
        this.firstUserId = firstUserId;
    }

    public Integer getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(Integer secondUserId) {
        this.secondUserId = secondUserId;
    }

    public AppUser getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(AppUser firstUser) {
        this.firstUser = firstUser;
    }

    public AppUser getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(AppUser secondUser) {
        this.secondUser = secondUser;
    }

    public Book getFirstUserRequestedBook() {
        return firstUserRequestedBook;
    }

    public void setFirstUserRequestedBook(Book firstUserRequestedBook) {
        this.firstUserRequestedBook = firstUserRequestedBook;
    }

    public Book getSecondUserRequestedBook() {
        return secondUserRequestedBook;
    }

    public void setSecondUserRequestedBook(Book secondUserRequestedBook) {
        this.secondUserRequestedBook = secondUserRequestedBook;
    }

    public Boolean getFirstUserAccept() {
        return firstUserAccept;
    }

    public void setFirstUserAccept(Boolean firstUserAccept) {
        this.firstUserAccept = firstUserAccept;
    }

    public Boolean getSecondUserAccept() {
        return secondUserAccept;
    }

    public void setSecondUserAccept(Boolean secondUserAccept) {
        this.secondUserAccept = secondUserAccept;
    }

    public Integer getFirstUserRequestedBookId() {
        return firstUserRequestedBookId;
    }

    public void setFirstUserRequestedBookId(Integer firstUserRequestedBookId) {
        this.firstUserRequestedBookId = firstUserRequestedBookId;
    }

    public Integer getSecondUserRequestedBookId() {
        return secondUserRequestedBookId;
    }

    public void setSecondUserRequestedBookId(Integer secondUserRequestedBookId) {
        this.secondUserRequestedBookId = secondUserRequestedBookId;
    }
}
