package com.example.one_mw.service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.one_mw.dao.ServiceBulkProcessingScheduleDao;
import com.example.one_mw.dao.ServiceBulkRequestCheckerDao;
import com.example.one_mw.dao.ServiceBulkRequestDao;
import com.example.one_mw.dao.ServiceRequestCheckerDao;
import com.example.one_mw.dao.ServiceRequestDao;
import com.example.one_mw.entity.ServiceBulkConfig;
import com.example.one_mw.entity.ServiceBulkProcessingReport;
import com.example.one_mw.entity.ServiceBulkProcessingSafe;
import com.example.one_mw.entity.ServiceBulkProcessingSchedule;
import com.example.one_mw.entity.ServiceBulkRequest;
import com.example.one_mw.entity.ServiceBulkRequestChecker;
import com.example.one_mw.entity.ServiceBulkRequestLine;
import com.example.one_mw.entity.ServiceRequest;
import com.example.one_mw.entity.ServiceRequestChecker;
import com.example.one_mw.entity.ServiceRequestLine;
import com.example.one_mw.repository.ServiceBulkConfigRepository;
import com.example.one_mw.repository.ServiceBulkProcessingReportRepository;
import com.example.one_mw.repository.ServiceBulkProcessingSafeRepository;
import com.example.one_mw.repository.ServiceBulkProcessingScheduleRepository;
import com.example.one_mw.repository.ServiceBulkRequestCheckerRepository;
import com.example.one_mw.repository.ServiceBulkRequestLineRepository;
import com.example.one_mw.repository.ServiceBulkRequestRepository;
import com.example.one_mw.repository.ServiceRequestCheckerRepository;
import com.example.one_mw.repository.ServiceRequestLineRepository;
import com.example.one_mw.repository.ServiceRequestRepository;

@Service
public class ServiceRequestService {

	private final ServiceRequestRepository serviceRequestRepository;
	
	private final ServiceRequestLineRepository serviceRequestLineRepository;
	
	private final ServiceRequestCheckerRepository serviceRequestCheckerRepository;
	
	private final ServiceRequestCheckerDao serviceRequestCheckerDao;
	
	private final ServiceRequestDao serviceRequestDao;
	
	private final ServiceBulkRequestRepository serviceBulkRequestRepository;
	
	private final ServiceBulkRequestLineRepository serviceBulkRequestLineRepository;
	
	private final ServiceBulkRequestCheckerRepository serviceBulkRequestCheckerRepository;
	
	private final ServiceBulkRequestCheckerDao serviceBulkRequestCheckerDao;
	
	private final ServiceBulkRequestDao serviceBulkRequestDao;
	
	private final ServiceBulkConfigRepository serviceBulkConfigRepository;
	
	private final ServiceBulkProcessingScheduleDao serviceBulkProcessingScheduleDao;
	
	private final ServiceBulkProcessingScheduleRepository serviceBulkProcessingScheduleRepository;
	
	private final ServiceBulkProcessingSafeRepository serviceBulkProcessingSafeRepository;
	
	private final ServiceBulkProcessingReportRepository serviceBulkProcessingReportRepository;
	
	@Autowired
	public ServiceRequestService(ServiceRequestRepository serviceRequestRepository,
			ServiceRequestLineRepository serviceRequestLineRepository,
			ServiceRequestCheckerRepository serviceRequestCheckerRepository,
			ServiceRequestCheckerDao serviceRequestCheckerDao, 
			ServiceRequestDao serviceRequestDao, 
			ServiceBulkRequestRepository serviceBulkRequestRepository, 
			ServiceBulkRequestLineRepository serviceBulkRequestLineRepository, 
			ServiceBulkRequestDao serviceBulkRequestDao, 
			ServiceBulkRequestCheckerRepository serviceBulkRequestCheckerRepository, 
			ServiceBulkRequestCheckerDao serviceBulkRequestCheckerDao, 
			ServiceBulkConfigRepository serviceBulkConfigRepository, 
			ServiceBulkProcessingScheduleRepository serviceBulkProcessingScheduleRepository, 
			ServiceBulkProcessingScheduleDao serviceBulkProcessingScheduleDao, 
			ServiceBulkProcessingSafeRepository serviceBulkProcessingSafeRepository, 
			ServiceBulkProcessingReportRepository serviceBulkProcessingReportRepository) {
		super();
		this.serviceRequestRepository = serviceRequestRepository;
		this.serviceRequestLineRepository = serviceRequestLineRepository;
		this.serviceRequestCheckerRepository = serviceRequestCheckerRepository;
		this.serviceRequestCheckerDao = serviceRequestCheckerDao;
		this.serviceRequestDao = serviceRequestDao;
		this.serviceBulkRequestRepository = serviceBulkRequestRepository;
		this.serviceBulkRequestLineRepository = serviceBulkRequestLineRepository;
		this.serviceBulkRequestCheckerRepository = serviceBulkRequestCheckerRepository;
		this.serviceBulkRequestCheckerDao = serviceBulkRequestCheckerDao;
		this.serviceBulkRequestDao = serviceBulkRequestDao;
		this.serviceBulkConfigRepository = serviceBulkConfigRepository;
		this.serviceBulkProcessingScheduleDao = serviceBulkProcessingScheduleDao;
		this.serviceBulkProcessingScheduleRepository = serviceBulkProcessingScheduleRepository;
		this.serviceBulkProcessingSafeRepository = serviceBulkProcessingSafeRepository;
		this.serviceBulkProcessingReportRepository = serviceBulkProcessingReportRepository;
	}
	
	/*
	 * Regular Service Requests Headers,Lines,Approvers & Pending Requests
	 */
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createNewServiceRequest(ServiceRequest entity) {
		
		serviceRequestRepository.save(entity);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void updateServiceRequest(ServiceRequest entity) {
		
		serviceRequestRepository.save(entity);
	}
	
	public ServiceRequest getServiceRequestById(BigInteger id) {
		
		return serviceRequestRepository.getById(id);
	}
	
	public List<ServiceRequest> findByServiceIdAndMakerAndAccountId(Integer id,String maker,String account){
		
		return serviceRequestRepository.findByServiceIdAndServiceRequestMakerAndRequestAccountId(id, maker, account);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public ServiceRequestLine createNewRequestLine(ServiceRequestLine entity) {
		
		return serviceRequestLineRepository.save(entity);
	}
	
	public List<ServiceRequestLine> getLinesByRequestId(BigInteger id){
		
		return serviceRequestLineRepository.findByRequestId(id);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createNewRequestChecker(ServiceRequestChecker entity) {
		
		serviceRequestCheckerRepository.save(entity);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void updateRequestChecker(ServiceRequestChecker entity) {
		
		serviceRequestCheckerRepository.save(entity);
	}
	
	public List<ServiceRequestChecker> getRequestCheckersByRequestId(BigInteger id){
		
		return serviceRequestCheckerRepository.findByRequestId(id);
	}
	
	public ServiceRequestChecker getNextApprover(BigInteger requestId) {
		
		List<ServiceRequestChecker> approvers=serviceRequestCheckerDao.findNextCheckers(requestId);
		
		if(approvers.size()<=0)
			return null;
		return approvers.get(0);
	}

	public Map<String,Object> getPendingApprovalRequests(String accountId,Map<String,Object> privilegesMap){
		
		List<String> privileges=new ArrayList<>();
		
		for(Map.Entry<String,Object> entry:privilegesMap.entrySet()) {
			privileges.add(entry.getValue().toString());
		}
		
		List<ServiceRequest> requests= serviceRequestDao.findPendingApprovalRequests(accountId, privileges);
		
		Map<String,Object> result = new HashMap<>();
		
		result.put("requests", requests);
		
		return result;
	}
	
	/*
	 * END Regular Service Requests Headers,Lines,Approvers & Pending Requests
	 */
	
	/*
	 * Bulk Service Requests Headers,Lines,Approvers & Pending Requests
	 */
	@Transactional(rollbackFor = { SQLException.class })
	public void createNewServiceRequest(ServiceBulkRequest entity) {
		
		serviceBulkRequestRepository.save(entity);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void updateServiceRequest(ServiceBulkRequest entity) {
		
		serviceBulkRequestRepository.save(entity);
	}
	
	public ServiceBulkRequest getServiceBulkRequestById(BigInteger id) {
		
		return serviceBulkRequestRepository.getById(id);
	}
	
	public List<ServiceBulkRequest> findBulkRequestByServiceIdAndMakerAndAccountId(Integer id,String maker,String account){
		
		return serviceBulkRequestRepository.findByServiceIdAndServiceRequestMakerAndRequestAccountId(id, maker, account);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public ServiceBulkRequestLine createNewRequestLine(ServiceBulkRequestLine entity) {
		
		return serviceBulkRequestLineRepository.save(entity);
	}
	
	public List<ServiceBulkRequestLine> getBulkLinesByRequestId(BigInteger id){
		
		return serviceBulkRequestLineRepository.findByRequestId(id);
	}
	
	public List<ServiceBulkRequestLine> getBulkLinesByRequestIdAndLineNumber(BigInteger id,Integer number){
		
		return serviceBulkRequestLineRepository.findByRequestIdAndLineNumber(id, number);
	}
	
	public List<Integer> getBulkRequestLineNumbers(BigInteger id){
		
		return serviceBulkRequestDao.getRequestLineNumbers(id);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void createNewRequestChecker(ServiceBulkRequestChecker entity) {
		
		serviceBulkRequestCheckerRepository.save(entity);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public void updateRequestChecker(ServiceBulkRequestChecker entity) {
		
		serviceBulkRequestCheckerRepository.save(entity);
	}
	
	public List<ServiceBulkRequestChecker> getBulkRequestCheckersByRequestId(BigInteger id){
		
		return serviceBulkRequestCheckerRepository.findByRequestId(id);
	}
	
	public ServiceBulkRequestChecker getBulkNextApprover(BigInteger requestId) {
		
		List<ServiceBulkRequestChecker> approvers=serviceBulkRequestCheckerDao.findNextCheckers(requestId);
		
		if(approvers.size()<=0)
			return null;
		return approvers.get(0);
	}

	public Map<String,Object> getPendingApprovalBulkRequests(String accountId,Map<String,Object> privilegesMap){
		
		List<String> privileges=new ArrayList<>();
		
		for(Map.Entry<String,Object> entry:privilegesMap.entrySet()) {
			privileges.add(entry.getValue().toString());
		}
		
		List<ServiceBulkRequest> requests= serviceBulkRequestDao.findPendingApprovalRequests(accountId, privileges);
		
		Map<String,Object> result = new HashMap<>();
		
		result.put("requests", requests);
		
		return result;
	}
	
	/*
	 * END Bulk Service Requests Headers,Lines,Approvers & Pending Requests
	 */
	
	/*
	 * Bulk Service Configuration
	 */
	
	public List<ServiceBulkConfig> getServiceBulkConfig(Integer service_id){
		
		return serviceBulkConfigRepository.findByServiceId(service_id);
	}
	
	/*
	 * END Bulk Service Configuration
	 */
	
	/*
	 * Bulk Processing Schedules
	 */
	
	public ServiceBulkProcessingSchedule getBulkProcessingScheduleByRequestId(BigInteger id) {
		
		return serviceBulkProcessingScheduleRepository.findByHeaderId(id);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public ServiceBulkProcessingSchedule createNewBulkProcessingSchedule(ServiceBulkProcessingSchedule entity) {
		
		return serviceBulkProcessingScheduleRepository.save(entity);
	}
	
	@Transactional(rollbackFor = { SQLException.class })
	public ServiceBulkProcessingSchedule updateBulkProcessingSchedule(ServiceBulkProcessingSchedule entity) {
		
		return serviceBulkProcessingScheduleRepository.save(entity);
		
	}
	
	public List<ServiceBulkProcessingSchedule> getPendingBulkProcessingSchedules(){
		
		return serviceBulkProcessingScheduleDao.findPendingBulkSchedules();
	}
	
	/*
	 * END Bulk Processing Schedules
	 */
	
	/*
	 * Bulk Processing Safe
	 */
	
	@Transactional(rollbackFor = { SQLException.class })
	public ServiceBulkProcessingSafe createNewBulkProcessingSafe(ServiceBulkProcessingSafe entity) {
		
		return serviceBulkProcessingSafeRepository.save(entity);
	}
	
	public ServiceBulkProcessingSafe getBulkProcessingSafeByScheduleId(BigInteger scheduleId) {
		
		return serviceBulkProcessingSafeRepository.findByScheduleId(scheduleId).get(0);
	}
	
	/*
	 * END Bulk Processing Safe
	 */
	
	/*
	 * Bulk Processing Report
	 */
	
	@Transactional(rollbackFor = { SQLException.class })
	public ServiceBulkProcessingReport createNewBulkProcessingReport(ServiceBulkProcessingReport entity) {
		
		return serviceBulkProcessingReportRepository.save(entity);
	}
	
	public List<ServiceBulkProcessingReport> getBulkProcessingReportByHeaderId(BigInteger id) {
		
		return serviceBulkProcessingReportRepository.findByHeaderId(id);
	}
	
	/*
	 * END Bulk Processing Report
	 */
}
