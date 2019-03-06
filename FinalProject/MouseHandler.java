
import java.awt.Component;
import java.awt.event.*;

public class MouseHandler implements MouseListener, MouseMotionListener{
    //JPanel mousepanel;
    //JLabel statusbar;
    int x = 0;
    int y = 0;
    int currentX = 0;
    int currentY = 0;
    boolean dragStat = false;
    boolean pressStat = false;
    public MouseHandler(Component c){
        c.addMouseListener(this);
        c.addMouseMotionListener(this);
    }
    //    public boolean mouseClicked(int mouseCode){
    //        if (mouseCode > 0 && mouseCode < 14){
    //            return mouseEvts[mouseCode];
    //        }
    //
    //        return false;
    //    }
    /*public boolean clickL(int mouseCode){
     if (mouseCode > 0){
     mouseEvts[mouseCode] = true;
     return mouseEvts[mouseCode];
     }
     mouseEvts[mouseCode] = false;
     return mouseEvts[mouseCode];
     }*/
    
    //    public void mouseClicked(MouseEvent e){
    //        if (e.getButton() > -1){
    //            mouseEvts[e.getButton()] = true;
    //        }
    //        mouseEvts[e.getButton()] = false;
    //    }
    
    public void mouseClicked(MouseEvent e){
        //statusbar.setText(String.format("Clicked at %d, %d", e.getX(), e.getY()));
        x = e.getX();
        y = e.getY();
    }
    
    public void mouseEntered(MouseEvent e){
        //statusbar.setText("Entered");
        //mousepanel.setBackground(Color.RED);
    }
    public void mouseExited(MouseEvent e){
        //statusbar.setText("Exited");
        //mousepanel.setBackground(Color.WHITE);
    }
    public void mousePressed(MouseEvent e){
        x = e.getX();
        y = e.getY();
        pressStat = true;
    }
    public void mouseReleased(MouseEvent e){
        //statusbar.setText("Released");
        pressStat = false;
    }
    public void mouseDragged(MouseEvent e){
        x = e.getX();
        y = e.getY();
        /*if(graphics2D != null)
         graphics2D.drawLine(oldX, oldY, currentX, currentY);
         repaint();*/
        //x = currentX;
        //y = currentY;
        pressStat = true;
        dragStat = true;
    }
    public void mouseMoved(MouseEvent e){
        //statusbar.setText("Moved");
        x = e.getX();
        y = e.getY();
    }
    
}