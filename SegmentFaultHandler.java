import java.io.*;
import java.util.*;

/* f. SegmentFaultHandler is used for handling
 *    the segment faults.
 *    Segment faults ofr the first RD and WR instructions.
 *    It creates the PMT for each segment
 */
public class SegmentFaultHandler {

	public static void SegmentFaultHandler_func(int x)
	{
		int npage = 0;

		MEMORY M = new MEMORY();
		int reference = 1;int dirty = 0;

		PCB pcb = SYSTEM.pcbs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));

		int position = pcb.GET_ALLOCATED_MEMORY();
		PMT SEGMENT_PMT;
		int page;
		int disk_page;
		switch (x)
		{
			case 1:
				SYSTEM.createInpPMT(SYSTEM.CURRENT_JOB);

				SEGMENT_PMT = SYSTEM.INPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
				page = pcb.GET_INPUT_PAGES(npage);
				if(page == -1)
				{
					break;
				}
				disk_page = pcb.GET_INPUT_DISK_PAGE(page);
				if (SYSTEM.MEMORY_FMBV[position] == 1)
				{
					PageFaultHandler.runReplacement(page, disk_page, 1);
				}
				else
				{
					DISK_MANAGER.LOAD_TO_MEMORY(disk_page, position);
					SEGMENT_PMT.ADD_PAGE(page, position, reference, dirty);
				}
				break;
			case 2:
				SYSTEM.createOutPMT(SYSTEM.CURRENT_JOB);
				SEGMENT_PMT = SYSTEM.OUTPUT_PMTs.get(Integer.valueOf(SYSTEM.CURRENT_JOB));
				page = pcb.GET_OUTPUT_PAGES(npage);
				disk_page = pcb.GET_OUTPUT_DISK_PAGE(page);
				if (SYSTEM.MEMORY_FMBV[position] == 1)
				{
					PageFaultHandler.runReplacement(page, disk_page, 2);
				}
				else
				{
					DISK_MANAGER.LOAD_TO_MEMORY(disk_page, position);

					SEGMENT_PMT.ADD_PAGE(page, position, reference, dirty);
				}
				break;
		}

	}
}
