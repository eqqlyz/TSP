package SYMTSP;

public class city {
  public int N;
  public double CITY[][];
  public city(int n)
  {
    N=n;
    initCities();
  }

  public void initCities() {
    int i;
    CITY = new double[N][];
    for (i = 0; i < N; i++) {
      CITY[i] = new double[2];
    }
    for (i = 0; i < N; i++) {
      CITY[i][0] = Math.random() * 100;
      CITY[i][1] = Math.random() * 100;
    }
  }

  public void printCities()
  {
    for(int i=0;i<N;i++)
      System.out.println("("+CITY[i][0]+","+CITY[i][1]+")");
  }

}
