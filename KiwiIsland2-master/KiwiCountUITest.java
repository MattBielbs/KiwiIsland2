/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.ense701.gameModel;

import nz.ac.aut.ense701.gui.KiwiCountUI;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class KiwiCountUITest extends junit.framework.TestCase{
    private Game game;
    private KiwiCountUI UI;
    /**
     * Default constructor for test class FoodTest
     */
    public KiwiCountUITest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Override
    protected void setUp()
    {
        game = new Game();
        UI = new KiwiCountUI(game);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @Override
    protected void tearDown()
    {
        game = null;
        UI = null;
    }

    @Test
    public void testencodeHTML()
    {
        assertEquals("name",KiwiCountUI.encodeHTML("name"));
    }
    
}
