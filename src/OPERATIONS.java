/*
 *  f. The OPERATIONS class is used to performs the
 *     job instruction operations.
 */
class OPERATIONS
{
  public static ERROR_HANDLER ERROR = new ERROR_HANDLER();
  
  public static String OR(String one, String two)
  {
    String result = "";
    
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    for (int i = 0; i < one.length(); i++)
    {
      char bit_one = one.charAt(i);
      char bit_two = two.charAt(i);
      if ((bit_one == '0') && (bit_two == '0')) {
        result = result + "0";
      } else {
        result = result + "1";
      }
    }
    return result;
  }
  
  public static String AND(String one, String two)
  {
    String result = "";
    
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    for (int i = 0; i < one.length(); i++)
    {
      char bit_one = one.charAt(i);
      char bit_two = two.charAt(i);
      if ((bit_one == '1') && (bit_two == '1')) {
        result = result + "1";
      } else {
        result = result + "0";
      }
    }
    return result;
  }
  
  public static String NOT(String input)
  {
    String result = "";
    
    input = CHECK.OPERAND(input);
    for (int i = 0; i < input.length(); i++)
    {
      char bit = input.charAt(i);
      if (bit == '1') {
        result = result + "0";
      } else if (bit == '0') {
        result = result + "1";
      }
    }
    return result;
  }
  
  public static String XOR(String one, String two)
  {
    String result = "";
    
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    if (one.length() == two.length()) {
      for (int i = 0; i < one.length(); i++)
      {
        char bit_one = one.charAt(i);
        char bit_two = two.charAt(i);
        if (bit_one != bit_two) {
          result = result + "1";
        } else if (bit_one == bit_two) {
          result = result + "0";
        }
      }
    }
    return result;
  }
  
  public static String ADD(String one, String two)
  {
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    
    int int_one = CONVERSIONS.TWOs_TO_DECIMAL(one);
    int int_two = CONVERSIONS.TWOs_TO_DECIMAL(two);
    
    int sum = int_one + int_two;
    String result = CONVERSIONS.DECIMAL_TO_TWOs(sum);
    
    return result;
  }
  
  public static String SUB(String one, String two)
  {
    int MSB_limit = one.length();
    
    String temp = "";
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    
    int int_one = CONVERSIONS.TWOs_TO_DECIMAL(one);
    two = CONVERSIONS.TWOs_COMPLEMENT(two);
    int int_two = CONVERSIONS.TWOs_TO_DECIMAL(two);
    
    int sum = int_one + int_two;
    
    String result = CONVERSIONS.DECIMAL_TO_TWOs(sum);
    if (result.length() == MSB_limit + 1)
    {
      for (int i = 1; i < result.length(); i++) {
        temp = temp + result.charAt(i);
      }
      result = temp;
    }
    return result;
  }
  
  public static String MUL(String one, String two)
  {
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    
    int int_one = CONVERSIONS.TWOs_TO_DECIMAL(one);
    int int_two = CONVERSIONS.TWOs_TO_DECIMAL(two);
    
    int product = int_one * int_two;
    String result = CONVERSIONS.DECIMAL_TO_TWOs(product);
    
    return result;
  }
  
  public static String DIV(String one, String two)
  {
    String result = "0";
    
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    
    int int_one = CONVERSIONS.TWOs_TO_DECIMAL(one);
    int int_two = CONVERSIONS.TWOs_TO_DECIMAL(two);
    try
    {
      int quotient = int_one / int_two;
      result = CONVERSIONS.DECIMAL_TO_TWOs(quotient);
    }
    catch (ArithmeticException e)
    {
      ERROR.ERROR_HANDLER(8, SYSTEM.CURRENT_JOB);
    }
    return result;
  }
  
  public static String MOD(String one, String two)
  {
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    
    int int_one = CONVERSIONS.TWOs_TO_DECIMAL(one);
    int int_two = CONVERSIONS.TWOs_TO_DECIMAL(two);
    
    int mod = int_one % int_two;
    String result = CONVERSIONS.DECIMAL_TO_TWOs(mod);
    
    return result;
  }
  
  public static String SL(String input)
  {
    String output = "";
    char zero = '0';
    input = CHECK.OPERAND(input);
    for (int i = 1; i < input.length(); i++) {
      output = output + input.charAt(i);
    }
    output = output + zero;
    
    return output;
  }
  
  public static String SR(String input)
  {
    String output = "";
    input = CHECK.OPERAND(input);
    
    char msb = input.charAt(0);
    int limit_lsb = input.length() - 1;
    for (int i = 0; i < limit_lsb; i++) {
      output = output + input.charAt(i);
    }
    output = msb + output;
    
    return output;
  }
  
  public static String CPG(String one, String two)
  {
    //CHECK.INFINITE_LOOP(one, two);
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    
    int int_one = CONVERSIONS.TWOs_TO_DECIMAL(one);
    int int_two = CONVERSIONS.TWOs_TO_DECIMAL(two);
    String result;
    if (int_one > int_two) {
      result = "0";
    } else {
      result = "1";
    }
    String result1 = PADDING(result, 16);
    
    return result1;
  }
  
  public static String CPL(String one, String two)
  {
    //CHECK.INFINITE_LOOP(one, two);
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    
    int int_one = CONVERSIONS.TWOs_TO_DECIMAL(one);
    int int_two = CONVERSIONS.TWOs_TO_DECIMAL(two);
    String result;
    if (int_one < int_two) {
      result = "0";
    } else {
      result = "1";
    }
    String result1 = PADDING(result, 16);
    
    return result1;
  }
  
  public static String CPE(String one, String two)
  {
   // CHECK.INFINITE_LOOP(one, two);
    one = CHECK.OPERAND(one);
    two = CHECK.OPERAND(two);
    
    int int_one = CONVERSIONS.TWOs_TO_DECIMAL(one);
    int int_two = CONVERSIONS.TWOs_TO_DECIMAL(two);
    String result;
    if (int_one == int_two) {
      result = "0";
    } else {
      result = "1";
    }
    String result1 = PADDING(result, 16);
    
    return result1;
  }
  
  public static String PADDING(String input, int length)
  {
    String output = input;
    
    int input_length = input.length();
    if (input_length < length)
    {
      int zero_pad = length - input_length;
      for (int i = 0; i < zero_pad; i++) {
        output = "0" + output;
      }
    }
    return output;
  }
}

