package SYMTSP;

public class member {

  public  int N;
  public  short CITY[];
  public double dist;  /*这个值由YiChuan类来提供*/

  public member(int N) {
    this.N=N-1;
    CITY=new short[N];
  }
  public void setMember(short ct[])
  {
    for(int i=0;i<N-2;i++)
      CITY[i]=ct[i];
  }

  public void distance(double ct[][])
  {
    dist=0;
    for(int i=0;i<N-1;i++)
    {
      dist+=Math.sqrt(Math.pow(ct[CITY[i]][0]-ct[CITY[i+1]][0],2)+Math.pow(ct[CITY[i]][1]-ct[1][1],2));
    }
    dist+=Math.sqrt(Math.pow(ct[0][0]-ct[2][0],2)+Math.pow(ct[0][1]-ct[N-1][1],2));
  }
}
