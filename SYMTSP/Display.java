package SYMTSP;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Rectangle;
import java.awt.Font;
import javax.swing.BorderFactory;
import java.awt.Color;


public class Display
    extends JFrame implements ActionListener{

  /*��Ϣ��������*/
  public int num_of_city=9;
  Displaypanel DISPLAY_PANEL;
  JPanel CONTRAL_PANEL = new JPanel();
  JLabel ������Ŀ = new JLabel();
  JTextField CITY_NUM = new JTextField();
  JLabel ��Ϣ = new JLabel();
  JButton START = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea INFO = new JTextArea();
  JScrollPane jScrollPane2 = new JScrollPane();
  JTextArea INSTRUCTION = new JTextArea();
  JLabel ˵�� = new JLabel();
  public Display() {

      Init();
      init_display();
      display_message();
  }


  private void Init() {
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.getContentPane().setLayout(null);
    this.setSize(700,600);
    this.setTitle("TSP����");
    this.setResizable(false);

    CONTRAL_PANEL.setBorder(BorderFactory.createLineBorder(Color.black));
    CONTRAL_PANEL.setBounds(new Rectangle(11, 16, 125, 525));
    CONTRAL_PANEL.setLayout(null);
    ������Ŀ.setFont(new java.awt.Font("����", Font.BOLD, 12));
    ������Ŀ.setText("������Ŀ");
    ������Ŀ.setEnabled(true);
    ������Ŀ.setBounds(new Rectangle(14, 19, 100, 35));
    CITY_NUM.setText("");
    CITY_NUM.setBounds(new Rectangle(12, 72, 100, 35));
    ��Ϣ.setFont(new java.awt.Font("����", Font.BOLD, 12));
    ��Ϣ.setText("�Ƚ���Ϣ");
    ��Ϣ.setEnabled(true);
    ��Ϣ.setBounds(new Rectangle(14, 195, 100, 35));
    START.setBounds(new Rectangle(14, 139, 100, 36));
    START.addActionListener(this);
    START.setText("��ʼ����");
    jScrollPane1.setBounds(new Rectangle(16, 239, 96, 154));
    INFO.setText("");
    jScrollPane2.setBounds(new Rectangle(16, 452, 95, 64));
    INSTRUCTION.setText("��ɫ:�˻��㷨\n��ɫ:�Ŵ��㷨\n��ɫ:��Ⱥ�㷨");
    INSTRUCTION.setEditable(false);
    ˵��.setFont(new java.awt.Font("����", Font.BOLD, 12));
    ˵��.setText("˵��");
    ˵��.setEnabled(true);
    ˵��.setBounds(new Rectangle(12, 407, 100, 35));
    this.getContentPane().add(CONTRAL_PANEL);
    CONTRAL_PANEL.add(������Ŀ);
    CONTRAL_PANEL.add(CITY_NUM);
    CONTRAL_PANEL.add(START);
    CONTRAL_PANEL.add(��Ϣ);
    CONTRAL_PANEL.add(jScrollPane1);
    CONTRAL_PANEL.add(jScrollPane2);
    CONTRAL_PANEL.add(˵��);
    jScrollPane2.getViewport().add(INSTRUCTION);
    jScrollPane1.getViewport().add(INFO);
  }

  public void init_display()
  {
    DISPLAY_PANEL=new Displaypanel(this.num_of_city);
    DISPLAY_PANEL.setBorder(BorderFactory.createLineBorder(Color.black));
    DISPLAY_PANEL.setBounds(new Rectangle(141, 16, 549, 525));
    this.getContentPane().add(DISPLAY_PANEL);
  }

  public void display_message()
  {
    StringBuffer sb=new StringBuffer("���е���������:");
    for(int i=0;i<this.num_of_city;i++)
      sb.append("("+DISPLAY_PANEL.CITY.CITY[i][0]+","+DISPLAY_PANEL.CITY.CITY[i][1]+")\n");
    // �������ַ�����ÿ��ֻ������һ������2015.05.09
    sb.append("\n�˻��㷨��·��Ϊ:"+DISPLAY_PANEL.th.distance(DISPLAY_PANEL.th.CITY)+"\n");
    //sb.append("\n�Ŵ��㷨��·��Ϊ:"+DISPLAY_PANEL.yc.SUPER.dist+"\n");
    //sb.append("\n��Ⱥ�㷨��·��Ϊ:"+DISPLAY_PANEL.yq.min_length+"\n");
    INFO.setText(sb.toString());
  }
  public void actionPerformed(ActionEvent e)
  {
    if(e.getSource()==START)
    {
      this.num_of_city=new Integer(CITY_NUM.getText());
      init_display();
      display_message();
      DISPLAY_PANEL.repaint();
    }
  }
  public static void main(String[] args) {
    Display display = new Display();
    display.setVisible(true);
  }
}

