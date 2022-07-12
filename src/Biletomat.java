import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Biletomat implements I_Biletomat{

    /*
    Kompozycja
     */
    private ArrayList<Bilet> bilety;
    protected ArrayList<Transkacja> transkacje;

    private Serwis serwisowanie;
    private String lokalizacja;

    private int iloscBlankietow;
    private double iloscPieniedzy;
    private boolean awaria;
    private boolean blokada;

    private String[] rodzajeBiletow;
    private double[] ceny;


    public Biletomat() {
        bilety = new ArrayList<Bilet>(0);
        transkacje = new ArrayList<Transkacja>();
        lokalizacja = "";
        serwisowanie = new Serwis();

        awaria = false;
        blokada = false;

        obslugaBiletow obslugujBilety = new obslugaBiletow();
        obslugujBilety.inicjalizujBilety(this);

        obslugaTransakcji obslugujTransakcji = new obslugaTransakcji();
        obslugujTransakcji.wczytajTransakcjeNaListe(this);
    }

    public Biletomat(String lokalizacja) {
        bilety = new ArrayList<Bilet>(0);
        transkacje = new ArrayList<Transkacja>();
        this.lokalizacja = lokalizacja;
        serwisowanie = new Serwis();

        awaria = false;
        blokada = false;

        obslugaBiletow obslugujBilety = new obslugaBiletow();
        obslugujBilety.inicjalizujBilety(this);

        obslugaTransakcji obslugujTransakcji = new obslugaTransakcji();
        obslugujTransakcji.wczytajTransakcjeNaListe(this);
    }

    public void setRodzajeBiletow(String[] rodzajeBiletow) {
        this.rodzajeBiletow = rodzajeBiletow;
    }

    public void setCeny(double[] ceny) {
        this.ceny = ceny;
    }

    public void setIloscBlankietow(int iloscBlankietow) {
        this.iloscBlankietow = iloscBlankietow;
    }

    public void setIloscSerwisowan(int ilosc) {
        this.serwisowanie.setLicznik(ilosc);
    }

    public Serwis getSerwisowanie() {
        return serwisowanie;
    }

    public ArrayList<Transkacja> getTranskacje() {
        return transkacje;
    }

    public int getIloscBlankietow() {
        return this.iloscBlankietow;
    }

    public int iloscSprzedanychBiletow() {
        return this.bilety.size();
    }

    /**
     * Głowna metoda sterujaca sprzedaza biletow
     * @param pieniadze - argument typu Pieniadz odpowiadajacy za platnosci
     */
    public void buyTicket(Pieniadz pieniadze) throws MyException{
        if(this.blokada) {
            throw new MyException("Trwa blokada na czas kontroli!");
        } else if(this.awaria) {
            throw new MyException("Awaria biletomatu. Przepraszamy");
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("Dostępne bilety: ");
        for(int i=0; i<this.rodzajeBiletow.length; i++) {
            System.out.println(i+1 + ". " + rodzajeBiletow[i] + " - " + ceny[i] + " zl");
        }
        boolean multipleTickets = false;
        System.out.println("Chcesz kupic tylko jeden bilet? \n 1)TAK 2)NIE");
        if(scan.nextInt()==2)
            multipleTickets = true;

        if(!multipleTickets) {
            System.out.print("Podaj numer biletu: ");
            int wyborBiletu = scan.nextInt();
            this.iloscBlankietow--;

            try {
                sellOneTicket(pieniadze, wyborBiletu);
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
        else   //kilka biletow
        {
            System.out.print("Podaj ile biletow: ");
            int ileBiletow = scan.nextInt();
            System.out.print("Podaj numery biletow: ");
            int[] kilkaBiletow = new int[ileBiletow];
            for(int i=0; i<ileBiletow; i++) {
                kilkaBiletow[i] = scan.nextInt();
            }
            this.iloscBlankietow -= ileBiletow;
            sellManyTickets(pieniadze, kilkaBiletow);
        }
        //aktualizowanie ilosci pieniedzy w biletomacie
        this.setIloscPieniedzy(pieniadze.ilePieniedzyWBiletomacie());

        //aktualizowanie stanu transakcji
        obslugaTransakcji obslugujTransakcje = new obslugaTransakcji();
        obslugujTransakcje.zapiszTransakcjedoPliku(this);
    }

    /**
     * Metoda pomocnicza odpowiadajaca za sprzedaz jednego biletu
     * @param pieniadze -argument typu Pieniadz odpowiadajacy za platnosci
     * @param wyborBiletu - numer biletu do sprzedazy
     */
    public void sellOneTicket(Pieniadz pieniadze, int wyborBiletu) throws MyException{
        System.out.println("Płatność: 1) Bilon 2) Karta płatnicza");
        Scanner scan = new Scanner(System.in);
        int platnosc = scan.nextInt();
        //platnosc bilonem
        if(platnosc == 1) {
            ArrayList<Double> reszta = pieniadze.przeprowadzTransakcje(ceny[wyborBiletu-1]);
            utworzTransakcje(null, "", 1, ceny[wyborBiletu-1]);
            if(reszta == null) {
                this.transkacje.remove(transkacje.size()-1);
                throw new MyException("Zbyt malo pieniedzy! Sprzedaż zakonczona niepowodzniem!");
            }
            //utworzenie nowego biletu i dodanie go na liste sprzedanych
            Bilet tempBilet = new Bilet(this.rodzajeBiletow[wyborBiletu-1], this.ceny[wyborBiletu-1]);
            bilety.add(tempBilet);
            //pobranie daty
            Date tempDate = new Date();

            System.out.println("Bilet został pomyślnie zakupiony");
            if(reszta.size() > 0) {
                System.out.print("Wydawanie reszty: ");
                for(int i=0; i<reszta.size(); i++) {

                    System.out.print(reszta.get(i) + ", ");
                }
                System.out.println();
            }
            //ustawienie daty transakcji
            this.transkacje.get(this.transkacje.size()-1).setDataSprzedazy(tempDate);
            this.transkacje.get(this.transkacje.size()-1).setLiczbaBiletow(1);
            this.transkacje.get(this.transkacje.size()-1).setSprzedaneBilety(this.rodzajeBiletow[wyborBiletu-1]);
        }
        //platnosc karta
        else if(platnosc == 2) {
            System.out.println("Platnosc karta przebiegla pomylnie!");
            utworzTransakcje(null, "", 1, ceny[wyborBiletu-1]);
            //utworzenie nowego biletu i dodanie go na liste sprzedanych
            Bilet tempBilet = new Bilet(this.rodzajeBiletow[wyborBiletu-1], this.ceny[wyborBiletu-1]);
            bilety.add(tempBilet);
            //pobranie daty
            Date tempDate = new Date();
            System.out.println("Bilet został pomyślnie zakupiony");
            //ustawienie daty transakcji
            this.transkacje.get(this.transkacje.size()-1).setDataSprzedazy(tempDate);
            this.transkacje.get(this.transkacje.size()-1).setLiczbaBiletow(1);
            this.transkacje.get(this.transkacje.size()-1).setSprzedaneBilety(this.rodzajeBiletow[wyborBiletu-1]);

            pieniadze.dodajBezgotowkowa(this.ceny[wyborBiletu-1]);
        }
    }

    /**
     * Metoda pomocnicza odpowiadajaca za sprzedaz kilku biletow
     * @param pieniadze - argument typu Pieniadz odpowiadajacy za platnosci
     * @param numeryBiletow - tablica z numerami biletow do sprzedazy
     */
    public void sellManyTickets(Pieniadz pieniadze, int[] numeryBiletow) throws MyException {
        double sumaCen = 0;

        for(int i=0; i<numeryBiletow.length; i++) {
            sumaCen += ceny[numeryBiletow[i] - 1];
        }

        System.out.println("Płatność: 1) Bilon 2) Karta płatnicza");
        Scanner scan = new Scanner(System.in);
        int platnosc = scan.nextInt();
        if(platnosc == 1) {
            utworzTransakcje(null, "", 1, sumaCen);
            ArrayList<Double> reszta = pieniadze.przeprowadzTransakcje(sumaCen);

            if(reszta == null) {
                //usun transakcje jesli nie udalo sie przeprowadzic
                this.transkacje.remove(transkacje.size()-1);
                throw new MyException("Zbyt malo pieniedzy! Sprzedaż zakonczona niepowodzniem!");
            }
            Date tempDate = new Date();
            String polaczoneBilety = "";
            //dodanie wszystkich zakupionych biletow
            for(int i=0; i<numeryBiletow.length; i++) {
                Bilet tempBilet = new Bilet(rodzajeBiletow[numeryBiletow[i] - 1], ceny[numeryBiletow[i] - 1]);
                bilety.add(tempBilet);
                polaczoneBilety += rodzajeBiletow[numeryBiletow[i] - 1] + "_";
            }
            System.out.println("Bilety zostały pomyślnie zakupione");

            //ustawienie daty transakcji
            this.transkacje.get(this.transkacje.size()-1).setDataSprzedazy(tempDate);
            this.transkacje.get(this.transkacje.size()-1).setLiczbaBiletow(numeryBiletow.length);
            this.transkacje.get(this.transkacje.size()-1).setSprzedaneBilety(polaczoneBilety);

            if(reszta.size() > 0) {
                System.out.print("Wydawanie reszty: ");
                for(int i=0; i<reszta.size(); i++) {

                    System.out.print(reszta.get(i) + ", ");
                }
                System.out.println();
            }
        } else if(platnosc == 2) {
            System.out.println("Platnosc karta przebiegla pomylnie!");
            utworzTransakcje(null, "", 1, sumaCen);

            Date tempDate = new Date();
            String polaczoneBilety = "";
            //dodanie wszystkich zakupionych biletow
            for(int i=0; i<numeryBiletow.length; i++) {
                Bilet tempBilet = new Bilet(rodzajeBiletow[numeryBiletow[i] - 1], ceny[numeryBiletow[i] - 1]);
                bilety.add(tempBilet);
                polaczoneBilety += rodzajeBiletow[numeryBiletow[i] - 1] + "_";
            }
            System.out.println("Bilety zostały pomyślnie zakupione");

            //ustawienie daty transakcji
            this.transkacje.get(this.transkacje.size()-1).setDataSprzedazy(tempDate);
            this.transkacje.get(this.transkacje.size()-1).setLiczbaBiletow(numeryBiletow.length);
            this.transkacje.get(this.transkacje.size()-1).setSprzedaneBilety(polaczoneBilety);
            pieniadze.dodajBezgotowkowa(sumaCen);
        }

    }

    /**
     * Główna metoda odpowiadająca za serwisowanie biletomatu
     * @param pieniadze - argument typu Pieniadz odpowiadajacy za platnosci
     */
    public void startServis(Pieniadz pieniadze) throws MyException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj hasło administracyjne: ");
        if(!this.serwisowanie.checkPassword(scan.next())) {
            throw new MyException("Błędne hasło");
        }
        else
        {
            System.out.println("Dostepne opcje: \n1) Zmien rodzaj biletu \n2) Zmien cene biletu \n3) Wyswietl ilosc blankietow");
            System.out.println("4) Edytuj ilosc blankietow \n5) Blokada kupna \n6) Przelacz stan awarii");
            System.out.println("7) WYswietl liczbe serwisowan \n8) Wypisz liste transakcji \n9) Wypisz nominaly w biletomacie");
            System.out.println("10) Wydrukuj transakcje sprzed okreslonej daty\n11) Wyswietl ilosc pieniedzy");
            System.out.println("12) Ustaw lokalizacje \n15) Wyjdz z serwisowania\n");
            boolean condition = true;
            while(condition) {
                System.out.print("Podaj swoj wybor: ");
                int wybor = scan.nextInt();
                switch(wybor) {
                    case 1:
                        serwisowanie.zmienRodzajBiletu(this);
                        break;

                    case 2:
                        serwisowanie.zmienCeneBiletu(this);
                        break;

                    case 3:
                        System.out.println(this.iloscBlankietow);
                        break;

                    case 4:
                        System.out.print("Ile blankietow dodac: ");
                        int blankiety = scan.nextInt();
                        if(blankiety>0 && blankiety < 1000)
                            this.iloscBlankietow += blankiety;
                        break;

                    case 5:
                        if(this.blokada)
                            this.blokada = false;
                        else this.blokada = true;
                        break;

                    case 6:
                        if(this.awaria)
                            this.awaria = false;
                        else this.awaria = true;
                        break;

                    case 7:
                        System.out.println(serwisowanie.getLicznik());
                        break;

                    case 8:
                        this.toString();
                        break;

                    case 9:
                        pieniadze.wypiszNominaly();
                        break;

                    case 10:
                        System.out.print("Podaj date w formacie YYYY-MM-DD: ");
                        String dataWString = scan.next();
                        LocalDate nowaData = LocalDate.parse(dataWString);

                        obslugaTransakcji obslugujTransakcji = new obslugaTransakcji();
                        obslugujTransakcji.wydrukujTransakcje(this, nowaData);
                        break;

                    case 11:
                        System.out.println(this.iloscPieniedzy);
                        break;

                    case 12:
                        System.out.print("Podaj lokalizacje: ");
                        this.setLokalizacja(scan.nextLine());
                        break;
                    case 15:
                        condition = false;
                        continue;

                    default:
                        break;
                }
                serwisowanie.inkrementujSerwisowanie();
            }
            //aktualizowanie stanu biletow w pliku

            obslugaBiletow obslugujBilety = new obslugaBiletow();
            obslugujBilety.zapiszBilety(this);
        }
    }

    public String getLokalizacja() {
        return lokalizacja;
    }

    public void setLokalizacja(String lokalizacja) {
        this.lokalizacja = lokalizacja;
    }

    public String[] getRodzajeBiletow() {
        return rodzajeBiletow;
    }

    public double[] getCeny() {
        return ceny;
    }

    public void setIloscPieniedzy(double iloscPieniedzy) {
        this.iloscPieniedzy = iloscPieniedzy;
    }

    public void zmienRodzajBiletu(String nowyRodzaj, int ktoryIndeks) {
        rodzajeBiletow[ktoryIndeks] = nowyRodzaj;
    }

    public void zmienCeneBiletu(double nowaCena, int ktoryIndeks) {
        ceny[ktoryIndeks] = nowaCena;
    }

    /**
     * Metoda dodajaca okreslona w argumencie ilosc pieniedzy do biletomatu
     * @param ilosc - ilosc pieniedzy(double)
     */
    protected void dodajPieniadze(double ilosc) {
        this.iloscPieniedzy += ilosc;
    }

    /**
     * Metoda pomocnicza sluzaca do utworzenia nowej transakcji(dodaje ja od razu na liste w biletomacie)
     * @param data
     * @param sprzedaneBilety
     * @param liczbaBiletow
     * @param dochod
     */
    protected void utworzTransakcje(Date data, String sprzedaneBilety, int liczbaBiletow, double dochod) {
        Transkacja tempTransakcja = new Transkacja(data, sprzedaneBilety, liczbaBiletow, dochod);
        this.transkacje.add(tempTransakcja);
    }

    public String toString() {
        String toReturn = "";
        for(int i=0; i<this.transkacje.size(); i++) {
            toReturn += ("" + transkacje.get(i).getDate() + " : " + transkacje.get(i).getSprzedaneBilety() +
                    " : " + transkacje.get(i).getLiczbaBiletow() + " : " + transkacje.get(i).getDochod() + "\n");

            System.out.println("(" + transkacje.get(i).getDate() + " : " + transkacje.get(i).getSprzedaneBilety() +
                    " : " + transkacje.get(i).getLiczbaBiletow() + " : " + transkacje.get(i).getDochod() + ")");

        }
        return toReturn;
    }

    public void changeTicket(int i, String zmiana) throws IndexOutOfBoundsException{
        if(i < 0 || i > 7)
            throw new IndexOutOfBoundsException("Zly indeks biletu");
        this.rodzajeBiletow[i] = zmiana;
    }

    public void changeTicketPrice(int i, double cena) throws IndexOutOfBoundsException{
        if(i < 0 || i > 7)
            throw new IndexOutOfBoundsException("Zly indeks biletu");
        this.ceny[i] = cena;
    }

    public void odswiezIloscPieniedzy(Pieniadz pieniadze) {
        this.iloscPieniedzy = pieniadze.ilePieniedzyWBiletomacie();
    }

}
