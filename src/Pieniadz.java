import java.io.*;
import java.util.*;

/*
Pieniadz dziedziczy z Biletomatu
 */
public class Pieniadz extends Biletomat implements I_Pieniadz{
    private double bezgotowkowa;
    private ArrayList<Double> nominaly;

    public Pieniadz() {
        bezgotowkowa = 0;
        nominaly = new ArrayList<Double>();
        Nominal.zainicjalizujNominaly(this);
        //sumowanie monet i dodanie pieniedzy do biletomatu
        super.setIloscPieniedzy(Nominal.getSum(this.nominaly));
    }

    /**
     * Metoda dodajaca kwote zaplacona kartą
     * @param d
     */
    public void dodajBezgotowkowa(double d) {
        bezgotowkowa += d;
        Nominal.zapiszNominalydoPliku(nominaly, bezgotowkowa);
    }

    /**
     * Metoda wykorzystywana do przeprowadzenia transakcji (wrzucenie oraz aktualizacja stanu nominalow w biletomacie)
     * @param cenaBiletu
     * @return lista double z wydanymi nominalami
     */
    public ArrayList<Double> przeprowadzTransakcje(double cenaBiletu) {
        Scanner scan = new Scanner(System.in);
        ArrayList<Double> wrzuconeNominaly = new ArrayList<Double>(0);
        System.out.println("Podaj ilosc nominalow: ");
        int iloscNominalow = scan.nextInt();
        //wrzucanie nominalow;
        double sumaPieniedzy = 0;
        System.out.println("Wrzuc monety: ");
        for(int i=0; i<iloscNominalow; i++) {
            double temp = scan.nextDouble();
            wrzuconeNominaly.add(temp);
            sumaPieniedzy += temp;
        }

        if(sumaPieniedzy < cenaBiletu)
            return null;

        //dodanie wrzuconych monet do wszystkich dostepnych nominalow w automacie oraz obliczenie reszty
        ArrayList<Double> reszta = this.wydawanieReszty(sumaPieniedzy - cenaBiletu, wrzuconeNominaly);

        //usuwanie reszty z maszyny/wydanie reszty
        Nominal.usunNominaly(nominaly, reszta);
        //aktualizowanie nominalow w pliku
        Nominal.zapiszNominalydoPliku(nominaly, bezgotowkowa);

        //zaktualizowanie ilosci pieniedzy a biletomacie
        this.dodajPieniadze(Nominal.getSum(this.nominaly));

        return reszta;
    }

    /**
     * Metoda zwracajaca liste z nominalami do wydania
     * @param reszta - kwota do wydania
     * @param noweNominaly - wrzucone nominaly przez klienta
     * @return Arraylist Double z nominalami potrzebnymi do wydania reszty
     */
    public ArrayList<Double> wydawanieReszty(double reszta, ArrayList<Double> noweNominaly) {
        Nominal.dodajNoweNominalyDoListy(nominaly, noweNominaly);
        Nominal.zapiszNominalydoPliku(nominaly, bezgotowkowa);

        ArrayList<Double> wynik = new ArrayList<Double>(0);
        for (int i = 0; ((i < this.nominaly.size()) && (reszta > 0.00)); i++) {
            if (reszta >= this.nominaly.get(i)) {
                int temp = (int)Math.floor(reszta/this.nominaly.get(i));

                for(int j=0; j<temp; j++) {
                    wynik.add(this.nominaly.get(i));
                }
                reszta = (double) Math.round(100*(reszta-(temp*this.nominaly.get(i))))/100;
            }
        }

        System.out.println();
        return wynik;
    }

    public void wypiszNominaly() {
        Nominal.wypiszNominaly(this.nominaly);
    }

    public ArrayList<Double> getNominaly() {
        return nominaly;
    }

    public void setBezgotowkowa(double bezgotowkowa) {
        this.bezgotowkowa = bezgotowkowa;
    }

    /**
     * Metoda zwracajaca sume wszystkich dostepnych nominalow
     * @return
     */
    public double ilePieniedzyWBiletomacie() {
        return Nominal.getSum(this.nominaly);
    }
}
