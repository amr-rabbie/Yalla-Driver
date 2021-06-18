package Models;

/**
 * Created by ASI on 4/5/2017.
 */

public class moneyModel {
    String Mon,Tus,Wen,Thr,Fri,Sat,Sun;

    public moneyModel(String Mon, String Wen, String Tus, String Thr,String Fri, String Sat, String Sun) {
        this.Mon = Mon;
        this.Wen = Wen;
        this.Tus = Tus;
        this.Fri = Fri;
        this.Thr = Thr;
        this.Sat = Sat;
        this.Sun = Sun;
    }

    public String getM() {
        return Mon;
    }

    public String getTu() {
        return Tus;
    }

    public String getW() {
        return Wen;
    }

    public String getTh() {
        return Thr;
    }

    public String getSa() {
        return Sat;
    }

    public String getF() {
        return Fri;
    }

    public String getSu() {
        return Sun;
    }
}
