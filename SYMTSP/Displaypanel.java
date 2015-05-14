package SYMTSP;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class Displaypanel
    extends JPanel {
  public city CITY;
  public int N;
  public TuiHuo th;
  public YiChuan yc;
  public YiQun yq;
  public static final int ZOOM = 5;

  public Displaypanel(int n) {
    N = n;
    CITY = new city(N);
    // �������ַ�����ÿ��ֻ������һ������2015.05.09
    /*�˻��㷨*/
    th = new TuiHuo(CITY);
    th.TuiHuoexecute();
    /*�Ŵ��㷨*/
   // yc = new YiChuan(CITY);
   // yc.init_Group();
   // yc.GA_alg();
    /*��Ⱥ�㷨*/
   // yq = new YiQun(CITY);
   // yq.schedule();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // �������ַ�����ÿ��ֻ������һ������2015.05.09
    // ��ʾ��������ԭ�򣬿�����ECLIPSE��JDK�汾�ϸߣ�������ʹ�õ�JDK�汾�ϵ�
    // ʹ��
    /*��ʼ��ͼ*/
    Line2D l2;
    /*����������*/
    g2.setPaint(Color.black);
    for (int i = 0; i < N; i++) {
      g2.fillOval( (int) (ZOOM * th.CITY[i][0]), (int) (ZOOM * th.CITY[i][1]),
                  5, 5);
      g2.drawString(new String(i + ""), (int) (ZOOM * th.CITY[i][0]),
                    (int) (ZOOM * th.CITY[i][1]));
    }

    /*���˻�Ľ��*/
    g2.setPaint(Color.BLUE);
    for (int i = 0; i < N - 1; i++) {
      l2 = new Line2D.Double(ZOOM * th.CITY[yq.SUPER[i]][0],
                             ZOOM * yq.CITY[yq.SUPER[i]][1],
                             ZOOM * yq.CITY[yq.SUPER[i + 1]][0],
                             ZOOM * yq.CITY[yq.SUPER[i + 1]][1]);
       g2.draw(l2);
    }
    l2 = new Line2D.Double(ZOOM * th.CITY[0][0], ZOOM * th.CITY[0][1],
                           ZOOM * th.CITY[N - 1][0],
                           ZOOM * th.CITY[N - 1][1]);
    g2.draw(l2);
    /*���Ŵ��Ľ��*/
    /*g2.setPaint(Color.green);
    for (int i = 0; i < N - 1; i++) {
      l2 = new Line2D.Double(ZOOM * yc.CITY[i][0], ZOOM * yc.CITY[i][1],
                             ZOOM * yc.CITY[i + 1][0],
                             ZOOM * yc.CITY[i + 1][1]);
      g2.draw(l2);
    }
    l2 = new Line2D.Double(ZOOM * yc.CITY[0][0], ZOOM * yc.CITY[0][1],
                           ZOOM * yc.CITY[N - 1][0],
                           ZOOM * yc.CITY[N - 1][1]);
    g2.draw(l2);
*/
    /*����Ⱥ�Ľ��*/
   /* g2.setPaint(Color.RED);
    for (int i = 0; i < N - 1; i++) {
      l2 = new Line2D.Double(ZOOM * yq.CITY[yq.SUPER[i]][0],
                             ZOOM * yq.CITY[yq.SUPER[i]][1],
                             ZOOM * yq.CITY[yq.SUPER[i + 1]][0],
                             ZOOM * yq.CITY[yq.SUPER[i + 1]][1]);
      g2.draw(l2);
    }
    l2 = new Line2D.Double(ZOOM * yq.CITY[yq.SUPER[0]][0],
                           ZOOM * yq.CITY[yq.SUPER[0]][1],
                           ZOOM * yq.CITY[yq.SUPER[N - 1]][0],
                           ZOOM * yq.CITY[yq.SUPER[N - 1]][1]);
    g2.draw(l2);*/
  }
}
