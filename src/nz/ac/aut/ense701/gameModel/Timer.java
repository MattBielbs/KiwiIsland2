/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.ense701.gameModel;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import nz.ac.aut.ense701.gui.KiwiCountUI;

/**
 *
 * @author Administrator
 */
public class Timer extends Thread
{
    private Thread xc;
    private int seconds;
    public KiwiCountUI ui;
    
    public Timer(KiwiCountUI ui)
    {
        this.seconds = 0;
        this.ui = ui;
    }
    
    @Override
    public void run() 
    {
        while (this.ui.game.getState() == GameState.PLAYING)
        {
            this.seconds++;
            this.ui.SetTimerText(this.seconds+" seconds");
            try 
            {
                sleep(980); //left 20ms for computer to run other code
            } 
            catch (InterruptedException ex) 
            {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.seconds = 0; this.ui.SetTimerText(this.seconds+" seconds");
    }   
}
