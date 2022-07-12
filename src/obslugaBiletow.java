import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class obslugaBiletow {
    final static String fileName = "ustawieniaSerwisowe.txt";


    /**
     * Metoda wczytujaca dane o stanie licznika serwisowego, ilosci blankietow oraz danych dostepnych biletow
     */
    public void inicjalizujBilety(Biletomat biletomat) {
        String[] rodzajeBiletow = new String[8]; //{"20-MINUTOWY", "20-MINUTOWY-ULGOWY", "GODZINNY", "GODZINNY-ULGOWY", "24-GODZINY",
        // "24-GODZINY-ULGOWY", "TYGODNIOWY", "TYGODNIOWY-ULGOWY"};

        double[] ceny = new double[8]; //{4, 2, 6, 3, 23, 11.5, 68, 34};

        File file = new File(fileName);
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //wczytanie liczby serwisowan
        biletomat.setIloscSerwisowan(scan.nextInt());

        //wczytanie blaniektow
        biletomat.setIloscBlankietow(scan.nextInt());

        int i = 0;
        while(scan.hasNext()) {
            rodzajeBiletow[i] = scan.next();
            String cena = scan.next();
            ceny[i] = Double.parseDouble(cena);
            i++;
        }

        biletomat.setRodzajeBiletow(rodzajeBiletow);
        biletomat.setCeny(ceny);
    }

    /**
     * Metoda zapisujaca aktualy stan licznika serwisowego, ilosci blankietow oraz rodzajow i cen biletow
     */
    public void zapiszBilety(Biletomat biletomat) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write(biletomat.getSerwisowanie().getLicznik() + "\n");
            fileWriter.write(biletomat.getIloscBlankietow() + "\n");
            for(int i=0; i<biletomat.getRodzajeBiletow().length; i++) {
                String temp = biletomat.getRodzajeBiletow()[i] + " " + biletomat.getCeny()[i] + "\n";
                fileWriter.write(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
