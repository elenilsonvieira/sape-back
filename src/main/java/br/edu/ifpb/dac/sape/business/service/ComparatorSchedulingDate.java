package br.edu.ifpb.dac.sape.business.service;

import br.edu.ifpb.dac.sape.model.entity.Scheduling;

import java.util.Comparator;

public class ComparatorSchedulingDate implements Comparator<Scheduling> {

	@Override
	public int compare(Scheduling sched1, Scheduling sched2) {
		int result = 0;
		
		if(sched1.getScheduledDate().isBefore(sched2.getScheduledDate())) {
			result = -1;
		}else if(sched1.getScheduledDate().isAfter(sched2.getScheduledDate())) {
			result = 1;
		}
		
		return result;
	}

}
