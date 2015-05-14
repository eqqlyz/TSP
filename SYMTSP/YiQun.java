package SYMTSP;

/*���ϸ��ݸ���·���ϵ���Ϣ����·����������Ϣ������״̬ת�Ƹ���
 *��������õ��ڳ�����
 */
public class YiQun {

  public int N; /*��¼���и���*/
  public double CITY[][]; /*��¼���е����꣺N*2*/
  public Information ACO[][];
  public MaYi ANTS[]; /*��¼��i�����е�������:M*1*/
  public int SUPER[]; /*��¼���·��*/
  public double min_length; /*��¼���·���ĳ���*/

  /*����ϵͳ�����Ķ���*/
  public int M; /*��¼���ϸ���*/
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

  /*Ϊ�����ṹ������ռ䣬���Ҽ���һЩ��Ϣ*/

  public void init() {
    /*ΪACO����ռ�*/
    ACO = new Information[N][];
    for (int i = 0; i < N; i++) {
      ACO[i] = new Information[N];
    }
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        ACO[i][j] = new Information();
      }
    }
    /*��ʼ��ACO*/
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

    /*ΪANT����ռ䲢��ʼ��*/
    ANTS = new MaYi[M];
    for (int i = 0; i < M; i++) {
      ANTS[i] = new MaYi(N);
    }
    /*��ʼʱ�̣������Ϊi�����Ϸ��ڱ��Ϊi�ĳ�����*/
    for (int i = 0; i < M; i++) {
        ANTS[i].Tour[0] = i;
        ANTS[i].Tuba[i] = 1;
    }
  }

  /*ת�Ƹ��ʼ��㷽��*/
  public void choose_city(int k) {
    /*����:
      k:��k������
     */
    int from, to;
    double d = 0;
    double p=0; /*ת�Ƹ���*/
    from = ANTS[k].Tour[ANTS[k].Current];
    /*�������δ���ʵĳ���*/
    for (to = 0; to < N; to++) {
      if (ANTS[k].Tuba[to] == 0) {
        /*�ø��ʹ�ʽ���м���*/
        d += Math.pow(ACO[from][to].pheromone, this.INFO_HERI_FACTOR_ALF) *
            Math.pow( (1.0 / (ACO[from][to].distance)),
                     this.EXCP_HERI_FACTOR_BETA);
        //System.out.println("from to distance "+ACO[from][to].distance);
        //System.out.println("from to pheromone "+ACO[from][to].pheromone);
      }
    }
    /*���ݸ���ѡ�����*/
    to = (int)((Math.random()*(N))%N);
    while (true) {
      /*���г���δ������*/
      /*��֤��ԶҲ�ظ�����·��*/
      if (ANTS[k].Tuba[to] == 0) {
        p = (Math.pow(ACO[from][to].pheromone, this.INFO_HERI_FACTOR_ALF) *
             Math.pow( (1.0 / (ACO[from][to].distance)),
                      this.EXCP_HERI_FACTOR_BETA)) / d;
        /*�����ѡ��ĳһ��δ�߹��ĳ���*/
        /*�����������������Ӧ��pij�Ĵ�С*/
        if (Math.random() < p) {
          break;
        }
      }
      to = (to + 1) % (N);
    }
    /*���ˣ��Ѿ�ѡ����to������ĳ��У����³�����Ϣ*/
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
    for (i = 0; i < M; i++) /*������Ŀ*/
        {
      for (city = 0; city < N; city++) {
        from = ANTS[i].Tour[city];
        to = ANTS[i].Tour[ (city + 1) % N];
        /*ʹ�ù�ʽTij=Tij+(detTij*rou)*/
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

  /*������ȷ���*/
  public void schedule() {
    int interation = 0;
    int i = 0;
    while (interation < this.MAX_INTERATION_TIME) {
      /*1.��ÿ�����϶����һ��ȫ������*/
      for (i = 0; i < M; i++) {
        while (!ANTS[i].isOver()) {
          choose_city(i);
        }
      }

        /*2.�������·������Ϣ��*/
        update_info();
      /*3.�ӷ������ڵ����Ϣ��*/
      huifa_info();
      /*4.��Tuba��ظ�*/
      HuiFu_Tuba();
      interation++;
    }
    /*4.�ӽڵ�0��ʼ������Ϣ��Ũ�Ȳ���һ��Ũ������·*/
    SUPER = new int[N];
    int JinJi[]=new int[N];
    int j = 0;
    int k = 0; /*��SUPER[go]��ֵ*/
    int t = 0; /*��SUPER���±�*/
    int max = 0;
    double max_info = -5;
    SUPER[t] = max;
    /*��ʼ�����ɱ�*/
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
    /*�����Ϊi�����Ϸ��ڱ��Ϊi�ĳ�����*/
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
    System.out.println("��Ⱥ�㷨�������ž���:" + this.min_length);

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
