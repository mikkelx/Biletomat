import java.text.SimpleDateFormat;
import java.util.Date;

public class Transkacja {
    private Date dataSprzedazy;
    private String sprzedaneBilety;
    private int liczbaBiletow;
    private double dochod;

    public Transkacja(Date data, String sprzedaneBilety, int liczbaBiletow, double dochod) {
        dataSprzedazy = new Date();
        dataSprzedazy = data;

        this.sprzedaneBilety = sprzedaneBilety;
        this.liczbaBiletow = liczbaBiletow;
        this.dochod = dochod;
    }

    public Transkacja() {
        dataSprzedazy = new Date();
        dataSprzedazy = null;
        this.sprzedaneBilety = "";
        this.liczbaBiletow = 0;
        this.dochod = 0;
    }

    public void addDate(Date data) {
        this.dataSprzedazy = data;
    }

    public String getSprzedaneBilety() {
        return sprzedaneBilety;
    }

    public void setSprzedaneBilety(String sprzedaneBilety) {
        this.sprzedaneBilety = sprzedaneBilety;
    }

    public int getLiczbaBiletow() {
        return liczbaBiletow;
    }

    public void setLiczbaBiletow(int liczbaBiletow) {
        this.liczbaBiletow = liczbaBiletow;
    }

    public double getDochod() {
        return dochod;
    }

    public void setDochod(double dochod) {
        this.dochod = dochod;
    }

    public void setDataSprzedazy(Date data) {
        dataSprzedazy = data;
    }

    public Date getDataSprzedazy() {
        return dataSprzedazy;
    }

    /**
     * Metoda zwracajaca date w Stringu w formacie "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public String getDate() {
        Date d1 = dataSprzedazy;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = sdf1.format(d1);
        return data;
    }

    public String toString() {
        String toReturn = "";
        toReturn += ("" + this.getDate() + " : " + this.getSprzedaneBilety() +
                " : " + this.getLiczbaBiletow() + " : " + this.getDochod() + "\n");

        return toReturn;
    }

}
