package SYMTSP;

/*
   ���Ŵ��㷨���У�����ͱ��������ֲ���ȡ�Ĳ�����
   ֻ�ܽ����ڷ�ת�ͽ�����
   ʵ���ϣ��������ٻ���·���Ĺ��̾��ǲ���������N�����е����п��ܵ����к���ϣ���
   ����Ч���ǣ����򽻲����ӡ�����һ��˫�׵�·����ѡ����������д��������
 */
public class YiChuan {
  public int N;
  public double CITY[][];
  /*��ʼ��Ⱥ��С*/
  public int GROUP_SIZE;
  /*������ģ����Ⱥ*/
  public member GROUP[];
  public member SUPER;

  public static final int CON=30;
  public static final int FITS=30;
  /*��¼��������*/
  public int GENEATION = 40;
  public static final double FanZhuanLv = 0.35;
  public static final double ShengZhiLv = 0.5;
  public static final double JiaoHuanLv = 0.37;

  /*��¼һ��ȫ�ָ���*/
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

    /*ΪMember����ռ�*/
    Member = new short[N];

  }

  /*��ʼ����Ⱥ*/
  public void init_Group() {
    GROUP = new member[GROUP_SIZE];
    double FITNESS = 10000;
    member temp = new member(N);
    int i = 0;
    int j=0;
    /*1.��ǰ��N*N��������г�ʼ��*/
    while (i < GROUP_SIZE) {
      GROUP[i] = new member(N);
      j=0;
      do {
        /*������Ⱥ�еĸ���*/
        randomMember();                 /*����һ���������*/
        GROUP[i].setMember(Member);     /*��������������Ⱥ*/
        GROUP[i].distance(CITY);        /*�����i���������Ӧ�ȡ����þ����������*/
        /*1.ѡ��һ����ʼ����Ӧ��*/
        if (i == 0)
          FITNESS = GROUP[i].dist;
        /*2.���������ж��Ƿ����FITNESS*/
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
      /*����0����N-1�������*/
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

  /*�����Ŵ�����*/
  public member YouXuJiaoCha(member x, member y) {
    /*�Ŵ���ԭ��X,Y������һ��Ļ����Z��
     *�������[N/2]��������Χ[0,N-1].
     *Ȼ�󣬽�X����[N/2]������Ӧ����������Ӧ��λ���ϣ�
     *��ʣ���λ�ã�����������Y�г��ֵ�˳�򣬲���Z�Ŀ�λ���ϡ�
     */
    member new_mem = new member(x.N);
    x.distance(CITY);
    y.distance(CITY);
    member min_gen=new member(N);
    double min = Math.min(x.dist, y.dist); /*��¼˫���бȽ�����ĸ���ľ���*/
    /*�����min_gen��ʼ��*/
    if(min==x.dist)
      min_gen.setMember(x.CITY);
    else
      min_gen.setMember(y.CITY);

    short X2Z[] = new short[x.N / 2]; /*�������λ��*/
    boolean flag = true;
    boolean fuzhi=true;
    int i = 0;
    int count=0;
    int m;
    short temp;
    do {
      /*0.��ʼ��new_mem�е�����*/
      for (i = 0; i < x.N; i++) {
        new_mem.CITY[i] = -1;
      }
      /*1.����[N/2]�����λ��*/
      i = 0;
      while (i < (x.N / 2)) {
        /*����0����N-1�������*/
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

      /*2.��X����[N/2]��λ���϶�Ӧ������ֵ��Z*/
      for (i = 0; i < (x.N / 2); i++) {
        new_mem.CITY[X2Z[i]] = x.CITY[X2Z[i]];
      }
      /*����֤����X2Z[]�����������ȷ*/

      /*3.������λ�ò�ΪY��ֵ��������ֱ��ӳ��ķ���*/
      for (m = 0; m < y.N; m++) /*ָʾy���±�*/
      {
        /*�ж�y.CITY[m]�Ƿ��Ѿ���new_mem����*/
        fuzhi=true;
        for(i=0;i<((x.N)/2);i++)
          if((y.CITY[m])==(x.CITY[X2Z[i]]))
          {
            fuzhi=false;
            break;
          }
        /*��ֵ*/
        if(fuzhi)
        {
          /*�ҵ�new_mem[]Ϊ-1��λ��*/
          int zero;
          boolean fuyi=false;
          for(zero=0;zero<(new_mem.N);zero++)
            if((new_mem.CITY[zero])==-1)
            {
              fuyi=true;
              break;
            }
          /*��y.CITY[m]��ֵ����new_mem.CITY[zero]*/
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
    /*���ѡ�������㣺start,end.
     *��Ϊ��תƬ�ε���ʼ�ͽ����ˣ��õ�����temp;
     *���temp��distanceС�ڸ���x��distance
     *��ѡ��ø��壬�����¸���new_mem����x
     *���򣬽��ø�����̭
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
      /*��ʼ��ת*/
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
    /*��������*/
    member new_mem = new member(x.N);
    member min=new member(N);
    new_mem.setMember(x.CITY);
    min.setMember(x.CITY);
    x.distance(CITY);
    int length;
    int source; /*Դ�������ʼ�ص�*/
    int dest; /*Ŀ��������ʼ�ص�*/
    int i;
    short t;
    int count = 0;
    /*ע�⣬�������û�ʱ����û��ĵ��ӣ�������ɵĽ���Ƚϡ��ҡ�*/
    do {
      /*1.���ѡ��һ��Ƭ��:Ƭ����:��󳤶�Ϊlog(x.N);��С����Ϊ1*/
      do {
        length = (int) (Math.random() * (Math.log(N)));
      }
      while (length == 0);
      /*2.���ѡ��������ʼ�ط�*/
      do {
        source = (int) (Math.random() * (x.N - length));
        dest = (int) (Math.random() * (x.N - length));
      }
      while ( (Math.abs(source - dest)) < length);
      /*3.��������Ƭ��*/
      for (i = 0; i < length; i++) {
        t = new_mem.CITY[source + i];
        new_mem.CITY[source + i] = new_mem.CITY[dest + i];
        new_mem.CITY[dest + i] = t;
      }

      new_mem.distance(CITY);
      min.distance(CITY);
      if(min.dist>new_mem.dist)   /*min��¼һ��ȫ����С�Ķ���*/
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
    System.out.println("�Ŵ��㷨�������ž���:" + SUPER.dist);

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

  /*�Ŵ��㷨�Ĺؼ�֮��*/
  public void GA_alg() {
    double best = GROUP[0].dist;
    int best_position = 0;
    int age = 0;
    double FZ; /*�����ж��Ƿ���з�ת*/
    int FZ1; /*������¼��ת����*/
    double JH; /*�����ж��Ƿ���н���*/
    int JH1; /*������¼��������*/
    double JP;
    int JP1;
    int JP2;
    int son;
    int count = 0;
    /*1.һЩ���ȹ���*/
    while (age < this.GENEATION) {
      /*1.ѡ�������з�ת�������Ѿ�����*/
      FZ = Math.random();
      count = 0;
      while (FZ < this.FanZhuanLv && count < (int)(this.GROUP_SIZE*this.FanZhuanLv)) {
        /*ѡȡ�Ƚϲ�Ľ��з�ת*/
        FZ1 = the_worst();
        count++;
        GROUP[FZ1].setMember(this.FanZhuan(GROUP[FZ1]).CITY);
        GROUP[FZ1].distance(CITY);
      }
      /*2.ѡ�������н����������Ѿ�����*/
      JH = Math.random();
      count = 0;
      while (JH < this.JiaoHuanLv && count < (int)(this.GROUP_SIZE*this.JiaoHuanLv)) {
        count++;
        JH1 = the_worst();
        GROUP[JH1].setMember(this.JiaoHuan(GROUP[JH1]).CITY);
        GROUP[JH1].distance(CITY);
      }
      /*3.ѡ������������н��䣬�����Ѿ�����*/
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
      /*4.�����ϸ�ĸ��������̭*/
      age++;
    }
    /*2.�ó����Ÿ���*/
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
