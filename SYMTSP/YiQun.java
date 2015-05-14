package SYMTSP;

/*蚂蚁根据各条路径上的信息量及路径的启发信息来计算状态转移概率
 *蚂蚁数最好等于城市数
 */
public class YiQun {

  public int N; /*记录城市个数*/
  public double CITY[][]; /*记录城市的坐标：N*2*/
  public Information ACO[][];
  public MaYi ANTS[]; /*记录第i个城市的蚂蚁数:M*1*/
  public int SUPER[]; /*记录最佳路径*/
  public double min_length; /*记录最短路径的长度*/

  /*几个系统常量的定义*/
  public int M; /*记录蚂蚁个数*/
  public static final double INIT_INFO = 0.7;
  public static final double INFO_HERI_FACTOR_ALF = 0.52;
  public static final double EXCP_HERI_FACTOR_BETA = 0.53;
  public static final double INFO_HUIFA_FACTOR_ROU = 0.4;
  public static final int MAX_INTERATION_TIME = 200;

  public YiQun(city ct) {

    this.N = ct.N;
    this.M = this.N;
    CITY = new double[N][];
    for (int i = 0; i < N; i++) {
      CITY[i] = new double[2];
    }

    for (int i = 0; i < N; i++) {
      CITY[i][0] = ct.CITY[i][0];
      CITY[i][1] = ct.CITY[i][1];
    }
    init();
  }

  /*为各个结构量申请空间，并且计算一些信息*/

  public void init() {
    /*为ACO申请空间*/
    ACO = new Information[N][];
    for (int i = 0; i < N; i++) {
      ACO[i] = new Information[N];
    }
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        ACO[i][j] = new Information();
      }
    }
    /*初始化ACO*/
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        ACO[i][j].distance = Math.sqrt(Math.pow(CITY[i][0] - CITY[j][0], 2) +
                                       Math.pow(CITY[i][1] - CITY[j][1], 2));
        if (i != j) {
          ACO[i][j].pheromone = INIT_INFO;
        }
        else {
          ACO[i][j].pheromone = 0;
        }
      }
    }

    /*为ANT申请空间并初始化*/
    ANTS = new MaYi[M];
    for (int i = 0; i < M; i++) {
      ANTS[i] = new MaYi(N);
    }
    /*初始时刻，将编号为i的蚂蚁放在编号为i的城市上*/
    for (int i = 0; i < M; i++) {
        ANTS[i].Tour[0] = i;
        ANTS[i].Tuba[i] = 1;
    }
  }

  /*转移概率计算方法*/
  public void choose_city(int k) {
    /*输入:
      k:第k个蚂蚁
     */
    int from, to;
    double d = 0;
    double p=0; /*转移概率*/
    from = ANTS[k].Tour[ANTS[k].Current];
    /*如果还有未访问的城市*/
    for (to = 0; to < N; to++) {
      if (ANTS[k].Tuba[to] == 0) {
        /*用概率公式进行计算*/
        d += Math.pow(ACO[from][to].pheromone, this.INFO_HERI_FACTOR_ALF) *
            Math.pow( (1.0 / (ACO[from][to].distance)),
                     this.EXCP_HERI_FACTOR_BETA);
        //System.out.println("from to distance "+ACO[from][to].distance);
        //System.out.println("from to pheromone "+ACO[from][to].pheromone);
      }
    }
    /*根据概率选择城市*/
    to = (int)((Math.random()*(N))%N);
    while (true) {
      /*还有城市未被访问*/
      /*保证永远也重复不了路径*/
      if (ANTS[k].Tuba[to] == 0) {
        p = (Math.pow(ACO[from][to].pheromone, this.INFO_HERI_FACTOR_ALF) *
             Math.pow( (1.0 / (ACO[from][to].distance)),
                      this.EXCP_HERI_FACTOR_BETA)) / d;
        /*随机的选择某一个未走过的城市*/
        /*用随机概率来表是相应的pij的大小*/
        if (Math.random() < p) {
          break;
        }
      }
      to = (to + 1) % (N);
    }
    /*至此，已经选择了to所代表的城市，更新城市信息*/
    //System.out.println("Current is "+ANTS[k].Current);
    ANTS[k].Current = (ANTS[k].Current + 1) ;
    ANTS[k].Tour[ANTS[k].Current] = to;
    ANTS[k].Tuba[to] = 1;
    if (ANTS[k].Current == N ) {
      ANTS[k].Current=0;
      ANTS[k].Total_length(CITY);
    }
    return;
  }

  public void update_info() {
    int from, to, i, city;
    for(int k=0;k<M;k++)
     ANTS[k].Total_length(CITY);
    for (i = 0; i < M; i++) /*蚂蚁数目*/
        {
      for (city = 0; city < N; city++) {
        from = ANTS[i].Tour[city];
        to = ANTS[i].Tour[ (city + 1) % N];
        /*使用公式Tij=Tij+(detTij*rou)*/
        ACO[from][to].pheromone +=
            ( (this.INIT_INFO / ANTS[i].tour_length) *
             this.INFO_HUIFA_FACTOR_ROU);
        ACO[to][from].pheromone = ACO[from][to].pheromone;
      }
    }
  }

  public void huifa_info() {
    int from, to;
    for (from = 0; from < N; from++) {
      for (to = 0; to < N; to++) {
        ACO[from][to].pheromone *= (1.0 - this.INFO_HUIFA_FACTOR_ROU);
        if (ACO[from][to].pheromone < 0) {
          ACO[from][to].pheromone = this.INIT_INFO;
        }
      }
    }
  }

  /*整体调度方法*/
  public void schedule() {
    int interation = 0;
    int i = 0;
    while (interation < this.MAX_INTERATION_TIME) {
      /*1.让每个蚂蚁都完成一次全程旅行*/
      for (i = 0; i < M; i++) {
        while (!ANTS[i].isOver()) {
          choose_city(i);
        }
      }

        /*2.计算各个路径的信息量*/
        update_info();
      /*3.挥发各个节点的信息量*/
      huifa_info();
      /*4.将Tuba表回复*/
      HuiFu_Tuba();
      interation++;
    }
    /*4.从节点0开始按照信息素浓度查找一条浓度最大的路*/
    SUPER = new int[N];
    int JinJi[]=new int[N];
    int j = 0;
    int k = 0; /*给SUPER[go]赋值*/
    int t = 0; /*做SUPER的下标*/
    int max = 0;
    double max_info = -5;
    SUPER[t] = max;
    /*初始化禁忌表*/
    for(int x=0;x<N;x++)
      JinJi[x]=0;
    while (t < N) {
      k = 0;
      while (k < N) {
        if (max_info<ACO[j][k].pheromone && j != k&&JinJi[k]!=1) {
          max = k;
          max_info = ACO[j][k].pheromone;
        }
        k++;
      }
      SUPER[t] = max;
      JinJi[max]=1;
      max_info=-5;
      j = max;
      t++;
    }
    //print_info();

    MaYi my=new MaYi(N);
    my.set_Tour(SUPER);
    my.Total_length(CITY);
    min_length=my.tour_length;
  }

  public void HuiFu_Tuba() {
    /*将编号为i的蚂蚁放在编号为i的城市上*/
    for (int i = 0; i < M; i++) {
      ANTS[i].Current=0;
      ANTS[i].Tour[ANTS[i].Current] = i;
      for (int j = 0; j < N; j++) {
        if (j == i) {
          ANTS[i].Tuba[j] = 1;

        }
        else {
          ANTS[i].Tuba[j] = 0;
        }
        if(j==0)
          ANTS[i].Tour[j]=i;
        else
          ANTS[i].Tour[j]=-1;
      }
    }
  }

  public void printTheSUPER() {
    for (int j = 0; j < N; j++) {
      System.out.println("(" + CITY[SUPER[j]][0] + "," + CITY[SUPER[j]][1] +
                         ")");
    }
    System.out.println("蚁群算法近似最优距离:" + this.min_length);

  }

  public void print_info() {
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        System.out.print(ACO[i][j].pheromone + "  ");
      }
      System.out.println();
    }
    System.out.println();

    /*  for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
          System.out.print(ACO[i][j].distance + "  ");
        }
        System.out.println();
      }*/

  }
}
