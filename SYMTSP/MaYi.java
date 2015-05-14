package SYMTSP;

public class MaYi {
  public int N;           /*记录城市的个数*/
  public int  Tuba[];    /*用下标表示城市的编号，用值表示第i个城市是否可走*/
  public int Tour[];    /*用下标表示第几次，用值表示经过的城市的编号*/
  public int   Current;   /*用来指示当前蚂蚁所在的位置——即记录Tour数组的下标*/
  public double tour_length;

  public MaYi(int N) {
    this.N=N;
    Tuba=new int[this.N];
    Tour=new int[this.N];
    Current=0;
    tour_length=0;
    for(int i=0;i<N;i++)
    {
      Tuba[i]=0;    /*初始时候，所有城市都可以走*/
      Tour[i]=-1;
    }
  }

  public void set_Tour(int path[])
  {
    for(int i=0;i<N;i++)
      Tour[i]=path[i];
  }

  public boolean isOver()
  {
    if(this.Current==N-1)
      return true;
    else
      return false;
  }
  public void Tour_length(double CITY[][])
  {
    tour_length=0;
    for(int i=0;i<Current-1;i++)
    {
      tour_length+=Math.sqrt(Math.pow(CITY[Tour[i]][0]-CITY[Tour[i+1]][0],2)
                            +Math.pow(CITY[Tour[i]][1]-CITY[Tour[i+1]][1],2));
    }
  }

  public void Total_length(double CITY[][])
{
  tour_length=0;
  for(int i=0;i<N-1;i++)
  {
    tour_length+=Math.sqrt(Math.pow(CITY[Tour[i]][0]-CITY[Tour[i+1]][0],2)
                          +Math.pow(CITY[Tour[i]][1]-CITY[Tour[i+1]][1],2));
  }
  tour_length+=Math.sqrt(Math.pow(CITY[Tour[0]][0]-CITY[Tour[N-1]][0],2)
                          +Math.pow(CITY[Tour[0]][1]-CITY[Tour[N-1]][1],2));
}

}
