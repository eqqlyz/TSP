package SYMTSP;

public class Test {
  public Test() {
  }

  public static void main(String[] args) {
    city ct=new city(29);
    ct.printCities();
    System.out.println("�����㷨�Ľ�����:");
    /*�˻��㷨*/
    TuiHuo th=new TuiHuo(ct);
    th.TuiHuoexecute();
    th.printCities();
    /*�Ŵ��㷨*/
    System.out.println();
    YiChuan yc=new YiChuan(ct);
    yc.init_Group();
    yc.GA_alg();
    yc.printTheSUPER();
    /*��Ⱥ�㷨*/
    System.out.println();
    YiQun yq=new YiQun(ct);
    yq.schedule();
    yq.printTheSUPER();
  }
}
