import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class obslugaTransakcji {
    final static String fileName = "transakcje.txt";

    public void wczytajTransakcjeNaListe(Biletomat biletomat){
        File file = new File(fileName);
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        while(scan.hasNext()) {
            String dni = scan.next();
            String godzina = scan.next();
            scan.next();
            String bilet = scan.next();
            scan.next();
            int iloscBiletow = scan.nextInt();
            scan.next();
            String cena1 = scan.next();
            double cena = Double.parseDouble(cena1);

            //konwersja Stringa do formatu Data
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date data = null;
            try {
                data = formatter.parse(dni + " " + godzina);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //dodawanie transakcji zapisanych w pliku na liste
            Transkacja tempTransakcja = new Transkacja(data, bilet, iloscBiletow, cena);
            biletomat.getTranskacje().add(tempTransakcja);
        }
    }

    /**
     * Metoda służąca do wydrukowania wszystkich transakcji przed podana w argumencie data
     * @param date - parametr typu LocalDate w formacie "YYYY-MM-DD"
     */
    public void wydrukujTransakcje(Biletomat biletomat, LocalDate date) {
        int i = 0;
        LocalDate tempDate = convertToLocalDateViaInstant(biletomat.getTranskacje().get(i).getDataSprzedazy());

        System.out.println(tempDate);
        System.out.println(date);

        while(tempDate.isBefore(date)) {
            System.out.println(biletomat.getTranskacje().get(i).getDate() + " : " + biletomat.getTranskacje().get(i).getSprzedaneBilety() +
                    " : " + biletomat.getTranskacje().get(i).getLiczbaBiletow() + " : " + biletomat.getTranskacje().get(i).getDochod());

            i++;
            tempDate = convertToLocalDateViaInstant(biletomat.getTranskacje().get(i).getDataSprzedazy());

            if((i+1) >= biletomat.getTranskacje().size())
                break;
        }
    }

    /**
     * Konwerscja typu daty Date do LocalDate
     * @param dateToConvert
     * @return
     */
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Metoda zapisujaca transakcje z listy do pliku tekstowego
     */
    public void zapiszTransakcjedoPliku(Biletomat biletomat) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);
            for(int i=0; i<biletomat.getTranskacje().size(); i++) {
                String temp = "" + biletomat.getTranskacje().get(i).toString();
                fileWriter.write(temp);
            }
            //fileWriter.write(this.toString());
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
