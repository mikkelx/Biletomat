public class Bilet {
    private String rodzajBiletu;
    private double cena;

    public Bilet(String rodzaj, double cena) {
        this.rodzajBiletu = rodzaj;
        this.cena = cena;
    }

    public String getRodzajBiletu() {
        return rodzajBiletu;
    }

    public void setRodzajBiletu(String rodzajBiletu) {
        this.rodzajBiletu = rodzajBiletu;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public String toString() {
        return "" + rodzajBiletu + ", " + cena + " zł";
    }
}
