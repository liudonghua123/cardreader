package com.dengaidi.apps.cardreader.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Repository;

import com.dengaidi.apps.cardreader.entity.Employee;
import com.dengaidi.apps.cardreader.entity.EntranceRecord;

@Repository
public interface EntranceRecordRepository extends JpaRepository<EntranceRecord, Integer>{

	List<EntranceRecord> findEntranceRecordByEmployeeId(int id);
	
}
