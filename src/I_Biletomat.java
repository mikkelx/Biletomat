public interface I_Biletomat {

    void buyTicket(Pieniadz pieniadze) throws MyException;

    void sellOneTicket(Pieniadz pieniadze, int wyborBiletu) throws MyException;

    void sellManyTickets(Pieniadz pieniadze, int[] numeryBiletow) throws MyException;

    void startServis(Pieniadz pieniadze) throws MyException;

    void changeTicket(int i, String zmiana) throws IndexOutOfBoundsException;
    void changeTicketPrice(int i, double cena) throws IndexOutOfBoundsException;

    void odswiezIloscPieniedzy(Pieniadz pieniadze);
}
