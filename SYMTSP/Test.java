package SYMTSP;

public class Test {
  public Test() {
  }

  public static void main(String[] args) {
    city ct=new city(29);
    ct.printCities();
    System.out.println("各种算法的解如下:");
    /*退火算法*/
    TuiHuo th=new TuiHuo(ct);
    th.TuiHuoexecute();
    th.printCities();
    /*遗传算法*/
    System.out.println();
    YiChuan yc=new YiChuan(ct);
    yc.init_Group();
    yc.GA_alg();
    yc.printTheSUPER();
    /*蚁群算法*/
    System.out.println();
    YiQun yq=new YiQun(ct);
    yq.schedule();
    yq.printTheSUPER();
  }
}
