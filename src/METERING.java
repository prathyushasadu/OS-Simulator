import java.util.ArrayList;
/*
 * f. The Metering class is used to calculate
 *    the execution time, turn around time ,
 *    CPU shots and min ,max and avg values
 *    for each job.
 */
class METERING
{
  int JOBS_PROCESSED;
  ArrayList<ArrayList<Integer>> EXECUTION_TIMES = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> TURN_AROUND_TIMES = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> GIVEN_CODE_SIZES = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> LOADER_CODE_SIZES = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> GIVEN_INPUT_SIZES = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> LOADER_INPUT_SIZES = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> GIVEN_OUTPUT_SIZES = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> LOADER_OUTPUT_SIZES = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> CPU_SHOTS = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> IO_REQUESTS = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> NORMAL_TURN_AROUND = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> NORMAL_WAITING = new ArrayList<ArrayList<Integer>>();
  ArrayList<ArrayList<Integer>> PAGE_FAULTS = new ArrayList<ArrayList<Integer>>();
  int a;
  int b;
  int c;
  int d;
  int e;
  int f;
  int g;
  int h;
  int i;
  int j;
  int k;
  int l;
  int m;
  
  METERING()
  {
    this.JOBS_PROCESSED = 0;
  }
  
  void ADD_EXECUTION_TIMES(int JOB_ID, int TIME)
  {
    this.EXECUTION_TIMES.add(new ArrayList<Integer>());
    (this.EXECUTION_TIMES.get(this.a)).add(Integer.valueOf(JOB_ID));
    (this.EXECUTION_TIMES.get(this.a)).add(Integer.valueOf(TIME));
    this.a += 1;
  }
  
  void ADD_TURN_AROUND_TIMES(int JOB_ID, int TIME)
  {
    this.TURN_AROUND_TIMES.add(new ArrayList<Integer>());
    (this.TURN_AROUND_TIMES.get(this.b)).add(Integer.valueOf(JOB_ID));
    (this.TURN_AROUND_TIMES.get(this.b)).add(Integer.valueOf(TIME));
    this.b += 1;
  }
  
  void ADD_GIVEN_CODE_SIZES(int JOB_ID, int SIZE)
  {
    this.GIVEN_CODE_SIZES.add(new ArrayList<Integer>());
    (this.GIVEN_CODE_SIZES.get(this.c)).add(Integer.valueOf(JOB_ID));
    (this.GIVEN_CODE_SIZES.get(this.c)).add(Integer.valueOf(SIZE));
    this.c += 1;
  }
  
  void ADD_LOADER_CODE_SIZES(int JOB_ID, int SIZE)
  {
    this.LOADER_CODE_SIZES.add(new ArrayList<Integer>());
    (this.LOADER_CODE_SIZES.get(this.d)).add(Integer.valueOf(JOB_ID));
    (this.LOADER_CODE_SIZES.get(this.d)).add(Integer.valueOf(SIZE));
    this.d += 1;
  }
  
  void ADD_GIVEN_INPUT_SIZES(int JOB_ID, int SIZE)
  {
    this.GIVEN_INPUT_SIZES.add(new ArrayList<Integer>());
    (this.GIVEN_INPUT_SIZES.get(this.e)).add(Integer.valueOf(JOB_ID));
    (this.GIVEN_INPUT_SIZES.get(this.e)).add(Integer.valueOf(SIZE));
    this.e += 1;
  }
  
  void ADD_LOADER_INPUT_SIZES(int JOB_ID, int SIZE)
  {
    this.LOADER_INPUT_SIZES.add(new ArrayList<Integer>());
    (this.LOADER_INPUT_SIZES.get(this.f)).add(Integer.valueOf(JOB_ID));
    (this.LOADER_INPUT_SIZES.get(this.f)).add(Integer.valueOf(SIZE));
    this.f += 1;
  }
  
  void ADD_GIVEN_OUTPUT_SIZES(int JOB_ID, int SIZE)
  {
    this.GIVEN_OUTPUT_SIZES.add(new ArrayList<Integer>());
    (this.GIVEN_OUTPUT_SIZES.get(this.g)).add(Integer.valueOf(JOB_ID));
    (this.GIVEN_OUTPUT_SIZES.get(this.g)).add(Integer.valueOf(SIZE));
    this.g += 1;
  }
  
  void ADD_LOADER_OUTPUT_SIZES(int JOB_ID, int SIZE)
  {
    this.LOADER_OUTPUT_SIZES.add(new ArrayList<Integer>());
    (this.LOADER_OUTPUT_SIZES.get(this.h)).add(Integer.valueOf(JOB_ID));
    (this.LOADER_OUTPUT_SIZES.get(this.h)).add(Integer.valueOf(SIZE));
    this.h += 1;
  }
  
  void ADD_CPU_SHOTS(int JOB_ID, int SHOT)
  {
    this.CPU_SHOTS.add(new ArrayList<Integer>());
    (this.CPU_SHOTS.get(this.i)).add(Integer.valueOf(JOB_ID));
    (this.CPU_SHOTS.get(this.i)).add(Integer.valueOf(SHOT));
    this.i += 1;
  }
  
  void ADD_IO_REQUESTS(int JOB_ID, int REQUEST)
  {
    this.IO_REQUESTS.add(new ArrayList<Integer>());
    (this.IO_REQUESTS.get(this.j)).add(Integer.valueOf(JOB_ID));
    (this.IO_REQUESTS.get(this.j)).add(Integer.valueOf(REQUEST));
    this.j += 1;
  }
  
  void ADD_NORMAL_TURN_AROUND(int JOB_ID, int REQUEST)
  {
    this.NORMAL_TURN_AROUND.add(new ArrayList<Integer>());
    (this.NORMAL_TURN_AROUND.get(this.k)).add(Integer.valueOf(JOB_ID));
    (this.NORMAL_TURN_AROUND.get(this.k)).add(Integer.valueOf(REQUEST));
    this.k += 1;
  }
  
  void ADD_NORMAL_WAITING(int JOB_ID, int REQUEST)
  {
    this.NORMAL_WAITING.add(new ArrayList<Integer>());
    (this.NORMAL_WAITING.get(this.l)).add(Integer.valueOf(JOB_ID));
    (this.NORMAL_WAITING.get(this.l)).add(Integer.valueOf(REQUEST));
    this.l += 1;
  }
  
  void ADD_PAGE_FAULTS(int JOB_ID, int REQUEST)
  {
    this.PAGE_FAULTS.add(new ArrayList<Integer>());
    (this.PAGE_FAULTS.get(this.m)).add(Integer.valueOf(JOB_ID));
    (this.PAGE_FAULTS.get(this.m)).add(Integer.valueOf(REQUEST));
    this.m += 1;
  }
  
  void UPDATE_CPU_SHOTS(int JOB_ID)
  {
    for (int s = 0; s < this.CPU_SHOTS.size(); s++) {
      if (((Integer)((ArrayList)this.CPU_SHOTS.get(s)).get(0)).intValue() == JOB_ID)
      {
        int temp = ((Integer)((ArrayList)this.CPU_SHOTS.get(s)).get(1)).intValue();
        temp += 1;
        (this.CPU_SHOTS.get(s)).set(1, Integer.valueOf(temp));
        break;
      }
    }
  }
  
  void UPDATE_IO_REQUESTS(int JOB_ID)
  {
    for (int s = 0; s < this.CPU_SHOTS.size(); s++) {
      if (((Integer)((ArrayList)this.IO_REQUESTS.get(s)).get(0)).intValue() == JOB_ID)
      {
        int temp = ((Integer)((ArrayList)this.IO_REQUESTS.get(s)).get(1)).intValue();
        temp += 1;
        (this.IO_REQUESTS.get(s)).set(1, Integer.valueOf(temp));
        break;
      }
    }
  }
}

