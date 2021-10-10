package com.cg.service;

import java.util.List;

import com.cg.entity.Insurance;

public interface InsuranceService {

	int calculateInsurance(Insurance insurance);

	Insurance fetchInsuranceById(int id);

	Insurance fetchInsuranceByRegistrationNo(String regNo);

	List<Insurance> getList();

}
