package com.cg.insuranceDao;

import java.util.List;

import com.cg.entity.Insurance;

public interface InsuranceDao {

	int saveInsurance(Insurance insurance);

	Insurance getInsuranceById(int id);

	Insurance getInsuranceByRegistrationNo(String regNo);

	List<Insurance> getInsurances();

}
