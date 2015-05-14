package SYMTSP;

public class TuiHuo {
  public int N;
  public double CITY[][];
  public double CITY2[][];
  public double CITY3[][];
  public static final int DIM = 2;
  public static final int RANGE = 100;
  public static final int INT = 50;

  //系统参数
  public static final double ALPHA = 0.9;
  public static final int INTERATION = 100;
  public static final double INIT_TMP = 300;
  public static final double MIM_TMP = 0.0001;

  public TuiHuo(city ct) {
    N = ct.N;
    /*将ct的CITY、CITY2拷贝到TuiHuo中进行操作*/
    CITY = new double[N][];
    CITY2 = new double[N][];
    for (int i = 0; i < N; i++) {
      CITY[i] = new double[2];
      CITY2[i] = new double[2];
    }

    for (int i = 0; i < N; i++) {
      CITY[i][0] = ct.CITY[i][0];
      CITY[i][1] = ct.CITY[i][1];

      CITY2[i][0] = ct.CITY[i][0];
      CITY2[i][1] = ct.CITY[i][1];
    }
    CITY3 = new double[N][];
    for (int i = 0; i < N; i++) {
      CITY3[i] = new double[2];
    }

  }

  public double distance(double CITY[][]) {
    double temp = 0;
    int i;
    for (i = 0; i < N - 1; i++) {
      temp +=
          Math.sqrt(Math.pow(CITY[i][0] - CITY[i + 1][0], 2) +
                    Math.pow(CITY[i][1] - CITY[i + 1][1], 2));
    }
    temp +=
        Math.sqrt(Math.pow(CITY[0][0] - CITY[N - 1][0], 2) +
                  Math.pow(CITY[0][1] - CITY[N - 1][1], 2));
    return temp;
  }

  public void disturb() {
    int p1, p2;
    int k = 0;
    double x, y;
    for (int i = 0; i < N; i++) {
      CITY3[i][0] = CITY[i][0];
      CITY3[i][1] = CITY[i][1];
    }
    do {
      do {
        p1 = (int) (Math.random() * (N)) % (N - 1) + 1;
        p2 = (int) (Math.random() * (N)) % (N - 1) + 1;
      }
      while (p1 == p2);
      // System.out.println("p1:" + p1 + "  p2:" + p2);
      x = CITY[p1][0];
      y = CITY[p1][1];
      CITY[p1][0] = CITY[p2][0];
      CITY[p1][1] = CITY[p2][1];
      CITY[p2][0] = x;
      CITY[p2][1] = y;
      k++;
    }
    while ( (distance(CITY) > distance(CITY3)) && k < INT);
    if (k == INT && (distance(CITY) > distance(CITY3))) {
      for (int i = 0; i < N; i++) {
        CITY[i][0] = CITY3[i][0];
        CITY[i][1] = CITY3[i][1];
      }

    }
  }

  public void exchange() {
    int i;
    double temp;
    for (i = 0; i < N; i++) {
      temp = CITY[i][0];
      CITY[i][0] = CITY2[i][0];
      CITY2[i][0] = temp;
      temp = CITY[i][1];
      CITY[i][1] = CITY2[i][1];
      CITY2[i][1] = temp;
    }
  }

  public void TuiHuoexecute() {
    double temperature = INIT_TMP;
    int i;
    double city_dist;
    double city2_dist;
    double delt;
    while (temperature > MIM_TMP) {
      for (i = 0; i < INTERATION; i++) {
        disturb();
        city_dist = distance(CITY);
        city2_dist = distance(CITY2);
        delt = city_dist - city2_dist;
        if (delt >= 0.0) {
          if (Math.exp( -delt / temperature) > Math.random()) {
            exchange();
          }
        }
      }
      temperature *= ALPHA;
    }
  }

  public void printCities() {
    for (int i = 0; i < N; i++) {
      System.out.println("(" + CITY[i][0] + "," + CITY[i][1] + ")");
    }
    System.out.println("退火算法近似最优距离:" + distance(CITY));
  }
}
