package com.sharecrest.test01;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Hello world!
 *
 */
public class App extends JFrame implements ActionListener, WindowListener
{
    private static final long serialVersionUID = 1L;

    /** メニュー文字列(ファイル)。*/
    private final String[] strMenuFileItems = {"再配置", null, "終了"};

    /** 描画用パネル。*/
    private MainPanel mainPanel;
    
    public static void main( String[] args )
    {
        System.out.println("test");
        new App();
    }

    public App() {
            super();

            // メニュー作成
            JMenu menuFile = new JMenu("ファイル");
            for(String str : strMenuFileItems) {
                    if (str == null) {
                            menuFile.addSeparator();	// セパレータ
                    } else {
                            JMenuItem item = new JMenuItem(str);
                            item.addActionListener(this);
                            menuFile.add(item);
                    }
            }
            JMenuBar menuBar = new JMenuBar();
            menuBar.add(menuFile);
            setJMenuBar(menuBar);

            setResizable(false);	// ウインドウサイズ固定
            addWindowListener(this);
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            setTitle("Test01：取り敢えずなんか書いてみる");
            mainPanel = new MainPanel(800, 600);
            setContentPane(mainPanel);
            pack();	// コンポーネントサイズにウインドウを変更
            setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
            int ret = JOptionPane.showConfirmDialog(this, "終了しますか？", "終了", JOptionPane.YES_NO_OPTION);
            if (ret == JOptionPane.YES_NO_OPTION) {
                    dispose();	// WINDOW_CLOSEDイベントが呼び出されます
            }
    }

    @Override
    public void windowClosed(WindowEvent e) {
            System.exit(0);	// アプリケーション終了
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            // addActionListener()を実行したものがgetSource()で取得できるのでそれで判定する。
            // これが正しいやり方なのか知らないけど。
            if (e.getSource().getClass() == JMenuItem.class) {
                    JMenuItem item = (JMenuItem)e.getSource();
                    if (item.getText().equals("再配置")) {
                            // 描画初期化
                            mainPanel.init();
                            this.repaint();
                    } else if (item.getText().equals("終了")) {
                            // 終了 WINDOW_CLOSINGイベントをポストします。
                            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                    }
            }
    }

    /**
     * 描画領域用のパネル。<br/>
     * @author gin
     */
    class MainPanel extends JPanel {
            private static final long serialVersionUID = 1L;

            /** 推奨サイズ。*/
            private Dimension viewSize = new Dimension();

            /**
             * コンストラクタ。<br/>
             * 推奨サイズの保存。<br/>
             * @param width 幅
             * @param height 高さ
             */
            public MainPanel(int width, int height) {
                    viewSize.setSize(width, height);
                    init();
            }

            /**
             * 親のpack()時の推奨サイズ。<br/>
             */
            @Override
            public Dimension getPreferredSize() {
                    return new Dimension(viewSize.width, viewSize.height);
            }

            /** 描画用オブジェクト。*/
            private UnitCircle[] objs = null;

            /**
             * 描画初期化。<br/>
             */
            public void init() {
                    objs = new UnitCircle[10];
                    Color[] cols = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW, Color.BLACK, Color.DARK_GRAY};
                    for(int i=0; i<objs.length; i++) {
                            objs[i] = new UnitCircle();
                            int diameter = objs[i].getDiameter();
                            objs[i].setX(Math.random() * (viewSize.width - (diameter * 2)) + diameter);
                            objs[i].setY(Math.random() * (viewSize.height - (diameter * 2)) + diameter);
                            objs[i].setCol(cols[i]);
                    }
            }

            /**
             * 描画。<br/>
             * 描画用オブジェクト(objs)の描画
             */
            @Override
            public void paint(Graphics g) {
                    Graphics2D g2d = (Graphics2D)g;

                    // テキスト情報描画
                    StringBuilder sbMsg = new StringBuilder();
                    sbMsg.append("オブジェクト数 : ").append(objs.length);
                    Font font = new Font("MS UI Gothic", Font.PLAIN, 32);
                    g2d.setColor(Color.BLUE);
                    g2d.setFont(font);
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	// テキストアンチエイリアスON
                    g2d.drawString(sbMsg.toString(), 10, 30);

                    // 描画用オブジェクト
                    for(UnitCircle obj : objs) {
                            obj.paint(g2d);
                    }

                    // ちょっと三角関数でテスト
                    g2d.setFont(new Font("MS UI Gothic", Font.PLAIN, 10));
                    double ax = 200.0;
                    double ay = 200.0;
                    double rad = Math.PI / 180.0;
                    double r = 100.0;
                    for(double a=0.0; a<360.0; a+=10.0) {
                            double x = r * Math.sin(a * rad);
                            double y = r * Math.cos(a * rad);
                            g2d.drawLine((int)ax, (int)ay, (int)(ax + x), (int)(ax + y));
                            g2d.drawString(String.format("%d", (int)a), (float)(ax + x), (float)(ax + y));
                    }

                    g2d.setColor(Color.YELLOW);
                    double x1 = r * Math.sin(300.0 * rad);
                    double y1 = r * Math.cos(300.0 * rad);
                    double x2 = r * Math.sin(60.0 * rad);
                    double y2 = r * Math.cos(60.0 * rad);
                    double x3 = r * Math.sin(180.0 * rad);
                    double y3 = r * Math.cos(180.0 * rad);
                    g2d.drawLine((int)ax, (int)ay, (int)(ax + x1), (int)(ax + y1));
                    g2d.drawLine((int)ax, (int)ay, (int)(ax + x2), (int)(ax + y2));
                    g2d.drawLine((int)ax, (int)ay, (int)(ax + x3), (int)(ax + y3));
            }
    }

    /**
     * 描画用の円キャラクタ(?)。<br/>
     * @author gin
     */
    class UnitCircle {
            /** x座標。*/
            private double x;
            /** y座標。*/
            private double y;
            /** 色。*/
            private Color col;
            /** 円直径(pix)。*/
            final private int diameter = 7;

            /**
             * コンストラクタ。<br/>
             */
            public UnitCircle() {
                    x = 0.0;
                    y = 0.0;
                    col = Color.BLACK;
            }

            /**
             * 描画。<br/>
             * @param g
             */
            public void paint(Graphics2D g) {
                    Point pos = new Point((int)x, (int)y);
                    int h = diameter / 2;
                    g.setColor(col);
                    g.fillOval(pos.x - h, pos.y - h, diameter, diameter);
            }

            /**
             * x座標取得。<br/>
             * @return x座標
             */
            public double getX() {
                    return x;
            }

            /**
             * x座標設定。<br/>
             * @param x x座標
             */
            public void setX(double x) {
                    this.x = x;
            }

            /**
             * y座標取得。<br/>
             * @return y座標
             */
            public double getY() {
                    return y;
            }

            /**
             * y座標設定。<br/>
             * @param y y座標
             */
            public void setY(double y) {
                    this.y = y;
            }

            /**
             * 色取得。<br/>
             * @return 色
             */
            public Color getCol() {
                    return col;
            }

            /**
             * 色設定。<br/>
             * @param col 色
             */
            public void setCol(Color col) {
                    this.col = col;
            }

            /**
             * 円直径(pix)。<br/>
             * @return 円直径(pix)
             */
            public int getDiameter() {
                    return diameter;
            }
    }
}
