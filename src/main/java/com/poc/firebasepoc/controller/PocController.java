package com.poc.firebasepoc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.poc.firebasepoc.domain.Employee;
import com.poc.firebasepoc.service.FireBaseInit;

@RestController
public class PocController {
	
	@Autowired
	FireBaseInit db;
	
	@GetMapping("/getAllEmployees")
	public List<Employee> getAllEmployees() throws InterruptedException, ExecutionException {
		List<Employee> employeeList = new ArrayList<>();
		CollectionReference employee = db.getFireBase().collection("Employee");
		ApiFuture<QuerySnapshot> querySnapshot = employee.get();
		for(DocumentSnapshot ds: querySnapshot.get().getDocuments()) {
			Employee e = ds.toObject(Employee.class);
			employeeList.add(e);
		}
		return employeeList;
	}
	
	@GetMapping("/getEmployee/{id}")
	public Employee getEmployee(@PathVariable("id") String id) throws InterruptedException, ExecutionException {
		DocumentReference reference = db.getFireBase().collection("Employee").document(id);
		ApiFuture<DocumentSnapshot> future = reference.get();
		DocumentSnapshot document = future.get();
		Employee employee = null;
		if(document.exists()) {
			employee = document.toObject(Employee.class);
		}
		
		return employee;
	}
	
	@PostMapping("/addEmployee")
	public int addEmployee(@RequestBody Employee employee) {
		CollectionReference cr = db.getFireBase().collection("Employee");
		cr.document(String.valueOf(employee.getId())).set(employee);
		return employee.getId();
	}
	
	@PutMapping("/updateEmployee/{id}")
	public Employee updateEmployee(@PathVariable("id") String id, @RequestBody Employee employee) {
		CollectionReference cr = db.getFireBase().collection("Employee");
		cr.document(String.valueOf(id)).set(employee);
		return employee;
	}
	
	@DeleteMapping("/deleteEmployee/{id}")
	public String deleteEmployee(@PathVariable("id") String id){
		ApiFuture<WriteResult> writeResult = db.getFireBase().collection("Employee").document(id).delete();
		return "Document: "+id+" has been deleted !";
	}

}
