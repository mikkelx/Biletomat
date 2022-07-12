import java.io.Console;
import java.util.Scanner;


public class Test {
    public static void main(String[] args) {
        Biletomat biletomat = new Biletomat("Wyki");
        Pieniadz pieniadze = new Pieniadz();
        biletomat.odswiezIloscPieniedzy(pieniadze);

        Scanner scan = new Scanner(System.in);

        while(true) {
            System.out.println("Biletomat MPK");
            System.out.print("1) Kupno biletu \n2) Serwis \n");
            int serviceChoice = scan.nextInt();
            switch(serviceChoice) {
                case 1:
                    try {
                        biletomat.buyTicket(pieniadze);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        biletomat.startServis(pieniadze);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    System.exit(0);
                    break;

                default:
                    continue;

            }
        }
    }

}
