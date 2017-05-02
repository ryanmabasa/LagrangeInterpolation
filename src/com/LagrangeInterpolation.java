package com;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.*;

import com.sun.prism.paint.*;
import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.*;
import org.matheclipse.core.interfaces.IExpr;
import org.scilab.forge.jlatexmath.*;


public class LagrangeInterpolation implements ActionListener{

    private JPanel p1, p2, p3,p4,p5;
    private JLabel j1,j2,j3,j4;
    private JTextField tf1,tf2;
    private JButton btn1,btn2;
    private JFrame f;

    private	double x[] = new double[25];
    private double y[] = new double[25];

    private GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private int screenWidth = graphicsDevice.getDisplayMode().getWidth();
    private int screenHeight = graphicsDevice.getDisplayMode().getHeight();

    private static ExprEvaluator util = new ExprEvaluator();
    private NumberFormat formatter = new DecimalFormat("#0.00");



    public void prepareGUI(){

        home();
        results();

        p1 = new JPanel(new CardLayout());
        p1.add(p2, "home");
        p1.add(p3, "results");

        f = new JFrame();

        f.add(p1);
        f.setTitle("Lagrange Interpolation");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // f.pack();
        f.setSize(300,200);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setVisible(true);

    }


    public void home(){

        p2 = new JPanel();
        BorderLayout layout = new BorderLayout();
        layout.setHgap(5);
        layout.setVgap(5);
        p2.setLayout(layout);

        btn1 = new JButton("Submit");
        btn1.setFont(new Font("Verdana", Font.PLAIN, 15));
        btn1.addActionListener(this);

        j1 = new JLabel("x-value in f(x):", JLabel.CENTER);
        j1.setFont(new Font("Verdana", Font.PLAIN, 15));
        tf1 = new JTextField("2", 10);
        tf1.setFont(new Font("Verdana", Font.PLAIN, 15));
        tf1.setHorizontalAlignment(JTextField.CENTER);

        j2 = new JLabel("Points:", JLabel.CENTER);
        tf2 = new JTextField("(1,1) (2,2) (3,3)",20);
        tf2.setFont(new Font("Verdana", Font.PLAIN, 15));
        tf2.setHorizontalAlignment(JTextField.CENTER);

        p4 = new JPanel();
        GridBagLayout layout1 = new GridBagLayout();
        p4.setLayout(layout1);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        p4.add(j1,c);

        c.gridx = 0;
        c.gridy = 1;
        p4.add(tf1,c);

        c.gridx = 0;
        c.gridy = 2;
        p4.add(tf2,c);

        p2.add(p4,BorderLayout.CENTER);
        p2.add(btn1,BorderLayout.SOUTH);

    }


    public void results(){

        p3 = new JPanel();
        BorderLayout layout = new BorderLayout();
        layout.setHgap(10);
        layout.setVgap(10);
        p3.setLayout(layout);
        p3.setBackground(Color.WHITE);

        j3 = new JLabel("", JLabel.LEFT);
        j4 = new JLabel("", JLabel.LEFT);
        btn2 = new JButton("Back");
        btn2.setFont(new Font("Verdana", Font.PLAIN, 15));
        btn2.addActionListener(this);

        DrawGraph g = new DrawGraph(this.x,this.y);

        p5=new JPanel();
        GridBagLayout layout1 = new GridBagLayout();
        p5.setLayout(layout1);
        p5.setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        c.fill = GridBagConstraints.HORIZONTAL;

        c.weightx = 1.0;
        c.weighty = 0.8;
        c.gridx = 0;
        c.gridy = 0;
        p5.add(j3,c);

        c.weighty = 0.2;
        c.gridx = 0;
        c.gridy = 1;
        p5.add(j4,c);

        p3.add(p5,BorderLayout.NORTH);
        p3.add(g ,BorderLayout.CENTER);
        p3.add(btn2,BorderLayout.SOUTH);


    }

    public void lagrange_evaluation(String points,double x_value){

        String []pts = points.split("\\s+");

        double result = 0;

        for(int i=0; i<pts.length;i++){

            int commaLoc = pts[i].indexOf(",");
            int parentLoc = pts[i].indexOf(")");

            double a = Double.parseDouble(pts[i].substring(1,commaLoc));
            double b = Double.parseDouble(pts[i].substring(commaLoc+1,parentLoc));

            x[i] = a;
            y[i] = b;
        }

        String str = "";
		/*Lagrange Interpolation Evaluation*/
        for (int i=0; i<pts.length; i++){

            double term = y[i];

            for (int j=0;j<pts.length;j++)
            {
                if (j!=i)

                    term = term*(x_value - x[j])/(x[i] - x[j]);
                    str +=  formatter.format(term) +"*"+"("+
                    formatter.format(x_value) + "-" +
                            formatter.format(x[j]) + ")/(" +
                            formatter.format(x[i]) + "-" +
                            formatter.format(x[j]) + ") \n";

            }
            if(i==pts.length -1){

            }

            else{
                str +="+";
            }


            result += term;
        }
        JOptionPane.showMessageDialog(f,str);

        EvalEngine engine = new EvalEngine(false);
        TeXUtilities texUtil = new TeXUtilities(engine, false);

        StringWriter stw = new StringWriter();
        texUtil.toTeX( "P" +
                "[" +
                x_value +
                "]" +
                " =" + formatter.format(result),stw);
        TeXFormula formula = new TeXFormula(stw.toString());
        TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();
        icon.setInsets(new Insets(10, 5, 5, 5));

        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);
        File file = new File("f-eval.png");
        try {
            ImageIO.write(image, "png", file.getAbsoluteFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        ImageIcon imgThisImg = new ImageIcon("f-eval.png");
        j4.setIcon(imgThisImg);
        imgThisImg.getImage().flush();

    }

    public void lagrange_function(String points){

        String []pts = points.split("\\s+");
        String str = "";

        for(int i=0;i<pts.length;i++){
            if(i==pts.length-1){
                str += pts[i].replaceAll("[(]", "{").replaceAll("[)]","}");
            }

            else{
                str += pts[i].replaceAll("[(]", "{").replaceAll("[)]","}") + ",";
            }

        }

        Config.PARSER_USE_LOWERCASE_SYMBOLS = true;

        IExpr r1 = util.evaluate("InterpolatingPolynomial({" +
                str +
                "},x)");

        r1 = util.evaluate("Simplify(" +r1.toString() +")");

        EvalEngine engine = new EvalEngine(false);
        TeXUtilities texUtil = new TeXUtilities(engine, false);

        StringWriter stw = new StringWriter();
        texUtil.toTeX( "P[x] =" + r1.toString(),stw);

        TeXFormula formula = new TeXFormula(stw.toString());

        TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();

        icon.setInsets(new Insets(10, 5, 5, 5));

        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);
        File file = new File("fx.png");
        try {
            ImageIO.write(image, "png", file.getAbsoluteFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        ImageIcon imgThisImg = new ImageIcon("fx.png");
        j3.setIcon(imgThisImg);
        imgThisImg.getImage().flush();
    }


    public void actionPerformed(ActionEvent e){

        CardLayout cl = (CardLayout)(p1.getLayout());

        if(e.getSource() == btn1){
            lagrange_evaluation(tf2.getText(),Double.parseDouble(tf1.getText()));
            lagrange_function(tf2.getText());

            f.setSize(screenWidth/2,screenHeight/2);
            f.setResizable(true);
            f.setLocationRelativeTo(null);
            cl.show(p1, "results");
        }

        else if(e.getSource() == btn2){
            f.setSize(300,200);
            f.setResizable(false);
            f.setLocationRelativeTo(null);
            cl.show(p1, "home");
        }

    }

    public static void main(String []args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LagrangeInterpolation f = new LagrangeInterpolation();
                f.prepareGUI();
            }
        });
    }

}


class DrawGraph extends JPanel {

    private double []x;
    private double []y;
    private int w;
    private int h;


    public DrawGraph(double []x, double []y){
        this.x = x;
        this.y = y;
    }

    public void paint(Graphics g){

        w = getWidth();
        h = getHeight();

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        BasicStroke bs = new BasicStroke(3.0f);
        g2d.setStroke(bs);
        setBackground(Color.white);
        g2d.setPaint(Color.BLACK);
        Line2D.Double l1 = new Line2D.Double(0, h / 2, w, h / 2);
        Line2D.Double l2 = new Line2D.Double(w/2, 0, w/2, h);
        g2d.draw(l1);
        g2d.draw(l2);

        for(int i=0; i<x.length; i++){
            AffineTransform translate = new AffineTransform();
            translate.setToTranslation(w/2,h/2);

            g2d.setPaint(Color.RED);
            Ellipse2D.Double ellipse = new Ellipse2D.Double((x[i] * 20) + 3,(-y[i] * 20) - 13, 10,10);
            g2d.fill(translate.createTransformedShape(ellipse));
        }

    }
}


