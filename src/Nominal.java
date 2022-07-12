import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Nominal {
    final static String fileName = "nominal.txt";

    public static void zapiszNominalydoPliku(ArrayList<Double> nominaly, double bezgotowkowa) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write(bezgotowkowa + "\n");
            for(int i=0; i<nominaly.size(); i++) {
                String temp = "" + nominaly.get(i) + "\n";
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

    public static void dodajNoweNominalyDoListy(ArrayList<Double> nominaly, ArrayList<Double> toAdd) {
        for(int i=0; i<toAdd.size(); i++) {
            nominaly.add(toAdd.get(i));
        }
        Collections.sort(nominaly);
        Collections.reverse(nominaly);

    }

    public static void usunNominaly(ArrayList<Double> nominaly, ArrayList<Double> toDelete) {
        for(int j=0; j<toDelete.size(); j++) {
            double temp = toDelete.get(j);
            for(int i=0; i<nominaly.size(); i++) {
                if(temp == nominaly.get(i)) {
                    nominaly.remove(i);
                    break;
                }
            }
        }
    }

    public static void wypiszNominaly(ArrayList<Double> nominaly) {
        for(int i=0; i<nominaly.size(); i++) {
            System.out.print(nominaly.get(i) + " ");
        }
        System.out.println();
    }

    public static double getSum(ArrayList<Double> nominaly) {
        double sum = 0;
        for(int i=0; i<nominaly.size(); i++) {
            sum += nominaly.get(i);
        }
        return sum;
    }


    public static void zainicjalizujNominaly(Pieniadz pieniadze) {
        File file = new File(fileName);
        Scanner scan = null;
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //wczytanie najpierw ilosci pieniedzy z platnosci bezgotowkowych
        pieniadze.setBezgotowkowa(Double.parseDouble(scan.nextLine()));
        while(scan.hasNext()) {
            String line = scan.nextLine();
            double temp = Double.parseDouble(line);
            pieniadze.getNominaly().add(temp);
        }
    }
}
