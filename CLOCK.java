/*
 * f. The CLOCK is used for updating
 *    the clock and writing the output
 *    for regular intervals
 */
public class CLOCK
{
  public static int timer = 0;
  
  public static void INCREMENT(int unit)
  {
    SYSTEM.TIME += unit;
    
    timer += unit;
    if (timer >= 500)
    {
      OUTPUT.UTILIZATION();
      timer = 0;
    }
  }
  
  public static int GET_TIME()
  {
    return SYSTEM.TIME;
  }
}

