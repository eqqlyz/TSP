package SYMTSP;

/*
   在遗传算法当中，交叉和变异是两种不可取的操作！
   只能进行内反转和交换！
   实际上，搜索最少花费路径的过程就是产生和评估N个城市的所有可能的排列和组合！、
   最有效的是：有序交叉算子——在一个双亲的路径中选择城市子序列创建后代。
 */
public class YiChuan {
  public int N;
  public double CITY[][];
  /*初始种群大小*/
  public int GROUP_SIZE;
  /*用数组模拟种群*/
  public member GROUP[];
  public member SUPER;

  public static final int CON=30;
  public static final int FITS=30;
  /*记录迭代代数*/
  public int GENEATION = 40;
  public static final double FanZhuanLv = 0.35;
  public static final double ShengZhiLv = 0.5;
  public static final double JiaoHuanLv = 0.37;

  /*记录一个全局个体*/
  public short Member[];

  public YiChuan(city ct) {
    N = ct.N;
    GROUP_SIZE = 8 * N;
    SUPER = new member(N);

    CITY = new double[N][];
    for (int i = 0; i < N; i++) {
      CITY[i] = new double[2];
    }

    for (int i = 0; i < N; i++) {
      CITY[i][0] = ct.CITY[i][0];
      CITY[i][1] = ct.CITY[i][1];
    }

    /*为Member申请空间*/
    Member = new short[N];

  }

  /*初始化种群*/
  public void init_Group() {
    GROUP = new member[GROUP_SIZE];
    double FITNESS = 10000;
    member temp = new member(N);
    int i = 0;
    int j=0;
    /*1.将前面N*N个个体进行初始化*/
    while (i < GROUP_SIZE) {
      GROUP[i] = new member(N);
      j=0;
      do {
        /*构造种群中的个体*/
        randomMember();                 /*产生一个随机个体*/
        GROUP[i].setMember(Member);     /*将随机个体加入种群*/
        GROUP[i].distance(CITY);        /*计算第i个个体的适应度——用距离和来衡量*/
        /*1.选择一个初始的适应度*/
        if (i == 0)
          FITNESS = GROUP[i].dist;
        /*2.根据条件判断是否跟新FITNESS*/
        j++;
        if(j>FITS)
          FITNESS=GROUP[i].dist;
      }while (GROUP[i].dist>FITNESS);
      i++;
    }
  }

  public void randomMember() {
    short temp;
    boolean flag = true;
    int i = 0;
    while (i < N) {
      /*产生0——N-1的随机数*/
      temp = (short) (Math.random() * N);
      for (int j = 0; j < i; j++) {
        if (temp == Member[j]) {
          flag = false;
          break;
        }
      }
      if (flag) {
        Member[i] = temp;
        i++;
      }
      flag = true;
    }
  }

  /*几种遗传方法*/
  public member YouXuJiaoCha(member x, member y) {
    /*遗传的原理：X,Y各将其一半的基因给Z。
     *随机生成[N/2]个数，范围[0,N-1].
     *然后，将X与这[N/2]个数对应的数放在相应的位置上，
     *将剩余的位置，按照他们在Y中出现的顺序，补到Z的空位置上。
     */
    member new_mem = new member(x.N);
    x.distance(CITY);
    y.distance(CITY);
    member min_gen=new member(N);
    double min = Math.min(x.dist, y.dist); /*记录双亲中比较优秀的个体的距离*/
    /*必须对min_gen初始化*/
    if(min==x.dist)
      min_gen.setMember(x.CITY);
    else
      min_gen.setMember(y.CITY);

    short X2Z[] = new short[x.N / 2]; /*代表随机位置*/
    boolean flag = true;
    boolean fuzhi=true;
    int i = 0;
    int count=0;
    int m;
    short temp;
    do {
      /*0.初始化new_mem中的数组*/
      for (i = 0; i < x.N; i++) {
        new_mem.CITY[i] = -1;
      }
      /*1.生成[N/2]个随机位置*/
      i = 0;
      while (i < (x.N / 2)) {
        /*产生0——N-1的随机数*/
        temp = (short) (Math.random() * x.N);

        for (int j = 0; j < i; j++) {
          if (temp == X2Z[j]) {
            flag = false;
            break;
          }
        }
        if (flag) {
          X2Z[i] = temp;
          i++;
        }
        flag = true;
      }

      /*2.将X的这[N/2]个位置上对应的数赋值给Z*/
      for (i = 0; i < (x.N / 2); i++) {
        new_mem.CITY[X2Z[i]] = x.CITY[X2Z[i]];
      }
      /*已验证——X2Z[]数组的数据正确*/

      /*3.将其余位置补为Y的值——采用直接映射的方法*/
      for (m = 0; m < y.N; m++) /*指示y的下标*/
      {
        /*判断y.CITY[m]是否已经在new_mem当中*/
        fuzhi=true;
        for(i=0;i<((x.N)/2);i++)
          if((y.CITY[m])==(x.CITY[X2Z[i]]))
          {
            fuzhi=false;
            break;
          }
        /*赋值*/
        if(fuzhi)
        {
          /*找到new_mem[]为-1的位置*/
          int zero;
          boolean fuyi=false;
          for(zero=0;zero<(new_mem.N);zero++)
            if((new_mem.CITY[zero])==-1)
            {
              fuyi=true;
              break;
            }
          /*将y.CITY[m]的值赋给new_mem.CITY[zero]*/
          if(fuyi)
            new_mem.CITY[zero]=(y.CITY[m]);
        }
      }
      new_mem.distance(CITY);
      min_gen.distance(CITY);
      if(min_gen.dist>new_mem.dist)
        min_gen.setMember(new_mem.CITY);
      count++;
    }while (new_mem.dist > min&&count<CON);

    if(count>=CON)
      new_mem.setMember(min_gen.CITY);
    return new_mem;
  }

  public member FanZhuan(member x) {
    /*随机选择两个点：start,end.
     *作为反转片段的起始和结束端，得到个体temp;
     *如果temp的distance小于父本x的distance
     *则，选择该个体，并用新个体new_mem代替x
     *否则，将该个体淘汰
     */
    member new_mem = new member(x.N);
     member min=new member(x.N);
    new_mem.setMember(x.CITY);
    x.distance(CITY);
    min.setMember(x.CITY);
    int start = 0;
    int end = 0;
    int temp;
    short t;
    int i;
    int j;
    int count = 0;
    do {
      do {
        start = (int) (Math.random() * (N));
        end = (int) (Math.random() * (N));
      }while (start == end);

      if (end < start) {
        temp = start;
        start = end;
        end = temp;
      }
      //System.out.println("start is:"+start +",end is :"+ end);
      /*开始反转*/
      i = start;
      j = end;
      while (i < j) {
        t = new_mem.CITY[i];
        new_mem.CITY[i] = new_mem.CITY[j];
        new_mem.CITY[j] = t;
        i++;
        j--;
      }
      new_mem.distance(CITY);
      min.distance(CITY);
      if(min.dist>=new_mem.dist)
        min.setMember(new_mem.CITY);
      count++;
    }while (new_mem.dist > x.dist && count < this.CON);

     if(count>=this.CON)
        new_mem.setMember(min.CITY);
    return new_mem;
  }

  public member JiaoHuan(member x) {
    /*新生个体*/
    member new_mem = new member(x.N);
    member min=new member(N);
    new_mem.setMember(x.CITY);
    min.setMember(x.CITY);
    x.distance(CITY);
    int length;
    int source; /*源基因的起始地点*/
    int dest; /*目标基因的起始地点*/
    int i;
    short t;
    int count = 0;
    /*注意，这样的置换时多个置换的叠加：因此生成的结果比较‘乱’*/
    do {
      /*1.随机选择一个片长:片长的:最大长度为log(x.N);最小长度为1*/
      do {
        length = (int) (Math.random() * (Math.log(N)));
      }
      while (length == 0);
      /*2.随机选择两个起始地方*/
      do {
        source = (int) (Math.random() * (x.N - length));
        dest = (int) (Math.random() * (x.N - length));
      }
      while ( (Math.abs(source - dest)) < length);
      /*3.交换两个片段*/
      for (i = 0; i < length; i++) {
        t = new_mem.CITY[source + i];
        new_mem.CITY[source + i] = new_mem.CITY[dest + i];
        new_mem.CITY[dest + i] = t;
      }

      new_mem.distance(CITY);
      min.distance(CITY);
      if(min.dist>new_mem.dist)   /*min记录一个全局最小的东西*/
      {
        min.setMember(new_mem.CITY);
      }

      count++;
    }while (new_mem.dist > x.dist && count < this.CON);

    if(count>=this.CON)
      new_mem.setMember(min.CITY);

    return new_mem;
  }

  public void printTheSUPER() {
    for (int j = 0; j < N; j++) {
      System.out.println("(" + CITY[SUPER.CITY[j]][0] + "," +
                         CITY[SUPER.CITY[j]][1] + ")");
    }
    System.out.println("遗传算法近似最优距离:" + SUPER.dist);

  }

  public int the_worst() {
    int nth = 0;
    double temp = -1;
    for (int i = 0; i < this.GROUP_SIZE; i++) {
      if (temp < this.GROUP[i].dist) {
        nth = i;
        temp = this.GROUP[i].dist;
      }
    }
    return nth;
  }


  public void print() {
    double temp=0;
    for (int i = 0; i < GROUP_SIZE; i++) {
      System.out.println("the " + i + "th member is" + GROUP[i].dist);
      temp+=GROUP[i].dist;
    }
    System.out.println("the average distance is "+temp/GROUP_SIZE);

  }

  /*遗传算法的关键之处*/
  public void GA_alg() {
    double best = GROUP[0].dist;
    int best_position = 0;
    int age = 0;
    double FZ; /*用来判断是否进行反转*/
    int FZ1; /*用来记录反转个体*/
    double JH; /*用来判断是否进行交换*/
    int JH1; /*用来记录交换个体*/
    double JP;
    int JP1;
    int JP2;
    int son;
    int count = 0;
    /*1.一些调度过程*/
    while (age < this.GENEATION) {
      /*1.选择个体进行反转，概率已经给出*/
      FZ = Math.random();
      count = 0;
      while (FZ < this.FanZhuanLv && count < (int)(this.GROUP_SIZE*this.FanZhuanLv)) {
        /*选取比较差的进行反转*/
        FZ1 = the_worst();
        count++;
        GROUP[FZ1].setMember(this.FanZhuan(GROUP[FZ1]).CITY);
        GROUP[FZ1].distance(CITY);
      }
      /*2.选择个体进行交换，概率已经给出*/
      JH = Math.random();
      count = 0;
      while (JH < this.JiaoHuanLv && count < (int)(this.GROUP_SIZE*this.JiaoHuanLv)) {
        count++;
        JH1 = the_worst();
        GROUP[JH1].setMember(this.JiaoHuan(GROUP[JH1]).CITY);
        GROUP[JH1].distance(CITY);
      }
      /*3.选择两个个体进行交配，概率已经给出*/
      JP = Math.random();
      count = 0;
      while (JP < this.ShengZhiLv &&count<(int)(this.GROUP_SIZE*this.ShengZhiLv)) {
        count++;
        JP1 = (int) (Math.random() * this.GROUP_SIZE);
        JP2 = (int) (Math.random() * this.GROUP_SIZE);
        son = the_worst();
        GROUP[son].setMember(YouXuJiaoCha(GROUP[JP1], GROUP[JP2]).CITY);
        GROUP[son].distance(CITY);
      }
      /*4.将不合格的个体进行淘汰*/
      age++;
    }
    /*2.得出最优个体*/
    for(int i=0;i<GROUP_SIZE;i++)
      GROUP[i].distance(CITY);
    best=GROUP[0].dist;

    for (int i = 0; i < GROUP_SIZE; i++) {
      if (GROUP[i].dist < best) {
        best = GROUP[i].dist;
        best_position = i;
      }
    }
    SUPER.setMember(GROUP[best_position].CITY);
    SUPER.distance(CITY);
    for(int q=0;q<N;q++)
      System.out.print("  "+SUPER.CITY[q]);
    System.out.println();
    System.out.println("YiChuan distance is "+SUPER.dist);
  }
}
