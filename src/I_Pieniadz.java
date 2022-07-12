import java.util.ArrayList;

public interface I_Pieniadz {

    ArrayList<Double> przeprowadzTransakcje(double cenaBiletu);

    ArrayList<Double> wydawanieReszty(double reszta, ArrayList<Double> noweNominaly);

    void wypiszNominaly();

    double ilePieniedzyWBiletomacie();
}
