package com.dengaidi.apps.cardreader.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dengaidi.apps.cardreader.entity.Employee;
import com.dengaidi.apps.cardreader.entity.EntranceRecord;
import com.dengaidi.apps.cardreader.repository.EmployeeRepository;
import com.dengaidi.apps.cardreader.repository.EntranceRecordRepository;

@Component
public class EntranceRecordService {
	
	@Autowired
	private EntranceRecordRepository entranceRecordRepository;
	
	
	public void addEntranceRecord(EntranceRecord entranceRecord) {
		entranceRecordRepository.save(entranceRecord);
	}
	
	public List<EntranceRecord> findEntranceRecordByEmployeeId(int id) {
		return entranceRecordRepository.findEntranceRecordByEmployeeId(id);
	}


	public EntranceRecordRepository getEntranceRecordRepository() {
		return entranceRecordRepository;
	}


	public void setEntranceRecordRepository(
			EntranceRecordRepository entranceRecordRepository) {
		this.entranceRecordRepository = entranceRecordRepository;
	}

	
	

}
