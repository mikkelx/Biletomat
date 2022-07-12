import java.util.Scanner;

public class Serwis {
    private int licznik;
    private final String password = "Student";

    Serwis() {
        licznik = 0;
    }

    /**
     * Metoda dodaje jeden do licznika serwisowan
     */
    public void inkrementujSerwisowanie() {
        this.licznik++;
    }

    /**
     * Metoda pozwalajaca zmienic rodzaj biletu
     * @param b1 - obiekt biletomatu
     */
    public void zmienRodzajBiletu(Biletomat b1) {
        Scanner scan = new Scanner(System.in);
        int wyborBiletu = wypiszBilety(b1);
        if(wyborBiletu<1 || wyborBiletu>9) {
            System.out.println("Poza zakresem");
            return;
        }
        else
        {
            b1.changeTicket(wyborBiletu-1, scan.next());
        }
    }

    /**
     * Metoda pozwalajaca zmieniac cene biletu
     * @param b1 - obiekt biletomatu
     */
    public void zmienCeneBiletu(Biletomat b1) {
        Scanner scan = new Scanner(System.in);
        int wyborBiletu = wypiszBilety(b1);
        if(wyborBiletu<1 || wyborBiletu>9) {
            System.out.println("Poza zakresem");
            return;
        }
        else
        {
            b1.changeTicketPrice(wyborBiletu - 1, Double.parseDouble(scan.next()));
        }
    }

    /**
     * Metoda pomocnicza wypisujaca bilety i zwracajaca wybrany bilet
     * @param b1 - obiekt biletomatu
     * @return
     */
    private int wypiszBilety(Biletomat b1) {
        Scanner scan = new Scanner(System.in);
        String[] rodzajeBiletow = b1.getRodzajeBiletow();
        double[] cena = b1.getCeny();
        for(int i=0; i<b1.getRodzajeBiletow().length; i++) {
            System.out.println(i+1 + ". " + rodzajeBiletow[i] + " - " + cena[i] + " zl");
        }
        System.out.println("Ktory numer edytowac: ");
        int wyborBiletu = scan.nextInt();
        return wyborBiletu;
    }

    public int getLicznik() {
        return licznik;
    }

    public void setLicznik(int licznik) {
        this.licznik = licznik;
    }

    public String toString() {
        String toReturn = "";
        System.out.println("Liczba serwisowan: " + this.licznik);
        return toReturn;
    }

    /**
     * Metoda porownujaca podane w argumencie haslo z ustawionym
     * @param toCheck - argument String z haslem do sprawedzenia
     * @return true jesli haslo pasuje
     */
    public boolean checkPassword(String toCheck) {
        if(this.password.equals(toCheck))
            return true;
        else return false;
    }
}
