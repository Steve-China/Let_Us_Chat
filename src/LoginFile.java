import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFile {
	JFrame f = new JFrame("Let Us Chat__Login");
	JLabel l1 = new JLabel("�ʺţ�");
	JLabel l2 = new JLabel("���룺");
	JButton b1 = new JButton("��¼");
	JButton b2 = new JButton("ע��");
	JTextField t1 = new JTextField(20);
	JPasswordField t2=new JPasswordField(20);
	Panel p1 = new Panel();
	Panel p2 = new Panel();
	Panel p3 = new Panel();
	
	JFrame f1 = new JFrame("Let Us Chat");
	TextArea ta = new TextArea(15,60);
	TextArea t = new TextArea(3,50);
	JButton b = new JButton("����");
	Panel p = new Panel();
	String login_user;
	
	public boolean CheckLogin (String name,String pwd)
	{
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/chat","root","root");
			String sql="select * from user where name='"+name+"' and password='"+pwd+"';";
			st = con.createStatement();
			rs = (ResultSet) st.executeQuery(sql);
			while(rs.next())
            {
				if(rs.getObject(1)!=null)
					return true;
				else
					return false;
            }
			st.close();
			con.close();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("����������ʧ�ܣ�");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean register(String name,String pwd)
	{
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/chat","root","root");
			String sql="insert into user (name,password) values('"+name+"','"+pwd+"');";
			String check="select * from user where name='"+name+"';";
			st = con.createStatement();
			st.executeUpdate(sql);
			rs = (ResultSet) st.executeQuery(check);
			while(rs.next())
            {
				if(rs.getObject(1)!=null)
					return true;
				else
					return false;
            }
			st.close();
			con.close();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("����������ʧ�ܣ�");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void readMessage(){
		ta.setText(null);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance(); // newһ��Driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat","root","root");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from log order by message_number desc");
			while(rs.next()){
				ta.setText(rs.getString("user")+"    "+rs.getString("time")+"\n"+rs.getString("message")+"\n"+ta.getText());
			}
		 }
		catch(SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		catch(InstantiationException e){
			e.printStackTrace(); 
		}
		catch(IllegalAccessException e){
			e.printStackTrace(); 
		}
		catch(ClassNotFoundException e){
			e.printStackTrace(); 
		}
		finally{
			try{
				if(conn != null){
					conn.close();
					conn = null;
				}
				if(stmt != null){
					stmt.close();
					stmt = null;
				}
				if(rs != null){
					rs.close();
					rs = null;
				}
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessage(String user,String Message)
	{
		Connection con=null;
		Statement st=null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/chat","root","root");
			String sql="insert into log (user,message) values('"+user+"','"+Message+"');";
			st = con.createStatement();
			st.executeUpdate(sql);
			st.close();
			con.close();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("����������ʧ�ܣ�");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void chatWindows(){
		ta.setEditable(false);   //�����ı���ֻ��
		ta.setBackground(new Color(255,255,255));  //���ö����ı��򱳾�ɫ
		f1.setLayout(new GridLayout());
		p.add(t);
		p.add(b);
		Box top = Box.createVerticalBox();
		top.add(ta);
		top.add(p);
		f1.add(top);
		f1.pack();
		f1.setVisible(true);
		f1.setResizable(false);    //�����С���ɱ�
		f1.setLocationRelativeTo(null);   //������Ļ����
		t.requestFocus();
		readMessage();
		
		f1.addWindowListener(new WindowAdapter(){      //���ڰ�ť�˳�
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		t.addKeyListener(new KeyAdapter(){     //����������Enter������
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER)
				{
					b.doClick();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				{
					t.setText(null);
				}
			}

			public void keyTyped(KeyEvent arg0) {
				String s = t.getText();  
			    if(s.length() >= 80)
			    	arg0.consume();
			}
		});
		
		b.addActionListener(new ActionListener(){    //����������Ͱ�ť���·���

			@Override
			public void actionPerformed(ActionEvent e) {
				if(t.getText().length()==0)
					JOptionPane.showMessageDialog(null, "���������֣�", "Warning",JOptionPane.YES_OPTION);
					//���ı�������ʾ
				else{
					sendMessage(login_user,t.getText());
					t.setText(null);
					ta.setCaretPosition(ta.getText().length());   //�����ı�����ʾ��ײ�
					t.requestFocus();
					readMessage();
				}
			}
		});
		Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                readMessage();
            }
        }, 2000, 2000);
	}
	
	public void init(){
		p1.add(l1);
		p1.add(t1);
		p2.add(l2);
		p2.add(t2);
		p3.add(b1);
		p3.add(b2);
		Box top = Box.createVerticalBox();
		top.add(p1);
		top.add(p2);
		top.add(p3);
		f.add(top);
		f.pack();
		f.setVisible(true);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		login_user=null;
		
		KeyListener ck = new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					b1.doClick();
				}
			}
			public void keyTyped(KeyEvent e){
                if(t1.getText().length() >= 20)
                    e.consume();
            }
		};
		
		t1.addKeyListener(ck);
		t2.addKeyListener(ck);
		
		f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boolean z = CheckLogin (t1.getText(),t2.getText());
				if(t1.getText() == null || t2.getText() == null)
					JOptionPane.showMessageDialog(null, "�������ʺŻ����룡", "Warning",JOptionPane.WARNING_MESSAGE);
				else{
	                if(z == true){
	                	f.dispose();
	                	login_user=t1.getText();
	                	chatWindows();
	                }
	                else
	                	JOptionPane.showMessageDialog(null, "�ʺŻ����벻��ȷ�����������룡", "Warning",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		b2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				boolean z = register (t1.getText(),t2.getText());
				if(t1.getText() == null || t2.getText() == null)
					JOptionPane.showMessageDialog(null, "�������ʺŻ����룡", "Warning",JOptionPane.WARNING_MESSAGE);
				else{
	                if(z == true)
	                	JOptionPane.showMessageDialog(null, "ע��ɹ�����ĵ�¼�ʺ��ǣ�'"+t1.getText()+"',���¼��");
	                else
	                	JOptionPane.showMessageDialog(null, "ע��ʧ�ܣ������ԣ�", "Warning",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}
	
	
	public static void  main(String[] args){
		new LoginFile().init();
	}
}