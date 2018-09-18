/*
 * f. Stack class is mainly used for push and pop of values into stack.
 *    isEmpty , size methods were used to take check the stack is empty ans to get size of stack
 */
public class Stack
{
  public int top;
  public String value;
  public String[] Stack;
  public int weight;
  public static ERROR_HANDLER ERROR = new ERROR_HANDLER();
  
  Stack(int registers)
  {
    this.Stack = new String[registers + 1];
    this.weight = registers;
    this.top = 0;
  }
  
  void PUSH(String element)
  {
    if(element == "" || element == null)
    {
	    element = "0000000000000000";
    }
    int decimal = Integer.parseInt(element, 2);
    String hexStr = Integer.toString(decimal,16);
    int s = (short) Integer.parseInt(hexStr,16);
    INPUT_RULES(s);
    if (this.top < this.weight)
    {
      this.top += 1;
      this.Stack[this.top] = element;
    }
    else
    {
      ERROR.ERROR_HANDLER(1, SYSTEM.CURRENT_JOB);
    }
  }
  
  String PEEK()
  {
    return this.Stack[this.top];
  }
  
  String POP()
  {
    if (this.top < 1)
    {
      ERROR.ERROR_HANDLER(2, SYSTEM.CURRENT_JOB);
    }
    else
    {
      this.value = this.Stack[this.top];
      this.top -= 1;
    }
    return this.value;
  }
  
  int SIZE()
  {
    return this.top;
  }
  
  void INPUT_RULES(int number)
  {
    int HIGH_RANGE = (int)Math.pow(2.0D, 15.0D) - 1;
    int LOW_RANGE = (int)Math.pow(-2.0D, 15.0D);

    if ((number > HIGH_RANGE) || (number < LOW_RANGE)) {
       ERROR.ERROR_HANDLER(3, SYSTEM.CURRENT_JOB);
    }
  }
}

